package servicios;

import arreglos.CursoDao;
import clases.Ciclo;
import clases.Curso;
import clases.Texto;
import java.sql.SQLIntegrityConstraintViolationException;
import mediador.ReglaNegocioException;

public class CursoService {
    private final CursoDao cursos = new CursoDao();

    public void adicionar(Curso c) throws Exception {
        normalizar(c);
        cursos.adicionar(c);
    }

    public void modificar(Curso datos) throws Exception {
        normalizar(datos);
        Curso actual = cursos.buscar(datos.getCodCurso());
        if (actual == null) throw new ReglaNegocioException("El curso no existe");
        actual.setAsignatura(datos.getAsignatura());
        actual.setCiclo(datos.getCiclo());
        actual.setCreditos(datos.getCreditos());
        actual.setHoras(datos.getHoras());
        cursos.actualizar(actual);
    }

    /** Regla: la asignatura se guarda en formato "Nombre Propio" y el ciclo debe existir. */
    private void normalizar(Curso c) throws ReglaNegocioException {
        if (!Ciclo.esValido(c.getCiclo())) throw new ReglaNegocioException("El ciclo no es valido");
        c.setAsignatura(Texto.tituloCurso(c.getAsignatura()));
    }

    public void eliminar(int codCurso) throws Exception {
        try {
            cursos.eliminar(codCurso);
        } catch (SQLIntegrityConstraintViolationException ex) {   // FK: hay matr\u00edculas con este curso
            throw new ReglaNegocioException("No puede eliminar un curso que tiene matr\u00edculas registradas");
        }
    }
}
