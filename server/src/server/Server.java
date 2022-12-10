package server;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.net.ConnectException;
import java.net.ServerSocket;
 
public class Server {
	
	static String user;
	static String operator;
	static int SignalPower;
	static String snr;
	static String networkType;
	static String FrequencyBand;
	static String CellID;
	static String date;
	static String time;
	
	static String date1;
	static String date2;
	
	static Socket s;
	static InputStreamReader isr;
	static BufferedReader br;
	static String data = "";
	static String stat;
	static Statistics statistics = new Statistics();
 
    public static void main(String[] args) throws IOException, ParseException {
    	
    	/**
		 * The following section of the code creates a database if one doesn't already exist
		 */
		File tmpDir = new File("db.db");
		
		if(!tmpDir.exists()) {

			Connection c = null;

			Statement stmt = null;

			try {

				Class.forName("org.sqlite.JDBC");
	
				c = DriverManager.getConnection("jdbc:sqlite:db.db");
	
				System.out.println("Database Opened...\n");
	
				stmt = c.createStatement();
	
				String sql = "CREATE TABLE userData " +
	
				"(uid INTEGER PRIMARY KEY AUTOINCREMENT," +
	
				" mobileUser TEXT NOT NULL, " +
				" operator TEXT NOT NULL, " +
				" SignalPower INTEGER, " +
				" SNR INTEGER, " + 
				" NetworkType TEXT, " + 
				" FrequencyBand TEXT, " + 
				" CellID TEXT, " + 
				" Date DATE, " +
				" Time TEXT)";
	
				stmt.executeUpdate(sql);
	
				stmt.close();
	
				c.close();

			}

			catch ( Exception e ) {

				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	
				System.exit(0);

			}

			System.out.println("Database created!");
		}
		

       int port = 7800;

       ServerSocket socket = new ServerSocket(port);
       Socket ss;

       while(true) {

    	   System.out.println("\nServer is Listening!!");
    	   s = socket.accept();
    	   isr = new InputStreamReader(s.getInputStream());
    	   br = new BufferedReader(isr);
    	   data = br.readLine();
    	   

    	   if(data != null && data.startsWith("REQUEST")) {
    		   
    		   System.out.println("Request is Sent");
    		   System.out.println("Check the Statistics on the Mobile");
    	   
    	   	    	
		    	try {
		    		   
		    		   stat = statistics.print();
		    		   
		    		   ss = new Socket("10.169.32.87", 4000);
		    		   
		    		   
		    		   
		    		   PrintWriter writer = new PrintWriter(ss.getOutputStream());
		               writer.write(stat);
		               writer.flush();
		               writer.close();
		               ss.close();
		               
		    	}catch(ConnectException ioe) {
		    		ioe.printStackTrace();
		    		
		    	}
		    	
		    	StringTokenizer t = new StringTokenizer(data);
	        	   
	        	String word ="";
	        	   
        	    int i = 0; 
        	    String[] arr1 = new String[3];
        	   
        	    while(t.hasMoreTokens()){
        		   
        		    word = t.nextToken();
        		    System.out.print(word + "  ");
        		   
        		    arr1[i] = word;
        		   
        		    i++;
        	    }
        	    
        	    date1 = arr1[1];
        	    date2 = arr1[2];
	       
	        	   
    	   }
    	   else {
    		   
    		   System.out.println("Mobile Data are: ");
    		   
        	   StringTokenizer t = new StringTokenizer(data);
        	   
        	   String word ="";
        	   
        	   int i = 0; 
        	   String[] arr = new String[9];
        	   
        	   while(t.hasMoreTokens()){
        		   
        		   word = t.nextToken();
        		   System.out.print(word + "  ");
        		   
        		   arr[i] = word;
        		   
        		   i++;
        	   }
        	   
    		   
    		   user = arr[0];
    		   operator = arr[1];
    		   SignalPower = Integer.valueOf(arr[2]);
    		   snr = arr[3];
    		   networkType = arr[4];
    		   FrequencyBand = arr[5];
    		   CellID = arr[6];
    		   date = arr[7];
    		   time = arr[8];
        	   
       		   Connection c = null;
       		   Statement stmt = null;
       		   
       		   try {
       			   
       	   		   Class.forName("org.sqlite.JDBC");
       			   c = DriverManager.getConnection("jdbc:sqlite:db.db");
       			   stmt = c.createStatement();
       			   
       			   String sql = "INSERT INTO userData(mobileUser, operator, SignalPower,"
       			   								+ "SNR, NetworkType, FrequencyBand, CellID,"
       			   								+ "Date, Time) "
       			   								+ "VALUES('" + user +"', '" 
       					   										+ operator + "'," 
       					   										+ SignalPower + ",'"
       					   										+ snr + "','" 
       					   										+ networkType + "','"
       					   										+ FrequencyBand + "','"
       					   										+ CellID + "', '"
       					   										+ date + "','"
       					   										+ time + "')";
       			   
       			   stmt.executeUpdate(sql);
       			   stmt.close();
       			   c.close();
       			   
       		   }catch(Exception e) {
       				e.printStackTrace();
       			}   
    	   }
       }

     }
  }