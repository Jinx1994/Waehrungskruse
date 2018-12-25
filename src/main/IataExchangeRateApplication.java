package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.File;

public class IataExchangeRateApplication {
	
	public void run() throws Exception {

		List<String> dataStructure = new ArrayList<>();
		readIataExchangeRates(dataStructure);
		printDataEntries(dataStructure);

		displayMenu();
		
		boolean exitRequested = false;
		
		while(!exitRequested) {
			String userInput = getUserInput();
			
			exitRequested = processUserInputAndCheckForExitRequest(userInput, dataStructure);
		}
		
		System.out.println("Auf Wiedersehen!");
	}
	
	private boolean readIataExchangeRates(List<String> dataStructure) {
		//TODO: Hier muss das Einlesen der IATA-W�hrungskurse aus der Datei geschehen.
		try
		{
			String filePath = getUserInputForStringField("Full path of CSV file");
			File csvFile = new File(filePath);

			if(csvFile.exists())
			{
				writeIataExchangeRatesInDataStructure(csvFile, dataStructure);
				return true;
			}
			else
			{
				System.out.println("Invalid path!");
				System.out.println();
				readIataExchangeRates(dataStructure);
				return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private void displayMenu() {
		System.out.println("IATA W�hrungskurs-Beispiel");
		System.out.println();
		
		System.out.println("W�hlen Sie eine Funktion durch Auswahl der Zifferntaste und Dr�cken von 'Return'");
		System.out.println("[1] W�hrungskurs anzeigen");
		System.out.println("[2] Neuen W�hrungskurs eingeben");
		System.out.println();
		
		System.out.println("[0] Beenden");
	}
	
	private String getUserInput() throws Exception {
		BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
		
		return consoleInput.readLine().trim();
	}
	
	//Returns true when the user wants to exit the application 
	private boolean processUserInputAndCheckForExitRequest(String userInput, List<String> dataStructure) throws Exception {
		if(userInput.equals("0")) {
			return true;
		}
		
		if(userInput.equals("1")) {
			displayIataExchangeRate(dataStructure);
		} else if(userInput.equals("2")) {
			enterIataExchangeRate();
		} else {
			System.out.println("Falsche Eingabe. Versuchen Sie es bitte erneut.");
		}
		
		return false;
	}
	
	private boolean displayIataExchangeRate(List<String> dataStructure) throws Exception {
		try
		{
			String currencyIsoCode = getUserInputForStringField("W�hrung");
			LocalDate date = getUserInputForDateField("Datum");

			for(String dataEntry: dataStructure)
			{
				String[] data = dataEntry.split(",");
				double euroValue = 1/Double.parseDouble(data[0]);
				if(checkIfInputForDisplayingIataExchangeRatesIsValid(currencyIsoCode, date, data))
				{
					System.out.println("1 " + data[1] + " entspricht " + euroValue + " Euro");
					return true;
				}
			}

			System.out.println("Eingaben ungültig. Bitte versuchen Sie es erneut.");
			return false;
			//TODO: Mit currencyIsoCode und date sollte hier der Kurs ermittelt und ausgegeben werden.
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private void enterIataExchangeRate() throws Exception {
		String currencyIsoCode = getUserInputForStringField("W�hrung");
		LocalDate from = getUserInputForDateField("Von");
		LocalDate to = getUserInputForDateField("Bis");
		Double exchangeRate = getUserInputForDoubleField("Euro-Kurs f�r 1 " + currencyIsoCode);
		
		//TODO: Aus den Variablen muss jetzt ein Kurs zusammengesetzt und in die eingelesenen Kurse eingef�gt werden. 
	}
	
	private String getUserInputForStringField(String fieldName) throws Exception {
		System.out.print(fieldName + ": ");
		return getUserInput();
	}
	
	private LocalDate getUserInputForDateField(String fieldName) throws Exception {
		System.out.print(fieldName + " (tt.mm.jjjj): ");
		String dateString = getUserInput();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		return LocalDate.parse(dateString, formatter);
	}
	
	private Double getUserInputForDoubleField(String fieldName) throws Exception {
		String doubleString = getUserInputForStringField(fieldName);
		return Double.valueOf(doubleString);
	}

	private boolean writeIataExchangeRatesInDataStructure(File file, List<String> dataStructure)
	{
		String exchangeRatesFile = file.getAbsolutePath();
		BufferedReader bufferedReader = null;
		String line = "";
		String csvSplitBy = ";";
		Set<String> tempDataEntries = new LinkedHashSet<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

		try
		{
			bufferedReader = new BufferedReader(new FileReader(exchangeRatesFile));
			while ((line = bufferedReader.readLine()) != null)
			{
				try
				{
					String[] exchangeRatesData = line.split(csvSplitBy);

					LocalDate.parse(exchangeRatesData[4], formatter);
					LocalDate.parse(exchangeRatesData[3], formatter);

					String exchangeRatesDataWithNewFormat = "";
					exchangeRatesData[1] = exchangeRatesData[1].replace(',', '.');

					if(exchangeRatesData.length == 6)
					{
						continue;
					}
					else
					{
						for(int i = 1; i < exchangeRatesData.length; i++)
						{
							exchangeRatesDataWithNewFormat += exchangeRatesData[i] + ",";
						}
					}

					tempDataEntries.add(exchangeRatesDataWithNewFormat);
				}
				catch(Exception e)
				{
					continue;
				}
			}

			if (bufferedReader != null)
			{
				bufferedReader.close();
			}

			for(String tempDataEntry: tempDataEntries)
			{
				dataStructure.add(tempDataEntry);
			}

			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private void printDataEntries(List<String> dataEntries)
	{
		for(String dataEntry: dataEntries)
		{
			String[] data = dataEntry.split(",");
			System.out.println(data[0] + " " + data[1] + " " + data[2] + " " + data[3]);
		}
	}

	private boolean checkIfInputForDisplayingIataExchangeRatesIsValid(String isoCodeInput, LocalDate dateInput, String[] data)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDate = LocalDate.parse(data[2], formatter);
			LocalDate endDate = LocalDate.parse(data[3], formatter);
			return checkIfIsoCodeIsValid(isoCodeInput, data[1]) &&
					(dateInput.isEqual(startDate) ||
							dateInput.isEqual(endDate) ||
							dateInput.isAfter(startDate) &&
									dateInput.isBefore(endDate));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private boolean checkIfIsoCodeIsValid(String isoCodeInput, String data)
	{
		return isoCodeInput.equals(data);
	}

	private boolean checkIfStartDateIsBeforeEndDate(LocalDate startDateInput, LocalDate endDateInput)
	{
		return startDateInput.isBefore(endDateInput);
	}
}
