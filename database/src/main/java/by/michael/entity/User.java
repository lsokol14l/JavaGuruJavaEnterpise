package by.michael.entity;

public class User {
  private String name;
  private String login;
  private String phoneNumber;
  private String password;

  public User(String name, String login, String phoneNumber, String password) {
    this.name = name;
    this.login = login;
    this.phoneNumber = phoneNumber;
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
