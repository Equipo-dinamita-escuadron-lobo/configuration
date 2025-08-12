package co.unicauca.edu.co.contables.configuration.classesOfDocuments.presentation.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentClassUpdateReq {
    @NotNull(message = "El id es obligatorio")
    private Long id;

    @NotBlank(message = "La empresa es obligatoria")
    private String idEnterprise;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "El nombre solo puede contener letras")
    private String name;
}


