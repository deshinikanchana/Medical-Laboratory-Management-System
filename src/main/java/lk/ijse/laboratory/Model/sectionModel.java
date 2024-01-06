package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.Tm.sectionTm;
import lk.ijse.laboratory.Dto.designationDto;
import lk.ijse.laboratory.Dto.employeeDto;
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

public class sectionModel {

    public static String generateNextSectionId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT secId FROM section ORDER BY secId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String currentSectionId = null;

        if (resultSet.next()) {
            currentSectionId = resultSet.getString(1);
            return splitSectionId(currentSectionId);
        }
        return splitSectionId(null);
    }

    private static String splitSectionId(String currentSectionId) {
        if (currentSectionId != null) {
            String[] split = currentSectionId.split("S");
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

    public static List<sectionDto> loadAllSectionIds() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM section";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<sectionDto> secList = new ArrayList<>();

        while (resultSet.next()) {
            secList.add(new sectionDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            ));
        }
        return secList;
    }


    public List<sectionTm> getAllSections() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM section";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<sectionTm> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String secId = resultSet.getString(1);
            String secName = resultSet.getString(2);
            String consultant = resultSet.getString(3);
            int count = getTestCount(secId);
            var dto = new sectionTm(secId,secName,consultant,count);
            dtoList.add(dto);
        }
        return dtoList;
    }

    private int getTestCount(String secId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(secId) FROM test WHERE secId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, secId);

        ResultSet resultSet = pstm.executeQuery();

        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt(1);
            return count;

        }
        return count;
    }

    public boolean saveSection(sectionDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO section VALUES(?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getSecId());
        pstm.setString(2, dto.getSecName());
        pstm.setString(3, dto.getConsultant());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public boolean updateSection(sectionDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE section SET consultantName = ? WHERE secId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getConsultant());
        pstm.setString(2, dto.getSecId());


        return pstm.executeUpdate() > 0;
    }

    public boolean deleteSection(String secId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM section WHERE secId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, secId);

        return pstm.executeUpdate() > 0;
    }

    public static sectionDto searchSectionById(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM section WHERE secId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        sectionDto dto = new sectionDto();

        if (resultSet.next()) {
            String secId = resultSet.getString(1);
            String secName = resultSet.getString(2);
            String consultant = resultSet.getString(3);

            dto = new sectionDto(secId,secName,consultant);
        }

        return dto;
    }

    public sectionDto searchSectionBySec(String sec) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM section WHERE secName = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, sec);

        ResultSet resultSet = pstm.executeQuery();

        sectionDto dto = null;

        if (resultSet.next()) {
            String secId = resultSet.getString(1);
            String secName = resultSet.getString(2);
            String consultant = resultSet.getString(3);

            dto = new sectionDto(secId,secName,consultant);
        }

        return dto;
    }
}

