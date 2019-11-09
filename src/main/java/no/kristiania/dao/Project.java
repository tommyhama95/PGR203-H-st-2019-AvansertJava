package no.kristiania.dao;

import java.util.Objects;

public class Project {
    private String name;
    private long id;

    public Project() {}
    public Project(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    //Show how the project object is returned in String form
    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id == project.id &&
                Objects.equals(name, project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}
