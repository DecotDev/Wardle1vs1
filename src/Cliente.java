// Cliente.java (Corregido)
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
public class Cliente {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Scanner scanner;

    public Cliente(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        scanner = new Scanner(System.in);
    }

    public void jugar() throws IOException, ClassNotFoundException {
        System.out.println((String) in.readObject()); // Mensaje inicial

        while (true) {
            String mensaje = (String) in.readObject();
            System.out.println(mensaje);

            if (mensaje.contains("Elige una palabra") || mensaje.contains("Es tu turno")) {
                String input = scanner.nextLine();
                out.writeObject(input);
                out.flush();
            } else if (mensaje.contains("¡Has ganado!")) {
                break;
            }
        }
        cerrar();
    }

    private void cerrar() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Cliente cliente = new Cliente("localhost", 5225);
        cliente.jugar();
    }
}