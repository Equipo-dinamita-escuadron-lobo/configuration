package co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar;

public class AccountingCalendarOverlapException extends RuntimeException {
    private final AccountingCalendarErrorCode code = AccountingCalendarErrorCode.OVERLAPPING_PERIODS;

    public AccountingCalendarOverlapException() {
        super("El rango de fechas se solapa con periodos existentes");
    }

    public AccountingCalendarErrorCode getCode() {
        return code;
    }
}


