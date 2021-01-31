package alifPart;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static alifPart.ConnectionMySQL.getConnection;

/**
 *
 * @author alifm
 */
public class Comment{
    
    /**
     * Method displays options for user to leave a comment on videos.Call this method when/after displaying a video.
     * If a user leaves a comment, this method will INSERT user's comment into FIELD comment in TABLE video_details.
     * 
     * @param id temporary parameter
     * @param videoId 
     * @throws java.lang.Exception
     */
    public static void promptComment(int id, int videoId) throws Exception{
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Add a comment...");
        String comment = sc.nextLine();

        if(LoggedIn.loggedIn() > 0){
            // Asks the user to confirm their comment
            System.out.println("\nConfirm comment: \"" + comment + "\"?");
            System.out.println("0. Cancel/Return\n1. Confirm");
            int i = sc.nextInt();
            
            if(i == 1){
                updateComment(id, videoId, comment);
            }else{
                System.out.println("Please log in");
                return;
            }
        }
    }
    
    
    /**
     * Method prints all comments under a video
     * by retrieving from FIELD comment in TABLE video_details.
     * NOTE: Method has to be tinkered again once database is properly set up to 
     * display both username and comment.
     * @param videoId id of video you want to display comments of
     */    
    public static ArrayList<String> getComment(int videoId) throws SQLException, Exception {
        Connection con = getConnection();
        
        try(PreparedStatement statement = 
                con.prepareStatement("SELECT video_id, comment FROM video_details WHERE video_id = '"+videoId+"'"); 
                ResultSet result = statement.executeQuery())
        {
            
            ArrayList<String> array = new ArrayList<>();
            
            System.out.print("Video ID: ");
            System.out.println(videoId);
            
            while(result.next()){
//                System.out.println(result.getString("video_id"));
                System.out.println("Comments: ");
                System.out.println(result.getString("comment"));
                array.add(result.getString("comment"));
            }
            System.out.println("No comments posted.");
            return array;
            
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
    
    
    private static void updateComment(int id, int videoId, String comment) throws Exception{
        Connection con = getConnection();
        
        // insert comments into field comment in table video_details
        try(PreparedStatement posted = con.prepareStatement(
                "INSERT INTO video_details(user_id, video_id, comment) VALUES('"+id+"', '"+videoId+"', '"+comment+"') "
                        + "ON DUPLICATE KEY UPDATE video_id='"+videoId+"', comment='"+comment+"'"))
        {
            posted.executeUpdate();
            System.out.println("Insert video_details succesful");
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

        // insert comments into field comment in table user_history
        try(PreparedStatement posted = con.prepareStatement(
                "INSERT INTO user_history(user_id, video_id, comment) VALUES('"+id+"', '"+videoId+"', '"+comment+"') "
                        + "ON DUPLICATE KEY UPDATE video_id='"+videoId+"', comment='"+comment+"'"))
        {
            posted.executeUpdate();
            System.out.println("Insert user_history succesful");
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        
        
        System.out.println("Comment posted");
    }
    
    
}


