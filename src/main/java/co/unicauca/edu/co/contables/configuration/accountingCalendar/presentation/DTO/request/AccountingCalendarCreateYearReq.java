package co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountingCalendarCreateYearReq {

    @NotBlank
    private String idEnterprise;

    @Min(2000)
    private int year;

    @NotNull
    private Boolean status; // true: OPEN, false: CLOSED
}



