package ui.epsTableRows;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrankNicolsonSchemeEpsTableRow {
    private Integer J;
    private Integer K;
    private double ε_1_2h_r_1_2h_z;
    private double ε_h_r_h_z;
    private double δ_h_r_h_z;
}