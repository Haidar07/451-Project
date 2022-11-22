package server;

import java.io.*;
import java.net.*;
 

public class Server {
	
	static Socket s;
	static InputStreamReader isr;
	static BufferedReader br;
	static String data;
 
    public static void main(String[] args) {
        //if (args.length < 1) return;
 
           int port = 7800;
           
           try (ServerSocket socket = new ServerSocket(port)) {

               while(true) {
            	   
            	   s = socket.accept();
            	   isr = new InputStreamReader(s.getInputStream());
            	   br = new BufferedReader(isr);
            	   data = br.readLine();
            	   
            	   System.out.println(data);
            	   
               }
           }catch(IOException e) {
        	   e.printStackTrace();
           }
       }
  }