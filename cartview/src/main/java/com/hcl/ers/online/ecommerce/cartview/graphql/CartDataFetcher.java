package com.hcl.ers.online.ecommerce.cartview.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.hcl.ers.online.ecommerce.cartview.model.Cart;
import com.hcl.ers.online.ecommerce.cartview.model.CartView;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

@Service
public class CartDataFetcher implements DataFetcher<Cart> {

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public Cart get(DataFetchingEnvironment env) {
		final String id = env.getArgument("id");

		if (id == null) {
			throw new IllegalArgumentException("id is missing.");
		}

		CartView cartAggregate = applicationContext.getBean(CartView.class, id);
		return cartAggregate.getCart();
	}

}
