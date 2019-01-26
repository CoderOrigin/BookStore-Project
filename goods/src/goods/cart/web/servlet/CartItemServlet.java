package goods.cart.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import goods.book.domain.Book;
import goods.cart.domain.CartItem;
import goods.cart.service.CartItemService;
import goods.user.domain.User;

/**
 * Servlet implementation class CartItemServlet
 */
@WebServlet("/CartItemServlet")
public class CartItemServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    private CartItemService cartService = new CartItemService();
	
    /**
     * 返回用户购物车信息
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
	public String myCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 需要从session中获取user数据，根据uid查询购物车，并返回List<CartItem>
		 */
		User user = (User)request.getSession().getAttribute("usersession");
		List<CartItem> cartItemList = cartService.myCart(user.getUid());
		request.getSession().setAttribute("cartItemList", cartItemList);
		return "f:/jsps/cart/list.jsp";
	}
	/**
	 * 添加到购物车
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 从desc的我要购买转来，需要有book的bid，user的uid，在session中
		 * 还有页面上的quantity，封装为CartItem，调用service层的addCartItem方法
		 * 还要重新计算购物车，因此要调用myCart，转发到list
		 */
		CartItem cartItem = new CartItem();
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		User owner = (User) req.getSession().getAttribute("usersession");
		Book book = new Book();
		book.setBid(req.getParameter("bid"));
		cartItem.setQuantity(quantity);
		cartItem.setBook(book);
		cartItem.setOwner(owner);
		cartService.addCartItem(cartItem);
		return myCart(req,resp);
	}
	/**
	 * 删除或批量删除购物车
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String batchDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 接受的是AD141414,124AFA123,121414,....
		 * 这样一个带逗号的字符串作为cartItemIds参数，需要调用service层方法删除
		 * 然后返回myCart
		 */
		String cartItemIds = req.getParameter("cartItemIds");
		cartService.batchDelete(cartItemIds);
		return myCart(req,resp);
		
	}

	/**
	 * ajax异步请求更新数量和小计，返回的是json对象
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxUpdateQuantity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * request的参数有 cartItemId，quantity
		 * 需要调用service层更新，返回cartItem
		 * 获取新的 quantity，subtotal返回
		 * 因为是ajax异步请求，所以return null
		 */
		CartItem cartItem = cartService.updateQuantity((String)req.getParameter("cartItemId"), 
				Integer.parseInt(req.getParameter("quantity")));
		//需要返回一个json对象！
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		sb.append(",");
		sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
		sb.append("}");
		resp.getWriter().print(sb);
		return null;
	}
	/**
	 * 加载被选中的购物车信息
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String loadCartItems(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 根据ids查找items，保存到resp中
		 * 返回showitem.jsp
		 */
		String cartItemIds = req.getParameter("cartItemIds");
		List<CartItem> cartItems = cartService.loadCartItems(cartItemIds);
		req.setAttribute("cartItemList", cartItems);
		req.setAttribute("cartItemIds", cartItemIds);
		return "f:/jsps/cart/showitem.jsp";
	}
}
