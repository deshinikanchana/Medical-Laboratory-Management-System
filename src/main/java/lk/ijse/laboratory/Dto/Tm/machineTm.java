package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class machineTm {
    private LocalDate date;
    private String machineId;
    private String machineName;
    private String  sectionName;
    private String condition;
}
