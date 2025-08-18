package co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.BaseBusinessException;

public class AccountingCalendarInvalidDateException extends BaseBusinessException {

    public AccountingCalendarInvalidDateException() {
        super(AccountingCalendarErrorCode.INVALID_DATE);
    }
}
