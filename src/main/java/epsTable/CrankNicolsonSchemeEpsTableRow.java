package epsTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrankNicolsonSchemeEpsTableRow {
    private Integer J;
    private Integer K;
    private double ε_2h_r_2h_z;
    private double ε_h_r_h_z;
    private double δ_h_r_h_z;
}