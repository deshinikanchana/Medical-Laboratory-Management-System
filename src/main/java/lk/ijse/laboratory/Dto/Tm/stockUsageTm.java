package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class stockUsageTm {
    private String testCode;
    private String testName;
    private String itemCode;
    private String itemName;
    private int usagePerTest;
}
