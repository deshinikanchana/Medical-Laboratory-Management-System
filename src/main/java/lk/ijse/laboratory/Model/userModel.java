package lk.ijse.laboratory.Model;

import javafx.scene.control.Alert;
import lk.ijse.laboratory.Dto.userDto;
import lk.ijse.laboratory.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static lk.ijse.laboratory.db.DbConnection.newDto;

@AllArgsConstructor
@Data
public class userModel {


    public static String generateNextId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT userId FROM user ORDER BY userId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String currentId = null;

        if (resultSet.next()) {
            currentId = resultSet.getString(1);
            return splitUserId(currentId);
        }
        return splitUserId(null);
    }

    private static String splitUserId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("U");
            int id = Integer.parseInt(split[1]);
            if (id < 10) {
                id++;
                return "U00" + id;
            } else {
                id++;
                return "U0" + id;
            }
        }
        return "U001";
    }

    public static List<userDto> getAllUsers() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        List<userDto> dtoList = new ArrayList<>();

        String sql = "SELECT * FROM user";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String type = resultSet.getString(3);
            String pw = resultSet.getString(4);
            String email = resultSet.getString(5);

            var Dto = new userDto(id,name,type,pw,email);

            dtoList.add(Dto);
        }
        return dtoList;
    }

    public static boolean saveUser(userDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO user VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getUserId());
        pstm.setString(2, dto.getUserName());
        pstm.setString(3, dto.getUserType());
        pstm.setString(4, dto.getPassword());
        pstm.setString(5,dto.getEmail());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public static userDto getLastAdmin() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM user WHERE userType = 'Admin' ORDER BY userId DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
        userDto dto = new userDto();
        if (resultSet.next()) {
            dto.setUserId(resultSet.getString(1));
            dto.setUserName(resultSet.getString(2));
            dto.setUserType(resultSet.getString(3));
            dto.setPassword(resultSet.getString(4));
            dto.setEmail(resultSet.getString(5));
            return dto;
        }
        return dto;
    }

    public static userDto getUser(String email) throws SQLException {
        userDto dto = new userDto();
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM user WHERE email = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, email);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            dto.setUserId(resultSet.getString(1));
            dto.setUserName(resultSet.getString(2));
            dto.setUserType(resultSet.getString(3));
            dto.setPassword(resultSet.getString(4));
            dto.setEmail(resultSet.getString(5));
            return dto;
        }

        return dto;

    }

    public static boolean resetPassword(userDto ud) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE user SET password = ? WHERE userId =?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, ud.getPassword());
        pstm.setString(2, ud.getUserId());


        return pstm.executeUpdate() > 0;
    }

    public static boolean saveUserTemp(userDto user, Date dt, Time tm) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO user_temp VALUES(?, ?, ?, ?, ?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, user.getUserId());
        pstm.setString(2, user.getUserName());
        pstm.setString(3, user.getUserType());
        pstm.setString(4, user.getPassword());
        pstm.setString(5,user.getEmail());
        pstm.setDate(6,dt);
        pstm.setTime(7,tm);

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public static List<userDto> searchNewUsers(Date today, Time just) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        List<userDto> dtoList = new ArrayList<>();

        String sql = "SELECT * FROM user_temp where date < ? and time <= ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setDate(1, today);
        pstm.setTime(2, just);

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String type = resultSet.getString(3);
            String pw = resultSet.getString(4);
            String email = resultSet.getString(5);

            var Dto = new userDto(id,name,type,pw,email);

            dtoList.add(Dto);
        }
        return dtoList;
    }

    public static boolean deleteTempUsers(userDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "Delete from user_temp where userId = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getUserId());
        return pstm.executeUpdate() > 0;
    }

    public userDto CheckCredential(userDto dtoUser) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        if (dtoUser.getUserName().length() > 0 && dtoUser.getPassword().length() > 0) {

            try {
                String sql = "SELECT * FROM user WHERE userName = ? AND password = ?";
                PreparedStatement pstm = connection.prepareStatement(sql);
                pstm.setString(1, dtoUser.getUserName());
                pstm.setString(2, dtoUser.getPassword());


                ResultSet resultSet = pstm.executeQuery();

                if (resultSet.next()) {
                    dtoUser.setUserId(resultSet.getString(1));
                    dtoUser.setUserType(resultSet.getString(3));
                    dtoUser.setEmail(resultSet.getString(5));
                    return dtoUser;
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
        new Alert(Alert.AlertType.ERROR, "Invalid Username Or Password !!!").show();
        return dtoUser;
    }


    public boolean updateMyProfile(userDto dto) throws SQLException {
       Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE user SET userName = ?, password = ? ,email =? WHERE userId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getUserName());
        pstm.setString(2, dto.getPassword());
        pstm.setString(3,dto.getEmail());
        pstm.setString(4, newDto.getUserId());

        return pstm.executeUpdate() > 0;
    }

    public userDto searchUser(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM user WHERE userId = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        userDto dto = null;

        if (resultSet.next()) {
            String Uid = resultSet.getString(1);
            String name = resultSet.getString(2);
            String type = resultSet.getString(3);
            String pw = resultSet.getString(4);
            String email = resultSet.getString(5);


            dto = new userDto(Uid,name,type,pw,email);
        }

        return dto;
    }
}
