# 🎓 Sistema de Matrícula Académica

Sistema de escritorio para la gestión completa de matrículas universitarias — 
alumnos, cursos, registros y reportes, todo desde una sola aplicación.

## ✨ Funcionalidades

- 📊 **Dashboard en tiempo real** — tarjetas con estadísticas y gráfico de alumnos por curso
- 👤 **Gestión de alumnos** — altas, bajas, modificaciones y consultas
- 📚 **Gestión de cursos** — registro completo con ciclo, créditos y horas
- 📋 **Registro de matrículas** — control de estado del alumno (Registrado / Matriculado / Retirado)
- 🔄 **Registro de retiros** — gestión de bajas con historial
- 🔍 **Consultas** — búsqueda por alumno, curso y matrícula
- 📄 **Reportes** — alumnos matriculados, pendientes y vigentes

---

## 🏗️ Arquitectura del proyecto

El sistema sigue una arquitectura de **3 capas**:

```
┌─────────────────────────────────────┐
│         Capa de Presentación        │  ← Java Swing (paquete gui)
│  FrmPrincipal · DlgMant* · DlgReg* │
└────────────────┬────────────────────┘
                 │
┌────────────────▼────────────────────┐
│           Capa de Acceso a Datos    │  ← DAOs (paquete arreglos)
│  AlumnoDao · CursoDao · Matricula   │
│  Dao · RetiroDao · DashboardDao     │
└────────────────┬────────────────────┘
                 │
┌────────────────▼────────────────────┐
│             Persistencia            │  ← MySQL 8
│  alumnos · cursos · matriculas      │
│  retiros · usuarios                 │
└──────────────────────────────────-──┘
```

---

## 🛠️ Tecnologías

| Capa | Tecnología | Versión |
|------|-----------|---------|
| Lenguaje | Java | 8+ |
| Interfaz de usuario | Java Swing | — |
| Base de datos | MySQL | 8.0 |
| Conector BD | MySQL JDBC Driver | 8.0+ |
| IDE | Apache NetBeans | 12+ |

---

## 🚀 Instalación y configuración

### Requisitos previos

- ✅ Java JDK 8 o superior
- ✅ MySQL Server 8.0 o superior
- ✅ Apache NetBeans 12+
- ✅ MySQL JDBC Connector (`.jar` en las librerías del proyecto)

1. Clonar el repositorio

`bash
git clone https://github.com/ThiagoCalderon/sistema-matricula.git
cd sistema-matricula


### 2. Crear la base de datos

Abre **MySQL Workbench** y ejecuta el script completo:

```bash
database/schema.sql


Esto creará la base de datos `registro_matricula` con todas las tablas y datos de prueba incluidos.

### 3. Configurar la conexión

Copia el archivo de ejemplo y edita tus credenciales:

```bash
# En Windows
copy src\config.properties.example src\config.properties

# En Linux / Mac
cp src/config.properties.example src/config.properties
```

Edita `src/config.properties` con tus datos:

```properties
db.url=jdbc:mysql://localhost:3306/registro_matricula?useSSL=false&serverTimezone=UTC
db.user=root
db.pass=TU_CONTRASEÑA
```

> ⚠️ **Importante:** `config.properties` está en `.gitignore` — nunca se sube al repositorio.

### 4. Abrir en NetBeans

```
File → Open Project → seleccionar carpeta del proyecto → Open
Run → Run Project (F6)
```

---

## 📁 Estructura del proyecto

```
sistema-matricula/
│
├── src/
│   ├── clases/              # Modelos de dominio
│   │   ├── Alumno.java
│   │   ├── Curso.java
│   │   ├── Matricula.java
│   │   ├── Retiro.java
│   │   ├── Calendario.java
│   │   ├── Conexion.java
│   │   └── EstadoAlumno.java
│   │
│   ├── arreglos/            # Capa de acceso a datos (DAOs)
│   │   ├── AlumnoDao.java
│   │   ├── CursoDao.java
│   │   ├── MatriculaDao.java
│   │   ├── RetiroDao.java
│   │   └── DashboardDao.java
│   │
│   ├── gui/                 # Interfaz gráfica (Swing)
│   │   ├── FrmPrincipal.java
│   │   ├── PnlDashboard.java
│   │   ├── DlgMantAlumno.java
│   │   ├── DlgMantCurso.java
│   │   ├── DlgRegMatricula.java
│   │   └── DlgRegRetiro.java
│   │
│   └── config.properties.example   # Plantilla de configuración
│
├── database/
│   └── schema.sql           # Script completo de la BD con datos de prueba
│
├── screenshots/             # Capturas de pantalla del sistema
├── .gitignore
└── README.md
```

---

## 🗄️ Modelo de base de datos

```sql
alumnos       → cod_alumno (PK), nombres, apellidos, dni, edad, celular, estado
cursos        → cod_curso (PK), asignatura, ciclo, creditos, horas
matriculas    → num_matricula (PK, AUTO_INCREMENT), cod_alumno (FK), cod_curso (FK), fecha, hora, activo
retiros       → num_retiro (PK, AUTO_INCREMENT), num_matricula (FK), fecha, hora
usuarios      → id (PK), usuario, clave, nombre
```

**Estados del alumno:**
| Valor | Estado | Descripción |
|-------|--------|-------------|
| 0 | REGISTRADO | Alumno sin matrícula activa |
| 1 | MATRICULADO | Alumno con matrícula activa |
| 2 | RETIRADO | Alumno que se retiró del curso |

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para cambios importantes:

1. Haz fork del proyecto
2. Crea una rama (`git checkout -b feature/nueva-funcionalidad`)
3. Commitea tus cambios (`git commit -m 'feat: agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

---

## 👤 Autor
**Thiago Calderon**

