package no.kristiania.dao;

import java.util.Objects;

public class ProjectUser {


    private long projectID;
    private long userID;

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    public long getProjectID() {
        return projectID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "ProjectUser{" +
                "projectID=" + projectID +
                ", userID=" + userID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectUser that = (ProjectUser) o;
        return projectID == that.projectID &&
                userID == that.userID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectID, userID);
    }
}
