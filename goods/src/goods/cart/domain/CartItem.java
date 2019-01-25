package goods.cart.domain;

import java.math.BigDecimal;

import goods.book.domain.Book;
import goods.user.domain.User;

public class CartItem {
	private String cartItemId;//主键
	private int quantity;//数量
	private Book book;//书
	private User owner;//购物车的所有者

	//单价乘以数量，返回小计
	public double getSubtotal() {
		BigDecimal v1 = new BigDecimal(Double.toString(book.getCurrPrice()));
		BigDecimal v2 = new BigDecimal(Double.toString(quantity));
		return v1.multiply(v2).doubleValue();
	}
	
	public CartItem() {
		// TODO Auto-generated constructor stub
	}

	public String getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "CartItem [cartItemId=" + cartItemId + ", quantity=" + quantity + ", book=" + book + ", owner=" + owner
				+ "]";
	}

}
