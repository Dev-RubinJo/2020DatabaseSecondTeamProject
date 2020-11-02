package Interface.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Employee {

    // Properties

    private String name;
    private Integer ssn;
    private String birthDate;
    private String address;
    private String sex;
    private Double salary;
    private String supervisor;
    private String department;

    // Initializer

    public Employee(String name, Integer ssn, String birthDate, String address, String sex, Double salary, String supervisor, String department) {
        this.name = name;
        this.ssn = ssn;
        this.birthDate = birthDate;
        this.address = address;
        this.sex = sex;
        this.salary = salary;
        this.supervisor = supervisor;
        this.department = department;
    }

    // getter

    public String getName() {
        return name;
    }

    public Integer getSsn() {
        return ssn;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getSex() {
        return sex;
    }

    public Double getSalary() {
        return salary;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public String getDepartment() {
        return department;
    }
}
