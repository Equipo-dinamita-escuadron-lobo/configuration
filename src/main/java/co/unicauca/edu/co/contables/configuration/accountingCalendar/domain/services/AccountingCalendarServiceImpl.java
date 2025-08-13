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
import java.util.ArrayList;
import java.util.List;
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

	@Transactional
	public AccountingCalendar update(AccountingCalendarUpdateReq request) {
        AccountingCalendarEntity current = repository.findById(request.getId())
                .orElseThrow(AccountingCalendarNotFoundException::new);

		if (request.getIdEnterprise() != null) current.setIdEnterprise(request.getIdEnterprise());
        if (request.getStartDate() != null) current.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) current.setEndDate(request.getEndDate());
        if (request.getStatus() != null) current.setStatus(request.getStatus());

        // Validación rango fecha inicio < fecha fin
        if (current.getStartDate().isAfter(current.getEndDate())) {
            throw new AccountingCalendarInvalidRangeException();
        }

        // Validar solapamiento en update si cambian fechas o empresa
        String targetEnterprise = request.getIdEnterprise() != null ? request.getIdEnterprise() : current.getIdEnterprise();
        boolean startChanged = request.getStartDate() != null;
        boolean endChanged = request.getEndDate() != null;
        if (startChanged || endChanged) {
            LocalDate newStart = startChanged ? request.getStartDate() : current.getStartDate();
            LocalDate newEnd = endChanged ? request.getEndDate() : current.getEndDate();
            boolean overlap = repository.existsByIdEnterpriseAndIdNotAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                    targetEnterprise, current.getId(), newEnd, newStart);
            if (overlap) {
                throw new AccountingCalendarOverlapException();
            }
        }

		return dataMapper.toDomain(repository.save(current));
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

    @Transactional(readOnly = true)
    public Page<AccountingCalendar> findByYear(String idEnterprise, int year, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByEnterpriseAndYear(idEnterprise, year, pageable).map(dataMapper::toDomain);
    }

	@Transactional
	public void changeStateAll(String idEnterprise, Boolean status) {
		int page = 0;
		Page<AccountingCalendarEntity> result;
		do {
			result = repository.findAllByIdEnterprise(idEnterprise, PageRequest.of(page, 500));
			List<AccountingCalendarEntity> toUpdate = new ArrayList<>();
			for (AccountingCalendarEntity entity : result.getContent()) {
				if (entity.isStatus() != status) {
					entity.setStatus(status);
					toUpdate.add(entity);
				}
			}
			if (!toUpdate.isEmpty()) {
				repository.saveAll(toUpdate);
				entityManager.flush();
				entityManager.clear();
			}
			page++;
		} while (result.hasNext());
	}

	@Transactional
	public void changeStateRange(AccountingCalendarRangeStateReq request) {
		int page = 0;
		Page<AccountingCalendarEntity> result;
		do {
			result = repository.findAllByIdEnterpriseAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
					request.getIdEnterprise(), request.getStartDate(), request.getEndDate(), PageRequest.of(page, 500));
			List<AccountingCalendarEntity> toUpdate = new ArrayList<>();
			for (AccountingCalendarEntity entity : result.getContent()) {
				if (entity.isStatus() != request.getStatus()) {
					entity.setStatus(request.getStatus());
					toUpdate.add(entity);
				}
			}
			if (!toUpdate.isEmpty()) {
				repository.saveAll(toUpdate);
				entityManager.flush();
				entityManager.clear();
			}
			page++;
		} while (result.hasNext());
	}

    

    @Transactional
    public void delete(Long id, String idEnterprise) {
        AccountingCalendarEntity entity = repository.findByIdAndIdEnterprise(id, idEnterprise)
                .orElseThrow(AccountingCalendarNotFoundException::new);
        repository.delete(entity);
    }
}


