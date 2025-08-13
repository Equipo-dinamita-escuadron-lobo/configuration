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
public class AccountingCalendarRangeStateReq {
    @NotBlank
    private String idEnterprise;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private Boolean status; // true: OPEN, false: CLOSED
}


