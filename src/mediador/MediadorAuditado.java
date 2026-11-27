package mediador;

import auditoria.Bitacora;

/**
 * PATRÓN DECORATOR (Estructural).
 * Envuelve cualquier Mediador y le añade auditoría (qué se pidió, quién, resultado, duración)
 * sin tocar MediadorMatricula ni los diálogos. Mismo contrato: es intercambiable.
 */
public class MediadorAuditado implements Mediador {
    private final Mediador interno;
    private final Bitacora bitacora;

    public MediadorAuditado(Mediador interno) {
        this(interno, Bitacora.getInstancia());
    }

    public MediadorAuditado(Mediador interno, Bitacora bitacora) {
        this.interno = interno;
        this.bitacora = bitacora;
    }

    @Override
    public void registrar(Colega colega) {
        interno.registrar(colega);
        bitacora.registrar("INFO", "Colega registrado: " + colega.getClass().getSimpleName());
    }

    @Override
    public void eliminar(Colega colega) {
        interno.eliminar(colega);
        bitacora.registrar("INFO", "Colega dado de baja: " + colega.getClass().getSimpleName());
    }

    @Override
    public void notificar(Colega emisor, Evento evento, Object datos) {
        bitacora.registrar("SOLICITUD", evento + " <- " + emisor.getClass().getSimpleName() + describir(datos));
        interno.notificar(new ColegaAuditado(emisor, evento), evento, datos);
    }

    /** Solo tipo e id: nunca se vuelcan datos personales (DNI, celular) a la bitácora. */
    private static String describir(Object datos) {
        if (datos == null) return "";
        if (datos instanceof Integer) return " (id=" + datos + ")";
        return " (" + datos.getClass().getSimpleName() + ")";
    }

    /** Decora al emisor para observar el resultado que el mediador le devuelve. */
    private class ColegaAuditado implements Colega {
        private final Colega real;
        private final Evento solicitud;
        private final long inicio = System.nanoTime();

        ColegaAuditado(Colega real, Evento solicitud) {
            this.real = real;
            this.solicitud = solicitud;
        }

        @Override public void setMediador(Mediador m) { real.setMediador(m); }

        @Override
        public void recibir(Evento evento, Object datos) {
            long ms = (System.nanoTime() - inicio) / 1_000_000;
            switch (evento) {
                case OPERACION_EXITOSA:
                    bitacora.registrar("OK", solicitud + " (" + ms + " ms): " + datos);
                    break;
                case OPERACION_RECHAZADA:
                    bitacora.registrar("RECHAZO", solicitud + ": " + datos);
                    break;
                case ERROR_TECNICO:
                    bitacora.registrar("ERROR", solicitud + ": " + datos);
                    break;
                default:
                    break;
            }
            real.recibir(evento, datos);   // el log va ANTES: el diálogo puede bloquear con un JOptionPane
        }
    }
}
