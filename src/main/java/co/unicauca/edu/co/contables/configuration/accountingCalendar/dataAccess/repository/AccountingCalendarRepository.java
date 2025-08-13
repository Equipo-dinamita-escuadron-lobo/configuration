package co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.repository;

import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.entity.AccountingCalendarEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AccountingCalendarRepository extends JpaRepository<AccountingCalendarEntity, Long> {

    Page<AccountingCalendarEntity> findAllByIdEnterprise(String idEnterprise, Pageable pageable);

    Page<AccountingCalendarEntity> findAllByIdEnterpriseAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
            String idEnterprise, LocalDate startDate, LocalDate endDate, Pageable pageable);

    boolean existsByIdEnterpriseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(String idEnterprise, LocalDate endDate, LocalDate startDate);

    boolean existsByIdEnterpriseAndIdNotAndStartDateLessThanEqualAndEndDateGreaterThanEqual(String idEnterprise, Long id, LocalDate endDate, LocalDate startDate);
}


