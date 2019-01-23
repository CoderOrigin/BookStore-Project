package goods.pager;

import java.util.List;

public class PageBean<T> {
	private int pc;//当前页码 pageCode
	private int tr;//总记录数 totalRecord
	private int ps;//每页记录数 PageSize
	private String url;//请求的路径，例如/BaseServlet?method=findXXX&cid=XX&cnam=XXX
	private List<T> beanList;//要显示的东西
	public PageBean() {
		super();
	}
	
	// 总页数 totalPage
	public int getTp() {
		int tp = tr / ps; 
		return tr%ps==0?tp:tp+1; 
	}
	public int getPc() {
		return pc;
	}
	public void setPc(int pc) {
		this.pc = pc;
	}
	public int getTr() {
		return tr;
	}
	public void setTr(int tr) {
		this.tr = tr;
	}
	public int getPs() {
		return ps;
	}
	public void setPs(int ps) {
		this.ps = ps;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<T> getBeanList() {
		return beanList;
	}
	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;
	}

	@Override
	public String toString() {
		return "PageBean [pc=" + pc + ", tr=" + tr + ", ps=" + ps + ", url=" + url + ", beanList=" + beanList + "]";
	}
	
	

}
