package Balls;

import Utility.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;


public class Panel extends JPanel {
    private static final Random rand = new Random();
    private static byte  mouse=0;
    protected ArrayList<Ball> balls;
    protected int size = 20;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Ball ball : balls){
            g.setColor(ball.color);
            g.drawOval(ball.coordinates.getX(),ball.coordinates.getY(),ball.size,ball.size);
        }
        g.setColor(Color.YELLOW);
        g.drawString("My balls: "+(balls.size()), 40, 40);
        g.drawString("size now: "+(size/2)+"px",40,55);
        g.drawString("Status: "+(mouse == 0 ?"Running": "Stop"),40,70);
    }

    private class Event implements MouseListener, ActionListener, MouseWheelListener{

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            balls.add(new Ball(e.getX(), e.getY(), size));
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            mouse = 0;
            for (Ball ball : balls){
                ball.melt();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            mouse = 1;
            for (Ball ball : balls){
                ball.freeze();
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            size -= e.getWheelRotation() * 5;
            if (size>40)
                size= 40;
            if (size<10)
                size = 10;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (Ball ball : balls){
                ball.update();
            }
            repaint();
        }
    }

    private class Ball{
        private static final int MAX_SPEED = 3;
        public Vector speed;
        public Vector buffer;
        public Vector coordinates;
        public int size;
        public Color color;

        public Ball(int x, int y, int size){
            this.coordinates = new Vector(x,y);
            this.size = size;
            color = new Color((float) Math.random(),(float) Math.random(),(float)Math.random());
            this.speed = new Vector(randSpeed());
            buffer = new Vector();
        }

        private Vector randSpeed() {
            return new Vector(randomInt(Ball.MAX_SPEED),randomInt(Ball.MAX_SPEED));
        }
        private int randomInt(int bounds){
            int number = rand.nextInt(bounds-Math.negateExact(bounds))+ Math.negateExact(bounds);
            if(number==0)
                number = randomInt(bounds);
            return number;

        }

        public  void update(){
            coordinates.set(coordinates.add(speed));
            if (coordinates.getX() <= 0 || coordinates.getX() >= getWidth())
                speed.setX(-speed.getX());
            if (coordinates.getY() <= 0 || coordinates.getY() >= getHeight())
                speed.setY(-speed.getY());
            detectCollision();
        }

        private void detectCollision() {
            for (int i =0;i<balls.size();i++){
                for (int j = i+1;j<balls.size(); j++){
                    if (balls.get(i).isColliding(balls.get(j)))
                        balls.get(i).calcCollision(balls.get(j));
                }
            }
        }
        private void calcCollision(Ball ball) {
            Vector delta = this.coordinates.sub(ball.coordinates);
            double r = this.getRadius()+ball.getRadius();
            double dist = delta.dot(delta);

            if (dist > r*r) return;

            double d = delta.getLength();
            if (d == 0){
                d = ball.getRadius() + getRadius() - 1;
                delta = new Vector(ball.getRadius() + getRadius(), 0);
            }
            Vector mtd = (delta.multiply((((getRadius() + ball.getRadius()) - d) / d)));
            Vector vel = (this.speed.sub(ball.speed));
            double v = vel.dot(mtd.norm());

            if (v > 0) return;

            Vector temp;
            temp = this.speed;
            this.speed = ball.speed;
            ball.speed = temp;
        }
        private boolean isColliding(Ball ball) {
            Vector d_coordinates = new Vector(
                    this.coordinates.getX()-ball.coordinates.getX(),
                    this.coordinates.getY()-ball.coordinates.getY());
            int sumR = (this.getRadius() + ball.getRadius());
            return ((d_coordinates.getX()*d_coordinates.getX())+(d_coordinates.getY()*d_coordinates.getY())) <= (sumR*sumR);
        }
        private int getRadius() {
            return this.size / 2;
        }

        public void freeze(){
            this.buffer.set(this.speed);
            speed.set(0,0);
        }
        public void melt(){this.speed.set(this.buffer);}
    }

    public Panel(){
        balls = new ArrayList<>();
        setBackground(Color.BLACK);
        addMouseListener(new Event());
        addMouseWheelListener(new Event());
        int DELAY = 8;
        Timer timer = new Timer(DELAY, new Event());
        timer.start();
    }

}
