package model;

import main.Logable;
import dao.Dao;
import dao.DaoImplJDBC;

public class ExternalEmployee extends Person implements Logable{
	
	int employeeID;
	int user;
	String password;
	String companyName = "Unknonwn";
	private Dao dao = new DaoImplJDBC();
	
	public ExternalEmployee(String name, int employeeID, String password, String companyName) {
		super(name);
		this.employeeID = employeeID;
		this.password = password;
		this.companyName = companyName;
	}
	
	public int getUSER() {
		return user;
	}
	public String getPASSWORD() {
		return password;
	}
	public String getCOMPANYNAME() {
		return companyName;
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
