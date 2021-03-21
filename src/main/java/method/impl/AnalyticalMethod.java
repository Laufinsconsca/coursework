package method.impl;


import complex.Complex;
import dto.InputDataDto;
import javafx.collections.FXCollections;
import method.ContinuousFunction;
import method.CrossSectionCalculated;
import model.Point;
import org.apache.commons.math3.special.BesselJ;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tabulatedFunctions.ArrayTabulatedFunction;
import tabulatedFunctions.TabulatedFunction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

public class AnalyticalMethod extends BaseMethod implements CrossSectionCalculated, ContinuousFunction {

    @Override
    public TabulatedFunction crossSectionCalculate(InputDataDto inputDataDto) {
        init(inputDataDto);
        double N = 1000;
        double h_r = R / N;
        List<Point> points = new ArrayList<>();
        for (double j = 0; j <= N; j++) {
            points.add(new Point(j * h_r, z, u(j * h_r, z, λ, n, R, besselZeros.get(nEigenfunction - 1))));
            //points.add(new Point(j * h_r, z, u1(j * h_r, z, λ, n, R, besselZeros)));
        }
        TabulatedFunction analyticalSolution = new ArrayTabulatedFunction(FXCollections.observableList(points), z);
        analyticalSolution.setName("аналитическое\nрешение");
        return analyticalSolution;
    }

    public static Complex u(double r, double z, double λ, double n, double R, double besselRoot) {
        return Complex.I.scaleOn(λ * z * besselRoot * besselRoot / (4 * Math.PI * R * R * n)).exp().scaleOn(BesselJ.value(0, besselRoot * r / R));
    }

    @Override
    public BiFunction<Double, Double, Complex> calculate(InputDataDto inputDataDto) {
        return (r, z) -> u(r, z, inputDataDto.getΛ(), inputDataDto.getNRefraction(), inputDataDto.getR(), besselZeros.get(inputDataDto.getNEigenfunction() - 1));
        //return (r, z) -> u1(r, z, inputDataDto.getΛ(), inputDataDto.getNRefraction(), inputDataDto.getR(), besselZeros);
    }

    public static Complex u1(double r, double z, double λ, double n, double R, List<Double> besselZeros) {
        Complex result = Complex.ZERO;
        for (double besselRoot : besselZeros) {
            result = result.add(Complex.I.scaleOn(λ * z * besselRoot * besselRoot / (4 * Math.PI * R * R * n)).add(-Math.pow(0.05 * besselRoot, 2)).exp().scaleOn(BesselJ.value(0, besselRoot * r / R) / Math.pow(BesselJ.value(1, besselRoot), 2)));
        }
        return result.divide(100);
    }
}
