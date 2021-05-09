package bsu.rfe.java.group10.lab6.Yaroshevich.varC2;

import java.awt.*;
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
    private boolean intersection = false;
    // Класс таймер отвечает за регулярную генерацию события типа ActionListener

    private Timer repaintTimer = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });

    public Field(){
        setBackground(Color.WHITE);
        //for(int i=0;i<5;i++) addBall();
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
    public synchronized Point intersectionSpeed(BouncingBall ball1) throws InterruptedException{
        int indexOfCurrentBall = balls.indexOf(ball1);
        Point speed = new Point(0,0);
        for(int i =0;i<balls.size();i++){
            if(i==indexOfCurrentBall) continue;
            BouncingBall ball2= balls.get(i);
            double length = Math.sqrt(Math.pow(ball1.getX()-ball2.getX(),2)+Math.pow(ball1.getY()-ball2.getY(),2));
            if( ball1.getRadius()+ball2.getRadius() >= length-5 && ball1.getRadius()+ball2.getRadius() <= length+5){
                double kof=((ball1.getX()-ball2.getX())*(ball2.getSpeed()*ball2.getSpeedX() - ball1.getSpeed()*ball1.getSpeedX())
                        +(ball1.getY()-ball2.getY())*(ball2.getSpeed()*ball2.getSpeedY() - ball1.getSpeed()*ball1.getSpeedY()))
                        *(2*Math.pow(ball2.getRadius(),2))/(Math.pow(ball1.getRadius(),2)+Math.pow(ball2.getRadius(),2))
                        /Math.pow(ball1.getRadius()+ball2.getRadius(),2);
                double speedX = (ball1.getX()-ball2.getX())*kof+ball1.getSpeed()*ball1.getSpeedX();
                double speedY = (ball1.getY()-ball2.getY())*kof+ball1.getSpeed()*ball1.getSpeedY();
                speed.setLocation(speedX,speedY);
                intersection=!intersection;
                if(!intersection){
                    ball2.getThisThread().setPriority(Thread.MAX_PRIORITY);
                    intersection=true;
                    wait();
                }else{
                    ball1.getThisThread().setPriority(Thread.NORM_PRIORITY);
                    notifyAll();
                }

                break;
            }
            if( length <=ball1.getRadius()+ball2.getRadius() -6){
                if(ball1.getX()-ball2.getX()>0) ball1.setX(ball1.getX()+10);
                else ball1.setX(ball1.getX()-10);
                if(ball1.getY()-ball2.getY()>0) ball1.setY(ball1.getY()+10);
                else ball1.setY(ball1.getY()-10);
            }

        }
        return speed;
    }
}
