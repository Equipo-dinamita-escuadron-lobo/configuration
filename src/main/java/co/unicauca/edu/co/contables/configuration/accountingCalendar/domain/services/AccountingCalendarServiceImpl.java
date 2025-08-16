package co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.services;

import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.entity.AccountingCalendarEntity;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.mapper.AccountingCalendarDataMapper;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.repository.AccountingCalendarRepository;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.mapper.AccountingCalendarDomainMapper;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.models.AccountingCalendar;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.request.*;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar.AccountingCalendarOverlapException;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar.AccountingCalendarInvalidRangeException;
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
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new AccountingCalendarInvalidRangeException();
        }
        // Validar solapamiento
        boolean overlap = repository.existsByIdEnterpriseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                request.getIdEnterprise(), request.getEndDate(), request.getStartDate());
        if (overlap) {
            throw new AccountingCalendarOverlapException();
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

	@Transactional(readOnly = true)
	public Page<AccountingCalendar> findAllByEnterprise(String idEnterprise, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByIdEnterprise(idEnterprise, pageable)
                .map(dataMapper::toDomain);
    }

	@Transactional(readOnly = true)
	public Page<AccountingCalendar> findByRange(String idEnterprise, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByIdEnterpriseAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
                idEnterprise, startDate, endDate, pageable).map(dataMapper::toDomain);
    }


    @Transactional
    public void delete(Long id, String idEnterprise) {
        AccountingCalendarEntity entity = repository.findByIdAndIdEnterprise(id, idEnterprise)
                .orElseThrow(AccountingCalendarNotFoundException::new);
        repository.delete(entity);
    }
}


