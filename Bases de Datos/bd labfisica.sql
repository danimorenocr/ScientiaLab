DROP DATABASE IF EXISTS labfisica ;
CREATE DATABASE IF NOT EXISTS labfisica;

use labfisica;

DROP TABLE IF EXISTS `elementos`;

CREATE TABLE IF NOT EXISTS `elementos` (
    `id_elemento` int NOT NULL AUTO_INCREMENT,
    `cod_inventario_elemento` varchar(200) DEFAULT NULL,
    `foto_elemento` mediumblob,
    `manual_elemento` tinyint DEFAULT NULL,
    `acta_elemento` varchar(45) DEFAULT NULL,
    `costo_elemento` double DEFAULT NULL,
    `fecha_compra_elemento` date DEFAULT NULL,
    `nombre_elemento` varchar(200) DEFAULT NULL,
    `marca_elemento` varchar(200) DEFAULT NULL,
    `disponibilidad_elemento` tinyint DEFAULT NULL,
    `existencias_elemento` int DEFAULT NULL,
    PRIMARY KEY (`id_elemento`)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET = utf8mb3;

DROP TABLE IF EXISTS accesorios;

CREATE TABLE IF NOT EXISTS accesorios (
    `id_accesorio` int NOT NULL AUTO_INCREMENT,
    `cod_inventario_accesorio` varchar(200) DEFAULT NULL,
    `nombre_accesorio` varchar(100) DEFAULT NULL,
    `referencia_accesorio` varchar(100) DEFAULT NULL,
    `cantidad_accesorio` int DEFAULT NULL,
    `id_elemento` int DEFAULT NULL,
    PRIMARY KEY (`id_accesorio`),
    KEY `fk_Accesorios_Elementos` (`id_elemento`)
) ENGINE = InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET = utf8mb3;

DROP TABLE IF EXISTS `equipos`;
CREATE TABLE IF NOT EXISTS `equipos` (
  `id_equipo` int NOT NULL AUTO_INCREMENT,
  `cod_inventario_equipo` varchar(100) DEFAULT NULL,
  `modelo_equipo` varchar(100) DEFAULT NULL,
  `serie_equipo` varchar(100) DEFAULT NULL,
  `mantenimiento_equipo` enum('C','P','CL') DEFAULT NULL,
  `req_mante_equipo` varchar(100) DEFAULT NULL,
  `estado_equipo` enum('A','I','DB') DEFAULT NULL,
  `nombre_equipo` varchar(200) DEFAULT NULL,
  `marca_equipo` varchar(200) DEFAULT NULL,
  `disponibilidad_equipo` tinyint DEFAULT NULL,
  `existencias_equipo` int DEFAULT NULL,
  PRIMARY KEY (`id_equipo`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;


DROP TABLE IF EXISTS `consumibles`;
CREATE TABLE IF NOT EXISTS `consumibles` (
  `id_consumible` int NOT NULL AUTO_INCREMENT,
  `referencia_consumible` varchar(100) DEFAULT NULL,
  `observaciones_consumible` varchar(100) DEFAULT NULL,
  `num_gabinete_consumible` int DEFAULT NULL,
  `nombre_consumible` varchar(200) DEFAULT NULL,
  `marca_consumible` varchar(200) DEFAULT NULL,
  `disponibilidad_consumible` tinyint DEFAULT NULL,
  `existencias_consumible` int DEFAULT NULL,
  `foto_consumible` mediumblob,
  PRIMARY KEY (`id_consumible`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;


DROP TABLE IF EXISTS `grupos_laboratorios`;
CREATE TABLE IF NOT EXISTS `grupos_laboratorios` (
  `id_grupo_laboratorio` int NOT NULL AUTO_INCREMENT,
  `nombre_laboratorio` varchar(45) DEFAULT NULL,
  `desc_laboratorio` tinytext,
  PRIMARY KEY (`id_grupo_laboratorio`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `grupos`;
CREATE TABLE IF NOT EXISTS `grupos` (
  `id_grupo` int NOT NULL AUTO_INCREMENT,
  `id_grupo_laboratorio` int DEFAULT NULL,
  PRIMARY KEY (`id_grupo`),
  KEY `fk_Grupo_GrupoLaboratorio1` (`id_grupo_laboratorio`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;


DROP TABLE IF EXISTS `estudiantes`;
CREATE TABLE IF NOT EXISTS `estudiantes` (
  `id_estudiante` int NOT NULL AUTO_INCREMENT,
  `codU_estudiante` varchar(6) DEFAULT NULL,
  `nom_estudiante` varchar(100) DEFAULT NULL,
  `apellido_estudiante` varchar(100) DEFAULT NULL,
  `correo_estudiante` varchar(100) DEFAULT NULL,
  `id_grupo` int DEFAULT NULL,
  PRIMARY KEY (`id_estudiante`),
  KEY `fk_Estudiantes_Grupo1` (`id_grupo`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;



DROP TABLE IF EXISTS `prestamos`;
CREATE TABLE IF NOT EXISTS `prestamos` (
  `id_prestamo` int NOT NULL AUTO_INCREMENT,
  `fecha_prestamo` date DEFAULT NULL,
  `hora_prestamo` time DEFAULT NULL,
  `id_grupo` int DEFAULT NULL,
  PRIMARY KEY (`id_prestamo`),
  KEY `fk_Prestamos_Grupo1` (`id_grupo`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;


DROP TABLE IF EXISTS `prestamos_items`;
CREATE TABLE IF NOT EXISTS `prestamos_items` (
  `id_prestamo_item` int NOT NULL AUTO_INCREMENT,
  `id_prestamo` int DEFAULT NULL,
  `id_elemento` int DEFAULT NULL,
  `id_equipo` int DEFAULT NULL,
  `id_consumible` int DEFAULT NULL,
  PRIMARY KEY (`id_prestamo_item`),
  KEY `fk_prestamoItem_Prestamos1` (`id_prestamo`),
  KEY `fk_prestamoItem_Elementos1` (`id_elemento`),
  KEY `fk_prestamoItem_Equipos1` (`id_equipo`),
  KEY `fk_prestamoItem_Consumibles1` (`id_consumible`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;