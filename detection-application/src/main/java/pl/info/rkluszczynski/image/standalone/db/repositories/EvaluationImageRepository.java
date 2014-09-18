package pl.info.rkluszczynski.image.standalone.db.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.info.rkluszczynski.image.standalone.db.entities.EvaluationImageEntity;

import java.util.List;

public interface EvaluationImageRepository extends CrudRepository<EvaluationImageEntity, Long> {

    List<EvaluationImageEntity> findByEvaluationId(Long evaluationId);

}
