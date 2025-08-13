package co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.services;

import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.models.AccountingCalendar;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.request.*;
import org.springframework.data.domain.Page;

public interface IAccountingCalendarService {

	AccountingCalendar create(AccountingCalendarCreateReq request);

	AccountingCalendar update(AccountingCalendarUpdateReq request);

	AccountingCalendar findById(Long id, String idEnterprise);

    Page<AccountingCalendar> findAllByEnterprise(String idEnterprise, int page, int size);

    Page<AccountingCalendar> findByRange(String idEnterprise, java.time.LocalDate startDate, java.time.LocalDate endDate, int page, int size);

	Page<AccountingCalendar> findByYear(String idEnterprise, int year, int page, int size);

    void changeStateAll(String idEnterprise, Boolean status);

    void changeStateRange(AccountingCalendarRangeStateReq request);

	void delete(Long id, String idEnterprise);
}


