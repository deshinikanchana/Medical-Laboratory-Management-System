package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.Tm.designationTm;
import lk.ijse.laboratory.Dto.designationDto;
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

public class designationModel {
    public static int getWorkingHours(String job) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT workingHoursPerMonth FROM job_role WHERE jobId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, job);

        ResultSet resultSet = pstm.executeQuery();

        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt(1);
            return count;

        }
        return count;
    }

    public static String generateNextId() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT jobId FROM job_role ORDER BY jobId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String currentId = null;

        if (resultSet.next()) {
            currentId = resultSet.getString(1);
            return splitId(currentId);
        }
        return splitId(null);
    }

    private static String splitId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("J");
            int id = Integer.parseInt(split[1]);
            if (id < 10) {
                id++;
                return "J00" + id;
            } else {
                id++;
                return "J0" + id;
            }
        }
        return "J001";
    }

    public List<designationDto> loadAllJobIds() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM job_role";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<designationDto> jobList = new ArrayList<>();

        while (resultSet.next()) {
            jobList.add(new designationDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getFloat(4),
                    resultSet.getFloat(5)
            ));
        }
        return jobList;
    }

    public List<designationTm> getAllDesignations() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM job_role";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<designationTm> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String jobId = resultSet.getString(1);
            String jobTitle = resultSet.getString(2);
            int workingHrs = resultSet.getInt(3);
            float basicSalary = resultSet.getFloat(4);
            float otPayments = resultSet.getFloat(5);
            int count = getEmpCount(jobId);

            var dto = new designationTm(jobId,jobTitle,workingHrs,basicSalary,otPayments,count);
            dtoList.add(dto);
        }
        return dtoList;
    }

    private int getEmpCount(String jobId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(jobId) FROM employee WHERE jobId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, jobId);

        ResultSet resultSet = pstm.executeQuery();

        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt(1);
            return count;

        }
        return count;
    }

    public static String getDesignation(String jobId) throws SQLException {
        String name = null;
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT jobTitle from job_role WHERE jobId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, jobId);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            name = resultSet.getString(1);
            return name;
        }
        return name;
    }
    public boolean saveDesignation(designationDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO job_role VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getJboId());
        pstm.setString(2, dto.getJobTitle());
        pstm.setInt(3, dto.getWorkingHoursPerMonth());
        pstm.setFloat(4, dto.getBasicSalary());
        pstm.setFloat(5, dto.getOtPaymentsPerHour());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public boolean updateDesignation(designationDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE job_role SET jobTitle = ?, workingHoursPerMonth = ? ,basicSalary = ?, otPaymentsPerHour = ? WHERE jobId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getJobTitle());
        pstm.setInt(2, dto.getWorkingHoursPerMonth());
        pstm.setFloat(3, dto.getBasicSalary());
        pstm.setFloat(4, dto.getOtPaymentsPerHour());
        pstm.setString(5, dto.getJboId());

        return pstm.executeUpdate() > 0;
    }

    public boolean deleteDesignation(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM job_role WHERE jobId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static designationDto searchDesignation(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM job_role WHERE jobId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        designationDto dto = null;

        if(resultSet.next()) {
            String jobId = resultSet.getString(1);
            String jobTitle = resultSet.getString(2);
            int workinghrs = resultSet.getInt(3);
            float basicSalary = resultSet.getFloat(4);
            float otPayments = resultSet.getFloat(5);

            dto = new designationDto(jobId,jobTitle,workinghrs,basicSalary,otPayments);
        }

        return dto;

    }
}
