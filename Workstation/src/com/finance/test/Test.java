package com.finance.test;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

import com.finance.main.java.database.SQLiteConnector;
import com.finance.main.java.database.StockDatabaseInterface;
import com.finance.main.java.enums.*;
import com.finance.main.java.stock.Stock;
import com.finance.main.java.util.*;

public class Test
{

	public static void main(String[] args) throws Exception
	{
		
		
		StockDatabaseInterface stocks = new StockDatabaseInterface();
		
		stocks.addNewStock("GOOG");
		
		ArrayList<Stock> list = stocks.getStocks("GOOG", new Date(System.currentTimeMillis() - (10 * 1000 * 60 * 60 * 24)), new Date(System.currentTimeMillis()));
		
		System.out.println(list);
		
		/*
		SQLiteConnector con = new SQLiteConnector();
		
		ResultSet results = con.executeQuery("SELECT * FROM GOOG_info;");
		System.out.println(results.getString("Date"));
		
		results.close();
		con.close();
		*/
	}

}
