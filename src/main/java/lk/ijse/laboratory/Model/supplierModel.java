package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.Tm.designationTm;
import lk.ijse.laboratory.Dto.Tm.supplierTm;
import lk.ijse.laboratory.Dto.employeeDto;
import lk.ijse.laboratory.Dto.supplierDto;
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
public class supplierModel {

    public static String generateNextSupId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT supId FROM supplier ORDER BY supId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String currentId = null;

        if (resultSet.next()) {
            currentId = resultSet.getString(1);
            return splitSupId(currentId);
        }
        return splitSupId(null);
    }

    private static String splitSupId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("S");
            int id = Integer.parseInt(split[1]);
            if (id < 10) {
                id++;
                return "S00" + id;
            } else {
                id++;
                return "S0" + id;
            }
        }
        return "S001";
    }

    public static List<supplierDto> getAllSuppliers() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM supplier";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<supplierDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String supId = resultSet.getString(1);
            String supName = resultSet.getString(2);
            String tel = resultSet.getString(3);
            String email = resultSet.getString(4);
            String category = resultSet.getString(5);



            var dto = new supplierDto(supId,supName,tel,email,category);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public boolean saveSupplier(supplierDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO supplier VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getSupId());
        pstm.setString(2, dto.getName());
        pstm.setString(3, dto.getTelNo());
        pstm.setString(4, dto.getEmail());
        pstm.setString(5, dto.getCategory());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public boolean updateSupplier(supplierDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE supplier SET name = ?, telNo = ?, email = ?, category = ? WHERE supId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);


        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getTelNo());
        pstm.setString(3, dto.getEmail());
        pstm.setString(4, dto.getCategory());
        pstm.setString(5, dto.getSupId());

        return pstm.executeUpdate() > 0;
    }

    public boolean deleteSupplier(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM supplier WHERE supId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public supplierDto searchSupplierName(String Name) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM supplier WHERE name = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, Name);

        ResultSet resultSet = pstm.executeQuery();

        supplierDto dto = null;

        if (resultSet.next()) {
            String supId = resultSet.getString(1);
            String name = resultSet.getString(2);
            String tel = resultSet.getString(3);
            String email = resultSet.getString(4);
            String category = resultSet.getString(5);

            dto = new supplierDto(supId,name,tel,email,category);
        }

        return dto;
    }

    public supplierDto searchSupplierId(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM supplier WHERE supId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        supplierDto dto = null;

        if (resultSet.next()) {
            String supId = resultSet.getString(1);
            String name = resultSet.getString(2);
            String tel = resultSet.getString(3);
            String email = resultSet.getString(4);
            String category = resultSet.getString(5);

            dto = new supplierDto(supId,name,tel,email,category);
        }

        return dto;
    }
}
