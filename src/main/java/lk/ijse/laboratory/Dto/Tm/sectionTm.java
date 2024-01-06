package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class sectionTm {
    private String secId;
    private String secName;
    private String consultant;
    private int testCount;
}
