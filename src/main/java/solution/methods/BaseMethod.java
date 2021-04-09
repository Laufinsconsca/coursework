package solution.methods;

import dto.InputDataDto;
import enums.FixedVariableType;
import javafx.collections.FXCollections;
import model.Point;
import model.complex.Complex;
import model.matrix.ComplexMatrix;
import model.tabulatedFunction.ArrayTabulatedFunction;
import model.tabulatedFunction.TabulatedFunction;
import org.apache.commons.math3.special.BesselJ;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import solution.CrossSectionCalculated;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BaseMethod implements CrossSectionCalculated {
    protected static final List<Double> besselZeros = besselZeros();
    protected Integer J;
    protected Integer K;
    protected Integer nEigenfunction;
    protected double L;
    protected double R;
    protected double λ;
    protected double n;
    protected double[] fixedVariable;
    protected FixedVariableType fixedVariableType;
    protected Complex α;
    ComplexMatrix A;
    ComplexMatrix B;
    ComplexMatrix C;
    ComplexMatrix p;
    ComplexMatrix q;
    ComplexMatrix U;

    private static List<Double> besselZeros() {
        InputStream file = BaseMethod.class.getResourceAsStream("/bessel_zeros.xlsx");
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<Double> zeros = new ArrayList<>();
        Iterator<Row> iterator = sheet.iterator();
        iterator.forEachRemaining(row -> zeros.add(row.getCell(0).getNumericCellValue()));
        return zeros;
    }

    void init(InputDataDto inputDataDto) {
        J = inputDataDto.getJ();
        K = inputDataDto.getK();
        L = inputDataDto.getL();
        R = inputDataDto.getR();
        λ = inputDataDto.getΛ();
        n = inputDataDto.getNRefraction();
        fixedVariable = inputDataDto.getFixedVariable();
        fixedVariableType = inputDataDto.getFixedVariableType();
        nEigenfunction = inputDataDto.getNEigenfunction();
        α = new Complex(0, L * λ / (4 * K * Math.PI * n * Math.pow(R / J, 2)));
        A = new ComplexMatrix(1, J);
        B = new ComplexMatrix(1, J);
        C = new ComplexMatrix(1, J);
        p = new ComplexMatrix(1, J);
        q = new ComplexMatrix(1, J);
        U = new ComplexMatrix(J + 1, K + 1);
    }

    protected double ψ(double r, double R) {
        return BesselJ.value(0, besselZeros.get(nEigenfunction - 1) * r / R);
    }

    protected List<TabulatedFunction> getTabulatedFunction(ComplexMatrix U) {
        List<TabulatedFunction> array = new ArrayList<>();
        for (int j = 0; j < fixedVariable.length; j++) {
            List<Point> points = new ArrayList<>();
            int layer;
            if (fixedVariableType.equals(FixedVariableType.r)) {
                layer = Math.round((float) (fixedVariable[j] * J / R));
            } else {
                layer = Math.round((float) (fixedVariable[j] * K / L));
            }
            double h_r = R / J;
            double h_z = L / K;
            double I = fixedVariableType.equals(FixedVariableType.r) ? K : J;
            for (int i = 0; i < I + 1; i++) {
                if (fixedVariableType.equals(FixedVariableType.r)) {
                    points.add(new Point(h_z * i, h_r * layer, U.get(layer + 1, i + 1)));
                } else {
                    points.add(new Point(h_r * i, h_z * layer, U.get(i + 1, layer + 1)));
                }
            }
            array.add(new ArrayTabulatedFunction(FXCollections.observableList(points), fixedVariable[j]));
        }
        return array;
    }
}
