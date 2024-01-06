package lk.ijse.laboratory.Dto;

import lk.ijse.laboratory.Dto.Tm.ordersTm;
import lk.ijse.laboratory.Dto.Tm.prescriptionTm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class itemOrderDetailDto {
    private String orderId;
    private String SupplierId;
    private List<ordersTm> tmList = new ArrayList<>();
}
