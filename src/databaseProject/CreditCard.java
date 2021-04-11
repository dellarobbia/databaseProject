package databaseProject;

import java.time.LocalDateTime;

public class CreditCard 
{
	private long cardNumber;
	private String cardCompanyName;
	private LocalDateTime expireDate;
	private long cvvNum;

	//Constructor
	public CreditCard(long cardNumber, String cardCompanyName, LocalDateTime expireDate, long cvvNum) 
	{
		setCardNumber(cardNumber);
		setCardCompanyName(cardCompanyName);
		setExpireDate(expireDate);
		setCVVNum(cvvNum);
	}

	//Getters/Setters
	public long getCardNumber() 
	{
		return cardNumber;
	}
	public void setCardNumber(long cardNumber) 
	{
		this.cardNumber = cardNumber;
	}

	public String getCardCompanyName() 
	{
		return cardCompanyName;
	}
	public void setCardCompanyName(String cardCompanyName) 
	{
		this.cardCompanyName = cardCompanyName;
	}

	public LocalDateTime getExpireDate() 
	{
		return expireDate;
	}
	public void setExpireDate(LocalDateTime expireDate) 
	{
		this.expireDate = expireDate;
	}

	public long getCVVNum() 
	{
		return cvvNum;
	}
	public void setCVVNum(long cvvNum) 
	{
		this.cvvNum = cvvNum;
	}

}
