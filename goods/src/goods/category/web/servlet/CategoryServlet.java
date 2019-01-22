package goods.category.web.servlet;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import goods.category.domain.Category;
import goods.category.service.CategoryService;

/**
 * 分类模块的WEB层
 */
@WebServlet("/CategoryServlet")
public class CategoryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private CategoryService categoryService = new CategoryService(); 
	
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		/*
		 * 调用Service层的findAll方法，返回一个list，并将该list保存在request中
		 */
		List<Category> parents = categoryService.findAll();
		request.setAttribute("parents", parents);
		return "f:/jsps/left.jsp";
	}
}
