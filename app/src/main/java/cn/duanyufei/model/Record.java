package cn.duanyufei.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Record implements Serializable {

    static final long serialVersionUID = 2L;

    @Id(autoincrement = true)
    Long id;
    Long motionId;
    Date date;
    int weight;

    public Record(long motionId, Date date, int weight) {
        this.motionId = motionId;
        this.date = date;
        this.weight = weight;
    }

    @Generated(hash = 477726293)
    public Record() {
    }
    @Generated(hash = 1747412156)
    public Record(Long id, Long motionId, Date date, int weight) {
        this.id = id;
        this.motionId = motionId;
        this.date = date;
        this.weight = weight;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public int getWeight() {
        return this.weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public Long getMotionId() {
        return this.motionId;
    }
    public void setMotionId(Long motionId) {
        this.motionId = motionId;
    }
}
