package alifPart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Patricia's class
 * Checks whether a user is logged in or not, if user is logged in return 1, else return 0
 * Changed the method name LoggedIn() to loggedIn() because of disrupting naming convention error
 * 
 */ 
public class LoggedIn {
    
    public static void main(String[]args) {
        int id=loggedIn();
        System.out.println(id);
    }
    
    
    public static int loggedIn(){

        int id =0;
        try{
            //create connection to database
            String path="jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
            String userid="root";
            String password="";

            Connection con=DriverManager.getConnection(path,userid,password);

            String mysql = "Select user_id from user where loggedin=1"; //update login setid=
            PreparedStatement ps=con.prepareStatement(mysql);
            ResultSet rs=ps.executeQuery();

            while(rs.next()){

               id=rs.getInt(1);

            }
            rs.close();         
            ps.close();
            con.close();



        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(LoggedIn.class.getName());lgr.log(Level.SEVERE, ex.getMessage(), ex);  
        }
                
        return id;
    }
}

