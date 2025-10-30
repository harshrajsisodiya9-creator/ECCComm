package com.harsh.Ecom.Repo;

import com.harsh.Ecom.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdRepo extends JpaRepository<Product, Integer> {
}
