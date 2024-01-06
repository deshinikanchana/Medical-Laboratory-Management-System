package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.SavePrescriptionDto;
import lk.ijse.laboratory.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
@Data

public class savePrescriptionModel {

    private final reportModel RepModel = new reportModel();

    private final prescriptionModel presModel = new prescriptionModel();

    private final stockItemModel itemModel = new stockItemModel();
    public  boolean savePrescription(SavePrescriptionDto savedDto) throws SQLException {
                boolean result = false;
                Connection connection = null;
                try {
                    connection = DbConnection.getInstance().getConnection();
                    connection.setAutoCommit(false);
                    boolean isPrescriptionSaved = presModel.savePrescription(savedDto.getPresId(),savedDto.getPtId(),savedDto.getRefBy(),savedDto.getTotal(),savedDto.getDuePayment(),connection);
                    if (isPrescriptionSaved) {
                        System.out.println("presModel eka ok");
                        boolean isDone = RepModel.addReport(savedDto.getPresId(), savedDto.getDate(), savedDto.getTmList(),connection);
                        if(isDone) {
                            System.out.println("rep model ekath ok");
                            boolean isUpdated = itemModel.updateItemQty(savedDto.getTmList(), connection);
                            if (isUpdated) {
                                System.out.println("itemModel ekath ok");
                                connection.commit();
                                result = true;
                            }
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Me transaction eke catch eka");
                    connection.rollback();
                } finally {
                    connection.setAutoCommit(true);
                }
                return result;
    }
}
