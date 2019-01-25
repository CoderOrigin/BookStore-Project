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
	
	public String myCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 需要从session中获取user数据，根据uid查询购物车，并返回List<CartItem>
		 */
		User user = (User)request.getSession().getAttribute("usersession");
		List<CartItem> cartItemList = cartService.myCart(user.getUid());
		request.getSession().setAttribute("cartItemList", cartItemList);
		return "f:/jsps/cart/list.jsp";
	}
	
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

}
