package goods.category.domain;

import java.util.List;
/**
 * 分类模块的实体类，双向自身关联
 * @author admin
 *
 */
public class Category {
	private String cid;//主键
	private String cname;
	private Category parent;//父分类的对象
	private String desc;
	private List<Category> children; //子分类表
	
	public Category() {
		super();
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<Category> getChildren() {
		return children;
	}
	public void setChildren(List<Category> children) {
		this.children = children;
	}
	
	
	
}
