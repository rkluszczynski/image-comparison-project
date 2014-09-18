package pl.info.rkluszczynski.image.standalone.db.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.info.rkluszczynski.image.standalone.db.entities.EvaluationEntity;

import java.util.List;

public interface EvaluationRepository extends CrudRepository<EvaluationEntity, Long> {

    List<EvaluationEntity> findByStatus(int status);

}
