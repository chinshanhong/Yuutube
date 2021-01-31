/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuutube;

import java.io.IOException;
import java.util.Scanner;
import patriciaPart.RegistrationForm;
import yuutube.retrieveDatabase;
import yuutube.retrievePathDatabase;
import yuutube.viewCountAddDatabase;
import yuutube.channelUser;
import yuutube.channelYuutuber;
import yuutube.openVideo;
import shanHongPart.Homepage;
import shanHongPart.Playlist;
import alifPart.LikeDislike;
import alifPart.Comment;
import alifPart.Subscription;
import alifPart.UserOperation;
/**
 *
 * @author User
 */
public class videoMenu {
    
    static int retrieveId(int videoID){
        return videoID;
        
    }
    
    public static void mainMenu(int videoid)throws IOException{
           
      
      int operation;
      int count=0;
      
      channelUser cu = new channelUser();
      channelYuutuber cy = new channelYuutuber();
      int user_id= LoggedIn.LoggedIn();
      openVideo ov = new openVideo();
      if(user_id!=0){
     
      do{
          try{
      int userid = retrieveDatabase.retrieveData(videoid);
      System.out.println(" ==Function for the video==");
      System.out.println(" 1. Watch the Video");
      System.out.println(" 2. Like or Dislike this video");
      System.out.println(" 3. Comment this video");
      System.out.println(" 4. Display comment");
      System.out.println(" 5. Add this video to your playlist");
      System.out.println(" 6. Subscribe to this channel");
      System.out.println(" 7. Go to your own channel");
      System.out.println(" 8. Go to your profile detail");
      System.out.println(" 9. Channel of the content creator");
      System.out.println(" 10. Back to Homepage");
      System.out.print(" Choose Operation (1-10):");
      Scanner s = new Scanner(System.in);
       operation = s.nextInt();
       
        if(operation>0&&operation<11){
      switch(operation) {
   case 1 :
      char rewatch;
      do{
      ov.openVideo(videoid);
      ov.addViewCount(videoid);
          System.out.print("Rewatch this video?(Y to rewatch and other to return back menu):");
          rewatch= s.next().charAt(0);
      }while(rewatch=='Y');
      break;
       
   case 2 :
       System.out.println("\n");
      LikeDislike.promptLikeDislike(user_id, videoid);
      break;
     
  
   case 3 :
       System.out.println("\n");
       Comment.promptComment(user_id, videoid);
      break;
    case 4 :
       Comment.getComment(videoid);
      break;
   case 5 :
       Playlist.addVideo(videoid);
      break;
   case 6 :
       Subscription.promptSub(user_id, userid);
      break;
   case 7 :
       System.out.println("\n");
      channelUser.userChannel();
      break;
  case 8 :
      UserOperation.getUserOperations(user_id);
       System.out.println("\n");
      
   case 9 :
       System.out.println("\n");
       if(userid==user_id){
       channelUser.userChannel();
       }
       else{
      cy.yuutuberChannel(videoid);
       }
      
   case 10 :
       System.out.println("\n");
      Homepage.displayHomepage();
      Homepage.userOperations();
   break;
      
}
        
       }
       else
       {
           System.out.println("Wrong input! Try again in between (1-10)!");
           continue;
          }
     count++;
          }catch(Exception e){
           System.out.println("You enter non numeric number! Please enter correctly!");
       }
      }while(count!=100);
      
   }
      else{
          do{
               try{
      int userid = retrieveDatabase.retrieveData(videoid);
      System.out.println("\n");
      System.out.println(" Function for the video:");
      System.out.println("Reminder : To like,dislike,comment and also preview your own channel, you have to login or register first.");
      System.out.println(" 1. Watch the Video");
      System.out.println(" 2. Login or Register");
      System.out.println(" 3. Channel of the content creator");
      System.out.println(" 4. Back to main menu");
      System.out.print(" Choose Operation (1-4):");
      Scanner s = new Scanner(System.in);
       operation = s.nextInt();
       if(operation>0&&operation<6){
      switch(operation) {
   case 1 :
      char rewatch;
      do{
      ov.openVideo(videoid);
      ov.addViewCount(videoid);
          System.out.print("Rewatch this video?(Y to rewatch and other to return back menu):");
          rewatch= s.next().charAt(0);
          System.out.println("\n");
      }while(rewatch=='Y');
      break;
       
   case 2 :
      RegistrationForm.LogIn();
      break;
     
   
       case 3 :
      if(userid==user_id){
           channelUser.userChannel();
       }
       else{
      cy.yuutuberChannel(videoid);
       }
      break;
   case 4 :
      Homepage.displayHomepage();
      Homepage.userOperations();
      break;

      
}
       }
       else
       {
           System.out.println("Wrong input! Try again in between (1-5)!");
           continue;
          }
       }catch(Exception e){
           System.out.println("You enter non numeric number! Please enter correctly!");
       }
     count++;
      }while(count!=100);
   
      }
        
    }
    

     
    
     
     
 
	

     
   
     
//     static void recommendation(){
//         System.out.print("Thanks for watching Suisei Cute Moment! Proceed to the next suggested video? (Y/N):");
//         Scanner s = new Scanner(System.in);
//         char recommendNextVideo = s.next().charAt(0);
//         if(recommendNextVideo=='Y'){
//             //randomgeneratevideo for him to watch
//             System.out.println("Go to another video");
//         }
//        
//     }
     
   
     
       
      
//      static void backToMenu(){
//          System.out.println("Go to Menu(Shan Hong part)");
//    }
      

 
 

}
