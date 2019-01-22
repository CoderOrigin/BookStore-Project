package goods.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import goods.category.domain.Category;
/**
 * 分类模块的Dao层
 * @author admin
 *
 */
public class CategoryDao {
	private TxQueryRunner qr = new TxQueryRunner();

	private Category toCategory(Map<String, Object> map) {
		//将cid,cname,desc,封装
		Category category = CommonUtils.toBean(map, Category.class);
		//封装parent
		String pid = (String)map.get("pid");
		if (pid != null) {
			Category parent = new Category();
			parent.setCid(pid);
			category.setParent(parent);
		}
		return category;
	}
	
	private List<Category> toCategoryList(List<Map<String, Object>> mapList){
		List<Category> categoryList = new ArrayList<Category>();
		for(Map<String, Object> map: mapList) {
			categoryList.add(toCategory(map));
		}
		return categoryList;
		
	}
	
	public List<Category> findByParent(String pid) throws SQLException{
		//查找父亲的sql语句
		String sql = "SELECT * FROM t_category WHERE pid=?";
		return toCategoryList(qr.query(sql, new MapListHandler(), pid));
	}
	
	public List<Category> findAll() throws SQLException {
		/*
		 * 1.查询出一级分类
		 * 2.将一级分类的的子分类保存
		 * 3.返回一级分类的list
		 */
		List<Category> categoryList = new ArrayList<Category>();
		String sql = "SELECT * FROM t_category WHERE pid is null";
		//用MapListHandler将结果封装为MapList
		//将MapList转化为List<Category>
		
		categoryList = toCategoryList(qr.query(sql, new MapListHandler()));
		for(Category category: categoryList) {
			//遍历一级分类从数据库中查找孩子并保存为一级分类的孩子
			List<Category> children = findByParent(category.getCid());
			category.setChildren(children);
		}
		return categoryList;
	} 
}
