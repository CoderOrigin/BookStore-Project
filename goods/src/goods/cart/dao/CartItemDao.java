package goods.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import goods.book.domain.Book;
import goods.cart.domain.CartItem;
import goods.user.domain.User;

public class CartItemDao {
	private TxQueryRunner qr = new TxQueryRunner();
	
	//需要将bid和uid分别对应到Book类和User类
	private CartItem toCartItem(Map<String,Object> map) {
		if(map==null || map.size()==0) return null;
		CartItem item = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User owner = CommonUtils.toBean(map, User.class);		
		item.setBook(book);
		item.setOwner(owner);
		return item;
	}
	
	private List<CartItem> toCartItemList(List<Map<String,Object>> mapList){
		List<CartItem> itemList = new ArrayList<CartItem>();
		for(Map<String,Object> map: mapList) {
			itemList.add(toCartItem(map));
		}
		return itemList;
	}
	/**
	 * 通过用户查找用户的购物车信息
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> findByUser(String uid) throws SQLException{
		//因为要显示书的信息，因为必须把书的各种信息也查到
		String sql = "select * from t_cartItem c, t_book b where c.bid=b.bid and uid=? order by c.orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), uid);
		return toCartItemList(mapList);
	} 
	/**
	 * 通过uid和bid查找并返回CartItem
	 * @param uid
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByUidAndBid(String uid, String bid) throws SQLException {
		//返回的购物车信息不需要详细信息调用
		String sql = "SELECT * FROM t_cartItem WHERE uid=? AND bid=?";
		Object[] params = {uid, bid};
		Map<String,Object> map = qr.query(sql, new MapHandler(), params);
		return toCartItem(map);
	}
	/**
	 * 更新购物车数量信息
	 * @param cartItemId
	 * @param quantity
	 * @throws SQLException
	 */
	public void updateQuantity(String cartItemId, int quantity) throws SQLException {
		String sql = "Update t_cartItem set quantity=? Where cartItemId=?";
		Object[] params = {quantity, cartItemId};
		qr.update(sql, params);
	}
	/**
	 * 通过id查询购物车信息
	 * @param cartItemId
	 * @return
	 * @throws SQLException
	 */
	public CartItem findById(String cartItemId) throws SQLException {
		//需要详细信息！
		String sql = "SELECT * From t_cartItem c,t_book b Where c.bid=b.bid AND cartItemId=? order by c.orderBy";
		Map<String,Object> map =  qr.query(sql, new MapHandler(), cartItemId);
		return toCartItem(map);
	}
	/**
	 * 添加新的cartItem
	 * @param cartItem
	 * @throws SQLException
	 */
	public void addCartItem(CartItem cartItem) throws SQLException {
		String sql = "INSERT INTO t_cartItem(cartItemId, quantity, bid, uid) VALUES(?,?,?,?)";
		String cartItemId = cartItem.getCartItemId();
		int quantity = cartItem.getQuantity();
		String bid = cartItem.getBook().getBid();
		String uid = cartItem.getOwner().getUid();
		Object[] params = {cartItemId, quantity, bid, uid};
		qr.update(sql, params);
	}
	/**
	 * 根据一个带逗号的字符串，查询cartItem，并删除
	 * @param cartItemIds
	 * @throws SQLException
	 */
	public void batchDelete(String cartItemIds) throws SQLException {
		Object[] params = cartItemIds.split(",");
		String sql = "DELETE FROM t_cartItem WHERE" + toWhereSql(params.length);
		qr.update(sql, params);
	}
	/**
	 * 加载被选中的购物车信息，生成订单，所以信息要重新查询
	 * @param cartItemIds
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> loadCartItems(String cartItemIds) throws SQLException{
		//用于加载被选中的购物车信息，生成订单，所以信息要重新查询
		Object[] params = cartItemIds.split(",");
		String sql = "SELECT * FROM t_cartItem c, t_book b Where c.bid=b.bid AND" + toWhereSql(params.length) + " order by c.orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), params);
		return toCartItemList(mapList);
	}
	/**
	 * 生成 cartItemId 需要不确定参数个数的 不含WHERE关键字的 where语句
	 * @param len
	 * @return
	 */
	private String toWhereSql(int len) {
		StringBuilder whereSql  = new StringBuilder(" cartItemId in (");
		for(int i=0;i<len;i++) {
			whereSql.append("?");
			if(i<len-1) {
				whereSql.append(",");
			}
		}
		whereSql.append(")");
		return whereSql.toString();
	}
}
