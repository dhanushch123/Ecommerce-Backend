package com.enterprise.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enterprise.models.Product;
import com.enterprise.models.ProductDTO;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
	
	
    @Query("SELECT new com.enterprise.models.ProductDTO(p.id, p.name) FROM Product p WHERE " +
    		"(LOWER(p.name) LIKE CONCAT('%',:keyword,'%') " +
    		"OR LOWER(p.brand) LIKE CONCAT('%',:keyword,'%') " +
    		"OR LOWER(p.category) LIKE CONCAT('%',:keyword,'%'))")
	List<ProductDTO> searchProductsByKeyword(@Param("keyword")String keyword);
    
    
    @Query("SELECT new com.enterprise.models.ProductDTO(p.id, p.name) FROM Product p WHERE " +
    		"p.price <= :val")
    List<ProductDTO> searchProductsByKeyword(@Param("val")int val);
    
    
	
}
