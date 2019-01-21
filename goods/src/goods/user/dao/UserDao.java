package goods.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.jdbc.*;
import goods.user.domain.User;

public class UserDao {
	
	/**
	 * 
	 * @param name
	 * @param pass
	 * @return
	 * @throws SQLException 
	 */
	public User findByNameAndPass(String name, String pass) throws SQLException {
		//建立数据库连接，并获得QueryRuner
		Connection conn = JdbcUtils.getConnection();
		TxQueryRunner qr = new TxQueryRunner();
		//设置sql语句和参数
		String sql = "SELECT * FROM t_user WHERE loginname=? && loginpass=?";
		Object[] params = {name, pass};
		
		User user = qr.query(conn, sql, new BeanHandler<User>(User.class), params);
		return user;
	}
}
