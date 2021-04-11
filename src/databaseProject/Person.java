package databaseProject;

public class Person 
{
	private String firstName;
	private String lastName;
	private int age;
	private long ssn;
	private long customerCard;

	public Person(String firstName, String lastName, int age, long ssn) 
	{
		setFirstName(firstName);
		setLastName(lastName);
		setAge(age);
		setSSN(ssn);
	}
	
	public Person(String firstName, String lastName, int age, long ssn, long customerCard)
	{
		setFirstName(firstName);
		setLastName(lastName);
		setAge(age);
		setSSN(ssn);
		setCustomerCard(customerCard);
	}

	//ToString
	public String toString()
	{
		String personString = 
			"Name: " + getFirstName() + " " + getLastName() + "\n" +
					"\tAge: " + getAge() + "\n" +
					"\tSSN: " + getSSN() + "\n" +
					"\tCredit Card# " + getCustomerCard() + "\n";
		
		return personString;
	}
	
	//Getters & Setters
	public String getFirstName() 
	{
		return firstName;
	}
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}

	public String getLastName() 
	{
		return lastName;
	}
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}

	public int getAge() 
	{
		return age;
	}
	public void setAge(int age) 
	{
		this.age = age;
	}

	public long getSSN() 
	{
		return ssn;
	}
	public void setSSN(long ssn) 
	{
		this.ssn = ssn;
	}

	public long getCustomerCard() 
	{
		return customerCard;
	}
	public void setCustomerCard(long customerCard) 
	{
		this.customerCard = customerCard;
	}

}
