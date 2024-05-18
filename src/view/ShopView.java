package view;

import main.Shop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ShopView extends JFrame implements ActionListener, KeyListener {
    private JButton a1ContarCajaButton;
    private JButton a2AñadirProductoButton;
    private JButton a3AñadirStockButton;
    private JButton a9EliminarProductoButton;
    private JPanel menuOpciones;

    public ShopView() {
        Shop shop = new Shop();
        shop.loadInventory();
        setContentPane(menuOpciones);
        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
		a1ContarCajaButton.addActionListener(this);
        a1ContarCajaButton.addKeyListener(this);
        a2AñadirProductoButton.addActionListener(this);
        a2AñadirProductoButton.addKeyListener(this);
        a3AñadirStockButton.addActionListener(this);
        a3AñadirStockButton.addKeyListener(this);
        a9EliminarProductoButton.addActionListener(this);
        a9EliminarProductoButton.addKeyListener(this);
        addKeyListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        ProductView productView = new ProductView();
        Shop shop = new Shop();
        int opcion = 0;

        if (source == a1ContarCajaButton) {
            openCashView();
        }
        if (source == a2AñadirProductoButton) {
            opcion = 2;
            productView.openProductView(opcion, shop);
        }
        if (source == a3AñadirStockButton) {
            opcion = 3;
            productView.openProductView(opcion, shop);
        }
        if (source == a9EliminarProductoButton) {
            opcion = 4;
            productView.openProductView(opcion, shop);
        }
    }

    private void openCashView() {
        CashView cashView = new CashView();
        cashView.setVisible(true);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ShopView();
        });
    }


}
