package pl.info.rkluszczynski.image.standalone.db.entities;

import javax.persistence.*;
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
}
