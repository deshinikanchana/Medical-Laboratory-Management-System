package lk.ijse.laboratory.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class testDto {
    private String testId;
    private String secId;
    private String test;
    private String estimatedTime;
    private float price;
    private String sampleType;
    private String machineId;

}
