package pl.info.rkluszczynski.image.standalone.db.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.info.rkluszczynski.image.standalone.db.entities.ProcessedImageEntity;

public interface ProcessedImageRepository extends CrudRepository<ProcessedImageEntity, Long> {
}
