package co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.mapper;

import co.unicauca.edu.co.contables.configuration.accountingCalendar.dataAccess.entity.AccountingCalendarEntity;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.models.AccountingCalendar;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountingCalendarDataMapper {
    AccountingCalendarEntity toEntity(AccountingCalendar domain);
    AccountingCalendar toDomain(AccountingCalendarEntity entity);
}


