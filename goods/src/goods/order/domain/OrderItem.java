package goods.order.domain;

import goods.book.domain.Book;

public class OrderItem {
  	private String orderItemId;	//	orderItemId	char(32)	主键
	private int quantity; 	//	quantity	int	数量
	private double subtotal;	//	subtotal	decimal(8,2)	金额小计
  	private Book book;	//	bid	char(32)	图书id
						//	bname	varchar(200)	图书名称
						//	currPrice	decimal(8,2)	当前价
						//	image_b	varchar(100)	小图路径
	private Order order;	//	oid	char(32)	所属订单id
	
	public OrderItem() {
	}

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "OrderItem [orderItemId=" + orderItemId + ", quantity=" + quantity + ", subtotal=" + subtotal + ", book="
				+ book+"]";
	}
	
	
}
