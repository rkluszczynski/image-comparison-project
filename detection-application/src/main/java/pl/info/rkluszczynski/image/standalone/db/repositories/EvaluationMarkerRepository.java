package pl.info.rkluszczynski.image.standalone.db.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.info.rkluszczynski.image.standalone.db.entities.EvaluationMarkerEntity;

public interface EvaluationMarkerRepository extends CrudRepository<EvaluationMarkerEntity, Long> {
}
