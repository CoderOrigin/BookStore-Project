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

	/**
	 * 将Map转化为Category，需要处理的pid，找到对应的parent
	 * @param map
	 * @return
	 */
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
	/**
	 * 将map的List转化为Category的List
	 * 主要是调用toCategory方法，遍历
	 * @param mapList
	 * @return
	 */
	private List<Category> toCategoryList(List<Map<String, Object>> mapList){
		List<Category> categoryList = new ArrayList<Category>();
		for(Map<String, Object> map: mapList) {
			categoryList.add(toCategory(map));
		}
		return categoryList;
		
	}
	
	/**
	 * 通过父亲的id找所有的孩子，并返回List
	 * @param pid
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findByParent(String pid) throws SQLException{
		//查找父亲的sql语句
		String sql = "SELECT * FROM t_category WHERE pid=?";
		return toCategoryList(qr.query(sql, new MapListHandler(), pid));
	}
	/**
	 * 返回的是所有的一级分类的list,并将一级分类的Children保存下来
	 * @return
	 * @throws SQLException
	 */
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
