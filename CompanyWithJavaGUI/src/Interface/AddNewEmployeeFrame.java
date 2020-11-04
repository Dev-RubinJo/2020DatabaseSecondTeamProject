package Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddNewEmployeeFrame extends JFrame {
    JTextField fNameTextField = new JTextField(5);
    JTextField minitTextField = new JTextField(5);
    JTextField lNameTextField = new JTextField(5);
    JTextField ssnTextField = new JTextField(6);
    JTextField birthDateTextField = new JTextField(10);
    JTextField addressTextField = new JTextField(15);
    JTextField sexTextField = new JTextField(3);
    JTextField salaryTextField = new JTextField(7);
    JTextField supervisorTextField = new JTextField(6); // 상급자의 ssn 입력
    JTextField departmentTextField = new JTextField(3); // 부서 번호 입력

    JButton okButton = new JButton("Ok");
    JButton cancelButton = new JButton("cancel");

    CompanyFrame companyFrame;

    public AddNewEmployeeFrame(CompanyFrame companyFrame) {
        this.companyFrame = companyFrame;
        setTitle("직원 추가하기");
        setSize(400, 280);
        JPanel basePanel = new JPanel(new GridLayout(12, 2));

        basePanel.add(new JLabel("    first name: "));
        basePanel.add(fNameTextField);

        basePanel.add(new JLabel("    minit: "));
        basePanel.add(minitTextField);

        basePanel.add(new JLabel("    last name: "));
        basePanel.add(lNameTextField);

        basePanel.add(new JLabel("    ssn: "));
        basePanel.add(ssnTextField);

        basePanel.add(new JLabel("    Birth Date: "));
        basePanel.add(birthDateTextField);

        basePanel.add(new JLabel("    Address: "));
        basePanel.add(addressTextField);

        basePanel.add(new JLabel("    Gender: "));
        basePanel.add(sexTextField);

        basePanel.add(new JLabel("    Salary: "));
        basePanel.add(salaryTextField);

        basePanel.add(new JLabel("    Supervisor's ssn: "));
        basePanel.add(supervisorTextField);

        basePanel.add(new JLabel("    Department number: "));
        basePanel.add(departmentTextField);

        basePanel.add(new JPanel());
        basePanel.add(new JPanel());

        basePanel.add(new JPanel());
        JPanel buttonGroup = new JPanel(new GridLayout());
        buttonGroup.add(okButton);
        buttonGroup.add(cancelButton);
        basePanel.add(buttonGroup);

        okButton.addActionListener(new OkButtonListener());
        cancelButton.addActionListener(new CancelButtonListener());

        add(basePanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    class OkButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("OkButton Selected");
            JOptionPane.showConfirmDialog(null, "직원을 추가하시겠습니까?", "직원 추가", JOptionPane.OK_CANCEL_OPTION);

        }
    }

    class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            System.out.println("CancelButton Selected");
        }
    }

}
