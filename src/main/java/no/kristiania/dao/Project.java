package no.kristiania.dao;

import java.util.Objects;

public class Project {
  private String name;

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

  @Override
  public String toString() {
    return "Project{" +
            "name='" + name + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Project project = (Project) o;
    return Objects.equals(name, project.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
