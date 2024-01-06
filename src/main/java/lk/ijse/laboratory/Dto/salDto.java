package lk.ijse.laboratory.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class salDto {
    private  float nopayHrs;
    private float nopay;
    private Double otHrs;
    private Double otPay;
    private Double epfCut;
    private Double netsal;

}
