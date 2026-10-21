package com.maverickbank.service;

import com.maverickbank.dto.request.AccountClosureDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountClosureRequestService {

    public void add(String name, long accountId, @Valid AccountClosureDto dto) {

    }
}
