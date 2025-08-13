package co.unicauca.edu.co.contables.configuration.costCenters.presentation.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostCenterUpdateReq {

    @NotNull
    private Long id;

    @NotBlank
    private String idEnterprise;

    @NotNull(message = "El codigo es obligatorio")
    @Positive(message = "El codigo debe ser un número positivo")
    private Integer code;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "El nombre solo puede contener letras")
    private String name;

    private Long parentId; // opcional

    @AssertTrue(message = "El codigo debe tener un longitud de 2, 4 o 6 digitos.")
    public boolean isCodeLengthValid() {
        if (code == null) {
            return false;
        }
        int length = String.valueOf(Math.abs(code)).length();
        return length == 2 || length == 4 || length == 6;
    }
}


