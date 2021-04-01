package dto;

import enums.FixedVariableType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class InputDataDto {

    //    private String name;
    private final Integer J;
    private final Integer K;
    private final Integer nEigenfunction;
    private final double λ;
    private final double nRefraction;
    private final double L;
    private final double R;
    private final double[] fixedVariable;
    private final FixedVariableType fixedVariableType;
}
