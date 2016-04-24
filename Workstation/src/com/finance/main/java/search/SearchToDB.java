package com.finance.main.java.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.finance.main.java.database.SQLiteConnector;
import com.finance.main.java.enums.TableFields;
import com.finance.main.java.enums.Tables;

/*
public interface SearchToDB {
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
}*/

public class SearchToDB
{
	protected SQLiteConnector connection;
	
	protected static final String COMPANIES_FILE = "Resources/companies.csv";
	
	public SearchToDB()
	{
		connection = new SQLiteConnector();	
		checkTable();
	}
	
	public boolean checkTable()
	{
		boolean value = false;
		if(!connection.tableExists(Tables.COMPANIES.toString())){
				value = createTable();
		}
		else
		{
			String query = String.format("SELECT %s FROM %s;",
					"Symbol", Tables.COMPANIES.toString());
			ResultSet results = connection.executeQuery(query);
			try
			{
				if(!results.next())
					createTable();
				results.close();
				
				value = true;
			}
			catch(Exception e)
			{
				System.out.println(e.getLocalizedMessage());
			}
		}
		
		return value;
	}
	
	public String isCompanyName(String userInput)
	{
		String query = String.format("SELECT %s FROM %s WHERE %s LIKE '%%%s%%' OR %s = '%s';",TableFields.SYMBOL.toString(),Tables.COMPANIES.toString(), 
				TableFields.COMPANY_NAME.toString(), userInput,TableFields.COMPANY_NAME.toString(),userInput);
		ResultSet results = connection.executeQuery(query);
		
		String symbol = "";
		try
		{
			if(results.next())
				symbol = results.getString(1);
			
			results.close();
		}
		catch (SQLException e)
		{
			System.out.println(e.getLocalizedMessage());
		}
		
		connection.close();
		return symbol;
	}
	
	public boolean isStockName(String userInput)
	{
		if(userInput.length() > 5)
			return false;
		String query = String.format("SELECT %s FROM %s WHERE %s = '%s' OR %s LIKE '%%%s%%';",
				TableFields.SYMBOL.toString(), Tables.COMPANIES.toString(),
				TableFields.SYMBOL.toString(),userInput,TableFields.SYMBOL.toString(),userInput);
		System.out.println(query);
		ResultSet results = connection.executeQuery(query);
		boolean value = false;
		try
		{
			if(results.next())
				value = true;
			
			if(!results.isClosed())
				results.close();
		}
		catch(SQLException e)
		{
			System.out.println(e.getLocalizedMessage());
		}
		
		connection.close();
		
		return value;
	}
	
	public boolean createTable()
	{
		BufferedReader reader;
		
		boolean created = false;
		
		try
		{
			reader = new BufferedReader(new FileReader(COMPANIES_FILE));
			
			ArrayList<String> list = new ArrayList<String>();
			while(reader.ready())
			{
				String[] variables = splitString(reader.readLine());
				
				list.add(variables[0]);
				list.add(variables[1]);
			}
			
			reader.close();
			
			String query = String.format("INSERT INTO %s (%s,%s) VALUES (?,?)",
					Tables.COMPANIES.toString(), TableFields.SYMBOL.toString(), TableFields.COMPANY_NAME.toString());
			String[] arr = list.toArray(new String[list.size()]);
			created = connection.batchUpdate(query, arr);
		}
		catch(Exception e)
		{
			System.out.println("createTable()"+e.getLocalizedMessage());
		}
		
		return created;
	}
	
	public String[] splitString(String full)
	{
		int comma = full.indexOf(',');
		String[] values = new String[2];
		
		values[0] = full.substring(0, comma).replaceAll("\"", "");
		values[1] = full.substring(comma+1,full.length()).replaceAll("\"", "");
			
		return values;		
	}
}
