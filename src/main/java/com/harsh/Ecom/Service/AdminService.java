package com.harsh.Ecom.Service;

import com.harsh.Ecom.DTO.SellerDto.SellerRequestDto;
import com.harsh.Ecom.DTO.SellerDto.SellerResponseDto;
import com.harsh.Ecom.Model.Role;
import com.harsh.Ecom.Model.Seller;
import com.harsh.Ecom.Model.User;
import com.harsh.Ecom.Repo.CustomerRepo;
import com.harsh.Ecom.Repo.SellerRepo;
import com.harsh.Ecom.Repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final SellerRepo sellerRepo;
    private final CustomerRepo customerRepo;

    @Transactional
    public void onBoardSeller(SellerRequestDto sellerRequestDto){
        User user = userRepository.findById(sellerRequestDto.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        if(sellerRepo.existsById(user.getId())){
            throw new IllegalArgumentException("Already a Seller");
        }

        user.getRole().add(Role.SELLER);

        Seller seller = Seller.builder()
                .user(user)
                .name(sellerRequestDto.getName())
                .email(sellerRequestDto.getEmail())
                .build();

        sellerRepo.save(seller);

        customerRepo.deleteById(user.getId());
    }
}
