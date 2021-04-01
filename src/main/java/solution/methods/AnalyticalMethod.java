package solution.methods;

import complex.Complex;
import dto.InputDataDto;
import enums.FixedVariableType;
import javafx.collections.FXCollections;
import model.Point;
import org.apache.commons.math3.special.BesselJ;
import solution.ContinuousFunction;
import solution.CrossSectionCalculated;
import tabulatedFunctions.ArrayTabulatedFunction;
import tabulatedFunctions.TabulatedFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static complex.Complex.I;

public class AnalyticalMethod extends BaseMethod implements CrossSectionCalculated, ContinuousFunction {

    public static Complex u(double r, double z, double λ, double n, double R, double besselRoot) {
        return I.scaleOn(λ * z * besselRoot * besselRoot / (4 * Math.PI * R * R * n)).exp().scaleOn(BesselJ.value(0, besselRoot * r / R));
    }

    @Override
    public List<TabulatedFunction> crossSectionCalculate(InputDataDto inputDataDto) {
        init(inputDataDto);
        List<TabulatedFunction> array = new ArrayList<>();
        for (int j = 0; j < fixedVariable.length; j++) {
            double N = 1000; //количество разбиений аналитического решения для построения графика
            double h;
            if (fixedVariableType.equals(FixedVariableType.r)) {
                h = L / N;
            } else {
                h = R / N;
            }
            List<Point> points = new ArrayList<>();
            for (double i = 0; i <= N; i++) {
                if (fixedVariableType.equals(FixedVariableType.r)) {
                    points.add(new Point(i * h, fixedVariable[j], u(fixedVariable[j], i * h, λ, n, R, besselZeros.get(nEigenfunction - 1))));
                } else {
                    points.add(new Point(i * h, fixedVariable[j], u(i * h, fixedVariable[j], λ, n, R, besselZeros.get(nEigenfunction - 1))));
                }
            }
            TabulatedFunction analyticalSolution = new ArrayTabulatedFunction(FXCollections.observableList(points), fixedVariable[j]);
            if (fixedVariableType.equals(FixedVariableType.r)) {
                analyticalSolution.setName("аналитическое\nрешение,\nr = " + fixedVariable[j]);
            } else {
                analyticalSolution.setName("аналитическое\nрешение,\nz = " + fixedVariable[j]);
            }
            array.add(analyticalSolution);
        }
        return array;
    }

    @Override
    public BiFunction<Double, Double, Complex> calculate(InputDataDto inputDataDto) {
        return (r, z) -> u(r, z, inputDataDto.getΛ(), inputDataDto.getNRefraction(), inputDataDto.getR(), besselZeros.get(inputDataDto.getNEigenfunction() - 1));
    }
}
