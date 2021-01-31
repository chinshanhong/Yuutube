/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuutube;

import java.io.IOException;
import static yuutube.videoMenu.retrieveId;

/**
 *
 * @author User
 */
public class openVideo {
        static int openVideo(int videoid) throws IOException{
        
        String video = retrievePathDatabase.getLink(videoid);
        String command = "C:\\Program Files\\Windows Media Player\\wmplayer.exe";
      String arg = video;
      //Building a process
      ProcessBuilder builder = new ProcessBuilder(command, arg);
      System.out.println("\n");
      System.out.println("Playing the video on media player. . . .");
      System.out.println("\n");
      //Starting the process
      builder.start();
      return 0;
    }
        
        static int addViewCount(int videoid){
       viewCountAddDatabase.addingViewCount(videoid);
       return 0;
    }
}
