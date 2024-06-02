package model;

import main.Logable;
import dao.Dao;
import dao.DaoImplJDBC;

public class Employee extends Person implements Logable{
	
	int employeeID;
	int user;
	String password;
	private Dao dao = new DaoImplJDBC();
	
	public Employee(String name, int employeeID, String password) {
		super(name);
		this.employeeID = employeeID;
		this.password = password;
	}
	
	public int getUSER() {
		return user;
	}
	public String getPASSWORD() {
		return password;
	}
	
	@Override
	public boolean login(int user, String password) {
		boolean success = false;
    	dao.connect();
    	if(dao.getEmployee(user,password) != null) {
    		success = true;
    	}
    	dao.disconnect();
    	return success;
    }
}
