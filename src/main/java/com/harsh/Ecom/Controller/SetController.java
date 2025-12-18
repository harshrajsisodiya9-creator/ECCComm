package com.harsh.Ecom.Controller;

import com.harsh.Ecom.Model.Product;
import com.harsh.Ecom.Service.ProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping(value="/product" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addProd(@RequestBody Product prod) throws IOException {
        service.addProduct(prod, null);
        return new ResponseEntity<>("Product Added",HttpStatus.CREATED);
    }

    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProd(@RequestPart("product") Product prod, @RequestPart("imageFile") MultipartFile imageFile) throws IOException{

        try {
            service.addProduct(prod, imageFile);
            return new ResponseEntity<>("Product Added",HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{prodId}")
    public ResponseEntity<String> updateProd(@PathVariable int prodId, @RequestPart Product prod, @RequestPart MultipartFile imageFile){
        try {
            service.updateProduct(prodId,prod,imageFile);
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