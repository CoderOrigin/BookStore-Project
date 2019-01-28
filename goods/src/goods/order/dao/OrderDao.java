package goods.order.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import goods.book.domain.Book;
import goods.order.domain.Order;
import goods.order.domain.OrderItem;
import goods.pager.Expression;
import goods.pager.PageBean;

public class OrderDao {
	private TxQueryRunner qr = new TxQueryRunner();
	/**
	 * 通过用户的uid查询订单
	 * @param uid
	 * @param pc
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public PageBean<Order> findByUser(String uid, int pc) throws IOException, SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("uid", "=", uid));
		return findByCriteria(exprList, pc);
	}
	/**
	 * 通用的查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	private PageBean<Order> findByCriteria(List<Expression> exprList, int pc) throws IOException, SQLException{
		/*
		 * 1.得到ps, 通过配置文件
		 * 2.得到tr,总记录数
		 * 3.通过搜索，得到beanList
		 * 4.返回pageBean
		 */
		//1.
		Properties prop = new Properties();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("pageConfig.properties");
		prop.load(in);
		int ps = Integer.parseInt((String)prop.get("orderps"));
		
		//2.得到tr,总记录数

		// 2.1得到where语句
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder(" WHERE 1=1");
		for (Expression expr : exprList) {
			sb.append(" AND ").append(expr.getName()).append(" ").append(expr.getOperator());
			if (!expr.getOperator().equalsIgnoreCase("is null")) {
				sb.append(" ?");
				params.add(expr.getValue());
			}
		}
		//2.2得到tr
		String sql = "SELECT count(*) FROM t_order" + sb;
		Number number = (Number)qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();
		
		
		//3.得到beanList
		sql = "SELECT * FROM t_order" + sb + " ORDER BY ordertime desc LIMIT ?,?";
		params.add(ps*(pc-1));
		params.add(ps);
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(), params.toArray());
		List<Order> orderList = new ArrayList<Order>();
		for(Map<String,Object>map: mapList) {
			//这里得到的 order 没有user，没有itemlist
			Order order = CommonUtils.toBean(map, Order.class);
			//加载 itemlist
			order.setOrderItems(loadOrderItemList(order.getOid()));
			orderList.add(order);
		}
		//4.封装并返回
		PageBean<Order> pageBean = new PageBean<Order>();
		pageBean.setPc(pc);
		pageBean.setPs(ps);
		pageBean.setTr(tr);
		pageBean.setBeanList(orderList);
		return pageBean;
	}
	
	//需要先查orderItem表，需要加载item
	private List<OrderItem> loadOrderItemList(String oid) throws SQLException{
		List<OrderItem> oderItemList = new ArrayList<OrderItem>();
		String sql = "SELECT * FROM t_orderitem WHERE oid =?";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(), oid);
		for(Map<String,Object> map: mapList) {
			OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
			Book book =CommonUtils.toBean(map, Book.class);
			orderItem.setBook(book);
			oderItemList.add(orderItem);
		}
		return oderItemList;
	}
	
	//添加订单
	public void addOrder(Order order) throws SQLException {
		String sql = "INSERT INTO t_order VALUES(?,?,?,?,?,?)";
		Object[] params = {order.getOid(), order.getOrdertime(), order.getTotal(),
				order.getStatus(), order.getAddress(), order.getOwner().getUid()};
		qr.update(sql, params);
		//插如item表
		sql = "INSERT INTO t_orderitem VALUES(?,?,?,?,?,?,?,?)";
		
		int len = order.getOrderItems().size();
		Object[][] paramss = new Object[len][];
		for(int i=0; i<len;i++) {
			OrderItem oi = order.getOrderItems().get(i);	
			paramss[i] =new Object[]{oi.getOrderItemId(), oi.getQuantity(), oi.getSubtotal(),oi.getBook().getBid(),
					oi.getBook().getBname(), oi.getBook().getCurrPrice(), oi.getBook().getImage_b(),
					oi.getOrder().getOid()};
		}
		qr.batch(sql, paramss);
	}
	
}
