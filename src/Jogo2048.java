import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

public class Jogo2048{

    public static void main(String args[]) {
        boolean inicia = false;

        int flag=0;

        Terminal term;
        GameLog g = new GameLog();
        VerEstado v = new VerEstado();

        int width = 100, height = 30;
        term = TerminalFacade.createSwingTerminal(width,height);
        term.enterPrivateMode();
        term.setCursorVisible(false);
        term.applySGR(Terminal.SGR.ENTER_BOLD);

        desenhaBorda(term);
        desenhaMenu(term);

        GameLog anterior = new GameLog();
        GameLog anterior2=new GameLog();
        copiar(g,anterior,anterior2);

        while(true){
            Key k = term.readInput();

            if (v.getEstado() == Estado.CONTINUA){
                if (k != null) {
                    if (k.getKind() == Key.Kind.Escape) {
                        term.exitPrivateMode();
                        return;
                    }

                    if(!inicia) {
                        if (k.getKind() == Key.Kind.Enter) {
                            desenhaL(65, 26, "                      ", term);
                            inicia = true;
                            g.addNovo();
                            g.addNovo();
                            actHighscore(g, term);
                            play(g,anterior, term, 0);
                        }
                    }

                    if (inicia){

                        switch (k.getKind()) {
                            case ArrowLeft:
                                flag++;
                                copiar(g, anterior,anterior2);
                                g.pushLeft();
                                inputSeta(g, anterior,anterior2, v, term);

                                break;

                            case ArrowRight:
                                flag++;
                                copiar(g, anterior,anterior2);
                                g.pushRight();

                                inputSeta(g, anterior,anterior2, v, term);                                break;

                            case ArrowDown:
                                flag++;
                                copiar(g, anterior,anterior2);
                                g.pushDown();

                                inputSeta(g, anterior,anterior2, v, term);                                break;

                            case ArrowUp:
                                flag++;
                                copiar(g, anterior,anterior2);
                                g.pushUp();

                                inputSeta(g, anterior,anterior2, v, term);                                break;

                            case Backspace:
                                if(flag>0)
                                {
                                copiar(anterior, g,anterior2);
                                Undo(g, term, v);
                                actScoreJogada(g, term, 1);}
                                break;

                            case NormalKey:
                                if (Character.valueOf('r').equals(k.getCharacter()) || Character.valueOf('R').equals(k.getCharacter())) {
                                    //copiar(g, anterior);
                                    flag=0;
                                    v.estado=Estado.CONTINUA;
                                    play(g,anterior, term, 1);

                                }
                                break;

                        }
                    }
                }
            }

            else if(v.getEstado() == Estado.VITORIA){

                desenhaL(60,19,"      __            __     ",term);
                desenhaL(60,20,"\\ / |  ||  |  |  ||  ||\\ |",term);
                desenhaL(60,21,"|  |__||__|  |/\\||__|| \\|",term);

                k=term.readInput();


                if (k != null) {
                    if (k.getKind() == Key.Kind.Escape) {
                        term.exitPrivateMode();
                        return;
                    }

                    if (k.getKind() == Key.Kind.NormalKey) {
                        if (Character.valueOf('r').equals(k.getCharacter()) || Character.valueOf('R').equals(k.getCharacter())) {
                            v.estado = Estado.CONTINUA;
                            play(g,anterior, term, 1);
                        }
                    }
                }
                flag=0;
            }

            else if(v.getEstado() == Estado.DERROTA){
                desenhaL(60,19," __  __      ___   __      ___ __ ",term);
                desenhaL(60,20,"[ __[__]|\\/|[__   |  |\\  /[__ [__)",term);
                desenhaL(60,21,"[__/|  ||  |[___  |__| \\/ [___|  \\",term);

                k=term.readInput();


                if (k != null) {
                    if (k.getKind() == Key.Kind.Escape) {
                        term.exitPrivateMode();
                        return;
                    }

                    if (k.getKind() == Key.Kind.NormalKey) {
                        if (Character.valueOf('r').equals(k.getCharacter()) || Character.valueOf('R').equals(k.getCharacter())) {
                            v.estado = Estado.CONTINUA;
                            play(g,anterior, term, 1);
                        }
                    }
                }
                flag=0;
            }
        }
    }

