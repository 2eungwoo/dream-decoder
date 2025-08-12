package sideprojects.dreamdecoder.infrastructure.persistence.dream;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sideprojects.dreamdecoder.application.dream.service.DreamRepository;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamEntity;
import sideprojects.dreamdecoder.domain.dream.util.exception.DreamNotFoundException;
import sideprojects.dreamdecoder.domain.dream.util.mapper.DreamMapper;

import java.util.List;
import java.util.stream.Collectors;

import static sideprojects.dreamdecoder.domain.dream.persistence.QDreamEntity.dreamEntity;

@Repository
@RequiredArgsConstructor
public class DreamRepositoryImpl implements DreamRepository {

    private final DreamJpaRepository dreamJpaRepository;
    private final DreamMapper dreamMapper;
    private final JPAQueryFactory queryFactory;

    @Override
    public DreamModel save(DreamModel dreamModel) {
        DreamEntity dreamEntity = dreamMapper.toEntity(dreamModel);
        DreamEntity savedEntity = dreamJpaRepository.save(dreamEntity);
        return dreamMapper.toModel(savedEntity);
    }

    @Override
    public List<DreamModel> findAll() {
        return queryFactory
                .select(new QDreamProjectionDto(dreamEntity.id, dreamEntity.userId, dreamEntity.dreamContent, dreamEntity.interpretationResult, dreamEntity.aiStyle, dreamEntity.interpretedAt))
                .from(dreamEntity)
                .fetch()
                .stream()
                .map(dto -> DreamModel.builder()
                        .id(dto.id())
                        .userId(dto.userId())
                        .dreamContent(dto.dreamContent())
                        .interpretationResult(dto.interpretationResult())
                        .aiStyle(dto.aiStyle())
                        .interpretedAt(dto.interpretedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public DreamModel findById(Long id) {
        DreamEntity entity = dreamJpaRepository.findById(id)
            .orElseThrow(DreamNotFoundException::new);
        return dreamMapper.toModel(entity);

    }

    @Override
    public DreamModel update(DreamModel updatedDreamModel) {
        return dreamMapper.toModel(dreamJpaRepository.save(updatedDreamModel));
    }

    @Override
    public void deleteById(Long dreamId) {
        dreamJpaRepository.deleteById(dreamId);
    }

}
