import java.io.Serializable;

public class Jugada implements Serializable {
    private static final long serialVersionUID = 1L;
    private String intento;
    private String resultado;
    private int turno;

    public Jugada(String intento, String resultado, int turno) {
        this.intento = intento;
        this.resultado = resultado;
        this.turno = turno;
    }

    public String getIntento() {
        return intento;
    }

    public String getResultado() {
        return resultado;
    }

    public int getTurno() {
        return turno;
    }
}
