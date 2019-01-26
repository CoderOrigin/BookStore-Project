package goods.order.service;

import java.sql.SQLException;

import cn.itcast.jdbc.JdbcUtils;
import goods.order.dao.OrderDao;
import goods.order.domain.Order;
import goods.pager.PageBean;

public class OrderService {
	private OrderDao orderDao = new OrderDao();
	
	public void addOrder(Order order) {
		try {
			JdbcUtils.beginTransaction();
			orderDao.addOrder(order);
			JdbcUtils.commitTransaction();
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	/**
	 * 查看我的订单
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Order> myOrders(String uid, int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb =  orderDao.findByUser(uid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}

}
