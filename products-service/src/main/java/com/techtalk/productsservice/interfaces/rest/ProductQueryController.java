package com.techtalk.productsservice.interfaces.rest;

import com.techtalk.productsservice.domain.queries.FindProductsQuery;
import com.techtalk.productsservice.interfaces.rest.transform.dto.ProductRestModel;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductQueryController {
	
	@Autowired
	QueryGateway queryGateway;
	
	@GetMapping
	public List<ProductRestModel> getProducts() {
		FindProductsQuery findProductsQuery = new FindProductsQuery();
		List<ProductRestModel> products = queryGateway.query(findProductsQuery,
				ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
		
		return products;
	}
}
