package pl.info.rkluszczynski.image.standalone.db.old;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProcessedImageRepository extends CrudRepository<ProcessedImageEntity, Long> {

    List<ProcessedImageEntity> findByStatus(int status);
}
