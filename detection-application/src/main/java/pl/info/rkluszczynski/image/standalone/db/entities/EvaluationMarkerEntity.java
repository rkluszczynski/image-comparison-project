package pl.info.rkluszczynski.image.standalone.db.entities;

import javax.persistence.*;

@Entity(name = "evaluationmarker")
public class EvaluationMarkerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "evaluation_id")
    private Long evaluationId;
    private String path;
    @Column(name = "tobefound")
    private Long toBeFound;

    private Long found;
    private String color;
}
