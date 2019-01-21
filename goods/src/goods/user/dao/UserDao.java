package goods.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;
import goods.user.domain.User;

public class UserDao {
	private TxQueryRunner qr = new TxQueryRunner();
	
	public boolean ajaxValidateLoginname(String loginname) throws SQLException {
		String sql  = "SELECT count(1) FROM t_user WHERE loginname=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(), loginname);
		//如果int=0，说明没有找到，校验成功，返回true
		return number.intValue() == 0;
	}
	
	public boolean ajaxValidateEmail(String email) throws SQLException {
		String sql  = "SELECT count(1) FROM t_user WHERE email=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(), email);
		//如果int=0，说明没有找到，校验成功，返回true
		return number.intValue() == 0;
	}
	
	public void add(User user) throws SQLException {
		String sql = "INSERT t_user VALUES(?,?,?,?,?,?)";
		Object[] params = {user.getUid(), user.getLoginname(), user.getLoginpass(), user.getEmail(), user.isStatus(), user.getActivationCode()};
		qr.update(sql, params);
	}
	/**
	 * 通过用户名和密码查询用户，返回的User如果不为空则找到用户，如果为null，则登录失败
	 * @param name
	 * @param pass
	 * @return
	 * @throws SQLException 
	 */
	public User findByNameAndPass(String name, String pass) throws SQLException {
		//建立数据库连接，并获得QueryRuner...一句代码都被封装起来了
		//设置sql语句和参数
		String sql = "SELECT * FROM t_user WHERE loginname=? && loginpass=?";
		Object[] params = {name, pass};
		User user = qr.query(sql, new BeanHandler<User>(User.class), params);
		return user;
	}
	
	public int findByName(String name) throws SQLException {
		// 设置sql语句和参数
		String sql = "SELECT * FROM t_user WHERE loginname=?";
		User user = qr.query(sql, new BeanHandler<User>(User.class), name);
		return (user != null) ? 1 : 0;
	}
	
	public int findByEmail(String email) throws SQLException {
		// 设置sql语句和参数
		String sql = "SELECT * FROM t_user WHERE email=?";
		User user = qr.query(sql, new BeanHandler<User>(User.class), email);
		return (user != null) ? 1 : 0;
	}
}
