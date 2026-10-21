package com.maverickbank.controller;

import com.maverickbank.dto.request.BeneficiaryDto;
import com.maverickbank.dto.response.BeneficiaryRespDto;
import com.maverickbank.service.BeneficiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BeneficiaryController {
    private final BeneficiaryService beneficiaryService;

    @PostMapping("/api/beneficiary/add")
    public void add(Principal principal, @Valid @RequestBody BeneficiaryDto dto){
        beneficiaryService.add(principal.getName(), dto);
    }

    @GetMapping("/api/beneficiary/my")
    public List<BeneficiaryRespDto> getMyBeneficiaries(Principal principal){
        return beneficiaryService.getMyBeneficiaries(principal.getName());
    }
}
