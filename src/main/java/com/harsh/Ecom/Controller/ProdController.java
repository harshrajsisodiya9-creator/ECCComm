package com.harsh.Ecom.Controller;

import com.harsh.Ecom.DTO.ProdDto;
import com.harsh.Ecom.Model.Product;
import com.harsh.Ecom.Service.ProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/product")
public class ProdController {

    @Autowired
    private ProdService service;

    @GetMapping("/all")
    public List<ProdDto> getProds(){
        return service.getProducts();
    }

    @GetMapping("id/{prodId}")
    public ResponseEntity<ProdDto> getProduct(@PathVariable int prodId){
        ProdDto pro = service.getProduct(prodId);
        if(pro != null){
            return new ResponseEntity<>(pro, HttpStatus.FOUND);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("name/{prodName}")
    public ResponseEntity<?> getProductByName(@PathVariable String prodName){
        try{
            List<ProdDto> prod = service.getProduct(prodName);
            return new ResponseEntity<>(prod, HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{prodId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable int prodId){
        ProdDto prod = service.getProduct(prodId);
        byte[] imageFile = prod.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(prod.getImageType()))
                .body(imageFile);                                      // new method of returning responseEntity.ok() (static factory method)        both methods are doing same thing no difference in response time/memory taken
                                                                       // old one/legacy style was returning like this: return new ResponseEntity<>() (creating a new instance)
    }
}
