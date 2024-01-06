package lk.ijse.laboratory.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.laboratory.Dto.Tm.EmployeeTm;
import lk.ijse.laboratory.Dto.Tm.machineTm;
import lk.ijse.laboratory.Dto.designationDto;
import lk.ijse.laboratory.Dto.machineDto;
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
public class machineModel {


    public static List<machineDto> getAllMachines() throws SQLException {

        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM machine";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<machineDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String  machineId= resultSet.getString(1);
            String secId = resultSet.getString(2);
            String machineName = resultSet.getString(3);
            String condition = resultSet.getString(4);


            var dto = new machineDto(machineId,secId,machineName,condition);
            dtoList.add(dto);
        }
        return dtoList;
    }


    public static List<String> loadAllMachineIds() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT distinct secId FROM machine";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<String> Idlist = new ArrayList<>();

        while (resultSet.next()) {
            Idlist.add(resultSet.getString(1));
        }
            return Idlist;

    }

    public boolean saveMachine(machineDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO machine VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getMachineId());
        pstm.setString(2, dto.getMachineName());
        pstm.setString(3, dto.getSecId());
        pstm.setString(4, dto.getStatus());


        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public boolean deleteMachine(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM machine WHERE machineId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public boolean updateMachine(machineDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE machine SET status = ? WHERE machineId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getStatus());
        pstm.setString(2, dto.getMachineId());



        return pstm.executeUpdate() > 0;
    }

    public machineDto searchMachinesByName(String mcName) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM machine WHERE machineName = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, mcName);

        ResultSet resultSet = pstm.executeQuery();

        machineDto dto = null;

        if(resultSet.next()) {
            String machineId = resultSet.getString(1);
            String machineName = resultSet.getString(2);
            String sectionId = resultSet.getString(3);
            String status = resultSet.getString(4);


            dto = new machineDto(machineId,machineName,sectionId,status);
        }

        return dto;

    }

    public static machineDto searchMachinesById(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM machine WHERE machineId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        machineDto dto = null;

        if(resultSet.next()) {
            String machineId = resultSet.getString(1);
            String machineName = resultSet.getString(2);
            String sectionId = resultSet.getString(3);
            String status = resultSet.getString(4);


            dto = new machineDto(machineId,machineName,sectionId,status);
        }

        return dto;

    }
}

