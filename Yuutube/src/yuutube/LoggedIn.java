package yuutube;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

//checks whether a user is logged in or not, if user is logged in return 1,else return 0
public class LoggedIn {
    
      
  public static int LoggedIn(){

      int id =0;
    try{
        //create connection to database
        String path="jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String userid="root";
        String password="";
        
        Connection con=DriverManager.getConnection(path,userid,password);
        
         String mysql = "Select user_id from user where loggedin=1";
         PreparedStatement ps=con.prepareStatement(mysql);
         ResultSet rs=ps.executeQuery();
         
         while(rs.next()){
 
            id=rs.getInt(1);

         }
         
        

        } catch (SQLException ex) {Logger lgr = Logger.getLogger(LoggedIn.class.getName());lgr.log(Level.SEVERE, ex.getMessage(), ex);  }
                
    return id;
  }
}
