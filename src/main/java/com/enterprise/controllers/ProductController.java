package com.enterprise.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enterprise.models.Product;
import com.enterprise.models.ProductDTO;
import com.enterprise.services.ProductService;

import java.util.*;




@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {
	
	@Autowired
	ProductService service;
	
	@GetMapping("/products")
	public ResponseEntity<?> getProducts(){
		List<Product> products;
		try {
			products = service.getAllProducts();
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(service.getMap(e.getLocalizedMessage()));
		}
		return ResponseEntity.ok(products);
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<?> getProduct(@PathVariable("id") int id) {
		Product p;
		try {
			p = service.getProductById(id);
			System.out.println(p);
			if(p==null) {
				String msg = "No product exists with that id!!";
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.getMap(msg));
			}
			
		}
		catch(Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(service.getMap(e.getLocalizedMessage()));
		}
		return ResponseEntity.ok(p);
	}
	
	@SuppressWarnings("deprecation")
	@PostMapping("/product")
	public ResponseEntity<?> addProduct(@RequestPart Product product,@RequestPart MultipartFile image){
		try {
			
			product.setImageName(image.getOriginalFilename());
			product.setImageType(image.getContentType());
			product.setImageData(image.getBytes());
			Product p = service.save(product);
			return new ResponseEntity<>(p,HttpStatus.CREATED);
			
		}
		catch(Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(),HttpStatus.METHOD_FAILURE);
		}
	}
	
	@GetMapping(value="/product/{id}/image")
	public ResponseEntity<?> getImage(@PathVariable("id") int id){
		
		try {
			Product product = service.getProductById(id);
			byte[] imageData = product.getImageData();
			return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageData);
		}
		catch(Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("product/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable("id") int id,@RequestPart Product product,@RequestPart MultipartFile imageFile){
		
		try {
			service.update(id,product,imageFile);
			return new ResponseEntity<>("Product Updated Successfully",HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") int id){
		
		try {
			service.deleteProduct(id);
			return new ResponseEntity<>("Product Deleted Successfully",HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/products/search")
	public ResponseEntity<?> search(@RequestParam("keyword") String keyword){
		
		try {
			keyword = keyword.toLowerCase();
			List<ProductDTO> list = service.searchProducts(keyword);
			return ResponseEntity.ok(list);
		}
		catch(Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
}
