-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: JURISDB_USERS
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Aplicacions`
--

DROP TABLE IF EXISTS `Aplicacions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Aplicacions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `realm` char(50) DEFAULT NULL,
  `codigo` char(20) DEFAULT NULL,
  `nombre` varchar(200) DEFAULT NULL,
  `sigla` char(50) DEFAULT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  `estado` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `realm_idx` (`realm`) /*!80000 INVISIBLE */,
  KEY `codigo_idx` (`codigo`) /*!80000 INVISIBLE */,
  KEY `nombre_idx` (`nombre`) /*!80000 INVISIBLE */,
  KEY `sigla_idx` (`sigla`) /*!80000 INVISIBLE */,
  KEY `estado_idx` (`estado`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Aplicacions`
--

LOCK TABLES `Aplicacions` WRITE;
/*!40000 ALTER TABLE `Aplicacions` DISABLE KEYS */;
INSERT INTO `Aplicacions` VALUES (1,'jurisia','appjurisia','APP-JURISIA','APPJURISIA','Aplicación Web de Apoyo Jurisdiccional empleando la Inteligencia Artificial Generativa',1);
/*!40000 ALTER TABLE `Aplicacions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AplicacionsHasUsers`
--

DROP TABLE IF EXISTS `AplicacionsHasUsers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AplicacionsHasUsers` (
  `aplicacionId` bigint NOT NULL,
  `userId` bigint NOT NULL,
  PRIMARY KEY (`aplicacionId`,`userId`),
  KEY `fk_aplicacions_has_users_users1_idx` (`userId`),
  KEY `fk_aplicacions_has_users_aplicacions1_idx` (`aplicacionId`),
  CONSTRAINT `fk_aplicacions_has_users_aplicacions1` FOREIGN KEY (`aplicacionId`) REFERENCES `Aplicacions` (`id`),
  CONSTRAINT `fk_aplicacions_has_users_users1` FOREIGN KEY (`userId`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AplicacionsHasUsers`
--

LOCK TABLES `AplicacionsHasUsers` WRITE;
/*!40000 ALTER TABLE `AplicacionsHasUsers` DISABLE KEYS */;
INSERT INTO `AplicacionsHasUsers` VALUES (1,1);
/*!40000 ALTER TABLE `AplicacionsHasUsers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Cargos`
--

DROP TABLE IF EXISTS `Cargos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Cargos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` char(20) DEFAULT NULL,
  `nombre` varchar(200) DEFAULT NULL,
  `sigla` char(25) DEFAULT NULL,
  `regDate` date DEFAULT NULL,
  `regDatetime` datetime DEFAULT NULL,
  `regTimestamp` bigint DEFAULT NULL,
  `regUserId` bigint DEFAULT NULL,
  `updDate` date DEFAULT NULL,
  `updDatetime` datetime DEFAULT NULL,
  `updTimestamp` bigint DEFAULT NULL,
  `updUserId` bigint DEFAULT NULL,
  `activo` tinyint DEFAULT NULL,
  `borrado` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `codigo_idx` (`codigo`) /*!80000 INVISIBLE */,
  KEY `nombre_idx` (`nombre`) /*!80000 INVISIBLE */,
  KEY `sigla_idx` (`sigla`) /*!80000 INVISIBLE */,
  KEY `regDate_idx` (`regDate`) /*!80000 INVISIBLE */,
  KEY `regDatetime_idx` (`regDatetime`) /*!80000 INVISIBLE */,
  KEY `regTimestamp_idx` (`regTimestamp`) /*!80000 INVISIBLE */,
  KEY `regUserId_idx` (`regUserId`) /*!80000 INVISIBLE */,
  KEY `updDate_idx` (`updDate`) /*!80000 INVISIBLE */,
  KEY `updDatetime_idx` (`updDatetime`) /*!80000 INVISIBLE */,
  KEY `updTimestamp_idx` (`updTimestamp`) /*!80000 INVISIBLE */,
  KEY `updUserId_idx` (`updUserId`) /*!80000 INVISIBLE */,
  KEY `activo_idx` (`activo`) /*!80000 INVISIBLE */,
  KEY `borrado_idx` (`borrado`) /*!80000 INVISIBLE */
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Cargos`
--

LOCK TABLES `Cargos` WRITE;
/*!40000 ALTER TABLE `Cargos` DISABLE KEYS */;
INSERT INTO `Cargos` VALUES (1,'cargo_1ed','cargo 1 tested','C1Ted',NULL,NULL,NULL,NULL,'2025-02-11','2025-02-11 23:43:57',1739299436,1,1,1),(2,'cargo_2ed','cargo 2 tested','C2Ted','2025-02-11','2025-02-11 23:42:55',1739299375,1,'2025-02-11','2025-02-11 23:45:57',1739299556,1,1,0);
/*!40000 ALTER TABLE `Cargos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Dependencias`
--

DROP TABLE IF EXISTS `Dependencias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Dependencias` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` char(20) DEFAULT NULL,
  `nombre` varchar(200) DEFAULT NULL,
  `sigla` char(25) DEFAULT NULL,
  `dependenciaId` bigint DEFAULT NULL,
  `regDate` date DEFAULT NULL,
  `regDatetime` datetime DEFAULT NULL,
  `regTimestamp` bigint DEFAULT NULL,
  `regUserId` bigint DEFAULT NULL,
  `updDate` date DEFAULT NULL,
  `updDatetime` datetime DEFAULT NULL,
  `updTimestamp` bigint DEFAULT NULL,
  `updUserId` bigint DEFAULT NULL,
  `activo` tinyint DEFAULT NULL,
  `borrado` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dependencias_dependencias1_idx` (`dependenciaId`),
  KEY `codigo_idx` (`codigo`) /*!80000 INVISIBLE */,
  KEY `nombre_idx` (`nombre`) /*!80000 INVISIBLE */,
  KEY `sigla_idx` (`sigla`) /*!80000 INVISIBLE */,
  KEY `regDate_idx` (`regDate`) /*!80000 INVISIBLE */,
  KEY `regDatetime_idx` (`regDatetime`) /*!80000 INVISIBLE */,
  KEY `regTimestamp_idx` (`regTimestamp`) /*!80000 INVISIBLE */,
  KEY `regUserId_idx` (`regUserId`) /*!80000 INVISIBLE */,
  KEY `updDate_idx` (`updDate`) /*!80000 INVISIBLE */,
  KEY `updDatetime_idx` (`updDatetime`),
  KEY `updTimestamp_idx` (`updTimestamp`) /*!80000 INVISIBLE */,
  KEY `updUserId_idx` (`updUserId`) /*!80000 INVISIBLE */,
  KEY `activo_idx` (`activo`) /*!80000 INVISIBLE */,
  KEY `borrado_idx` (`borrado`),
  CONSTRAINT `fk_dependencias_dependencias1` FOREIGN KEY (`dependenciaId`) REFERENCES `Dependencias` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Dependencias`
--

LOCK TABLES `Dependencias` WRITE;
/*!40000 ALTER TABLE `Dependencias` DISABLE KEYS */;
INSERT INTO `Dependencias` VALUES (1,'dependencia_1_ed','Dependencia 1 test edit','D1Ted',NULL,'2025-02-11','2025-02-12 04:37:05',1739317025,1,'2025-02-11','2025-02-12 04:39:08',1739317148,1,1,1),(2,'dependencia_1','Dependencia 1 test','D1T',NULL,'2025-02-12','2025-02-13 04:28:49',1739402929,1,NULL,NULL,NULL,NULL,1,0);
/*!40000 ALTER TABLE `Dependencias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Logins`
--

DROP TABLE IF EXISTS `Logins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Logins` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `ip` char(60) DEFAULT NULL,
  `mac` char(60) DEFAULT NULL,
  `userId` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_logins_users1_idx` (`userId`),
  KEY `fecha_idx` (`fecha`) /*!80000 INVISIBLE */,
  KEY `hora_idx` (`hora`) /*!80000 INVISIBLE */,
  KEY `ip_idx` (`ip`) /*!80000 INVISIBLE */,
  KEY `mac` (`mac`),
  CONSTRAINT `fk_logins_users1` FOREIGN KEY (`userId`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Logins`
--

LOCK TABLES `Logins` WRITE;
/*!40000 ALTER TABLE `Logins` DISABLE KEYS */;
/*!40000 ALTER TABLE `Logins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Modulos`
--

DROP TABLE IF EXISTS `Modulos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Modulos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo` char(20) DEFAULT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  `estado` tinyint DEFAULT NULL,
  `aplicacionId` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_modulo_aplicacion_idx` (`aplicacionId`),
  KEY `codigo_idx` (`codigo`) /*!80000 INVISIBLE */,
  KEY `nombre_idx` (`nombre`),
  KEY `estado_idx` (`estado`) /*!80000 INVISIBLE */,
  CONSTRAINT `fk_modulo_aplicacion` FOREIGN KEY (`aplicacionId`) REFERENCES `Aplicacions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Modulos`
--

LOCK TABLES `Modulos` WRITE;
/*!40000 ALTER TABLE `Modulos` DISABLE KEYS */;
INSERT INTO `Modulos` VALUES (1,'jurisia-usuarios','Modulo de Usuarios','Modulo de Usuarios',1,1),(2,'jurisia-expedientes','Modulo de Expediente Judicial','Modulo de Expediente Judicial',1,1),(3,'jurisia-consulta-ia','Modulo de Consulta a la IA','Modulo de Consulta a la IA',1,1),(4,'jurisia-metricas','Modulo de Metricas','Modulo de Metricas',1,1);
/*!40000 ALTER TABLE `Modulos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Roles`
--

DROP TABLE IF EXISTS `Roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` char(50) DEFAULT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `name_idx` (`name`) /*!80000 INVISIBLE */
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Roles`
--

LOCK TABLES `Roles` WRITE;
/*!40000 ALTER TABLE `Roles` DISABLE KEYS */;
INSERT INTO `Roles` VALUES (1,'jurisia-usuarios','Rol de Acceso al Módulo de Usuarios'),(2,'jurisia-expedientes','Rol de Acceso al Modulo de Expediente Judicial'),(3,'jurisia-consulta-ia','Rol de Acceso al Modulo de Consulta a la IA'),(4,'jurisia-metricas','Rol de Acceso al Modulo de Metricas');
/*!40000 ALTER TABLE `Roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RolesHasModulos`
--

DROP TABLE IF EXISTS `RolesHasModulos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RolesHasModulos` (
  `roleId` bigint NOT NULL,
  `moduloId` bigint NOT NULL,
  PRIMARY KEY (`roleId`,`moduloId`),
  KEY `fk_roles_has_modulos_modulos1_idx` (`moduloId`),
  KEY `fk_roles_has_modulos_roles1_idx` (`roleId`),
  CONSTRAINT `fk_roles_has_modulos_modulos1` FOREIGN KEY (`moduloId`) REFERENCES `Modulos` (`id`),
  CONSTRAINT `fk_roles_has_modulos_roles1` FOREIGN KEY (`roleId`) REFERENCES `Roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RolesHasModulos`
--

LOCK TABLES `RolesHasModulos` WRITE;
/*!40000 ALTER TABLE `RolesHasModulos` DISABLE KEYS */;
INSERT INTO `RolesHasModulos` VALUES (1,1),(2,2),(3,3),(4,4);
/*!40000 ALTER TABLE `RolesHasModulos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TipoUsers`
--

DROP TABLE IF EXISTS `TipoUsers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TipoUsers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  `activo` tinyint DEFAULT NULL,
  `borrado` tinyint DEFAULT NULL,
  `regDate` date DEFAULT NULL,
  `regDatetime` datetime DEFAULT NULL,
  `regTimestamp` bigint DEFAULT NULL,
  `regUserId` bigint DEFAULT NULL,
  `updDate` date DEFAULT NULL,
  `updDatetime` datetime DEFAULT NULL,
  `updTimestamp` bigint DEFAULT NULL,
  `updUserId` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `nombre_idx` (`nombre`) /*!80000 INVISIBLE */,
  KEY `activo_idx` (`activo`) /*!80000 INVISIBLE */,
  KEY `borrado_idx` (`borrado`) /*!80000 INVISIBLE */,
  KEY `regDate_idx` (`regDate`) /*!80000 INVISIBLE */,
  KEY `regDatetime_idx` (`regDatetime`) /*!80000 INVISIBLE */,
  KEY `regTimestamp_idx` (`regTimestamp`) /*!80000 INVISIBLE */,
  KEY `regUser_idx` (`regUserId`) /*!80000 INVISIBLE */,
  KEY `updDate_idx` (`updDate`),
  KEY `updDatetime_idx` (`updDatetime`),
  KEY `updTimestamp_idx` (`updTimestamp`),
  KEY `updUser_idx` (`updUserId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TipoUsers`
--

LOCK TABLES `TipoUsers` WRITE;
/*!40000 ALTER TABLE `TipoUsers` DISABLE KEYS */;
INSERT INTO `TipoUsers` VALUES (1,'Super Administrador','SA',1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,'Administrador','Admin',1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'Usuario','user',1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'Usuario de Reportes','reports',1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `TipoUsers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tipoDocumento` tinyint DEFAULT NULL COMMENT '1-> DNI\n2->RUC\n3->CE\n4->PS',
  `documento` char(20) DEFAULT NULL,
  `apellidos` char(150) DEFAULT NULL,
  `nombres` char(150) DEFAULT NULL,
  `dependenciaId` bigint NOT NULL,
  `cargoId` bigint NOT NULL,
  `username` char(250) DEFAULT NULL,
  `password` char(250) DEFAULT NULL,
  `email` char(250) DEFAULT NULL,
  `tipoUserId` bigint NOT NULL,
  `genero` tinyint DEFAULT NULL COMMENT '1->masculino\n0->femenino',
  `telefono` varchar(45) DEFAULT NULL,
  `direccion` varchar(500) DEFAULT NULL,
  `activo` tinyint DEFAULT NULL,
  `borrado` tinyint DEFAULT NULL,
  `regDate` datetime DEFAULT NULL,
  `regDatetime` datetime DEFAULT NULL,
  `regTimestamp` bigint DEFAULT NULL,
  `regUserId` bigint DEFAULT NULL,
  `updDate` date DEFAULT NULL,
  `updDatetime` datetime DEFAULT NULL,
  `updTimestamp` bigint DEFAULT NULL,
  `updUserId` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `fk_users_Dependencias1_idx` (`dependenciaId`),
  KEY `fk_users_Cargos1_idx` (`cargoId`),
  KEY `fk_users_tipoUsers1_idx` (`tipoUserId`),
  KEY `tipoDocumento_idx` (`tipoDocumento`) /*!80000 INVISIBLE */,
  KEY `documento_idx` (`documento`),
  KEY `apellidos_idx` (`apellidos`) /*!80000 INVISIBLE */,
  KEY `nombres_idx` (`nombres`) /*!80000 INVISIBLE */,
  KEY `password_idx` (`password`) /*!80000 INVISIBLE */,
  KEY `genero_idx` (`genero`) /*!80000 INVISIBLE */,
  KEY `activo_idx` (`activo`) /*!80000 INVISIBLE */,
  KEY `borrado_idx` (`borrado`) /*!80000 INVISIBLE */,
  KEY `regDate_idx` (`regDate`) /*!80000 INVISIBLE */,
  KEY `regDatetime_idx` (`regDatetime`),
  KEY `regTimestamp_idx` (`regTimestamp`) /*!80000 INVISIBLE */,
  KEY `reguserId_idx` (`regUserId`) /*!80000 INVISIBLE */,
  KEY `updDate_idx` (`updDate`) /*!80000 INVISIBLE */,
  KEY `updDatetime_idx` (`updDatetime`) /*!80000 INVISIBLE */,
  KEY `updTimestamp_idx` (`updTimestamp`) /*!80000 INVISIBLE */,
  KEY `updUserId_idx` (`updUserId`),
  CONSTRAINT `fk_users_Cargos1` FOREIGN KEY (`cargoId`) REFERENCES `Cargos` (`id`),
  CONSTRAINT `fk_users_Dependencias1` FOREIGN KEY (`dependenciaId`) REFERENCES `Dependencias` (`id`),
  CONSTRAINT `fk_users_tipoUsers1` FOREIGN KEY (`tipoUserId`) REFERENCES `TipoUsers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES (1,1,'47331640','Chavez Torres','Cristian Fernando',2,2,'user1','$2a$10$3EQsk1MMk4KmGVdKkqSIFeEyqIAf1l.OP3KLb11LYoYyc7y65WdJe','cristian_7_70@hotmail.com',1,1,'944129804','Huaraz',1,0,'2025-02-13 00:00:00','2025-02-14 01:43:56',1739479435,1,'2025-02-13','2025-02-14 01:44:47',1739479487,1);
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UsersHasRoles`
--

DROP TABLE IF EXISTS `UsersHasRoles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UsersHasRoles` (
  `userId` bigint NOT NULL,
  `roleId` bigint NOT NULL,
  `regDate` date DEFAULT NULL,
  `regDatetime` datetime DEFAULT NULL,
  `regTimestamp` bigint DEFAULT NULL,
  `regUserId` bigint DEFAULT NULL,
  PRIMARY KEY (`roleId`,`userId`),
  KEY `fk_users_has_roles_roles1_idx` (`roleId`),
  KEY `fk_users_has_roles_users1_idx` (`userId`),
  KEY `regDate_idx` (`regDate`) /*!80000 INVISIBLE */,
  KEY `regDatetime` (`regDatetime`) /*!80000 INVISIBLE */,
  KEY `regTimestamp` (`regTimestamp`) /*!80000 INVISIBLE */,
  KEY `regUserId` (`regUserId`),
  CONSTRAINT `fk_users_has_roles_roles1` FOREIGN KEY (`roleId`) REFERENCES `Roles` (`id`),
  CONSTRAINT `fk_users_has_roles_users1` FOREIGN KEY (`userId`) REFERENCES `Users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UsersHasRoles`
--

LOCK TABLES `UsersHasRoles` WRITE;
/*!40000 ALTER TABLE `UsersHasRoles` DISABLE KEYS */;
INSERT INTO `UsersHasRoles` VALUES (1,1,NULL,NULL,NULL,NULL),(1,2,NULL,NULL,NULL,NULL),(1,3,NULL,NULL,NULL,NULL),(1,4,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `UsersHasRoles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-24 22:03:53
