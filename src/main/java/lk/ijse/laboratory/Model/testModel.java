package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.Tm.EmployeeTm;
import lk.ijse.laboratory.Dto.employeeDto;
import lk.ijse.laboratory.Dto.patientDto;
import lk.ijse.laboratory.Dto.testDto;
import lk.ijse.laboratory.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Data
public class testModel {


    public static String getTestName(String id) throws SQLException {
        String name = null;
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT test FROM test WHERE testId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            name = resultSet.getString(1);
        }
        return name;

         }

    public static List<testDto> loadAllTests() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM test";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<testDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String testId = resultSet.getString(1);
            String secId = resultSet.getString(2);
            String test = resultSet.getString(3);
            String estTime = resultSet.getString(4);
            float testPrice = resultSet.getFloat(5);
            String sampleType = resultSet.getString(6);
            String machineId = resultSet.getString(7);


            var dto = new testDto(testId,secId,test,estTime,testPrice,sampleType,machineId);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public static testDto searchTestById(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM test WHERE testId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        testDto dto = new testDto();

        while (resultSet.next()) {
            String testId = resultSet.getString(1);
            String secId = resultSet.getString(2);
            String test = resultSet.getString(3);
            String estTime = resultSet.getString(4);
            float testPrice = resultSet.getFloat(5);
            String sampleType = resultSet.getString(6);
            String machineId = resultSet.getString(7);


            dto = new testDto(testId,secId,test,estTime,testPrice,sampleType,machineId);

        }
        return dto;
    }

    public static String generateNextTestId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT testId FROM test ORDER BY testId DESC LIMIT 1";
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
            String[] split = currentId.split("T");
            int id = Integer.parseInt(split[1]);
            if (id < 10) {
                id++;
                return "T00" + id;
            } else {
                id++;
                return "T0" + id;
            }
        }
        return "T001";
    }

    public static List<testDto> getTestLoad(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM test WHERE testId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);
        List<testDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String testId = resultSet.getString(1);
            String secId = resultSet.getString(2);
            String test = resultSet.getString(3);
            String estTime = resultSet.getString(4);
            float testPrice = resultSet.getFloat(5);
            String sampleType = resultSet.getString(6);
            String machineId = resultSet.getString(7);


            var dto = new testDto(testId,secId,test,estTime,testPrice,sampleType,machineId);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public testDto searchTestByName(String name) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM test WHERE test = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, name);

        ResultSet resultSet = pstm.executeQuery();

        testDto dto = null;

        while (resultSet.next()) {
            String testId = resultSet.getString(1);
            String secId = resultSet.getString(2);
            String test = resultSet.getString(4);
            String estTime = resultSet.getString(5);
            float testPrice = resultSet.getFloat(6);
            String sampleType = resultSet.getString(7);
            String machineId = resultSet.getString(8);


            dto = new testDto(testId,secId,test,estTime,testPrice,sampleType,machineId);

        }
        return dto;
    }

    public boolean saveTest(testDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO test VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getTestId());
        pstm.setString(2, dto.getSecId());
        pstm.setString(3, dto.getTest());
        pstm.setString(4, dto.getEstimatedTime());
        pstm.setFloat(5, dto.getPrice());
        pstm.setString(6, dto.getSampleType());
        pstm.setString(7, dto.getMachineId());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public boolean updateTest(testDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE test SET estimatedTime = ?, testPrice = ?  WHERE testId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getEstimatedTime());
        pstm.setFloat(2, dto.getPrice());
        pstm.setString(3, dto.getTestId());


        return pstm.executeUpdate() > 0;
    }

    public boolean deleteTest(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM test WHERE testId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }
}
