package com.haduart.core;

import com.haduart.dto.ViewDTO;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "views")
public class View implements ViewDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long profileId;
    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ts;

    public View() {
    }

    public View(Long profileId, Long userId, Date ts) {
        this.profileId = profileId;
        this.userId = userId;
        this.ts = ts;
    }

    public View(Long userId, Date ts) {
        this.userId = userId;
        this.ts = ts;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTimeStamp() {
        return new DateTime(ts).toString();
    }

    public void setTimeStamp(String ts) {
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        View view = (View) o;

        if (id != view.id) return false;
        if (profileId != view.profileId) return false;
        if (userId != view.userId) return false;
        if (!ts.equals(view.ts)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (profileId ^ (profileId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + ts.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", profileId: " + profileId + ", userId: " + userId + ", ts: " + ts.toString() + " }";
    }
}
