package co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.services;

import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.entity.AccountingCalendarEntity;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.mapper.AccountingCalendarDataMapper;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.repository.AccountingCalendarRepository;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.mapper.AccountingCalendarDomainMapper;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.models.AccountingCalendar;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.request.*;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar.AccountingCalendarDateExistsException;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar.AccountingCalendarNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class AccountingCalendarServiceImpl implements IAccountingCalendarService {

	private final AccountingCalendarRepository repository;
	private final AccountingCalendarDataMapper dataMapper;
	private final AccountingCalendarDomainMapper domainMapper;

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public AccountingCalendar create(AccountingCalendarCreateReq request) {
        // Validar si ya existe una fecha para ese día
        boolean exists = repository.existsByIdEnterpriseAndDate(
                request.getIdEnterprise(), request.getDate());
        if (exists) {
            throw new AccountingCalendarDateExistsException();
        }
        AccountingCalendar domain = domainMapper.toDomain(request);
        if (request.getStatus() == null) {
            domain.setStatus(false);
        }
		AccountingCalendarEntity saved = repository.save(dataMapper.toEntity(domain));
		return dataMapper.toDomain(saved);
	}

    @Transactional(readOnly = true)
    public AccountingCalendar findById(Long id, String idEnterprise) {
        return dataMapper.toDomain(repository.findByIdAndIdEnterprise(id, idEnterprise)
                .orElseThrow(AccountingCalendarNotFoundException::new));
    }

    @Transactional
    public void delete(Long id, String idEnterprise) {
        AccountingCalendarEntity entity = repository.findByIdAndIdEnterprise(id, idEnterprise)
                .orElseThrow(AccountingCalendarNotFoundException::new);
        repository.delete(entity);
    }

    @Transactional
    public AccountingCalendar openMonth(AccountingCalendarCreateMonthReq request) {
        YearMonth ym = YearMonth.of(request.getYear(), request.getMonth());
        LocalDate cursor = ym.atDay(1);
        LocalDate endOfMonth = ym.atEndOfMonth();
        AccountingCalendar lastCreated = null;
        while (!cursor.isAfter(endOfMonth)) {
            AccountingCalendarCreateReq createReq = AccountingCalendarCreateReq.builder()
                    .idEnterprise(request.getIdEnterprise())
                    .date(cursor)
                    .status(request.getStatus())
                    .build();
            lastCreated = create(createReq);
            cursor = cursor.plusDays(1);
        }
        return lastCreated;
    }

    @Transactional
    public long deleteByMonth(AccountingCalendarDeleteMonthReq request) {
        YearMonth ym = YearMonth.of(request.getYear(), request.getMonth());
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        return repository.deleteByIdEnterpriseAndDateBetween(
                request.getIdEnterprise(), start, end);
    }

    @Transactional
    public AccountingCalendar openYear(AccountingCalendarCreateYearReq request) {
        LocalDate cursor = LocalDate.of(request.getYear(), 1, 1);
        LocalDate endOfYear = LocalDate.of(request.getYear(), 12, 31);
        AccountingCalendar lastCreated = null;
        while (!cursor.isAfter(endOfYear)) {
            AccountingCalendarCreateReq createReq = AccountingCalendarCreateReq.builder()
                    .idEnterprise(request.getIdEnterprise())
                    .date(cursor)
                    .status(request.getStatus())
                    .build();
            lastCreated = create(createReq);
            cursor = cursor.plusDays(1);
        }
        return lastCreated;
    }

    @Transactional
    public long deleteByYear(AccountingCalendarDeleteYearReq request) {
        LocalDate start = LocalDate.of(request.getYear(), 1, 1);
        LocalDate end = LocalDate.of(request.getYear(), 12, 31);
        return repository.deleteByIdEnterpriseAndDateBetween(
                request.getIdEnterprise(), start, end);
    }

    @Transactional(readOnly = true)
    public Page<AccountingCalendar> findActiveByEnterpriseAndYear(String idEnterprise, int year, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);
        return repository.findAllByIdEnterpriseAndStatusAndDateBetweenOrderByDateAsc(
                idEnterprise, true, startOfYear, endOfYear, pageable).map(dataMapper::toDomain);
    }

}


