import java.io.*;
import java.net.Socket;

class ThreadServer extends Thread {
    private Socket clientSocket = null;
    private InputStream in = null;
    private OutputStream out = null;
    private final Joc joc;
    private boolean continua;

    public ThreadServer(Socket clientSocket, Joc joc) throws IOException {
        this.clientSocket = clientSocket;
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
        continua = true;
        this.joc = joc;
    }

    @Override
    public void run() {
        Jugada jugada = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            ObjectOutputStream oos = new ObjectOutputStream(out);

            //Enviar joc per establir el número de jugador i paraula
            oos.writeObject(joc);
            oos.reset();
            oos.flush();

            //Llegir joc amb la paraula escollida
            jugada = (Jugada) ois.readObject();
            actualitzarGame(jugada);

            //Esperar a que sigui torn 2
            while (joc.getTorn() >= 1) {
                oos.writeObject(joc);
                oos.reset();
                oos.flush();
                jugada = (Jugada) ois.readObject();
                actualitzarGame(jugada);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void actualitzarGame(Jugada jugada) {
        joc.actualitzaTorn();
        joc.setParaula(jugada.getPlayer(), jugada.getParaula());
        joc.setResposata(jugada.getPlayer(), jugada.getResposta());
        if (joc.getGuanyador() == -1) {
            joc.setGuanyador(jugada.getGuanyador());
        }

    }
}