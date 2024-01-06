package lk.ijse.laboratory.Dto.Tm;

import lk.ijse.laboratory.Dto.designationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class designationTm{
    private String jboId;
    private  String jobTitle;
    private int workingHoursPerMonth;
    private float basicSalary;
    private float otPaymentsPerHour;
    private int empCount;
}
