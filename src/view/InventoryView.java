package view;

import model.Product;
import main.Shop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class InventoryView extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
    private Shop shop;

    public InventoryView() {
        shop = new Shop();
        shop.loadInventory();

        setTitle("Inventari");
        setSize(800, 250);

        String[] columnNames = {"Codi", "Nom", "Preu Públic", "Preu Majorista", "Inventari", "Disponible"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        loadData();

        setVisible(true);
    }

    private void loadData() {
        ArrayList<Product> products = shop.inventory;
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int codigo = 0;
        for (Product product : products) {
        	String avaliable = "Disponible";
        	codigo++;
        	if (product.getStock() == 0) {
        		avaliable = "No disponible";
        	}
            model.addRow(new Object[]{codigo, product.getName(), product.getWholesalerPrice().getValue() * 2, product.getWholesalerPrice().getValue(), product.getStock(), avaliable});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryView());
    }
}
