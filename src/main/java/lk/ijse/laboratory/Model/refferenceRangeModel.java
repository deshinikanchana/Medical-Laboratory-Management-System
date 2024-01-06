package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.refferenceRangesDto;
import lk.ijse.laboratory.Dto.subTestDto;
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

public class refferenceRangeModel {

    public static refferenceRangesDto getRanges(String sId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM refference_range Where subTestId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, sId);

        ResultSet resultSet = pstm.executeQuery();
        refferenceRangesDto dto = new refferenceRangesDto();
        while (resultSet.next()) {
            String subTestId = resultSet.getString(1);
            String ranges = resultSet.getString(2);
            String unit = resultSet.getString(3);


            dto = new refferenceRangesDto(subTestId,ranges,unit);
        }
        return dto;
    }

    public static List<refferenceRangesDto> getRangeList(String subId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM refference_range Where subTestId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, subId);


        List<refferenceRangesDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String subTId = resultSet.getString(1);
            String ranges = resultSet.getString(2);
            String unit = resultSet.getString(3);


            var dto = new refferenceRangesDto(subTId,ranges,unit);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<refferenceRangesDto> getAllRanges() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM refference_range";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<refferenceRangesDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String subTId = resultSet.getString(1);
            String ranges = resultSet.getString(2);
            String unit = resultSet.getString(3);


            var dto = new refferenceRangesDto(subTId,ranges,unit);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public boolean saveRanges(refferenceRangesDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO refference_range VALUES(?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getSubTestId());
        pstm.setString(2, dto.getRanges());
        pstm.setString(3, dto.getUnit());


        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public String getLastTestCode() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT testId FROM test ORDER BY testId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String lastId = null;

        if (resultSet.next()) {
            lastId = resultSet.getString(1);
        }
       return lastId;
    }

    public boolean updateRanges(refferenceRangesDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE refference_range SET ranges = ?, unit = ? WHERE subTestId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getRanges());
        pstm.setString(2, dto.getUnit());
        pstm.setString(3, dto.getSubTestId());


        return pstm.executeUpdate() > 0;
    }

    public boolean deleteRanges(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM refference_range WHERE subTestId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }
}
