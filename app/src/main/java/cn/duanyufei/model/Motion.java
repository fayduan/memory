package cn.duanyufei.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class Motion {
    @Id(autoincrement = true)
    Long id;
    String text;
    int group;
    int number;
    @Transient
    int curWeight;

    @Generated(hash = 408308330)
    public Motion(Long id, String text, int group, int number) {
        this.id = id;
        this.text = text;
        this.group = group;
        this.number = number;
    }

    @Generated(hash = 499522415)
    public Motion() {
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

    public int getGroup() {
        return this.group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCurWeight() {
        return curWeight;
    }

    public void setCurWeight(int curWeight) {
        this.curWeight = curWeight;
    }
}
