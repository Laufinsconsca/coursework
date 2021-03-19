package method.impl;

import dto.InputDataDto;
import method.Method;
import org.apache.commons.math3.special.BesselJ;
import tabulatedFunctions.TabulatedFunction;

public abstract class BaseMethod implements Method {
    protected static final double ksi_0_1 = 2.404825557695773;
    protected TabulatedFunction tabulatedFunction;
    protected Integer J;
    protected Integer K;
    protected double L;
    protected double R;
    protected double λ;
    protected double n;
    protected double z;

    void init(InputDataDto inputDataDto) {
        J = inputDataDto.getJ();
        K = inputDataDto.getK();
        L = inputDataDto.getL();
        R = inputDataDto.getR();
        λ = inputDataDto.getΛ();
        n = inputDataDto.getNRefraction();
        z = inputDataDto.getZ();
    }

    protected double ψ(double r, double R) {
        return BesselJ.value(0, ksi_0_1 * r / R);
    }
}
