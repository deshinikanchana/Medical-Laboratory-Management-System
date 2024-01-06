package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.*;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class ordersModel {

    public static String generateNextOrderId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String currentId = null;

        if (resultSet.next()) {
            currentId = resultSet.getString(1);
            return splitOrderId(currentId);
        }
        return splitOrderId(null);
    }

    private static String splitOrderId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("O");
            int id = Integer.parseInt(split[1]);
            if (id < 10) {
                id++;
                return "O00" + id;
            } else {
                id++;
                return "O0" + id;
            }
        }
        return "O001";
    }

    public static int getOrderCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(orderId) FROM orders WHERE status = 'PENDING'";
        PreparedStatement pstm = connection.prepareStatement(sql);


        ResultSet resultSet = pstm.executeQuery();

        int count = 0;

        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        return count;
    }

    public static boolean saveOrder(String orderId, String supplierId, Connection connection) throws SQLException {
        Date date = Date.valueOf(LocalDate.now());

        String sql = "INSERT INTO orders VALUES(?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, orderId);
        pstm.setString(2, supplierId);
        pstm.setDate(3,date);
        pstm.setString(4,"PENDING");

        return pstm.executeUpdate() > 0;
    }
}
