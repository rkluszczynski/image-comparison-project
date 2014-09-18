package pl.info.rkluszczynski.image.standalone.db.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.info.rkluszczynski.image.standalone.db.entities.EvaluationImageEntity;

public interface EvaluationImageRepository extends CrudRepository<EvaluationImageEntity, Long> {
}
