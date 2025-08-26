package co.unicauca.edu.co.contables.configuration.commons.exceptions.accountingCalendar;

import co.unicauca.edu.co.contables.configuration.commons.exceptions.BaseBusinessException;

public class AccountingCalendarNotFoundException extends BaseBusinessException {

    public AccountingCalendarNotFoundException() {
        super(AccountingCalendarErrorCode.NOT_FOUND);
    }
}


 