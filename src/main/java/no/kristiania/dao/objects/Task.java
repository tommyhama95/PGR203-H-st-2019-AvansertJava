package no.kristiania.dao.objects;

import java.util.Objects;

public class Task {
    private String name;
    private long id;
    private String status;
    private long projectId;

    public Task() {}

    public Task(String name, String status, long projectId) {
        this.name = name;
        this.status = status;
        this.projectId = projectId;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
    public long getProjectId() {
        return projectId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", projectId=" + projectId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                projectId == task.projectId &&
                Objects.equals(name, task.name) &&
                Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, status, projectId);
    }
}

