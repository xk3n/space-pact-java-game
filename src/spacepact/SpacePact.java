/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacepact;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author Kim
 */
public class SpacePact implements Runnable{
    JFrame f;
    JPanel p;
    JLabel[] alien;
    JLabel[] shoot = new JLabel[1000];
    JLabel player;
    JLabel bg,earth;
    JLabel score, health;
    Font style;
    boolean fire = true;
    int scoresInt = 0;
    int life = 10;
    int height = 800;
    int width = 900;
    int speed = 1;
    int shipSpeed = 0;
    int p_x = width / 2 - 50;
    int p_y = height - 150;
    
    int[] shoot_x = new int[shoot.length];
    int[] shoot_y = new int[shoot.length];
    int shoot_dir = 5;
    int bullets = 0;
    SpacePact game;
    ImageIcon shootProjectile = new ImageIcon("resource\\shoot.png");
    ImageIcon cometProjectile = new ImageIcon("resource\\glitch_meteor\\meteor1.png");
    float meteorCounter = 1;
    
    float alienCounter = 0;
    int numOfAliens =0;
    Mover mover;
    Shooting shooting = new Shooting();
    Timer sho = new Timer(1000 / 30, shooting);
    ImageIcon ship = new ImageIcon("resource\\ship.gif");
    ImageIcon[] meteor = new ImageIcon[11];

    SpacePact() {
        style = new Font("Helvetica", Font.BOLD, 25);
        for (int i = 0; i < meteor.length; i++) {
            meteor[i] = new ImageIcon("resource\\glitch_meteor\\meteor"+i+".png");
        }
    }

    public static void main(String[] args) {
        SpacePact game = new SpacePact();
        game.run();
    }

    public void move(JLabel shoot, int i) {
        shoot_y[i] -= 5 * 2;
        if (shoot.isVisible()) {
            shoot.setBounds(shoot_x[i], shoot_y[i], shoot.getBounds().width, shoot.getBounds().height);
        }
        f.repaint();
    }

