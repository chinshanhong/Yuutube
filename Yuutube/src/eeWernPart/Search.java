package eeWernPart;

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
import yuutube.channelName;
import yuutube.videoMenu;

/**
 *
 * @author USER
 */
public class Search{
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/yuutube?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static int[] videoID = {0, 0, 0, 0, 0};
    public static void main(String[] args) throws IOException {
    }

    public static void searchMenu() throws IOException {
        Scanner s = new Scanner(System.in);
        boolean error = true;
        int searchOption=0;
        while (error) {
            System.out.println("Enter 1 - Search based on video title");
            System.out.println("Enter 2 - Search based on channel");
            System.out.print("Enter: ");
            if(s.hasNextInt()){
            searchOption = s.nextInt();
            if (searchOption == 1) 
                searchVideo();
            else if (searchOption == 2) 
                searchChannel();
            else {
                System.out.println("Pleaese enter the number correctly.");
                error=true;
                continue;
            }
            }
            else{
                System.out.println("Sorry you had enter non numeric number.");
                s.next();
                continue;
            }
            error = false;
            
            }
    }

    public static void randomVideo() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            int j = 0;
            PreparedStatement stment1 = conn.prepareStatement("SELECT video.videoName,video.videoViewCount,user.displayName,video.videoLikeCount,video.user_id,video.video_id  "
                    + "FROM user INNER JOIN video ON user.user_id = video.user_id "
                    + "ORDER BY RAND() LIMIT 5");
            ResultSet rs1 = stment1.executeQuery();
            while (rs1.next()) {
                j++;
                System.out.println("No " + j);
                videoID[j - 1] = rs1.getInt("video_id");
                System.out.print("Video Title      : ");
                System.out.println(rs1.getString("videoName"));
                System.out.print("Channel          : ");
                System.out.println(rs1.getString("displayName"));
                System.out.print("Video Like Counts: ");
                System.out.println(rs1.getInt("videoLikeCount"));
                System.out.print("Video View Counts: ");
                System.out.println(rs1.getInt("videoViewCount"));
                System.out.println("-----------------------------------------------------------------------------");

            }

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(Search.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    public static void searchVideo() throws IOException {
        Scanner s = new Scanner(System.in);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            int options = 1;
            while (options == 1) {
                System.out.print("Enter video title: ");
                String text = s.nextLine();
                //Full text search               
                PreparedStatement stment = conn.prepareStatement("SELECT video.videoName,video.videoViewCount,user.displayName,video.videoLikeCount,video.user_id,video.video_id "
                        + "FROM user INNER JOIN video ON user.user_id = video.user_id "
                        + "WHERE MATCH (videoName)AGAINST('" + text + "' IN BOOLEAN MODE)LIMIT 5");
                ResultSet rs = stment.executeQuery();
                int i = 0;
                while (rs.next()) {
                    i++;
                    System.out.println("No " + i);
                    videoID[i - 1] = rs.getInt("video_id");
                    System.out.print("Video Title      : ");
                    System.out.println(rs.getString("videoName"));
                    System.out.print("Channel          : ");
                    System.out.println(rs.getString("displayName"));
                    System.out.print("Video Like Counts: ");
                    System.out.println(rs.getInt("videoLikeCount"));
                    System.out.print("Video View Counts: ");
                    System.out.println(rs.getInt("videoViewCount"));
                    System.out.println("-----------------------------------------------------------------------------");

                    System.out.println("-----------------------------------------------------------------------------");
                }

                //Randomly recommend videos if no matched results
                if (i == 0) {
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.println("Sorry the video is not found. Here are some recommendations special for you.");
                    System.out.println("-----------------------------------------------------------------------------");
                    randomVideo();
                    System.out.println("Options: ");
                    System.out.printf(" 1 - SEARCH AGAIN\n "
                            + "2 - PLAY VIDEO\n "
                            + "3 - BACK TO HOMEPAGE\n "
                            + "4 - SEARCH AGAIN BASED ON [CHANNEL NAME]"
                            + "\n");
                    System.out.print("Enter option: ");
                    options = s.nextInt();
                    System.out.println("-----------------------------------------------------------------------------");
                    s.nextLine();
                    switch (options) {
                        case 1:
                            break;
                        case 2: //play video
                            System.out.print("Enter video no to play the video:");
                            int videoNO = s.nextInt();
                            int videoId = videoID[videoNO - 1];
                            videoMenu.mainMenu(videoId);
                            break;
                        case 3: //Back to homepage
                            Homepage.displayHomepage();
                            Homepage.userOperations();
                            break;
                        case 4: //Search based on channel name
                            searchChannel();
                            break;
                        default:
                            System.out.println("Invalid options");
                            break;
                    }
                } else if (i > 0) {
                    System.out.println("Options: ");
                    System.out.printf(" 1 - SEARCH AGAIN\n "
                            + "2 - PLAY VIDEO\n "
                            + "3 - SORT VIDEOS based on VIEW COUNTS in DESCENDING ORDER\n "
                            + "4 - SORT VIDEOS based on LIKE COUNTS in DESCENDING ORDER\n "
                            + "5 - BACK TO HOMEPAGE\n "
                            + "6 - SEARCH BASED ON [CHANNEL NAME]\n");
                    System.out.print("Enter option: ");
                    options = s.nextInt();
                    System.out.println("-----------------------------------------------------------------------------");
                    s.nextLine();

                    switch (options) {
                        case 1:
                            break;
                        case 2: //play video
                            System.out.print("Enter video no to play the video:");
                            int videoNO = s.nextInt();
                            int videoId = videoID[videoNO - 1];
                            videoMenu.mainMenu(videoId);
                            break;
                        case 3: //sort based on view counts
                            PreparedStatement stmentV = conn.prepareStatement("SELECT video.videoName,video.videoViewCount,user.displayName,video.videoLikeCount,video.user_id,video.video_id  "
                                    + "FROM user INNER JOIN video ON user.user_id = video.user_id "
                                    + "WHERE MATCH (videoName)AGAINST('" + text + "' IN BOOLEAN MODE) "
                                    + "ORDER BY video.videoViewCount DESC LIMIT 5");
                            ResultSet rs2 = stmentV.executeQuery();
                            int k = 0;
                            while (rs2.next()) {
                                k++;
                                System.out.println("No " + k);
                                videoID[k - 1] = rs2.getInt("video_id");
                                System.out.print("Video Title      : ");
                                System.out.println(rs2.getString("videoName"));
                                System.out.print("Channel          : ");
                                System.out.println(rs2.getString("displayName"));
                                System.out.print("Video Like Counts: ");
                                System.out.println(rs2.getInt("videoLikeCount"));
                                System.out.print("Video View Counts: ");
                                System.out.println(rs2.getInt("videoViewCount"));
                                System.out.println("-----------------------------------------------------------------------------");
                            }
                            System.out.println("Options: ");
                            System.out.printf(" 1 - SEARCH AGAIN\n "
                                    + "2 - PLAY VIDEO\n "
                                    + "3 - BACK TO HOMEPAGE\n "
                                    + "4 - SEARCH AGAIN BASED ON [CHANNEL NAME]\n");
                            System.out.print("Enter option: ");
                            options = s.nextInt();
                            System.out.println("-----------------------------------------------------------------------------");
                            s.nextLine();
                            switch (options) {
                                case 2:
                                    System.out.print("Enter video no to play the video:");
                                    videoNO = s.nextInt();
                                    videoId = videoID[videoNO - 1];
                                    videoMenu.mainMenu(videoId);
                                    break;
                                case 3:
                                    Homepage.displayHomepage();
                                    Homepage.userOperations();
                                    break;
                                case 1:
                                    System.out.println();
                                    break;
                                case 4:
                                    searchChannel();
                                    break;
                                default:
                                    System.out.println("Invalid option");
                                    break;
                            }
                            break;

                        case 4: //sort based on like counts
                            PreparedStatement stmentL = conn.prepareStatement("SELECT video.videoName,video.videoViewCount,user.displayName,video.videoLikeCount,video.user_id,video.video_id  "
                                    + "FROM user INNER JOIN video ON user.user_id = video.user_id "
                                    + "WHERE MATCH (videoName)AGAINST('" + text + "' IN BOOLEAN MODE) "
                                    + "ORDER BY video.videoLikeCount DESC LIMIT 5");
                            ResultSet rs3 = stmentL.executeQuery();
                            int h = 0;
                            while (rs3.next()) {
                                h++;
                                System.out.println("No " + h);
                                videoID[h - 1] = rs3.getInt("video_id");
                                System.out.print("Video Title      : ");
                                System.out.println(rs3.getString("videoName"));
                                System.out.print("Channel          : ");
                                System.out.println(rs3.getString("displayName"));
                                System.out.print("Video Like Counts: ");
                                System.out.println(rs3.getInt("videoLikeCount"));
                                System.out.print("Video View Counts: ");
                                System.out.println(rs3.getInt("videoViewCount"));
                                System.out.println("-----------------------------------------------------------------------------");
                            }
                            System.out.printf(" 1 - SEARCH AGAIN\n "
                                    + "2 - PLAY VIDEO\n "
                                    + "3 - BACK TO HOMEPAGE\n "
                                    + "4 - SEARCH AGAIN BASED ON [CHANNEL NAME]\n");
                            System.out.print("Enter option: ");
                            options = s.nextInt();
                            System.out.println("-----------------------------------------------------------------------------");
                            s.nextLine();
                            switch (options) {
                                case 2://play video
                                    System.out.print("Enter video no to play the video: ");
                                    videoNO = s.nextInt();
                                    videoId = videoID[videoNO - 1];
                                    videoMenu.mainMenu(videoId);
                                    break;
                                case 3://Back to homepage
                                    Homepage.displayHomepage();
                                    Homepage.userOperations();
                                    break;
                                case 1:
                                    System.out.println();
                                    break;
                                case 4://Search based on channel name
                                    searchChannel();
                                    break;
                                default:
                                    System.out.println("Invalid options");
                                    break;
                            }

                            break;

                        case 5: //back to homepage
                            Homepage.displayHomepage();
                            Homepage.userOperations();
                            break;
                        case 6://search based on channel name
                            searchChannel();
                            break;
                        default:
                            System.out.println("Invalid options");
                            break;
                    }
                }

            }

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(Search.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void searchChannel() throws IOException {
        Scanner s = new Scanner(System.in);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            int options = 1;
            while (options == 1) {
                System.out.print("Enter channel name: ");
                String text = s.nextLine();
                //Full text search               
                PreparedStatement stment = conn.prepareStatement("SELECT video.videoName,video.videoViewCount,user.displayName,video.videoLikeCount,video.user_id,video.video_id "
                        + "FROM user INNER JOIN video ON user.user_id = video.user_id "
                        + "WHERE MATCH (displayName)AGAINST('" + text + "' IN BOOLEAN MODE)LIMIT 5");
                ResultSet rs = stment.executeQuery();
                int i = 0;
                while (rs.next()) {
                    i++;
                    System.out.println("No " + i);
                    videoID[i - 1] = rs.getInt("video_id");
                    System.out.print("Video Title      : ");
                    System.out.println(rs.getString("videoName"));
                    System.out.print("Channel          : ");
                    System.out.println(rs.getString("displayName"));
                    System.out.print("Video Like Counts: ");
                    System.out.println(rs.getInt("videoLikeCount"));
                    System.out.print("Video View Counts: ");
                    System.out.println(rs.getInt("videoViewCount"));
                    System.out.println("-----------------------------------------------------------------------------");

                    System.out.println("-----------------------------------------------------------------------------");
                }

                //Randomly recommend videos if no matched result
                if (i == 0) {
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.println("Sorry the channel is not found. Here are some recommendations special for you.");
                    System.out.println("-----------------------------------------------------------------------------");
                    randomVideo();
                    System.out.println("Options: ");
                    System.out.printf(" 1 - SEARCH AGAIN\n "
                            + "2 - PLAY VIDEO\n "
                            + "3 - BACK TO HOMEPAGE\n "
                            + "4 - SEARCH AGAIN BASED ON [VIDEO TITLE]"
                            + "\n");
                    System.out.print("Enter option: ");
                    options = s.nextInt();
                    System.out.println("-----------------------------------------------------------------------------");
                    s.nextLine();
                    switch (options) {
                        case 1:
                            break;
                        case 2: //play video
                            System.out.print("Enter video no to play the video:");
                            int videoNO = s.nextInt();
                            int videoId = videoID[videoNO - 1];
                            videoMenu.mainMenu(videoId);
                            break;
                        case 3: //back to homepage
                            //homepage method
                            Homepage.displayHomepage();
                            Homepage.userOperations();
                            break;
                        case 4: //Search based on video title
                            searchVideo();
                            break;
                        default:
                            System.out.println("Invalid options");
                            break;
                    }
                } else if (i > 0) {
                    System.out.println("Options: ");
                    System.out.printf(" 1 - SEARCH AGAIN\n "
                            + "2 - PLAY VIDEO\n "
                            + "3 - SORT VIDEOS based on VIEW COUNTS in DESCENDING ORDER\n "
                            + "4 - SORT VIDEOS based on LIKE COUNTS in DESCENDING ORDER\n "
                            + "5 - BACK TO HOMEPAGE\n "
                            + "6 - SEARCH BASED ON [VIDEO TITLE]\n");
                    System.out.print("Enter option: ");
                    options = s.nextInt();
                    System.out.println("-----------------------------------------------------------------------------");
                    s.nextLine();

                    switch (options) {
                        case 1:
                            break;
                        case 2: //play video
                            System.out.print("Enter video no to play the video:");
                            int videoNO = s.nextInt();
                            int videoId = videoID[videoNO - 1];
                            videoMenu.mainMenu(videoId);
                            break;
                        case 3: //sort based on view counts
                            PreparedStatement stmentV = conn.prepareStatement("SELECT video.videoName,video.videoViewCount,user.displayName,video.videoLikeCount,video.user_id,video.video_id  "
                                    + "FROM user INNER JOIN video ON user.user_id = video.user_id "
                                    + "WHERE MATCH (displayName)AGAINST('" + text + "' IN BOOLEAN MODE) "
                                    + "ORDER BY video.videoViewCount DESC LIMIT 5");
                            ResultSet rs2 = stmentV.executeQuery();
                            int k = 0;
                            while (rs2.next()) {
                                k++;
                                System.out.println("No " + k);
                                videoID[k - 1] = rs2.getInt("video_id");
                                System.out.print("Video Title      : ");
                                System.out.println(rs2.getString("videoName"));
                                System.out.print("Channel          : ");
                                System.out.println(rs2.getString("displayName"));
                                System.out.print("Video Like Counts: ");
                                System.out.println(rs2.getInt("videoLikeCount"));
                                System.out.print("Video View Counts: ");
                                System.out.println(rs2.getInt("videoViewCount"));
                                System.out.println("-----------------------------------------------------------------------------");
                            }
                            System.out.println("Options: ");
                            System.out.printf(" 1 - SEARCH AGAIN\n "
                                    + "2 - PLAY VIDEO\n "
                                    + "3 - BACK TO HOMEPAGE\n "
                                    + "4 - SEARCH AGAIN BASED ON [VIDEO TITLE]\n");
                            System.out.print("Enter option: ");
                            options = s.nextInt();
                            System.out.println("-----------------------------------------------------------------------------");
                            s.nextLine();
                            switch (options) {
                                case 2://play video
                                    System.out.print("Enter video no to play the video:");
                                    videoNO = s.nextInt();
                                    videoId = videoID[videoNO - 1];
                                    videoMenu.mainMenu(videoId);
                                    break;
                                case 3://Back to homepage
                                    Homepage.displayHomepage();
                                    Homepage.userOperations();
                                    break;
                                case 1:
                                    System.out.println();
                                    break;
                                case 4:
                                    searchVideo();
                                    break;
                                default:
                                    System.out.println("Invalid option");
                                    break;
                            }
                            break;

                        case 4: //sort based on like counts
                            PreparedStatement stmentL = conn.prepareStatement("SELECT video.videoName,video.videoViewCount,user.displayName,video.videoLikeCount,video.user_id,video.video_id  "
                                    + "FROM user INNER JOIN video ON user.user_id = video.user_id "
                                    + "WHERE MATCH (displayName)AGAINST('" + text + "' IN BOOLEAN MODE) "
                                    + "ORDER BY video.videoLikeCount DESC LIMIT 5");
                            ResultSet rs3 = stmentL.executeQuery();
                            int h = 0;
                            while (rs3.next()) {
                                h++;
                                System.out.println("No " + h);
                                videoID[h - 1] = rs3.getInt("video_id");
                                System.out.print("Video Title      : ");
                                System.out.println(rs3.getString("videoName"));
                                System.out.print("Channel          : ");
                                System.out.println(rs3.getString("displayName"));
                                System.out.print("Video Like Counts: ");
                                System.out.println(rs3.getInt("videoLikeCount"));
                                System.out.print("Video View Counts: ");
                                System.out.println(rs3.getInt("videoViewCount"));
                                System.out.println("-----------------------------------------------------------------------------");
                            }
                            System.out.printf(" 1 - SEARCH AGAIN\n "
                                    + "2 - PLAY VIDEO\n "
                                    + "3 - BACK TO HOMEPAGE\n "
                                    + "4 - SEARCH AGAIN BASED ON [VIDEO TITLE]\n");
                            System.out.print("Enter option: ");
                            options = s.nextInt();
                            System.out.println("-----------------------------------------------------------------------------");
                            s.nextLine();
                            switch (options) {
                                case 2://Play video
                                    System.out.print("Enter video no to play the video: ");
                                    videoNO = s.nextInt();
                                    videoId = videoID[videoNO - 1];
                                    videoMenu.mainMenu(videoId);
                                    break;
                                case 3://Back to homepage
                                    Homepage.displayHomepage();
                                    Homepage.userOperations();
                                    break;
                                case 1:
                                    System.out.println();
                                    break;
                                case 4://Search based on channel name
                                    searchVideo();
                                    break;
                                default:
                                    System.out.println("Invalid options");
                                    break;
                            }

                            break;

                        case 5: //back to homepage
                            Homepage.displayHomepage();
                            Homepage.userOperations();
                            break;
                        case 6://Search based on video title
                            searchVideo();
                            break;
                        default:
                            System.out.println("Invalid options");
                            break;
                    }
                }

            }

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(Search.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}



