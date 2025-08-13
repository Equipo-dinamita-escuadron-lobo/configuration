package co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.BaseBusinessException;

public class AccountingCalendarOverlapException extends BaseBusinessException {

    public AccountingCalendarOverlapException() {
        super(AccountingCalendarErrorCode.OVERLAPPING_PERIODS);
    }
}


 