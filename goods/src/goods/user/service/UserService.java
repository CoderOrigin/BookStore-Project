package goods.user.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import goods.user.dao.UserDao;
import goods.user.domain.User;
import goods.user.domain.Exception.UserException;

public class UserService {
	private UserDao dao = new UserDao();
	
	//ajax用户名校验
	public boolean ajaxValidateLoginname(String loginname) {
		try {
			return dao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	//ajax邮箱校验
	public boolean ajaxValidateEmail(String email) {
		try {
			return dao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 注册，并发送激活邮件
	 * @param user
	 */
	public void regist(User user) {
		//数据补全
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());
		
		//发邮件
		//加载配置文件
		Properties pro = new Properties();
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("email_template.properties");
		try {
			pro.load(inStream);
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		//登录服务器得到session
		String host = pro.getProperty("host");//主机名
		String username = pro.getProperty("username");//登录名
		String password = pro.getProperty("password");//登录密码
		Session session = MailUtils.createSession(host, username, password);
		//创建mail对象
		String from = pro.getProperty("from");
		String to = user.getEmail();//debug了好久，错误在还加了getProperty。。。
		String subject = pro.getProperty("subject");
		String content = MessageFormat.format(pro.getProperty("content"), user.getActivationCode());
		Mail mail = new Mail(from, to, subject, content);
		//发送邮件
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		//测试的时候，发送邮件失败了，但是用户已经被添加进去了，因此先发邮件，再插入数据
		// 向数据库插入数据
		try {
			dao.add(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void activation(String activationCode) throws UserException {
		
		try {
			User user = dao.findByActivationCode(activationCode);
			if(user == null) throw new UserException("你这激活码哪来的？数据库中没记录呀");
			if(user.isStatus()) throw new UserException("你激活过了，怎么又来了");
			dao.updateStatus(user, true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	
	}
	
	/**
	 * 接受UserServlet封装好的User，调用UserDao的查找函数
	 * 如果用户名不存在，则抛出异常，如果密码不对抛出异常
	 * @param name, pass
	 * @throws SQLException 
	 * @throws UserException 
	 */
	public User login(String name, String pass) throws SQLException, UserException {
		User user = dao.findByNameAndPass(name, pass);
		if(user == null) throw new UserException("用户名或密码错误！");
		if(!user.isStatus())  throw new UserException("用户尚未激活！");
		return user;
	}
	
	
	
}
