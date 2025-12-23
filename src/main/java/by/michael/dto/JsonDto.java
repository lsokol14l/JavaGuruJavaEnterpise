package by.michael.dto;

import java.util.List;

public class JsonDto {
  private String info;
  List<EmployeeDto> employees;

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public List<EmployeeDto> getEmployees() {
    return employees;
  }

  public void setEmployees(List<EmployeeDto> employees) {
    this.employees = employees;
  }
}
