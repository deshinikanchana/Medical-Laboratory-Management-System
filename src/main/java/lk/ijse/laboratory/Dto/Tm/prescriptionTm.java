package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class prescriptionTm {
    private String testCode;
    private String testName;
    private Float price;
    private String estTime;
}
