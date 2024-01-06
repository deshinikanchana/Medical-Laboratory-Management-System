package lk.ijse.laboratory.Model;


import lk.ijse.laboratory.Dto.Tm.ordersTm;
import lk.ijse.laboratory.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class itemOrderDetailsModel {

    public static boolean saveItems(List<ordersTm> tmList, String orderId, Connection connection) throws SQLException {
        for (ordersTm Otm : tmList) {
            if (!saveOrderItems(Otm,orderId,connection)) {
                return false;
            }
        }
        return true;
    }

    private static boolean saveOrderItems(ordersTm otm, String orderId, Connection connection) throws SQLException {
        String sql = "INSERT INTO item_order_details VALUES(?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, otm.getItemCode());
        pstm.setString(2, orderId);
        pstm.setInt(3, otm.getQty());


        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }
}
