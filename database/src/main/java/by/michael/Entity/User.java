package by.michael.Entity;

public class User {
  private Long id;
  private String name;

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}
