package pong;

import java.applet.Applet;
import java.awt.*;

//para implementar:
//pontos são feitos somente ao bater nas bordas -> feito
//mudar cor dos batedores -> feito
//aumentar velocidade padrão dos batedores -> feito
//aumento da velocidade da bola a cada 5 pontos feitos no jogo -> feito
//controles alterados para w,s, UP, DOWN -> feito
//modo de jogo: lento, medio, rapido -> feito
//tela de game over -> feito
//reinicio de jogo -> feito
//aumento do cursor do jogador que mantiver uma diferença de 5 ou 6 pts -> quase

public class Pong extends Applet implements Runnable {
    
    int CursorDireito = 100, CursorEsquerdo = 100;
    int comprCursorE = 30, comprCursorD = 30;
    int MoveDireita = 0, MoveEsquerda = 0;
    int PosicaoHorizontal = 0, PosicaoVertical = 70;
    int Score1 = 0, Score2 = 0, ScoreTotal = 0, VantagemP1 = 0, VantagemP2 = 0;
    int SleepTime = 18;
    boolean SpeedAltered = true, initialSpeedSet = false, ingame = false;
    Thread runner;
    Graphics goff, g;
    Image Imagem, startImage;
    Dimension Dimensao;
    
    
    @Override
    public void init() 
    {
        setBackground(Color.black);
        setFont(new Font("Ubuntu", Font.BOLD, 15));
    }
    
    //Desenha tela inicial
    @Override
    public void paint(Graphics g) 
    { 
        g.setColor(Color.white); 
  
        g.drawString("PONG", 145, 40);
        g.drawString("VELOCIDADE: ", 120, 70);
        g.drawString("1. LENTO", 130, 90);
        g.drawString("2. MÉDIO", 130, 110);
        g.drawString("3. RÁPIDO", 130, 130);
        g.drawString("PRESSIONE 'I' PARA INICIAR", 75, 160);
        
    } 
    
    public void defSpeed(int ST) {
        if (ST == 1) {
            SleepTime = 25;
            g.setColor(Color.green);
            g.drawString("1. LENTO", 130, 90);
            g.setColor(Color.white);
            g.drawString("2. MÉDIO", 130, 110);
            g.drawString("3. RÁPIDO", 130, 130);
        } else if (ST == 2) {
            SleepTime = 20;
            g.setColor(Color.green);
            g.drawString("2. MÉDIO", 130, 110);
            g.setColor(Color.white);
            g.drawString("1. LENTO", 130, 90);
            g.drawString("3. RÁPIDO", 130, 130);
        } else if (ST == 3) {
            SleepTime = 15;
            g.setColor(Color.green);
            g.drawString("3. RÁPIDO", 130, 130);
            g.setColor(Color.white);
            g.drawString("1. LENTO", 130, 90);
            g.drawString("2. MÉDIO", 130, 110);          
        }
    }
    
    public void finalScore(){
        setBackground(Color.black);
        g.setColor(Color.white);
        g.drawString("PLACAR FINAL", 120, 80);
        g.drawString("TECLE R PARA REINICIAR", 85, 170);
        g.setColor(Color.green);
        g.drawString("PLAYER 1: " + Score1, 20, 120);
        g.setColor(Color.red);
        g.drawString("PLAYER 2: " + Score2, 240, 120);
        
        if (Score1 > Score2) {
            g.setColor(Color.green);
            g.drawString("PLAYER 1 FOI O VENCEDOR", 75, 150);      
        }else if (Score2 > Score1){
            g.setColor(Color.red);
            g.drawString("PLAYER 2 FOI O VENCEDOR", 75, 150);    
        }
        else {
            g.setColor(Color.white);
            g.drawString("EMPATE", 140, 150); 
        }
    }
    public void restart(){
        CursorDireito = 100; CursorEsquerdo = 100;
        comprCursorE = 30; comprCursorD = 30;
        MoveDireita = 0; MoveEsquerda = 0;
        PosicaoHorizontal = 0; PosicaoVertical = 70;
        Score1 = 0; Score2 = 0; ScoreTotal = 0; VantagemP1 = 0; VantagemP2 = 0;
        update(g, comprCursorE, comprCursorD); 
        SleepTime = 18;
        SpeedAltered = true; initialSpeedSet = true; 
        pause(1000);    
        ingame = true;
        runner.resume();
    }
    
    @Override
    public void start()       
    {   
        Dimensao = size();
        Imagem = createImage(Dimensao.width, Dimensao.height);
        goff = Imagem.getGraphics();
        g = getGraphics();
        
        if (runner == null) 
        {
            runner = new Thread(this);
            //runner.start();
        }
    }

