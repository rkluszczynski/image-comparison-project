package pl.info.rkluszczynski.image.standalone.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    public String getSourcePath() {
        return sourcePath;
    }

    public String getProcessedPath() {
        return processedPath;
    }

    public void setProcessedPath(String processedPath) {
        this.processedPath = processedPath;
    }

    @Override
    public String toString() {
        return String.format("EvaluationImage{id=%d, evaluationId=%d, sourcePath='%s', processedPath='%s'}",
                id, evaluationId, sourcePath, processedPath);
    }
}
