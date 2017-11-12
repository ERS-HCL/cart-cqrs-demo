package com.onefoundation.cqrsdemo.cartquery.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.onefoundation.cqrsdemo.cartquery.model.Cart;
import com.onefoundation.cqrsdemo.cartquery.model.CartAggregate;

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

		CartAggregate cartAggregate = applicationContext.getBean(CartAggregate.class, id);
		return cartAggregate.getCart();
	}

}
