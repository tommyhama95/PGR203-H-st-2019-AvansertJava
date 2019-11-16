package no.kristiania.dao.objects;

import java.util.Objects;

public class ProjectMember {

    private long projectId;
    private long userId;

    public  ProjectMember(){}

    public ProjectMember(long userId, long projectId) {
        this.projectId = projectId;
        this.userId = userId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
    public long getProjectId() {
        return projectId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    public long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "ProjectMember{" +
                "projectId=" + projectId +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectMember that = (ProjectMember) o;
        return projectId == that.projectId &&
                userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, userId);
    }
}
