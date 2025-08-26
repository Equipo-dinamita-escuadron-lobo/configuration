package co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.mapper;

import co.unicauca.edu.co.contables.configuration.accountingCalendar.domain.models.AccountingCalendar;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.request.*;
import co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.response.AccountingCalendarRes;
import org.mapstruct.Mapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountingCalendarDomainMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    AccountingCalendar toDomain(AccountingCalendarCreateReq req);

    AccountingCalendarRes toRes(AccountingCalendar domain);

    @AfterMapping
    default void setDefaultStatus(AccountingCalendarCreateReq req, @MappingTarget AccountingCalendar target) {
        if (req.getStatus() == null) {
            target.setStatus(false);
        }
    }
}


