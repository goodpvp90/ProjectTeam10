package serverprotopack;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.Statement;

public class ServerProto extends AbstractServer {

	public ServerProto(int port) {
		super(port);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		// TODO Auto-generated method stub
		
	}
	
	
	// connects to DB and returns the connection
	private static Connection connectToDB() {
		try
		{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
        	/* handle the error*/
        	System.out.println("Driver definition failed");
        }

        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/proto_schema?serverTimezone=IST","root","Aa12345");
            System.out.println("SQL connection succeed");
            return conn;
        }catch (SQLException ex)
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return null;
	}
	
	
	public static void main(String[] args) {
	    int port = 12345; // Replace with your desired port number
	    ServerProto server = new ServerProto(port);
		Connection con;
		try {
			con = connectToDB();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Statement stmt;
		try 
		{
			con = connectToDB();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("INSERT INTO Orders (Order_number, Total_price, Order_list_number, Order_address)\r\n"
					+ "VALUES (1, 50.00, 4, '123 Main St, Cityville, State, Zip');");
	 		while(rs.next())
	 		{
				 // Print out the values
				 System.out.println(rs.getString(1)+"  " +rs.getString(2));
			} 
			rs.close();
			//stmt.executeUpdate("UPDATE course SET semestr=\"W08\" WHERE num=61309");
		} catch (SQLException e) {e.printStackTrace();}
	}
}
