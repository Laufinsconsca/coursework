package method;

import dto.InputDataDto;
import model.Point;

import java.util.List;

public class CrankNicholsonScheme extends BaseMethod implements Method{
    @Override
    public List<Point> makeCalculation(InputDataDto inputDataDto) {
        init(inputDataDto);
        //calculation
        return points;
    }
}
