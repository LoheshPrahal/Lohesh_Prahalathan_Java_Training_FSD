package com.maverickbank.controller;

import com.maverickbank.dto.request.AccountClosureDto;
import com.maverickbank.dto.response.AccountClosureRespDto;
import com.maverickbank.repository.AccountClosureRequestRepository;
import com.maverickbank.service.AccountClosureRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AccountClosureRequestController {
    private final AccountClosureRequestService accountClosureRequestService;

    @PostMapping("/api/closure/add/{accountId}")
    public void add(@PathVariable Long accountId, Principal principal, @Valid @RequestBody AccountClosureDto dto){
        accountClosureRequestService.add(principal.getName(), accountId, dto);
    }

    @GetMapping("/api/closure/pending")
    public List<AccountClosureRespDto> getPendingRequests(){
        return accountClosureRequestService.getPendingRequests();
    }

    @PutMapping("/api/closure/approve/{requestId}")
    public void approve(@PathVariable Long requestId, Principal principal){
        accountClosureRequestService.approve(requestId, principal.getName());
    }

    @PutMapping("/api/closure/reject/{requestId}")
    public void reject(@PathVariable Long requestId, Principal principal){
        accountClosureRequestService.reject(requestId, principal.getName());
    }
}
