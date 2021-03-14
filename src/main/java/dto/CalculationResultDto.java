package dto;

import lombok.*;
import model.Point;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Data
public class CalculationResultDto {

    private Integer idCalculationResult;
    private String name;
    private Time date;
    private Integer numberPoint;
    private Integer numberOfMembers;
    private double lambda;
    private double nRefraction;
    private double length;
    private double radius;
    private List<Point> analyticalSolution;
    private List<Point> implicitSchemaSolution;
    private List<Point> solutionByTheCrankNicholsonScheme;
}
