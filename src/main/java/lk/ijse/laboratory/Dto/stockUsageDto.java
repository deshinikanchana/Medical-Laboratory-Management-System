package lk.ijse.laboratory.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class stockUsageDto {
    private String testId;
    private String itemCode;
    private int qtyPerTest;
}
