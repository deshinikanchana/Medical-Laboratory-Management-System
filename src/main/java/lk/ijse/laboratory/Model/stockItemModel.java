package lk.ijse.laboratory.Model;

import javafx.scene.control.Alert;
import lk.ijse.laboratory.Dto.Tm.prescriptionTm;
import lk.ijse.laboratory.Dto.stockItemDto;
import lk.ijse.laboratory.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lk.ijse.laboratory.Dto.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data

public class stockItemModel {


    public static String generateNextItemCode() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT itemCode FROM stock_item ORDER BY itemCode DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String currentItemId = null;

        if (resultSet.next()) {
            currentItemId = resultSet.getString(1);
            return splitItemId(currentItemId);
        }
        return splitItemId(null);
    }

    private static String splitItemId(String currentItemId) {
        if (currentItemId != null) {
            String[] split = currentItemId.split("I");
            int id = Integer.parseInt(split[1]);
            if (id < 10) {
                id++;
                return "I00" + id;
            } else {
                id++;
                return "I0" + id;
            }
        }
        return "I001";
    }

    public static boolean updateItemQty(List<prescriptionTm>  Test, Connection connection) throws SQLException {
       boolean isOk = false;
      // Connection conect = DbConnection.getInstance().getConnection();
       try {
           for(prescriptionTm tst : Test) {
               List<stockUsageDto> stock = stockUsageModel.getUsages(tst.getTestCode());
               for (stockUsageDto dto : stock) {

                   String sql = "UPDATE stock_item SET qtyOnHand = qtyOnHand - ?  WHERE itemCode = ?";
                   PreparedStatement pstm = connection.prepareStatement(sql);

                   pstm.setInt(1, dto.getQtyPerTest());
                   pstm.setString(2, dto.getItemCode());

                   isOk = pstm.executeUpdate() > 0;
                   System.out.println("item qty ok");
               }
           }

       } catch (Exception e) {
           new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
       }
        return isOk;
    }

    public static int warningStocks() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(itemCode) FROM stock_item WHERE qtyOnHand <= warningLevel";
        PreparedStatement pstm = connection.prepareStatement(sql);


        ResultSet resultSet = pstm.executeQuery();

        int count = 0;

        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        return count;
    }

    public static List<String> loadcategories() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "select distinct category from stock_item";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<String> list = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String cat = resultSet.getString(1);
            list.add(cat);
        }
        return list;
    }

    public boolean saveItem(stockItemDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO stock_item VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getItemCode());
        pstm.setString(2, dto.getUserId());
        pstm.setString(3, dto.getDescription());
        pstm.setInt(4, dto.getQtyOnHand());
        pstm.setString(5, dto.getCategory());
        pstm.setInt(6, dto.getWarningLevel());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public static List<stockItemDto> loadAllItems() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM stock_item";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<stockItemDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String itemCode = resultSet.getString(1);
            String userId = resultSet.getString(2);
            String description = resultSet.getString(3);
            int qtyOnHand = resultSet.getInt(4);
            String category = resultSet.getString(5);
            int warn = resultSet.getInt(6);

            var Dto = new stockItemDto(itemCode,userId,description,qtyOnHand,category,warn);

            dtoList.add(Dto);
        }
        return dtoList;
    }

    public boolean updateItem(stockItemDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE stock_item SET description = ?, qtyOnHand = ?, category = ? warningLevel = ? WHERE itemCode = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getDescription());
        pstm.setInt(2, dto.getQtyOnHand());
        pstm.setString(3, dto.getCategory());
        pstm.setInt(4, dto.getWarningLevel());
        pstm.setString(5, dto.getItemCode());

        return pstm.executeUpdate() > 0;
    }

    public boolean deleteItem(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM stock_item WHERE itemCode = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static stockItemDto searchItemId(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM stock_item WHERE itemCode = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        stockItemDto dto = null;

        if (resultSet.next()) {
            String itemCode = resultSet.getString(1);
            String userId = resultSet.getString(2);
            String description = resultSet.getString(3);
            int qtyOnHand = resultSet.getInt(4);
            String category = resultSet.getString(5);
            int warn = resultSet.getInt(6);
            dto = new stockItemDto(itemCode,userId,description,qtyOnHand,category,warn);
        }

        return dto;
    }

    public String searchItemName(String name) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM stock_item WHERE description = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, name);

        ResultSet resultSet = pstm.executeQuery();

            String Id = null;
        if (resultSet.next()) {
            Id = resultSet.getString(1);
        }

        return Id;
    }
}
