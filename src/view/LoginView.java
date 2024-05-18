package view;

import model.Employee;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import exception.LimitLoginException;

public class LoginView extends JFrame implements ActionListener {
    private JTextField numEmpleado;
    private JTextField passwd;
    private JButton accederButton;
    private JPanel LoginPage;
    public int errorLogCount = 1;

    public LoginView() {
        setContentPane(LoginPage);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        accederButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == accederButton) {
            loginCheck();
        }
    }

    public void loginCheck() {
        try {
            boolean logged;
            String user = "roc";
            int iduser = Integer.parseInt(numEmpleado.getText());
            String password = passwd.getText();
            Employee employee = new Employee(user, iduser);
            logged = employee.login(iduser, password);

            if (logged) {
                openMenu();
            } else {
                errorLogCount++;
                JOptionPane.showMessageDialog(null, "Usuari o contrasenya incorrecta, queden " + (4 - errorLogCount) + " intents", "Error", JOptionPane.ERROR_MESSAGE);
                if (errorLogCount >= 4) {
                    try {
                        throw new LimitLoginException("Masses intents d'iniciar sessió fallats");
                    } catch (LimitLoginException ex) {
                        JOptionPane.showMessageDialog(null, "Error: S'ha superat el límite d'intentos", "Error", JOptionPane.ERROR_MESSAGE);
                        dispose();
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: Inici de sessió fallat" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openMenu() {
        setVisible(false);
        ShopView shopView = new ShopView();
        shopView.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}
