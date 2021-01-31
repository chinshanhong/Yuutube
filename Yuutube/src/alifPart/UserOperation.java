
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
import shanHongPart.Homepage;

/**
 * All methods in this class requires parameter userId, which has to be passed externally
 * @author alifm
 */
public class UserOperation {
    
    /**
     * Method displays these user operations:
     * 1. Display user info
     * 2. Prompt to change password
     * 3. Prompt to change display name
     * 
     * @param userId 
     */
    public static void getUserOperations(int userId) throws SQLException, Exception {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("0. Cancel/Return\n1. Display user info\n2. Change display name\n3. Change password");
        int userInput = sc.nextInt();
        
        switch (userInput) {
            case 1 -> displayUserInfo(userId);
            case 2 -> promptChangeDisplayName(userId);
            case 3 -> promptChangePass(userId);
            default -> {
                Homepage.displayHomepage();
                Homepage.userOperations();
            }
        }
    }
    
    
    public static ArrayList<String> displayUserInfo(int userId) throws SQLException, Exception{
        Connection con = getConnection();

        try(PreparedStatement statement = 
                con.prepareStatement("SELECT user_email, username, displayName FROM user WHERE user_id = '"+userId+"'"); 
                ResultSet result = statement.executeQuery())
        {
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                
                System.out.print("Name: ");
                System.out.println(result.getString("displayName"));
                System.out.print("Username: ");
                System.out.println(result.getString("username"));
                System.out.print("Email: ");
                System.out.println(result.getString("user_email"));

                array.add(result.getString("displayName"));
                array.add(result.getString("username"));
                array.add(result.getString("user_email"));
            }
            
            System.out.println("\nAll records retrieved");
            getUserOperations(userId);
            return array;
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        getUserOperations(userId);
        return null;        
    }
    
    
    /**
     * Method displays prompts to guide users to change password
     * @param userId 
     */ 
    private static void promptChangePass(int userId) throws Exception{
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter old password: ");
        String currPassword = sc.nextLine();
        int verified = checkCurrPassword(userId, currPassword);
        
        // Checks if old password matches the database
        if(verified == 1){
            System.out.print("Enter new password: ");
            String newPassword = sc.nextLine();
            
            System.out.print("Confirm new password: ");
            String confirmNewPassword = sc.nextLine();
            
            while(!confirmNewPassword.equals(newPassword)){
                System.out.println("Passwords entered not the same!");
                System.out.print("Enter new password: ");
                newPassword = sc.nextLine();

                System.out.print("Confirm new password: ");
                confirmNewPassword = sc.nextLine();
            }
            
            // Once user enters the same password for both, new password is updated to database
            updatePassword(userId, newPassword);
            getUserOperations(userId);
        }else{
            System.out.println("Password does not match database. Exiting...");
            getUserOperations(userId);
        }
    }
    
    
    /**
     * Method to prompt user if they want to change their display_name.
     * Requires parameter user_id in order to change the correct user's display_name.
     * @param userId 
     */ 
    private static void promptChangeDisplayName(int userId) throws SQLException, Exception{
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter display name: ");
        String newDisplayName = sc.nextLine();
        
        System.out.println("");
        System.out.printf("Your new display name is \"%s\"\n", newDisplayName);
        System.out.println("0. Cancel/Return\n1. Confirm");
        
        int i = sc.nextInt();
        
        if(i == 1) {
            requestCurrPassword(userId);
            updateNewDisplayName(userId, newDisplayName);
            getUserOperations(userId);
        }else{
            getUserOperations(userId);
        }
    }
    
    
    /**
     * Method requests user's current password and passes currPassword into method checkCurrPass for verification.
     * You may use this method any time you require a password check.
     * @param userId 
     */
    public static void requestCurrPassword(int userId) throws Exception{
        Scanner sc = new Scanner(System.in);
        
        // Prompts user to enter password to confirm operation
        System.out.print("Enter your password: ");
        sc.next();
        String currPassword = sc.nextLine();
        System.out.println("");
        checkCurrPassword(userId, currPassword);
    }
    

    /**
     * Method verifies currPassword in FIELD userpassword in TABLE userinfo
     * @param currPassword
     * @param userId 
     */
    private static int checkCurrPassword(int userId, String currPassword) throws SQLException, Exception{
        Connection con = getConnection();
        
        try(PreparedStatement statement = 
                con.prepareStatement("SELECT * FROM user WHERE user_id = '"+userId+"' AND userpassword='"+currPassword+"'");
                ResultSet result = statement.executeQuery())
        {
            
            String userpassword = "";
            while(result.next()){
                userpassword = result.getString("userpassword");
            }
            
            if(currPassword.equals(userpassword)){
                System.out.println("Verified\n");
                return 1;         
            }
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
        return 0;
    }
    
    
    /**
     * Method UPDATE new display name into FIELD display_name in TABLE userinfo
     * @param newDisplayName 
     * @param userId 
     */ 
    private static void updateNewDisplayName(int userId, String newDisplayName) throws Exception {
        Connection con = getConnection();
        
        try(PreparedStatement posted = con.prepareStatement(
                    "UPDATE `user` SET `displayName` = '"+newDisplayName+"' WHERE `user_id` = '"+userId+"'")){
            posted.executeUpdate();
            System.out.println("Update display_name in userinfo successful");
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    
    /**
     * Method UPDATE FIELD userpassword in TABLE userinfo
     * @param newPassword 
     * @param userId 
     */ 
    private static void updatePassword(int userId, String newPassword) throws SQLException, Exception{      
        Connection con = getConnection();
        
        try(PreparedStatement posted = con.prepareStatement(
                "UPDATE `user` SET `userpassword` = '"+newPassword+"' WHERE 'user_id' = '"+userId+"'")){
            posted.executeUpdate();
            System.out.println("Update userpassword in userinfo successful");
        }catch(SQLException e){
            Logger lgr = Logger.getLogger(LikeDislike.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }         
    }
    
    
}