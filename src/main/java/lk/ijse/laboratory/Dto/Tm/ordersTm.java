package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.sql.Date;

@AllArgsConstructor
@Data
public class ordersTm {
    private String description;
    private int Qty;
   private String ItemCode;
}
