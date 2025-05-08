package pj.gob.pe.security.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
//import pj.gob.pe.security.model.entities.Cargo;
import pj.gob.pe.security.model.entities.Dependencia;
import pj.gob.pe.security.model.entities.TipoUser;
import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.repository.custom.UserCustomRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserCustomRepoImpl implements UserCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findUsersByFiltersV2(Map<String, Object> filters, Map<String, Object> notEqualFilters) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);

        // Hacer JOIN con Cargo
        //Join<User, Cargo> cargoJoin = user.join("cargo");

        // Hacer JOIN con Dependencia
        Join<User, Dependencia> dependenciaJoin = user.join("dependencia");

        // Hacer JOIN con Cargo
        Join<User, TipoUser> tipoUserJoin = user.join("tipoUser");

        // Lista para predicados que se combinarán con OR
        List<Predicate> orPredicates = new ArrayList<>();

        // Lista para predicados que se combinarán con AND
        List<Predicate> andPredicates = new ArrayList<>();

        // Lista para predicados que se combinarán con AND y representan desigualdades (!=)
        List<Predicate> notEqualPredicates = new ArrayList<>();

        // Agregar filtros dinámicos según los valores en el Map
        filters.forEach((key, value) -> {
            if (value != null) {

                //Filtros por Cargo
                /*
                if (key.startsWith("cargo.")) {
                    String keyC = key.replace("cargo.", "");
                    if (keyC.startsWith("or_")) {
                        // Filtros que se combinarán con OR
                        orPredicates.add(cb.equal(cargoJoin.get(keyC.replace("or_", "")), value));
                    } else {
                        // Filtros que se combinarán con AND
                        andPredicates.add(cb.equal(cargoJoin.get(keyC), value));
                    }
                    return;
                }
                */

                //Filtros por Dependencia
                if (key.startsWith("dependencia.")) {
                    String keyD = key.replace("dependencia.", "");
                    if (keyD.startsWith("or_")) {
                        // Filtros que se combinarán con OR
                        orPredicates.add(cb.equal(dependenciaJoin.get(keyD.replace("or_", "")), value));
                    } else {
                        // Filtros que se combinarán con AND
                        andPredicates.add(cb.equal(dependenciaJoin.get(keyD), value));
                    }
                    return;
                }

                //Filtros por Tipo de Usuario
                if (key.startsWith("tipo_user.")) {
                    String keyT = key.replace("tipo_user.", "");
                    if (keyT.startsWith("or_")) {
                        // Filtros que se combinarán con OR
                        orPredicates.add(cb.equal(tipoUserJoin.get(keyT.replace("or_", "")), value));
                    } else {
                        // Filtros que se combinarán con AND
                        andPredicates.add(cb.equal(tipoUserJoin.get(keyT), value));
                    }
                    return;
                }

                // Aquí decides qué filtros van con OR y cuáles con AND
                if (key.startsWith("or_")) {
                    // Filtros que se combinarán con OR
                    orPredicates.add(cb.equal(user.get(key.replace("or_", "")), value));
                } else {
                    // Filtros que se combinarán con AND
                    andPredicates.add(cb.equal(user.get(key), value));
                }
            }
        });

        // Agregar filtros dinámicos que representan desigualdades (!=)
        notEqualFilters.forEach((key, value) -> {
            if (value != null) {
                notEqualPredicates.add(cb.notEqual(user.get(key), value));
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
        query.select(user).where(finalPredicate);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Page<User> findUsersByFiltersPage(Map<String, Object> filters, Map<String, Object> notEqualFilters, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);

        // Hacer JOIN con Cargo
        //Join<User, Cargo> cargoJoin = user.join("cargo");

        // Hacer JOIN con Dependencia
        Join<User, Dependencia> dependenciaJoin = user.join("dependencia");

        // Hacer JOIN con Cargo
        Join<User, TipoUser> tipoUserJoin = user.join("tipoUser");

        // Lista para predicados que se combinarán con OR
        List<Predicate> orPredicates = new ArrayList<>();

        // Lista para predicados que se combinarán con AND
        List<Predicate> andPredicates = new ArrayList<>();

        // Lista para predicados que se combinarán con AND y representan desigualdades (!=)
        List<Predicate> notEqualPredicates = new ArrayList<>();

        // Agregar filtros dinámicos según los valores en el Map
        filters.forEach((key, value) -> {
            if (value != null) {

                //Filtros por Cargo
                /*
                if (key.startsWith("cargo.")) {
                    String keyC = key.replace("cargo.", "");
                    if (keyC.startsWith("or_")) {
                        Expression<String> field = cargoJoin.get(keyC.replace("or_", ""));
                        String pattern = "%" + value + "%"; // Búsqueda con comodines
                        // Filtros que se combinarán con OR
                        orPredicates.add(cb.like(cb.lower(field), pattern.toLowerCase()));
                    } else {
                        // Filtros que se combinarán con AND
                        andPredicates.add(cb.equal(cargoJoin.get(keyC), value));
                    }
                    return;
                }
                */

                //Filtros por Dependencia
                if (key.startsWith("dependencia.")) {
                    String keyD = key.replace("dependencia.", "");
                    if (keyD.startsWith("or_")) {
                        Expression<String> field = dependenciaJoin.get(keyD.replace("or_", ""));
                        String pattern = "%" + value + "%"; // Búsqueda con comodines
                        // Filtros que se combinarán con OR
                        orPredicates.add(cb.like(cb.lower(field), pattern.toLowerCase()));
                    } else {
                        // Filtros que se combinarán con AND
                        andPredicates.add(cb.equal(dependenciaJoin.get(keyD), value));
                    }
                    return;
                }

                //Filtros por TipoUser
                if (key.startsWith("tipo_user.")) {
                    String keyT = key.replace("tipo_user.", "");
                    if (keyT.startsWith("or_")) {
                        Expression<String> field = tipoUserJoin.get(keyT.replace("or_", ""));
                        String pattern = "%" + value + "%"; // Búsqueda con comodines
                        // Filtros que se combinarán con OR
                        orPredicates.add(cb.like(cb.lower(field), pattern.toLowerCase()));
                    } else {
                        // Filtros que se combinarán con AND
                        andPredicates.add(cb.equal(tipoUserJoin.get(keyT), value));
                    }
                    return;
                }

                // Aquí decides qué filtros van con OR y cuáles con AND
                if (key.startsWith("or_")) {
                    Expression<String> field = user.get(key.replace("or_", ""));
                    String pattern = "%" + value + "%"; // Búsqueda con comodines
                    // Filtros que se combinarán con OR
                    orPredicates.add(cb.like(cb.lower(field), pattern.toLowerCase()));
                } else {
                    // Filtros que se combinarán con AND
                    andPredicates.add(cb.equal(user.get(key), value));
                }
            }
        });

        // Agregar filtros dinámicos que representan desigualdades (!=)
        notEqualFilters.forEach((key, value) -> {
            if (value != null) {
                notEqualPredicates.add(cb.notEqual(user.get(key), value));
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
        query.select(user).where(finalPredicate);

        // Crear la consulta tipada
        TypedQuery<User> typedQuery = entityManager.createQuery(query);

        // Aplicar paginación
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        // Obtener los resultados
        List<User> resultList = typedQuery.getResultList();

        // Crear consulta para contar el total de elementos sin paginación
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);

        // Replicar los joins y predicados en la consulta de conteo
        Predicate countFinalPredicate = cb.and(
                !orPredicates.isEmpty() ? cb.or(orPredicates.stream()
                        .filter(p -> !p.getExpressions().isEmpty())
                        .map(p -> p.getExpressions().get(0))
                        .toArray(Predicate[]::new)) : cb.conjunction(),
                !andPredicates.isEmpty() ? cb.and(andPredicates.stream()
                        .filter(p -> !p.getExpressions().isEmpty())
                        .map(p -> p.getExpressions().get(0))
                        .toArray(Predicate[]::new)) : cb.conjunction(),
                !notEqualPredicates.isEmpty() ? cb.and(notEqualPredicates.stream()
                        .filter(p -> !p.getExpressions().isEmpty())
                        .map(p -> p.getExpressions().get(0))
                        .toArray(Predicate[]::new)) : cb.conjunction()
        );

        countQuery.select(cb.count(countRoot)).where(countFinalPredicate);

        //Obtener el total de registros
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        // Crear y devolver el objeto Page
        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public User findByUsername(String username){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);

        // Crear predicado WHERE username = :username
        Predicate usernamePredicate = cb.equal(user.get("username"), username);
        cq.where(usernamePredicate);

        return entityManager.createQuery(cq).getResultStream().findFirst().orElse(null);

    }
}
