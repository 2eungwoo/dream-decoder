package sideprojects.dreamdecoder.infrastructure.persistence.dream;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamEntity;

public interface DreamJpaRepository extends JpaRepository<DreamEntity, Long> {
    DreamEntity save(Dream dream);
    List<DreamEntity> findAll();
    Optional<DreamEntity> findById(Long id);
    void deleteById(Long dreamId);
}
