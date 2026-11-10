package servicios;

import arreglos.AlumnoDao;
import clases.Alumno;
import clases.EstadoAlumno;
import mediador.ReglaNegocioException;

public class AlumnoService {
    private final AlumnoDao alumnos = new AlumnoDao();

    public void adicionar(Alumno a) throws Exception {
        if (alumnos.existeDNI(a.getDni()))
            throw new ReglaNegocioException("No puede ingresar un DNI ya existente");
        alumnos.adicionar(a);
    }

    /** Actualiza datos personales; el estado NUNCA se cambia desde aquí (lo gobiernan matrícula/retiro). */
    public void modificar(Alumno datos) throws Exception {
        Alumno actual = alumnos.buscar(datos.getCodAlumno());
        if (actual == null) throw new ReglaNegocioException("El alumno no existe");
        actual.setNombres(datos.getNombres());
        actual.setApellidos(datos.getApellidos());
        actual.setEdad(datos.getEdad());
        actual.setCelular(datos.getCelular());
        alumnos.actualizar(actual);
    }

    public void eliminar(int codAlumno) throws Exception {
        Alumno a = alumnos.buscar(codAlumno);
        if (a == null) throw new ReglaNegocioException("El alumno no existe");
        if (a.getEstado() != EstadoAlumno.REGISTRADO.getValor())
            throw new ReglaNegocioException("No puede eliminar a un alumno ya matriculado");
        alumnos.eliminar(codAlumno);
    }
}
