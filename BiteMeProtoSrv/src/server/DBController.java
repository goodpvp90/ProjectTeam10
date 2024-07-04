package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBController {
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/bitemeproto?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "Aa12345";

	private Connection connection;

	public void connect() throws ClassNotFoundException, SQLException {
		// Load MySQL JDBC Driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		// Establish the connection
		connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
		System.out.println("Connected to the database successfully!");
	}

	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
				System.out.println("Database connection closed.");
			} catch (SQLException e) {
				System.out.println("Failed to close the database connection.");
				e.printStackTrace();
			}
		}
	}

	public void updateOrder(int orderNumber, String restaurantName, double totalPrice, int orderListNumber,
			String orderAddress) {
		String query = "UPDATE `orders` SET name_of_restaurant=?, Total_price=?, order_list_number=?, order_address=? WHERE OrderNumber=?";

		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setString(1, restaurantName);
			preparedStatement.setDouble(2, totalPrice);
			preparedStatement.setInt(3, orderListNumber);
			preparedStatement.setString(4, orderAddress);
			preparedStatement.setInt(5, orderNumber);

			int rowsAffected = preparedStatement.executeUpdate();
			System.out.println("Order updated successfully. Rows affected: " + rowsAffected);
		} catch (SQLException e) {
			System.out.println("Failed to update the order.");
			e.printStackTrace();
		}
	}

	public void insertOrder(int orderNumber, String restaurantName, double totalPrice, int orderListNumber, String orderAddress) {
        String query = "INSERT INTO orders (OrderNumber, name_of_restaurant, Total_price, order_list_number, order_address) VALUES (?, ?, ?, ?, ?)";

        try {
        	Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, orderNumber);
            preparedStatement.setString(2, restaurantName);
            preparedStatement.setDouble(3, totalPrice);
            preparedStatement.setInt(4, orderListNumber);
            preparedStatement.setString(5, orderAddress);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Order inserted successfully. Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            System.out.println("Failed to insert the order.");
            e.printStackTrace();
        }
    }

	public void showOrders() {
        String query = "SELECT * FROM orders";

        try {
        	Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println(rs);
            /*while (rs.next()) {
                // Assuming your_table has columns: id, name, and value
                int ordernum = rs.getInt("ordernumber");
                String name = rs.getString("name_of_restaurant");
                double price = rs.getDouble("Total_price");
                int order_lst = rs.getInt("order_list_number");
                String order_add = rs.getString("order_address");

                // Print the results
                System.out.println("ID: " + ordernum + ", Name: " + name + ", price: " + price + ", list num: " + order_lst + ", order_add: " + order_add);}
        */} catch (SQLException e) {
            System.out.println("Failed to insert the order.");
            e.printStackTrace();
        }
    }
//    public void getData() {
//    	try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)
//    			{
//    				
//    			}
//    }
}