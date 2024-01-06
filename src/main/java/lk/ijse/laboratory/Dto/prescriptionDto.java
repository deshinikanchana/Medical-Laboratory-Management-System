package lk.ijse.laboratory.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class prescriptionDto {
    private String presId;
    private String ptId;
    private String refferedBy;
    private Double totalAmount;
    private Double duePayment;
}
