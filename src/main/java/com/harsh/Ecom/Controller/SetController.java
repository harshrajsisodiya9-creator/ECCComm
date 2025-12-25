package com.harsh.Ecom.Controller;

import com.harsh.Ecom.DTO.ProdDto;
import com.harsh.Ecom.DTO.ProdMapper;
import com.harsh.Ecom.DTO.ProdResponseDto;
import com.harsh.Ecom.Model.Product;
import com.harsh.Ecom.Service.ProdService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SetController {

    @Autowired
    private ProdService service;

   private final ProdMapper mapper;

    @PostMapping(value="/product" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProdDto> addProd(@RequestBody ProdResponseDto prod) throws IOException {
        ProdDto dto = service.addProduct(prod, null);
        return new ResponseEntity<>(dto,HttpStatus.CREATED);
    }

    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProd(@RequestPart("prodResponseDto") ProdResponseDto prod, @RequestPart("imageFile") MultipartFile imageFile) throws IOException{

        try {
            ProdDto dto = service.addProduct(prod, imageFile);
            return new ResponseEntity<>(dto,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{prodId}")
    public ResponseEntity<?> updateProd(@PathVariable int prodId, @RequestPart ProdResponseDto prod, @RequestPart MultipartFile imageFile){
        try {
             ProdDto dto = service.updateProduct(prodId,prod,imageFile);
            return new ResponseEntity<>(dto, HttpStatus.OK);
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