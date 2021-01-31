package yuutube;

import java.io.IOException;
import java.lang.Object.*;
import java.util.Scanner;
import shanHongPart.Homepage;
import yuutube.uploadDatabase;
import yuutube.displayVideoBaseChannel;
import yuutube.videoMenu;
import yuutube.channelName;
import yuutube.editVideo;
import yuutube.deleteVideo;
import yuutube.channelSubscriber;
/**
 *
 * @author User
 */
public class channelYuutuber {
    

    
    static void display(int videoid) throws IOException{
         String watch="";
        do{
        Scanner s = new Scanner(System.in);
         String[]userVideo = displayVideoBaseChannel.getVideoName(videoid);
      String[]userVideoID = displayVideoBaseChannel.getVideoID(videoid);
      openVideo ov = new openVideo();
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
         System.out.println("Choose the number u wanna watch? (R to return back to channel menu)");
         watch = s.next();
         if(!watch.equals("R")){
             int id = Integer.parseInt(watch);
             ov.openVideo(id);
             ov.addViewCount(id);
         }
         
    }while(!watch.equals("R"));
        }

    

    
    
     static void yuutuberChannel(int videoid)throws IOException{
         int wrongInput;
         int operation;
         
         int user_id_for_yt = channelName.retrieveUserID(videoid);
         String name = channelName.getChannelName(user_id_for_yt);
         
      
      
      do{
      System.out.println("\n");
      int channelSubCount = channelSubscriber.getChannelSubscriber(user_id_for_yt);
      System.out.println("Channel Name:"+name);
      System.out.println("Channel Sub count:"+channelSubCount);
      System.out.println(" 1. Display Video Uploaded");
      System.out.println(" 2. Back to Homepage");
      System.out.print(" Choose Operation (1-2):");
      Scanner s = new Scanner(System.in);
       operation = s.nextInt();
      switch(operation) {

       
   case 1 :
     display(user_id_for_yt);
     break;
    
   case 2:
        Homepage.displayHomepage();
      Homepage.userOperations();
      
   default : 
       System.out.println("Wrong input! Try again in between (1-2):");
        operation = s.nextInt();
}
     
      }while(operation!=2);
   }
     }
   


