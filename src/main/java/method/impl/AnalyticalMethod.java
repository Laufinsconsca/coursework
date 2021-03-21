package method.impl;


import complex.Complex;
import dto.InputDataDto;
import javafx.collections.FXCollections;
import method.ContinuousFunction;
import method.CrossSectionCalculated;
import model.Point;
import org.apache.commons.math3.special.BesselJ;
import tabulatedFunctions.ArrayTabulatedFunction;
import tabulatedFunctions.TabulatedFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class AnalyticalMethod extends BaseMethod implements CrossSectionCalculated, ContinuousFunction {
    public static final double ksi_0_1 = 2.404825557695773;
    public static final double ksi_0_5 = 14.930917708487787;

    @Override
    public TabulatedFunction crossSectionCalculate(InputDataDto inputDataDto) {
        init(inputDataDto);
        double N = 1000;
        double h_r = R / N;
        List<Point> points = new ArrayList<>();
        for (double j = 0; j <= N; j++) {
            points.add(new Point(j * h_r, z, u(j * h_r, z, λ, n, R, ksi_0_1)));
        }
        TabulatedFunction analyticalSolution = new ArrayTabulatedFunction(FXCollections.observableList(points), z);
        analyticalSolution.setName("аналитическое\nрешение");
        return analyticalSolution;
    }

    public static int A(double precision){
        return (int)Math.ceil(1./4+20*Math.sqrt(Math.log(Math.pow(Math.PI,3./2)/80)-Math.log(precision))/Math.PI);
    }

    public static Complex u(double r, double z, double λ, double n, double R, double bessel_root) {
        return Complex.I.scaleOn(λ * z * bessel_root * bessel_root / (4 * Math.PI * R * R * n)).exp().scaleOn(BesselJ.value(0, bessel_root * r / R));
    }

    @Override
    public BiFunction<Double, Double, Complex> calculate(InputDataDto inputDataDto) {
        return (r, z) -> u(r, z, inputDataDto.getΛ(), inputDataDto.getNRefraction(), inputDataDto.getR(), ksi_0_1);
    }
}
