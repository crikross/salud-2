-- Crear base de datos
CREATE DATABASE IF NOT EXISTS salud;
USE salud;
-- login usuario
CREATE TABLE tbl_usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    edad INT NOT NULL,
    nivel_academico ENUM('Universitario', 'Técnico'), 
    fecha_inscripcion DATE
);


-- Crear tabla para signos vitales
CREATE TABLE IF NOT EXISTS SignosVitales (
    usuarioId INT(11) AUTO_INCREMENT PRIMARY KEY,
    pulsaciones INT(10),
    presionArterial INT(10),
    fechaRegistro DATE
);

-- Crear tabla para actividad física
CREATE TABLE IF NOT EXISTS ActividadFisica (
    usuarioId INT AUTO_INCREMENT PRIMARY KEY,
    pasos INT(100),
    distancia FLOAT(10),
    caloriasQuemadas FLOAT(10),
    fechaRegistro DATE
);

-- crear tabla para actividad estres

CREATE TABLE IF NOT EXISTS Estres (
    usuarioId INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    edad INT,
    nivelEstres ENUM('Bajo', 'Medio','Alto'),
    motivo VARCHAR(255),
    fechaRegistro DATE
);

-- Crear tabla para mediciones
CREATE TABLE Medicion (
    usuarioId INT (11) AUTO_INCREMENT PRIMARY KEY,
    pulsaciones INT(10) NOT NULL,
    pasos INT(10) NOT NULL,
    nivelEstres ENUM('Bajo', 'Medio','Alto'),
    fechaRegistro DATE
);

DELIMITER $$

-- registrar medicion
CREATE PROCEDURE RegistrarMedicion(
    IN pulsaciones INT,
    IN pasos INT,
    IN nivelEstres INT
)
BEGIN
    INSERT INTO Medicion (pulsaciones, pasos, nivelEstres)
    VALUES (pulsaciones, pasos, nivelEstres);
END$$

DELIMITER ;

DELIMITER $$

-- guardar signosvitales
CREATE PROCEDURE guardarSignosVitales(
    IN usuarioId INT,
    IN pulsaciones INT,
    IN presionArterial INT
)
BEGIN
    INSERT INTO SignosVitales (usuarioId, pulsaciones, presionArterial, fechaRegistro)
    VALUES (usuarioId, pulsaciones, presionArterial, NOW())
    ON DUPLICATE KEY UPDATE
        pulsaciones = VALUES(pulsaciones),
        presionArterial = VALUES(presionArterial),
        fechaRegistro = NOW();
END$$

DELIMITER ;

-- guardar actividad fisica

DELIMITER $$

CREATE PROCEDURE guardarActividadFisica(
    IN usuarioId INT,
    IN pasos INT,
    IN distancia FLOAT,
    IN caloriasQuemadas INT
)
BEGIN
    INSERT INTO ActividadFisica (usuarioId, pasos, distancia, caloriasQuemadas)
    VALUES (usuarioId, pasos, distancia, caloriasQuemadas)
    ON DUPLICATE KEY UPDATE
        pasos = VALUES(pasos),
        distancia = VALUES(distancia),
        caloriasQuemadas = VALUES(caloriasQuemadas);
END$$

DELIMITER ;

-- guardar nivel  de estres
DELIMITER $$

CREATE PROCEDURE guardarNivelEstres(
    IN usuarioId INT,
    IN nivelEstres INT,
    IN motivo VARCHAR(255)
)
BEGIN
    INSERT INTO Estres (usuarioId, nivelEstres, motivo, fechaRegistro)
    VALUES (usuarioId, nivelEstres, motivo, NOW())
    ON DUPLICATE KEY UPDATE
        nivelEstres = VALUES(nivelEstres),
        motivo = VALUES(motivo),
        fechaRegistro = NOW();
END$$

DELIMITER ;

-- consultar tipos

DELIMITER $$

-- Consultar signos vitales por usuario
CREATE PROCEDURE consultarSignosVitales(
    IN usuarioId INT
)
BEGIN
    SELECT * FROM SignosVitales WHERE usuarioId = usuarioId;
END$$

-- Consultar actividad física por usuario
CREATE PROCEDURE consultarActividadFisica(
    IN usuarioId INT
)
BEGIN
    SELECT * FROM ActividadFisica WHERE usuarioId = usuarioId;
END$$

-- Consultar nivel de estrés por usuario
CREATE PROCEDURE consultarNivelEstres(
    IN usuarioId INT
)
BEGIN
    SELECT * FROM Estres WHERE usuarioId = usuarioId;
END$$

DELIMITER ;




