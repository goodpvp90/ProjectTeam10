package common;

public class Order {
	private String name_of_restaurant;
	private String order_address;
	private int order_list_number;
	private int order_number;
	private double total_price;
	
	public Order(String name_of_restaurant, String order_address, int order_list_number, int order_number,
			double total_price) {
		this.name_of_restaurant = name_of_restaurant;
		this.order_address = order_address;
		this.order_list_number = order_list_number;
		this.order_number = order_number;
		this.total_price = total_price;
	}
	public double getTotal_price() {
		return total_price;
	}
	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}
	public int getOrder_number() {
		return order_number;
	}
	public void setOrder_number(int order_number) {
		this.order_number = order_number;
	}
	public int getOrder_list_number() {
		return order_list_number;
	}
	public void setOrder_list_number(int order_list_number) {
		this.order_list_number = order_list_number;
	}
	public String getOrder_address() {
		return order_address;
	}
	public void setOrder_address(String order_address) {
		this.order_address = order_address;
	}
	public String getName_of_restaurant() {
		return name_of_restaurant;
	}
	public void setName_of_restaurant(String name_of_restaurant) {
		this.name_of_restaurant = name_of_restaurant;
	}
	

	
	
}
