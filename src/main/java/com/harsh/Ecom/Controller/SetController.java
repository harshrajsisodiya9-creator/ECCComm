package com.harsh.Ecom.Controller;

import com.harsh.Ecom.Model.Product;
import com.harsh.Ecom.Service.ProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/set")
public class SetController {

    @Autowired
    private ProdService service;

    @PostMapping("/product")
    public ResponseEntity<?> addProd(@RequestPart Product prod, @RequestPart MultipartFile imageFile){

        try {
            Product prod1 = service.addProduct(prod, imageFile);
            return new ResponseEntity<>(prod1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{prodId}")
    public ResponseEntity<String> updateProd(@PathVariable int prodId, @RequestPart(required = false) Product prod, @RequestPart(required = false) MultipartFile imageFile){
        try {
            Product prod1 = service.updateProduct(prodId,prod,imageFile);
            return new ResponseEntity<>("Product Updated", HttpStatus.OK);
        }
        catch(NoSuchElementException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Processing Image");
        }
    }

    @DeleteMapping("/{prodId}")
    public ResponseEntity<String> deleteProd(@PathVariable int prodId){
        try{
            service.deleteProd(prodId);
            return new ResponseEntity<>("Product Successfully Deleted", HttpStatus.OK);
        }
        catch(NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}