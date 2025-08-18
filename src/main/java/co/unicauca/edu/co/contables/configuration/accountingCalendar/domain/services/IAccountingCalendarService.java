package co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.services;

import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.models.AccountingCalendar;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.request.*;
import org.springframework.data.domain.Page;

public interface IAccountingCalendarService {

	AccountingCalendar create(AccountingCalendarCreateReq request);

	AccountingCalendar findById(Long id, String idEnterprise);

	void delete(Long id, String idEnterprise);

	AccountingCalendar openMonth(AccountingCalendarCreateMonthReq request);

	long deleteByMonth(AccountingCalendarDeleteMonthReq request);

	AccountingCalendar openYear(AccountingCalendarCreateYearReq request);

	long deleteByYear(AccountingCalendarDeleteYearReq request);

	Page<AccountingCalendar> findActiveByEnterpriseAndYear(String idEnterprise, int year, int page, int size);

}


