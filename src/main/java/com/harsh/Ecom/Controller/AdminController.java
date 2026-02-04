package com.harsh.Ecom.Controller;


import com.harsh.Ecom.DTO.SellerDto.SellerRequestDto;
import com.harsh.Ecom.DTO.SellerDto.SellerResponseDto;
import com.harsh.Ecom.Service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/sellerBoard")
    public ResponseEntity<String> sellerOnBoard(@RequestBody SellerRequestDto sellerRequestDto){
        adminService.onBoardSeller(sellerRequestDto);
        return ResponseEntity.ok().body("Seller On Boarded");
    }
}
