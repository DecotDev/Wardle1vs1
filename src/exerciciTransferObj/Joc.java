package exerciciTransferObj;

import java.io.Serializable;

public class Joc implements Serializable {
    private static final long serialVersionUID = 1L;
    private int numJugadros;

    public int getNumJugadros() {
        return numJugadros;
    }

    public void setNumJugadros(int numJugadros) {
        this.numJugadros = numJugadros;
    }

    @Override
    public String toString() {
        return "Joc{" +
                "numJugadros=" + numJugadros +
                '}';
    }
}
