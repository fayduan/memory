package cn.duanyufei.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by fayduan on 2019/1/15.
 */
@Entity
public class Plan {

    @Id(autoincrement = true)
    Long id;
    String text;
    Date date;
    boolean isDone;
    int position;
    @Generated(hash = 231979778)
    public Plan(Long id, String text, Date date, boolean isDone, int position) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.isDone = isDone;
        this.position = position;
    }
    @Generated(hash = 592612124)
    public Plan() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public boolean getIsDone() {
        return this.isDone;
    }
    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }
    public int getPosition() {
        return this.position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
}
