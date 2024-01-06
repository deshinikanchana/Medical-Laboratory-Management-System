package lk.ijse.laboratory.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class stockItemDto {
    private String itemCode;
    private String userId;
    private String description;
    private int qtyOnHand;
    private String category;
    private int warningLevel;
}
