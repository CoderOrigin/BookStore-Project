package goods.book.dao;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import goods.book.domain.Book;
import goods.pager.Expression;
import goods.pager.PageBean;

class BookDaoTest {

	
	@Test
	void testFind() throws IOException, SQLException {
		BookDao dao = new BookDao();
		//exprList.add(expr3);
		Book criteria = new Book();
		criteria.setBname("Spring");
		criteria.setAuthor("Walls");
		criteria.setPress("邮电");
		PageBean<Book> re = dao.findByCriteria(criteria, 1);
		assertEquals(null,re);
	}

}
