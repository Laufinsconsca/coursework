package method;


import dto.InputDataDto;
import model.Point;
import org.apache.commons.math3.special.BesselJ;

import java.util.List;

public class AnalyticalMethod extends BaseMethod implements Method {

    @Override
    public List<Point> makeCalculation(InputDataDto inputDataDto) {
        init(inputDataDto);
        //calculation
        for (double x = 0; x < (length + h); x += h) {
            double J_0 = BesselJ.value(0, x);
            for (double y = 0; y < (radius + r_h); y += r_h) {
                u = J_0*(ksi_0_1 * y / radius) * ((ksi_0_1 / radius) * ((x * lambda) / (4 * Math.PI * n)));//Todo add right calculation
                points.add(new Point(x, y, u));
            }
        }
        return points;
    }
}
