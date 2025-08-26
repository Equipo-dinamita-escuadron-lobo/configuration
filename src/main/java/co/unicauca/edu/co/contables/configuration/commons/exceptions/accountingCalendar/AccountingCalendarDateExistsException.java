package co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.BaseBusinessException;

public class AccountingCalendarDateExistsException extends BaseBusinessException {

    public AccountingCalendarDateExistsException() {
        super(AccountingCalendarErrorCode.DATE_ALREADY_EXISTS);
    }
}
