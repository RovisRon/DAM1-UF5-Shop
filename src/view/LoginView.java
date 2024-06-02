package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import exception.LimitLoginException;

public class LoginView extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txt_contrasenya;
    private JTextField txt_NumTreballador;
    private JLabel lbl_Contrasenya;
    private int loginErrorCount = 0;
    private static final int MAX_LOGIN_ERRORS = 3;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginView frame = new LoginView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public LoginView() {
        setTitle("Iniciar Sessió");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBounds(100, 100, 400, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lbl_NumTreballador = new JLabel("Número de treballador");
        lbl_NumTreballador.setBounds(20, 20, 140, 20);
        contentPane.add(lbl_NumTreballador);

        txt_NumTreballador = new JTextField();
        txt_NumTreballador.setBounds(200, 20, 140, 20);
        contentPane.add(txt_NumTreballador);
        txt_NumTreballador.setColumns(10);

        lbl_Contrasenya = new JLabel("Contrasenya");
        lbl_Contrasenya.setBounds(20, 60, 140, 20);
        contentPane.add(lbl_Contrasenya);

        txt_contrasenya = new JTextField();
        txt_contrasenya.setBounds(200, 60, 140, 20);
        contentPane.add(txt_contrasenya);
        txt_contrasenya.setColumns(10);

        JButton btn_Accedir = new JButton("Accedir");
        btn_Accedir.setBounds(240, 120, 100, 20);
        btn_Accedir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pass;
                int employeeID = 0;

                pass = txt_contrasenya.getText();
                String employeeIDText = txt_NumTreballador.getText();
                try {
                    employeeID = Integer.parseInt(employeeIDText);

                    if (login(employeeID, pass)) {
                        LoginView.this.setVisible(false);
                        ShopView shopView = new ShopView();
                        shopView.setVisible(true);
                    } else {
                        loginErrorCount++;
                        if (loginErrorCount >= MAX_LOGIN_ERRORS) {
                            throw new LimitLoginException("S'han superat els intents d'iniciar sessió.");
                        } else {
                            JOptionPane.showMessageDialog(LoginView.this, "Usuari o contrasenya incorrectes", "Error", JOptionPane.ERROR_MESSAGE);
                            txt_contrasenya.setText("");
                            txt_NumTreballador.setText("");
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(LoginView.this, "Introdueixi un ID de treballador correcte", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (LimitLoginException ex) {
                    JOptionPane.showMessageDialog(LoginView.this, "S'han superat els intents d'iniciar sessió. Tancant... ", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
        });
        contentPane.add(btn_Accedir);
    }

    private boolean login(int employeeID, String pass) {
        try {
            String url = "jdbc:mysql://localhost:3306/shop";
            String dbUser = "root";
            String dbPassword = "DAM1T_M03";

            Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);

            String query = "SELECT * FROM employee WHERE employeeID = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, employeeID);
            preparedStatement.setString(2, pass);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Inicio de sesión exitoso.");
                return true;
            } else {
                System.out.println("Nombre de usuario o contraseña incorrectos.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }
}
