
package alifPart;

import java.sql.*;

/**
 * Contains getConnection method to connect program to database.
 * @author alifm
 */
public class ConnectionMySQL {

    public static Connection getConnection() throws SQLException{
        try{
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/yuutube";
            String username = "root";
            String password = "";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            
            // for debugging purposes, can be removed
            System.out.println("Connected to database in ConnectionMySQL.getConnection()");
            
            return conn;
            
        } catch(ClassNotFoundException | SQLException e){
            System.err.println(e);
        }
        
        return null;
    }
}
