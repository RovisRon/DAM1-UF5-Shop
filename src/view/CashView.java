package view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CashView extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txt_Caixa;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Shop shop = new Shop();
			CashView dialog = new CashView(shop);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CashView(Shop shop) {
		setTitle("Caixa");
		setBounds(100, 100, 300, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Caixa:");
			lblNewLabel.setBounds(20, 20, 100, 20);
			contentPanel.add(lblNewLabel);
		}
		txt_Caixa = new JTextField();
		txt_Caixa.setBounds(140, 20, 100, 20);
		txt_Caixa.setEditable(false);
		contentPanel.add(txt_Caixa);
		txt_Caixa.setColumns(10);
		double valorCash = shop.showCash();
		String valorCashStr = valorCash + "€";
		txt_Caixa.setText(valorCashStr);
		{
			JButton btn_ok = new JButton("OK");
			btn_ok.setBounds(140, 60, 100, 20);
			contentPanel.add(btn_ok);
			btn_ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CashView.this.setVisible(false);
				}
			});
			btn_ok.setActionCommand("OK");
			getRootPane().setDefaultButton(btn_ok);
		}
	}
}
