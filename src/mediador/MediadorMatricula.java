package mediador;

import clases.Alumno;
import clases.Curso;
import clases.Matricula;
import clases.Retiro;
import java.util.ArrayList;
import java.util.List;
import servicios.AlumnoService;
import servicios.CursoService;
import servicios.MatriculaService;
import servicios.RetiroService;

/**
 * Mediador Concreto del sistema de matrícula.
 * Decide QUÉ servicio atiende cada solicitud y A QUIÉN avisar después.
 * Las reglas de negocio viven en los servicios; aquí solo hay coordinación.
 */
public class MediadorMatricula implements Mediador {

    private final List<Colega> colegas = new ArrayList<>();

    private final AlumnoService    alumnos;
    private final CursoService     cursos;
    private final MatriculaService matriculas;
    private final RetiroService    retiros;

    public MediadorMatricula() {
        this(new AlumnoService(), new CursoService(), new MatriculaService(), new RetiroService());
    }

    public MediadorMatricula(AlumnoService a, CursoService c, MatriculaService m, RetiroService r) {
        this.alumnos = a;
        this.cursos = c;
        this.matriculas = m;
        this.retiros = r;
    }

    @Override
    public void registrar(Colega colega) {
        if (!colegas.contains(colega)) colegas.add(colega);
    }

    @Override
    public void eliminar(Colega colega) {
        colegas.remove(colega);
    }

    @Override
    public void notificar(Colega emisor, Evento evento, Object datos) {
        try {
            String mensaje = procesar(evento, datos);
            if (mensaje == null) return;                       // no era una solicitud
            difundir(Evento.DATOS_ACTUALIZADOS, null);         // 1) todos se refrescan
            emisor.recibir(Evento.OPERACION_EXITOSA, mensaje); // 2) el emisor muestra el resultado
        } catch (ReglaNegocioException ex) {
            emisor.recibir(Evento.OPERACION_RECHAZADA, ex.getMessage());
        } catch (Exception ex) {
            emisor.recibir(Evento.ERROR_TECNICO, "Error de base de datos: " + ex.getMessage());
        }
    }

    /** Ejecuta la solicitud y devuelve el mensaje de éxito (null si el evento no es una solicitud). */
    private String procesar(Evento evento, Object datos) throws Exception {
        switch (evento) {
            case ALUMNO_ADICIONAR:
                alumnos.adicionar((Alumno) datos);
                return "Nuevo alumno añadido exitosamente";
            case ALUMNO_MODIFICAR:
                alumnos.modificar((Alumno) datos);
                return "Alumno modificado exitosamente";
            case ALUMNO_ELIMINAR:
                alumnos.eliminar((Integer) datos);
                return "Alumno eliminado exitosamente";

            case CURSO_ADICIONAR:
                cursos.adicionar((Curso) datos);
                return "Nuevo curso añadido exitosamente";
            case CURSO_MODIFICAR:
                cursos.modificar((Curso) datos);
                return "Curso modificado exitosamente";
            case CURSO_ELIMINAR:
                cursos.eliminar((Integer) datos);
                return "Curso eliminado exitosamente";

            case MATRICULA_ADICIONAR:
                int numMat = matriculas.registrar((Matricula) datos);
                return "Matrícula registrada. Número asignado: " + numMat;
            case MATRICULA_MODIFICAR:
                Matricula m = (Matricula) datos;
                matriculas.modificarCurso(m.getNumeroMatricula(), m.getCodigoCurso());
                return "Matrícula modificada exitosamente";
            case MATRICULA_ELIMINAR:
                matriculas.eliminar((Integer) datos);
                return "Matrícula eliminada exitosamente";

            case RETIRO_ADICIONAR:
                int numRet = retiros.registrar((Retiro) datos);
                return "Nuevo retiro añadido. Número: " + numRet;
            case RETIRO_MODIFICAR:
                CambioCurso cc = (CambioCurso) datos;
                retiros.modificarCurso(cc.getNumero(), cc.getCodCurso());
                return "Retiro modificado exitosamente";
            case RETIRO_ELIMINAR:
                retiros.eliminar((Integer) datos);
                return "Retiro eliminado exitosamente";

            default:
                return null;
        }
    }

    private void difundir(Evento evento, Object datos) {
        for (Colega c : new ArrayList<>(colegas)) {   // copia: un colega puede darse de baja al recibir
            c.recibir(evento, datos);
        }
    }
}
