package pl.info.rkluszczynski.image.standalone.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "processedimage")
public class ProcessedImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String sourcepath;
    private String processedpath;

    private Date adddate;
    private Date startdate;
    private Date enddate;

    private int status;
    private Integer result;
    private String fullstatus;


    public ProcessedImageEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSourcepath() {
        return sourcepath;
    }

    public void setSourcepath(String sourcepath) {
        this.sourcepath = sourcepath;
    }

    public String getProcessedpath() {
        return processedpath;
    }

    public void setProcessedpath(String processedpath) {
        this.processedpath = processedpath;
    }

    public Date getAdddate() {
        return adddate;
    }

    public void setAdddate(Date adddate) {
        this.adddate = adddate;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getFullstatus() {
        return fullstatus;
    }

    public void setFullstatus(String fullstatus) {
        this.fullstatus = fullstatus;
    }

    @Override
    public String toString() {
        return String.format("ProcessedImageEntity{id=%d, sourcepath='%s', processedpath='%s', " +
                        "adddate=%s, startdate=%s, enddate=%s, status=%d, result=%d, fullstatus='%s'}",
                id, sourcepath, processedpath, adddate, startdate, enddate, status, result, fullstatus);
    }
}
