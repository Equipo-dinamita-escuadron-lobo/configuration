package co.unicauca.edu.co.contables.configuration.accountingCalendar.presentation.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountingCalendarUpdateReq {

    @NotNull
    private Long id;

    private String idEnterprise;
    private String tenantId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean status; // true: OPEN, false: CLOSED
}


