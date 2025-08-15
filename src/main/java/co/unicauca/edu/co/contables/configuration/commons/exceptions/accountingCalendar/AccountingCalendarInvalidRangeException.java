package co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.BaseBusinessException;

public class AccountingCalendarInvalidRangeException extends BaseBusinessException {

    public AccountingCalendarInvalidRangeException() {
        super(AccountingCalendarErrorCode.INVALID_RANGE);
    }
}


 