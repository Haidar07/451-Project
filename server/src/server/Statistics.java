package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Statistics {
	
	ResultSet rs;
	
	String AvgOperator(){
		
		Connection c = null;
	    Statement stmt = null;
	    int touch = 0;
	    int alfa = 0;
		   
		   try {
			   
	   		   Class.forName("org.sqlite.JDBC");
			   c = DriverManager.getConnection("jdbc:sqlite:db.db");
			   stmt = c.createStatement();
			   
			   String sql = "SELECT operator FROM userData";
			   
			   rs = stmt.executeQuery(sql);
			   
			   String operator;
			   
			   while(rs.next()) {
				   
				   operator = rs.getString("operator");
				   if(operator.equals("Touch")) {
					   touch++;
				   }
				   else {
					   alfa++;
				   }
			   }
			   
			   stmt.close();
			   c.close();
			   
		    }catch(Exception e) {
				e.printStackTrace();
			}  
		   
		int per1 = (touch/(touch+alfa))*100;
		int per2 = (alfa/(touch+alfa))*100;
		
		return String.valueOf(per2) + "%-Touch_" + String.valueOf(per2) + "%-ALFA";
	}
	
	String AvgNetwork() {
		
		Connection c = null;
	    Statement stmt = null;
	    int g3 = 0;
	    int g4 = 0;
		   
		   try {
			   
	   		   Class.forName("org.sqlite.JDBC");
			   c = DriverManager.getConnection("jdbc:sqlite:db.db");
			   stmt = c.createStatement();
			   
			   String sql = "SELECT NetworkType FROM userData";
			   
			   rs = stmt.executeQuery(sql);
			   
			   String network;
			   
			   while(rs.next()) {
				   
				   network = rs.getString("NetworkType");
				   if(network.equals("3G")) {
					   g3++;
				   }
				   else {
					   g4++;
				   }
			   }
			   
			   stmt.close();
			   c.close();
			   
		    }catch(Exception e) {
				e.printStackTrace();
			}  
		   
		int per1 = (g3/(g3+g4))*100;
		int per2 = (g4/(g4+g3))*100;
		
		return String.valueOf(per2) + "%-3G_" + String.valueOf(per2) + "%-4G";
	}
	
	String AvgSignalPower() {
		
		Connection c = null;
	    Statement stmt = null;
		int sum = 0;
		int counter = 0;
		   
		   try {
			   
	   		   Class.forName("org.sqlite.JDBC");
			   c = DriverManager.getConnection("jdbc:sqlite:db.db");
			   stmt = c.createStatement();
			   
			   String sql = "SELECT SignalPower FROM userData";
			   
			   rs = stmt.executeQuery(sql);
			   
			   int network;
			   
			   while(rs.next()) {
				   
				   network = rs.getInt("SignalPower");
				   sum =+ network;
				   counter ++;
			   }
			   
			   stmt.close();
			   c.close();
			   
		    }catch(Exception e) {
				e.printStackTrace();
			}  
		   
		
		int avg = sum/counter;
		
		return String.valueOf(avg);
	}
	
	String AvgSNR() {
		
		Connection c = null;
	    Statement stmt = null;
		int sum = 0;
		int counter = 0;
		   
		   try {
			   
	   		   Class.forName("org.sqlite.JDBC");
			   c = DriverManager.getConnection("jdbc:sqlite:db.db");
			   stmt = c.createStatement();
			   
			   String sql = "SELECT SignalPower FROM userData";
			   
			   rs = stmt.executeQuery(sql);
			   
			   int network;
			   
			   while(rs.next()) {
				   
				   network = rs.getInt("SignalPower");
				   sum =+ network;
				   counter ++;
			   }
			   
			   stmt.close();
			   c.close();
			   
		    }catch(Exception e) {
				e.printStackTrace();
			}  
		   
		
		int avg = sum/counter;
		
		return String.valueOf(avg);
	}

	String print() {
		
		return AvgOperator() + " " + AvgNetwork() + " " + AvgSignalPower() + " " + AvgSNR();
	}
}
