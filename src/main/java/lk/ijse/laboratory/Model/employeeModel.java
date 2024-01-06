package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.Tm.EmployeeTm;
import lk.ijse.laboratory.Dto.employeeDto;
import lk.ijse.laboratory.Dto.prescriptionDto;
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

public class employeeModel {

    public static String generateNextEmployeeId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT empId FROM employee ORDER BY empId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String currentEmployeeId = null;

        if (resultSet.next()) {
            currentEmployeeId = resultSet.getString(1);
            return splitEmployeeId(currentEmployeeId);
        }
        return splitEmployeeId(null);
    }

    private static String splitEmployeeId(String currentEmployeeId) {
        if (currentEmployeeId != null) {
            String[] split = currentEmployeeId.split("E");
            int id = Integer.parseInt(split[1]);
            if (id < 10) {
                id++;
                return "E00" + id;
            } else {
                id++;
                return "E0" + id;
            }
        }
        return "E001";
    }

    public static List<employeeDto> loadAllemployees() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        List<employeeDto> dtoList = new ArrayList<>();

        String sql = "SELECT * FROM employee";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String emp_id = resultSet.getString(1);
            String emp_jobId = resultSet.getString(2);
            String emp_userId = resultSet.getString(3);
            String emp_name = resultSet.getString(4);
            String emp_Nic = resultSet.getString(5);
            String emp_Address = resultSet.getString(6);
            String emp_Email = resultSet.getString(7);
            String emp_Telno = resultSet.getString(8);

var Dto = new employeeDto(emp_id,emp_jobId,emp_userId,emp_name,emp_Nic,emp_Address,emp_Email,emp_Telno);

            dtoList.add(Dto);
        }
        return dtoList;
    }

    public static int getJobId(String empId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT jobId FROM employee WHERE empId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, empId);

        ResultSet resultSet = pstm.executeQuery();
        String job = null;
        if (resultSet.next()) {
            job = resultSet.getString(1);
        }
        return designationModel.getWorkingHours(job);
    }

    public boolean saveEmployee(employeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO employee VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getEmpId());
        pstm.setString(2, dto.getJobId());
        pstm.setString(3, dto.getUserId());
        pstm.setString(4, dto.getName());
        pstm.setString(5, dto.getNic());
        pstm.setString(6, dto.getAddress());
        pstm.setString(7, dto.getEmail());
        pstm.setString(8, dto.getTelNo());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public String SearchJobRole(String id) throws SQLException {
        String jobTitle = null;
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT jobTitle FROM job_role WHERE jobId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            jobTitle = resultSet.getString(1);
        }
        return jobTitle;
    }

    public List<employeeDto> getAllEmployees() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<employeeDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String emp_id = resultSet.getString(1);
            String emp_jobId = resultSet.getString(2);
            String userId = resultSet.getString(3);
            String emp_name = resultSet.getString(4);
            String emp_Nic = resultSet.getString(5);
            String emp_Address = resultSet.getString(6);
            String emp_Email = resultSet.getString(7);
            String emp_Telno = resultSet.getString(8);


            var dto = new employeeDto(emp_id,emp_jobId,userId,emp_Nic,emp_name,emp_Email, emp_Telno,emp_Address);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public employeeDto searchEmployeeByNIC(String nic) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee WHERE nic = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, nic);

        ResultSet resultSet = pstm.executeQuery();

        employeeDto dto = null;

        if (resultSet.next()) {
            String emp_id = resultSet.getString(1);
            String emp_jobId = resultSet.getString(2);
            String emp_userId = resultSet.getString(3);
            String emp_name = resultSet.getString(4);
            String emp_Nic = resultSet.getString(5);
            String emp_Address = resultSet.getString(6);
            String emp_Email = resultSet.getString(7);
            String emp_Telno = resultSet.getString(8);

            dto = new employeeDto(emp_id, emp_jobId, emp_userId, emp_name, emp_Nic, emp_Address, emp_Email, emp_Telno);
        }

        return dto;
    }

    public employeeDto searchEmployeeByName(String name) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee WHERE name = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, name);

        ResultSet resultSet = pstm.executeQuery();

        employeeDto dto = null;

        if (resultSet.next()) {
            String emp_id = resultSet.getString(1);
            String emp_jobId = resultSet.getString(2);
            String emp_userId = resultSet.getString(3);
            String emp_name = resultSet.getString(4);
            String emp_Nic = resultSet.getString(5);
            String emp_Address = resultSet.getString(6);
            String emp_Email = resultSet.getString(7);
            String emp_Telno = resultSet.getString(8);

            dto = new employeeDto(emp_id, emp_jobId, emp_userId, emp_name, emp_Nic, emp_Address, emp_Email, emp_Telno);
        }

        return dto;
    }

    public static employeeDto searchEmployeeById(String eId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee WHERE empId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, eId);

        ResultSet resultSet = pstm.executeQuery();

        employeeDto dto = null;

        if (resultSet.next()) {
            String emp_id = resultSet.getString(1);
            String emp_jobId = resultSet.getString(2);
            String emp_userId = resultSet.getString(3);
            String emp_name = resultSet.getString(4);
            String emp_Nic = resultSet.getString(5);
            String emp_Address = resultSet.getString(6);
            String emp_Email = resultSet.getString(7);
            String emp_Telno = resultSet.getString(8);

            dto = new employeeDto(emp_id, emp_jobId, emp_userId, emp_name, emp_Nic, emp_Address, emp_Email, emp_Telno);
        }

        return dto;
    }

    public boolean updateEmployee(employeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE employee SET jobId = ?, address = ?, email = ?, telNo = ? WHERE empId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getJobId());
        pstm.setString(2, dto.getAddress());
        pstm.setString(3, dto.getEmail());
        pstm.setString(4, dto.getTelNo());
        pstm.setString(5, dto.getEmpId());

        return pstm.executeUpdate() > 0;
    }

    public boolean deleteEmployee(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "Delete from employee where empId = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,"id");
        return pstm.executeUpdate() > 0;
    }
}
