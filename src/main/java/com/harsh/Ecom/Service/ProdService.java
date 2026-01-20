package com.harsh.Ecom.Service;

import com.harsh.Ecom.DTO.ProdDto;
import com.harsh.Ecom.DTO.ProdMapper;
import com.harsh.Ecom.DTO.ProdResponseDto;
import com.harsh.Ecom.Model.Product;
import com.harsh.Ecom.Repo.ProdRepo;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProdService {

    private final String cache_name = "product";

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
    }                                                                               // if already created object instance is there then no problem we can write proddto without the .class thingi

    // .map() requires a function(lambda) as a expression not an object so we provide with a lambda expression(a simplified version of function)
    // cant write .map(modelMapper.map(prod,ProdDTO.class)) because modelMapper.map() returns a ProdDTO object not a function expression.

    @Cacheable(value=cache_name, key = "#prodId")
    public ProdDto getProduct(int prodId){
        Product prod = repo.findById(prodId).orElseThrow(()-> new EntityNotFoundException("Product not found"));
        return modelMapper.map(prod, ProdDto.class);
    }

    @Cacheable(value = cache_name, key ="#prodName")
    public List<ProdDto> getProduct(String prodName){
       List <Product> prod = repo.findByProdNameContainingIgnoreCase(prodName);
       return prod
               .stream()
               .map(mapper::toDto)              //prods -> mapper.toDto(prods)
               .toList();
    }

    private final ProdMapper mapper;

    @CachePut(cacheNames = cache_name, key = "#result.id")
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

    @CachePut(value = cache_name, key = "#result.prodId")
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

    @CacheEvict(cacheNames = cache_name, key = "#prodId")
    public void deleteProd(int prodId){
        repo.findById(prodId).orElseThrow(()-> new NoSuchElementException("Product not found"));
        repo.deleteById(prodId);
    }

    // @Caching can be used for multiple things like here its used for different keys(not a good practice just for example
    //  can be used for multiple cache deletion) , ex (@CacheEvict("address")),(@CacheEvict("product"))
    
    //@Caching(evict = {
    //  @CacheEvict(value="product", key="#prodName"),
    //  @CacheEvict(value="product", key="#prodId")
    // })

    @CacheEvict(cacheNames = cache_name, key = "#prodName")
    public void deleteProds(String prodName){
        repo.findByProdName(prodName).orElseThrow(() -> new NoSuchElementException("No product"));
        repo.deleteByProdName(prodName);
    }

    @CachePut(value = cache_name, key = "#prodId")
    public ProdDto patchProduct (int prodId, ProdResponseDto prod, MultipartFile imageFile) throws IOException {
        Product prod1 = repo.findById(prodId).orElseThrow(() -> new NoSuchElementException("No such Product"));

        if( prod!= null){
            mapper.updateEntityFromDto(prod, prod1);
        }

        if(!imageFile.isEmpty() && imageFile != null){
            prod1.setImageData(imageFile.getBytes());
            prod1.setImageName(imageFile.getOriginalFilename());
            prod1.setImageType(imageFile.getContentType());
        }
        return mapper.toDto(repo.save(prod1));
    }
}
