package goods.user.domain;

/*
 * 1.对应数据库的t_user表的所有列
 * 2.对用用户功能模块的所有表单，因为比较简单所以可以在一个User中
 * 如果功能过多，需要在没有功能都重建利益实体类
 */
public class User {
	private String uid;//主键
	private String loginname;//登录名
	private String loginpass;//登录密码
	private String email;//邮箱
	private boolean status;//激活状态
	private String activationCode;//激活码，用UUID生成，唯一确定
	//注册功能表单和修改密码表单
	private String reloginpass;
	private String verifyCode;
	private String newloginpass;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	public String getReloginpass() {
		return reloginpass;
	}
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getNewloginpass() {
		return newloginpass;
	}
	public void setNewloginpass(String newloginpass) {
		this.newloginpass = newloginpass;
	}
	
	
}
