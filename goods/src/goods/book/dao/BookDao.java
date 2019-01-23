package goods.book.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;
import goods.book.domain.Book;
import goods.pager.Expression;
import goods.pager.PageBean;

public class BookDao {
	private TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * 通过类别ID查询
	 * @param cid
	 * @param pc
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public PageBean<Book> findByCategory(String cid, int pc) throws IOException, SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("cid", "=", cid));
		return findByCriteria(exprList, pc);
	}
	/**
	 * 通过名字模糊查询
	 * @param bname
	 * @param pc
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public PageBean<Book> findByBname(String bname, int pc) throws IOException, SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%"+bname+"%"));
		return findByCriteria(exprList, pc);
	}
	/**
	 * 通过出版社模糊查询
	 * @param press
	 * @param pc
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public PageBean<Book> findByPress(String press, int pc) throws IOException, SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("press", "like", "%"+press+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 通过作者模糊查询
	 * @param press
	 * @param pc
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public PageBean<Book> findByAuthor(String author, int pc) throws IOException, SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("author", "like", "%"+author+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 通过名字，作者，出版社组合查询
	 * @param press
	 * @param pc
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public PageBean<Book> findByCriteria(Book criteria, int pc) throws IOException, SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%"+criteria.getBname()+"%"));
		exprList.add(new Expression("author", "like", "%"+criteria.getAuthor()+"%"));
		exprList.add(new Expression("press", "like", "%"+criteria.getPress()+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 通用的查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	private PageBean<Book> findByCriteria(List<Expression> exprList, int pc) throws IOException, SQLException{
		/*
		 * 1.得到ps, 通过配置文件
		 * 2.得到tr,总记录数
		 * 3.通过搜索，得到beanList
		 * 4.返回pageBean
		 */
		//1.得到ps
		Properties pro = new Properties();
		pro.load(this.getClass().getClassLoader().getResourceAsStream("pageConfig.properties"));
		int ps = Integer.parseInt((String)pro.get("ps"));
		
		//2.得到tr,总记录数
		
		//2.1得到where语句
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder(" WHERE 1=1");
		for(Expression expr : exprList) {
			sb.append(" AND ").append(expr.getName()).append(" ")
						.append(expr.getOperator());
			if(!expr.getOperator().equalsIgnoreCase("is null")) {
				sb.append(" ?");
				params.add(expr.getValue());
			}	
		}
		//2.2得到tr
		String sql = "SELECT count(*) FROM t_book" +sb;
		Number number = (Number)qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();
		
		//3.得到beanList,需要用上limit a,b 从开始一共b个
		//b=ps, a= ps*(pc-1)
		sql = "SELECT * FROM t_book" + sb + " ORDER BY orderBy LIMIT ?,?";
		params.add(ps*(pc-1));
		params.add(ps);
		List<Book> bookList = qr.query(sql, new BeanListHandler<Book>(Book.class), params.toArray());
		
		//4.封装并返回
		PageBean<Book> pageBean = new PageBean<Book>();
		pageBean.setPc(pc);
		pageBean.setPs(ps);
		pageBean.setTr(tr);
		pageBean.setBeanList(bookList);
		return pageBean;
	}

}
