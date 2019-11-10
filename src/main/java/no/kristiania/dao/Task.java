package no.kristiania.dao;

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
}
