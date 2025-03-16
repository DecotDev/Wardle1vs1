import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    private List<String> getRandomWords() {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/Palabras.txt"))) {
            String line = br.readLine();
            if (line != null) {
                String[] allWords = line.split(";");
                Collections.addAll(words, allWords);
                Collections.shuffle(words); // Shuffle the list to get random words
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words.subList(0, 3); // Return the first 3 words after shuffling
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
            // Llegir joc inicial i posar id de jugadors
            joc = (Joc) ois.readObject();
            jugador = joc.getNumJugadros() - 1;
            if (jugador == 0) adversari = 1;
            else adversari = 0;
            jugada.setPlayer(jugador);
            System.out.println("Joc rebut, ets el jugador: " + jugador);

            // Escollir paraula i enviar joc
            joc.actualitzaTorn();
            List<String> randomWords = getRandomWords();
            System.out.println("Escull una paraula per al rival:");
            for (int i = 0; i < randomWords.size(); i++) {
                System.out.println((i + 1) + ". " + randomWords.get(i));
            }
            System.out.print("Introdueix el número de la paraula escollida: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume the newline character
            jugada.setParaula(randomWords.get(choice - 1));

            jugada.setGuanyador(joc.getGuanyador());

            oos.writeObject(jugada);
            oos.reset();
            oos.flush();
            System.out.println("\nParaula enviada.");
            Thread.sleep(500);

            // Llegir paraula escollida pel rival
            Joc jocNou = null;
            jocNou = (Joc) ois.readObject();
            joc = jocNou;
            while (true) {
                Thread.sleep(500);
                System.out.print("Intenta endevinar la paraula: ");
                jugada.setResposta(sc.nextLine());
                jugada.setGuanyador(joc.getGuanyador());

                oos.writeObject(jugada);
                oos.reset();
                oos.flush();

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

                if (joc.getGuanyador() == jugador) {
                    Thread.sleep(100);
                    System.exit(0);
                }

                if (!continueConnected) System.exit(0);
            }
        } catch (RuntimeException | IOException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //Metode utilizat durant el desenvolupament
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
        }
        if (joc.getGuanyador() == (adversari)) {
            System.out.println("MASSA LENT, HAS PERDUT");
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