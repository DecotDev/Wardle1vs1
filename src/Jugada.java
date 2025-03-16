import java.io.Serializable;

public class Jugada implements Serializable {
    private static final long serialVersionUID = 1L;
    private int player;
    private String paraula;
    private String resposta;

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
    //enviar resultando del intento de avdivinanza del cliente
    //enviar turno
}
