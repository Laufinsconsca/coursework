package model;


import lombok.Data;

import java.math.BigInteger;

@Data
public class Point {

    private BigInteger idPoint;
    private double x;
    private double y;
    private double u;

    public Point(double x, double y, double u) {
        this.x = x;
        this.y = y;
        this.u = u;
    }

    public Point() {
    }

    public BigInteger getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(BigInteger idPoint) {
        this.idPoint = idPoint;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getU() {
        return u;
    }

    public void setU(double u) {
        this.u = u;
    }
}
