package goods.user.web.servlet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import goods.user.domain.User;
import goods.user.domain.Exception.UserException;
import goods.user.service.UserService;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    private UserService userService = new UserService();  
	
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, SQLException {
		/*
		 * 1.封装表单数据到User
		 * 2.校验表单数据
		 * 3.把登录工作给Service返回User对象
		 * 4.如果用户存在，判断是否激活
		 * 5.如果用户不存在，保存错误信息，进行回显
		 */
		
		//封装表单数据
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		//创建一个保存error的map，key为出错的地方，值为为何出错
		//应该有四处，没找到：用户名或密码错误；用户名为空，密码为空，验证码不对
		Map<String, String> errors = new HashMap<String, String>();
		
		String username = form.getLoginname();
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空！");
		}
		String password = form.getLoginpass();
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空");
		}
		
		//如果有错误，就返回到了登录页面，并保存错误信息和表单数据进行回显
		if (errors.size() > 0) {
			request.setAttribute("user", form);
			request.setAttribute("errors", errors);
			return "f:/jsps/user/login.jsp";
		}
		
		try {
			User user = userService.login(form.getLoginname(), form.getLoginpass());
			//如果进行到这，说明成功登录了
			
			request.getSession().setAttribute("usersession", user);
			
			return "r:/index.jsp";
		}catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("user", form);
			return "f:/jsps/user/login.jsp";
		}
		
	}
	
	public String quit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//清除session，并返回到主页
		request.getSession().invalidate();
		return "r:/index.jsp";
	}
}
