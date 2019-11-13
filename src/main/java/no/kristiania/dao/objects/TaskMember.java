package no.kristiania.dao.objects;

import java.util.Objects;

public class TaskMember {


    private long tID;
    private long uID;
    private long pId;

    public TaskMember() {
    }

    public TaskMember(long tID, long uID, long pID) {
        this.tID = tID;
        this.uID = uID;
        this.pId = pID;
    }

    public long getuID() {
        return uID;
    }

    public void setuID(long uID) {
        this.uID = uID;
    }

    public long gettID() {
        return tID;
    }

    public void settID(long tID) {
        this.tID = tID;
    }

    public long getpId() {
        return pId;
    }

    public void setpId(long pId) {
        this.pId = pId;
    }

    @Override
    public String toString() {
        return "TaskMember{" +
                "tID=" + tID +
                ", uID=" + uID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskMember that = (TaskMember) o;
        return tID == that.tID &&
                uID == that.uID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tID, uID);
    }

}
