package lk.ijse.laboratory.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class collectingCenterDto {
    private String ccId;
    private String centerName;
    private String address;
    private String telNo;
    private String  email;
}
