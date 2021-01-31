
package alifPart;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import static alifPart.ConnectionMySQL.getConnection;

/**
 * 
 * @author alifm
 */
public class Subscription {
    private static int subBoolean;
    
    /**
     * Method displays options for user to subscribe or unsubscribe.
     * Call this method when/after displaying a video.
     * If user subscribes/unsubscribes to a channel, method will do two things:
     * 1. UPDATE INT subscribers in TABLE channel_subs.
     * 2. UPDATE TINYINT subscribed in TABLE user_subs.
     * @param userId current user that is logged in
     * @param chanUserId current channel user is at
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    public static void promptSub(int userId, int chanUserId) throws SQLException, Exception{
        Scanner sc = new Scanner(System.in);

        //Request user input and store in variable userAction
        System.out.println("0. Cancel/Return\n1. Subscribe\n2. Unsubscribe");
        int userAction = sc.nextInt();        

        // Only enter loop if user is logged in
        if(LoggedIn.loggedIn() > 0){
            
            // if user is not subscribed to the channel
            if(checkSubOrNot(userId, chanUserId) == 0) {
                switch (userAction) {
                    case 0 -> {
                        shanHongPart.Homepage.userOperations();
                    }
                    case 1 -> {
                        subBoolean = 1;
                        increaseSub(chanUserId);
                        updateSubBoolean(userId, chanUserId, subBoolean);
                        System.out.println("Subscribed");
                    }
                    case 2 -> {
                        System.out.println("You are not subscribed to this channel!\n");
                        promptSub(userId, chanUserId);
                    }
                    default -> {
                        shanHongPart.Homepage.userOperations();
                    }
                }
            }
            
            // if user is already subscribed to the channel
            else {
                switch (userAction) {
                    case 0 -> {
                        shanHongPart.Homepage.userOperations();
                    }
                    case 1 -> {
                        System.out.println("You are already subscribed to this channel!\n");
                        promptSub(userId, chanUserId);
                    }
                    case 2 -> {
                        subBoolean = 0;
                        decreaseSub(chanUserId);
                        updateSubBoolean(userId, chanUserId, subBoolean);
                        System.out.println("Unsubscribed");
                    }
                    default -> {
                        shanHongPart.Homepage.userOperations();
                    }
                }
            }
            
        }else{
            System.out.println("Please log in");
        }
    }
    
    /**
     * Method displays subscriber counts.
     * You can add more information to display depending on your needs.
     * Once database is finalized, you can add a FIELD channel_name to be displayed.
     * @return numSub
     * @throws SQLException
     * @throws Exception
     */
    public static ArrayList<String> getSub(int channelId) throws SQLException, Exception {
        Connection con = getConnection();
        
        try(
                PreparedStatement statement = 
                        con.prepareStatement("SELECT channel_id, subscribers FROM channel_subs WHERE channel_id = '"+channelId+"'");
                ResultSet result = statement.executeQuery())
        {
            ArrayList<String> numSub = new ArrayList<>();
            
            while(result.next()){
                System.out.print("Channel ID: ");
                System.out.println(result.getString("channel_id"));
                System.out.print("Subscribers: ");
                System.out.print(result.getString("subscribers"));

                numSub.add(result.getString("channel_id"));
                numSub.add(result.getString("subscribers"));
            }
            
            // for debugging purposes
            System.out.println("\nAll records retrieved");
            return numSub;
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    
    /**
     * Method returns a subscribed value of either 0 or 1.
     * If 0, user is not subscribed. 
     * If 1, user is subscribed.
     * Default return value is 0.
     */
    private static int checkSubOrNot(int userId, int chanUserId) throws Exception{
        Connection con = getConnection();   

        try(PreparedStatement statement = con.prepareStatement(
                "SELECT user_id, channel_user_id, subscribed FROM user_subs WHERE user_id = '"+userId+"' AND channel_user_id = '"+chanUserId+"'");
                ResultSet result = statement.executeQuery())
        {
            if(result.next())
                return result.getInt("subscribed");
            
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }        
        return 0;
    }
    
    // Method UPDATE FIELD subscribers + 1 in TABLE channel_subs
    private static void increaseSub(int chanUserId) throws Exception{
        Connection con = getConnection();   

        try(PreparedStatement posted = con.prepareStatement(
                "UPDATE `user` SET `subscribers` = `subscribers` + 1 WHERE user_id = '"+chanUserId+"'")){
            posted.executeUpdate();
            System.out.println("Update user successful");
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }    

    // Method UPDATE FIELD subscribers - 1 in TABLE channel_subs    
    private static void decreaseSub(int chanUserId) throws Exception{
        Connection con = getConnection();   

        // updates subscribed field, insert into table channel_subs
        try(PreparedStatement posted = con.prepareStatement(
                "UPDATE `user` SET `subscribers` = `subscribers` - 1 WHERE user_id = '"+chanUserId+"'");){
            posted.executeUpdate();
            System.out.println("Update user successful");
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    // Method UPDATE field subscribed = 1 in TABLE user_subs
    private static void updateSubBoolean(int userId, int chanUserId, int subBoolean) throws Exception{
        Connection con = getConnection();
        
        try(PreparedStatement posted = con.prepareStatement(
                "UPDATE `user_subs` SET `subscribed` = '"+subBoolean+"' WHERE user_id = '"+userId+"' AND channel_user_id = '"+chanUserId+"'")){
            posted.executeUpdate();
            System.out.println("Update user_subs successful");
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }        
    }
    
   
}