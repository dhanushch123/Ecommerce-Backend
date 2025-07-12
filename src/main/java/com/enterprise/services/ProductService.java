package com.enterprise.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enterprise.models.Product;
import com.enterprise.models.ProductDTO;
import com.enterprise.repositories.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class ProductService {
	
	@Autowired
	ProductRepository repo;
	
	@PersistenceContext
	EntityManager em;

	public List<Product> getAllProducts() {
		List<Product> products = repo.findAll();
		return products;
	}

	public Product getProductById(int id) {
		return repo.findById(id).orElse(null);
	}
	
	public HashMap<String,String> getMap(String msg){
		HashMap<String,String> map = new HashMap<>();
		map.put("msg", msg);
		return map;
	}

	public Product save(Product product) {
		return repo.save(product);
	}

	public Product update(int id, Product updatedProduct, MultipartFile imageFile) throws IOException {
		System.out.println(updatedProduct);
		Product existingProduct = repo.findById(id).get();
		existingProduct.setName(updatedProduct.getName());
	    existingProduct.setDescription(updatedProduct.getDescription());
	    existingProduct.setBrand(updatedProduct.getBrand());
	    existingProduct.setPrice(updatedProduct.getPrice());
	    existingProduct.setCategory(updatedProduct.getCategory());
	    existingProduct.setReleaseDate(updatedProduct.getReleaseDate());
	    existingProduct.setAvailability(updatedProduct.isAvailability());
	    existingProduct.setQuantity(updatedProduct.getQuantity());
	    existingProduct.setImageName(imageFile.getName());
	    existingProduct.setImageType(imageFile.getContentType());
	    existingProduct.setImageData(imageFile.getBytes());
		return repo.save(existingProduct);
		
	}

	public void deleteProduct(int id) {
		repo.deleteById(id);
	}
	
	
	
	public List<ProductDTO> searchProducts(String keyword){
		
		List<ProductDTO> list = null;
		try {
			int val = Integer.parseInt(keyword);
			list = repo.searchProductsByKeyword(val);
		}
		catch(Exception e) {
			list = repo.searchProductsByKeyword(keyword);
		}
		return list;
	}
	


	
	
}
