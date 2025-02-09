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
    private Llista llista;
    private Joc joc;
    private boolean continua;

    public ThreadSrvModifyList(Socket clientSocket, Joc joc) throws IOException {
        this.clientSocket = clientSocket;
        this.llista = llista;
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
        continua = true;
        this.joc = joc;
    }

    @Override
    public void run() {

        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            ObjectOutputStream oos = new ObjectOutputStream(out);

            //Enviar joc per establir el número de jugador i paraula
            oos.writeObject(joc);
            oos.flush();

            //Llegir joc amb la paraula escollida
            joc = (Joc) ois.readObject();

            //Esperar a que sigui torn 2
            if (joc.getTorn() >= 1) {
                oos.writeObject(joc);
                oos.flush();
                joc = (Joc) ois.readObject();
            }


            //Thread.sleep(500);
            //joc.setNumJugadros(joc.getNumJugadros() + 1);
            //oos.writeObject(joc);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}