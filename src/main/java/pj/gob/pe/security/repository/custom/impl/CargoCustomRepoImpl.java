package pj.gob.pe.security.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.model.entities.Cargo;
import pj.gob.pe.security.repository.custom.CargoCustomRepo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CargoCustomRepoImpl implements CargoCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Cargo> findCargosNoBorrados() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cargo> query = cb.createQuery(Cargo.class);
        Root<Cargo> cargo = query.from(Cargo.class);

        // WHERE borrado = 0
        query.select(cargo).where(cb.equal(cargo.get("borrado"), 0));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Cargo> findCargosByFilters(Map<String, Object> filters){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cargo> query = cb.createQuery(Cargo.class);
        Root<Cargo> cargo = query.from(Cargo.class);

        List<Predicate> predicates = new ArrayList<>();

        // Agregar filtros dinámicos según los valores en el Map
        filters.forEach((key, value) -> {
            if (value != null) {
                predicates.add(cb.equal(cargo.get(key), value));
            }
        });

        // WHERE borrado = 0 (para excluir registros eliminados lógicamente)
        //predicates.add(cb.equal(cargo.get("borrado"), 0));

        query.select(cargo).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Cargo> findCargosByFiltersV2(Map<String, Object> filters, Map<String, Object> notEqualFilters) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cargo> query = cb.createQuery(Cargo.class);
        Root<Cargo> cargo = query.from(Cargo.class);

        // Lista para predicados que se combinarán con OR
        List<Predicate> orPredicates = new ArrayList<>();

        // Lista para predicados que se combinarán con AND
        List<Predicate> andPredicates = new ArrayList<>();

        // Lista para predicados que se combinarán con AND y representan desigualdades (!=)
        List<Predicate> notEqualPredicates = new ArrayList<>();

        // Agregar filtros dinámicos según los valores en el Map
        filters.forEach((key, value) -> {
            if (value != null) {
                // Aquí decides qué filtros van con OR y cuáles con AND
                if (key.startsWith("or_")) {
                    // Filtros que se combinarán con OR
                    orPredicates.add(cb.equal(cargo.get(key.replace("or_", "")), value));
                } else {
                    // Filtros que se combinarán con AND
                    andPredicates.add(cb.equal(cargo.get(key), value));
                }
            }
        });

        // Agregar filtros dinámicos que representan desigualdades (!=)
        notEqualFilters.forEach((key, value) -> {
            if (value != null) {
                notEqualPredicates.add(cb.notEqual(cargo.get(key), value));
            }
        });

        // Combinar los predicados OR en un solo predicado
        Predicate orPredicate = orPredicates.isEmpty() ? null : cb.or(orPredicates.toArray(new Predicate[0]));

        // Combinar los predicados AND en un solo predicado
        Predicate andPredicate = andPredicates.isEmpty() ? null : cb.and(andPredicates.toArray(new Predicate[0]));

        // Combinar los predicados NOT EQUAL en un solo predicado
        Predicate notEqualPredicate = notEqualPredicates.isEmpty() ? null : cb.and(notEqualPredicates.toArray(new Predicate[0]));

        // Combinar todos los predicados en un predicado final
        Predicate finalPredicate = cb.and(
                orPredicate != null ? orPredicate : cb.conjunction(), // Si no hay OR, usar conjunción (true)
                andPredicate != null ? andPredicate : cb.conjunction(), // Si no hay AND, usar conjunción (true)
                notEqualPredicate != null ? notEqualPredicate : cb.conjunction() // Si no hay NOT EQUAL, usar conjunción (true)
        );

        // Aplicar el predicado final a la consulta
        query.select(cargo).where(finalPredicate);
        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Construye el predicado de filtrado sobre el Root recibido. Se invoca una vez para la
     * consulta de datos y otra para la de conteo: cada CriteriaQuery necesita predicados
     * construidos sobre su propio Root, no se pueden reutilizar entre consultas.
     */
    private Predicate construirPredicadoPage(CriteriaBuilder cb, Root<Cargo> cargo,
                                             Map<String, Object> filters,
                                             Map<String, Object> notEqualFilters) {

        // Lista para predicados que se combinarán con OR
        List<Predicate> orPredicates = new ArrayList<>();

        // Lista para predicados que se combinarán con AND
        List<Predicate> andPredicates = new ArrayList<>();

        // Lista para predicados que se combinarán con AND y representan desigualdades (!=)
        List<Predicate> notEqualPredicates = new ArrayList<>();

        // Agregar filtros dinámicos según los valores en el Map
        filters.forEach((key, value) -> {
            if (value != null) {

                // Aquí decides qué filtros van con OR y cuáles con AND
                if (key.startsWith("or_")) {
                    Expression<String> field = cargo.get(key.replace("or_", ""));
                    String pattern = "%" + value + "%"; // Búsqueda con comodines
                    // Filtros que se combinarán con OR
                    orPredicates.add(cb.like(cb.lower(field), pattern.toLowerCase()));
                } else {
                    // Filtros que se combinarán con AND
                    andPredicates.add(cb.equal(cargo.get(key), value));
                }
            }
        });

        // Agregar filtros dinámicos que representan desigualdades (!=)
        notEqualFilters.forEach((key, value) -> {
            if (value != null) {
                notEqualPredicates.add(cb.notEqual(cargo.get(key), value));
            }
        });

        Predicate orPredicate = orPredicates.isEmpty() ? null : cb.or(orPredicates.toArray(new Predicate[0]));
        Predicate andPredicate = andPredicates.isEmpty() ? null : cb.and(andPredicates.toArray(new Predicate[0]));
        Predicate notEqualPredicate = notEqualPredicates.isEmpty() ? null : cb.and(notEqualPredicates.toArray(new Predicate[0]));

        return cb.and(
                orPredicate != null ? orPredicate : cb.conjunction(),
                andPredicate != null ? andPredicate : cb.conjunction(),
                notEqualPredicate != null ? notEqualPredicate : cb.conjunction()
        );
    }

    @Override
    public Page<Cargo> findCargosByFiltersPage(Map<String, Object> filters, Map<String, Object> notEqualFilters, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Consulta de datos
        CriteriaQuery<Cargo> query = cb.createQuery(Cargo.class);
        Root<Cargo> cargo = query.from(Cargo.class);
        query.select(cargo).where(construirPredicadoPage(cb, cargo, filters, notEqualFilters));

        if (pageable.getSort().isSorted()) {
            List<Order> ordenes = new ArrayList<>();
            pageable.getSort().forEach(orden -> ordenes.add(
                    orden.isAscending() ? cb.asc(cargo.get(orden.getProperty()))
                                        : cb.desc(cargo.get(orden.getProperty()))));
            query.orderBy(ordenes);
        }

        TypedQuery<Cargo> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Cargo> resultList = typedQuery.getResultList();

        // Consulta de conteo: Root propio y predicados reconstruidos sobre él
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Cargo> countRoot = countQuery.from(Cargo.class);
        countQuery.select(cb.count(countRoot))
                  .where(construirPredicadoPage(cb, countRoot, filters, notEqualFilters));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        // Crear y devolver el objeto Page
        return new PageImpl<>(resultList, pageable, total);
    }
}
