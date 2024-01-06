package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.attendanceDto;
import lk.ijse.laboratory.Dto.employeeDto;
import lk.ijse.laboratory.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@Data
public class attendanceModel {

    public static float calculateTotalHours(String id,int month) throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT sum(TIMEDIFF(outTime,inTime)) FROM attendance WHERE month(date) = ?  AND  empId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, month);
        pstm.setString(2, id);

        ResultSet resultSet = pstm.executeQuery();

       float hours =0;

        if (resultSet.next()) {
             hours = resultSet.getInt(1);

        }
        return (hours/10000);
    }

    public static int getCount(String empId, int mon) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(*) FROM attendance WHERE month(date) = ? AND  empId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, mon);
        pstm.setString(2,empId);

        ResultSet resultSet = pstm.executeQuery();

        int count = 0;

        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        return count;
    }

    public static int getDailyCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(*) FROM attendance WHERE date = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setDate(1, java.sql.Date.valueOf(LocalDate.now()));


        ResultSet resultSet = pstm.executeQuery();

        int count = 0;

        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        return count;

    }

    public List<attendanceDto> getAllAttendances() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM attendance";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<attendanceDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            Date date = resultSet.getDate(2);
            Time inTime = resultSet.getTime(3);
            Time outTime = resultSet.getTime(4);
            String status = resultSet.getString(5);


            var dto = new attendanceDto(id,date,inTime,outTime,status);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public attendanceDto searchAttendance(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM attendance WHERE empId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        attendanceDto dto = null;

        if(resultSet.next()) {
            String Id = resultSet.getString(1);
            Date date = resultSet.getDate(2);
            Time inTime = resultSet.getTime(3);
            Time outTime = resultSet.getTime(4);
            String status = resultSet.getString(5);


            dto = new attendanceDto(Id,date,inTime,outTime,status);
        }

        return dto;
    }

    public List<employeeDto> loadAllEmployeeIds() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<employeeDto> empList = new ArrayList<>();

        while (resultSet.next()) {
            empList.add(new employeeDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
            ));
        }
        return empList;
    }

    public boolean saveAttendance(attendanceDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO attendance VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getEmpId());
        pstm.setDate(2, (java.sql.Date) dto.getDate());
        pstm.setTime(3, dto.getInTime());
        pstm.setTime(4, dto.getOutTime());
        pstm.setString(5, dto.getStatus());


        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public attendanceDto searchAttendancesById(String id, String text) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        java.sql.Date today = java.sql.Date.valueOf(LocalDate.now());
        String sql = "SELECT * FROM attendance WHERE empId = ? AND date = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);
        pstm.setDate(2, java.sql.Date.valueOf(LocalDate.now()));

        ResultSet resultSet = pstm.executeQuery();

        attendanceDto dto = null;

        if (resultSet.next()) {
            String empId = resultSet.getString(1);
            Date date = resultSet.getDate(2);
            Time inTime = resultSet.getTime(3);
            Time outTime = resultSet.getTime(4);
            String status = resultSet.getString(5);


            dto = new attendanceDto(empId,date,inTime,outTime,status);
        }

        return dto;
    }


    public boolean updateattendance(attendanceDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE attendance SET outTime = ?,status = ?  WHERE empId = ?";


        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setTime(1, dto.getOutTime());
        pstm.setString(2, dto.getStatus());
        pstm.setString(3, dto.getEmpId());

        return pstm.executeUpdate() > 0;
    }

    public boolean isMArked(String id) throws SQLException {
        boolean ans = false;
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM attendance WHERE status = 'WORKED' AND empId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        if(resultSet != null){
            ans = true;
        }
        return ans;
    }
}
