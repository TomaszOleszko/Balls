package Utility;

public class Vector {
    private int x;
    private int y;
    public Vector(){
        this.x=0;
        this.y=0;
    }
    public Vector(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Vector(Vector vec2){
        this.x = vec2.getX();
        this.y = vec2.getY();
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void set(Vector v2){
        this.x = v2.getX();
        this.y = v2.getY();
    }
    public double getLength(){
        return Math.sqrt(getX()*getX() + getY()*getY());
    }
    public Vector add(Vector v2){
        Vector sum = new Vector();
        sum.setX(getX()+v2.getX());
        sum.setY(getY()+v2.getY());
        return sum;
    }
    public Vector sub(Vector v2){
        Vector sum = new Vector();
        sum.setX(getX()-v2.getX());
        sum.setY(getY()-v2.getY());
        return sum;
    }
    public Vector multiply(double factor){
        Vector sum = new Vector();
        sum.setX((int) (getX()*factor));
        sum.setY((int) (getY()*factor));
        return sum;
    }
    public  Vector norm(){
        double len = getLength();
        if(len != 0){
            this.setX((int) (this.getX()/len));
            this.setY((int) (this.getY()/len));
        }else{
            this.set(0,0);
        }
        return this;
    }
    public double dot(Vector v2) {
        return this.getX() * v2.getX() + this.getY()*v2.getY();
    }
    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
