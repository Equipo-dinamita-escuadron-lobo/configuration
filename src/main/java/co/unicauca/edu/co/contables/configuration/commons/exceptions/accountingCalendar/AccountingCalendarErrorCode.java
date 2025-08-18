package co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.ErrorCodeDefinition;
import lombok.Getter;

@Getter
public enum AccountingCalendarErrorCode implements ErrorCodeDefinition {
    NOT_FOUND("ACCOUNTING_CALENDAR_NOT_FOUND", "Calendario contable no encontrado"),
    DATE_ALREADY_EXISTS("ACCOUNTING_CALENDAR_DATE_EXISTS", "Ya existe una fecha para el día especificado");

    private final String code;
    private final String message;

    AccountingCalendarErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}


 