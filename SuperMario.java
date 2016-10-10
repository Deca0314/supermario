/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supermario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author keigo
 */
public class SuperMario extends JPanel implements KeyListener, Runnable{
    static Image back;
    static Image mario;
    int mariox = 100, marioy = 370;
    boolean isjumping;
    boolean righton;
    boolean lefton;

    /**
     * @param args the command line arguments
     */
    
    public SuperMario(){
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
    }
    
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(back, 0, 0, (int)(back.getWidth(null) * 0.5), (int)(back.getHeight(null) * 0.5), null);
        g.drawImage(mario, mariox, marioy, (int)(mario.getWidth(null) * 0.25), (int)(mario.getHeight(null) * 0.25), null);
    }
    
    public void keyPressed(KeyEvent e){
        int k = e.getKeyCode();
        System.out.println("push");
        if(k == KeyEvent.VK_RIGHT){
            righton = true;
            mariox = mariox + 10;
        }else if(k == KeyEvent.VK_LEFT){
            lefton = true;
            mariox = mariox - 10;
        }else if(k == KeyEvent.VK_UP){
            Thread t = new Thread(this);
            t.start();
        }
        repaint();
    }
    
    public void jump(){
        int x = 0;
        if(righton){
            x = 5;
        }else if(lefton){
            x = -5;
        }
        for(int i = 0; i < 10; i++){
            mariox = mariox + x;
            marioy = marioy - 10;
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex){
                Logger.getLogger(SuperMario.class.getName()).log(Level.SEVERE, null, ex);
            }
            repaint();
        }
        for(int i = 0; i < 10; i++){
            mariox = mariox + x;
            marioy = marioy + 10;
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Logger.getLogger(SuperMario.class.getName()).log(Level.SEVERE, null, ex);
            }
            repaint();
        }
    }
    
    public void keyReleased(KeyEvent e){
        int k = e.getKeyCode();
        if(k == KeyEvent.VK_RIGHT){
            righton = false;
        }else if(k == KeyEvent.VK_LEFT){
            lefton = false;
        }
    }
    
    public void keyTyped(KeyEvent e){}
    
    public void run(){
        if(!isjumping){
            isjumping = true;
            this.jump();
            isjumping = false;
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("SuperMario");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            back = ImageIO.read(new File(".\\image\\slack-imgs.png"));
            mario = ImageIO.read(new File(".\\image\\mario.png"));
        } catch (IOException ex) {
            System.out.println("ファイルが読み込めません");
        }
        SuperMario m = new SuperMario();
        frame.add(m, BorderLayout.CENTER);
        int width = (int)(back.getWidth(null) * 0.5);
        int height = (int)(back.getHeight(null) * 0.5);
        frame.setSize(width, height);
        frame.setVisible(true);
    }
    
}
