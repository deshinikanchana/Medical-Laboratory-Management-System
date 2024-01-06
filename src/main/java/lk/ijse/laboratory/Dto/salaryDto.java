package lk.ijse.laboratory.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class salaryDto {
    private String salId;
    private String empId;
    private Double amount;
    private Date paidDate;

}
