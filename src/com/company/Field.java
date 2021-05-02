package com.company;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Field  extends JPanel {

    // Динамический список скачущих мячей
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(8);
    // Флаг приостановления движения
    private boolean paused;
    // Класс таймер отвечает за регулярную генерацию события типа ActionListener

    private Timer repaintTimer = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });

    public Field(){
        setBackground(Color.WHITE);
        for(int i=0;i<5;i++) addBall();
        repaintTimer.start();
    }

    public int countBalls(){
        return balls.size();
    }

    public void addBall(){
        balls.add(new BouncingBall(this));
    }

    // Унаследованный от JPanel метод перересовки компонента

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        for(BouncingBall ball:balls){
            ball.paint(canvas);
        }
    }

    // synchronized, т.е. только 1 поток может одновременно быть внутри
    public synchronized void pause(){
        paused=true;
    }
    // Проверка может ли мяч двигатся
    public synchronized void canMove (BouncingBall ball) throws InterruptedException{
        if(paused){
            wait();
        }
    }
    public synchronized  void resume(){
        paused = false;
        notifyAll();
    }
}
