package main;

import model.Amount;
import model.Client;
import model.Employee;
import model.Product;
import model.Sale;
import view.ProductView;

import javax.swing.*;
import java.awt.Button;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Shop {
	public Amount cash = new Amount(100.00);
	private ArrayList<Product> inventory;
	private int numberProducts;
	private ArrayList<Sale> sales;
	
	final static double TAX_RATE = 1.04;
	//create a new variable to count the sales of the shop.
	private int countSales;
	
	public Shop() {
		// cash = 0.0; initial cash = 100.00 [CORRECTION]
	// Change array inventory to ArrayList inventory
		inventory = new ArrayList<Product>();		
		//create a new sales array to inset the sales [CORRECTION]
		sales = new ArrayList<Sale>();
		countSales = 0;
	}

	public static void main(String[] args) {
		Shop shop = new Shop();

			//shop.loadInventory();
			shop.readFileInventory();
		
		Scanner scanner = new Scanner(System.in);
		int opcion = 0;
		boolean exit = false;
		
// UF4 create initSession using Employee obj
	shop.initSession();

		do {
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
			System.out.println("8) Ver Precio total de ventas");
			System.out.println("9) Eliminar producto");
			System.out.println("10) Salir programa");
			System.out.print("Seleccione una opción: ");
			opcion = scanner.nextInt();

			switch (opcion) {
			case 1:
				shop.showCash();
				break;

			case 2:
				shop.addNewProduct();
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
				shop.showAmountVentas();
				break;
			case 9:
				shop.deleteProduct();
				break;
			case 10:
				exit = true;
				break;
			default:System.out.println("This option not exist");
			}
			
		} while (!exit);
		
	}

	public void showInv() {
		for (Product product : inventory) {
			if (product != null) {
				System.out.println(product);
			}
		}
	}
	public void loadInventory() {
		
		addProduct(new Product("Manzana", 10.00, true, 10));
		addProduct(new Product("Pera", 20.00, true, 20));
		addProduct(new Product("Hamburguesa", 30.00, true, 30));
		addProduct(new Product("Fresa", 5.00, true, 20));
		
	}
	private void readFileInventory() {
		int count = 0;
		boolean exit = false;
		String x = null; String y = null; String z = null;
		
		try {
			File fileInventory = new File("inputInventory.txt");
			if(fileInventory.exists()) {
				FileReader fr = new FileReader("inputInventory.txt");
				BufferedReader br = new BufferedReader(fr);
				while(exit == false) {
            		String myLine = br.readLine();
            		if(myLine != null) {
		            	String[] result1 = myLine.split(";");
		            	while(count<3) {
			            	String[] result2 = result1[count].split(":");
			            	if(count == 0) {
			            		 x = result2[1];
			            	}else if(count == 1) {
			            		 y = result2[1];
			            	}else if(count == 2) {
			            		 z = result2[1];
			            	}
			            	count++;
		            	}
		            	double price = Double.parseDouble(y);
		            	int stock = Integer.parseInt(z);
		            	addProduct(new Product(x, price, true, stock));
		            	count = 0;
            		}else {
            			exit = true;
            		}
				}
	            	fr.close();
	        		br.close();
			}else if(fileInventory.createNewFile()) {
            	System.out.println("File created: " + fileInventory.getName());
            	loadInventory();
            	FileWriter myWriter = new FileWriter("inputInventory.txt"); 
        					for (Product product : inventory) {
        		    			if (product != null) {
        		    				myWriter.write("Product:"+product.getName()+";Wholesaler Price:"
        		    			+product.getWholesalerPrice()+";Stock:"+product.getStock()+";\n");  
        		    			}
        					}        			        		
                System.out.println("File inventory finished");
                myWriter.close();
                readFileInventory();
            }
 
            
        } catch (IOException e) {
            System.out.println("Error: Archivo no encontrado");
            e.printStackTrace();
        }
		
	}
	private void writeNewInventory() {
		try {
			File fileInventory = new File("inputInventory.txt");
			FileWriter myWriter = new FileWriter("inputInventory.txt");
			if(fileInventory.exists()) {		
				for (Product product : inventory) {
	    			if (product != null) {
	    				myWriter.write("Product:"+product.getName()+";Wholesaler Price:"
	    			+product.getWholesalerPrice()+";Stock:"+product.getStock()+";\n");  
	    			}
				}        			        		
    System.out.println("File inventory finished");
    myWriter.close();
			}
		} catch (IOException e) {
            System.out.println("Error: Archivo no encontrado");
            e.printStackTrace();
        }
	}
	// write the new sales in the fileSales
	private void updateFileSales() {
		int numberSale = 1;
		
		try {
        	//create file
			LocalDate dateSaleFile = LocalDate.now();
            File fileSales = new File("sales"+dateSaleFile+".txt");
            if(fileSales.createNewFile()) {
            	System.out.println("File created: " + fileSales.getName());
            }else{
            	System.out.println("uploading file");
            	fileSales.delete();	
            }
            
            //write on the file the information
            FileWriter myWriter = new FileWriter("sales"+dateSaleFile+".txt");
            for (Sale sale : sales) {
    			if (sale != null) {
    					myWriter.write(numberSale+"; Cliente="+sale.getClient()+
    							";Date="+sale.getDate()+";\n");  
    					for (Product product : inventory) {
    		    			if (product != null) {
    		    				myWriter.write(numberSale+";Products="+product.getName()+","
    		    			+product.getPublicPrice()+";");  
    		    			}
    		    		}	
    					myWriter.write("\n"+numberSale+";Amount="+sale.getAmount()+";\n");
    				numberSale++;
    			}
    		}
            System.out.println("File finished");
            myWriter.close();
            
        } catch (IOException e) {
            System.out.println("Error: Archivo no encontrado");
            e.printStackTrace();
        }
		
	}
	
	/**
	 * show current total cash 
	 */
	
	// Show total cash [CORRECTION]
	private void showCash() {
		System.out.println("Dinero actual: "+ cash.toString());
	}

	/**
	 * add a new product to inventory getting data from console
	 */
	
	// changed the name of methot [CORRECTION]
	public void addNewProduct() {
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Nombre: ");
		String name = scanner.nextLine();
		
		// check if name exist if not return nothing [CORRECTION]
		Product product = findProduct(name);
			if(product != null){
				System.out.println("The product alredy exists");
				return;
			}
		//
		System.out.print("Precio mayorista: ");
		double wholesalerPrice = scanner.nextDouble();
		System.out.print("Stock: ");
		int stock = scanner.nextInt();

		addProduct(new Product(name, wholesalerPrice, true, stock));

		writeNewInventory();
	}

	public void inventory(String name, double wholesalerPrice, boolean available, int stock) {
		try {
			File fileInventory = new File("inputInventory.txt");
			ArrayList<String> inventoryLines = new ArrayList<>();

			if (fileInventory.exists()) {
				Scanner myReader = new Scanner(fileInventory);
				while (myReader.hasNextLine()) {
					inventoryLines.add(myReader.nextLine());
				}
				myReader.close();

				for (String line : inventoryLines) {
					if (line.contains("Product:" + name + ";")) {
						throw new Exception("El Producto ya existe en el inventario");
					}
				}

				inventoryLines.add("Product:" + name + ";Wholesaler Price:" + wholesalerPrice + ";Stock:" + stock + ";");

				FileWriter myWriter = new FileWriter("inputInventory.txt");
				for (String line : inventoryLines) {
					myWriter.write(line + "\n");
				}
				myWriter.close();

				System.out.println("Producto agregado con éxito al inventario.");
			} else {
				System.out.println("Error: Archivo de inventario no encontrado.");
			}
		} catch (IOException e) {
			System.out.println("Ocurrió un error al agregar el producto al inventario.");
		} catch (Exception ex) {
			// Mostrar mensaje de error usando JOptionPane
			ex.printStackTrace();
			ProductView productView = new ProductView();
			productView.setExeption(true);
		}
	}

	public void productStock(String name, int stock) {
		try {
			File fileInventory = new File("inputInventory.txt");
			ArrayList<String> inventoryLines = new ArrayList<>();
			boolean productFound = false;

			if (fileInventory.exists()) {
				Scanner myReader = new Scanner(fileInventory);
				while (myReader.hasNextLine()) {
					String line = myReader.nextLine();
					if (line.contains("Product:" + name + ";")) {
						// Actualizar el stock del producto existente
						String stockString = line.split(";Stock:")[1].replaceAll("[^\\d]", "");
						int currentStock = Integer.parseInt(stockString);
						currentStock += stock;
						line = line.replaceFirst(";Stock:\\d+;", ";Stock:" + currentStock + ";");
						productFound = true;
					}

					// Agregar la línea actualizada o la línea original a la lista
					inventoryLines.add(line);
				}
				myReader.close();

				if (!productFound) {
					System.out.println("Producto no encontrado en el inventario.");
					ProductView productView = new ProductView();
					productView.setExeptionProductNotFound(true);
					return;
				}

				FileWriter myWriter = new FileWriter("inputInventory.txt");
				for (String line : inventoryLines) {
					myWriter.write(line + "\n");
				}
				myWriter.close();

				System.out.println("Stock del producto actualizado con éxito en el inventario.");


			} else {
				System.out.println("Error: Archivo de inventario no encontrado.");
			}
		} catch (IOException e) {
			System.out.println("Ocurrió un error al actualizar el stock del producto en el inventario.");
			ProductView productView = new ProductView();
			productView.setExeption(true);
		}
	}



	/**
	 * add stock for a specific product
	 */
	public void addStock() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();
		Product product = findProduct(name);

		if ((product != null)) {
		//make available if stock = 0 [CORRECTION]
			if(product.getStock() == 0) {
				product.setAvailable(true);
			}
			// ask for stock
			//System.out.println(product.getStock()); Stock after change
			System.out.print("Seleccione la cantidad a añadir: ");
			int stock = scanner.nextInt();
			// update stock product
		//plus the stock that you write [CORRECTION]
			product.setStock(product.getStock() + stock);
			System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());
			writeNewInventory();
			//System.out.println(product.getStock()); Stock before change
		} else {
			System.out.println("No se ha encontrado el producto con nombre " + name);
		}
	}


	/**
	 * set a product as expired
	 */
	private void setExpired() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();

		Product product = findProduct(name);

		if (product != null) {
			//use methot expire() [CORRECTION]
			product.expire();
			System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getPublicPrice());
		}
	}

	/**
	 * show all inventory
	 */
	public void showInventory() {
		System.out.println("Contenido actual de la tienda:");
		try {
			File fileInventory = new File("inputInventory.txt");
		    Scanner myReader = new Scanner(fileInventory);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        System.out.println(data);
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}

	/**
	 * make a sale of products to a client
	 */
	public void sale() {
		// ask for client name
	//Create account Products
		ArrayList<Product> products = new ArrayList<Product>();
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Realizar venta, escribir nombre cliente");
		String nameClient = sc.nextLine();
	//Create object Client 
		Client client = new Client(nameClient);
		// sale product until input name is not 0
		Amount totalAmount = new Amount(0);
		String name = "";
		while (!name.equals("0")) {
// show the list of products [CORRECTION]
			showInventory();
			System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
			name = sc.nextLine();
			
			if (name.equals("0")) {
				break;
			}
			Product product = findProduct(name);
			boolean productAvailable = false;

			if (product != null && product.isAvailable()) {
				productAvailable = true;
				double sum = totalAmount.getValue() + product.getPublicPrice();
				totalAmount.setValue(sum); 
				product.setStock(product.getStock() - 1);
				// if no more stock, set as not available to sale
				if (product.getStock() == 0) {
					product.setAvailable(false);
				}
				//add product to products
				System.out.println("Producto añadido con éxito");
				products.add(product);
			}

			if (!productAvailable) {
				System.out.println("Producto no encontrado o sin stock");
			}

		}
		
		// show cost total
		double sum = totalAmount.getValue() * TAX_RATE;
		totalAmount.setValue(sum);
		cash.setValue(totalAmount.getValue() + cash.getValue());
		System.out.println("Venta realizada con éxito, total " + totalAmount +"\n");
		client.pay(totalAmount);

		
		LocalDateTime date = LocalDateTime.now();
		
		// Create a new sale and add to array sales [CORRECTION]	
		Sale sale = new Sale(client, products, totalAmount, date);
		sales.add(sale);
		
		writeNewInventory();
	}
	
	/**
	 * show all sales
	 */
	private void showSales() {
		Scanner sc = new Scanner(System.in);
		//View the sales [CORRECTION]
		boolean check;
		if(sales != null) {
		System.out.println("Lista de ventas:");
			for (Sale sale : sales) {
				if (sale != null) {
					System.out.println(sale.toString());
				}
			}
			do {
				check = false;
			System.out.println("Do you what to safe this file sales (Y/N)");
			String button = sc.next();
			if(button.equalsIgnoreCase("Y")) {
				updateFileSales();
				check = true;
			}else if(button.equalsIgnoreCase("N")) {
				check = true;
			}else {
				System.out.println("Error, please insert Y or N");
			}
			}while(check == false);
		}else {
			System.out.println("There are not sales");
		}		
	}

	public void showAmountVentas() {
		double total = 0;
		if(sales != null) {
			for (Sale sale : sales) {
				if (sale != null) {
					total += sale.getAmount().getValue();
				}
			}
		}
		
		System.out.println("Amount sales = "+total+Amount.getCurrency());
		
	}

	public void delForProductView(String productName) {
		try {
			File fileInventory = new File("inputInventory.txt");
			ArrayList<String> inventoryLines = new ArrayList<>();
			boolean productFound = false;

			if (fileInventory.exists()) {
				Scanner myReader = new Scanner(fileInventory);
				while (myReader.hasNextLine()) {
					String line = myReader.nextLine();
					if (line.contains("Product:" + productName + ";")) {
						// Eliminar la línea que contiene el producto
						continue;
					}
					// Agregar la línea actualizada o la línea original a la lista
					inventoryLines.add(line);
				}
				myReader.close();

				productFound = true; // Marcamos como encontrado si llegamos aquí

				FileWriter myWriter = new FileWriter("inputInventory.txt");
				for (String line : inventoryLines) {
					myWriter.write(line + "\n");
				}
				myWriter.close();

				System.out.println("El producto fue eliminado con éxito del inventario.");
			} else {
				System.out.println("Error: Archivo de inventario no encontrado.");
			}
		} catch (IOException e) {
			System.out.println("Ocurrió un error al borrar el producto del inventario.");
			ProductView productView = new ProductView();
			productView.setExeption(true);
		}
	}


	// method delete product.
	public void deleteProduct() {
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();
		Product product = findProduct(name);
		
		if(product != null) {
			for (int i=0;i<inventory.size();i++) {
				if (product != null) {
					inventory.remove(product);
				}
			}
			System.out.println(product.getName()+" was deleted");
			//Rewrite the inventory without the product
			writeNewInventory();
		}else {
			System.out.println("This product not exists");
		}
	}
	
	
	/**
	 * add a product to inventory
	 * 
	 * @param product
	 */
	public void addProduct(Product product) {
		inventory.add(product);
	}
	
	
	/**
	 * check if inventory is full or not
	 */
	
	/*
	 * public boolean isInventoryFull() { 
	 * 		if (numberProducts == 10) {
	 *  		return true; 
	 * 		}else { 
	 * 			return false; 
	 * 		} 
	 * }
	 */


	public Product findProduct(String name) {
		for (int i = 0; i < inventory.size(); i++) {
			// add IgnoreCase to not key sensitive [CORRECTION]
			if (inventory.get(i) != null && inventory.get(i).getName().equalsIgnoreCase(name)) {
				return inventory.get(i);
			}
		}
		return null;

	}

	public void initSession(){
		Scanner sc = new Scanner(System.in);
		boolean logged;
		System.out.println("Name User: ");
		String user = sc.next();
		do {
			
			System.out.println("ID User: ");
			int iduser = sc.nextInt();
			System.out.println("Password: ");
			String password = sc.next();
			
			Employee employee = new Employee(user, iduser);
			
			logged = employee.login(iduser, password);
			
		}while(logged == false);
	}
	
}