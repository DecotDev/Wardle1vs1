import java.io.Serializable;

public class Jugada implements Serializable {
    private static final long serialVersionUID = 1L;
    private int player;
    private String paraula;
    private String resposta;
    private int guanyador = -1;

    public Jugada() {}

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public String getParaula() {
        return paraula;
    }

    public void setParaula(String paraula) {
        this.paraula = paraula;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public int getGuanyador() {
        return guanyador;
    }

    public void setGuanyador(int guanyador) {
        this.guanyador = guanyador;
    }

//enviar resultando del intento de avdivinanza del cliente
    //enviar turno
}
