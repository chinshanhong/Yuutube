/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuutube;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import yuutube.LoggedIn;
/**
 *
 * @author User
 */
public class displayVideoBaseChannel {
    static String[] getVideoID(int userId){
        
        String url = "jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String password = "";
        String video="";
        String[]userVideo = new String[100];
        int count=0;
        String query = "SELECT * FROM video WHERE user_id='"+userId+"'";
        
        try (Connection con = DriverManager.getConnection(url, user, password);
                PreparedStatement pst = con.prepareStatement(query);
                ResultSet rs = pst.executeQuery()) {
while (rs.next()) {
           video =  rs.getString(1);  
           userVideo[count] = video;
           count++;
          
}
            
            

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(retrievePathDatabase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return userVideo;
    }
         static String[] getVideoName(int userId){

        String url = "jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String password = "";
        String video="";
        String[]userVideo = new String[100];
        int count=0;
         String query = "SELECT * FROM video WHERE user_id='"+userId+"'";
        
        try (Connection con = DriverManager.getConnection(url, user, password);
                PreparedStatement pst = con.prepareStatement(query);
                ResultSet rs = pst.executeQuery()) {
while (rs.next()) {
           video =  rs.getString(2);  
           userVideo[count] = video;
           count++;
          
}
            
            

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(retrievePathDatabase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return userVideo;
    }
     
     public static void main(String[] args) {
         
     }
}
