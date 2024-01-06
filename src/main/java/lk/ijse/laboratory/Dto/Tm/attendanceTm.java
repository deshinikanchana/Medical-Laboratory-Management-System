package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class attendanceTm {
    private Date date;
    private String empId;
    private String name;
    private Time inTime;
    private Time outTime;
    private String status;
}
