package goods.user.service;

import java.sql.SQLException;

import goods.user.dao.UserDao;
import goods.user.domain.User;
import goods.user.domain.Exception.UserException;

public class UserService {
	private UserDao dao = new UserDao();
		
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
	
	public void regist(User form) {
		
	}
}
