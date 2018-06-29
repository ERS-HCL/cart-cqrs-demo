package com.hcl.ers.online.ecommerce.ms.price.cart;

import org.springframework.stereotype.Service;

import com.hcl.ers.online.ecommerce.ms.price.cart.priceview.Cart;
import com.hcl.ers.online.ecommerce.ms.price.cart.priceview.CartItem;
import com.hcl.ers.online.ecommerce.ms.price.cart.priceview.CartItemPrice;
import com.hcl.ers.online.ecommerce.ms.price.cart.priceview.CartPrice;

@Service
public class CartPriceService {
	
	public CartPrice calculatePrice(Cart cart) {
		CartPrice cartPrice = new CartPrice(cart.getId());
		
		float cartTotal = 0;
		for(CartItem cartItem: cart.getItems()) {
			CartItemPrice cartItemPrice = new CartItemPrice();
			cartItemPrice.setSkuId(cartItem.getSkuId());
			cartItemPrice.setQuantity(cartItem.getQuantity());
			float itemTotal = cartItem.getQuantity() * 2;
			cartTotal += itemTotal;
			cartItemPrice.setPrice(itemTotal);
			cartPrice.addItemPrice(cartItemPrice);
			cartPrice.setTotalAmount(cartTotal);
		}
		
		return cartPrice;
	}
}
