package lk.ijse.laboratory.Dto.Tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class stockItemTm {
    private String itemCode;
    private String description;
    private int qtyOnHand;
    private String category;
    private Button btn;
}
