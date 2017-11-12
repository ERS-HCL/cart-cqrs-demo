package com.onefoundation.cqrsdemo.cartquery.query;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.onefoundation.cqrsdemo.cartquery.graphql.CartSchemaBuilder;

import graphql.ExecutionResult;
import graphql.GraphQL;

@RestController
public class CartQueryController {
	
	@Autowired
	CartSchemaBuilder cartSchemaBuilder;
	
	
	@RequestMapping(value = "/cartquery/view", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Object handle(@RequestBody final Map<String, Object> payload) {
		final Map<String, Object> variables = (Map<String, Object>) payload.get("variables");
		final String query = payload.get("query").toString();
		GraphQL graphQL = cartSchemaBuilder.getGraphQL();
		final ExecutionResult executionResult = graphQL.execute(query, null, null, variables);
		
		if (executionResult.getErrors().size() > 0) {
			throw new RuntimeException("Error:"+executionResult.getErrors());
		}
		
		return executionResult.getData();
	}

}
