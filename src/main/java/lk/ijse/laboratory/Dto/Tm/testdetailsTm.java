package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class testdetailsTm {
    private String testCode;
    private String testSection;
    private String testName;
    private String sampleType;
    private String estTime;
    private String consultant;
    private Float price;
}
