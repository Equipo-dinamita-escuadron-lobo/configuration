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
public class AccountingCalendarCreateReq {

    @NotBlank
    private String idEnterprise;

    @NotNull
    private LocalDate date;

    private Boolean status; // true: OPEN, false: CLOSED (default false)

    // Validación personalizada para fecha mínima
    public boolean isValidDate() {
        return date != null && date.getYear() >= 2000;
    }
}


