package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
