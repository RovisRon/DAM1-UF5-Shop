package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;
import model.Product;
import model.Amount;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ProductView extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txt_NomProducte;
	private JTextField txt_Stock;
	private JTextField txt_Preu;
	private static int opcion;
	private ArrayList<Product> inventory;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Shop shop = new Shop();
			ProductView dialog = new ProductView(shop, opcion);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ProductView(Shop shop, int opcion) {
		setTitle("Afegir Producte");
		setBounds(100, 100, 400, 250);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lbl_NomProducte = new JLabel("Nom del producte: ");
		lbl_NomProducte.setBounds(20, 20, 150, 20);
		contentPanel.add(lbl_NomProducte);

		JLabel lbl_Stock = new JLabel("Stock:");
		lbl_Stock.setBounds(20, 60, 150, 20);
		contentPanel.add(lbl_Stock);

		JLabel lbl_Preu = new JLabel("Preu:");
		lbl_Preu.setBounds(20, 100, 150, 20);
		contentPanel.add(lbl_Preu);

		txt_NomProducte = new JTextField();
		txt_NomProducte.setBounds(200, 20, 140, 20);
		contentPanel.add(txt_NomProducte);
		txt_NomProducte.setColumns(10);

		txt_Stock = new JTextField();
		txt_Stock.setBounds(200, 60, 140, 20);
		contentPanel.add(txt_Stock);
		txt_Stock.setColumns(10);

		txt_Preu = new JTextField();
		txt_Preu.setBounds(200, 100, 140, 20);
		contentPanel.add(txt_Preu);
		txt_Preu.setColumns(10);
		switch(opcion) {
		case 3:
			setTitle("Afegir Stock");
			lbl_Preu.setVisible(false);
			txt_Preu.setVisible(false);
			break;
		case 9:
			setTitle("Eliminar Producte");
			lbl_Preu.setVisible(false);
			txt_Preu.setVisible(false);
			lbl_Stock.setVisible(false);
			txt_Stock.setVisible(false);
			break;
		}		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btn_ok = new JButton("OK");
				btn_ok.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switch(opcion) {
						case 2:
							String name = txt_NomProducte.getText();
							String stocktext = txt_Stock.getText();
							Amount wholesalerPrice = new Amount(Double.parseDouble(txt_Preu.getText()));
							int stock = Integer.parseInt(stocktext);
							boolean productoExistente = false;
							for (Product product : shop.inventory) {
							    if (product.getName().equalsIgnoreCase(name)) {
							    	JOptionPane.showMessageDialog(null, "ERROR: Aquest producte ja existeix", "Error", JOptionPane.ERROR_MESSAGE);
							    	productoExistente = true;
							        break; 
							    }
							}
							if (!productoExistente) {
								addProduct(new Product(name, wholesalerPrice, true, stock));
							    JOptionPane.showMessageDialog(null, "Producte afegit con èxit!", "Afegir Producte", JOptionPane.INFORMATION_MESSAGE);
							    for (Product product : shop.inventory) {
									if (product != null) {
										System.out.println("Nom: "+product.getName()+" // Id: "+product.getId()+" // Preu Proveedor Unitat: "+product.getWholesalerPrice()
										+" // Preu Venta Client Unitat: "+product.getPublicPrice()+" // Stock: "+product.getStock()+" // isAvailable: "+product.isAvailable());
									}
								}
							    ProductView.this.setVisible(false);
							}
							break;
						case 3:
							name = txt_NomProducte.getText();
							stocktext = txt_Stock.getText();
							stock = Integer.parseInt(stocktext);
							Product product = shop.findProduct(name);

							if (product != null) {
								product.setStock((product.getStock() + stock));
								if (product.getStock() > 0) {
					                product.setAvailable(true);
					            }
								JOptionPane.showMessageDialog(null, "El stock del producte " + name + " s'ha actualitzat a " + product.getStock(), "Afegir Stock", JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(null, "ERROR: No s'ha trobat el producte " + name, "Error", JOptionPane.ERROR_MESSAGE);
							}
							break;
						case 9:
							productoExistente = false;
							name = txt_NomProducte.getText();
							boolean existe = false;
							for (int i = 0; i < shop.inventory.size(); i++) {
								Product product2 = shop.inventory.get(i);
								if (product2 != null) {
									if (product2.getName().equalsIgnoreCase(name)) {
										shop.inventory.remove(i);
										existe = true;
										JOptionPane.showMessageDialog(null, "El producte " + name + " s'ha eliminat amb èxit ", "Eliminar producte", JOptionPane.INFORMATION_MESSAGE);
										for (Product product3 : shop.inventory) {
									    	System.out.println("Nombre: "+product3.getName()+" // Id: "+product3.getId()+" // Preu Proveedor Unitat: "+product3.getWholesalerPrice()
											+" // Preu Venta Client Unitat: "+product3.getPublicPrice()+" // Stock: "+product3.getStock()+" // isAvailable: "+product3.isAvailable());
									    }
										break;
									}
								}
							}
							if (!existe) {
								JOptionPane.showMessageDialog(null, "ERROR: No s'ha trobat el producte " + name, "Error", JOptionPane.ERROR_MESSAGE);
							}
							break;
						}
					}

					private void addProduct(Product product) {
						inventory.add(product);
						
					}
				});
				btn_ok.setActionCommand("OK");
				buttonPane.add(btn_ok);
				getRootPane().setDefaultButton(btn_ok);
			}
			{
				JButton btn_cancelar = new JButton("Cancel·lar");
				btn_cancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ProductView.this.setVisible(false);
					}
				});
				btn_cancelar.setActionCommand("Cancel·lar");
				buttonPane.add(btn_cancelar);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
