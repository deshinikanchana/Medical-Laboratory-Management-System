package lk.ijse.laboratory.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ordersDto {
    private String orderId;
    private String supId;
    private Date date;
    private String status;
}
