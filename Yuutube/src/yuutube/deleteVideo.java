/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuutube;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
//import static javaapplication29.JavaApplication29.retrieveId;

/**
 *
 * @author User
 */
public class deleteVideo {
    static void deleteVideo(int videoid){
//        int videoid = retrieveId(id);
           
        
        
         
        String url = "jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String password = "";
        String link="";
        String query = "DELETE FROM video WHERE video_id='"+videoid+"'";

         try (Connection con = DriverManager.getConnection(url, user, password);
               
                PreparedStatement pst = con.prepareStatement(query);
               
              ) {
             pst.executeUpdate();


        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(retrievePathDatabase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
         
        
        
      
    }
}
