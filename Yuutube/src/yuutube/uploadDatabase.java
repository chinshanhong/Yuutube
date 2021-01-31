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
import yuutube.channelUser;
import yuutube.LoggedIn;

/**
 *
 * @author User
 */
public class uploadDatabase {
      public static void uploadDatabase() {

        String url = "jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String password = "";
        String[] video = channelUser.uploadVideo();
        int user_id = LoggedIn.LoggedIn();
        
        String name = video[0];
        String uRL = video[1];
        String category = video[2];
        String video_length = video[3];
        
        String query = " INSERT INTO video ( videoName, videoURL, videoCategory, video_length, user_id)"
        + " VALUES ('"+name+"','"+uRL+"','"+category+"','"+video_length+"','"+user_id+"')";

        try {
            Connection con = DriverManager.getConnection(url, user, password);
        
                PreparedStatement pst = con.prepareStatement(query);
                pst.executeUpdate();

            System.out.println("Successfully Upload!");
            System.out.println("\n");
          
            
            

        } catch (SQLException e) {

             System.err.println("Got an exception!");
      System.err.println(e.getMessage());
        }
    }
}
