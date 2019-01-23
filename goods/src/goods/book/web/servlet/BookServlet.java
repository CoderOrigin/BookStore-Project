package goods.book.web.servlet;

import javax.servlet.annotation.WebServlet;

import cn.itcast.servlet.BaseServlet;
import goods.book.service.BookService;

/**
 * Servlet implementation class BookServlet
 */
@WebServlet("/BookServlet")
public class BookServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private BookService bookService = new BookService();

}
