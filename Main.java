import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        
        Game b = new Game();
        b.jugar(generarCliente());
    }
    
    public static Cliente generarCliente() throws FileNotFoundException {
        File palabraslist = new File(Main.class.getResource("Palabras.txt").getFile());
        Scanner sc = new Scanner(palabraslist);
        Scanner lector = new Scanner(System.in);
        String enviar ="Choose one of these words:\n";

        String [] palabras = sc.nextLine().split(";");
        
        String[] elecciones = new String[3];
        for (int i = 0; i< elecciones.length;i++){
            elecciones[i] = palabras[(int)(Math.random()*palabras.length)];
            enviar+=(i+1)+"-\t"+elecciones[i]+"\n";
        }
        System.out.println(enviar);
        int a = lector.nextInt();
        try {
            return new Cliente(elecciones[a-1]);
        }catch (Exception e){
            System.out.println("Reading Error");
            return null;
        }
    }
}
