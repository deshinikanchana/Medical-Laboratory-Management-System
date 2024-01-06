package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.subTestDto;
import lk.ijse.laboratory.Dto.testDto;
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

public class subTestModel {
    public static List<subTestDto> getAllsubTests(String text) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM sub_test Where testId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, text);

        List<subTestDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String testId = resultSet.getString(1);
            String subTestId = resultSet.getString(2);
            String name = resultSet.getString(3);


            var dto = new subTestDto(testId,subTestId,name);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public static subTestDto getsubTest(String subTestId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM sub_test Where subTestId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, subTestId);

        List<subTestDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
            subTestDto dto = new subTestDto();
        while (resultSet.next()) {
            String testId = resultSet.getString(1);
            String STestId = resultSet.getString(2);
            String name = resultSet.getString(3);


           dto = new subTestDto(testId, STestId,name);
        }
        return dto;
    }

    public static String getLastsubTestId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT subTestId FROM sub_test ORDER BY subTestId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String id = null;

        if (resultSet.next()) {
            id = resultSet.getString(1);
        }
        return id;
    }

    public static String generateNextTestId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT subTestId FROM sub_test ORDER BY subTestId DESC LIMIT 1";
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
                return "ST000" + id;
            } else if(id<100){
                id++;
                return "ST00" + id;
            }else if(id < 1000){
                id++;
                return "ST0" + id;
            }else{
                id++;
                return "ST" + id;
            }
        }
        return "ST0001";
    }

    public static List<subTestDto> loadSubTestIds(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM sub_test WHERE testId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);
        List<subTestDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String testId = resultSet.getString(1);
            String subTestId = resultSet.getString(2);
            String test = resultSet.getString(3);


            var dto = new subTestDto(testId,subTestId,test);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
