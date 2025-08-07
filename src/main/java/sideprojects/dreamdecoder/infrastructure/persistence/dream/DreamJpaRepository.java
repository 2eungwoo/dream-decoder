package sideprojects.dreamdecoder.infrastructure.persistence.dream;

import org.springframework.data.jpa.repository.JpaRepository;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamEntity;

public interface DreamJpaRepository extends JpaRepository<DreamEntity, Long> {
}
