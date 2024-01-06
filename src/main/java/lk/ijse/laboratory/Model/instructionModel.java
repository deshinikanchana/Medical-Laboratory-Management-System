package lk.ijse.laboratory.Model;

import lk.ijse.laboratory.Dto.machineDto;
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

public class instructionModel {

    public static ArrayList<String> getInstructions(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM instruction where testId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,id);
        ArrayList<String> ins = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
          ins.add(resultSet.getString(2));
        }
        return ins;
    }
}
