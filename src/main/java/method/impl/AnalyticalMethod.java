package method.impl;


import complex.Complex;
import dto.InputDataDto;
import javafx.collections.FXCollections;
import method.Method;
import model.Point;
import org.apache.commons.math3.special.BesselJ;
import tabulatedFunctions.ArrayTabulatedFunction;
import tabulatedFunctions.TabulatedFunction;

import java.util.ArrayList;
import java.util.List;

public class AnalyticalMethod extends BaseMethod implements Method {

    @Override
    public TabulatedFunction doCalculation(InputDataDto inputDataDto) {
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

    private Complex u(double r, double z, double λ, double n, double R, double bessel_root) {
        return Complex.I.scale(λ * z * bessel_root * bessel_root / (4 * Math.PI * R * R * n)).exp().scale(BesselJ.value(0, bessel_root * r / R));
    }
}
