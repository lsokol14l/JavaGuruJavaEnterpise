package by.michael.dto;

public class EmployeeDto {
  private int Id;
  private String name;
  private int salary;
  private int tax;

  public int getId() {
    return Id;
  }

  public void setId(int id) {
    Id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSalary() {
    return salary;
  }

  public void setSalary(int salary) {
    this.salary = salary;
  }

  public int getTax() {
    return tax;
  }

  public void setTax(int tax) {
    this.tax = tax;
  }
}
