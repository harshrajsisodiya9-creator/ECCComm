package com.harsh.Ecom.Service;

import com.harsh.Ecom.DTO.ProdDto;
import com.harsh.Ecom.DTO.ProdMapper;
import com.harsh.Ecom.DTO.ProdResponseDto;
import com.harsh.Ecom.Model.Product;
import com.harsh.Ecom.Repo.ProdRepo;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProdService {

    @Autowired                // field injection
    private ProdRepo repo;

    private final ModelMapper modelMapper;

    public ProdService(ModelMapper modelMapper, ProdMapper mapper) {
        this.modelMapper = modelMapper;
        this.mapper = mapper;
    }         // constructor injection can be avoided if we use @RequiredArgsConstructor from lombok

    public List<ProdDto> getProducts(){
        List<Product> prods = repo.findAll();
        return prods
                .stream()
                .map(prod -> modelMapper.map(prod, ProdDto.class))          // we write ProdDTO.class and not ProdDTo or prod.class too because
                .toList();                                                          // prod is already a object of the product class so its already an instance of the product class
                                                                                    // but ProductDto is not initialized(new ProdDto()) so we need to provide modelMapper with a blueprint
                                                                                    // it needs to know which object need to be created from which class and copy fields from the source to destination
    }

    // .map() requires a function(lambda) as a expression not an object so we provide with a lambda expression(a simplified version of function)
    // cant write .map(modelMapper.map(prod,ProdDTO.class)) because modelMapper.map() returns a ProdDTO object not a function expression.

    public ProdDto getProduct(int prodId){
        Product prod = repo.findById(prodId).orElseThrow(()-> new EntityNotFoundException("Product not found"));
        return modelMapper.map(prod, ProdDto.class);
    }

    public ProdDto getProduct(String prodName){
       Product prod = repo.findByProdNameContainingIgnoreCase(prodName).orElseThrow(() -> new EntityNotFoundException("Product not found"));
       return modelMapper.map(prod, ProdDto.class);
    }

    private final ProdMapper mapper;

    public ProdDto addProduct(ProdResponseDto prodResponseDto, MultipartFile imageFile) throws IOException {

        Product prod = mapper.toEntity(prodResponseDto);
        System.out.println(prod.getProdPrice());
        if(imageFile!= null && !imageFile.isEmpty()){
            prod.setImageName(imageFile.getOriginalFilename());
            prod.setImageType(imageFile.getContentType());
            prod.setImageData(imageFile.getBytes());
        }                                                    // idhar image save hui hai object(prod) ki field mein
        Product prod1 = repo.save(prod);
        return mapper.toDto(prod1);                                                     // idhar vo image aur prod ki baki fields object(prod) se pakad ke database mein update ki ja rhi hai

    }

    public ProdDto updateProduct(int prodId,ProdResponseDto prod,MultipartFile imageFile) throws IOException{
        Product newProd = repo.findById(prodId).orElseThrow(() -> new NoSuchElementException("Product not found"));

        if(imageFile != null && !imageFile.isEmpty()){
            newProd.setImageData(imageFile.getBytes());
            newProd.setImageName(imageFile.getOriginalFilename());
            newProd.setImageType(imageFile.getContentType());
        }
        Product prod1 = repo.save(mapper.toEntity(prod));
        return mapper.toDto(prod1);
    }

    public void deleteProd(int prodId){
        repo.findById(prodId).orElseThrow(()-> new NoSuchElementException("Product not found"));
        repo.deleteById(prodId);
    }
}
