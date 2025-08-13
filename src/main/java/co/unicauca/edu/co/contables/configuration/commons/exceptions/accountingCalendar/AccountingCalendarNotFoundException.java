package co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar;

public class AccountingCalendarNotFoundException extends RuntimeException {
    private final AccountingCalendarErrorCode code = AccountingCalendarErrorCode.NOT_FOUND;

    public AccountingCalendarNotFoundException() {
        super("Calendario contable no encontrado");
    }

    public AccountingCalendarErrorCode getCode() {
        return code;
    }
}


