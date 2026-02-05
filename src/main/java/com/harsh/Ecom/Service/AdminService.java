package com.harsh.Ecom.Service;

import com.harsh.Ecom.DTO.SellerDto.SellerMapper;
import com.harsh.Ecom.DTO.SellerDto.SellerRequestDto;
import com.harsh.Ecom.DTO.SellerDto.SellerResponseDto;
import com.harsh.Ecom.Model.Role;
import com.harsh.Ecom.Model.Seller;
import com.harsh.Ecom.Model.User;
import com.harsh.Ecom.Repo.CustomerRepo;
import com.harsh.Ecom.Repo.SellerRepo;
import com.harsh.Ecom.Repo.UserRepository;
import com.harsh.Ecom.error.ConflictException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final SellerRepo sellerRepo;
    private final CustomerRepo customerRepo;
    private final SellerMapper sellerMapper;

    @Transactional
    public SellerResponseDto onBoardSeller(SellerRequestDto sellerRequestDto){
        User user = userRepository.findById(sellerRequestDto.getId()).orElseThrow(() -> new UsernameNotFoundException(sellerRequestDto.getId().toString()));  // Global Exception Handler

        if(sellerRepo.existsById(user.getId())){
            throw new ConflictException("Seller Already Exist"); // Global Exception Handler
        }

        user.getRole().add(Role.SELLER);

        Seller seller = Seller.builder()
                .user(user)
                .name(sellerRequestDto.getName())
                .email(sellerRequestDto.getEmail())
                .build();

        sellerRepo.save(seller);
        customerRepo.deleteById(user.getId());

        return sellerMapper.toResponse(seller);
    }
}
