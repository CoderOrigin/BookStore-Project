package goods.book.service;

import goods.book.dao.BookDao;
import goods.book.domain.Book;
import goods.pager.PageBean;

public class BookService {
	private BookDao bookDao = new BookDao();
	
	/**
	 * 通过分类查询，调用Dao层方法
	 * @param cid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCategory(String cid, int pc){
		try {
			return bookDao.findByCategory(cid, pc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过名字模糊查询，调用Dao层方法
	 * @param bname
	 * @param pc
	 * @return
	 */
	 
	public PageBean<Book> findByBname(String bname, int pc){
		try {
			return bookDao.findByBname(bname, pc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过作者模糊查询，调用Dao层方法
	 * @param author
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByAuthor(String author, int pc){
		try {
			return bookDao.findByAuthor(author, pc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过出版社查询，调用Dao层方法
	 * @param press
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByPress(String press, int pc){
		try {
			return bookDao.findByPress(press, pc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过多条件查询，调用Dao层方法
	 * @param criteria
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCriteria(Book criteria, int pc){
		try {
			return bookDao.findByCriteria(criteria, pc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
