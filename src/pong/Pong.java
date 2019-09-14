package pong;

import java.applet.Applet;
import java.awt.*;

//para implementar:
//pontos são feitos somente ao bater nas bordas -> feito
//mudar cor dos batedores -> feito
//aumentar velocidade padrão dos batedor -> feito

//aumento da velocidade da bola a cada 10 pontos feitos no jogo (total % 10 == 0)

//aumento do cursor por um tempo se fizer 5 pontos seguidos
//aparecem dois cursores se fizer 10 pontos seguidos
//se forem feitos 20 pontos no jogo aparece mais uma bola no jogo (total 5 % 20 ==0)


public class Pong extends Applet implements Runnable {

    int CursorDireito = 100, CursorEsquerdo = 100;
    int MoveDireita = 0, MoveEsquerda = 0;
    int PosicaoHorizontal = 0, PosicaoVertical = 0;
    int Score1 = 0, Score2 = 0;
    int Seq1 = 0, Seq2 = 0;
    int Speed = 20;
    Thread runner;
    Graphics goff, g;
    Image Imagem;
    Dimension Dimensao;

    @Override
    public void init() {
        setBackground(Color.black);
        setFont(new Font("Helvetica", Font.BOLD, 15));
    }

    @Override
    public void start() {
        Dimensao = size();
        Imagem = createImage(Dimensao.width, Dimensao.height);
        goff = Imagem.getGraphics();
        g = getGraphics();
        if (runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    @Override
    public void stop() {
        if (runner != null) {
            runner.interrupt();
            runner = null;
        }
    }

    @Override
    public void run() {
        int Movimento;     
        Movimento = MoveBolaEDI();
        while (true) {
       
            if (Movimento == 1) {
                Movimento = MoveBolaEDI();
            }
            if (Movimento == 2) {
                Movimento = MoveBolaEDS();
            }
            if (Movimento == 3) {
                Movimento = MoveBolaDEI();
            }
            if (Movimento == 4) {
                Movimento = MoveBolaDES();
            }
        }
    }

    public void repeat(int speed) {
        update(g);
        pause(speed);
    }

    // Move Bola da Esquerda para Diagonal Direita Inferior
    public int MoveBolaEDI() {
        for (;; PosicaoVertical += 2, PosicaoHorizontal += 2) {
            // Se Bater na Borda Inferior
            if (PosicaoVertical > getSize().height - 10) {
                return 2;
            }
            // Se Bater na Borda Direita
            if (PosicaoHorizontal > getSize().width - 10) {
                Score1++;
                return 3;
            }
            // Se Bater no Cursor Direito
            if (PosicaoVertical > CursorDireito - 5
                    && PosicaoVertical < CursorDireito + 31
                    && PosicaoHorizontal > 239 && PosicaoHorizontal < 246) {
                return 3;
            }
            repeat(Speed);
        }
    }

    // Move Bola da Direita para Diagonal Esquerda Inferior
    public int MoveBolaDEI() {
        for (;; PosicaoHorizontal -= 2, PosicaoVertical += 2) {
            // Se Bater na Borda Inferior
            if (PosicaoVertical > getSize().height - 10) {
                return 4;
            }
            // Se Bater na Borda Esquerda
            if (PosicaoHorizontal < 1) {
                Score2++;
                return 1;
            }
            // Se Bater no Cursor Esquerdo
            if (PosicaoVertical > CursorEsquerdo - 5
                    && PosicaoVertical < CursorEsquerdo + 31
                    && PosicaoHorizontal > 49 && PosicaoHorizontal < 56) {       
                return 1;
            }
            repeat(Speed);
        }
    }

    // Move Bola da Esquerda para Diagonal Direita Superior
    public int MoveBolaEDS() {
        for (;; PosicaoHorizontal += 2, PosicaoVertical -= 2) {
            // Se Bater na Borda Superior
            if (PosicaoVertical < 1) {
                return 1;
            }
            // Se Bater na Borda Direita
            if (PosicaoHorizontal > getSize().width - 10) {
                Score1++; Seq1++; Seq2 = 0;
                return 4;
            }
            // Se Bater no Cursor Direito
            if (PosicaoVertical > CursorDireito - 5
                    && PosicaoVertical < CursorDireito + 31
                    && PosicaoHorizontal > 239 && PosicaoHorizontal < 246) {
                return 4;
            }
            repeat(Speed);
        }
    }

    // Move Bola da Direita para Diagonal Esquerda Superior
    public int MoveBolaDES() {
        for (;; PosicaoVertical -= 2, PosicaoHorizontal -= 2) {
            System.out.println(Seq1);
            // Se Bater na Borda Superior
            if (PosicaoVertical < 1) {
                return 3;
            }
            // Se Bater na Borda Esquerda
            if (PosicaoHorizontal < 1) {
                Score2++; Seq2++; Seq1 = 0;      
                return 2;
            }
            // Se Bater no Cursor Esquerdo
            if (PosicaoVertical > CursorEsquerdo - 5
                    && PosicaoVertical < CursorEsquerdo + 31
                    && PosicaoHorizontal > 49 && PosicaoHorizontal < 56) {           
                return 2;
            }
            repeat(Speed);
        }
    }

    void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void update(Graphics g) {
        if ((CursorEsquerdo <= 0 && MoveEsquerda < 0)
                || (CursorEsquerdo >= getSize().height - 30 && MoveEsquerda > 0)) {
            MoveEsquerda = 0;
        }
        if ((CursorDireito <= 0 && MoveDireita < 0)
                || (CursorDireito >= getSize().height - 30 && MoveDireita > 0)) {
            MoveDireita = 0;
        }
        CursorEsquerdo += MoveEsquerda;
        CursorDireito += MoveDireita;
        goff.setColor(Color.black);
        goff.fillRect(0, 0, Dimensao.width, Dimensao.height);
        goff.setColor(Color.green);
        goff.fillRect(50, CursorEsquerdo, 5, 30);
        goff.setColor(Color.red);
        goff.fillRect(250, CursorDireito, 5, 30);
        goff.setColor(Color.yellow);
        goff.fillOval(PosicaoHorizontal, PosicaoVertical, 10, 10);
        goff.setColor(Color.white);
        goff.drawString("Player A: " + Score1, 10, 15);
        goff.drawString("Player B: " + Score2, 200, 15);
        g.drawImage(Imagem, 0, 0, this);
    }

    @Override
    public boolean keyDown(Event e, int key) {
        if (key == Event.DOWN) {
            MoveEsquerda = 5;
        }
        if (key == Event.UP) {
            MoveEsquerda = -5;
        }
        if (key == Event.LEFT) {
            MoveDireita = 5;
        }
        if (key == Event.RIGHT) {
            MoveDireita = -5;
        }
        return true;
    }

    @Override
    public boolean keyUp(Event e, int key) {
        if (key == Event.DOWN || key == Event.UP) {
            MoveEsquerda = 0;
        }
        if (key == Event.LEFT || key == Event.RIGHT) {
            MoveDireita = 0;
        }
        return true;
    }
}
