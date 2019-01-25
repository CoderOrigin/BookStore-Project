package goods.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.commons.CommonUtils;
import goods.cart.dao.CartItemDao;
import goods.cart.domain.CartItem;

public class CartItemService {
	private CartItemDao cartdao = new CartItemDao();

	public List<CartItem> myCart(String uid) {
		try {
			return cartdao.findByUser(uid);
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
			CartItem currCart = cartdao.findByUidAndBid(uid, bid);
			if(currCart == null) {
				String cartItemId = CommonUtils.uuid();
				cartItem.setCartItemId(cartItemId);
				cartdao.addCartItem(cartItem);
			}else {
				int quantity = currCart.getQuantity()+ cartItem.getQuantity();
				cartdao.updateQuantity(currCart.getCartItemId(), quantity);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
