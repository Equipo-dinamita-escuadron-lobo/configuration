package co.unicauca.edu.co.contables.configuration.noCommercialTags.domain.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.unicauca.edu.co.contables.configuration.noCommercialTags.dataAccess.entity.TagEntity;
import co.unicauca.edu.co.contables.configuration.noCommercialTags.dataAccess.mapper.ITagEntityMapper;
import co.unicauca.edu.co.contables.configuration.noCommercialTags.dataAccess.repository.ITagRepository;
import co.unicauca.edu.co.contables.configuration.noCommercialTags.domain.models.Tag;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements ITagService {
    private final ITagRepository tagRepository;
    private final  ITagEntityMapper tagEntityMapper;

    @Override
    public Tag create(Tag tag) {
        TagEntity tagEntity=tagEntityMapper.toEntity(tag);
        return tagEntityMapper.toDomain(tagRepository.save(tagEntity));

    }

    @Override
    public Tag update(Long idTag,Tag tag) {
        TagEntity tagEntity=tagRepository.getReferenceById(idTag);
        tagEntity.setTitle(tag.getTitle());
        tagEntity.setDescription(tag.getDescription());
        return tagEntityMapper.toDomain(tagRepository.save(tagEntity));
    }

    @Override
    public Optional<Tag> getTag(Long idTag) {
        return tagRepository.findById(idTag).map(tagEntityMapper::toDomain);
    }

    @Override
    public List<Tag> getAllTag() {
        return tagEntityMapper.listToDomain(tagRepository.findAll());
    }

    @Override
    public boolean delete(Long idTag) {
        boolean exist=false;
        TagEntity tagEntity=tagRepository.getReferenceById(idTag);
        if(tagEntity!=null){
            tagRepository.deleteById(idTag);
            exist=true;
        }else{


        }
        return exist;
    }
    
}
