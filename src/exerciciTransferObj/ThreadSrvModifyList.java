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

            try {
                //enviar llista
                //llista = (Llista) ois.readObject();
                //llista.setNumberList(sortList(llista));
                //oos.writeObject(llista);
                //oos.flush();
                //enviar game
                //for (int i = 0; i < 500; i++) {
                oos.writeObject(joc);

                oos.flush();

                joc = (Joc) ois.readObject();
                Thread.sleep(500);
                joc.setNumJugadros(joc.getNumJugadros() + 1);
                oos.writeObject(joc);
                //}

                //while (true) {
                //this.joc = (Joc) ois.readObject();
                    //Thread.sleep(500);
                //oos.writeObject(joc);
                //}

                } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private List<Integer> sortList(Llista llista) {
        List<Integer> numberList = llista.getNumberList();
        List<Integer> numberListUnique = new ArrayList<>(new HashSet<>(numberList));
        Collections.sort(numberListUnique);
        return numberListUnique;
    }
}