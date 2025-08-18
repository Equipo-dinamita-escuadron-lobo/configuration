package co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.controller;

import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.mapper.AccountingCalendarDomainMapper;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.models.AccountingCalendar;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.services.IAccountingCalendarService;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.request.*;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.response.AccountingCalendarRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/config/accounting-calendar")
@RequiredArgsConstructor
public class AccountingCalendarController {

    private final IAccountingCalendarService service;
    private final AccountingCalendarDomainMapper mapper;

    @PostMapping("/create")
    public ResponseEntity<AccountingCalendarRes> create(@Valid @RequestBody AccountingCalendarCreateReq req) {
        AccountingCalendar created = service.create(req);
        return ResponseEntity.ok(mapper.toRes(created));
    }

    @GetMapping("/findById/{id}/{enterpriseId}")
    public ResponseEntity<AccountingCalendarRes> getById(@PathVariable Long id, @PathVariable String enterpriseId) {
        return ResponseEntity.ok(mapper.toRes(service.findById(id, enterpriseId)));
    }

    
    @GetMapping("/findByRange/{enterpriseId}")
    public ResponseEntity<Page<AccountingCalendarRes>> findByRange(
            @PathVariable String enterpriseId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AccountingCalendar> result = service.findByRange(enterpriseId, startDate, endDate, page, size);
        return ResponseEntity.ok(result.map(mapper::toRes));
    }


    @DeleteMapping("/delete/{id}/{enterpriseId}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @PathVariable String enterpriseId) {
        service.delete(id, enterpriseId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/open-month")
    public ResponseEntity<AccountingCalendarRes> openMonth(@Valid @RequestBody AccountingCalendarCreateMonthReq req) {
        AccountingCalendar created = service.openMonth(req);
        return ResponseEntity.ok(mapper.toRes(created));
    }

    @DeleteMapping("/delete-month")
    public ResponseEntity<Void> deleteByMonth(@Valid @RequestBody AccountingCalendarDeleteMonthReq req) {
        service.deleteByMonth(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/open-year")
    public ResponseEntity<AccountingCalendarRes> openYear(@Valid @RequestBody AccountingCalendarCreateYearReq req) {
        AccountingCalendar created = service.openYear(req);
        return ResponseEntity.ok(mapper.toRes(created));
    }

    @DeleteMapping("/delete-year")
    public ResponseEntity<Void> deleteByYear(@Valid @RequestBody AccountingCalendarDeleteYearReq req) {
        service.deleteByYear(req);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active/{enterpriseId}")
    public ResponseEntity<Page<AccountingCalendarRes>> findActiveByEnterpriseAndYear(
            @PathVariable String enterpriseId,
            @RequestParam int year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AccountingCalendar> result = service.findActiveByEnterpriseAndYear(enterpriseId, year, page, size);
        return ResponseEntity.ok(result.map(mapper::toRes));
    }

}
