package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.salaryDto;
import lk.ijse.laboratory.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class salaryModel {

    public static String generateNextSalaryId() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT salId FROM salary ORDER BY salId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String currentSalId = null;

        if (resultSet.next()) {
            currentSalId = resultSet.getString(1);
            return splitSalId(currentSalId);
        }
        return splitSalId(null);
    }

    private static String splitSalId(String currentSalId) {
        if (currentSalId != null) {
            String[] split = currentSalId.split("S");
            int id = Integer.parseInt(split[1]);
            if (id < 10) {
                id++;
                return "S00" + id;
            } else {
                id++;
                return "S0" + id;
            }
        }
        return "S001";
    }

    public static salaryDto getSalary(String empId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM salary where empId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,empId);

        ResultSet resultSet = pstm.executeQuery();
        salaryDto dto = null;
        while (resultSet.next()) {
            String salId = resultSet.getString(1);
            String emId = resultSet.getString(2);
            Double amount = resultSet.getDouble(3);
            Date date = resultSet.getDate(4);

            dto = new salaryDto(salId, emId, amount, date);
        }
        return dto;
    }

    public List<salaryDto> getAllsalaries() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM salary";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<salaryDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String salId = resultSet.getString(1);
            String empId = resultSet.getString(2);
            Double amount = resultSet.getDouble(3);
            Date date = resultSet.getDate(4);

            var Dto = new salaryDto(salId,empId,amount,date);

            dtoList.add(Dto);
        }
        return dtoList;
    }

    public boolean savePaidDetails(salaryDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO salary VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getSalId());
        pstm.setString(2, dto.getEmpId());
        pstm.setDouble(3, dto.getAmount());
        pstm.setDate(4, (Date) dto.getPaidDate());


        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }
}
