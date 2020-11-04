package Interface;

import Controller.DatabaseController;
import Model.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CompanyFrame extends JFrame {

    // Properties
    Boolean isName = true;
    Boolean isSsn = true;
    Boolean isBirthDate = true;
    Boolean isAddress = true;
    Boolean isSex = true;
    Boolean isSalary = true;
    Boolean isSupervisor = true;
    Boolean isDepartment = true;

    Employee selectedEmployee = null;

    Vector<String> departmentList = new Vector<>();
    List<Employee> employeeList = new ArrayList<Employee>();

    // Database Property
    DatabaseController databaseController = new DatabaseController();
    Connection con = null;

    // GUI Config
    Dimension frameDim = new Dimension(1000, 700);

    // GUI Components
    JLabel departmentSelectionLabel = new JLabel("Select Department");
    JComboBox<String> departmentComboBox;

    JButton searchButton = new JButton("조회");

    JCheckBox nameCheckBox = new JCheckBox("Name", true);
    JCheckBox ssnCheckBox = new JCheckBox("Ssn", true);
    JCheckBox bDateCheckBox = new JCheckBox("BirthDate", true);
    JCheckBox addressCheckBox = new JCheckBox("Address", true);
    JCheckBox sexCheckBox = new JCheckBox("Sex", true);
    JCheckBox salaryCheckBox = new JCheckBox("Salary", true);
    JCheckBox supervisorCheckBox = new JCheckBox("Supervisor", true);
    JCheckBox departmentCheckBox = new JCheckBox("Department", true);

    JPanel tableViewBorderPanel;
    JTable employeeTableView;
    JScrollPane scrollList;

    JLabel selectedEmployeeLabel = new JLabel("Now Not selected");
    JLabel newSalaryGuideLabel = new JLabel("새로운 급여 입력");
    JTextField newSalaryTextField = new JTextField();
    JButton editSalaryButton = new JButton("Edit");
    JButton addNewDepartmentButton = new JButton("Create new Department");
    JButton addNewEmployeeButton = new JButton("Create new Employee");
    JButton deleteEmployeeButton = new JButton("Delete Employee");

    CompanyFrame companyFrame = this;

    // Initializer
    public CompanyFrame() {
        setLocation(100, 100);
        setTitle("Company");
        setSize(frameDim);
        setLayout(null);

        con = databaseController.connectDatabase();
        databaseController.retrieveDepartmentList(departmentList, con);

        configureGUIComponents();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Helper
    void configureGUIComponents() {
        // Add GUI Component
        // Department UI 설정부분
        JPanel departmentContainer = new JPanel();
        departmentContainer.add(departmentSelectionLabel);
        departmentComboBox = new JComboBox<>();
        departmentComboBox.setModel(new DefaultComboBoxModel(departmentList));
        departmentContainer.add(departmentComboBox);
        departmentContainer.setLocation(0, 10);
        departmentContainer.setSize(1000, 30);
        add(departmentContainer);

        // Attribute UI 설정부분
        JPanel menuContainer = new JPanel();
        menuContainer.add(new JLabel("Select Attribute"));
        menuContainer.setLayout(new FlowLayout());
        menuContainer.add(nameCheckBox);
        menuContainer.add(ssnCheckBox);
        menuContainer.add(bDateCheckBox);
        menuContainer.add(addressCheckBox);
        menuContainer.add(sexCheckBox);
        menuContainer.add(salaryCheckBox);
        menuContainer.add(supervisorCheckBox);
        menuContainer.add(departmentCheckBox);
        menuContainer.add(searchButton);
        menuContainer.setLocation(0, 40);
        menuContainer.setSize(1000, 50);
        add(menuContainer);

        // TableView UI 설정부분
        setEmployeeTableView();

        // 하단 Edit UI 설정부분
        JPanel selectionContainer = new JPanel();
        selectedEmployeeLabel.setLocation(0, 0);
        selectionContainer.add(selectedEmployeeLabel);
        selectionContainer.setLocation(0, 550);
        selectionContainer.setSize(1000, 30);
        add(selectionContainer);

        JPanel editContainer = new JPanel(new GridLayout());
        editContainer.add(newSalaryGuideLabel);
        editContainer.add(newSalaryTextField);
        editContainer.add(editSalaryButton);
//        editContainer.add(new JPanel());
//        editContainer.add(new JPanel());
        editContainer.add(addNewDepartmentButton);
        editContainer.add(addNewEmployeeButton);
        editContainer.add(deleteEmployeeButton);
        editContainer.setLocation(0, 580);
        editContainer.setSize(1000, 50);
        add(editContainer);

        // 각 필요 컴포넌트들 Listener 적용부분
        nameCheckBox.addItemListener(new NameCheckBoxListener());
        ssnCheckBox.addItemListener(new SsnCheckBoxListener());
        bDateCheckBox.addItemListener(new BirthDateCheckBoxListener());
        addressCheckBox.addItemListener(new AddressCheckBoxListener());
        sexCheckBox.addItemListener(new SexCheckBoxListener());
        salaryCheckBox.addItemListener(new SalaryCheckBoxListener());
        supervisorCheckBox.addItemListener(new SupervisorCheckBoxListener());
        departmentCheckBox.addItemListener(new DepartmentCheckBoxListener());
        searchButton.addActionListener(new SearchButtonListener());
        editSalaryButton.addActionListener(new EditSalaryButtonListener());
        deleteEmployeeButton.addActionListener(new DeleteEmployeeButtonListener());

        addNewEmployeeButton.addActionListener(new AddNewEmployeeButtonListener());
    }

    void setEmployeeTableView() {
        tableViewBorderPanel = new JPanel(new BorderLayout());

        Vector<String> columns = new Vector<>();
        if (isName) columns.add("Name");
        if (isSsn) columns.add("Ssn");
        if (isBirthDate) columns.add("BirthDate");
        if (isAddress) columns.add("Address");
        if (isSex) columns.add("Sex");
        if (isSalary) columns.add("Salary");
        if (isSupervisor) columns.add("Supervisor");
        if (isDepartment) columns.add("Department");

        Vector<Object> dataSet = new Vector<>();
        for (int i = 0; i < employeeList.size(); i++) {
            Vector<Object> data = new Vector<>();
            if (isName) data.add(employeeList.get(i).getName());
            if (isSsn) data.add(employeeList.get(i).getSsn());
            if (isBirthDate) data.add(employeeList.get(i).getBirthDate());
            if (isAddress) data.add(employeeList.get(i).getAddress());
            if (isSex) data.add(employeeList.get(i).getSex());
            if (isSalary) data.add(employeeList.get(i).getSalary());
            if (isSupervisor) data.add(employeeList.get(i).getSupervisor());
            if (isDepartment) data.add(employeeList.get(i).getDepartment());
            dataSet.add(data);
        }

        employeeTableView = new JTable(dataSet, columns);
        employeeTableView.getColumnModel().getColumn(0).setPreferredWidth(30);
        employeeTableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollList = new JScrollPane(employeeTableView);

        tableViewBorderPanel.add(scrollList, BorderLayout.CENTER);
        tableViewBorderPanel.setSize(1000, 400);
        tableViewBorderPanel.setLocation(0, 100);

        employeeTableView.addMouseListener(new EmployeeTableViewMouseListener());
        this.add(tableViewBorderPanel);
        setVisible(true);
    }

    void updateTableView() {
        remove(tableViewBorderPanel);
        setEmployeeTableView();
    }

    void checkDatabaseConnection() {
        if (con == null) {
            con = databaseController.connectDatabase();
        }
    }

    void disconnectDatabase() {
        if (con != null) {
            databaseController.disconnectDatabase(con);
        }
    }

    void search() {
        employeeList.clear();
        selectedEmployee = null;
        checkDatabaseConnection();
        String department = departmentComboBox.getSelectedItem().toString();
        databaseController.retrieveEmployeeList(employeeList, department, con);

        updateTableView();
        selectedEmployeeLabel.setText("Now Not selected");
    }

    // Listener Class
    class NameCheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
                isName = true;
            } else if (e.getStateChange() == 2) {
                isName = false;
            }
        }
    }

    class SsnCheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
                isSsn = true;
            } else if (e.getStateChange() == 2) {
                isSsn = false;
            }
        }
    }

    class BirthDateCheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
                isBirthDate = true;
            } else if (e.getStateChange() == 2) {
                isBirthDate = false;
            }
        }
    }

    class AddressCheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
                isAddress = true;
            } else if (e.getStateChange() == 2) {
                isAddress = false;
            }
        }
    }

    class SexCheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
                isSex = true;
            } else if (e.getStateChange() == 2) {
                isSex = false;
            }
        }
    }

    class SalaryCheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
                isSalary = true;
            } else if (e.getStateChange() == 2) {
                isSalary = false;
            }
        }
    }

    class SupervisorCheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
                isSupervisor = true;
            } else if (e.getStateChange() == 2) {
                isSupervisor = false;
            }
        }
    }

    class DepartmentCheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
                isDepartment = true;
            } else if (e.getStateChange() == 2) {
                isDepartment = false;
            }
        }
    }

    class SearchButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            search();
        }
    }

    class EmployeeTableViewMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) { }

        @Override
        public void mousePressed(MouseEvent e) { }

        @Override
        public void mouseReleased(MouseEvent e) {
            int row = employeeTableView.getSelectedRow();
            selectedEmployee = employeeList.get(row);
            selectedEmployeeLabel.setText("Selected " + employeeList.get(row).getName());
        }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }
    }

    class EditSalaryButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Double newSalary = Double.parseDouble(newSalaryTextField.getText());
                if (selectedEmployee != null) {
                    checkDatabaseConnection();
                    databaseController.updateEmployeeSalary(selectedEmployee, newSalary, con);
                    search();
                    newSalaryTextField.setText("");
                    selectedEmployee = null;
                } else {
                    JOptionPane.showMessageDialog(null, "직원 선택 후 이용 가능합니다");
                }
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(null, "입력값 형태를 다시 확인해주세요");
            }
        }
    }

    class DeleteEmployeeButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedEmployee == null) {
                JOptionPane.showMessageDialog(null, "직원 선택 후 이용 가능합니다");
            } else {
                checkDatabaseConnection();
                databaseController.deleteEmployee(selectedEmployee, con);
                search();
                selectedEmployee = null;
            }
        }
    }


    // 여기부터 추가 기능생성

    class AddNewEmployeeButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String name, birthDate, address, sex, supervisor, department;
            int ssn;
            Double salary;
            new AddNewEmployeeFrame(companyFrame);
//            String test = JOptionPane.showInputDialog(null, "")
        }
    }

    public void showSomething() {
        JOptionPane.showMessageDialog(null, "test");
    }

    class AddNewDepartmentButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
