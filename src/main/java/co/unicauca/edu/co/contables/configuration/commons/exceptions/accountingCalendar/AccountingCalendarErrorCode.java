package co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.ErrorCodeDefinition;
import lombok.Getter;

@Getter
public enum AccountingCalendarErrorCode implements ErrorCodeDefinition {
    OVERLAPPING_PERIODS("ACCOUNTING_CALENDAR_OVERLAPPING_PERIODS", "El rango de fechas se solapa con periodos existentes"),
    INVALID_RANGE("ACCOUNTING_CALENDAR_INVALID_RANGE", "La fecha de inicio no puede ser mayor que la fecha de fin"),
    NOT_FOUND("ACCOUNTING_CALENDAR_NOT_FOUND", "Calendario contable no encontrado");

    private final String code;
    private final String message;

    AccountingCalendarErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}


 