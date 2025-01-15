import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        Cliente a = new Cliente();
        Game b = new Game();
        b.jugar(a);
    }
}
