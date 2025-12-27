package by.michael.dto;

import java.util.Objects;

public class UserDto {
  private String name;
  private String login;
  private String phoneNumber;
  private String password;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    UserDto userDto = (UserDto) o;
    return Objects.equals(name, userDto.name)
        && Objects.equals(phoneNumber, userDto.phoneNumber)
        && Objects.equals(login, userDto.login)
        && Objects.equals(password, userDto.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, login, phoneNumber, password);
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

  public UserDto(String name, String login, String phoneNumber, String password) {
    this.name = name;
    this.login = login;
    this.phoneNumber = phoneNumber;
    this.password = password;
  }
}
