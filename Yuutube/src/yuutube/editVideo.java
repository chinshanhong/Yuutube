/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuutube;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static yuutube.videoMenu.retrieveId;

/**
 *
 * @author User
 */
public class editVideo {
    //edit video
    static int editVideo(int videoid){
        System.out.println(videoid);
        Scanner s = new Scanner(System.in);
         System.out.print("Enter Video Name : ");
        String videoName = s.nextLine();
        
        System.out.println("Example of video location: C:\\\\Users\\\\User\\\\Desktop\\\\videofile\\\\suiseipeek.mp4");
        System.out.print("Enter Video Location :");
       
         String videoPath = s.nextLine();
        
         System.out.print("Enter Video Category:");
         String videoCategory = s.nextLine();
         
         System.out.print("Video Length (in second):");
         String videoLength = s.nextLine();
         
         
         
        String url = "jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String password = "";
        String link="";
        String query = "UPDATE video SET videoName='"+videoName+"', videoURL='"+videoPath+"', videoCategory='"+videoCategory+"', video_length='"+videoLength+"' WHERE video_id='"+videoid+"'";

         try (Connection con = DriverManager.getConnection(url, user, password);
               
                PreparedStatement pst = con.prepareStatement(query);
               
              ) {
             pst.executeUpdate();


        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(retrievePathDatabase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
         
        return 0;
        
      
    }
}
