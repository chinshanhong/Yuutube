package patriciaPart;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shanHongPart.Homepage;


public class LoggedIn {
    
public static void main(String[]args) throws IOException{
    
    LoggingOut();
    
}


//method for login to existing account
public static void LoggingIn() throws IOException{
    
    Scanner s=new Scanner(System.in);
        
        try{
              
        //create connection to database
        String path="jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String userid="root";
        String password="";
        
        Connection con=DriverManager.getConnection(path,userid,password);

                System.out.println("-----------------------------------------------"); 
                System.out.println("\t\tLog In Page");
               
                System.out.println("------------------------------------------------");
                
                
                
                System.out.print("Email address: ");
                String user_email=s.next();
                boolean checku=RegistrationForm.check_user_email(user_email);
 
                while(true){
                if(checku==false){
              
                
                        System.out.println("This email address does not have an account with us.Would you like to register a new account?(Y/N)");
                        char yesno=s.next().charAt(0);
                    
                    if (yesno=='Y'||yesno=='y'){
                        RegistrationForm.Register();
                      
                    }
                    else if(yesno=='N'||yesno=='n')    {                 
                        System.out.println("Back To Homepage");
                         Homepage.displayHomepage();
                          Homepage.userOperations();
                        break;
                    }
                    
                    //updated 30/12
                    else {
                        System.out.println("Invalid input. Please enter a valid input : Y / N"); //updated 30/12
                        yesno=s.next().charAt(0);
                        
                        boolean test=true;
                      label : while(true){
   
                       if (yesno=='Y'||yesno=='y'){
                        LoggedIn.LoggingIn();
                        test=false;
                        break label ;
                            }
                        else if(yesno=='N'||yesno=='n')    {                 
                        System.out.println("Back To Homepage");
                         Homepage.displayHomepage();
                          Homepage.userOperations();
                          test=false;
                        break label ;
                    }
                       
                        else
                        System.out.println("Invalid input. Please enter a valid input : Y / N"); //updated 30/12
                        yesno=s.next().charAt(0);
                        break;
                }

                    }
                }
                
                
                else
     
                s.nextLine();  
                System.out.print("Password: ");
                String userpassword=s.nextLine();
                boolean checkpw=RegistrationForm.check_user_password(userpassword);
                
                while(true){
                    
                    checkpw=RegistrationForm.check_user_password(userpassword);

                if (checkpw==false){
                    
                    System.out.println("Incorrect password. Please try again");
                    userpassword=s.nextLine();
                    checkpw=RegistrationForm.check_user_password(userpassword);
 
                }
                
               
                else if (checkpw==true) {
                    
                String mysql="SELECT username FROM user where user_email='"+user_email+"' and userpassword='"+userpassword+"';";
                String column="username";
                PreparedStatement ps=con.prepareStatement(mysql);
                ResultSet rs=ps.executeQuery();
                
                 String getusername="";  
                
                if(rs.next()){
               
                getusername=rs.getString(column);    
                
                }

                System.out.println("Welcome back,"+getusername+"!");
                
                UpdateLogIn(user_email);
                 Homepage.displayHomepage();
      Homepage.userOperations();
                break;
                }

                    
                }
                }

                 } catch (SQLException ex) {Logger lgr = Logger.getLogger(RegistrationForm.class.getName());lgr.log(Level.SEVERE, ex.getMessage(), ex);  }
                

}
    
 //updates the logged in status for the user, status=1 for user that has logged in
  public static void UpdateLogIn(String user_email){

    try{
        //create connection to database
        String path="jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String userid="root";
        String password="";
        
        Connection con=DriverManager.getConnection(path,userid,password);
  
         String mysql1 = "UPDATE user SET loggedin=1 where user_email='"+user_email+"';"; 
         String mysql2="UPDATE user SET loggedin=0 where user_email!='"+user_email+"';"; 
         
         PreparedStatement ps1=con.prepareStatement(mysql1);
         PreparedStatement ps2=con.prepareStatement(mysql2);
         
         ps1.executeUpdate();
         ps2.executeUpdate();

         
        } catch (SQLException ex) {Logger lgr = Logger.getLogger(RegistrationForm.class.getName());lgr.log(Level.SEVERE, ex.getMessage(), ex);  }

  }
  
  
  //method that logs out user from account
  public static void LoggingOut() throws IOException{

      System.out.println("Key in 'Log Out' to log out of your account");
      
      Scanner s=new Scanner(System.in);
     
      String loggingout="Log Out";
      
      String userlogout=s.nextLine();
      
      if (userlogout.equalsIgnoreCase(loggingout)){
          
          System.out.println("Are you sure you want to log out?(Y/N)");
           char yesno=s.next().charAt(0);
                    
                    if (yesno=='Y'||yesno=='y'){
                 
                 System.out.println("Logging Out....");
                 System.out.println("----------------");
                 UpdateLogOut();
                 System.out.println("Logged Out Successfully!");
                    Homepage.displayHomepage();
      Homepage.userOperations();
                    }
                    
                    else {
                        System.out.println("You haven't logged out! Continue enjoying YuuTube!");
                           Homepage.displayHomepage();
      Homepage.userOperations();
                        
                    }
             
                   
                }
          }

         
 //method that updates status of log out in database after user has logged out
 // loggedin=0 means user has logged out
  public static void UpdateLogOut(){

    try{
        //create connection to database
        String path="jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String userid="root";
        String password="";
        
        Connection con=DriverManager.getConnection(path,userid,password);
        
     


         String mysql3 = "UPDATE user SET loggedin=0 where loggedin=1";
                //user_email='"+user_email+"';";

                PreparedStatement ps3=con.prepareStatement(mysql3);
                ps3.executeUpdate();

         
        } catch (SQLException ex) {Logger lgr = Logger.getLogger(RegistrationForm.class.getName());lgr.log(Level.SEVERE, ex.getMessage(), ex);  }
                
  
  } 

  //checks which user is logged in and returns the id of that user
  public static int LoggedIn(){

      int id =0;
    try{
        //create connection to database
        String path="jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String userid="root";
        String password="";
        
        Connection con=DriverManager.getConnection(path,userid,password);
        
         String mysql4 = "Select user_id from user where loggedin=1"; 
         PreparedStatement ps4=con.prepareStatement(mysql4);
         ResultSet rs1=ps4.executeQuery();
         
         while(rs1.next()){
 
            id=rs1.getInt(1);

         }
         
        } catch (SQLException ex) {Logger lgr = Logger.getLogger(RegistrationForm.class.getName());lgr.log(Level.SEVERE, ex.getMessage(), ex);  }
                
    return id;
  }
  
  
}
