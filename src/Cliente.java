import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class Cliente {
    Game partida;
    String palabra;
    String respuesta="";
    char correct = 'O', existent = '?', wrong = '-';




    public Cliente(String palabra) throws FileNotFoundException {
        this.palabra = palabra;
        for(int i =0; i<palabra.length();i++){
            respuesta += wrong;
        }
    }



    public String comprobar(String guess){


        char[] divG = guess.toCharArray();
        char[] divP = palabra.toCharArray();
        char[] divR = respuesta.toCharArray();

        for (int i =0; i < divG.length; i++){

            if (divR[i] != wrong) {
                if (divG[i] != divP[i]) {
                    divR[i] = wrong;
                } else if (!existe(divG[i], divP)) {
                    divR[i] = wrong;
                }
            }
            if (divG[i]==divP[i]){
                divR[i]=correct;
            } else if(existe(divG[i], divP)){
                divR[i]=existent;
            }
        }


        respuesta = String.valueOf(divR);
        return respuesta;
    }

    public boolean existe(char a, char[] charArr){
        int found = -1;
        for (int i = 0; i < charArr.length; ++i) {
            if (charArr[i] == a) {
                found = i;
                break;
            }
        }

        return found!=-1;
    }

    public boolean victoria(String guess){
        String comp ="";
        for(int i =0; i<palabra.length();i++){
            comp+=correct;
        }
        return (Objects.equals(comprobar(guess), comp));
    }

    public Game getPartida() {
        return partida;
    }

    public void setPartida(Game partida) {
        this.partida = partida;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }
}
