package method;


import dto.InputDataDto;
import model.Point;

import java.util.List;

public interface Method {
    List<Point> makeCalculation(InputDataDto inputDataDto);
}
