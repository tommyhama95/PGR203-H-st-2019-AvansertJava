package no.kristiania.dao;

import java.util.Objects;

public class Task {
    private String name;
    private Object id;
    private String status;
    private long projectID;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    public long getProjectID() {
        return projectID;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", projectID=" + projectID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return projectID == task.projectID &&
                Objects.equals(name, task.name) &&
                Objects.equals(id, task.id) &&
                Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, status, projectID);
    }
}

