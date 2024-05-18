package model;

public class Employee extends Person {

	private int employeeId;
	
	final static int USER = 123;
    final static String PASSWORD = "test";

	public Employee(String name, int employeeId) {
		super(name);
		this.employeeId = employeeId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	
	public boolean login(int user, String password) {
    	
        if (user == USER && password.equals(PASSWORD)) {
        	System.out.println("User "+user+" logged");
            return true;
        } else {
        	System.out.println("ERROR, wrong dates");
            return false;
        }
    }
    
}
