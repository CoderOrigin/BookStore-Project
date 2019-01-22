package goods.user.web.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    	boolean b = userService.ajaxValidateLoginname(loginname);
    	//返回校验信息
    	response.getWriter().print(b);
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
    	boolean b =  text.equalsIgnoreCase(verifyCode);
        response.getWriter().print(b);
    	return null;
    }
    public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, SQLException, UnsupportedEncodingException {
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
		//校验
		Map<String, String> errors = activateLogin(form, request.getSession());
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
			//保存cookie
			String loginname = user.getLoginname();
			loginname = URLEncoder.encode(loginname, "utf-8");
			Cookie cookie = new Cookie("loginname", loginname);
			cookie.setMaxAge(60 * 60 * 24 * 10); //保存十天
			response.addCookie(cookie);
			
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
		 * 1.封装表单数据到form
		 * 2.校验表单数据，为什么还要校验，因为前台很容易被人绕过去，后台还需要重新校验
		 * 		如果失败，保存错误信息，返回到regist.jsp
		 * 3.调用service层完成业务
		 * 4.成功，保存成功信息，到msg.jsp
		 */
		System.out.println("regist..");
		//封装
		User formUser = CommonUtils.toBean(request.getParameterMap(), User.class);
		//校验,如果有错误转发到注册页面
		Map<String,String> errors = activateRegist(formUser, request.getSession());
		if(errors.size() > 0) {
			request.setAttribute("form", formUser);
			request.setAttribute("errors", errors);
			return "f:/jsps/user/regist.jsp";
		}
		//regist
		userService.regist(formUser);
		//保存成功信息code:success|error，msg:String
		request.setAttribute("code", "success");
		request.setAttribute("msg", "注册成功，请到邮箱进行激活！");
		//信息保存在request中，必须是转发
		return "f:/jsps/msg.jsp";
	}
	
	private Map<String, String> activateRegist(User form, HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		
		String loginname = form.getLoginname();
		if (loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		}else if(loginname.length()< 3 ||loginname.length() >20) {
			errors.put("loginname", "用户名长度必须在3 ~ 20之间！");
		}else if(!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "用户名已经被注册！");
		}
		
		String loginpass = form.getLoginpass();
		if (loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		}else if(loginpass.length()< 3 ||loginpass.length() >20) {
			errors.put("loginpass", "密码长度必须在3 ~ 20之间！");
		}
		
		String reloginpass = form.getReloginpass();
		if (reloginpass == null || reloginpass.trim().isEmpty()) {
			errors.put("reloginpass", "确认密码不能为空！");
		}else if(!loginpass.equals(reloginpass)) {
			errors.put("reloginpass", "两次输入密码不一致！");
		}
		
		String email = form.getEmail();
		if (email== null || email.trim().isEmpty()) {
			errors.put("email", "邮箱不能为空！");
		}
		//java中用正则表达式，\需要转义
		else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "邮箱格式错误！");
		}else if(!userService.ajaxValidateEmail(email)) {
			errors.put("email", "该邮箱已经被注册！");
		}
		
		String verifyCode = form.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if (verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！后台");
		}
		
		return errors;
	}
	
	private Map<String, String> activateLogin(User form, HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		
		String username = form.getLoginname();
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空！");
		}
		String password = form.getLoginpass();
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空！");
		}
		String vcode = form.getVerifyCode();
		String vcodetext = (String)session.getAttribute("vCode");
		if (vcode == null || vcode.trim().isEmpty()) {
			errors.put("verifycode", "验证码不能为空！");
		}else if (!vcode.equalsIgnoreCase(vcodetext)) {
			errors.put("verifycode", "验证码错误！后台");
		}
		return errors;
	}
	
	public String activation(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取验证码
		String activationCode = request.getParameter("activationCode");
		//调用service层的activation方法
		try{
			userService.activation(activationCode);
			request.setAttribute("code", "success");
			request.setAttribute("msg", "竟然激活成功了！令人吃惊！");
		}catch (UserException e){
			request.setAttribute("code", "error");
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/msg.jsp";
	}
}
