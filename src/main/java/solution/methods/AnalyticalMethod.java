package solution.methods;

import dto.InputDataDto;
import enums.FixedVariableType;
import model.Point;
import model.complex.Complex;
import model.tabulatedFunction.ArrayTabulatedFunction;
import model.tabulatedFunction.TabulatedFunction;
import org.apache.commons.math3.special.BesselJ;
import solution.ContinuousFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static model.complex.Complex.I;

public class AnalyticalMethod extends BaseMethod implements ContinuousFunction {

    public static Complex u(double r, double z, double λ, double n, double R, double besselRoot) {
        return I.multiply(λ * z * besselRoot * besselRoot / (4 * Math.PI * R * R * n)).exp().multiply(BesselJ.value(0, besselRoot * r / R));
    }

    @Override
    public BiFunction<Double, Double, Complex> calculate(InputDataDto inputDataDto) {
        return (r, z) -> u(r, z, inputDataDto.getΛ(), inputDataDto.getNRefraction(), inputDataDto.getR(), besselZeros.get(inputDataDto.getNEigenfunction() - 1));
    }

    @Override
    public List<TabulatedFunction> crossSectionCalculate(InputDataDto inputDataDto) {
        init(inputDataDto);
        List<TabulatedFunction> array = new ArrayList<>();
        for (double v : fixedVariable) {
            double N = 10000; //количество разбиений аналитического решения для построения графика
            double h;
            if (fixedVariableType.equals(FixedVariableType.r)) {
                h = L / N;
            } else {
                h = R / N;
            }
            List<Point> points = new ArrayList<>();
            for (double i = 0; i <= N; i++) {
                if (fixedVariableType.equals(FixedVariableType.r)) {
                    points.add(new Point(i * h, v, u(v, i * h, λ, n, R, besselZeros.get(nEigenfunction - 1))));
                } else {
                    points.add(new Point(i * h, v, u(i * h, v, λ, n, R, besselZeros.get(nEigenfunction - 1))));
                }
            }
            TabulatedFunction analyticalSolution = new ArrayTabulatedFunction(points, v);
            if (fixedVariableType.equals(FixedVariableType.r)) {
                analyticalSolution.setName("аналитическое\nрешение,\nr = " + v);
            } else {
                analyticalSolution.setName("аналитическое\nрешение,\nz = " + v);
            }
            array.add(analyticalSolution);
        }
        return array;
    }
}
