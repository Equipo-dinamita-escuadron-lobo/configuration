package co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.services;

import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.entity.AccountingCalendarEntity;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.mapper.AccountingCalendarDataMapper;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.repository.AccountingCalendarRepository;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.mapper.AccountingCalendarDomainMapper;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.models.AccountingCalendar;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.request.*;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar.AccountingCalendarDateExistsException;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar.AccountingCalendarNotFoundException;
import co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar.AccountingCalendarInvalidDateException;
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
import java.util.List;
import java.util.stream.Collectors;

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
        // Validar que la fecha sea después del año 2000
        if (request.getDate().getYear() < 2000) {
            throw new AccountingCalendarInvalidDateException();
        }
        
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
    public List<AccountingCalendar> openMonthBatch(AccountingCalendarCreateMonthReq request) {
        YearMonth ym = YearMonth.of(request.getYear(), request.getMonth());
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        
        // 1. Generar todas las fechas del mes
        List<LocalDate> datesToCreate = generateDateRange(start, end);
        
        // 2. Buscar fechas que ya existen
        List<AccountingCalendarEntity> existingEntities = repository.findAllByIdEnterpriseAndDateBetween(
                request.getIdEnterprise(), start, end);
        List<LocalDate> existingDates = existingEntities.stream()
                .map(AccountingCalendarEntity::getDate)
                .collect(Collectors.toList());
        
        // 3. Filtrar fechas que no existen
        List<LocalDate> newDates = datesToCreate.stream()
                .filter(date -> !existingDates.contains(date))
                .collect(Collectors.toList());
        
        // 4. Crear entidades en batch
        List<AccountingCalendarEntity> entities = newDates.stream()
                .map(date -> createEntity(request.getIdEnterprise(), date, request.getStatus()))
                .collect(Collectors.toList());
        
        // 5. Insertar en batch
        List<AccountingCalendarEntity> saved = repository.saveAll(entities);
        
        return saved.stream()
                .map(dataMapper::toDomain)
                .collect(Collectors.toList());
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
    public List<AccountingCalendar> openYearBatch(AccountingCalendarCreateYearReq request) {
        LocalDate start = LocalDate.of(request.getYear(), 1, 1);
        LocalDate end = LocalDate.of(request.getYear(), 12, 31);
        
        // 1. Generar todas las fechas del año
        List<LocalDate> datesToCreate = generateDateRange(start, end);
        
        // 2. Buscar fechas que ya existen
        List<AccountingCalendarEntity> existingEntities = repository.findAllByIdEnterpriseAndDateBetween(
                request.getIdEnterprise(), start, end);
        List<LocalDate> existingDates = existingEntities.stream()
                .map(AccountingCalendarEntity::getDate)
                .collect(Collectors.toList());
        
        // 3. Filtrar fechas que no existen
        List<LocalDate> newDates = datesToCreate.stream()
                .filter(date -> !existingDates.contains(date))
                .collect(Collectors.toList());
        
        // 4. Crear entidades en batch
        List<AccountingCalendarEntity> entities = newDates.stream()
                .map(date -> createEntity(request.getIdEnterprise(), date, request.getStatus()))
                .collect(Collectors.toList());
        
        // 5. Insertar en batch
        List<AccountingCalendarEntity> saved = repository.saveAll(entities);
        
        return saved.stream()
                .map(dataMapper::toDomain)
                .collect(Collectors.toList());
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

    // Métodos auxiliares privados
    private List<LocalDate> generateDateRange(LocalDate start, LocalDate end) {
        return start.datesUntil(end.plusDays(1))
                .collect(Collectors.toList());
    }

    private AccountingCalendarEntity createEntity(String idEnterprise, LocalDate date, boolean status) {
        return AccountingCalendarEntity.builder()
                .idEnterprise(idEnterprise)
                .date(date)
                .status(status)
                .build();
    }

}


