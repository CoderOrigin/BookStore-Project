package goods.user.web.servlet;

import java.io.IOException;
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
	
	//ajax校验用户名
    public String ajaxValidateLoginname(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//获取用户名
    	String loginname = request.getParameter("loginname");
    	//获取校验是否成功
    	boolean f = userService.ajaxValidateLoginname(loginname);
    	//返回校验信息
    	response.getWriter().print(f);
    	return null;
    }
    
    //ajax校验邮箱
    public String ajaxValidateEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//获取用户名
    	String email = request.getParameter("email");
    	//获取校验是否成功
    	boolean b = userService.ajaxValidateEmail(email);
    	//返回校验信息
    	response.getWriter().print(b);
    	return null;
    }
    
    //ajax校验验证码
    public String ajaxValidateVerifyCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/*
    	 * 对比Session中保留的vCode就行
    	 */
    	String text = (String)request.getSession().getAttribute("vCode");
    	String verifyCode = request.getParameter("verifyCode");
    	boolean b =  text.toLowerCase().equals(verifyCode.toLowerCase());
        response.getWriter().print(b);
    	return null;
    }
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
		System.out.println();
		//创建一个保存error的map，key为出错的地方，值为为何出错
		//应该有四处，没找到：用户名或密码错误；用户名为空，密码为空，验证码不对
		Map<String, String> errors = new HashMap<String, String>();
		
		String username = form.getLoginname();
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空！");
		}
		String password = form.getLoginpass();
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空！");
		}
		String vcode = request.getParameter("verifyCode").toLowerCase();
		String vcodetext = ((String)request.getSession().getAttribute("vCode")).toLowerCase();
		if (vcode == null || vcode.trim().isEmpty()) {
			errors.put("verifycode", "验证码不能为空！");
		}else if (!vcode.equals(vcodetext)) {
			errors.put("verifycode", "验证码错误！");
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
	
	public String regist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*
		 * 1.封装表单数据到User
		 * 3.把登录工作给Service返回User对象
		 * 4.如果用户存在，判断是否激活
		 * 5.如果用户不存在，保存错误信息，进行回显
		 */
		System.out.println("regist..");
		//User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		
		return "r:/index.jsp";
	}
}
