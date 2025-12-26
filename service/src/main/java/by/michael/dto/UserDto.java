package by.michael.dto;

import java.util.Objects;

public class UserDto {
  private String name;
  private int age;
  private String phoneNumber;
  private String login;
  private String password;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    UserDto userDto = (UserDto) o;
    return age == userDto.age && Objects.equals(name, userDto.name) && Objects.equals(phoneNumber, userDto.phoneNumber) && Objects.equals(login, userDto.login) && Objects.equals(password, userDto.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, age, phoneNumber, login, password);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
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

  public UserDto(String name, int age, String phoneNumber, String login, String password) {
    this.name = name;
    this.age = age;
    this.phoneNumber = phoneNumber;
    this.login = login;
    this.password = password;
  }
}
