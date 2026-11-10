package com.maverickbank.controller;

import com.maverickbank.dto.response.FinancialReportDto;
import com.maverickbank.dto.response.TransactionRespDto;
import com.maverickbank.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/api/report/financial")
    public FinancialReportDto getFinancialReport(){
        return reportService.getFinancialReport();
    }

    @GetMapping("/api/report/regulatory")
    public List<TransactionRespDto> getRegulatoryReport(@RequestParam(value = "threshold", required = false, defaultValue = "1000000") BigDecimal threshold,
                                                        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                        @RequestParam(value = "size", required = false, defaultValue = "20") Integer size){
        return reportService.getRegulatoryReport(threshold, page, size);
    }
}
