public class VerEstado{

    GameLog g = new GameLog();
    Estado estado;

    VerEstado(){
        estado = Estado.CONTINUA;
    }

    public Estado getEstado(){
        return estado;
    }

    public boolean verifcVit(int tab[][]){
        for(int i=0; i<4;i++){
            for(int j=0;j<4;j++){
                if(tab[i][j]==2048)
                    return true;
            }
        }
        return false;
    }

    public boolean verifcTabCheio(int tab[][]){
        for(int i=0; i<4;i++){
            for(int j=0;j<4;j++){
                if(tab[i][j]==0)
                    return false;
            }
        }
        g.cheio=true;
        return true;
    }

    //verifica se tem algum numero adjacente quando tab cheia
    public boolean verifcAdj(int tab[][]){
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if((j!=0 && tab[i][j]== tab[i][j-1]) || (i!=0 && tab[i][j]==tab[i-1][j]))
                    return true;
            }
        }
        return false;
    }

    //atualizar score em cada movimento
    public Estado verifcEstado(GameLog g){
        if(verifcVit(g.tab))
            estado = Estado.VITORIA;

        else if(verifcTabCheio(g.tab)){
            if(verifcAdj(g.tab))
                estado = Estado.CONTINUA;
            else
                estado = Estado.DERROTA;
        }

        else
            estado = Estado.CONTINUA;

        if(estado==Estado.VITORIA || estado==Estado.DERROTA)
            g.verfcHighscore();

        return estado;
    }
}