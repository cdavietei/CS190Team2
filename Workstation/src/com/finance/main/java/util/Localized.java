package com.finance.main.java.util;

/**
 * This interface defines a method to be implemented in any GUI window to allow
 * the main program controller to update the UI labels and text fields in the
 * event that the application's global language is changed. To be used with
 * {@link LocalizedStrings}.
 *
 * @author Christopher Davie
 * @version 1.0
 */
public interface Localized
{
    /**
     * Updates all the labels and text fields present within a GUI window to
     * the correct language in the event the language has changed
     * @return  True if labels updated successfully and false if not
     * @since  1.0
     */
    public boolean updateLabels();

}//Localized interface
