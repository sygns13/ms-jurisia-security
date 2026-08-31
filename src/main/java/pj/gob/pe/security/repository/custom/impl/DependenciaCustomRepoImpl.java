package pj.gob.pe.security.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.model.entities.Dependencia;
import pj.gob.pe.security.model.entities.Dependencia;
import pj.gob.pe.security.repository.custom.DependenciaCustomRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class DependenciaCustomRepoImpl implements DependenciaCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Dependencia> findDependenciasByFiltersV2(Map<String, Object> filters, Map<String, Object> notEqualFilters) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Dependencia> query = cb.createQuery(Dependencia.class);
        Root<Dependencia> dependencia = query.from(Dependencia.class);

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
                    orPredicates.add(cb.equal(dependencia.get(key.replace("or_", "")), value));
                } else {
                    // Filtros que se combinarán con AND
                    andPredicates.add(cb.equal(dependencia.get(key), value));
                }
            }
        });

        // Agregar filtros dinámicos que representan desigualdades (!=)
        notEqualFilters.forEach((key, value) -> {
            if (value != null) {
                notEqualPredicates.add(cb.notEqual(dependencia.get(key), value));
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
        query.select(dependencia).where(finalPredicate);
        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Construye el predicado de filtrado sobre el Root recibido. Se invoca una vez para la
     * consulta de datos y otra para la de conteo: cada CriteriaQuery necesita predicados
     * construidos sobre su propio Root, no se pueden reutilizar entre consultas.
     */
    private Predicate construirPredicadoPage(CriteriaBuilder cb, Root<Dependencia> dependencia,
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
                    Expression<String> field = dependencia.get(key.replace("or_", ""));
                    String pattern = "%" + value + "%"; // Búsqueda con comodines
                    // Filtros que se combinarán con OR
                    orPredicates.add(cb.like(cb.lower(field), pattern.toLowerCase()));
                } else {
                    // Filtros que se combinarán con AND
                    andPredicates.add(cb.equal(dependencia.get(key), value));
                }
            }
        });

        // Agregar filtros dinámicos que representan desigualdades (!=)
        notEqualFilters.forEach((key, value) -> {
            if (value != null) {
                notEqualPredicates.add(cb.notEqual(dependencia.get(key), value));
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
    public Page<Dependencia> findDependenciasByFiltersPage(Map<String, Object> filters, Map<String, Object> notEqualFilters, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Consulta de datos
        CriteriaQuery<Dependencia> query = cb.createQuery(Dependencia.class);
        Root<Dependencia> dependencia = query.from(Dependencia.class);
        query.select(dependencia).where(construirPredicadoPage(cb, dependencia, filters, notEqualFilters));

        if (pageable.getSort().isSorted()) {
            List<Order> ordenes = new ArrayList<>();
            pageable.getSort().forEach(orden -> ordenes.add(
                    orden.isAscending() ? cb.asc(dependencia.get(orden.getProperty()))
                                        : cb.desc(dependencia.get(orden.getProperty()))));
            query.orderBy(ordenes);
        }

        TypedQuery<Dependencia> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Dependencia> resultList = typedQuery.getResultList();

        // Consulta de conteo: Root propio y predicados reconstruidos sobre él
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Dependencia> countRoot = countQuery.from(Dependencia.class);
        countQuery.select(cb.count(countRoot))
                  .where(construirPredicadoPage(cb, countRoot, filters, notEqualFilters));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        // Crear y devolver el objeto Page
        return new PageImpl<>(resultList, pageable, total);
    }
}
