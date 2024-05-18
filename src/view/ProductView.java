package view;

import main.Shop;
import model.Product;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class ProductView extends JDialog {
    private Shop shop;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameProductTextField;
    private JTextField stockProductTextField;
    private JTextField priceProductTextField;
    private JLabel nameProductLabel;
    private JLabel stockProductLabel;
    private JLabel priceProductLabel;
    int publicOption = 0;
    private static boolean errorExists = false;
    private static boolean stockNotExists = false;
    private static boolean productsNotExists = false;
    public ProductView() {
        setContentPane(contentPane);
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        }



    public void openProductView(int opcion, Shop shop) {
        this.shop = shop;
        // Establecer el título aquí
        switch (opcion) {
            case 2:
                setTitle("Add Product");
                break;
            case 3:
                setTitle("Add Stock");
                priceProductTextField.setVisible(false);
                priceProductLabel.setVisible(false);
                setSize(400, 150);
                break;
            case 4:
                setTitle("Delete Product");
                stockProductTextField.setVisible(false);
                stockProductLabel.setVisible(false);
                priceProductTextField.setVisible(false);
                priceProductLabel.setVisible(false);
                setSize(400, 120);
                break;
            default:
                System.out.println("Opcion no valida");
        }
        setVisible(true);
        reciveData(opcion);
    }


    private void reciveData(int opcion) {
        this.publicOption = opcion;
        System.out.println(publicOption);
    }

    public void setExeption(boolean exTrue) {
        errorExists = exTrue;
    }

    public void setExeptionProductNotFound (boolean extTrue) {
        stockNotExists = extTrue;
    }

    public void productNotExist (boolean extsTrue) {
        productsNotExists = extsTrue;
    }

    private void onOK() {
        String name = nameProductTextField.getText();
        String priceText = priceProductTextField.getText();
        String stockText = stockProductTextField.getText();

        // Verificar campos requeridos según la opción seleccionada
        if ((publicOption == 2 && (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty())) ||
                (publicOption == 3 && (name.isEmpty() || stockText.isEmpty())) ||
                (publicOption == 4 && (name.isEmpty()))) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price = 0.0;
        int stock = 0;
        boolean available = true;

        try {
            // Convertir a double solo si el campo de precio no está vacío y la opción es 2
            if (!priceText.isEmpty() && publicOption == 2) {
                price = Double.parseDouble(priceText);
            }
            if (!stockText.isEmpty() && publicOption == 3) {
                stock = Integer.parseInt(stockText);
            }

            // Convertir siempre el campo de stock a int

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos para el precio y el stock.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (publicOption) {
            case 2:
                shop.inventory(name, price, available, stock);
                if (errorExists) {
                    JOptionPane.showMessageDialog(this, "El Producto ya existe en el Inventario", "Error", JOptionPane.ERROR_MESSAGE);
                    errorExists = false;
                } else {
                    JOptionPane.showMessageDialog(this, "Producto agregado con éxito al inventario.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
                break;
            case 3:
                shop.productStock(name, stock);
                if (stockNotExists) {
                    JOptionPane.showMessageDialog(this, "Producto no Existe", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Stock del producto actualizado con éxito en el inventario.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
                break;
            case 4:
                shop.delForProductView(name);
                if (productsNotExists) {
                    JOptionPane.showMessageDialog(this, "Producto no Existe", "Éxito", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "El Producto Fue Eliminado Con Exito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
                // Lógica para eliminar el producto
                break;
            default:
                System.out.println("Opción no válida");
        }
    }





    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ProductView dialog = new ProductView();
        dialog.pack();
        System.exit(0);
        dialog.setVisible(true);
    }

}
