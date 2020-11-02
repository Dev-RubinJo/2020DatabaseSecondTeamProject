package Interface;

import Controller.DatabaseController;
import Interface.Model.Employee;

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

    // TODO: DB에서 가져와서 추후 데이터 넣어주기
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

    public CompanyFrame() {
        setLocation(100, 100);
        setTitle("Company");
        setSize(frameDim);
        setLayout(null);

        con = databaseController.connectDatabase();
        departmentList.add("All");
        databaseController.retrieveDepartmentList(departmentList, con);

        configureGUIComponents();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

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
        JPanel editContainer = new JPanel();
        editContainer.add(selectedEmployeeLabel);
        editContainer.setLocation(0, 550);
        editContainer.setSize(1000, 100);
        add(editContainer);

        // 각 필요 컴포넌트들 Listener 적용부분
        NameCheckBoxListener nameCheckBoxListener = new NameCheckBoxListener();
        nameCheckBox.addItemListener(nameCheckBoxListener);

        SsnCheckBoxListener ssnCheckBoxListener = new SsnCheckBoxListener();
        ssnCheckBox.addItemListener(ssnCheckBoxListener);

        BirthDateCheckBoxListener birthDateCheckBoxListener = new BirthDateCheckBoxListener();
        bDateCheckBox.addItemListener(birthDateCheckBoxListener);

        AddressCheckBoxListener addressCheckBoxListener = new AddressCheckBoxListener();
        addressCheckBox.addItemListener(addressCheckBoxListener);

        SexCheckBoxListener sexCheckBoxListener = new SexCheckBoxListener();
        sexCheckBox.addItemListener(sexCheckBoxListener);

        SalaryCheckBoxListener salaryCheckBoxListener = new SalaryCheckBoxListener();
        salaryCheckBox.addItemListener(salaryCheckBoxListener);

        SupervisorCheckBoxListener supervisorCheckBoxListener = new SupervisorCheckBoxListener();
        supervisorCheckBox.addItemListener(supervisorCheckBoxListener);

        DepartmentCheckBoxListener departmentCheckBoxListener = new DepartmentCheckBoxListener();
        departmentCheckBox.addItemListener(departmentCheckBoxListener);

        SearchButtonListener searchButtonListener = new SearchButtonListener();
        searchButton.addActionListener(searchButtonListener);
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
        System.out.println(employeeTableView.getColumnCount());
    }

    void checkDatabaseConnection() {
        if (con == null) {
            con = databaseController.connectDatabase();
        }
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
            employeeList.clear();
            checkDatabaseConnection();
            String department = departmentComboBox.getSelectedItem().toString();
            databaseController.retrieveEmployeeList(employeeList, department, con);
            
            updateTableView();
            selectedEmployeeLabel.setText("Now Not selected");
            System.out.println("Search Button Clicked");
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

}
