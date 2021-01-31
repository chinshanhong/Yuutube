
package alifPart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static alifPart.ConnectionMySQL.getConnection;
import yuutube.videoMenu;

/*
 *
 * @author alifm
 */
public class LikeDislike{

    /*
     * class variable
     */
    private static int liked = 0;
    private static int disliked = 0;
    private static String wrongInputMsg = "WRONG INPUT";
    
    
    /**
     * Method displays options for user to exit, like or dislike.
     * Call this method when/after displaying a video.
     * If user like/dislike a video, method will do two things:
     * 1. UPDATE INT likes / dislikes and INT video_id in TABLE video_details.
     * 2. UPDATE TINYINT liked/disliked in TABLE user_history.
     * @param userId current user that is logged in
     * @param videoId determine which video is liked/disliked
     * @throws java.lang.Exception
     */
    public static void promptLikeDislike(int userId, int videoId) throws Exception{
        Scanner sc = new Scanner(System.in);
      
        // Request user input and store in variable userAction
        System.out.println("0. Cancel/Return\n1. Like\n2. Dislike");
        int userAction = sc.nextInt();
        
        /**
         * updates Boolean values of variables liked and disliked based on userAction
         * if 0, everything remains, if 1, video is liked, if 2 video is disliked
         */
        switch (userAction) {
            case 0:
                videoMenu.mainMenu(videoId);
                break;
            
            // like
            case 1:                
                // checks if user is logged in
                if(LoggedIn.loggedIn() !=0){
                    
                    // checks if user has never liked nor disliked the video
                    // if the user has not liked nor disliked
                    if(checkLiked(userId, videoId) == 0 && checkDisliked(userId, videoId) == 0) {
                        updateLikedDislikedBoolean(userId, videoId, 1, 0);
                        increaseNumLikes(videoId);
                    
                    // if the user has already disliked the video
                    }else if(checkLiked(userId, videoId) == 0 && checkDisliked(userId, videoId) == 1) {
                        updateLikedDislikedBoolean(userId, videoId, 1, 0);
                        decreaseNumDislikes(videoId);
                        increaseNumLikes(videoId);
                    
                    // if the user has already liked the video, ask user if they want to unlike
                    }else if(checkLiked(userId, videoId) == 1 && checkDisliked(userId, videoId) == 0){
                        System.out.println("You've already liked this video, do you wish to unlike?");
                        System.out.println("1. No\n2. Yes");
                        int a = sc.nextInt();
                        if(a==2) {
                            updateLikedDislikedBoolean(userId, videoId, 0, 0);
                            decreaseNumLikes(videoId);
                        }
                    }else{
                        break;
                    }
                    videoMenu.mainMenu(videoId);
                    
                }else {
                    System.out.println("Please log in");
                    videoMenu.mainMenu(videoId);
                }
                
                break;
                
            // dislike    
            case 2:

                // checks if user is logged in
                if(LoggedIn.loggedIn() !=0){                
                    
                    // checks if user has never liked nor disliked the video
                    // if the user has not liked nor disliked
                    if(checkLiked(userId, videoId) == 0 && checkDisliked(userId, videoId) == 0) {
                        updateLikedDislikedBoolean(userId, videoId, 0, 1);
                        increaseNumDislikes(videoId);
                    
                    // if the user has already disliked the video, ask user if they want to undislike
                    }else if(checkLiked(userId, videoId) == 0 && checkDisliked(userId, videoId) == 1) {
                        System.out.println("You've already disliked this video, do you wish to undislike?");
                        System.out.println("1. No\n2. Yes");
                        int a = sc.nextInt();
                        if(a==2) {
                            updateLikedDislikedBoolean(userId, videoId, 0, 0);
                            decreaseNumDislikes(videoId);
                        }
                    
                    // if the user has already liked the video
                    }else if(checkLiked(userId, videoId) == 1 && checkDisliked(userId, videoId) == 0){
                        decreaseNumLikes(videoId);
                        updateLikedDislikedBoolean(userId, videoId, 0, 1);
                        increaseNumDislikes(videoId);
                        
                    }else{
                        break;
                    }
                }else{
                    System.out.println("Please log in");
                    videoMenu.mainMenu(videoId);                    
                }
                    break;
                    
            // cancel/return
            default:
                videoMenu.mainMenu(videoId);
                break;
        }
//        updateLikedDislikedBoolean(userId, videoId, liked, disliked);
    }

