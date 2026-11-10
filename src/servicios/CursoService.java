package servicios;

import arreglos.CursoDao;
import clases.Curso;
import java.sql.SQLIntegrityConstraintViolationException;
import mediador.ReglaNegocioException;

public class CursoService {
    private final CursoDao cursos = new CursoDao();

    public void adicionar(Curso c) throws Exception {
        cursos.adicionar(c);
    }

    public void modificar(Curso datos) throws Exception {
        Curso actual = cursos.buscar(datos.getCodCurso());
        if (actual == null) throw new ReglaNegocioException("El curso no existe");
        actual.setAsignatura(datos.getAsignatura());
        actual.setCiclo(datos.getCiclo());
        actual.setCreditos(datos.getCreditos());
        actual.setHoras(datos.getHoras());
        cursos.actualizar(actual);
    }

    public void eliminar(int codCurso) throws Exception {
        try {
            cursos.eliminar(codCurso);
        } catch (SQLIntegrityConstraintViolationException ex) {   // FK: hay matrículas con este curso
            throw new ReglaNegocioException("No puede eliminar un curso que tiene matrículas registradas");
        }
    }
}