    public void collide(JLabel[] shoot, JLabel[] enemy) {
        Rectangle rPlayer = new Rectangle(player.getBounds().x,player.getBounds().y,player.getBounds().width,player.getBounds().height);
        Rectangle[] p = new Rectangle[this.shoot.length];
        Rectangle[] rAlien = new Rectangle[numOfAliens];
        A: for (int i = 0; i < numOfAliens; i++) {
                rAlien[i] = new Rectangle(enemy[i].getBounds().x+5, enemy[i].getBounds().y, enemy[i].getBounds().width-10, enemy[i].getBounds().height-20);
                for (int j = 0; j < this.shoot.length; j++) {
                    p[j] = new Rectangle(this.shoot[j].getBounds().x, this.shoot[j].getBounds().y, this.shoot[j].getBounds().width, this.shoot[j].getBounds().height);
                    if (p[j].intersects(rAlien[i])) {
                        shoot[j].setVisible(false);
                        this.p.remove(shoot[j]);
                        shoot[j].setIcon(null);
                        enemy[i].setVisible(false);
                        this.p.remove(enemy[i]);
                        shoot[j].setBounds(-1000000, -10000000, this.shoot[0].getWidth(), this.shoot[0].getHeight());
                        enemy[i].setBounds(-100000, -100000, 10, 10);
                        enemy[i].setIcon(null);
                        scoresInt += 1;
                        score.setText("Score: " + scoresInt);
                        speed = (int) Math.floor(scoresInt/15) + 1;
                    }
                    if(p[j].y == -150){
                        this.p.remove(shoot[j]);
                        shoot[j].setVisible(false);
                        shoot[j].setIcon(null);
                        shoot[j].setBounds(-10000, -10000, this.shoot[0].getWidth(), this.shoot[0].getHeight());
                        System.out.println(p[j].y);
                    }

                    if(enemy[i].isShowing() == true){
                        if(rAlien[i].intersects(rPlayer) || rAlien[i].getY() + rAlien[i].getHeight() > height-50){
                            life--;
                            System.out.println("Life:" +life);
                            this.p.remove(enemy[i]);
                            enemy[i].setVisible(false);
                            enemy[i].setBounds(-100, -100, 10, 10);
                            enemy[i].setIcon(null);
                        }
                    }
                    if(life <= 0){
                        int selection = JOptionPane.showConfirmDialog(f, "Your final score is " + scoresInt +"\nDo you wish to try again?", "Earth has been taken down", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if(selection == JOptionPane.YES_OPTION){
                            life = 10;
                            scoresInt = 0;
                            speed = 1;
                            f.dispose();
                            f.setVisible(false);
                            SpacePact newPact = new SpacePact();
                            newPact.run();
                            sho.stop();
                        }
                    }
                }
            
            
        }

    }
    public void resetGame(){
        for(int i = 0; i < shoot.length; i++){
            shoot[i] = null;
            alien[i] = null;
        }
    }
    @Override
    public void run() {
        alien = new JLabel[shoot.length];
        mover = new Mover();
        f = new JFrame("Space Pact");
        f.setSize(width + 16, height + 40);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setFocusable(true);
        f.addKeyListener(mover);
        f.setVisible(true);

        p = new JPanel(null);
        p.setBounds(0, 0, width, height);
        p.setBackground(Color.BLACK);
        p.setFocusable(true);
        p.addKeyListener(mover);
        f.add(p);
        
        
        score = new JLabel("Scores " + scoresInt);
        score.setBounds(20, 20, 160, 20);
        score.setForeground(Color.red);
        score.setFont(style);
        p.add(score);
        
        
        health = new JLabel("Health " + life);
        health.setBounds(width-160, 20, 150, 20);
        health.setForeground(Color.red);
        health.setFont(style);
        health.setForeground(Color.green);
        p.add(health);
        
        player = new JLabel(ship);
        player.setBounds(p_x, p_y, ship.getIconWidth(), ship.getIconHeight());
        p.add(player);
        for (int i = 0; i < shoot.length; i++) {
            shoot[i] = new JLabel(shootProjectile);
            shoot[i].setBounds(-1000000, -100000, shootProjectile.getIconWidth(), shootProjectile.getIconHeight());
            shoot[i].setVisible(false);
            p.add(shoot[i]);
        }

        for (int i = 0; i < alien.length; i++) {
            alien[i] = new JLabel(cometProjectile);
            p.add(alien[i]);
        }
        
        
        earth = new JLabel();
        earth.setBounds(0, height-500, width, height);
        earth.setIcon(new ImageIcon("resource\\earth.png"));
        p.add(earth);
        
        bg = new JLabel();
        bg.setBounds(0, 0, width, height);
        bg.setIcon(new ImageIcon("resource\\bg.png"));
        p.add(bg);
        sho.start();
        f.validate();
        f.repaint();
    }

    class Mover implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            int grid = 50;
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    shipSpeed = -8;
            }

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    shipSpeed = 8;
            }

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if(fire){
                    fire =false;
                    shoot_x[bullets] = player.getBounds().x + (player.getBounds().width/2 - ship.getIconWidth()/4);
                    shoot_y[bullets] = player.getBounds().y;
                    shoot[bullets].setVisible(true);
                    bullets++;
                }
            }
            
            if (e.getKeyCode() == KeyEvent.VK_UP){
                scoresInt++;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            shipSpeed = 0;
        }

    }

    public class Shooting implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(p_x < 0){
                p_x = width-player.getBounds().width -1;
            }else if(p_x > width-player.getBounds().width){
                p_x = 0;
            }else{
                p_x += shipSpeed;
                p_x += shipSpeed;
            }
            player.setBounds(p_x, p_y, player.getBounds().width, player.getBounds().height);
            score.setText("Scores " + scoresInt);
            health.setText("Health " + life);
            meteorCounter += 0.60f;
            alienCounter += 1f;
            if(alienCounter%20 == 0){
                fire = true;
            }
            if(alienCounter >= 60){
                alienCounter = 0;
                alien[numOfAliens].setBounds((int) (Math.random() * (width - shoot[0].getBounds().width)), -cometProjectile.getIconHeight(), cometProjectile.getIconWidth(), cometProjectile.getIconHeight());
                numOfAliens++;
            }
            for (int i = 0; i < shoot.length; i++) {
                if (shoot[i].isVisible() && shoot[i].isShowing()) {
                    move(shoot[i], i);
                }
            }
            int countAlien = alien.length;
            for (int i = 0; i < numOfAliens; i++) {
                if(alien[i].isShowing()){
                    float alien_y = alien[i].getBounds().y;
                    
                    alien_y += speed;

                    alien[i].setIcon(meteor[(int)meteorCounter%10]);
                    if(meteorCounter == 10){
                        meteorCounter = 0;
                    }
                    alien[i].setBounds(alien[i].getBounds().x, (int) (alien_y), alien[i].getBounds().width, alien[i].getBounds().height);

                    collide(shoot,alien);
                    f.repaint();
                }
            }
        }

    }

}
