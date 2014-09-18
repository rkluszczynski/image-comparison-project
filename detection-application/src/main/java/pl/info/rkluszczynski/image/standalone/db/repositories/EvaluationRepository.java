package pl.info.rkluszczynski.image.standalone.db.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.info.rkluszczynski.image.standalone.db.entities.EvaluationEntity;

public interface EvaluationRepository extends CrudRepository<EvaluationEntity, Long> {
}
