/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supermario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author keigo
 */
public class SuperMario extends JPanel implements KeyListener, Runnable, ActionListener{
    static Image back;
    int backwidth, backheight;
    static Image mario;
    int mariowidth, marioheight;
    static Image bakudaniwa;
    int bakudaniwawidth, bakudaniwaheight;
    int mariox , marioy = 370;
    ArrayList<int[]> bakudaniwaxy = new ArrayList<int[]>();
    ArrayList<int[]> bakudaniwasidexy = new ArrayList<int[]>();
    boolean isjumping;
    boolean righton;
    boolean lefton;
    boolean upon;
    boolean jumpdown;
    boolean gameover;
    Integer bakudaniwacount = 0;
    JButton startbutton;

    /**
     * @param args the command line arguments
     */
    
    public SuperMario(){
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        startbutton = new JButton("スタート");
        startbutton.addActionListener(this);
        setLayout(null);
        backwidth = (int)(back.getWidth(null) * 0.5);
        backheight = (int)(back.getHeight(null) * 0.5);
        mariowidth = (int)(mario.getWidth(null) * 0.25);
        marioheight = (int)(mario.getHeight(null) * 0.25);
        bakudaniwawidth = bakudaniwa.getWidth(null);
        bakudaniwaheight = bakudaniwa.getHeight(null);
        mariox = backwidth / 2 - mariowidth / 2;
        startbutton.setBounds(backwidth / 2 - 150, backheight / 2 - 100, 300, 100);
        add(startbutton);
    }
    
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(back, 0, 0, backwidth, backheight, null);
        g.drawImage(mario, mariox, marioy, mariowidth, marioheight, null);
        for(int i = 0; i < bakudaniwaxy.size(); i++){
            g.drawImage(bakudaniwa, bakudaniwaxy.get(i)[0], bakudaniwaxy.get(i)[1], bakudaniwawidth, bakudaniwaheight, null);
        }
        for(int i = 0; i < bakudaniwasidexy.size(); i++){
            g.drawImage(bakudaniwa, bakudaniwasidexy.get(i)[1], bakudaniwasidexy.get(i)[2], bakudaniwawidth, bakudaniwaheight, null);
        }
        Graphics2D g2 = (Graphics2D)g;
        Font countfont = new Font("meiryo", Font.BOLD, 50);
        g2.setFont(countfont);
        g2.drawString(bakudaniwacount.toString(), 100, 100);
        if(gameover){
            Font font = new Font("meiryo", Font.BOLD, 100);
            g2.setColor(Color.red);
            g2.setFont(font);
            g2.drawString("GAME OVER", 160, backheight / 2);
        }
    }
    
    public void keyPressed(KeyEvent e){
        int k = e.getKeyCode();
        if(k == KeyEvent.VK_RIGHT){
            righton = true;
        }else if(k == KeyEvent.VK_LEFT){
            lefton = true;
        }else if(k == KeyEvent.VK_UP){
            upon = true;
        }
    }
    
    
    public void keyReleased(KeyEvent e){
        int k = e.getKeyCode();
        if(k == KeyEvent.VK_RIGHT){
            righton = false;
        }else if(k == KeyEvent.VK_LEFT){
            lefton = false;
        }else if(k == KeyEvent.VK_UP){
            upon = false;
        }
    }
    
    public void keyTyped(KeyEvent e){}
    
    public void run(){
        try {
            this.gamestart();
        } catch (Exception ex) {
            gameover = true;
            System.out.println("Gmae Over");
        }
    }
    
    public void actionPerformed(ActionEvent e){
        Thread t = new Thread(this);
        t.start();
        startbutton.setVisible(false);
        this.setFocusable(true);
    }
    
    public void bakudaniwaborn(){
        int[] newbakudaniwa = new int[2];
        newbakudaniwa[0] = (int)(Math.random() * backwidth);
        newbakudaniwa[1] = 0;
        bakudaniwaxy.add(newbakudaniwa);
    }
    
    public void bakudaniwasideborn(){
        int[] newbakudaniwaside = new int[3];
        newbakudaniwaside[0] = (int)(Math.random() * 2);
        if(newbakudaniwaside[0] == 0){
            newbakudaniwaside[1] = 0;
        }else{
            newbakudaniwaside[1] = backwidth;
        }
        newbakudaniwaside[2] = 370;
        bakudaniwasidexy.add(newbakudaniwaside);
    }
    
    public void bakudaniwacrash(int bakux, int bakuy) throws Exception{
        int bakuleft, bakuright, bakutop, bakuunder;
        int marioleft, marioright, mariotop, mariounder;
        bakuleft = bakux + 10;
        bakuright = bakuleft + bakudaniwawidth - 10;
        bakutop = bakuy + 10;
        bakuunder = bakutop + bakudaniwaheight - 10;
        marioleft = mariox + 10;
        marioright = marioleft + mariowidth - 10;
        mariotop = marioy + 10;
        mariounder = mariotop + marioheight - 10;
        if(bakuright > marioleft && bakuleft < marioright && bakuunder > mariotop && bakutop < mariounder){
            throw new Exception("Game Over");
        }
    }
    
    public void gamestart() throws Exception{
        while(!gameover){
            if(righton){
                mariox = mariox + 20;
                if(mariox + (int)(mario.getWidth(null) * 0.25) > (int)(back.getWidth(null) * 0.5)){
                    mariox = (int)(back.getWidth(null) * 0.5) - (int)(mario.getWidth(null) * 0.3);
                }
            }else if(lefton){
                mariox = mariox - 20;
                if(mariox < 0){
                    mariox = 0;
                }
            }
            if(upon){
                if(!isjumping){
                    isjumping = true;
                }
            }
            if(isjumping){
                if(!jumpdown){
                    marioy = marioy - 30;
                    if(marioy == 220){
                        jumpdown = true;
                    }
                }else{
                    marioy = marioy + 30;
                    if(marioy == 370){
                        isjumping = false;
                        jumpdown = false;
                    }
                }
            }
            if((int)(Math.random() * 10) == 0){
                this.bakudaniwaborn();
            }
            if((int)(Math.random() * 30) == 0){
                this.bakudaniwasideborn();
            }
            repaint();
            for(int i = 0; i < bakudaniwaxy.size(); i++){
                bakudaniwaxy.get(i)[1] = bakudaniwaxy.get(i)[1] + 10;
                if(bakudaniwaxy.get(i)[1] > backheight){
                    bakudaniwaxy.remove(i);
                    bakudaniwacount += 1;
                }
            }
            for(int i = 0; i < bakudaniwaxy.size(); i++){
                this.bakudaniwacrash(bakudaniwaxy.get(i)[0], bakudaniwaxy.get(i)[1]);
            }
            for(int i = 0; i < bakudaniwasidexy.size(); i++){
                if(bakudaniwasidexy.get(i)[0] == 0){
                    bakudaniwasidexy.get(i)[1] = bakudaniwasidexy.get(i)[1] + 20;
                    if(bakudaniwasidexy.get(i)[1] > backwidth){
                        bakudaniwasidexy.remove(i);
                        bakudaniwacount += 1;
                    }
                }else{
                    bakudaniwasidexy.get(i)[1] = bakudaniwasidexy.get(i)[1] - 20;
                    if(bakudaniwasidexy.get(i)[1] < 0){
                        bakudaniwasidexy.remove(i);
                        bakudaniwacount += 1;
                    }
                }
            }
            for(int i = 0; i < bakudaniwasidexy.size(); i++){
                this.bakudaniwacrash(bakudaniwasidexy.get(i)[1], bakudaniwasidexy.get(i)[2]);
            }
            try {
                Thread.sleep(64);
            } catch (InterruptedException ex) {
                Logger.getLogger(SuperMario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("SuperMario");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            Path backpath = Paths.get("image", "slack-imgs.png");
            back = ImageIO.read(new File(backpath.toString()));
            Path mariopath = Paths.get("image", "mario.png");
            mario = ImageIO.read(new File(mariopath.toString()));
            Path bakudaniwapath = Paths.get("image", "27bakudaniwa.gif");
            bakudaniwa = ImageIO.read(new File(bakudaniwapath.toString()));
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
