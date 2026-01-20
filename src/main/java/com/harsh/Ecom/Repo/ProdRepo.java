package com.harsh.Ecom.Repo;

import com.harsh.Ecom.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdRepo extends JpaRepository<Product, Integer> {
    List<Product> findByProdNameContainingIgnoreCase(String prodName);
    Optional<Product> findByProdName(String prodName);

    void deleteByProdName(String prodName);
}
