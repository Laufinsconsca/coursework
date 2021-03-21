package method.impl;

import complex.Complex;
import dto.InputDataDto;
import javafx.collections.FXCollections;
import matrices.matrix.Matrix;
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

public abstract class BaseMethod implements CrossSectionCalculated {
    protected TabulatedFunction tabulatedFunction;
    protected Integer J;
    protected Integer K;
    protected Integer nEigenfunction;
    protected double L;
    protected double R;
    protected double λ;
    protected double n;
    protected double z;
    protected static final List<Double> besselZeros = besselZeros();

    void init(InputDataDto inputDataDto) {
        J = inputDataDto.getJ();
        K = inputDataDto.getK();
        L = inputDataDto.getL();
        R = inputDataDto.getR();
        λ = inputDataDto.getΛ();
        n = inputDataDto.getNRefraction();
        z = inputDataDto.getZ();
        nEigenfunction = inputDataDto.getNEigenfunction();
    }

    protected double ψ(double r, double R) {
        return BesselJ.value(0, besselZeros.get(nEigenfunction - 1) * r / R);
        //double a = 0.1*R;
        //return Math.exp(-r*r/a*a);
    }

    protected TabulatedFunction getTabulatedFunction(Matrix<Complex> U, int k) {
        // k - временной слой
        List<Point> points = new ArrayList<>();
        double h_r = R / J;
        double h_z = L / K;
        for (int j = 0; j < J + 1; j++) {
            points.add(new Point(h_r * j, h_z * k, U.get(j + 1, k + 1).get()));
        }
        return new ArrayTabulatedFunction(FXCollections.observableList(points), z);
    }

    private static List<Double> besselZeros(){
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File("src/main/resources/bessel_zeros.xlsx"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
}
