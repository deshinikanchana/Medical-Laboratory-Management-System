package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.prescriptionDto;
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
public class prescriptionModel {

    public static List<prescriptionDto> loadAllPrescriptions() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM prescription";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<prescriptionDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String presId = resultSet.getString(1);
            String ptId = resultSet.getString(2);
            String refferedBy = resultSet.getString(3);
            Double total = resultSet.getDouble(4);
            Double due = resultSet.getDouble(5);


            var dto = new prescriptionDto(presId,ptId,refferedBy,total,due);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public static String SearchPrescriptionByPresId(String value) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM prescription WHERE presId = ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, value);

        ResultSet resultSet = pstm.executeQuery();
        String ptId = null;
        if (resultSet.next()) {
             ptId = resultSet.getString(2);
        }

        return ptId;
    }

    public static int getCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(presId) FROM prescription WHERE duePayment > 0";
        PreparedStatement pstm = connection.prepareStatement(sql);


        ResultSet resultSet = pstm.executeQuery();

        int count = 0;

        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        return count;
    }

    public String generateNextPrescriptionId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT presId FROM prescription ORDER BY presId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String lastId = null;

        if (resultSet.next()) {
            lastId = resultSet.getString(1);
            return splitPtId(lastId);
        }
        return splitPtId(null);
    }

    private String splitPtId(String lastId) {
        if (lastId != null) {
            String[] split = lastId.split("r");
            int id = Integer.parseInt(split[1]);
            if (id < 9) {
                id++;
                return "Pr00" + id;
            } else {
                id++;
                return "Pr0" + id;
            }
        }
        return "Pr001";
    }

    public static prescriptionDto SearchPrescriptionById(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM prescription WHERE ptId = ? ORDER BY presId DESC LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        prescriptionDto dto = null;

        if (resultSet.next()) {
            String presId = resultSet.getString(1);
            String ptId = resultSet.getString(2);
            String refBy = resultSet.getString(3);
            Double totalAmount = resultSet.getDouble(4);
            Double duePayment = resultSet.getDouble(5);

            dto = new prescriptionDto(presId,ptId,refBy,totalAmount,duePayment);
        }

        return dto;
    }

    public static prescriptionDto SearchPrescriptionPresId(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM prescription WHERE presId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        prescriptionDto dto = null;

        if (resultSet.next()) {
            String presId = resultSet.getString(1);
            String ptId = resultSet.getString(2);
            String refBy = resultSet.getString(3);
            Double totalAmount = resultSet.getDouble(4);
            Double duePayment = resultSet.getDouble(5);

            dto = new prescriptionDto(presId,ptId,refBy,totalAmount,duePayment);
        }

        return dto;
    }

    public static boolean savePrescription(String presId, String ptId, String refBy, float total, float duePayment, Connection connection) throws SQLException {
       boolean isdone = false;

            String sql = "INSERT INTO prescription VALUES(?, ?, ?, ?, ?)";
            PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setString(1, presId);
            pstm.setString(2, ptId);
            pstm.setString(3, refBy);
            pstm.setFloat(4, total);
            pstm.setFloat(5, duePayment);

             isdone = pstm.executeUpdate() > 0;
        return isdone;
    }
}
