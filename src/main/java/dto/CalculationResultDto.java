package dto;

import lombok.*;
import model.Point;

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
    private Date date;
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
