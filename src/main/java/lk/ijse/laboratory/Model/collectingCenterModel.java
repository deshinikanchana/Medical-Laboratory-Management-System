package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.Tm.collectingCenterTm;
import lk.ijse.laboratory.Dto.Tm.sectionTm;
import lk.ijse.laboratory.Dto.collectingCenterDto;
import lk.ijse.laboratory.Dto.sectionDto;
import lk.ijse.laboratory.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class collectingCenterModel {
    public static String generateNextCenterId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT ccId FROM collecting_center ORDER BY ccId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String currentCCId = null;

        if (resultSet.next()) {
            currentCCId = resultSet.getString(1);
            return splitCCId(currentCCId);
        }
        return splitCCId(null);
    }

    private static String splitCCId(String currentCCId) {
        if (currentCCId != null) {
            String[] split = currentCCId.split("C");
            int id = Integer.parseInt(split[1]);
            if (id < 10) {
                id++;
                return "C00" + id;
            } else {
                id++;
                return "C0" + id;
            }
        }
        return "C001";
    }


    public static collectingCenterDto searchCenterByName(String name) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM collecting_center WHERE centerName = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, name);

        ResultSet resultSet = pstm.executeQuery();

        collectingCenterDto dto = null;

        if (resultSet.next()) {
            String CcId = resultSet.getString(1);
            String centerName = resultSet.getString(2);
            String address = resultSet.getString(3);
            String telNo = resultSet.getString(4);
            String email = resultSet.getString(5);

            dto = new collectingCenterDto(CcId,centerName,address,telNo,email);
        }

        return dto;
    }


    public List<collectingCenterTm> getAllSections() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM collecting_center";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<collectingCenterTm> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String CcId = resultSet.getString(1);
            String centerName = resultSet.getString(2);
            String address = resultSet.getString(3);
            String telNo = resultSet.getString(4);
            String email = resultSet.getString(5);
            int count = getSampleCount(CcId);
            var dto = new collectingCenterTm(CcId,centerName,address,telNo,email,count);
            dtoList.add(dto);
        }
        return dtoList;
    }

    private int getSampleCount(String CcId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(ccId) FROM patient WHERE ccId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, CcId);

        ResultSet resultSet = pstm.executeQuery();

        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt(1);
            return count;

        }
        return count;
    }

    public boolean saveCenter(collectingCenterDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO collecting_center VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getCcId());
        pstm.setString(2, dto.getCenterName());
        pstm.setString(3, dto.getAddress());
        pstm.setString(4, dto.getTelNo());
        pstm.setString(5, dto.getEmail());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public boolean updateCenter(collectingCenterDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE collecting_center SET address = ?,telNo = ?,email = ? WHERE ccId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getAddress());
        pstm.setString(2, dto.getTelNo());
        pstm.setString(3, dto.getEmail());
        pstm.setString(4, dto.getCcId());


        return pstm.executeUpdate() > 0;
    }

    public boolean deleteCenter(String ccId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM collecting_center WHERE ccId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, ccId);

        return pstm.executeUpdate() > 0;
    }

    public static collectingCenterDto searchCenterById(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM collecting_center WHERE ccId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        collectingCenterDto dto = null;

        if (resultSet.next()) {
            String CcId = resultSet.getString(1);
            String centerName = resultSet.getString(2);
            String address = resultSet.getString(3);
            String telNo = resultSet.getString(4);
            String email = resultSet.getString(5);

            dto = new collectingCenterDto(CcId,centerName,address,telNo,email);
        }

        return dto;
    }

    public static List<collectingCenterDto> loadAllCenters() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM collecting_center";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<collectingCenterDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            var dto = new collectingCenterDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );

            dtoList.add(dto);
        }

        return dtoList;
    }
}
