package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.Tm.resultTm;
import lk.ijse.laboratory.Dto.resultDto;
import lk.ijse.laboratory.db.DbConnection;

import java.sql.*;
import java.util.List;

public class resultModel {

    public static boolean saveResult(List<resultTm> tmList, String PD) throws SQLException {
        for (resultTm RTM : tmList) {
            if(!saveResults(RTM,PD)) {
                return false;
            }
        }
        return true;
    }

    public static boolean saveResults(resultTm dto,String pd) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO result VALUES(?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, pd);
        pstm.setString(2, dto.getSubTestCode());
        pstm.setString(3, dto.getResult());


        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public static resultDto getResults(String subId, String presId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM result WHERE subTestId = ? AND presId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, subId);
        pstm.setString(2, presId);
        resultDto dto = null;
        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String pres = resultSet.getString(1);
            String sub = resultSet.getString(2);
            String res = resultSet.getString(3);

            dto = new resultDto(pres,sub,res);

        }
        return dto;
    }
}
