package com.harsh.Ecom.Repo;

import com.harsh.Ecom.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ProdRepo extends JpaRepository<Product, Integer> {
    Optional<Product> findByProdNameContainingIgnoreCase(String prodName);
}
