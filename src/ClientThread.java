import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientThread extends Thread {

    private int jugador;
    private int adversari;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private boolean continueConnected;
    private Joc joc;
    private Scanner sc = new Scanner(System.in);
    private Jugada jugada;
    private String resultat;

    private ClientThread(String hostname, int port) {
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            continueConnected = true;
            jugada = new Jugada();
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
                jugada.setPlayer(jugador);
                System.out.println("Joc rebut, ets el jugador: " + jugador);

                //Escollir paraula i enviar joc
                joc.actualitzaTorn();
                System.out.print("Escull una parula per al rival: ");
                jugada.setParaula(sc.nextLine());

                jugada.setGuanyador(joc.getGuanyador());

                oos.writeObject(jugada);
                oos.reset();
                oos.flush();
                System.out.println("\nParaula enviada.");
                Thread.sleep(500);

                //Llegir paraula escollida pel rival
                Joc jocNou = null;
                jocNou = (Joc) ois.readObject();
                joc = jocNou;
                //soutInfo();
                while (true) {
                    Thread.sleep(500);
                    //joc.setNumJugadros(joc.getNumJugadros() + 1);
                    //System.out.println("Jugadors +1 : " + joc.getNumJugadros());
                    System.out.print("Intenta endevinar la paraula: ");
                    jugada.setResposta(sc.nextLine());

                    //resultat = comprovarParaula(joc.getResposata(jugador), joc.getParaula(adversari));
                    //comprovarResultat(resultat);

                    jugada.setGuanyador(joc.getGuanyador());

                    oos.writeObject(jugada);
                    oos.reset();
                    oos.flush();

                    //dictarGuanyador();

                    Thread.sleep(500);

                    joc = (Joc) ois.readObject();

                    jugada.setGuanyador(joc.getGuanyador());

                    dictarGuanyador();

                    resultat = comprovarParaula(joc.getResposata(jugador), joc.getParaula(adversari));
                    System.out.println(resultat);
                    comprovarResultat(resultat);

                    dictarGuanyador();



                    oos.writeObject(jugada);
                    oos.reset();
                    oos.flush();
                    joc = (Joc) ois.readObject();

                    //soutInfo();

                    if (joc.getGuanyador() == jugador) {
                        Thread.sleep(100);
                        System.exit(0);
                    }


                    if (!continueConnected) close(socket);
                }
            }catch (RuntimeException | IOException | ClassNotFoundException | InterruptedException e) {
                throw new RuntimeException(e);
            }
    }

    private void soutInfo() {
        System.out.println("Llista de paraules i info");
        System.out.println("Parula 1: " + joc.getParaula(0));
        System.out.println("Parula 2: " + joc.getParaula(1));
        System.out.println("Resposta 1: " + joc.getResposata(0));
        System.out.println("Resposta 2: " + joc.getResposata(1));
        System.out.println("Torn: " + joc.getTorn());
        System.out.println("Jugador: " + jugador);
        System.out.println("Adversari: " + adversari);
        System.out.println("Guanyador: " + joc.getGuanyador());
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
            Logger.getLogger(ClientThread.class.toString());
        }
    }
    private String comprovarParaula(String intent, String paraula) {

        if (intent.length() != paraula.length()) {
            return new String("La paraula es de " + paraula.length() + " lletres.");
        }

        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < paraula.length(); i++) {
            if (intent.charAt(i) == paraula.charAt(i)) {
                resultado.append("O");
            } else if (paraula.contains(String.valueOf(intent.charAt(i)))) {
                resultado.append("?");
            } else {
                resultado.append("-");
            }
        }
        return resultado.toString();
    }

    private void comprovarResultat(String resultat) {

        boolean adivinada = true;
        for (int i = 0; i < resultat.length(); i++) {
            if (resultat.charAt(i) != 'O') {
                adivinada = false;
            }
        }
        if (adivinada) {
            joc.setGuanyador(jugador);
            jugada.setGuanyador(jugador);
        }
    }

    private void dictarGuanyador() {
        if (joc.getGuanyador() == (jugador)) {
            System.out.println("ENHORABONA, HAS GUANYAT!");
            //close(socket);

        }
        if (joc.getGuanyador() == (adversari)) {
            System.out.println("MASSA LENT, HAS PERDUT");
            //close(socket);
            System.out.println("La paraula que havies d'adivinar era: " + joc.getParaula(adversari));
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        String ipSrv = "127.0.0.1";
        ClientThread clientTcp = new ClientThread(ipSrv, 5558);
        clientTcp.start();

    }

}
