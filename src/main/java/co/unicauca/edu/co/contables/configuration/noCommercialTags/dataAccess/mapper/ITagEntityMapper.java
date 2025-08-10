package co.unicauca.edu.co.contables.configuration.noCommercialTags.dataAccess.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.unicauca.edu.co.contables.configuration.noCommercialTags.dataAccess.entity.TagEntity;
import co.unicauca.edu.co.contables.configuration.noCommercialTags.domain.models.Tag;

@Mapper(componentModel = "spring")
public interface  ITagEntityMapper {

    TagEntity toEntity(Tag tag);

    Tag toDomain(TagEntity tagEntity);
    
    List<Tag> listToDomain(List<TagEntity> tagEntities);
    
}
