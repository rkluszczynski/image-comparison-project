package pl.info.rkluszczynski.image.standalone.db.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.info.rkluszczynski.image.standalone.db.entities.PlanogramEntity;

public interface PlanogramRepository extends CrudRepository<PlanogramEntity, Long> {
}
