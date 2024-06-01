package model;

import main.Logable;

public class Employee extends Person implements Logable{
	
	int employeeID;
	final static int USER = 123;
	final static String PASSWORD = "test";
	
	public Employee(String name, int employeeID) {
		super(name);
		this.employeeID = employeeID;
	}
	public int getUSER() {
		return USER;
	}
	public String getPASSWORD() {
		return PASSWORD;
	}
	@Override
	public boolean login(int user, String password) {
        boolean isLogged = false;
        if (user == USER && password.equals(PASSWORD)) {
            isLogged = true;
            System.out.println("Inici de sessió correcte");
        }
        return isLogged;
    }
}
