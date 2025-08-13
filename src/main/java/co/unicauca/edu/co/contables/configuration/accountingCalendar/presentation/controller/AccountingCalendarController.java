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

    @PutMapping("/update")
    public ResponseEntity<AccountingCalendarRes> update(@Valid @RequestBody AccountingCalendarUpdateReq req) {
        AccountingCalendar updated = service.update(req);
        return ResponseEntity.ok(mapper.toRes(updated));
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

    @GetMapping("/findByYear/{enterpriseId}")
    public ResponseEntity<Page<AccountingCalendarRes>> findByYear(
            @PathVariable String enterpriseId,
            @RequestParam int year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AccountingCalendar> result = service.findByYear(enterpriseId, year, page, size);
        return ResponseEntity.ok(result.map(mapper::toRes));
    }

    @PostMapping("/changeState/all/{enterpriseId}")
    public ResponseEntity<Void> changeStateAll(@PathVariable String enterpriseId, @RequestParam Boolean status) {
        service.changeStateAll(enterpriseId, status);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/changeState/range")
    public ResponseEntity<Void> changeStateRange(@Valid @RequestBody AccountingCalendarRangeStateReq req) {
        service.changeStateRange(req);
        return ResponseEntity.noContent().build();
    }

    

    @DeleteMapping("/delete/{id}/{enterpriseId}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @PathVariable String enterpriseId) {
        service.delete(id, enterpriseId);
        return ResponseEntity.noContent().build();
    }
}


