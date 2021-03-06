package com.finance.main.java.util;

import java.util.HashMap;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import com.finance.main.java.enums.*;


/**
 * Stores all the labels for every text field internally to allow the
 * application to dynamically switch between languages and for the labels to be
 * consistent.
 *
 * To use simply call {@code LocalizedStrings.getLocalString(TextFields)} with
 * the desired label or text field name.
 *
 * To update language either set language variable to new Enum from
 * {@link Languages} and call {@code LocalizedStrings.update()}
 * or call LocalizedStrings.setLanguage(Languages).
 *
 * @author Christopher Davie
 * @version 1.0
 */
public class LocalizedStrings
{

	/* The current and previous language of the application */
	public static Languages language;
	protected static Languages prevLang;

	/* The directory of the language files, set at start up */
	public static final String DIRECTORY = "Resources/";

	/* The internal dictionary of the application */
	protected static HashMap<TextFields, String> dictionary;

	/**
	 * Updates the global language of the application to the one currently set
	 * for the language variable
	 * @return 		True if language was successfully set and false is changing
	 *              the language failed
	 * @since 1.0
	 */
	public static boolean update()
	{
		return setLanguage(language);
	}//update()

	/**
	 * Sets the global language of the application using an Enum from
	 * {@link Languages}
	 * @param  		lang An Enum value of the language to be set, from
	 *              Languages class
	 * @return		True if language was successfully set and false is changing
	 *              the language failed
	 * @since 1.0
	 */
	public static boolean setLanguage(Languages lang)
	{
		/* Checks to see if new language is the one already set */
		if(language != null && lang == language)
			return true;
		else
		{
			/* Records the previous language and sets new language */
			prevLang = language;
			language = lang;

			/* Updates the internal dictionary */
			String file = DIRECTORY+language.name()+".xml";
			dictionary = setUpDictionary(file);

			/* Checks if the dictionary was successfully created and returns */
			return dictionary == null ? false : true;
		}//else
	}//setLanguage(Languages)

	/**
	 * Sets up the application's internal dictionary in order to handle various
	 * languages and allows for the application to dynamically change between
	 * different languages at run time.
	 * @param  file    The path to the desired language XML file
	 * @return         A new HashMap containing the associated fields and text
	 * @since  1.0
	 */
	protected static HashMap<TextFields, String> setUpDictionary(String file)
	{
		/* Creates a null object incase an exception is thrown */
		HashMap<TextFields, String> dict = null;

		try
		{
			/* Opens the file containing the language and keys */
			File inputFile = new File(file);

			DocumentBuilder builder =
				DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document doc = builder.parse(inputFile);

			/* Gets a list of all available labels */
			NodeList list = doc.getElementsByTagName("text");
			list = ((Element)list.item(0)).getElementsByTagName("label");

			int length = list.getLength();

			dict = new HashMap<TextFields, String>(length);

			/* Pushes all the respective label keys and labels to the internal
			 dictionary */
			for(int i = 0; i < length; i++)
			{
				Element node = (Element)list.item(i);
				dict.put(TextFields.valueOf(node.getAttribute("id")),
						node.getTextContent());
			}//for i
		}//try
		catch(Exception e)
		{
			System.out.println(e.getLocalizedMessage());
		}//catch(Exception)

		//TODO Handle all types of exceptions differently

		return dict;
	}//setUpDictionary(String)

	/**
	 * Returns a localized String for the given text field based on the global
	 * language of the application.
	 * @param  field    A given TextFields enum denoting a text field
	 * @return  The localized String associated with the given field
	 * @since  1.0
	 */
	public static String getLocalString(TextFields field)
	{
		return dictionary != null ? dictionary.get(field) : null;
	}//getLocalString(TextFields)

}//LocalizedStrings class