    public static void copiar2(GameLog anterior,GameLog anterior2)
    {
        for(int i=0;i<4;i++)
            System.arraycopy(anterior2.tab[i], 0, anterior.tab[i], 0, 4);
    }

    public static void copiar(GameLog g,GameLog anterior,GameLog anterior2){

        for(int i=0;i<4;i++)
            System.arraycopy(anterior.tab[i], 0, anterior2.tab[i], 0, 4);
        for(int i=0;i<4;i++)
            System.arraycopy(g.tab[i], 0, anterior.tab[i], 0, 4);
        anterior.score=g.score;
    }

    public static boolean compararMatrizes(int tab[][],int tab_anterior[][]){
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                if(tab[i][j]!=tab_anterior[i][j]) return false;
            }
        }
        return true;
    }

    public static void inputSeta(GameLog g,GameLog anterior,GameLog anterior2, VerEstado v, Terminal term){
        g.imprime();
        if(!compararMatrizes(g.tab, anterior.tab))g.addNovo();
        if(compararMatrizes(g.tab,anterior.tab))
        {
            copiar2(anterior,anterior2);
        }
        grelha(term, g.tab);
        g.imprime();
        actScore(g, term);
        actScoreJogada(g, term, 0);
        v.verifcEstado(g);
    }

    public static void Undo(GameLog g, Terminal term, VerEstado v){
        g.imprime();
        grelha(term, g.tab);
        g.imprime();
        actScore(g, term);
        v.verifcEstado(g);
    }

    public static void play(GameLog g,GameLog anterior, Terminal term,int reinicia){
        term.clearScreen();
        logo1(term);
        desenhaBorda(term);

        if(reinicia==1){
            for(int i=0;i<4;i++)
                for(int j=0;j<4;j++)
                    g.tab[i][j]=0;
            for(int i=0;i<4;i++)
                for(int j=0;j<4;j++)
                    anterior.tab[i][j]=0;

            g.addNovo();
            g.addNovo();
            actHighscore(g, term);
            g.score=0;
        }

        term.applyForegroundColor(17,255,0);
        desenhaM(65, 25, "Press < R > to restart", term);
        desenhaM(65, 26, "Press < BACK SPACE > to undo", term);
        desenhaM(65, 27, "Press < ESC > to exit", term);
        actHighscore(g, term);
        grelha(term, g.tab);
        actScore(g, term);
    }

    public static void actScore(GameLog g, Terminal term){
        int score = g.getScore();
        term.applyForegroundColor(255,255,0);
        String text = "Score: ";
        String s = "" + score;
        imprime(text, s, term);
    }

    public static void actScoreJogada(GameLog g, Terminal term, int tipo){
        int scoreJogada = g.getScoreJogada();
        term.applyForegroundColor(255,0,0);
        String text;

        if(tipo==0)
            text = "+  ";

        else
            text = "-  ";

        String s = "" + scoreJogada;
        term.applySGR(Terminal.SGR.ENTER_BLINK);
        imprime(text, s, term);
        term.applySGR(Terminal.SGR.EXIT_BLINK);
    }

    public static void actHighscore(GameLog g, Terminal term){
        int highscore = g.getHighscore();
        term.applyForegroundColor(255,255,0);
        String text = "Best:  ";
        String s = "" + highscore;
        imprime(text, s, term);
    }

    public static void logo1(Terminal term){
        desenhaL(60,2,"██████╗  ██████╗ ██╗  ██╗ █████╗ ", term);
        desenhaL(60,3,"╚════██╗██╔═████╗██║  ██║██╔══██╗", term);
        desenhaL(60,4," █████╔╝██║██╔██║███████║╚█████╔╝", term);
        desenhaL(60,5,"██╔═══╝ ████╔╝██║╚════██║██╔══██╗", term);
        desenhaL(60,6,"███████╗╚██████╔╝     ██║╚█████╔╝", term);
        desenhaL(60,7,"╚══════╝ ╚═════╝      ╚═╝ ╚════╝ ", term);
    }

    public static void grelha(Terminal term, int tab[][]){
        int k;
        for(int i=0; i<4;i++){
            k=i;
            for(int j=0; j<4;j++){
                if(tab[i][j]==0){
                    term.applyForegroundColor(255,255,255);
                    desenha(i,j, k, " .---------. ", term);
                    desenha(i+1, j, k, "| .-------. |", term);
                    desenha(i+2, j, k, "| |       | |", term);
                    desenha(i+3, j, k, "| |       | |", term);
                    desenha(i+4, j, k, "| |       | |", term);
                    desenha(i+5, j, k, "| '-------' |", term);
                    desenha(i+6, j, k, " '---------' ", term);
                }

                else if(tab[i][j]==2){
                    term.applyForegroundColor(128,128,128);
                    desenha(i,j, k, " .---------. ", term);
                    desenha(i+1, j, k, "| .-------. |", term);
                    desenha(i+2, j, k, "| |       | |", term);
                    desenha(i+3, j, k, "| |   2   | |", term);
                    desenha(i+4, j, k, "| |       | |", term);
                    desenha(i+5, j, k, "| '-------' |", term);
                    desenha(i+6, j, k, " '---------' ", term);
                }

                else if(tab[i][j]==4){
                    term.applyForegroundColor(102,102,255);
                    desenha(i,j, k, " .---------. ", term);
                    desenha(i+1, j, k, "| .-------. |", term);
                    desenha(i+2, j, k, "| |       | |", term);
                    desenha(i+3, j, k, "| |   4   | |", term);
                    desenha(i+4, j, k, "| |       | |", term);
                    desenha(i+5, j, k, "| '-------' |", term);
                    desenha(i+6, j, k, " '---------' ", term);
                }

                else if(tab[i][j]==8){
                    term.applyForegroundColor(178,102,255);
                    desenha(i,j, k, " .---------. ", term);
                    desenha(i+1, j, k, "| .-------. |", term);
                    desenha(i+2, j, k, "| |       | |", term);
                    desenha(i+3, j, k, "| |   8   | |", term);
                    desenha(i+4, j, k, "| |       | |", term);
                    desenha(i+5, j, k, "| '-------' |", term);
                    desenha(i+6, j, k, " '---------' ", term);
                }

                else if(tab[i][j]==16){
                    term.applyForegroundColor(51,153,255);
                    desenha(i,j, k, " .---------. ", term);
                    desenha(i+1, j, k, "| .-------. |", term);
                    desenha(i+2, j, k, "| |       | |", term);
                    desenha(i+3, j, k, "| |  16   | |", term);
                    desenha(i+4, j, k, "| |       | |", term);
                    desenha(i+5, j, k, "| '-------' |", term);
                    desenha(i+6, j, k, " '---------' ", term);
                }

                else if(tab[i][j]==32){
                    term.applyForegroundColor(102,255,255);
                    desenha(i,j, k, " .---------. ", term);
                    desenha(i+1, j, k, "| .-------. |", term);
                    desenha(i+2, j, k, "| |       | |", term);
                    desenha(i+3, j, k, "| |  32   | |", term);
                    desenha(i+4, j, k, "| |       | |", term);
                    desenha(i+5, j, k, "| '-------' |", term);
                    desenha(i+6, j, k, " '---------' ", term);
                }
                else if(tab[i][j]==64){
                    term.applyForegroundColor(51,255,51);
                    desenha(i,j, k, " .---------. ", term);
                    desenha(i+1, j, k, "| .-------. |", term);
                    desenha(i+2, j, k, "| |       | |", term);
                    desenha(i+3, j, k, "| |  64   | |", term);
                    desenha(i+4, j, k, "| |       | |", term);
                    desenha(i+5, j, k, "| '-------' |", term);
                    desenha(i+6, j, k, " '---------' ", term);
                }
                else if(tab[i][j]==128){
                    term.applyForegroundColor(178,255,102);
                    desenha(i,j, k, " .---------. ", term);
                    desenha(i+1, j, k, "| .-------. |", term);
                    desenha(i+2, j, k, "| |       | |", term);
                    desenha(i+3, j, k, "| |  128  | |", term);
                    desenha(i+4, j, k, "| |       | |", term);
                    desenha(i+5, j, k, "| '-------' |", term);
                    desenha(i+6, j, k, " '---------' ", term);
                }
                else if(tab[i][j]==256){
                    term.applyForegroundColor(255, 255,102);
                    desenha(i,j, k, " .---------. ", term);
                    desenha(i+1, j, k, "| .-------. |", term);
                    desenha(i+2, j, k, "| |       | |", term);
                    desenha(i+3, j, k, "| |  256  | |", term);
                    desenha(i+4, j, k, "| |       | |", term);
                    desenha(i+5, j, k, "| '-------' |", term);
                    desenha(i+6, j, k, " '---------' ", term);
                }
                else if(tab[i][j]==512){
                    term.applyForegroundColor(255,153,51);
                    desenha(i,j, k, " .---------. ", term);
                    desenha(i+1, j, k, "| .-------. |", term);
                    desenha(i+2, j, k, "| |       | |", term);
                    desenha(i+3, j, k, "| |  512  | |", term);
                    desenha(i+4, j, k, "| |       | |", term);
                    desenha(i+5, j, k, "| '-------' |", term);
                    desenha(i+6, j, k, " '---------' ", term);
                }
                else if(tab[i][j]==1024){
                    term.applyForegroundColor(255,102,102);
                    desenha(i,j, k, " .---------. ", term);
                    desenha(i+1, j, k, "| .-------. |", term);
                    desenha(i+2, j, k, "| |       | |", term);
                    desenha(i+3, j, k, "| | 1024  | |", term);
                    desenha(i+4, j, k, "| |       | |", term);
                    desenha(i+5, j, k, "| '-------' |", term);
                    desenha(i+6, j, k, " '---------' ", term);
                }
                else if(tab[i][j]==2048){
                    term.applySGR(Terminal.SGR.ENTER_BLINK);
                    term.applyForegroundColor(255,0,0);
                    desenha(i,j, k, " .---------. ", term);
                    desenha(i+1, j, k, "| .-------. |", term);
                    desenha(i+2, j, k, "| |       | |", term);
                    desenha(i+3, j, k, "| | 2048  | |", term);
                    desenha(i+4, j, k, "| |       | |", term);
                    desenha(i+5, j, k, "| '-------' |", term);
                    desenha(i+6, j, k, " '---------' ", term);
                    term.applySGR(Terminal.SGR.ENTER_BLINK);
                }
            }
        }
    }

    public static void desenhaL(int x, int y, String logo, Terminal term){
        term.moveCursor(x, y);

        int len = logo.length();

        for(int i = 0; i < len; i++) {
            if(logo.charAt(i)=='╔' || logo.charAt(i)=='╗' || logo.charAt(i)=='╝'|| logo.charAt(i)=='═'|| logo.charAt(i)=='╚'|| logo.charAt(i)=='║')
                term.applyForegroundColor(0,250,154);
            else
                term.applyForegroundColor(124,252,0);
            term.putCharacter(logo.charAt(i));
        }
    }

    public static void desenhaM(int x, int y, String logo, Terminal term){
        term.moveCursor(x, y);

        int len = logo.length();

        for(int i = 0; i < len; i++)
            term.putCharacter(logo.charAt(i));
    }

    public static void desenha(int i, int j, int k, String logo, Terminal term){
        //x - len, y - 7
        int len = logo.length();

        int x= 2 + j*len;
        int y= 1 + i+k*6;
        term.moveCursor(x,y);

        for(int l=0; l<len; l++)
            term.putCharacter(logo.charAt(l));
    }

    public static void imprime(String text, String score, Terminal term){
        int x=60, y=10;

        if (text.equals("Best:  "))
            y=13;

        if (text.equals("+  ")){
            x += 7;
            y = 11;
        }

        if(text.equals("-  ")){
            x += 7;
            y=11;
        }

        term.moveCursor(x, y);

        int len = text.length();

        for(int i = 0; i < len; i++)
            term.putCharacter(text.charAt(i));

        x+=len;
        term.moveCursor(x, y);

        term.applySGR(Terminal.SGR.EXIT_BOLD);

        int len2 = score.length();

        for(int i = 0; i < len2; i++)
            term.putCharacter(score.charAt(i));

        String limpa = "    ";

        for(int i=0; i<limpa.length(); i++)
            term.putCharacter(limpa.charAt(i));

        term.applySGR(Terminal.SGR.ENTER_BOLD);
    }

    public static void desenhaMenu(Terminal term){
        logo1(term);
        term.applyForegroundColor(192,192,192);
        term.applySGR(Terminal.SGR.EXIT_BOLD);
        desenhaM(8, 4, "______    _____     ___________       ", term);
        desenhaM(8, 5, "___  /______  /_    __  /___  /______ ", term);
        desenhaM(8, 6, "__  /_  _ \\  __/    _  __/_  __ \\  _ \\", term);
        desenhaM(8, 7, "_  / /  __/ /_      / /_ _  / / /  __/", term);
        desenhaM(8, 8, "/_/  \\___/\\__/      \\__/ /_/ /_/\\___/ ", term);
        desenhaM(7, 9, "                                      ", term);
        desenhaM(7, 10, "                                       ", term);
        desenhaM(7, 11, "_______ ______ _______ ________________ ", term);
        desenhaM(7, 12, "__  __ `/  __ `/_  __ `__ \\  _ \\_  ___/ ", term);
        desenhaM(7, 13, "_  /_/ // /_/ /_  / / / / /  __/(__  )  ", term);
        desenhaM(7, 14, "_\\__, / \\__,_/ /_/ /_/ /_/\\___//____/   ", term);
        desenhaM(7, 15, "/____/                                  ", term);
        desenhaM(7, 16, "                                    ", term);
        desenhaM(9, 17, "______              _____        ", term);
        desenhaM(9, 18, "___  /_____________ ___(_)______ ", term);
        desenhaM(9, 19, "__  __ \\  _ \\_  __ `/_  /__  __ \\", term);
        desenhaM(9, 20, "_  /_/ /  __/  /_/ /_  / _  / / /", term);
        desenhaM(9, 21, " /_.___/\\___/_\\__, / /_/  /_/ /_/ ", term);
        desenhaM(9, 22, "             /____/               ", term);

        term.applySGR(Terminal.SGR.EXIT_BOLD);
        term.applyForegroundColor(255,0,0);
        term.applySGR(Terminal.SGR.ENTER_BLINK);
        desenhaM(65, 26, "Press <ENTER> to start", term);
        term.applySGR(Terminal.SGR.EXIT_BLINK);
        term.applyForegroundColor(17,255,0);
        desenhaM(66, 28, "Press <ESC> to exit", term);
    }

    public static void desenhaBorda(Terminal term){
        //╔ ╗ ╝ ═ ╚ ║

        term.applyForegroundColor(255,255,255);
        desenhaM(0,0, "╔", term);
        desenhaM(100,0, "╗", term);
        desenhaM(0,30, "╚", term);
        desenhaM(100,30, "╝", term);

        for(int i=1; i<29; i++){
            desenhaM(0,i, "║", term);
            desenhaM(100,i, "║", term);
        }

        for(int i=1; i<99; i++){
            desenhaM(i,0, "═", term);
            desenhaM(i,30, "═", term);
        }

    }
}