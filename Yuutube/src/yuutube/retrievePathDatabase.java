/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuutube;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author User
 */
public class retrievePathDatabase {
     static String getLink(int videoid){

        String url = "jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String password = "";
        String link="";
        
        String query = "SELECT * FROM video WHERE video_id='"+videoid+"'";

        try (Connection con = DriverManager.getConnection(url, user, password);
                PreparedStatement pst = con.prepareStatement(query);
                ResultSet rs = pst.executeQuery()) {
while (rs.next()) {
           link =  rs.getString(6);
            
            
}

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(retrievePathDatabase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return link;
    }
     
     public static void main(String[] args) {
         
     }
     
}

