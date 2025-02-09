package exerciciTransferObj;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientTcpListObj extends Thread {

    private int jugador;
    private int adversari;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private boolean continueConnected;
    private Joc joc;
    private Scanner sc = new Scanner(System.in);

    private ClientTcpListObj(String hostname, int port) {
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            continueConnected = true;
        } catch (UnknownHostException uhe) {
            System.out.println("Error de connexió. No existeix el host: " + uhe.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            oos = new ObjectOutputStream(out);
            ois = new ObjectInputStream(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

            try {
                //Llegir joc inicial i posar id de jugadors
                joc = (Joc) ois.readObject();
                jugador = joc.getNumJugadros() - 1;
                if (jugador == 0)adversari = 1;
                else adversari = 0;
                System.out.println("Joc rebut, ets el jugador: " + jugador);

                //Escollir paraula i enviar joc
                joc.actualitzaTorn();
                System.out.print("Escull una parula per al rival: ");
                joc.setParaula(jugador, sc.nextLine());
                oos.writeObject(joc);
                oos.flush();
                System.out.println("\nParaula enviada.");

                //Llegir paraula escollida pel rival
                joc = (Joc) ois.readObject();
                System.out.println("Parula escollida per el rival: " + joc.getParaula(adversari));
                System.out.println("Parula 1: " + joc.getParaula(0));
                System.out.println("Parula 2: " + joc.getParaula(1));
                System.out.println("Torn: " + joc.getTorn());
                while (true) {
                    Thread.sleep(500);
                    //joc.setNumJugadros(joc.getNumJugadros() + 1);
                    //System.out.println("Jugadors +1 : " + joc.getNumJugadros());
                    oos.writeObject(joc);
                    oos.flush();
                    Thread.sleep(500);

                    joc = (Joc) ois.readObject();
                    System.out.println("Jugadors del joc rebut" + joc.getNumJugadros());

                    if (!continueConnected) close(socket);
                }
            }catch (RuntimeException | IOException | ClassNotFoundException | InterruptedException e) {
                throw new RuntimeException(e);
            }
    }

    private void close(Socket socket){
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(ClientTcpListObj.class.toString());
        }
    }

    public static void main(String[] args) {
        String ipSrv = "127.0.0.1";
        ClientTcpListObj clientTcp = new ClientTcpListObj(ipSrv, 5558);
        clientTcp.start();

    }

}
