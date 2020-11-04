package Controller;

import Model.Employee;

import javax.swing.*;
import java.sql.*;
import java.util.List;
import java.util.Vector;

public class DatabaseController {

    public Connection connectDatabase() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //JDBC 드라이버 연결

            // 접속 url과 사용자, 비밀번호
            String url="jdbc:mysql://localhost:3306/COMPANY?serverTimezone=UTC";
            String user="root";
            String pwd="비밀번호를 입력해주시면 됩니다.";

            con = DriverManager.getConnection(url, user, pwd);
            System.out.println("정상적으로 연결되었습니다.");
        } catch (SQLException e) {
            System.err.println("연결할 수 없습니다.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("드라이버를 로드할 수 없습니다.");
            e.printStackTrace();
        }
        return con;
    }

    public void disconnectDatabase(Connection con) {
        try {
            if (con != null) {
                con.close();
                con = null;
                System.out.println("연결이 해제되었습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retrieveDepartmentList(Vector<String> departmentList, Connection con) {
        try {
            departmentList.clear();
            departmentList.add("All");
            String stmt1 = "SELECT Dname FROM DEPARTMENT";
            PreparedStatement p = con.prepareStatement(stmt1);
            ResultSet r = p.executeQuery();
            while(r.next()) {
                departmentList.add(r.getString(1));
            }
            r.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retrieveEmployeeList(List<Employee> employeeList, String department, Connection con) {
        try {
            String query = "";
            if (department != "All") {
                query += "AND DEPARTMENT.Dname = '" + department + "'";
            }

            String stmt1 = "SELECT DISTINCT CONCAT(EMPLOYEE.Fname, \" \", EMPLOYEE.Minit, \" \", EMPLOYEE.Lname) as Name,\n" +
                    "       EMPLOYEE.Ssn, EMPLOYEE.Bdate, EMPLOYEE.Address, EMPLOYEE.Sex, EMPLOYEE.Salary,\n" +
                    "       IF (EMPLOYEE.Super_ssn IS NOT NULL, CONCAT(MANAGER.Fname, \" \", MANAGER.Minit, \" \", MANAGER.Lname), 'NULL')  as Super_name, DEPARTMENT.Dname\n" +
                    "FROM EMPLOYEE, EMPLOYEE AS MANAGER, DEPARTMENT\n" +
                    "WHERE (EMPLOYEE.Super_ssn IS NULL OR EMPLOYEE.Super_ssn = MANAGER.Ssn) AND EMPLOYEE.Dno = DEPARTMENT.Dnumber " + query;
            PreparedStatement p = con.prepareStatement(stmt1);
            ResultSet r = p.executeQuery();

            while(r.next()) {
                String name = r.getString(1);
                Integer ssn = r.getInt(2);
                String birthDate = r.getString(3);
                String address = r.getString(4);
                String gender = r.getString(5);
                Double salary = r.getDouble(6);
                String superName = r.getString(7);
                String dName = r.getString(8);
                employeeList.add(new Employee(name, ssn, birthDate, address, gender, salary, superName, dName));
            }
            r.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(Employee employee, Connection con) {
        try {
            String stmt1 = "DELETE FROM EMPLOYEE\n" +
                    "WHERE Ssn = " + employee.getSsn();
            PreparedStatement p = con.prepareStatement(stmt1);
            int r = p.executeUpdate();
            JOptionPane.showMessageDialog(null, employee.getName() + "이 삭제되었습니다.");
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployeeSalary(Employee employee, Double salary, Connection con) {
        try {
            String stmt1 = "UPDATE EMPLOYEE\n" +
                    "SET Salary = " + salary + "\n" +
                    "WHERE Ssn = " + employee.getSsn();
            PreparedStatement p = con.prepareStatement(stmt1);
            int r = p.executeUpdate();
            JOptionPane.showMessageDialog(null, employee.getName() + "의 급여가 " +  salary + "로 수정되었습니다.");
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
