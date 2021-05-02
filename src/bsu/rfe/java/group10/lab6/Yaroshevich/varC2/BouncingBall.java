package bsu.rfe.java.group10.lab6.Yaroshevich.varC2;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Date;

public class BouncingBall implements Runnable {

    // Максимальный и минимальный размер мяча
    private static  final int MAX_RADIUS = 40;
    private static final int MIN_RADIUS = 3;
    // Максимальная скорость мяча
    private static final int MAX_SPEED = 15;

    private Field field;
    private int radius;
    private Color color;
    float time =  new Date().getTime()%1000000;
    // текущие координаты мяча
    private double x;
    private double y;

    // текущий поток
    private Thread thisThread = new Thread(this);
    // Скорость и её компоненты
    private int speed;
    private double speedX;
    private double speedY;

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public int getSpeed() {
        return speed;
    }

    public Thread getThisThread() {
        return thisThread;
    }

    public int getRadius() {
        return radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public BouncingBall(Field field){
        this.field = field;
        radius = new Double(Math.random()*(MAX_RADIUS - MIN_RADIUS)).intValue() + MIN_RADIUS;
        speed = new Double(Math.round(5*MAX_SPEED/ radius)).intValue();
        if(speed>MAX_SPEED){
            speed = MAX_SPEED;
        }
        // Начальное направление от нуля до 2pi
        double angel = Math.random()*2*Math.PI;
        // Компоненты скорости
        speedX = 3*Math.cos(angel);
        speedY = 3*Math.sin(angel);
        // Случайные цвет мяча
        color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
        x = Math.random()*(field.getSize().getWidth() - 2*radius) + radius;
        y = Math.random()*(field.getSize().getHeight() - 2*radius) + radius;
        // Созаём новый экземпляр потока, передавая аргументом ссылку на класс,
        // реализующий Runnable (т.е. на себя)
        thisThread.start();
    }

    // Метод прорисовки себя
    public void paint(Graphics2D canvas){
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x-radius, y-radius, 2*radius,2*radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }

    // Метод run исполняется внутри потока.
    // И когда он завершает работу, то завершается и поток
    public void run(){
        try{
            // Запускаем бесконечный цикл, т.к. шары всё время в движении пока поток не прервётся
            while(true){
                // Синхронизация потоков делается на самом объекте поля
                // Если движении разрешено - управление будет возвращено
                // В противном случае - активный поток заснёт
                field.canMove(this);

                Point speedV = new Point(field.intersectionSpeed(this));
                boolean flag =false;
                System.out.println(new Date().getTime()%1000000 - time);
                if(speedV.getX()!=0 && speedV.getY()!=0 && new Date().getTime()%1000000 - time>500){
                    double gipot = Math.sqrt(Math.pow(speedV.getX(),2)+Math.pow(speedV.getY(),2));
                    double cos = speedV.getX()/gipot;
                    double sin = speedV.getY()/gipot;
                    if(Math.abs(cos)>0.8) cos = 0.8;
                    if(Math.abs(sin)<0.2) sin =0.2;
                    speedX=3*cos;
                    speedY=3*sin;
                    speed=(int)Math.sqrt(Math.pow(speedV.getX()/speedX,2)+Math.pow(speedV.getY()/speedY,2)/2);
                    flag=true;
                    System.out.println(time-new Date().getTime()%100000);
                    time=new Date().getTime()%100000;
                }
                if(speed>15) speed=15;
                if(x + speedX <= radius){
                    // Достигли левой стенки, отскакиваем в право
                    speedX = -speedX;
                    x = radius;
                }else if (x + speedX >= field.getWidth() - radius){
                    // Достигли правой стенки, отскок влево
                    speedX = -speedX;
                    x = new Double(field.getWidth()-radius).intValue();
                }else if (y + speedY <= radius){
                    // Достигли верхней стенки
                    speedY = -speedY;
                    y = radius;
                }else if (y + speedY >= field.getHeight() - radius){
                    // Достигли нижней стенки
                    speedY = - speedY;
                    y = new Double(field.getHeight()-radius).intValue();
                }else {
                    if(flag){
                        x += speedX;
                        y += speedY;
                    }
                    x += speedX;
                    y += speedY;
                }
                // Засыпаем взависимости от нашей скорости
                thisThread.sleep(16-speed);

            }
        }catch (InterruptedException ex){

        }
    }
}
