package pl.info.rkluszczynski.image.standalone.db.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.info.rkluszczynski.image.standalone.db.entities.ProcessedImageEntity;

import java.util.List;

public interface ProcessedImageRepository extends CrudRepository<ProcessedImageEntity, Long> {

    List<ProcessedImageEntity> findByStatus(int status);
}
