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
public class retrieveDatabase {
    public static int retrieveData(int id){
         String url = "jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String password = "";
        
        String query = "SELECT * FROM video WHERE video_id='"+id+"'";

        try (Connection con = DriverManager.getConnection(url, user, password);
                PreparedStatement pst = con.prepareStatement(query);
                ResultSet rs = pst.executeQuery()) {

           
            while (rs.next()) {
                System.out.println("\n");
                System.out.println("==Selected Video Detail==");
                System.out.print("Video Title         :");
                System.out.println(rs.getString(2));
                System.out.print("Video Like Count    :");
                System.out.println(rs.getString(3));
                System.out.print("Video Dislike Count :");
                System.out.println(rs.getString(4));
                System.out.print("Video View Count    :");
                System.out.println(rs.getString(5));
                System.out.print("Video Category    :");
                System.out.println(rs.getString(7));
                 System.out.print("Video Duration   :");
                  int duration = Integer.parseInt(rs.getString(8));
                String timer = String.format("%02d:%02d", duration / 60, duration % 60);
                System.out.println(timer);
                
               
                
                
               return rs.getInt(9);
                
            }
            
            

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(retrieveDatabase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        return 0;
    }
    
  
    
     public static void main(String[] args) {
        
       
    }
     
     
}
