package co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountingCalendarDateStateReq {
    @NotBlank
    private String idEnterprise;
    @NotNull
    private LocalDate date;
    @NotNull
    private Boolean status; // true: OPEN, false: CLOSED
}


