package lk.ijse.laboratory.db;

import lk.ijse.laboratory.Dto.patientDto;
import lk.ijse.laboratory.Dto.userDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    public static userDto newDto;
    public static int verify;
    public static patientDto PTDto;
    private static DbConnection dbConnection;
    private Connection connection;

    private DbConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/lab",
                "root",
                "Ijse@1234"
        );
    }

    public static DbConnection getInstance() throws SQLException {
        return (null == dbConnection) ? dbConnection = new DbConnection() : dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
