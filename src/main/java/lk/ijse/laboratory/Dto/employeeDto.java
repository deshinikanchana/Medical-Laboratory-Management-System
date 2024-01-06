package lk.ijse.laboratory.Dto;

import lk.ijse.laboratory.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class employeeDto {
    private String empId;
    private String jobId;
    private String userId;
    private String name;
    private String nic;
    private String address;
    private String email;
    private String telNo;

}
