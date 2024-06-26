package main;

import model.Product;
import model.Sale;
import model.Amount;
import model.Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Shop {
	private Amount cash = new Amount(100.00);
//	private Product[] inventory;
	public ArrayList<Product> inventory;
	private int numberProducts;
//	private Sale[] sales;
	private ArrayList<Sale> sales;
	final static double TAX_RATE = 1.04;

	private static final String DB_URL = "jdbc:mysql://localhost:3306/shop";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12346578";
    
	public Shop() {
		inventory = new ArrayList<Product>();
		sales = new ArrayList<Sale>();
	}

	public static void main(String[] args) {
		if (!login()) {
            System.out.println("Error de autenticación. Saliendo del programa.");
            return;
        }
		
		Shop shop = new Shop();

		// load inventory from external data
		shop.loadInventory();

		try (Scanner scanneranner = new Scanner(System.in)) {
			int opcion = 0;
			boolean exit = false;

			do {
				// INSERTAR MENÚ DE INICIAR SESIÓN EN LA BASE DE DATOS AQUÍ
				System.out.println("\n");
				System.out.println("===========================");
				System.out.println("Menu principal miTienda.com");
				System.out.println("===========================");
				System.out.println("1) Contar caja");
				System.out.println("2) Añadir producto");
				System.out.println("3) Añadir stock");
				System.out.println("4) Marcar producto proxima caducidad");
				System.out.println("5) Ver inventario");
				System.out.println("6) Venta");
				System.out.println("7) Ver ventas");
				System.out.println("8) Ver venta total");
				System.out.println("9) Eliminar producto");
				System.out.println("10) Salir programa");
				System.out.print("Seleccione una opción: ");
				opcion = scanneranner.nextInt();

				switch (opcion) {
				case 1:
					shop.showCash();
					break;

				case 2:
					shop.addProduct();
					break;

				case 3:
					shop.addStock();
					break;

				case 4:
					shop.setExpired();
					break;

				case 5:
					shop.showInventory();
					break;

				case 6:
					shop.sale();
					break;

				case 7:
					shop.showSales();
					break;

				case 8:
					shop.showSalesAmount();
					break;

				case 9:
					shop.removeProduct();
					break;

				case 10:
					System.out.println("Cerrando programa ...");
					exit = true;
					break;
				}

			} while (!exit);
		}
	}
	
	private static boolean login() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el nombre de usuario: ");
        String username = scanner.nextLine();

        System.out.print("Ingrese la contraseña: ");
        String password = scanner.nextLine();

        try {
        	
        	String url = "jdbc:mysql://localhost:3306/shop";
            String dbUser = "root";
            String dbPassword = "12345678";

            Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
            
            String query = "SELECT * FROM employee WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Inicio de sesión exitoso.");
                return true;
            } else {
                System.out.println("Usuario o contraseña incorrectos.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	

	/**
	 * load initial inventory to shop
	 */
	public void loadInventory() {
//		addProduct(new Product("Manzana", new Amount(10.00), true, 10));
//		addProduct(new Product("Pera", new Amount(20.00), true, 20));
//		addProduct(new Product("Hamburguesa", new Amount(30.00), true, 30));
//		addProduct(new Product("Fresa", new Amount(5.00), true, 20));
		// now read from file
		this.readInventory();
	}

	/**
	 * read inventory from file
	 */
	private void readInventory() {
		// locate file, path and name
		File f = new File(System.getProperty("user.dir") + File.separator + "src/Files/inputInventory.txt");

		try {
			// wrap in proper classes
			FileReader fr;
			fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			// read first line
			String line = br.readLine();

			// process and read next line until end of file
			while (line != null) {
				// split in sections
				String[] sections = line.split(";");

				String name = "";
				double wholesalerPrice = 0.0;
				int stock = 0;

				// read each sections
				for (int i = 0; i < sections.length; i++) {
					// split data in key(0) and value(1)
					String[] data = sections[i].split(":");

					switch (i) {
					case 0:
						// format product name
						name = data[1];
						break;

					case 1:
						// format price
						wholesalerPrice = Double.parseDouble(data[1]);
						break;

					case 2:
						// format stock
						stock = Integer.parseInt(data[1]);
						break;

					default:
						break;
					}
				}
				// add product to inventory
				addProduct(new Product(name, new Amount(wholesalerPrice), true, stock));

				// read next line
				line = br.readLine();
			}
			fr.close();
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * show current total cash
	 * 
	 * @return
	 */
	public double showCash() {
		System.out.println("Dinero actual: " + cash);
		return numberProducts;
	}

	/**
	 * add a new product to inventory getting data from console
	 */
	public void addProduct() {
		if (isInventoryFull()) {
			System.out.println("No se pueden añadir más productos");
			return;
		}
		try (Scanner scanneranner = new Scanner(System.in)) {
			System.out.print("Nombre: ");
			String name = scanneranner.nextLine();
			System.out.print("Precio mayorista: ");
			double wholesalerPrice = scanneranner.nextDouble();
			System.out.print("Stock: ");
			int stock = scanneranner.nextInt();

			addProduct(new Product(name, new Amount(wholesalerPrice), true, stock));
		}
	}

	/**
	 * remove a new product to inventory getting data from console
	 */
	public void removeProduct() {
		if (inventory.size() == 0) {
			System.out.println("No se pueden eliminar productos, inventario vacio");
			return;
		}
		try (Scanner scanneranner = new Scanner(System.in)) {
			System.out.print("Seleccione un nombre de producto: ");
			String name = scanneranner.next();
			Product product = findProduct(name);

			if (product != null) {
				// remove it
				if (inventory.remove(product)) {
					System.out.println("El producto " + name + " ha sido eliminado");

				} else {
					System.out.println("No se ha encontrado el producto con nombre " + name);
				}
			} else {
				System.out.println("No se ha encontrado el producto con nombre " + name);
			}
		}
	}

	/**
	 * add stock for a specific product
	 */
	public void addStock() {
		try (Scanner scanneranner = new Scanner(System.in)) {
			System.out.print("Seleccione un nombre de producto: ");
			String name = scanneranner.next();
			Product product = findProduct(name);

			if (product != null) {
				// ask for stock
				System.out.print("Seleccione la cantidad a añadir: ");
				int stock = scanneranner.nextInt();
				// update stock product
				product.setStock(product.getStock() + stock);
				System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());

			} else {
				System.out.println("No se ha encontrado el producto con nombre " + name);
			}
		}
	}

	/**
	 * set a product as expired
	 */
	private void setExpired() {
		try (Scanner scanneranner = new Scanner(System.in)) {
			System.out.print("Seleccione un nombre de producto: ");
			String name = scanneranner.next();

			Product product = findProduct(name);

			if (product != null) {
				product.expire();
				System.out.println("El precio del producto " + name + " ha sido actualizado a " + product.getPublicPrice());
			}
		}
	}

	/**
	 * show all inventory
	 */
	public void showInventory() {
		System.out.println("Contenido actual de la tienda:");
		for (Product product : inventory) {
			if (product != null) {
				System.out.println(product);
			}
		}
	}

	
	public Client getClientFromDB(String clientName) throws SQLException {
	    Client client = null;
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	    	
	    	String url = "jdbc:mysql://localhost:3306/shop";
            String dbUser = "root";
            String dbPassword = "12345678";

            connection = DriverManager.getConnection(url, dbUser, dbPassword);
	        String query = "SELECT * FROM client WHERE username = ?";
	        preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, clientName);
	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            int memberID = resultSet.getInt("memberID");
	            double balance = resultSet.getDouble("balance");
	            Amount amount = new Amount(balance);
	            client = new Client(clientName, memberID, amount);
	        } else {
	            System.out.println("Client not found.");
	        }
	    } finally {
	        if (resultSet != null) {
	            resultSet.close();
	        }
	        if (preparedStatement != null) {
	            preparedStatement.close();
	        }
	        if (connection != null) {
	            connection.close();
	        }
	    }

	    return client;
	}

	
	/**
	 * make a sale of products to a client
	 */
	public void sale() {
	    try (Scanner scanner = new Scanner(System.in)) {
	        System.out.println("Realizar venta, escribir nombre cliente:");
	        String clientName = scanner.nextLine();
	        
	        Client client = getClientFromDB(clientName);
	        if (client == null) {
	            System.out.println("Cliente no encontrado. Cancelando venta.");
	            return;
	        }

	        ArrayList<Product> shoppingCart = new ArrayList<>();
	        Amount totalAmount = new Amount(0.0);
	        String name = "";

	        while (!name.equals("0")) {
	            System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
	            name = scanner.nextLine();

	            if (name.equals("0")) {
	                break;
	            }
	            Product product = findProduct(name);
	            boolean productAvailable = false;

	            if (product != null && product.isAvailable()) {
	                productAvailable = true;
	                totalAmount.setValue(totalAmount.getValue() + product.getPublicPrice().getValue());
	                product.setStock(product.getStock() - 1);
	                shoppingCart.add(product);
	                if (product.getStock() == 0) {
	                    product.setAvailable(false);
	                }
	                System.out.println("Producto añadido con éxito");
	            }

	            if (!productAvailable) {
	                System.out.println("Producto no encontrado o sin stock");
	            }
	        }

	        totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
	        
	        // Actualizar saldo del cliente y registrar la venta
	        updateClientBalance(client, totalAmount);
	        System.out.println("Venta realizada con éxito, total: " + totalAmount);

	        Sale sale = new Sale(client.getName(), shoppingCart, totalAmount);
	        sales.add(sale);
	        cash.setValue(cash.getValue() + totalAmount.getValue());
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	private void updateClientBalance(Client client, Amount amount) throws SQLException {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    try {
	    	String url = "jdbc:mysql://localhost:3306/shop";
            String dbUser = "root";
            String dbPassword = "12345678";

            connection = DriverManager.getConnection(url, dbUser, dbPassword);
	        String query = "UPDATE client SET balance = ? WHERE memberID = ?";
	        preparedStatement = connection.prepareStatement(query);
	        double newBalance = client.getBalance().getValue() - amount.getValue();
	        preparedStatement.setDouble(1, newBalance);
	        preparedStatement.setInt(2, client.getMemberID());
	        preparedStatement.executeUpdate();
	        client.setBalance(new Amount(newBalance));
	    } finally {
	        if (preparedStatement != null) {
	            preparedStatement.close();
	        }
	        if (connection != null) {
	            connection.close();
	        }
	    }
	}


	/**
	 * show all sales
	 */
	private void showSales() {
		System.out.println("Lista de ventas:");
		for (Sale sale : sales) {
			if (sale != null) {
				System.out.println(sale);
			}
		}

		try (// ask for client name
		Scanner scanner = new Scanner(System.in)) {
			System.out.println("Exportar fichero ventas? S / N");
			String option = scanner.nextLine();
			if ("S".equalsIgnoreCase(option)) {
				this.writeSales();
			}
		}

	}

	/**
	 * write in file the sales done
	 */
	private void writeSales() {
		// define file name based on date
		LocalDate myObj = LocalDate.now();
		String fileName = "sales_" + myObj.toString() + ".txt";

		// locate file, path and name
		File f = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + fileName);

		try {
			// wrap in proper classes
			FileWriter fw;
			fw = new FileWriter(f, true);
			PrintWriter pw = new PrintWriter(fw);

			// write line by line
			int counterSale = 1;
			for (Sale sale : sales) {
				// format first line TO BE -> 1;Client=PERE;Date=29-02-2024 12:49:50;
				StringBuilder firstLine = new StringBuilder(
						counterSale + ";Client=" + sale.getClient() + ";Date=" + sale.formatDate() + ";");
				pw.write(firstLine.toString());
				fw.write("\n");

				// format second line TO BE ->
				// 1;Products=Manzana,20.0€;Fresa,10.0€;Hamburguesa,60.0€;
				// build products line
				StringBuilder productLine = new StringBuilder();
				for (Product product : sale.getProducts()) {
					productLine.append(product.getName() + "," + product.getPublicPrice() + ";");
				}
				StringBuilder secondLine = new StringBuilder(counterSale + ";" + "Products=" + productLine + ";");
				pw.write(secondLine.toString());
				fw.write("\n");

				// format third line TO BE -> 1;Amount=93.60€;
				StringBuilder thirdLine = new StringBuilder(counterSale + ";" + "Amount=" + sale.getAmount() + ";");
				pw.write(thirdLine.toString());
				fw.write("\n");

				// increment counter sales
				counterSale++;
			}
			// close files
			pw.close();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * show total amount all sales
	 */
	private void showSalesAmount() {
		Amount totalAmount = new Amount(0.0);
		for (Sale sale : sales) {
			if (sale != null) {
				totalAmount.setValue(totalAmount.getValue() + sale.getAmount().getValue());
			}
		}
		System.out.println("Total cantidad ventas:");
		System.out.println(totalAmount);
	}

	/**
	 * add a product to inventory
	 * 
	 * @param product
	 */
	public void addProduct(Product product) {
		if (isInventoryFull()) {
			System.out.println("No se pueden añadir más productos, se ha alcanzado el máximo de " + inventory.size());
			return;
		}
		inventory.add(product);
		numberProducts++;
	}

	/**
	 * check if inventory is full or not
	 */
	public boolean isInventoryFull() {
		if (numberProducts == 10) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * find product by name
	 * 
	 * @param product name
	 */
	public Product findProduct(String name) {
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i) != null && inventory.get(i).getName().equalsIgnoreCase(name)) {
				return inventory.get(i);
			}
		}
		return null;

	}

}