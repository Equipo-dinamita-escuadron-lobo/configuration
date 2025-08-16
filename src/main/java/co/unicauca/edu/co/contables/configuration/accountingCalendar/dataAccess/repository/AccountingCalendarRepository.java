package co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.repository;

import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.entity.AccountingCalendarEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.time.LocalDate;

public interface AccountingCalendarRepository extends JpaRepository<AccountingCalendarEntity, Long> {

    Page<AccountingCalendarEntity> findAllByIdEnterprise(String idEnterprise, Pageable pageable);

    Page<AccountingCalendarEntity> findAllByIdEnterpriseAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
            String idEnterprise, LocalDate startDate, LocalDate endDate, Pageable pageable);

    default Page<AccountingCalendarEntity> findAllByEnterpriseAndYear(String idEnterprise, int year, Pageable pageable) {
        LocalDate yearStart = LocalDate.of(year, 1, 1);
        LocalDate yearEnd = LocalDate.of(year, 12, 31);
        return findAllByIdEnterpriseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(idEnterprise, yearEnd, yearStart, pageable);
    }

    Page<AccountingCalendarEntity> findAllByIdEnterpriseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String idEnterprise, LocalDate endDate, LocalDate startDate, Pageable pageable);

    boolean existsByIdEnterpriseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(String idEnterprise, LocalDate endDate, LocalDate startDate);

    Optional<AccountingCalendarEntity> findByIdAndIdEnterprise(Long id, String idEnterprise);
}


