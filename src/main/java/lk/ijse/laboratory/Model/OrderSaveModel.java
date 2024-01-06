package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.itemOrderDetailDto;
import lk.ijse.laboratory.db.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class OrderSaveModel {
    public static boolean saveOrder(itemOrderDetailDto savedDto) throws SQLException {
        boolean result = false;
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isItemsSaved = itemOrderDetailsModel.saveItems(savedDto.getTmList(),savedDto.getOrderId(),connection);

            if (isItemsSaved) {
                boolean isAdded = ordersModel.saveOrder(savedDto.getOrderId(),savedDto.getSupplierId(),connection);
                if(isAdded) {
                    connection.commit();
                    result = true;
                }
            }
        } catch (SQLException e) {
            assert connection != null;
            connection.rollback();
        } finally {
            assert connection != null;
            connection.setAutoCommit(true);
        }
        return result;
    }
}
