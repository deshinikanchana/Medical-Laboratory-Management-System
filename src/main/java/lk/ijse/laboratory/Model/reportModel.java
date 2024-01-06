package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.Tm.prescriptionTm;
import lk.ijse.laboratory.Dto.ptTestDetailsDto;
import lk.ijse.laboratory.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data

public class reportModel {

    public static boolean addReport(String presId, Date date, List<prescriptionTm> tmList, Connection connection) throws SQLException {
        boolean y = false;
        for (prescriptionTm presTm : tmList) {
            String id = presTm.getTestCode();

        // Connection conection = DbConnection.getInstance().getConnection();

            String sql = "INSERT INTO patient_test_details VALUES(?, ?, ?, ?, ?)";
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setDate(1, date);
            pstm.setString(2, presId);
            pstm.setString(3, id);
            pstm.setString(4, "report Not Ready");
            pstm.setString(5, "-");

            System.out.println("meke awlk nae.me report eka");

           y =  pstm.executeUpdate() > 0;

        }
        return y;
    }

    public static int getCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(testId) FROM patient_test_details WHERE date = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setDate(1, java.sql.Date.valueOf(LocalDate.now()));


        ResultSet resultSet = pstm.executeQuery();

        int count = 0;

        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        return count;
    }

    public static int getPendingReports() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT count(presId) FROM patient_test_details WHERE status = 'report Not Ready'";
        PreparedStatement pstm = connection.prepareStatement(sql);


        ResultSet resultSet = pstm.executeQuery();

        int count = 0;

        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        return count;
    }

    public static List<String> getTestId(String presId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT testId FROM patient_test_details where presId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, presId);
        List<String> IdList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String id = resultSet.getString(1);

            IdList.add(id);
            System.out.println(id);
        }
        return IdList;
    }

    public static String getDate(String pres) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT date FROM patient_test_details where presId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, pres);
        ResultSet resultSet = pstm.executeQuery();
        String date = " ";
        while (resultSet.next()) {
            Date d = resultSet.getDate(1);

            date = String.valueOf(d);
        }

        return date;
    }

    public List<ptTestDetailsDto> getAllReports() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM patient_test_details";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<ptTestDetailsDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            Date date = resultSet.getDate(1);
            String presId = resultSet.getString(2);
            String testId = resultSet.getString(3);
            String status = resultSet.getString(4);
            String comment = resultSet.getString(5);


            var dto = new ptTestDetailsDto(date,presId,testId,status,comment);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public static List<ptTestDetailsDto> loadTestIds(String Id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM patient_test_details WHERE presId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, Id);
        List<ptTestDetailsDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            Date date = resultSet.getDate(1);
            String presId = resultSet.getString(2);
            String testId = resultSet.getString(3);
            String status = resultSet.getString(4);
            String comment = resultSet.getString(5);


            var dto = new ptTestDetailsDto(date,presId,testId,status,comment);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public static boolean updateReport(String st, String com, String pr, String test) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE patient_test_details SET status = ?, comment = ?  WHERE presId = ? AND testId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,st);
        pstm.setString(2, com);
        pstm.setString(3, pr);
        pstm.setString(4, test);



        return pstm.executeUpdate() > 0;
    }
}
