CREATE DATABASE IF NOT EXISTS registro_matricula
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;
USE registro_matricula;

-- 1) alumnos (estado: 0=REGISTRADO, 1=MATRICULADO, 2=RETIRADO)
CREATE TABLE alumnos (
  cod_alumno   INT        NOT NULL,   -- ej: 202010001
  nombres      VARCHAR(50) NOT NULL,
  apellidos    VARCHAR(60) NOT NULL,
  dni          CHAR(8)     NOT NULL,
  edad         TINYINT     NOT NULL,
  celular      VARCHAR(15) NOT NULL,
  estado       TINYINT     NOT NULL DEFAULT 0,
  PRIMARY KEY (cod_alumno),
  UNIQUE KEY uq_alumnos_dni (dni)
) ENGINE=InnoDB;

-- 2) cursos (ciclo: 0..5 como en tu GUI)
CREATE TABLE cursos (
  cod_curso    INT         NOT NULL,   -- ej: 101..105
  asignatura   VARCHAR(80) NOT NULL,
  ciclo        TINYINT     NOT NULL,   -- 0..5
  creditos     TINYINT     NOT NULL,
  horas        TINYINT     NOT NULL,
  PRIMARY KEY (cod_curso)
) ENGINE=InnoDB;

-- 3) matriculas (activo: 1=Sí, 0=No)
CREATE TABLE matriculas (
  num_matricula INT        NOT NULL,
  cod_alumno    INT        NOT NULL,
  cod_curso     INT        NOT NULL,
  fecha         DATE       NOT NULL,
  hora          TIME       NOT NULL,
  activo        TINYINT    NOT NULL DEFAULT 1,
  PRIMARY KEY (num_matricula),
  KEY ix_m_alumno (cod_alumno),
  KEY ix_m_curso (cod_curso),
  CONSTRAINT fk_m_alumno FOREIGN KEY (cod_alumno)
    REFERENCES alumnos(cod_alumno) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_m_curso FOREIGN KEY (cod_curso)
    REFERENCES cursos(cod_curso) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 4) retiros (un retiro por matrícula activa)
CREATE TABLE retiros (
  num_retiro     INT      NOT NULL,
  num_matricula  INT      NOT NULL,
  fecha          DATE     NOT NULL,
  hora           TIME     NOT NULL,
  PRIMARY KEY (num_retiro),
  UNIQUE KEY uq_retiro_matricula (num_matricula),
  CONSTRAINT fk_r_matricula FOREIGN KEY (num_matricula)
    REFERENCES matriculas(num_matricula) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;
select*from cursos;

INSERT INTO alumnos (cod_alumno, nombres, apellidos, dni, edad, celular, estado)
VALUES (202010006, 'Pedro', 'García Ruiz', '12349876', 21, '987654321', 0);

INSERT INTO alumnos (cod_alumno, nombres, apellidos, dni, edad, celular, estado) VALUES
(202010007, 'Lucía', 'Morales Vega', '23456789', 20, '912345678', 1),
(202010008, 'Diego', 'Sánchez León', '34567890', 22, '999888777', 2),
(202010009, 'Valeria', 'Torres Silva', '45678901', 19, '955667788', 1),
(202010010, 'Andrés', 'Flores Ramos', '56789012', 23, '944332211', 0),
(202010011, 'Camila', 'Castro Paredes', '67890123', 21, '933221144', 1),
(202010012, 'Mateo', 'Reyes Delgado', '78901234', 20, '922334455', 2),
(202010013, 'Sofía', 'Mendoza Aguilar', '89012345', 22, '911223344', 1),
(202010014, 'Jorge', 'Salazar Núñez', '90123456', 24, '900112233', 0),
(202010015, 'Fernanda', 'Rojas Campos', '01234567', 19, '988776655', 1);

INSERT INTO cursos (cod_curso, asignatura, ciclo, creditos, horas) VALUES
(106, 'Algoritmos y Estructuras de Datos', 1, 5, 8),
(107, 'Ingeniería de Software', 2, 4, 6),
(108, 'Sistemas Operativos', 3, 4, 6),
(109, 'Compiladores', 4, 3, 5),
(110, 'Seguridad Informática', 5, 4, 6),
(111, 'Proyecto Integrador', 5, 6, 10);


INSERT INTO matriculas (num_matricula, cod_alumno, cod_curso, fecha, hora, activo) VALUES
(5006, 202010006, 106, '2025-03-06', '09:00:00', 1),
(5007, 202010007, 107, '2025-03-07', '10:30:00', 1),
(5008, 202010008, 108, '2025-03-08', '08:15:00', 0), -- retirado
(5009, 202010009, 109, '2025-03-09', '11:00:00', 1),
(5010, 202010010, 110, '2025-03-10', '14:00:00', 1),
(5011, 202010011, 111, '2025-03-11', '15:30:00', 1),
(5012, 202010012, 106, '2025-03-12', '09:45:00', 0), -- retirado
(5013, 202010013, 107, '2025-03-13', '10:00:00', 1),
(5014, 202010014, 108, '2025-03-14', '13:00:00', 1),
(5015, 202010015, 109, '2025-03-15', '16:00:00', 1);

INSERT INTO retiros (num_retiro, num_matricula, fecha, hora) VALUES
(7002, 5008, '2025-04-02', '15:00:00'),
(7003, 5012, '2025-04-03', '16:30:00');

select * from retiros;
select * from alumnos;
show databases;
show variables like 'port' ;
SHOW CREATE TABLE matriculas;

-- Solo matriculas y retiros pasan a AUTO_INCREMENT
ALTER TABLE matriculas MODIFY num_matricula INT NOT NULL AUTO_INCREMENT;
ALTER TABLE retiros    MODIFY num_retiro    INT NOT NULL AUTO_INCREMENT;
-- Aseguramos que arranque desde después del último existente
ALTER TABLE matriculas AUTO_INCREMENT = 5016;
ALTER TABLE retiros    AUTO_INCREMENT = 7004;
-- User
SELECT user, host FROM mysql.user;

CREATE TABLE IF NOT EXISTS usuarios (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    usuario  VARCHAR(50) NOT NULL UNIQUE,
    clave    VARCHAR(50) NOT NULL,
    nombre   VARCHAR(100) NOT NULL
);

-- Usuario de prueba
INSERT INTO usuarios (usuario, clave, nombre)
VALUES ('admin', 'admin123', 'Administrador')

select * from usuarios;
