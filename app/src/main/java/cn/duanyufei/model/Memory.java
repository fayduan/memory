package cn.duanyufei.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Calendar;
import java.util.Date;

@Entity
public class Memory {
    @Id(autoincrement = true)
    Long id;
    String text;
    @Transient
    int number;
    Date date;
    @Transient
    int type;
    Integer position;

    public Memory(String text, Date date) {
        this.text = text;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        this.date = cal.getTime();
        long now = cal.getTimeInMillis();
        long sub = now - this.date.getTime();
        //long temp = datetmp.getTime();
        //long sub = temp-date.getTime();
        long b = 1000;
        long l = b * 60 * 60 * 24;
        this.number = (int) (sub / l);
        if (sub >= 0) {
            this.type = 1;
            this.number++;
        }
        if (sub < 0) {
            this.type = 0;
            this.number = Math.abs(this.number);
            this.number++;
        }
    }

    public Memory(Long id, String text, Date date) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.position = 0;
    }

    @Generated(hash = 64278047)
    public Memory(Long id, String text, Date date, Integer position) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.position = position;
    }

    @Generated(hash = 884616065)
    public Memory() {
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

    public int getNumber() {
        return this.number;
    }

    public void update() {
        long now = new Date().getTime();
        long sub = now - this.date.getTime();
        //long temp = datetmp.getTime();
        //long sub = temp-date.getTime();
        long b = 1000;
        long l = b * 60 * 60 * 24;
        this.number = (int) (sub / l);
        if (sub >= 0) {
            this.type = 1;
            this.number++;
        }
        if (sub < 0) {
            this.type = 0;
            this.number = Math.abs(this.number);
            this.number++;
        }
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
