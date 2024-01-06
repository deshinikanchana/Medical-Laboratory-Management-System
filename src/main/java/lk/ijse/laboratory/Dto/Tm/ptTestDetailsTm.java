package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ptTestDetailsTm {
private Date date;
private String patientId;
private String patientName;
private String testName;
private String status;
}
