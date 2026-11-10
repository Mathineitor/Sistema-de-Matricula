package clases;

public enum EstadoAlumno {
    REGISTRADO(0),
    MATRICULADO(1),
    RETIRADO(2);

    private final int valor;

    EstadoAlumno(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    /** Convierte el entero de la BD al enum correspondiente */
    public static EstadoAlumno desde(int valor) {
        for (EstadoAlumno e : values()) {
            if (e.valor == valor) return e;
        }
        throw new IllegalArgumentException("Estado inválido: " + valor);
    }

    @Override
    public String toString() {
        return name(); // "REGISTRADO", "MATRICULADO", "RETIRADO"
    }
}
