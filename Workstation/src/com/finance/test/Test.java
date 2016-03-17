package com.finance.test;

import java.io.*;
import com.finance.main.java.enums.*;
import com.finance.main.java.util.*;

public class Test
{

	public static void main(String[] args)
	{
		LocalizedStrings.setLanguage(Languages.ENGLISH_US);

		for(TextFields i : TextFields.values())
			System.out.printf("%s: %s\n",i.toString(),LocalizedStrings.getLocalString(i));

		LocalizedStrings.setLanguage(Languages.SPANISH);
		System.out.println();

		for(TextFields i : TextFields.values())
			System.out.printf("%s: %s\n",i.toString(),LocalizedStrings.getLocalString(i));
	}

}
