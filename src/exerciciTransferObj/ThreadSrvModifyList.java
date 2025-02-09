package exerciciTransferObj;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

class ThreadSrvModifyList extends Thread {
    private Socket clientSocket = null;
    private InputStream in = null;
    private OutputStream out = null;
    private final Joc joc;
    private boolean continua;

    public ThreadSrvModifyList(Socket clientSocket, Joc joc) throws IOException {
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
    }
}