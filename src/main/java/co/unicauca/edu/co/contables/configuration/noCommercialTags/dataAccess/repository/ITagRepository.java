package co.unicauca.edu.co.contables.configuration.noCommercialTags.dataAccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.unicauca.edu.co.contables.configuration.noCommercialTags.dataAccess.entity.TagEntity;

public interface ITagRepository extends JpaRepository<TagEntity,Long>{
    
}
