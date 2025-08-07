package sideprojects.dreamdecoder.infrastructure.persistence.dream;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamEntity;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamRepository;
import sideprojects.dreamdecoder.domain.dream.util.mapper.DreamMapper;

@Repository
@RequiredArgsConstructor
public class DreamRepositoryImpl implements DreamRepository {

    private final DreamJpaRepository dreamJpaRepository;
    private final DreamMapper dreamMapper;

    @Override
    public Dream save(Dream dream) {
        DreamEntity dreamEntity = dreamMapper.toEntity(dream);
        DreamEntity savedEntity = dreamJpaRepository.save(dreamEntity);
        return dreamMapper.toModel(savedEntity);
    }
}
