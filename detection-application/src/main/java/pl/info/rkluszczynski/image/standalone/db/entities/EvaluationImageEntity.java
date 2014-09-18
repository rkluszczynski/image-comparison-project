package pl.info.rkluszczynski.image.standalone.db.entities;

import javax.persistence.*;

@Entity(name = "evaluationimage")
public class EvaluationImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "evaluation_id")
    private Long evaluationId;
    @Column(name = "path")
    private String sourcePath;
    @Column(name = "processedpath")
    private String processedPath;
}
