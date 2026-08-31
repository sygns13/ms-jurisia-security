-- ---------------------------------------------------------------------------
-- Completa la relacion Rol - Modulo en JURISDB_USERS.RolesHasModulos
--
-- Motivo: UsersServiceAuthImpl.getUserDetails() resuelve los roles que se
-- entregan a Keycloak como la interseccion entre los roles del usuario y los
-- roles colgados de los modulos de sus aplicaciones. Un rol sin fila en
-- RolesHasModulos nunca llega a Keycloak, por lo que asignarlo a un usuario
-- no le da acceso a nada.
--
-- El backup original solo traia los roles 1 a 4 asociados. Los roles 5 a 9
-- quedaban huerfanos pese a tener su modulo homonimo creado (Roles.name
-- coincide con Modulos.codigo y los identificadores se corresponden 1 a 1).
--
-- Este script es idempotente: se puede ejecutar varias veces sin duplicar.
-- ---------------------------------------------------------------------------

INSERT INTO JURISDB_USERS.RolesHasModulos (roleId, moduloId)
SELECT r.id, m.id
FROM JURISDB_USERS.Roles r
JOIN JURISDB_USERS.Modulos m ON m.codigo = r.name
WHERE NOT EXISTS (
    SELECT 1 FROM JURISDB_USERS.RolesHasModulos rm
    WHERE rm.roleId = r.id AND rm.moduloId = m.id
);

-- Verificacion: no debe quedar ningun rol sin modulo asociado.
-- SELECT r.id, r.name
-- FROM JURISDB_USERS.Roles r
-- WHERE r.id NOT IN (SELECT roleId FROM JURISDB_USERS.RolesHasModulos);

-- Estado resultante esperado (9 filas):
--   rol 1 jurisia-usuarios                -> modulo 1 Modulo de Usuarios
--   rol 2 jurisia-expedientes             -> modulo 2 Modulo de Expediente Judicial
--   rol 3 jurisia-consulta-ia             -> modulo 3 Modulo de Consulta a la IA
--   rol 4 jurisia-metricas-doc-generados  -> modulo 4 Modulo de Metricas Doc Generados
--   rol 5 jurisia-calificar-demanda       -> modulo 5 Modulo de Calificacion de Demandas
--   rol 6 jurisia-sentenciar-demanda      -> modulo 6 Modulo de Sentencia de Demandas
--   rol 7 jurisia-gestion-instancias      -> modulo 7 Modulo de Gestion de Instancias
--   rol 8 jurisia-metricas-consulta-ia    -> modulo 8 Modulo de Metricas Consulta a la IA
--   rol 9 jurisia-metricas-chatbot        -> modulo 9 Modulo de Metricas de Chatbot
