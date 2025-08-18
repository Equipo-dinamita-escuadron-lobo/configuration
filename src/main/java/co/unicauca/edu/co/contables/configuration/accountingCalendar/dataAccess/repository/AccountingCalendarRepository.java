package co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.repository;

import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.entity.AccountingCalendarEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.time.LocalDate;

public interface AccountingCalendarRepository extends JpaRepository<AccountingCalendarEntity, Long> {

    boolean existsByIdEnterpriseAndDate(String idEnterprise, LocalDate date);

    Optional<AccountingCalendarEntity> findByIdAndIdEnterprise(Long id, String idEnterprise);

    Page<AccountingCalendarEntity> findAllByIdEnterpriseAndStatusAndDateBetweenOrderByDateAsc(
            String idEnterprise, boolean status, LocalDate startOfYear, LocalDate endOfYear, Pageable pageable);

    long deleteByIdEnterpriseAndDateBetween(
            String idEnterprise, LocalDate startDate, LocalDate endDate);

}


