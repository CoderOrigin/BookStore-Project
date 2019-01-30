package goods.order.web.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import goods.cart.domain.CartItem;
import goods.cart.service.CartItemService;
import goods.order.domain.Order;
import goods.order.domain.OrderItem;
import goods.order.service.OrderService;
import goods.pager.PageBean;
import goods.user.domain.User;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private OrderService orderService = new OrderService();
	private CartItemService cartItemService = new CartItemService(); 

	/**
	 * 获取当前页码
	 * 
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if (param != null && !param.trim().isEmpty()) {// 判断有pc参数，或者pc的空格去掉还有参数
			try {
				pc = Integer.parseInt(param);// 有可能出现的是abc会抛出错误，变成一个RuntimeException
			} catch (RuntimeException e) {
			}
		}
		return pc;
	}

	/**
	 * 获取没有pc参数的url，pc要根据请求的页码变化
	 * 
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req) {
		/*
		 * 如果localhost:8080/goods/BookServlet?method=findByCategory&cid=xxxx&pc=2
		 * getRequestURI 获取的是 /goods/BookServlet getQueryString 获取的是
		 * method=findByCategory&cid=xxxx&pc=2
		 */
		String url = req.getRequestURI() + "?" + req.getQueryString();
		int index = url.lastIndexOf("&pc");
		if (index != -1) {// 存在pc参数 需要截取掉
			url = url.substring(0, index);
		}
		return url;
	}
	/**
	 * 查看自己的订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * pageBean：pc,tr,ps,url,list 从session中获取user,和pc如果没有pc则默认为1
		 * 调用service层的myOrder方法 要设置pageBean到session中
		 */
		int pc = getPc(req);
		String url = getUrl(req);
		User user = (User) req.getSession().getAttribute("usersession");
		PageBean<Order> pb = orderService.myOrders(user.getUid(), pc);
		pb.setUrl(url);
		req.getSession().setAttribute("pb", pb);
		return "/jsps/order/list.jsp";
	}
	
	public String createOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 功能分析：
		 * 创建Order对象
		 * 1.uid：uuid生成
		 * 2.ordertime：获取当前时间
		 * 3.total：可以从cartItem获取
		 * 4.status：默认生成为1
		 * 5.address：表单获得
		 * 6.User：从session获得
		 * 7.OrderItemList:
		 * 		创建OrderItem对象，从cartItem转成OrderItem
		 * 调用service完成添加
		 * 保存，转发到success页面
		 */
		//处理post请求的encoding
		req.setCharacterEncoding("utf-8");
		//得到CartItem
		String cartItemIds = req.getParameter("cartItemIds");
		List<CartItem> cartItems = cartItemService.loadCartItems(cartItemIds);
		//创建Order对象
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(String.format("%tF %<tT", new Date()));
		order.setStatus(1);
		order.setAddress(req.getParameter("address"));
		User owner = (User)req.getSession().getAttribute("usersession");
		order.setOwner(owner);
		
		//获取total
		BigDecimal total = new BigDecimal("0");
		for(CartItem cartItem: cartItems) {
			total = total.add(new BigDecimal(cartItem.getSubtotal()+ ""));
		}
		order.setTotal(total.doubleValue());
		
		//创建orderItemList
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		
		for(CartItem cartItem: cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(CommonUtils.uuid());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setBook(cartItem.getBook());
			orderItem.setOrder(order);
			orderItems.add(orderItem);
		}
		order.setOrderItems(orderItems);
		
		//添加和删除放一起，如果出现差错，都回滚
		orderService.addOrder(order, cartItemIds);
		
//		//还要先删除购物车条目，这里有问题，如果回滚 购物车信息还是被删除了
//		cartItemService.batchDelete(cartItemIds);
		req.setAttribute("order", order);
		return "/jsps/order/ordersucc.jsp";
	}
	

	public String desc(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 要根据bid查找详细信息，还要有btn参数，确定是取消还是确认收货
		 * 
		 * 
		 */
		String oid = req.getParameter("oid");
		Order order = orderService.loadOrder(oid);
		req.setAttribute("order", order);
		req.setAttribute("btn", req.getParameter("btn"));
		
		return "f:/jsps/order/desc.jsp";
	}

}
