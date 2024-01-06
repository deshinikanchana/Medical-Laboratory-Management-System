package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.patientDto;
import lk.ijse.laboratory.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data

public class patientModel {
    public boolean savePatient(patientDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO patient VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getPtId());
        pstm.setString(2, dto.getUserId());
        pstm.setString(3, dto.getCcId());
        pstm.setString(4, dto.getName());
        pstm.setString(5, dto.getGender());
        pstm.setString(6, dto.getDob());
        pstm.setString(7, dto.getTelNo());
        pstm.setString(8, dto.getEmail());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }


    public String generateNextPtId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT ptId FROM patient ORDER BY ptId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String currentPtId = null;

        if (resultSet.next()) {
            currentPtId = resultSet.getString(1);
            return splitPtId(currentPtId);
        }
        return splitPtId(null);
    }

    private String splitPtId(String currentPtId) {
        int id;
        if (currentPtId != null) {
            String[] split = currentPtId.split("P");
            id = Integer.parseInt(split[1]);
            if (id < 10) {
                id++;
                return "P00" + id;
            } else {
                id++;
                return "P0" + id;
            }
        }
        return "P001";
    }


    public static List<patientDto> getAllPatients() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM patient";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<patientDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String ptId = resultSet.getString(1);
            String userId = resultSet.getString(2);
            String ccId = resultSet.getString(3);
            String name = resultSet.getString(4);
            String gender = resultSet.getString(5);
            String dob = resultSet.getString(6);
            String telNo = resultSet.getString(7);
            String email = resultSet.getString(8);

            var dto = new patientDto(ptId,userId,ccId,name,gender, dob,telNo,email);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public boolean updatePatient(patientDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE patient SET name = ?, DOB = ?, telNo = ?, email = ? WHERE ptId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getDob());
        pstm.setString(3, dto.getTelNo());
        pstm.setString(4, dto.getEmail());
        pstm.setString(5, dto.getPtId());

        return pstm.executeUpdate() > 0;
    }

    public boolean deletePatient(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM patient WHERE ptId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static patientDto searchPatient(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM patient WHERE ptId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        patientDto PTDto = new patientDto();

        if (resultSet.next()) {
           PTDto.setPtId(resultSet.getString(1));
           PTDto.setUserId(resultSet.getString(2));
            PTDto.setCcId(resultSet.getString(3));
            PTDto.setName(resultSet.getString(4));
            PTDto.setGender(resultSet.getString(5));
            PTDto.setDob(resultSet.getString(6));
            PTDto.setTelNo(resultSet.getString(7));
            PTDto.setEmail(resultSet.getString(8));

        }
        return PTDto;
    }
}
