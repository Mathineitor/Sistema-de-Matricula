package mediador;

/**
 * Catálogo de mensajes que viajan por el Mediador.
 * - SOLICITUDES: las envía un Colega al Mediador ("quiero hacer X").
 * - NOTIFICACIONES: las envía el Mediador a los Colegas ("ya pasó Y").
 */
public enum Evento {
    // ── Solicitudes (Colega -> Mediador) ──────────────────────────────
    ALUMNO_ADICIONAR, ALUMNO_MODIFICAR, ALUMNO_ELIMINAR,
    CURSO_ADICIONAR, CURSO_MODIFICAR, CURSO_ELIMINAR,
    MATRICULA_ADICIONAR, MATRICULA_MODIFICAR, MATRICULA_ELIMINAR,
    RETIRO_ADICIONAR, RETIRO_MODIFICAR, RETIRO_ELIMINAR,

    // ── Notificaciones (Mediador -> Colegas) ──────────────────────────
    /** Los datos cambiaron en la BD: todos los colegas deben refrescarse. */
    DATOS_ACTUALIZADOS,
    /** Solo al emisor: la operación terminó bien. datos = mensaje (String). */
    OPERACION_EXITOSA,
    /** Solo al emisor: una regla de negocio la rechazó. datos = mensaje (String). */
    OPERACION_RECHAZADA,
    /** Solo al emisor: falló la BD u otro error técnico. datos = mensaje (String). */
    ERROR_TECNICO
}
