package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class SalaryTm {
    private Date date;
    private String EmpId;
    private Float TotalWorkedHours;
    private Float OTHours;
    private Double Salary;
}
