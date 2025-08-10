package co.unicauca.edu.co.contables.configuration.noCommercialTags.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.unicauca.edu.co.contables.configuration.noCommercialTags.domain.models.Tag;
import co.unicauca.edu.co.contables.configuration.noCommercialTags.presentation.DTO.request.TagDTORequest;
import co.unicauca.edu.co.contables.configuration.noCommercialTags.presentation.DTO.response.TagDTOResponse;

@Mapper(componentModel = "spring")
public interface ITagDomainMapper {
    
    Tag toDomain(TagDTORequest tagDTORequest);
    TagDTOResponse toResponse(Tag tag);

    List<TagDTOResponse> toReponse(List<Tag> tags);
    
}
