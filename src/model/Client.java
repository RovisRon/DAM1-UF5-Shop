package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.Payable;

public class Client extends Person implements Payable {

    int memberID;
    Amount balance;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/shop";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12346578";
    
    public Client(String name, int memberID, Amount balance) {
        super(name);
        this.memberID = memberID;
        this.balance = balance;
    }

    private Amount getBalanceFromDB(int memberID) throws SQLException {
        Amount amount = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "SELECT balance FROM clients WHERE memberID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, memberID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double balanceValue = resultSet.getDouble("balance");
                amount = new Amount(balanceValue);
            } else {
                
                throw new SQLException("Client with memberID " + memberID + " not found.");
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

        return amount;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public Amount getBalance() {
        return balance;
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
    }

    @Override
    public boolean pay(Amount amount) {
        double finalBalance = balance.getValue() - amount.getValue();
        boolean canPay = false;
        if (finalBalance > 0) {
            canPay = true;
        }
        return canPay;
    }
}
