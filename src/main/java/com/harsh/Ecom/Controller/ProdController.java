package com.harsh.Ecom.Controller;

import com.harsh.Ecom.Model.Product;
import com.harsh.Ecom.Service.ProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin         //As the backend and frontend run on two different ports Cross origin resource sharing(CORS) is not allowed by java so CrossOrigin is required for it to work
@RequestMapping("/product")
public class ProdController {

    @Autowired
    private ProdService service;

    @GetMapping("/all")
    public List<Product> getProds(){
        return service.getProducts();
    }

    @GetMapping("id/{prodId}")
    public ResponseEntity<Product> getProduct(@PathVariable int prodId){
        Product pro = service.getProduct(prodId);
        if(pro != null){
            return new ResponseEntity<>(pro, HttpStatus.FOUND);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("name/{prodName}")
    public ResponseEntity<?> getProductByname(@PathVariable String prodName){
        try{
            Product prod = service.getProduct(prodName);
            return new ResponseEntity<>(prod, HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{prodId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable int prodId){
        Product prod = service.getProduct(prodId);
        byte[] imageFile = prod.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(prod.getImageType()))
                .body(imageFile);
    }
}
