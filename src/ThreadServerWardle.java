import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ThreadServerWardle extends Thread{
    private Socket clientSocket = null;
    private InputStream in = null;
    private OutputStream out = null;


    public ThreadServerWardle(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
    }
}
