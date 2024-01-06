package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data


public class collectingCenterTm {
    private String ccId;
    private String centerName;
    private String address;
    private String telNo;
    private String  email;
    private int sampleCount;
}
