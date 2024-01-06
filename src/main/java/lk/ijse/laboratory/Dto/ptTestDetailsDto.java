package lk.ijse.laboratory.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ptTestDetailsDto {
    private Date date;
    private String presId;
    private String testId;
    private String status;
    private String comment;
}
