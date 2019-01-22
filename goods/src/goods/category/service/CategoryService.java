package goods.category.service;

import java.sql.SQLException;
import java.util.List;

import goods.category.dao.CategoryDao;
import goods.category.domain.Category;
/**
 * 分类模块的Service层
 * @author admin
 *
 */
public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();

	public List<Category> findAll() {
		
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
