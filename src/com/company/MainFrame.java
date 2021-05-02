package com.company;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.tools.Tool;

@SuppressWarnings("serial")
public class MainFrame  extends JFrame {

    // Размер дефолтного окна
    private static final int WIDTH =700;
    private static final int HEIGHT = 500;

    // Компоненты меню
    private JMenuItem pauseMenuItem;
    private JMenuItem resumeMenuItem;
    private JMenuItem ballMenuItem;
    // Поле по которому прыгают мячи
    private Field field = new Field();

    public MainFrame(){
        super("Программирование и синхронизация потоков");
        setSize(WIDTH,HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        // Центрирования окна
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
        // Установить начальное состояние окна
        //setExtendedState(MAXIMIZED_BOTH);

        // Создать меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu ballMenu = new JMenu("Мячи");
        Action addBallAction = new  AbstractAction("Добавить мяч"){
            @Override
            public void actionPerformed(ActionEvent event) {
                if(field.countBalls()<8) field.addBall();
                else ballMenuItem.setEnabled(false);
                if(!pauseMenuItem.isEnabled() && !resumeMenuItem.isEnabled()){
                    // Если ни один из пунктов меню не является
                    // Доступным - сделать доступным "Паузу"
                    pauseMenuItem.setEnabled(true);
                }
            }
        };
        menuBar.add(ballMenu);
        ballMenuItem = ballMenu.add(addBallAction);
        ballMenuItem.setEnabled(true);
        JMenu controlMenu = new JMenu("Управление");
        menuBar.add(controlMenu);
        Action pauseAction = new AbstractAction("Приостоновить движение") {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.pause();
                pauseMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseMenuItem = controlMenu.add(pauseAction);
        if (field.countBalls()>0)pauseMenuItem.setEnabled(true);
        else pauseMenuItem.setEnabled(false);
        Action resumeAction = new AbstractAction("Возобновить движение") {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.resume();
                pauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
            }
        };
        resumeMenuItem = controlMenu.add(resumeAction);
        resumeMenuItem.setEnabled(false);

        // Добавляем в центр граничной компановки поле Field
        getContentPane().add(field,BorderLayout.CENTER);
    }
}