    /**
     * Method checks if user has already liked the video or not
     * @param userId
     * @param videoId
     */
    private static int checkLiked(int userId, int videoId) throws SQLException {
        Connection con = getConnection();
        
        try(PreparedStatement statement = 
                con.prepareStatement("SELECT video_id, liked FROM user_history WHERE user_id = '"+userId+"' AND video_id = '"+videoId+"'"); 
                ResultSet result = statement.executeQuery())
        {
            while(result.next()){
                liked = result.getInt("liked");
            }
            
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        
        return liked;
    }

    /**
     * Method checks if user has already disliked the video or not
     * @param userId
     * @param videoId 
     */
    private static int checkDisliked(int userId, int videoId) throws SQLException {
        Connection con = getConnection();
        
        try(PreparedStatement statement = 
                con.prepareStatement("SELECT video_id, disliked FROM user_history WHERE user_id = '"+userId+"' AND video_id = '"+videoId+"'"); 
                ResultSet result = statement.executeQuery())
        {
            while(result.next()){
                disliked = result.getInt("disliked");
            }

        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        
        return disliked;       
    }

    /**
     * Method UPDATE FIELD likes, insert into TABLE video
     */
    private static void increaseNumLikes(int videoId) throws Exception{
        Connection con = getConnection();
        
        try(PreparedStatement posted = con.prepareStatement(
                "UPDATE `video` SET `videoLikeCount` = `videoLikeCount` + 1 WHERE `video_id` = '"+videoId+"'")){
            posted.executeUpdate();
            System.out.println("Update video successful");
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        videoMenu.mainMenu(videoId);
    }
    
    /**
     * Method UPDATE FIELD likes, insert into TABLE video
     */    
    private static void decreaseNumLikes(int videoId) throws Exception{
        Connection con = getConnection();
        
        try(PreparedStatement posted = con.prepareStatement(
                "UPDATE `video` SET `videoLikeCount` = `videoLikeCount` - 1 WHERE `video_id` = '"+videoId+"'")){
            posted.executeUpdate();
            System.out.println("Update video successful");
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        videoMenu.mainMenu(videoId);
    }

    /**
     * Method UPDATE FIELD dislikes, insert into TABLE video
     */    
    private static void increaseNumDislikes(int videoId) throws Exception{
        Connection con = getConnection();
        
        try(PreparedStatement posted = con.prepareStatement(
                "UPDATE `video` SET `videoDislikeCount` = `videoDislikeCount` + 1 WHERE `video_id` = '"+videoId+"'")){
            posted.executeUpdate();
            System.out.println("Update video successful");
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        videoMenu.mainMenu(videoId);
    }
    
    /**
     * Method UPDATE FIELD dislikes, insert into TABLE video
     */    
    private static void decreaseNumDislikes(int videoId) throws Exception{
        Connection con = getConnection();
        
        try(PreparedStatement posted = con.prepareStatement(
                "UPDATE `video` SET `videoDislikeCount` = `videoDislikeCount` - 1 WHERE `video_id` = '"+videoId+"'")){
            posted.executeUpdate();
            System.out.println("Update video successful");
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        videoMenu.mainMenu(videoId);
    }
    
    
    
    
    /**
     * UPDATE user's TINYINT/BOOLEAN fields liked/disliked in TABLE user_history
     * @param userId
     * @param videoId
     * @param likedBoolean 0 or 1
     * @param dislikedBoolean 0 or 1
     * @throws java.sql.SQLException
     */
    public static void updateLikedDislikedBoolean(int userId, int videoId, int likedBoolean, int dislikedBoolean) throws SQLException{
        Connection con = getConnection();
        
        try(PreparedStatement posted = con.prepareStatement(
                "UPDATE user_history SET liked = '"+likedBoolean+"', disliked = '"+dislikedBoolean+"' WHERE user_id = '"+userId+"' AND video_id = '"+videoId+"'")){
            posted.executeUpdate();
            System.out.println("Update user_history successful");
            
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}