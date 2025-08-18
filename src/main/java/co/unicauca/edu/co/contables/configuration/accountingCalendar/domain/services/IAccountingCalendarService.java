package co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.services;

import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.models.AccountingCalendar;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.request.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAccountingCalendarService {

	AccountingCalendar create(AccountingCalendarCreateReq request);

	AccountingCalendar findById(Long id, String idEnterprise);

	void delete(Long id, String idEnterprise);

	List<AccountingCalendar> openMonthBatch(AccountingCalendarCreateMonthReq request);

	long deleteByMonth(AccountingCalendarDeleteMonthReq request);

	List<AccountingCalendar> openYearBatch(AccountingCalendarCreateYearReq request);

	long deleteByYear(AccountingCalendarDeleteYearReq request);

	Page<AccountingCalendar> findActiveByEnterpriseAndYear(String idEnterprise, int year, int page, int size);

}


