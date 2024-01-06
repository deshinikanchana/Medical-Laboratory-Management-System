package lk.ijse.laboratory.Dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class patientTm {
  private String ptId;
  private String ptName;
  private String gender;
  private String age;
  private String tel;
  private String email;
}
