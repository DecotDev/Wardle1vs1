package exerciciTransferObj;



import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ClientTcpListObj extends Thread {
    private Llista llista;
    private String nom;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private boolean continueConnected;
    private Random random = new Random();
    private Joc joc;

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

    private List<Integer> createNumberList() {
        List<Integer> newList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            newList.add(random.nextInt(10)+1);
        }
        return newList;
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

        while (true) {
            llista = new Llista("Llista1", createNumberList());
            //llista.setNumberList(createNumberList());
            //llista.setNom("Liista1");
            try {
                oos.writeObject(llista);
                out.flush();
            } catch (IOException e) {
                System.out.println("There was a problem sending the list.");
            }

            try {
                llista = (Llista) ois.readObject();
                System.out.print("Llista rebuda: ");

                System.out.println(llista.getNumberList().stream().map(String::valueOf).collect(Collectors.joining(" ")));

                //while (joc == null) {
                try {
                    this.joc = (Joc) ois.readObject();
                    System.out.println(joc.getNumJugadros());
                } catch (Exception e) {
                    System.out.println("No s'ha llegit cap joc");
                } finally {
                    System.out.println("Joc llegit");
                }
                Thread.sleep(500);
                joc.setNumJugadros(joc.getNumJugadros() + 1);
                oos.writeObject(joc);
                oos.flush();
                Thread.sleep(500);
                try {
                    this.joc = (Joc) ois.readObject();
                    System.out.println(joc.getNumJugadros());
                } catch (Exception e) {
                    System.out.println("No s'ha llegit o no es pot accedir");
                } finally {
                    System.out.println("Joc llegit");
                }

                /*while (joc.getNumJugadros() < 50) {
                    Thread.sleep(500);
                    joc.setNumJugadros(joc.getNumJugadros() + 1);
                    oos.writeObject(joc);
                    oos.flush();
                    Thread.sleep(500);
                    try {
                        this.joc = (Joc) ois.readObject();
                        System.out.println(joc.getNumJugadros());
                    } catch (Exception e) {
                        System.out.println("No s'ha llegit o no es pot accedir");
                    } finally {
                        System.out.println("Joc llegit");
                    }
                }

                 */
                //TODO añadir bucle de jugar modificando el joc, enviarlo al thread, el thread modifica el joc, cliente 2 leer joc
                //}

                //System.out.println(joc.getNumJugadros());
                //System.out.println(joc.toString());

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Sleep per comprovar que el servidor pot gestionar diversos clients a l'hora.
            try {
                System.out.println("Finished");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!continueConnected) close(socket);
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
