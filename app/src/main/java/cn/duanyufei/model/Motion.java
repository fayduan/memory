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
    int groups;
    int number;
    @Transient
    int curWeight;

    public Motion(String text, int groups, int number) {
        this.text = text;
        this.groups = groups;
        this.number = number;
    }

    public Motion(String text, int groups, int number, int curWeight) {
        this.text = text;
        this.groups = groups;
        this.number = number;
        this.curWeight = curWeight;
    }

    @Generated(hash = 499522415)
    public Motion() {
    }

    @Generated(hash = 1970223015)
    public Motion(Long id, String text, int groups, int number) {
        this.id = id;
        this.text = text;
        this.groups = groups;
        this.number = number;
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

    public int getGroups() {
        return this.groups;
    }

    public void setGroups(int groups) {
        this.groups = groups;
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

    @Override
    public String toString() {
        return "Motion{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", groups=" + groups +
                ", number=" + number +
                ", curWeight=" + curWeight +
                '}';
    }
}
