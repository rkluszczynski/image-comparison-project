package pl.info.rkluszczynski.image.standalone.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "evaluation")
public class EvaluationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "startdate")
    private Date startDate;
    @Column(name = "enddate")
    private Date endDate;

    private int status;
    private Integer result;


    public long getId() {
        return id;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Evaluation{id=%d, startDate=%s, endDate=%s, status=%d, result=%d}",
                id, startDate, endDate, status, result);
    }

    public Date getStartDate() {
        return startDate;
    }
}
