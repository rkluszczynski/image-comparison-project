package pl.info.rkluszczynski.image.standalone.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

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

    public Integer getColor() {
        return color;
    }

    @Override
    public String toString() {
        return String.format("EvaluationMarker{id=%d, evaluationId=%d, path='%s', toBeFound=%d, found=%d, color=%d}",
                id, evaluationId, path, toBeFound, found, color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, evaluationId, path, toBeFound, found, color);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final EvaluationMarkerEntity other = (EvaluationMarkerEntity) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.evaluationId, other.evaluationId)
                && Objects.equals(this.path, other.path)
                && Objects.equals(this.toBeFound, other.toBeFound)
                && Objects.equals(this.found, other.found)
                && Objects.equals(this.color, other.color);
    }
}
