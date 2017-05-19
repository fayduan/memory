package cn.duanyufei.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

@Entity
public class Motion implements Serializable{

    static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    Long id;
    String text;
    int groups;
    int number;
    @Transient
    int curWeight;
    int pos;
    String part;

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

    public Motion(String text, int groups, int number, int curWeight, int pos) {
        this.text = text;
        this.groups = groups;
        this.number = number;
        this.curWeight = curWeight;
        this.pos = pos;
    }

    public Motion(String text, int groups, int number, int curWeight, int pos, String part) {
        this.text = text;
        this.groups = groups;
        this.number = number;
        this.curWeight = curWeight;
        this.pos = pos;
        this.part = part;
    }

    @Generated(hash = 499522415)
    public Motion() {
    }

    @Generated(hash = 579024221)
    public Motion(Long id, String text, int groups, int number, int pos,
            String part) {
        this.id = id;
        this.text = text;
        this.groups = groups;
        this.number = number;
        this.pos = pos;
        this.part = part;
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

    public int getPos() {
        return this.pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getPart() {
        return this.part;
    }

    public void setPart(String part) {
        this.part = part;
    }
}
