package co.unicauca.edu.co.contables.configuration.noCommercialTags.presentation.DTO.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagDTORequest {
    @JsonIgnore
    private Long id;
    private String title;
    private String description;
    
}
