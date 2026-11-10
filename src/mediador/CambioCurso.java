package mediador;

/** Carga útil para RETIRO_MODIFICAR: "al retiro N asígnale el curso C". */
public class CambioCurso {
    private final int numero;
    private final int codCurso;

    public CambioCurso(int numero, int codCurso) {
        this.numero = numero;
        this.codCurso = codCurso;
    }
    public int getNumero()   { return numero; }
    public int getCodCurso() { return codCurso; }
}
