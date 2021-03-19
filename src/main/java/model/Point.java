package model;

import complex.Complex;

public class Point {

    //    private BigInteger idPoint;
    private double r;
    private double z;
    private Complex u;

    public Point(double r, double z, Complex u) {
        this.r = r;
        this.u = u;
    }

    public Point() {
    }

//    public BigInteger getIdPoint() {
//        return idPoint;
//    }
//
//    public void setIdPoint(BigInteger idPoint) {
//        this.idPoint = idPoint;
//    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Complex getU() {
        return u;
    }

    public void setU(Complex y) {
        this.u = y;
    }
}
