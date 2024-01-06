package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class refferenceRangesTm {
    private String testCode;
    private String subTestCode;
    private String subTest;
    private String refferenceRange;
    private String unit;

}
