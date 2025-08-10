package co.unicauca.edu.co.contables.configuration.noCommercialTags.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    private Long id;
    private String title;
    private String description;
    
}
