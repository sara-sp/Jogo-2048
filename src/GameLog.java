import java.util.*;

public class GameLog{

    int tab[][];
    int score, scoreJogada, highscore=0;
    boolean cheio=false;
    Random r = new Random();
    Random r2 = new Random();

    Estado estado;

    public GameLog(){
        tab = new int[4][4];
        score = 0;
        scoreJogada = 0;
        estado = Estado.CONTINUA;
    }

    public int getScore(){
        return score;
    }

    public int getScoreJogada(){
        return scoreJogada;
    }

    public int getHighscore(){
        verfcHighscore();

        return highscore;
    }

    public void imprime(){
        for(int i=0; i<4; i++){
            for(int j=0;j<4;j++)
                System.out.print(tab[i][j] + "\t");
            System.out.println();
        }
        System.out.println();
    }

    public void addNovo() {
        if (!cheio) {
            ArrayList<Integer> vazioX = new ArrayList<>();
            ArrayList<Integer> vazioY = new ArrayList<>();


            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (tab[i][j] == 0) {
                        vazioX.add(i);
                        vazioY.add(j);
                    }
                }
            }


            if(vazioX.size()>0){
                int aux = r.nextInt(vazioX.size());
                int val = r2.nextInt(2);

                if (val == 0)
                    tab[vazioX.get(aux)][vazioY.get(aux)] = 2;
                else
                    tab[vazioX.get(aux)][vazioY.get(aux)] = 4;
            }
        }

    }

    public void pushLeft(){

        System.out.println("Seta para a esquerda...");
        scoreJogada = 0;

        for(int i=0; i<4; i++){
            boolean verifica[] = {false, false, false, false};
            for(int j=1; j<4; j++){
                if(tab[i][j] != 0){
                    int valor = tab[i][j];
                    int y = j-1;

                    while((y>=0) && (tab[i][y]==0)){
                        y--;
                    }

                    if(y==-1){
                        tab[i][j] = 0;
                        tab[i][0] = valor;
                    }

                    else if(tab[i][y] !=valor){
                        tab[i][j] = 0;
                        tab[i][y+1] = valor;
                    }

                    else{
                        if(verifica[y]){
                            tab[i][j] = 0;
                            tab[i][y+1] = valor;
                        }

                        else{
                            tab[i][j] = 0;
                            tab[i][y] *=2;

                            verifica[y] = true;

                            scoreJogada += tab[i][y];
                            score += tab[i][y];
                        }
                    }
                }
            }
        }
    }

    public void pushRight(){

        System.out.println("Seta para a direita...");
        scoreJogada = 0;

        for(int i=0; i<4; i++){
            boolean verifica[] = {false, false, false, false};
            for(int j=2; j>-1; j--){
                if(tab[i][j] != 0){
                    int valor = tab[i][j];
                    int y = j+1;

                    while((y<=3) && (tab[i][y]==0)){
                        y++;
                    }

                    if(y==4){
                        tab[i][j] = 0;
                        tab[i][3] = valor;
                    }

                    else if(tab[i][y] !=valor){
                        tab[i][j] = 0;
                        tab[i][y-1] = valor;
                    }

                    else{
                        if(verifica[y]){
                            tab[i][j] = 0;
                            tab[i][y-1] = valor;
                        }
                        else{
                            tab[i][j] = 0;
                            tab[i][y] *=2;

                            verifica[y] = true;

                            scoreJogada += tab[i][y];
                            score += tab[i][y];
                        }
                    }
                }
            }
        }
    }

    public void pushDown(){

        System.out.println("Seta para baixo...");
        scoreJogada = 0;

        for(int i=0; i<4; i++){
            boolean verifica[] = {false, false, false, false};
            for(int j=2; j>-1; j--){
                if(tab[j][i] != 0){
                    int valor = tab[j][i];
                    int x = j+1;

                    while((x<=3) && (tab[x][i]==0)){
                        x++;
                    }

                    if(x==4){
                        tab[j][i] = 0;
                        tab[3][i] = valor;
                    }

                    else if(tab[x][i] !=valor){
                        tab[j][i] = 0;
                        tab[x-1][i] = valor;
                    }

                    else{
                        if(verifica[x]){
                            tab[j][i] = 0;
                            tab[x-1][i] = valor;
                        }
                        else{
                            tab[j][i] = 0;
                            tab[x][i] *=2;

                            verifica[x] = true;

                            scoreJogada += tab[x][i];
                            score += tab[x][i];
                        }
                    }
                }
            }
        }
    }

    public void pushUp(){

        System.out.println("Seta para cima...");
        scoreJogada = 0;

        for(int i=0; i<4;i++){
            boolean verifica[] = {false, false, false, false};
            for(int j=1; j<4;j++){
                if(tab[j][i] != 0){
                    int valor = tab[j][i];
                    int x = j-1;

                    while((x>=0) && (tab[x][i]==0)){
                        x--;
                    }

                    if(x==-1){
                        tab[j][i] = 0;
                        tab[0][i] = valor;
                    }

                    else if(tab[x][i] !=valor){
                        tab[j][i] = 0;
                        tab[x+1][i] = valor;
                    }

                    else{
                        if(verifica[x]){
                            tab[j][i] = 0;
                            tab[x+1][i] = valor;
                        }
                        else{
                            tab[j][i] = 0;
                            tab[x][i] *=2;

                            verifica[x] = true;

                            scoreJogada += tab[x][i];
                            score += tab[x][i];
                        }
                    }
                }
            }
        }
    }

    public int verfcHighscore(){
        if(score>highscore)
            highscore=score;

        return highscore;
    }
}