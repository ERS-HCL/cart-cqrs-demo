package com.hcl.ers.online.ecommerce.ms.price.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.ers.online.ecommerce.ms.price.cart.priceview.CartPriceView;

@RestController
public class CartPriceController {
	
	@Autowired
	ApplicationContext applicationContext;
	@RequestMapping(value = "/ms/price/cart/{cartId}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Object handle(@PathVariable("cartId") String cartId) {
		
		CartPriceView cartPriceView = applicationContext.getBean(CartPriceView.class);
		cartPriceView.load(cartId);
		
		return cartPriceView.getCartPrice();
		
	}

}
