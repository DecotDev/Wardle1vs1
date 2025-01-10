public class Cliente {
    Game partida;
    String palabra;
    String respuesta="";

    public Cliente(String palabra) {
        this.palabra = palabra;
        for(int i =0; i<palabra.length();i++){
            respuesta+="x";
        }
    }


    public String comprobar(String guess){


        char[] divG = guess.toCharArray();
        char[] divP = palabra.toCharArray();
        char[] divR = respuesta.toCharArray();

        for (int i =0; i < divG.length; i++){

            if(divR[i]=='x'){
                if (divG[i]==divP[i]){
                    divR[i]='o';
                } else if(existe(divG[i], divP)){
                    divR[i]='-';
                }
            } else {
                if (divG[i]!=divP[i]){
                    divR[i]='x';
                } else if(!existe(divG[i], divP)){
                    divR[i]='x';
                }
                if (divG[i]==divP[i]){
                    divR[i]='o';
                } else if(existe(divG[i], divP)){
                    divR[i]='-';
                }
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
