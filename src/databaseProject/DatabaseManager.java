package databaseProject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DatabaseManager 
{
	public static void main(String[] args) 
	{
		//Open the connection to the sqlite db
		Connection customerDatabase = openDatabase("customerDatabase.db");
		
		//Create and join the tables in customerDatabase; only run this once then comment out
		//buildDatabase(customerDatabase);
		
		//Start program loop
		boolean finished = false;
		int selection = 0;
		while (finished == false)
		{
			selection = mainMenuPrompt(new Scanner(System.in));
			switch(selection)
			{
			//1: Add a new customer to the database
			case 1:
				insertPerson(newPersonPrompt(new Scanner(System.in)), customerDatabase);
				break;
			//2: Search for a customer in the database
			case 2:
				findPersonQuery(new Scanner(System.in), customerDatabase);
				break;
			//3: List all current customers (by name)
			case 3:
				findAllPeople(customerDatabase);
				break;
			//4: Delete a customer from the database
			case 4:
				deleteCustomerPrompt(new Scanner(System.in), customerDatabase);
				break;
			//Close the database and exit the program
			default:
				finished = true;
			}
		}
		
		System.out.println("Closing database...");
		System.out.println("Have a nice day");
		
		//Close the database connection
		try
		{
			customerDatabase.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.exit(0);
		}
	}
	
	//Main menu prompt
	public static int mainMenuPrompt(Scanner userSelection)
	{
		int selection = 0;
		
		System.out.println("Please select an option:");
		System.out.println("1: Add a new customer to the database.");
		System.out.println("2: Search for a customer in the database.");
		System.out.println("3: List all current customers (by name).");
		System.out.println("4: Delete a customer from the database.");
		System.out.println("0: Close the database and exit the program.");
		
		selection = userSelection.nextInt();
		
		return selection;
	}
	
	//Establish a connection to a database; if the sqlite db doesn't exist an empty one will be created
	public static Connection openDatabase(String filename)
	{
		Connection newConnection = null;
		
		try
		{
			Class.forName("org.sqlite.JDBC");
			newConnection = DriverManager.getConnection("jdbc:sqlite:" + filename);
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.exit(0);
		}
		
		System.out.println("Opened database successfully.");
		
		return newConnection;
	}
	
	//Create a new person object
	public static Person newPersonPrompt(Scanner userInput)
	{
		System.out.println("What is the customer's first name?");
		String personFirstName = userInput.next();
		
		System.out.println("What is the customer's last name?");
		String personLastName = userInput.next();
		
		System.out.println("How old is the customer (in years)?");
		int personAge = userInput.nextInt();
		
		System.out.println("What is the customer's SSN (exclude dashes)?");
		long personSSN = userInput.nextLong();
		
		System.out.println("What card did the customer use?");
		long personCard = userInput.nextLong();
		
		return new Person(personFirstName, personLastName, personAge, personSSN, personCard);
	}
	
	//Insert a new Person into the database
	public static void insertPerson(Person newPerson, Connection customerDatabase)
	{
		String newPersonSQL =
			"INSERT INTO Customer (Customer_FirstName, Customer_LastName, Customer_Age, Customer_SSN, Customer_CardNumber) " +
				"VALUES" +
				"(" +
					"'" + newPerson.getFirstName() + "', " +
					"'" + newPerson.getLastName() + "', " +
					newPerson.getAge() + ", " +
					newPerson.getSSN() + ", " +
					newPerson.getCustomerCard() +
				");";
		executeStatement(newPersonSQL, customerDatabase);
		
		System.out.println("Customer record added");
	}
	
	//Find a Person in the database
	public static void findPersonQuery(Scanner userInput, Connection customerDatabase)
	{
		//Prompt user for name criteria
		System.out.println("What is the customer's first name? (use '?' if unknown)");
		String searchFirstName = userInput.next();
		
		System.out.println("What is the customer's last name? (use '?' if unknown)");
		String searchLastName = userInput.next();
		
		//Query SQL to search by name
		String personQuery = null;
		if(searchFirstName.equals("?") == true && searchLastName.equals("?") == false)
			personQuery = queryAll() + "WHERE (Customer_LastName = '" + searchLastName + "')";
		else if(searchLastName.equals("?") == true && searchFirstName.equals("?") == false)
			personQuery = queryAll() + "WHERE (Customer_FirstName = '" + searchFirstName + "')";
		else if(searchFirstName.equals("?") == false && searchLastName.equals("?") == false)
			personQuery = queryAll() + "WHERE (Customer_FirstName = '" + searchFirstName + "' AND Customer_LastName = '" + searchLastName + "')";
		else
			personQuery = queryAll();
		
		//Store the query results
		ResultSet findPersonResults = queryResults(personQuery, customerDatabase);
		
		displayResults(findPersonResults);
	}
	
	//Return all customers in the database
	public static void findAllPeople(Connection customerDatabase)
	{
		ResultSet findAllResults = queryResults(queryAll(), customerDatabase);
		displayResults(findAllResults);
	}
	
	//Display results
	public static void displayResults(ResultSet findPersonResults)
	{
		
		ArrayList<Integer> personIDs = new ArrayList<Integer>();
		ArrayList<Person> people = new ArrayList<Person>();
		
		try 
		{
			if(findPersonResults.next())
			{
				do
				{
					personIDs.add(findPersonResults.getInt("Customer_ID"));
					people.add
						(new Person
							(
							findPersonResults.getString("Customer_FirstName"), 
							findPersonResults.getString("Customer_LastName"),
							findPersonResults.getInt("Customer_Age"),
							findPersonResults.getLong("Customer_SSN"), 
							findPersonResults.getLong("Customer_CardNumber")
							)
						);
				} while(findPersonResults.next());
			}
			else
				System.out.println("No customers with those names were found.");
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		int counter = 1;
		
		for(counter = 1; counter <= personIDs.size(); counter++)
		{
			System.out.print("Customer ID: " + personIDs.get(counter - 1) + "\n");
			System.out.print(people.get(counter - 1) + "\n");
		}
	}
	
	//Delete customer prompt
	public static void deleteCustomerPrompt(Scanner userInput, Connection customerDatabase)
	{
		System.out.println("Enter the Customer ID of the customer you wish to delete.");
		System.out.println("If the ID is not known, enter a numerical zero (0)");
		int userSelection = userInput.nextInt();
		
		switch(userSelection)
		{
		case 0:
			findPersonQuery(userInput, customerDatabase);
			deleteCustomerPrompt(userInput, customerDatabase);
			break;
		default:
			deleteCustomer(userSelection, customerDatabase);
			System.out.println("Customer record deleted.");
		}
	}
	
	//Delete a customer from the database
	public static void deleteCustomer(int customerID, Connection customerDatabase)
	{
		String deleteSQL = deleteRow() + "WHERE Customer_ID = " + customerID;
		
		executeStatement(deleteSQL, customerDatabase);
	}
	
	public static String deleteRow()
	{
		return "DELETE FROM Customer ";
	}
	
	//Method that returns SQL string to return all records
	public static String queryAll()
	{
		String querySQL =
			"SELECT * " +
			"FROM Customer ";
		return querySQL;
	}
	
	//Execute query and return a result set
	public static ResultSet queryResults(String sql, Connection customerDatabase)
	{
		ResultSet newResultSet = null;
		Statement sqlStatement = null;
		
		try
		{
			sqlStatement = customerDatabase.createStatement();
			newResultSet = sqlStatement.executeQuery(sql);
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.out.print("SQL statement: \n" + sql + "\n");
			System.exit(0);
		}
		
		return newResultSet;
	}
	
	//Execute any single statement
	public static void executeStatement(String sql, Connection customerDatabase)
	{
		Statement sqlStatement;
		
		try
		{
			sqlStatement = customerDatabase.createStatement();
			sqlStatement.executeUpdate(sql);
			sqlStatement.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.out.print("SQL statement: \n" + sql + "\n");
			System.exit(0);
		}
	}
	
	//Execute the statement to create the tables in the database; only run this once
	public static void buildDatabase(Connection customerDatabase)
	{
		//SQL statements
		String sqlCustomerTable =
				"CREATE TABLE Customer " +
				"(" +
					"Customer_ID INT PRIMARY KEY NOT NULL, " +
					"Customer_FirstName TEXT NOT NULL, " +
					"Customer_LastName TEXT NOT NULL, " +
					"Customer_Age INT NOT NULL, " +
					"Customer_SSN INT NOT NULL, " +
					"Customer_CardNumber INT, " +
					"FOREIGN KEY(Customer_CardNumber) REFERENCES CreditCard(CreditCard_CardNumber)" +
				")";
		String sqlCreditCardTable = 
				"CREATE TABLE CreditCard " +
				"(" +
					"CreditCard_CardNumber INT PRIMARY KEY NOT NULL, " +
					"CreditCard_CompanyName TEXT NOT NULL, " +
					"CreditCard_ExpireDate INT NOT NULL, " +
					"CredditCard_CVV INT NOT NULL" +
				")";
		Statement sqlStatement;
		
		try
		{
			sqlStatement = customerDatabase.createStatement();
			sqlStatement.executeUpdate(sqlCustomerTable);
			sqlStatement.executeUpdate(sqlCreditCardTable);
			sqlStatement.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.exit(0);
		}
	}

}
