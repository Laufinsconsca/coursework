package dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InputDataDto {

    private String name;
    private Integer numberPoint;
    private Integer numberOfMembers;
    private double lambda;
    private double nRefraction;
    private double length;
    private double radius;

}
