package com.mycompany.kolekcje;

public class Vector2D {
    private int x;
    private int y;

    public Vector2D(){
        this.x=0;
        this.y=0;
    }
    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
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
    public void set(Vector2D v2){
        this.x = v2.getX();
        this.y = v2.getY();
    }

    public double getLength(){
        return Math.sqrt(getX()*getX() + getY()*getY());
    }

    public double getDistance(Vector2D v2){
        return Math.sqrt(Math.pow(v2.getX()-getX(),2)+Math.pow(v2.getY()-getY(),2));
    }

    public Vector2D add(Vector2D v2){
        Vector2D sum = new Vector2D();
        sum.setX(getX()+v2.getX());
        sum.setY(getY()+v2.getY());
        return sum;
    }
    public Vector2D sub(Vector2D v2){
        Vector2D sum = new Vector2D();
        sum.setX(getX()-v2.getX());
        sum.setY(getY()-v2.getY());
        return sum;
    }
    public Vector2D multiply(double factor){
        Vector2D sum = new Vector2D();
        sum.setX((int) (getX()*factor));
        sum.setY((int) (getY()*factor));
        return sum;
    }

    public  Vector2D norm(){
        double len = getLength();
        if(len != 0){
            this.setX((int) (this.getX()/len));
            this.setY((int) (this.getY()/len));
        }else{
            this.set(0,0);
        }
        return this;
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public double dot(Vector2D v2) {
        double sum = 0;
        sum = this.getX() * v2.getX() + this.getY()*v2.getY();
        return sum;
    }
}
