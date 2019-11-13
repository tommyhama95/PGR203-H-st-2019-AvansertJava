package no.kristiania.dao.objects;

import java.util.Objects;

public class ProjectMember {


    private long projectID;
    private long userID;

    public  ProjectMember(){}

    public ProjectMember(long userID, long projectID) {
        this.projectID = projectID;
        this.userID = userID;
    }

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

    //Show how Project_user object is returned in String form
    @Override
    public String toString() {
        return "ProjectMember{" +
                "projectID=" + projectID +
                ", userID=" + userID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectMember that = (ProjectMember) o;
        return projectID == that.projectID &&
                userID == that.userID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectID, userID);
    }
}
