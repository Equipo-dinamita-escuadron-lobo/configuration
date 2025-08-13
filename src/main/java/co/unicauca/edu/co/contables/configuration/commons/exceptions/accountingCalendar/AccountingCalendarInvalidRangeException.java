package co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar;

public class AccountingCalendarInvalidRangeException extends RuntimeException {
    private final AccountingCalendarErrorCode code = AccountingCalendarErrorCode.INVALID_RANGE;

    public AccountingCalendarInvalidRangeException() {
        super("La fecha de inicio no puede ser mayor que la fecha de fin");
    }

    public AccountingCalendarErrorCode getCode() {
        return code;
    }
}


