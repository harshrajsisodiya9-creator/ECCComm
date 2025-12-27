package com.harsh.Ecom.config;

import com.harsh.Ecom.DTO.ProdDto;
import com.harsh.Ecom.Model.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration                      //  tells java we are making different beans in here
public class ModelConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();

        mapper.addMappings(new PropertyMap<Product, ProdDto>() {
            @Override
             protected void configure(){
                map().setName(source.getProdName());
                map().setPrice(source.getProdPrice());
            }
        });
        // reverse mapping from productDto to product
        mapper.addMappings(new PropertyMap<ProdDto, Product>() {
            @Override
            protected void configure(){
                map().setProdName(source.getName());
                map().setProdPrice(source.getPrice());
            }
        });
        return mapper;
    }
}
