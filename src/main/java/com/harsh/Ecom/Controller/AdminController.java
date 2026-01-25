package com.harsh.Ecom.Controller;


import com.harsh.Ecom.DTO.SellerDto.SellerRequestDto;
import com.harsh.Ecom.DTO.SellerDto.SellerResponseDto;
import com.harsh.Ecom.Service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/sellerBoard")
    public ResponseEntity<String> sellerOnBoard(@RequestBody SellerRequestDto sellerRequestDto){
        adminService.onBoardSeller(sellerRequestDto);
        return ResponseEntity.ok().body("Seller On Boarded");
    }
}
