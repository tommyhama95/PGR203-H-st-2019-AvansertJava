package no.kristiania.dao;

import java.util.Objects;

public class TaskMember {


    private long tID;
    private long uID;

    public TaskMember() {
    }

    public TaskMember(long tID, long uID) {
        this.tID = tID;
        this.uID = uID;
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
