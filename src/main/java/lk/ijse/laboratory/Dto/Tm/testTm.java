package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class testTm {
    private String testId;
    private String section;
    private String machine;
    private String test;
    private String estimatedTime;
    private float price;

}
