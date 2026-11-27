package clases;

/**
 * PATRÓN BUILDER (Creacional).
 * Construye un Alumno paso a paso y valida sus reglas antes de crearlo.
 * Si algo es inválido lanza IllegalArgumentException con el mensaje listo para el usuario.
 */
public class AlumnoBuilder {
    private int codAlumno;
    private String nombres, apellidos, dni, celular;
    private int edad = -1;
    private EstadoAlumno estado = EstadoAlumno.REGISTRADO;   // valor por defecto del dominio

    public AlumnoBuilder codigo(int codAlumno)      { this.codAlumno = codAlumno; return this; }
    public AlumnoBuilder nombres(String nombres)    { this.nombres = nombres;     return this; }
    public AlumnoBuilder apellidos(String apellidos){ this.apellidos = apellidos; return this; }
    public AlumnoBuilder dni(String dni)            { this.dni = dni;             return this; }
    public AlumnoBuilder edad(int edad)             { this.edad = edad;           return this; }
    public AlumnoBuilder celular(String celular)    { this.celular = celular;     return this; }
    public AlumnoBuilder estado(EstadoAlumno estado){ this.estado = estado;       return this; }

    public Alumno build() {
        if (codAlumno <= 0)
            throw new IllegalArgumentException("El código del alumno no es válido");
        validarTexto(nombres, "nombre");
        validarTexto(apellidos, "apellido");
        if (dni == null || !dni.matches("\\d{1,8}") || Integer.parseInt(dni) <= 0)
            throw new IllegalArgumentException("El DNI debe ser numérico y tener hasta 8 dígitos");
        if (edad < 0 || edad >= 60)
            throw new IllegalArgumentException("La edad debe ser menor a 60");
        if (celular == null || !celular.matches("9\\d{8}"))
            throw new IllegalArgumentException("El celular debe tener 9 dígitos y empezar con 9");
        if (estado == null)
            throw new IllegalArgumentException("Especifique el estado del alumno");

        return new Alumno(codAlumno, nombres.trim(), apellidos.trim(), dni, edad, celular, estado.getValor());
    }

    private static void validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty())
            throw new IllegalArgumentException("Ingrese el " + campo);
        for (char c : valor.toCharArray())
            if (Character.isDigit(c))
                throw new IllegalArgumentException("No se permiten números en el " + campo);
    }
}
