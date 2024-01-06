package lk.ijse.laboratory.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class supplierDto {
    private String supId;
    private String name;
    private String telNo;
    private String email;
    private String category;
}
