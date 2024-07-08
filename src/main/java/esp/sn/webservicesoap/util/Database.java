package esp.sn.webservicesoap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static Connection connection() throws  SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/soap", "root", "");
    }
}
