package goods.book.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import goods.book.domain.Book;
import goods.book.service.BookService;
import goods.pager.PageBean;

/**
 * Servlet implementation class BookServlet
 */
@WebServlet("/BookServlet")
public class BookServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private BookService bookService = new BookService();

	/**
	 * 获取当前页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if(param != null && !param.trim().isEmpty()) {//判断有pc参数，或者pc的空格去掉还有参数
			try {
				pc = Integer.parseInt(param);//有可能出现的是abc会抛出错误，变成一个RuntimeException
			} catch (RuntimeException e) {}
		}
		return pc;
	}
	/**
	 * 获取没有pc参数的url，pc要根据请求的页码变化
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req) {
		/*
		 * 如果localhost:8080/goods/BookServlet?method=findByCategory&cid=xxxx&pc=2
		 * getRequestURI 获取的是  /goods/BookServlet
		 * getQueryString 获取的是 method=findByCategory&cid=xxxx&pc=2
		 */
		String url = req.getRequestURI() + "?" + req.getQueryString();
		int index = url.lastIndexOf("&pc");
		if(index != -1) {//存在pc参数 需要截取掉
			url = url.substring(0,index);
		}
		return url;
	}
	/**
	 * 按分类进行查找
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1.得到pc，如果没有，则默认为1
		 * 2.获取url
		 * 3.获取查询条件，该方法为cid
		 * 4.调用service的查询函数，得到pageBean
		 * 5.给pageBean设置url，保存pageBean，转发到/jsps/book/list.jsp
		 */
		int pc = getPc(req);
		String url = getUrl(req);
		String cid = req.getParameter("cid");
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 通过作者查询。并列出所有结果
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByAuthor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1.得到pc，如果没有，则默认为1
		 * 2.获取url
		 * 3.获取查询条件，该方法为cid
		 * 4.调用service的查询函数，得到pageBean
		 * 5.给pageBean设置url，保存pageBean，转发到/jsps/book/list.jsp
		 */
		int pc = getPc(req);
		String url = getUrl(req);
		String author = req.getParameter("author");
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	public String findByPress(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1.得到pc，如果没有，则默认为1
		 * 2.获取url
		 * 3.获取查询条件，该方法为cid
		 * 4.调用service的查询函数，得到pageBean
		 * 5.给pageBean设置url，保存pageBean，转发到/jsps/book/list.jsp
		 */
		int pc = getPc(req);
		String url = getUrl(req);
		String press = req.getParameter("press");
		PageBean<Book> pb = bookService.findByPress(press, pc);
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	public String load (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
	
}
