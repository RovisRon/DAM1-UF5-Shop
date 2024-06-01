package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;

import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class ShopView extends JFrame implements ActionListener, KeyListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Shop shop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShopView frame = new ShopView();
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
	public ShopView() {
		
		shop = new Shop();
		shop.loadInventory();
		setTitle("MiTienda.com");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel lbl_MiTiendacom = new JLabel("MiTienda.com");
		lbl_MiTiendacom.setBounds(20, 20, 120, 20);
		contentPane.add(lbl_MiTiendacom);
		
		JLabel lbl_SeleccionaOpcio = new JLabel("Selecciona una opció:");
		lbl_SeleccionaOpcio.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_SeleccionaOpcio.setBounds(40, 60, 140, 20);
		contentPane.add(lbl_SeleccionaOpcio);
		
		JButton btn_ContarCaixa = new JButton("1. Contar Caixa");
		btn_ContarCaixa.setHorizontalAlignment(SwingConstants.LEFT);
		btn_ContarCaixa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            CashView cashView = new CashView(shop);
	            cashView.setVisible(true);
			}
		});
		btn_ContarCaixa.setBounds(200, 60, 150, 20);
		contentPane.add(btn_ContarCaixa);
		btn_ContarCaixa.addKeyListener(this);
		
		JButton btn_AfegirProducte = new JButton("2. Afegir producte");
		btn_AfegirProducte.setHorizontalAlignment(SwingConstants.LEFT);
		btn_AfegirProducte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = 2;
	            ProductView productView = new ProductView(shop, opcion);
	            productView.setVisible(true);
			}
		});
		btn_AfegirProducte.setBounds(200, 90, 150, 20);
		contentPane.add(btn_AfegirProducte);
		btn_AfegirProducte.addKeyListener(this);
		
		JButton btn_AfegirStock = new JButton("3. Afegir Stock");
		btn_AfegirStock.setHorizontalAlignment(SwingConstants.LEFT);
		btn_AfegirStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = 3;
	            ProductView productView = new ProductView(shop, opcion);
	            productView.setVisible(true);
			}
		});
		btn_AfegirStock.setBounds(200, 120, 150, 20);
		contentPane.add(btn_AfegirStock);
		btn_AfegirStock.addKeyListener(this);
		
		JButton btn_EliminarProducte = new JButton("9. Eliminar Producte");
		btn_EliminarProducte.setHorizontalAlignment(SwingConstants.LEFT);
		btn_EliminarProducte.setBounds(200, 180, 150, 20);
		contentPane.add(btn_EliminarProducte);
		btn_EliminarProducte.addKeyListener(this);
		btn_EliminarProducte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = 9;
	            ProductView productView = new ProductView(shop, opcion);
	            productView.setVisible(true);
			}
		});
		
		JButton btn_MostrarInventari = new JButton("5. Mostrar Inventari");
		btn_MostrarInventari.setHorizontalAlignment(SwingConstants.LEFT);
		btn_MostrarInventari.setBounds(200, 150, 150, 20);
		contentPane.add(btn_MostrarInventari);
		btn_MostrarInventari.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InventoryView inventoryView = new InventoryView();
	            inventoryView.setVisible(true);
			}
		});
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		char key = e.getKeyChar();
		switch(key) {
		case '1':
			int opcion = 1;
            CashView cashView = new CashView(shop);
            cashView.setVisible(true);
            break;
		case '2':
			opcion = 2;
            ProductView productView = new ProductView(shop, opcion);
            productView.setVisible(true);
            break;
		case '3':
			opcion = 3;
            productView = new ProductView(shop, opcion);
            productView.setVisible(true);
            break;
		case '5':
			opcion = 5;
            InventoryView inventoryView = new InventoryView();
            inventoryView.setVisible(true);
            break;
		case '9':
			opcion = 9;
            productView = new ProductView(shop, opcion);
            productView.setVisible(true);
            break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
