package pl.info.rkluszczynski.image.standalone.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private Integer color;

    public String getPath() {
        return path;
    }

    public Long getToBeFound() {
        return toBeFound;
    }

    @Override
    public String toString() {
        return String.format("EvaluationMarker{id=%d, evaluationId=%d, path='%s', toBeFound=%d, found=%d, color=%d}",
                id, evaluationId, path, toBeFound, found, color);
    }
}
