package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.Tm.stockUsageTm;
import lk.ijse.laboratory.Dto.stockUsageDto;
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

public class stockUsageModel {
    public static List<stockUsageDto> loadAllusages(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM stock_usage WHERE testId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet resultSet = pstm.executeQuery();

        List<stockUsageDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            var dto = new stockUsageDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3)
            );

            dtoList.add(dto);
        }

        return dtoList;
    }

    public static boolean saveUsage(List<stockUsageTm> tmList) throws SQLException {
        for (stockUsageTm usageTm : tmList) {
            if(!saveItemUsage(usageTm)) {
                return false;
            }
        }
        return true;
    }

    public static boolean saveItemUsage(stockUsageTm dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO stock_usage VALUES(?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getTestCode());
        pstm.setString(2, dto.getItemCode());
        pstm.setInt(3, dto.getUsagePerTest());


        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public static List<stockUsageDto> getUsages(String testId) throws SQLException {
        List<stockUsageDto> usageList = new ArrayList<>();

            Connection connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM stock_usage WHERE testId = ?";
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setString(1, testId);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                var dto = new stockUsageDto(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3)
                );
                usageList.add(dto);
            }

        return usageList;
    }
}
