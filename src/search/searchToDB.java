
public interface searchToDB {
	String isCompanyName(String userInput);
	//isCompanyName takes the users input as a parameter, and returns
	//a string based on what the database lookup finds. If it finds that
	//what the user inputed is a company name, it returns the stock name
	//as a string. If it doesn't find a company named that, it returns an empty
	//string ("").
	boolean isStockName(String userInput);
	//isStockName takes the users input as a parameter and returns true
	//if the stock name is in our database, or returns false if it is not in 
	//the database.
	boolean searchYQL(String stockName);
	//searchYQL is the last step in the search functionality, if there
	//is a stock name found either through looking up the company name
	//or by looking up the stock name, then searchYQL calls the query function for
	//YQL to lookup the stock. It returns true if the search was successful,
	//false if the search was unsuccessful. This function will not be called if
	//a stock name is not found.
}
