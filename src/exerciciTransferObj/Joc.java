package exerciciTransferObj;

import java.io.Serializable;

public class Joc implements Serializable {
    private static final long serialVersionUID = 1L;
    private int torn;
    private int numJugadros;
    private String[] paraula;
    private String[] resposta;

    public Joc() {
        this.torn = 0;
        this.numJugadros = 0;
        this.paraula =  new String[2];
        this.resposta =  new String[2];
    }

    public int getTorn() {
        return torn;
    }

    public void setTorn(int torn) {
        this.torn = torn;
    }

    public String getResposata(int jugador) {
        return resposta[jugador];
    }

    public void setResposata(int jugador, String resposta) {
        this.resposta[jugador] = resposta;
    }

    public String getParaula(int jugador) {
        return paraula[jugador];
    }

    public void setParaula(int jugador, String paraula) {
        this.paraula[jugador] = paraula;
    }

    public int getNumJugadros() {
        return numJugadros;
    }

    public void setNumJugadros(int numJugadros) {
        this.numJugadros = numJugadros;
    }
    public void actualitzaTorn() {
        this.torn = this.torn + 1;
    }

}