    @Override
    public void stop() 
    {   
        if (runner != null) 
        {
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

    public void repeat(int SleepTime, int comprCursorE, int comprCursorD) {
        update(g, comprCursorE, comprCursorD);
        pause(SleepTime);
    }

    // Move Bola da Esquerda para Diagonal Direita Inferior
    public int MoveBolaEDI() {
        for (;; PosicaoVertical += 2, PosicaoHorizontal += 2) 
        {       
            // Se Bater na Borda Inferior      
            if (PosicaoVertical > getSize().height - 10) 
            {
               return 2;
            }  
            // Se Bater na Borda Direita
            if (PosicaoHorizontal > getSize().width - 10) {
                Score1++; ScoreTotal++;
                SpeedAltered = false;
                return 3;
            }
            
            VantagemP1 = Score1 - Score2; // consertar negativo
            //Aumenta cursor se algum dos jogadores possuir uma vantagem de 5 pontos         
            if (VantagemP1 >= 7 || VantagemP1 <= 4)
            {   
                // Se Bater no Cursor Direito
                if (PosicaoVertical > CursorDireito - 10
                    && PosicaoVertical < CursorDireito + comprCursorD
                    && PosicaoHorizontal > 239 && PosicaoHorizontal < 246) 
                {                     
                    comprCursorE = 30;      
                    return 3;
                }
            }
            else if (VantagemP1 > 4 && VantagemP1 < 7) 
            {
                // Se Bater no Cursor Direito
                if (PosicaoVertical > CursorDireito - 10
                    && PosicaoVertical < CursorDireito + comprCursorD
                    && PosicaoHorizontal > 239 && PosicaoHorizontal < 246) 
                {      
                    comprCursorE = 40;
                    return 3;
                }   
            }   
            if ((Score1 + Score2) % 5 == 0 && SpeedAltered == false && SleepTime >= 10) {
                SleepTime = SleepTime - 3;
                SpeedAltered = true;
            }
            repeat(SleepTime, comprCursorE, comprCursorD);
        }
    }

    // Move Bola da Direita para Diagonal Esquerda Inferior
    public int MoveBolaDEI() {
        for (;; PosicaoHorizontal -= 2, PosicaoVertical += 2) {
             // Se Bater na Borda Inferior
            if (PosicaoVertical > getSize().height - 10) 
            {
                return 4;
            }
            // Se Bater na Borda Esquerda
            if (PosicaoHorizontal < 1) {
                Score2++; ScoreTotal++;
                SpeedAltered = false;
                return 1;
            }
            
            VantagemP2 = Score2 - Score1;
            //Aumenta cursor se algum dos jogadores possuir uma vantagem de 5 pontos
            if (VantagemP2 >= 7 || VantagemP2 <= 4)
            {
                // Se Bater no Cursor Esquerdo
                if (PosicaoVertical > CursorEsquerdo - 10
                    && PosicaoVertical < CursorEsquerdo + comprCursorE
                    && PosicaoHorizontal > 49 && PosicaoHorizontal < 56) 
                {
                    comprCursorD = 30;
                    return 1;
                }
            }              
            else if (VantagemP2 > 4 && VantagemP2 < 7)
            { 
                if (PosicaoVertical > CursorEsquerdo - 10
                    && PosicaoVertical < CursorEsquerdo + comprCursorE
                    && PosicaoHorizontal > 49 && PosicaoHorizontal < 56) 
                { 
                    comprCursorD = 40;
                    return 1;
                }
            }
            if ((Score1 + Score2) % 5 == 0 && SpeedAltered == false && SleepTime >= 10) {
                SleepTime = SleepTime - 3;
                SpeedAltered = true;
            }
            repeat(SleepTime, comprCursorE, comprCursorD);
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
                Score1++; ScoreTotal++;
                SpeedAltered = false;
                return 4;
            }
            
            VantagemP1 = Score1 - Score2;
            //Aumenta cursor se algum dos jogadores possuir uma vantagem de 5 pontos
            if (VantagemP1 >= 7 || VantagemP1 <= 4)
            {   
                // Se Bater no Cursor Direito
                if (PosicaoVertical > CursorDireito - 10
                    && PosicaoVertical < CursorDireito + comprCursorD
                    && PosicaoHorizontal > 239 && PosicaoHorizontal < 246) 
                {                     
                    comprCursorE = 30;         
                    return 4;
                }
            }
            else if (VantagemP1 > 4 && VantagemP1 < 7) 
            {
                // Se Bater no Cursor Direito
                if (PosicaoVertical > CursorDireito - 10
                    && PosicaoVertical < CursorDireito + comprCursorD
                    && PosicaoHorizontal > 239 && PosicaoHorizontal < 246) 
                {      
                    comprCursorE = 40;
                    return 4;
                }    
            }
            if ((Score1 + Score2) % 5 == 0 && SpeedAltered == false && SleepTime >= 10) {
                SleepTime = SleepTime - 3;
                SpeedAltered = true;
            }
            repeat(SleepTime, comprCursorE, comprCursorD);
        }
    }

    // Move Bola da Direita para Diagonal Esquerda Superior
    public int MoveBolaDES() {
        for (;; PosicaoVertical -= 2, PosicaoHorizontal -= 2) {
            // Se Bater na Borda Superior
            if (PosicaoVertical < 1) {
                return 3;
            }
            // Se Bater na Borda Esquerda
            if (PosicaoHorizontal < 1) 
            {
                Score2++; ScoreTotal++;
                SpeedAltered = false;
                return 2;
            }
            
            VantagemP2 = Score2 - Score1;
            //Aumenta cursor se algum dos jogadores possuir uma vantagem de 5 pontos
            if (VantagemP2 >= 7 || VantagemP2 <= 4)
            {
               // Se Bater no Cursor Esquerdo 
                if (PosicaoVertical > CursorEsquerdo - 10
                    && PosicaoVertical < CursorEsquerdo + comprCursorE
                    && PosicaoHorizontal > 49 && PosicaoHorizontal < 56) 
                {  
                    comprCursorD = 30;
                    return 2;
                }
            }
            if (VantagemP2 > 4 || VantagemP2 < 7)
            {    
                // Se Bater no Cursor Esquerdo 
                if(PosicaoVertical > CursorEsquerdo - 10
                    && PosicaoVertical < CursorEsquerdo + comprCursorE
                    && PosicaoHorizontal > 49 && PosicaoHorizontal < 56) 
                {
                    comprCursorD = 40;
                    return 2;
                }
            }
            if ((Score1 + Score2) % 5 == 0 && SpeedAltered == false && SleepTime >= 10) 
            {
                SleepTime = SleepTime - 3;
                SpeedAltered = true;
            }
            repeat(SleepTime, comprCursorE, comprCursorD);
        }
    }

    void pause(int time) {
        try {
              Thread.sleep(time);
            } catch (InterruptedException e) {
        }
    }

    //@Override
    public void update(Graphics g, int comprCursorE, int comprCursorD) {
        if ((CursorEsquerdo <= 0 && MoveEsquerda < 0)
                || (CursorEsquerdo >= getSize().height - comprCursorE && MoveEsquerda > 0)) {
            MoveEsquerda = 0;
        }
        if ((CursorDireito <= 0 && MoveDireita < 0)
                || (CursorDireito >= getSize().height - comprCursorD && MoveDireita > 0)) {
            MoveDireita = 0;
        }
        CursorEsquerdo += MoveEsquerda;
        CursorDireito += MoveDireita;
        goff.setColor(Color.black);
        goff.fillRect(0, 0, Dimensao.width, Dimensao.height);
        goff.setColor(Color.green);
        goff.fillRect(50, CursorEsquerdo, 6, comprCursorE);
        goff.setColor(Color.red);
        goff.fillRect(250, CursorDireito, 6, comprCursorD);
        goff.setColor(Color.yellow);
        goff.fillOval(PosicaoHorizontal, PosicaoVertical, 10, 10);
        goff.setColor(Color.white);
        goff.drawString("Player A: " + Score1, 20, 15);
        goff.drawString("Player B: " + Score2, 200, 15);
        g.drawImage(Imagem, 0, 0, this);
    }

    @Override
    public boolean keyDown(Event e, int key) {
        if (key == 's' || key == 'S') {
            MoveEsquerda = 5;
        }
        if (key == 'w' || key == 'W') {
            MoveEsquerda = -5;
        }
        if (key == Event.DOWN) {
            MoveDireita = 5;
        }
        if (key == Event.UP) {
            MoveDireita = -5;
        }
        if (key == Event.ESCAPE){  
            ingame = false;
            runner.suspend();
            pause(500);
            finalScore();  
        }  
        if (key == 'i' || key == 'I') {
            initialSpeedSet = true;
            ingame = true;
            runner.start();
        }  
        if ((key == 'r' || key == 'R') && ingame == false) {
            restart();
        }
        
        if (initialSpeedSet == false)
        {
            if (key == '1') {
            defSpeed(1); 
            }      
            if (key == '2') {
            defSpeed(2);
            }     
            if (key == '3') {
            defSpeed(3);
            }      
        }
            
        
        return true;
    }

    @Override
    public boolean keyUp(Event e, int key) {
        if (key == 'w' || key == 's' || 
            key == 'W' || key == 'S') {
            MoveEsquerda = 0;
        }
        if (key == Event.UP || key == Event.DOWN) {
            MoveDireita = 0;
        }
        return true;
    }
}
