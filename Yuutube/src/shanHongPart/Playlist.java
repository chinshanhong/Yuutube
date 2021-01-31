package shanHongPart;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 15-P248TX
 */
import java.io.IOException;
import patriciaPart.LoggedIn;
import java.util.Formatter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import yuutube.channelName;
import yuutube.videoMenu;
 public class Playlist {
    
    public static final Scanner scan = new Scanner(System.in);
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/yuutube?"
            + "zeroDateTimeBehavior=CONVERT_TO_NULL";
    public static final String USER = "root";
    public static final String PASS = "";
    public static Connection conn = null;
    public static PreparedStatement stmt = null;
    public static Statement statement = null;
    
    
   
    /*call the displayPlaylist method to display the user's available playlist, 
    */
    public static void displayPlaylist() throws IOException{
        int user_id = LoggedIn.LoggedIn();
        boolean result = checkPlaylist(user_id);
        
        if(result == true){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                String sql = "SELECT playlist_name FROM playlistuser "
                    + "WHERE user_id=?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, user_id);
                ResultSet rs = stmt.executeQuery();
                System.out.println("");
            
                //start to display user's available playlist
                System.out.println("Hello user, you have the following playlist");
                int count = 1;
                while(rs.next()){
                    String playlistName = rs.getString("playlist_name");
                    System.out.println(count + "." + " " + playlistName);
                    count += 1;
                
                }
                    rs.close();
                
                //Exception to handle error occured when retrieving data 
            }catch(SQLException se){
                se.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try{
                    if(stmt!=null){
                    conn.close();
                    }
                }catch(SQLException se){
                }
                try{
                    if(conn!=null){
                        conn.close();
                    }
                }catch(SQLException se){
                    se.printStackTrace();
                }
            } 
        
        }
        //If user do not have playlist ask the user want to create playlist or not
        else{
            System.out.print("\nIt seems that you don't have any playlist yet,"
                    + " do you want to create a playlist? (Y/N): ");
            while(true){
                String user_input = scan.nextLine();
                //If yes, call createPlaylist() method to help user create playlist
                if(user_input.equals("Y")){
                    createPlaylist();
                    Homepage.userOperations();
                    break;
                }
                //If no, go back to homepage operation
                else if(user_input.equals("N")){
                    Homepage.userOperations();
                    break;
                }
                else{
                    System.out.println("Please enter correct input");
                }
            }
        }
    }
  
    //Method to let the user to select the playlist they want to watch
    public static void PlaylistOperation() throws IOException{
        int user_id = LoggedIn.LoggedIn();
        ArrayList<Integer> playlistIdList = new ArrayList<Integer>();
        playlistIdList = retrievePlaylistId();
        
         //Start to prompt user to choose the playlist they want to watch
            System.out.println("Please select the above playlist to watch your "
                    + "playlist videos [1-" + (playlistIdList.size()) + "]");
            
            //Start looping to check user's input
            while(true){
                System.out.print("Please select: ");
                int user_input = scan.nextInt();
                scan.nextLine();
                /*If the user's input is in the range, call displayPlaylistVideo() method 
                with playlist_id as argument
                */
         
                if(user_input > 0 && user_input <= (playlistIdList.size())){
                    displayPlaylistVideo(playlistIdList.get(user_input-1));
                     
                    break;
                }
                else{
                    System.out.println("Please enter correct input");
                }
            }
    }
    
     //Method for users to perform general actions on playlist
    public static void PlaylistGeneralOperation() throws IOException{
        int user_id = LoggedIn.LoggedIn();
        System.out.println("\nYou can perform the following actions on your "
                + "playlists");
        System.out.println("A. Watch the videos in the playlist\nB. Create new playlist"
                + "\nC. Delete playlist\nD. Delete video in the playlist");
        System.out.println("");
        
        boolean status = true;
        while(status){
            System.out.print("Please select: ");
            String user_input = scan.nextLine();
            switch(user_input){
                case "A":
                    displayPlaylist();
                    PlaylistOperation();
                    status = false;
                    break;
                case "B":
                    createPlaylist();
                    status = false;
                    break;
                case "C":
                    deletePlaylist();
                    status = false;
                    break;
                case "D":
                    deletePlaylistVideo();
                    status = false;
                    break;
                default:
                    System.out.println("Please enter correct input");
                    status = true;
            }
        }
    }
    
    //To check the user got any playlist or not, need to receive user id as parameter
    public static boolean checkPlaylist(int user_id){
        
        boolean checkPlaylist = false;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT user_id FROM playlistuser WHERE EXISTS"
                    + "(SELECT user_id FROM playlistuser WHERE user_id=?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                //rs.getBoolean will return true if the user_id exist
                checkPlaylist =rs.getBoolean("user_id");
            }
            rs.close();
            
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null){
                    conn.close();
                }
            }catch(SQLException se){
            }
            try{
                if(conn!=null){
                    conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        } 
        return checkPlaylist;
    }
    
    //Display the videos inside the user's playlist
    public static void displayPlaylistVideo(int playlist_id) throws IOException{
        boolean result = checkPlaylistVideo(playlist_id);
      
        Scanner scan = new Scanner(System.in);
        /*Initalise videoIdList to store the video_id that will be passed to 
        displayVideo method
        */
        ArrayList<Integer> videoIdList = new ArrayList<Integer>();
        
        if(result == true){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                String sql = "SELECT * From video INNER JOIN playlistvideo ON "
                        + "playlistvideo.video_id=video.video_id WHERE playlist_id=?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, playlist_id);
                ResultSet rs = stmt.executeQuery();

                //display the video format before displaying available video
                videoOptions.displayVideosFormat();

                //Start to display videos insides user playlist
                int count  = 1;
                while(rs.next()){
                    Formatter f2 = new Formatter();
                    videoIdList.add(rs.getInt("video_id"));
                    int view = rs.getInt("videoViewCount");
                    String title = rs.getString("videoName");
                    String duration = rs.getString("video_length");
                    int user_id = rs.getInt("user_id");
                    String uploaderName = channelName.getChannelName(user_id);
                    String category = rs.getString("videoCategory");
                    f2.format("%d%60s%20d%25s%25s%25s", count, title, view, duration, 
                             uploaderName, category);
                    System.out.println(f2);
                    System.out.println("");
                    count += 1;
                    /*Call playlistVideoOperation with videoIdList as argument to 
                    allow the users to select the videos they want to watch in the playlist*/
                    playlistVideoOperations(videoIdList);
               }
                //Exception to handle error occured when retrieving data 
            }catch(SQLException se){
                se.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try{
                    if(stmt!=null){
                    }
                        conn.close();
                }catch(SQLException se){
                }
                try{
                    if(conn!=null){
                        conn.close();
                    }
                }catch(SQLException se){
                    se.printStackTrace();
                }
            } 
        }
        
        else{
            System.out.println("You do not have any video in this playlist yet");
            Homepage.displayHomepage();
            Homepage.userOperations();
        }
    }
    //Method to allow the user to choose the video they want to watch in the playlist
    public static void playlistVideoOperations(ArrayList<Integer> videoIdList)throws IOException{
        //Prompt the user to give choose the video wanted to watch
            while(true){
                System.out.print("Please select[1-" + videoIdList.size() + 
                        "] to watch the "+ "videos in this playlist or press "
                        + "0 to exit: ");
                int user_input = scan.nextInt();
                scan.nextLine();

                /*if user_input is within the range, call displayVideos method 
                with video_id as parameter
                */
                if(user_input >= 1 && user_input <= videoIdList.size()){
                     videoMenu.mainMenu(videoIdList.get(user_input-1));
                    break;
                }
                
                //if user_input is zero, go back to homepage
                else if(user_input == 0){
                    System.out.println("");
                    Homepage.displayHomepage();
                    Homepage.userOperations();
                     break;
                    
                }
                //if input out of the range, prompt the user over and over again
                else{
                    System.out.println("Please enter correct input");
                    System.out.println("");
                }
            }
    }
    //Method to help the user to create playlist
    public static void createPlaylist(){
        
        int user_id = LoggedIn.LoggedIn();
        //Prompt the user to give a name for the new playlist
        System.out.print("Please enter the name for your new playlist: ");
        String playlist_name = scan.nextLine();
        
        //Start to create the new playlist
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO playlistuser (playlist_id, playlist_name, "
                    + "user_id) VALUES (playlist_id, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, playlist_name);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
            System.out.printf("Your new playlist \'%s\' has been created", playlist_name);
            System.out.println("");
            System.out.println("");
            Homepage.displayHomepage();
            Homepage.userOperations();
            
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null){
                    conn.close();
                }
            }catch(SQLException se){
            }
            try{
                if(conn!=null){
                    conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        } 
    }
    
    
   //Help user to delete playlist, this method need to receive user id
    public static void deletePlaylist() throws IOException{
        int user_id = LoggedIn.LoggedIn();
        boolean result = checkPlaylist(user_id);
        //playlistIdList is initalised to store the playlist_id of the user
        ArrayList<Integer> playListIdList = new ArrayList<Integer>();
        playListIdList = retrievePlaylistId();
        //playListName is initialised to store the playlist_name of the user
        ArrayList<String> playListName = new ArrayList<String>();
      
        if(result == true){
            /*Retrive the playlist's name from the database and store
            in the array list*/
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                String sql = "SELECT playlist_name FROM playlistuser "
                        + "WHERE user_id=?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, user_id);
                ResultSet rs = stmt.executeQuery();
                System.out.println("");
            
                while(rs.next()){
                    playListName.add(rs.getString("playlist_name"));
                    String playlistName = rs.getString("playlist_name");
                }
                rs.close();
            
                //call the displayPlsyList method to display user's available playList
                displayPlaylist();
            
                //Start the playlist deletion process
                System.out.println("Please select the above playlist to be deleted"
                        + "[1-" + playListIdList.size() + "]");
            
                while(true){
                    System.out.print("Please select: ");
                    int user_input = scan.nextInt();
                    scan.nextLine();
                    if(user_input >= 1 && user_input <= playListIdList.size()){
                    
                        /*declare playlist_id to retrieve the id of the playlist the 
                        user wish to delete*/
                        int playlist_id = playListIdList.get(user_input-1);
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        conn = DriverManager.getConnection(DB_URL, USER, PASS);
                        sql = "DELETE FROM playlistuser WHERE playlist_id=?";
                        stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, playlist_id);
                        stmt.executeUpdate();

                        sql = "DELETE FROM playlistvideo WHERE playlist_id=? ";
                        stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, playlist_id);
                        stmt.executeUpdate();

                        System.out.printf("Your playlist \'%s\' has been deleted", 
                                playListName.get(user_input-1));
                        System.out.println("");
                        break;
                    }
                    else{
                        System.out.println("Please enter correct input");
                    }
                }

                Homepage.displayHomepage();
                Homepage.userOperations();
            //Exception to handle error occured when retrieving data 
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null){
                    conn.close();
                }
            }catch(SQLException se){
            }
            try{
                if(conn!=null){
                    conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        } 
        }
        else{
            System.out.println("You do not have any playlist to be deleted yet");
            Homepage.displayHomepage();
            Homepage.userOperations();
        }
    }
    
    //Method to allow users the delete video stored in the playlist
    public static void deletePlaylistVideo(){
        String sql;
        int count;
        int user_id = LoggedIn.LoggedIn();
        boolean playlistAvailable = checkPlaylist(user_id);
        
        //Initialise playListIdList array to be used for storing the playlist_id
        ArrayList<Integer> playlistIdList = new ArrayList<Integer>();
        playlistIdList = retrievePlaylistId();
        
        //Initialise videoIdList array to be used for storing the video_id
        ArrayList<Integer> videoIdList = new ArrayList<Integer>();
        
        //Initialise videoNameList array to be used for storing the video_name
        ArrayList<String> videoNameList = new ArrayList<String>();
        
        try{
            if(playlistAvailable == true){
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                sql = "SELECT playlist_name FROM playlistuser "
                        + "WHERE user_id=?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, user_id);
                ResultSet rs = stmt.executeQuery();
                System.out.println("");

                //start to display user's available playlist
                System.out.println("Hello user, you have the following playlist");
                count = 1;
                while(rs.next()){
                    String playlistName = rs.getString("playlist_name");
                    System.out.println(count + "." + " " + playlistName);
                    count += 1;
                }
                rs.close();
            }
            else{
                System.out.println("You do not have any playlist yet");
                Homepage.displayHomepage();
                Homepage.userOperations();
            }
            
            //Start to prompt user to choose the video playlist they want to delete
            System.out.println("Please select the above playlist to delete your "
                    + "playlist videos [1-" + playlistIdList.size() + "]");
            
            //Start looping to check user's input
            while(true){
                System.out.print("Please select: ");
                int user_input = scan.nextInt();
                scan.nextLine();
                
                /*If the user's input is in the range,display the user 
                available video
                */
                if(user_input > 0 && user_input <= playlistIdList.size()){
                    boolean result = checkPlaylistVideo(playlistIdList.get(user_input-1));
                    if(result == true){
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        conn = DriverManager.getConnection(DB_URL, USER, PASS);
                        sql = "SELECT *FROM video INNER JOIN "
                                + "playlistvideo ON playlistvideo.video_id=video.video_id "
                                + "WHERE playlist_id=?";
                        stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, playlistIdList.get(user_input-1));
                        ResultSet rs = stmt.executeQuery();

                        //display the video format before displaying available video
                        videoOptions.displayVideosFormat();

                        //Start to display videos insides user playlist
                        count  = 1;
                        while(rs.next()){

                        Formatter f2 = new Formatter();
                        videoIdList.add(rs.getInt("video_id"));
                        int view = rs.getInt("videoViewCount");
                        String title = rs.getString("videoName");
                        videoNameList.add(rs.getString("videoName"));
                        String duration = rs.getString("video_length");
                        int userid = rs.getInt("user_id");
                        String uploaderName = channelName.getChannelName(userid);
                        String category = rs.getString("videoCategory");
                        f2.format("%d%60s%20d%25s%25s%25s", count, title, view, duration, 
                                 uploaderName, category);
                        System.out.println(f2);
                        System.out.println("");
                        count += 1;
                        }
                        rs.close();
                    
                    }
                    else{
                        System.out.println("You do not have any video to be deleted inside this playlist");
                        Homepage.displayHomepage();
                        Homepage.userOperations();
                    }
                    break;
                }
                else{
                    System.out.println("Please enter correct input");
                }
            
            }
            
            System.out.println("Please select the videos you wanted to delete[1-"
                    + ""+ videoIdList.size() + "]");
            
            //Start looping to check user's choice
            while(true){
                System.out.print("Please select: ");
                int user_input = scan.nextInt();
                scan.nextLine();
                /*If the user input is in the range, delete the video selected 
                by the user*/
                if(user_input >= 1 && user_input <= videoIdList.size()){
                    
                    sql = "DELETE FROM playlistvideo WHERE video_id=?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, videoIdList.get(user_input-1));
                    stmt.executeUpdate();
                    System.out.printf("Your video \'%s\' has been deleted", 
                            videoNameList.get(user_input-1));
                    System.out.println("");
                    Homepage.displayHomepage();
                    Homepage.userOperations();
                    break;
                    
                }
                
                //Repeat the loop if user enter wrong input
                else{
                    System.out.println("Please enter correct input");
                }
            }
            
            
            
            System.out.println("");
            //Exception to handle error occured when retrieving data 
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null){
                    conn.close();
                }
            }catch(SQLException se){
            }
            try{
                if(conn!=null){
                    conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        } 
    }
    
    //Method to allow the user to add video into a playlist
    public static void addVideo(int video_id) throws IOException{
        
        int user_id = LoggedIn.LoggedIn();
        boolean result = checkPlaylist(user_id);
        if(result == true){
            ArrayList<Integer> playlistIdList = new ArrayList<Integer>();
            playlistIdList = retrievePlaylistId();
            displayPlaylist();
            System.out.println("Please select the above playlist to add"
                    + " your video [1-" + playlistIdList.size() + "]");
            try{
                while(true){
                    System.out.print("Please select: ");
                    int user_input = scan.nextInt();
                    if(user_input > 0 && user_input <= playlistIdList.size()){
                        int playlist_id = playlistIdList.get(user_input-1);
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        conn = DriverManager.getConnection(DB_URL, USER, PASS);
                        String sql = "INSERT INTO playlistvideo(playlist_id, video_id) VALUES"
                                + "(?, ?)";
                        stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, playlist_id);
                        stmt.setInt(2, video_id);
                        stmt.executeUpdate();
                        System.out.println("The video has been added into your playlist");
                        break;
                    }
                    else{
                        System.out.println("Please enter correct input");
                    }
                }
            }catch(SQLException se){
                se.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try{
                    if(stmt!=null){
                        conn.close();
                    }
                }catch(SQLException se){
                }
                try{
                    if(conn!=null){
                        conn.close();
                    }
                }catch(SQLException se){
                    se.printStackTrace();
                }
            } 
        }
        else{
            System.out.println("It seems that you do not have any playlist yet, "
                    + "do you want to create a playlist to store a video? ");
            System.out.println("Please enter \'Y\' to create a playlist or "
                    + "\'E\' to exit");
            stop: while(true){
                System.out.print("Please select: ");
                String user_input = scan.nextLine();
                switch(user_input){
                    case "Y":
                        createPlaylist();
                        addVideo(video_id);
                        break stop;
                    case "E":
                        System.out.println("");
                        Homepage.userOperations();
                        break stop;
                    default:
                        System.out.println("Please enter correct input");
                }
            }
        }
    }
    
    public static ArrayList <Integer> retrievePlaylistId(){
        int user_id = LoggedIn.LoggedIn();
        ArrayList<Integer> playlistIdList = new ArrayList<Integer>();
         try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT playlist_id FROM playlistuser "
                    + "WHERE user_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            System.out.println("");
            
            while(rs.next()){
                playlistIdList.add(rs.getInt("playlist_id"));
                        
            }
            rs.close();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{   
            try{
                if(stmt!=null){
                    conn.close();
                }
            }catch(SQLException se){
            }
            try{
                if(conn!=null){
                    conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
         return playlistIdList;
    }
    
     //To check the user got any video inside  playlist or not, need to receive playlist id as parameter
    public static boolean checkPlaylistVideo(int playlist_id){
        
        boolean checkPlaylistVideo = false;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT video_id from playlistvideo WHERE EXISTS"
                    + "(SELECT video_id FROM playlistvideo WHERE playlist_id=?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, playlist_id);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                //rs.getBoolean will return true if the video_id exist
                checkPlaylistVideo = rs.getBoolean("video_id");
            }
            rs.close();
            
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null){
                    conn.close();
                }
            }catch(SQLException se){
            }
            try{
                if(conn!=null){
                    conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        } 
        return checkPlaylistVideo;
    }
}





