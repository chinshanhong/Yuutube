/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuutube;

import java.io.IOException;
import java.lang.Object.*;
import java.util.Scanner;
import shanHongPart.Homepage;
import yuutube.uploadDatabase;
import yuutube.displayVideoBaseChannel;
import yuutube.videoMenu;
import yuutube.editVideo;
import yuutube.deleteVideo;
import yuutube.LoggedIn;
import yuutube.displayVideoBaseUser;
/**
 *
 * @author User
 */
public class channelUser {
    
    static String[] uploadVideo(){
        System.out.println("\n");
        Scanner s = new Scanner(System.in);
        System.out.println("Upload Video");
        System.out.print("Enter Video Name : ");
        String videoName = s.nextLine();
        
        System.out.println("Example of video location: C:\\\\Users\\\\User\\\\Desktop\\\\videofile\\\\suiseipeek.mp4");
        System.out.print("Enter Video Location :");
       
         String videoPath = s.nextLine();
        
         System.out.print("Enter Video Category:");
         String videoCategory = s.nextLine();
         
         System.out.print("Video Length (in second):");
         String videoLength = s.nextLine();
         System.out.println("\n");
         
         
         
           
           String[]video = {videoName,videoPath,videoCategory,videoLength};
       return video;
    }
    
    static void display() throws IOException{
         String watch="";
        do{
        Scanner s = new Scanner(System.in);
         String[]userVideo = displayVideoBaseUser.getVideoName();
      String[]userVideoID = displayVideoBaseUser.getVideoID();
      openVideo ov = new openVideo();
      int i=0;
      System.out.println("\n");
      System.out.println("==Video List==");
            System.out.println("Video ID         Video Name");
            if(userVideo[0]==null){
            System.out.println("This channel has no video.");
            }
         while(userVideo[i]!=null){
             
            System.out.println(userVideoID[i]+".\t\t "+userVideo[i]);
            i++;
        }
         System.out.print("Choose the number u wanna watch? (R to return back to channel menu)");
         watch = s.next();
         if(!watch.equals("R")){
             int id = Integer.parseInt(watch);
             ov.openVideo(id);
             ov.addViewCount(id);
         }
         
    }while(!watch.equals("R"));
        }
    
    static void edit() throws IOException{
         String watch="";
        do{
        Scanner s = new Scanner(System.in);
         String[]userVideo = displayVideoBaseUser.getVideoName();
      String[]userVideoID = displayVideoBaseUser.getVideoID();
      editVideo ev = new editVideo();
      int i=0;
            System.out.println("\n");
      System.out.println("==Video List==");
            System.out.println("Video ID         Video Name");
             if(userVideo[0]==null){
            System.out.println("This channel has no video.");
            }
         while(userVideo[i]!=null){
            
            System.out.println(userVideoID[i]+".\t\t "+userVideo[i]);
            i++;
        }
         System.out.print("Choose the number u to edit? (R to return back to channel menu)");
         watch = s.next();
            System.out.println(watch);
         if(!watch.equals("R")){
             int videoid = Integer.parseInt(watch);
             ev.editVideo(videoid);
         }
         
    }while(!watch.equals("R"));
        }
    

         
         static void userChannel() throws IOException{
               int wrongInput;
         int operation;
         int user_id = LoggedIn.LoggedIn();
        String name = channelName.getChannelName(user_id);
        int channelSubCount = channelSubscriber.getChannelSubscriber(user_id);
      do{
      System.out.println(" Your Channel Name:"+ name);
      System.out.println(" Channel Sub Count:"+ channelSubCount);
      System.out.println(" 1. Upload Video");
      System.out.println(" 2. Display Video Uploaded");
      System.out.println(" 3. Edit Video Uploaded");
      System.out.println(" 4. Delete Video Uploaded");
      System.out.println(" 5. Back to Homepage");
      System.out.print(" Choose Operation (1-5):");
      Scanner s = new Scanner(System.in);
       operation = s.nextInt();
      switch(operation) {
   case 1 :
       uploadDatabase obj = new uploadDatabase();
       
        obj.uploadDatabase();
      break;
       
   case 2 :
     display();
     break;
             
   case 3 :
       String watch="";
        do{
       String[]userVideo = displayVideoBaseUser.getVideoName();
      String[]userVideoID = displayVideoBaseUser.getVideoID();
       int i=0;
       System.out.println("\n");
      System.out.println("==Video List==");
            System.out.println("Video ID         Video Name");
             if(userVideo[0]==null){
            System.out.println("This channel has no video.");
            }
         while(userVideo[i]!=null){
            System.out.println(userVideoID[i]+".\t\t "+userVideo[i]);
            i++;
        }
         System.out.print("Choose the number u want to edit? (R to return back to channel menu):");
          watch = s.next();
           if(!watch.equals("R")){
             int id = Integer.parseInt(watch);
            editVideo ev = new editVideo();
            
            ev.editVideo(id);
               System.out.println("Successfully edited.");
         }
           else{
               System.out.println("Going back to the channel");
           }
            
   
   }while(!watch.equals("R"));
   break;
   
    case 4 :
        do{
       String[]userVideo = displayVideoBaseUser.getVideoName();
      String[]userVideoID = displayVideoBaseUser.getVideoID();
       int i=0;
       System.out.println("\n");
      System.out.println("==Video List==");
            System.out.println("Video ID         Video Name");
        if(userVideo[0]==null){
            System.out.println("This channel has no video.");
            }
         while(userVideo[i]!=null){
            System.out.println(userVideoID[i]+".\t\t"+userVideo[i]);
            i++;
        }
         System.out.print("Choose the number u want to delete? (R to return back to channel menu):");
          watch = s.next();
           if(!watch.equals("R")){
             int id = Integer.parseInt(watch);
            deleteVideo ev = new deleteVideo();
            ev.deleteVideo(id);
               System.out.println("Successfully delete.");
         }
           else{
               System.out.println("Going back to the channel");
           }
            
   
   }while(!watch.equals("R"));
   break;
   
    case 5:
        Homepage.displayHomepage();
      Homepage.userOperations();
      
   default : 
       System.out.println("Wrong input! Try again in between (1-5):");
        operation = s.nextInt();
}
     
      }while(operation!=5);
   }
         }
       
    
    
     
   

