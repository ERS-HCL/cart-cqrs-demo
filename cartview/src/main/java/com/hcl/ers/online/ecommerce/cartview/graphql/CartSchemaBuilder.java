package com.hcl.ers.online.ecommerce.cartview.graphql;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Service
public class CartSchemaBuilder {

	@Autowired
	private CartDataFetcher cartDataFetcher;
	
	public GraphQL getGraphQL() {
		SchemaParser schemaParser = new SchemaParser();
		SchemaGenerator schemaGenerator = new SchemaGenerator();
		
		InputStream inputStream = CartSchemaBuilder.class.getResourceAsStream("/graphql/schema/cart.graphqls");
		Reader schemaFile = new InputStreamReader(inputStream);
		TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaFile);
		RuntimeWiring wiring = buildRuntimeWiring();
		GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
		
		return GraphQL.newGraphQL(graphQLSchema).build();
	}

	private RuntimeWiring buildRuntimeWiring() {
		return RuntimeWiring.newRuntimeWiring()
				.type("QueryType",
						typeWiring -> typeWiring.dataFetcher("cart", cartDataFetcher))
				.build();
	}

}