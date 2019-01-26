package goods.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.commons.CommonUtils;
import goods.cart.dao.CartItemDao;
import goods.cart.domain.CartItem;

public class CartItemService {
	private CartItemDao cartDao = new CartItemDao();

	public List<CartItem> myCart(String uid) {
		try {
			return cartDao.findByUser(uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void addCartItem(CartItem cartItem) {
		/*
		 * 接受WEB层封装好的有book，有owner，有quantity的cartItem
		 * 现根据uid和bid查找是否有该购物车条目
		 * 如果返回了null，说明没有，调用add方法
		 * 如果有，调用updateQuantity方法
		 */
		String uid = cartItem.getOwner().getUid();
		String bid = cartItem.getBook().getBid();
		try {
			CartItem currCart = cartDao.findByUidAndBid(uid, bid);
			if(currCart == null) {
				String cartItemId = CommonUtils.uuid();
				cartItem.setCartItemId(cartItemId);
				cartDao.addCartItem(cartItem);
			}else {
				int quantity = currCart.getQuantity()+ cartItem.getQuantity();
				cartDao.updateQuantity(currCart.getCartItemId(), quantity);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 批量删除功能，直接调用的Dao层方法
	 * @param cartItemIds
	 */
	public void batchDelete(String cartItemIds) {
		try {
			cartDao.batchDelete(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 使用ID和quantity来更新某个购物车商品的信息，并返回新的购物车商品信息
	 * @param cartItemId
	 * @param quantity
	 * @return
	 */
	public CartItem updateQuantity(String cartItemId, int quantity) {
		/*
		 * 使用dao层的update，更新
		 * 再使用id查询，返回新的cartItem，因为不一定修改成功
		 */
		try {
			cartDao.updateQuantity(cartItemId, quantity);
			return cartDao.findById(cartItemId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	
	}
	/**
	 * 加载被选中的购物车信息
	 * @param cartItemIds
	 * @return
	 */
	public List<CartItem> loadCartItems(String cartItemIds){
		try {
			return cartDao.loadCartItems(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
