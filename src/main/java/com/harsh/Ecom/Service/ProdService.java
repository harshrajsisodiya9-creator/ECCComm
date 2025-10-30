package com.harsh.Ecom.Service;

import com.harsh.Ecom.Model.Product;
import com.harsh.Ecom.Repo.ProdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProdService {

    @Autowired
    ProdRepo repo;

    public List<Product> getProducts(){
        return repo.findAll();
    }

    public Product getProduct(int prodId){
        return repo.findById(prodId).orElse(null);
    }

    public Product addProduct(Product prod, MultipartFile imageFile) throws IOException {
        prod.setImageName(imageFile.getOriginalFilename());
        prod.setImageType(imageFile.getContentType());
        prod.setImageData(imageFile.getBytes());            // idhar image save hui hai object(prod) ki field mein
        return repo.save(prod);                             // idhar vo image aur prod ki baki fields object(prod) se pakad ke database mein update ki ja rhi hai

    }

    public Product updateProduct(int prodId,Product prod,MultipartFile imageFile) throws IOException{
        Product newProd = repo.findById(prodId).orElseThrow(() -> new NoSuchElementException("Product not found"));

        if(imageFile != null && !imageFile.isEmpty()){
            prod.setImageData(imageFile.getBytes());
            prod.setImageName(imageFile.getOriginalFilename());
            prod.setImageType(imageFile.getContentType());
        }


        return repo.save(prod);
    }

    public void deleteProd(int prodId){
        repo.findById(prodId).orElseThrow(()-> new NoSuchElementException("Product not found"));
        repo.deleteById(prodId);
    }
}
