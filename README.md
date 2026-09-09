# Sistema de Matrícula Académica

Aplicación de escritorio para la gestión integral de matrículas universitarias: alumnos, cursos, registros y reportes, todo centralizado en una sola plataforma.

## Funcionalidades

- **Dashboard en tiempo real**: estadísticas y gráfico de alumnos por curso.  
- **Gestión de alumnos**: altas, bajas, modificaciones y consultas.  
- **Gestión de cursos**: registro completo con ciclo, créditos y horas.  
- **Registro de matrículas**: control del estado del alumno (Registrado / Matriculado / Retirado).  
- **Registro de retiros**: gestión de bajas con historial.  
- **Consultas**: búsqueda por alumno, curso y matrícula.  
- **Reportes**: alumnos matriculados, pendientes y vigentes.  

---

## Tecnologías

| Capa                | Tecnología        | Versión |
|---------------------|------------------|---------|
| Lenguaje            | Java             | 8+      |
| Interfaz de usuario | Java Swing       | —       |
| Base de datos       | MySQL            | 8.0     |
| Conector BD         | MySQL JDBC Driver| 8.0+    |
| IDE                 | Apache NetBeans  | 12+     |

---

## Instalación y configuración

### Requisitos previos

- Java JDK 8 o superior  
- MySQL Server 8.0 o superior  
- Apache NetBeans 12+  
- MySQL JDBC Connector (`.jar` en las librerías del proyecto)  

### 1. Clonar el repositorio

```bash
git clone https://github.com/ThiagoCalderon/sistema-matricula.git
cd sistema-matricula
2. Crear la base de datos
Abrir MySQL Workbench y ejecutar el script:

bash
database/schema.sql
Esto creará la base de datos registro_matricula con todas las tablas y datos de prueba incluidos.

3. Configurar la conexión
Copiar el archivo de ejemplo y editar credenciales:

bash
# En Windows
copy src\config.properties.example src\config.properties

# En Linux / Mac
cp src/config.properties.example src/config.properties
Editar src/config.properties:

properties
db.url=jdbc:mysql://localhost:3306/registro_matricula?useSSL=false&serverTimezone=UTC
db.user=root
db.pass=TU_CONTRASEÑA
Nota: config.properties está en .gitignore y no debe subirse al repositorio.

4. Abrir en NetBeans
Código
File → Open Project → seleccionar carpeta del proyecto → Open
Run → Run Project (F6)
Modelo de base de datos
sql
alumnos       → cod_alumno (PK), nombres, apellidos, dni, edad, celular, estado
cursos        → cod_curso (PK), asignatura, ciclo, creditos, horas
matriculas    → num_matricula (PK, AUTO_INCREMENT), cod_alumno (FK), cod_curso (FK), fecha, hora, activo
retiros       → num_retiro (PK, AUTO_INCREMENT), num_matricula (FK), fecha, hora
usuarios      → id (PK), usuario, clave, nombre
Estados del alumno:

Valor	Estado	Descripción
0	REGISTRADO	Alumno sin matrícula activa
1	MATRICULADO	Alumno con matrícula activa
2	RETIRADO	Alumno que se retiró del curso


Contribuciones
Las contribuciones son bienvenidas. Para cambios importantes:

Realizar un fork del proyecto.

Crear una rama (git checkout -b feature/nueva-funcionalidad).

Registrar los cambios (git commit -m 'feat: agrega nueva funcionalidad').

Subir la rama (git push origin feature/nueva-funcionalidad).

Abrir un Pull Request.

Autor
Thiago Calderon


