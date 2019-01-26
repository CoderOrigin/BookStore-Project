package goods.order.domain;

import java.util.List;

import goods.user.domain.User;

public class Order {
	private String oid;//		oid	char(32)	主键
	private String ordertime;//	ordertime	char(19)	下单时间
    private double total;//		total	decimal(10,2)	合计金额
	private int status;//		status	int	订单状态：1 未付款、2 未发货、3 未收货、4 交易成功、5 已取消
	private String address;//	address	varchar(1000)	收货地址
	private User owner;//		uid	char(32)	当前会员id
	private List<OrderItem> orderItems;
	
	public Order() {
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@Override
	public String toString() {
		return "Order [oid=" + oid + ", ordertime=" + ordertime + ", total=" + total + ", status=" + status
				+ ", address=" + address + ", owner=" + owner + ", orderItems=" + orderItems + "]";
	}

	

}
