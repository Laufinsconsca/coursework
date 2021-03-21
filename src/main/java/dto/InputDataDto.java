package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
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
