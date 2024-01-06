package lk.ijse.laboratory.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class patientDto{
    private String ptId;
    private String userId;
    private String ccId;
    private String name;
    private String gender;
    private String dob;
    private String telNo;
    private String email;

}
