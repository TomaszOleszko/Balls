package com.mycompany.kolekcje;

package kulki;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Panel extends JPanel {
    private static final Random random = new Random();
    private static byte mous=0;
    protected ArrayList<Kula> listaKul;
    protected int size = 20;


    //dla 30fps -> 1s/30 = 0,033s
    public Panel() {
        listaKul = new ArrayList<>();
        setBackground(Color.BLACK);
        addMouseListener(new Event());
        addMouseWheelListener(new Event());
        int DELAY = 8;
        Timer timer = new Timer(DELAY, new Event());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Kula k : listaKul) {
            g.setColor(k.color);
            g.drawOval(k.xy.getX(), k.xy.getY(), k.size, k.size);
        }
        g.setColor(Color.YELLOW);
        g.drawString("My balls: "+(listaKul.size()), 40, 40);
        g.drawString("size now: "+(size/2)+"px",40,55);
        g.drawString("Status: "+(mous == 0 ?"Running": "Stop"),40,70);
    }

    private class Event implements MouseListener, ActionListener, MouseWheelListener {
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            listaKul.add(new Kula(e.getX(), e.getY(), size));
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            mous = 0;
            for (Kula k : listaKul) {
                k.melt();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            mous = 1;
            for (Kula k : listaKul) {
                k.freeze();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (Kula k : listaKul) {
                k.update();
            }
            repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int g = e.getWheelRotation();
            size -= g * 5;
            if(size>45)
                size = 45;
            if(size<5)
                size = 5;
        }
    }

    private class Kula {
        public Vector2D speed;
        public Vector2D buffer;
        public Vector2D xy;
        public int size;
        public Color color;

        public Kula(int x, int y, int size) {
            this.xy = new Vector2D(x, y);
            this.size = size;
            color = new Color((float) Math.random(), (float)
                    Math.random(), (float) Math.random());
            int MAX_SPEED = 5;
            int a = randSpeed(MAX_SPEED);
            int b = randSpeed(MAX_SPEED);
            this.speed = new Vector2D(a, b);
            buffer = new Vector2D();
        }

        private int randSpeed(int max_speed) {
            int speed = random.nextInt(max_speed - Math.negateExact(max_speed)) + Math.negateExact(max_speed);
            if (speed == 0) {
                speed = randSpeed(max_speed);
            }
            return speed;
        }

        public void update() {
            xy.set(this.xy.add(this.speed));

            if (this.xy.getX() <= 0 || this.xy.getX() >= getWidth()) {
                this.speed.setX(-speed.getX());
            }
            if (this.xy.getY() <= 0 || this.xy.getY() >= getHeight()) {
                this.speed.setY(-speed.getY());
            }
            collisionDetector();
        }

        private void collisionDetector() {
            for (int i = 0; i < listaKul.size(); i++) {
                for (int j = i + 1; j < listaKul.size(); j++) {
                    if (listaKul.get(i).isColliding(listaKul.get(j))) {
                        listaKul.get(i).calcCollision(listaKul.get(j));
                    }
                }
            }
        }

        private void calcCollision(Kula kula) {
            Vector2D delta = this.xy.sub(kula.xy);
            double r = this.getRadius() + kula.getRadius();
            double dist = delta.dot(delta);

            if (dist > r * r) return;

            double d = delta.getLength();
            Vector2D mtd;
            if (d == 0) {
                d = kula.getRadius() + getRadius() - 1;
                delta = new Vector2D(kula.getRadius() + getRadius(), 0);
            }
            mtd = (delta.multiply((((getRadius() + kula.getRadius()) - d) / d)));

            Vector2D v = (this.speed.sub(kula.speed));
            double vn = v.dot(mtd.norm());

            if (vn > 0) return;

            Vector2D temp;
            temp = this.speed;
            this.speed = kula.speed;
            kula.speed = temp;
        }

        private boolean isColliding(Kula kula) {
            int dX = this.xy.getX() - kula.xy.getX();
            int dY = this.xy.getY() - kula.xy.getY();

            int sumR = this.getRadius() + kula.getRadius();
            int sumR_sqr = sumR * sumR;

            int dist_sqr = (dX * dX) + (dY * dY);
            return dist_sqr <= sumR_sqr;
        }

        private int getRadius() {
            return this.size / 2;
        }

        public void freeze() {
            this.buffer.set(this.speed);
            speed.set(0, 0);
        }

        public void melt() {
            this.speed.set(this.buffer);
        }
    }
}
