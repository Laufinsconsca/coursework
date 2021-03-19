package dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InputDataDto {

    private String name;
    private Integer J;
    private Integer K;
    private double λ;
    private double nRefraction;
    private double L;
    private double R;
    private double z;
}
