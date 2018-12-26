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
		System.out.println("[3] Liste ausgeben");
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
			enterIataExchangeRate(dataStructure);
		} else if(userInput.equals("3")) {
			sortDataStructureAfterIso(dataStructure);
			printDataEntries(dataStructure);
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
				double euroValue = 1/Double.parseDouble(data[3]);
				if(checkIfInputForDisplayingIataExchangeRatesIsValid(date, currencyIsoCode, data))
				{
					System.out.println("1 " + data[2] + " entspricht " + euroValue + " Euro");
					return true;
				}
			}

			System.out.println("Eingaben ungültig. Bitte versuchen Sie es erneut.");
			displayIataExchangeRate(dataStructure);
			return false;
			//TODO: Mit currencyIsoCode und date sollte hier der Kurs ermittelt und ausgegeben werden.
		}
		catch(Exception e)
		{
			System.out.println("Eingaben ungültig. Bitte versuchen Sie es erneut.");
			displayIataExchangeRate(dataStructure);
			return false;
		}
	}
	
	private boolean enterIataExchangeRate(List<String> dataStructure) throws Exception {
		try
		{
			String currencyIsoCode = getUserInputForStringField("W�hrung");
			LocalDate from = getUserInputForDateField("Von");
			LocalDate to = getUserInputForDateField("Bis");
			Double exchangeRate = getUserInputForDoubleField("Euro-Kurs f�r 1 " + currencyIsoCode);

			boolean insertWasSuccessful = insertDataEntryToList(from, to, currencyIsoCode, exchangeRate, dataStructure);

			if(insertWasSuccessful)
			{
				return true;
			}
			else
			{
				System.out.println("Eingaben üngültig. Bitte versuche Sie es erneut.");
				enterIataExchangeRate(dataStructure);
				return false;
			}
			//TODO: Aus den Variablen muss jetzt ein Kurs zusammengesetzt und in die eingelesenen Kurse eingef�gt werden.
		}
		catch(Exception e)
		{
			System.out.println("Eingaben üngültig. Bitte versuche Sie es erneut.");
			enterIataExchangeRate(dataStructure);
			return false;
		}
	}

	public String createDataEntry(LocalDate startDateInput, LocalDate endDateInput, String isoCodeInput, Double exchangeRateInput, DateTimeFormatter formatter)
	{
		StringBuilder dataEntry = new StringBuilder();
		dataEntry.append(startDateInput.format(formatter)).append(",");
		dataEntry.append(endDateInput.format(formatter)).append(",");
		dataEntry.append(isoCodeInput).append(",");
		dataEntry.append(exchangeRateInput).append(",");
		return dataEntry.toString();
	}

	private boolean insertDataEntryToList(LocalDate startDateInput, LocalDate endDateInput, String isoCodeInput,
									Double exchangeRateInput, List<String> dataStructure)
	{
		try
		{
			ArrayList<String> dataEntriesToRemove = new ArrayList<>();
			ArrayList<String> dataEntriesToAdd = new ArrayList<>();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

			for(String dataEntry: dataStructure)
			{
				String data[] = dataEntry.split(",");
				if(data[2].equals(isoCodeInput))
				{
					dataEntriesToAdd.add(dataEntry);
					dataEntriesToRemove.add(dataEntry);
				}
			}

			if(!checkIfStartDateIsBeforeOrEqualEndDate(startDateInput, endDateInput)
			|| dataEntriesToAdd.size() == 0)
			{
				return false;
			}

			printDataEntries((dataEntriesToRemove));

			String newDataEntry = createDataEntry(startDateInput, endDateInput, isoCodeInput, exchangeRateInput, formatter);

			for(String dataEntry: dataEntriesToRemove)
			{
				String[] data = dataEntry.split(",");

				if(!checkIfDatesIntersectWithEachOther(startDateInput, endDateInput, data))
				{
					if(!dataEntriesToAdd.contains(newDataEntry))
					{
						dataEntriesToAdd.add(newDataEntry);
					}
					continue;
				}

				if(checkIfStartDateAndEndDateAreEqual(startDateInput, endDateInput, isoCodeInput, data))
				{
					dataEntriesToAdd.remove(dataEntry);
					if(!dataEntriesToAdd.contains(newDataEntry))
					{
						dataEntriesToAdd.add(newDataEntry);
					}
				}
				else if(checkIfStartDateIsEqualAndEndDateIsAfter(startDateInput, endDateInput, isoCodeInput, data))
				{
					dataEntriesToAdd.remove(dataEntry);
					if(!dataEntriesToAdd.contains(newDataEntry))
					{
						dataEntriesToAdd.add(newDataEntry);
					}
				}
				else if(checkIfStartDateIsEqualAndEndDateIsBefore(startDateInput, endDateInput, isoCodeInput, data))
				{
					if(!dataEntriesToAdd.contains(newDataEntry))
					{
						dataEntriesToAdd.add(newDataEntry);
					}

					LocalDate newStartDate = endDateInput.plusDays(1);
					String modifiedDataEntry = createDataEntry(newStartDate, LocalDate.parse(data[1], formatter), data[2], Double.parseDouble(data[3]), formatter);
					if(!dataEntriesToAdd.contains(modifiedDataEntry))
					{
						dataEntriesToAdd.add(modifiedDataEntry);
					}
					dataEntriesToAdd.remove(dataEntry);
				}
				else if(checkIfStartDateIsAfterAndEndDateIsEqual(startDateInput, endDateInput, isoCodeInput, data))
				{
					if(!dataEntriesToAdd.contains(newDataEntry))
					{
						dataEntriesToAdd.add(newDataEntry);
					}

					LocalDate newEndDate = startDateInput.minusDays(1);
					String modifiedDataEntry = createDataEntry(LocalDate.parse(data[0], formatter), newEndDate, data[2], Double.parseDouble(data[3]), formatter);
					if(!dataEntriesToAdd.contains(modifiedDataEntry))
					{
						dataEntriesToAdd.add(modifiedDataEntry);
					}
					dataEntriesToAdd.remove(dataEntry);
				}
				else if(checkIfStartDateIsBeforeAndEndDateIsEqual(startDateInput, endDateInput, isoCodeInput, data))
				{
					dataEntriesToAdd.remove(dataEntry);
					if(!dataEntriesToAdd.contains(newDataEntry))
					{
						dataEntriesToAdd.add(newDataEntry);
					}
				}
				else if(checkIfStartDateAndEndDateAreBefore(startDateInput, endDateInput, isoCodeInput, data))
				{
					if(!dataEntriesToAdd.contains(newDataEntry))
					{
						dataEntriesToAdd.add(newDataEntry);
					}

					LocalDate newStartDate = endDateInput.plusDays(1);
					String modifiedDataEntry = createDataEntry(newStartDate, LocalDate.parse(data[1], formatter), data[2], Double.parseDouble(data[3]), formatter);
					if(!dataEntriesToAdd.contains(modifiedDataEntry))
					{
						dataEntriesToAdd.add(modifiedDataEntry);
					}
					if(checkIfDatesIntersectWithEachOther(startDateInput, endDateInput, data))
					{
						dataEntriesToAdd.remove(dataEntry);
					}
				}
				else if(checkIfStartDateAndEndDateAreAfter(startDateInput, endDateInput, isoCodeInput, data))
				{
					if(!dataEntriesToAdd.contains(newDataEntry))
					{
						dataEntriesToAdd.add(newDataEntry);
					}

					LocalDate newEndDate = startDateInput.minusDays(1);
					String modifiedDataEntry = createDataEntry(LocalDate.parse(data[0], formatter), newEndDate, data[2], Double.parseDouble(data[3]), formatter);
					if(!dataEntriesToAdd.contains(modifiedDataEntry))
					{
						dataEntriesToAdd.add(modifiedDataEntry);
					}
					if(checkIfDatesIntersectWithEachOther(startDateInput, endDateInput, data))
					{
						dataEntriesToAdd.remove(dataEntry);
					}
				}
				else if(checkIfStartDateIsAfterAndEndDateIsBefore(startDateInput, endDateInput, isoCodeInput, data))
				{
					if(!dataEntriesToAdd.contains(newDataEntry))
					{
						dataEntriesToAdd.add(newDataEntry);
					}

					LocalDate newEndDate = startDateInput.minusDays(1);
					String modifiedDataEntry = createDataEntry(LocalDate.parse(data[0], formatter), newEndDate, data[2], Double.parseDouble(data[3]), formatter);
					if(!dataEntriesToAdd.contains(modifiedDataEntry))
					{
						dataEntriesToAdd.add(modifiedDataEntry);
					}
					dataEntriesToAdd.remove(dataEntry);
				}
				else if(checkIfStartDateIsBeforeAndEndDateIsAfter(startDateInput, endDateInput, isoCodeInput, data))
				{
					dataEntriesToAdd.remove(dataEntry);
					if(!dataEntriesToAdd.contains(newDataEntry))
					{
						dataEntriesToAdd.add(newDataEntry);
					}
				}
			}

			sortEntriesAfterStartDate(dataEntriesToAdd);
			dataStructure.removeAll(dataEntriesToRemove);
			dataStructure.addAll(dataEntriesToAdd);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private void sortEntriesAfterStartDate(List<String> dataEntriesToAdd)
	{
		List<DataEntry> tempList = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		for(String dataEntry: dataEntriesToAdd)
		{
			String[] data = dataEntry.split(",");
			DataEntry d = new DataEntry(LocalDate.parse(data[0], formatter), LocalDate.parse(data[1], formatter), data[2], Double.parseDouble(data[3]));
			tempList.add(d);
		}

		tempList.sort(Comparator.comparing(d -> d.getStartDate()));
		dataEntriesToAdd.clear();

		for(DataEntry d: tempList)
		{
			dataEntriesToAdd.add(d.toString());
			System.out.println(d.toString());
		}
	}

	private void sortDataStructureAfterIso(List<String> dataStructure)
	{
		List<DataEntry> tempList = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		for(String dataEntry: dataStructure)
		{
			String[] data = dataEntry.split(",");
			DataEntry d = new DataEntry(LocalDate.parse(data[0], formatter), LocalDate.parse(data[1], formatter), data[2], Double.parseDouble(data[3]));
			tempList.add(d);
		}

		tempList.sort(Comparator.comparing(d -> d.getIsoCode()));
		dataStructure.clear();

		for(DataEntry d: tempList)
		{
			dataStructure.add(d.toString());
		}
	}

	public boolean checkIfStartDateAndEndDateAreEqual(LocalDate startDateInput, LocalDate endDateInput, String isoCodeInput, String[] data)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDate = LocalDate.parse(data[0], formatter);
			LocalDate endDate = LocalDate.parse(data[1], formatter);
			return checkIfIsoCodeIsValid(isoCodeInput, data[2]) &&
					checkIfStartDateIsBeforeOrEqualEndDate(startDateInput, endDateInput) &&
					startDateInput.isEqual(startDate) &&
					endDateInput.isEqual(endDate);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkIfStartDateIsEqualAndEndDateIsAfter(LocalDate startDateInput, LocalDate endDateInput, String isoCodeInput, String[] data)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDate = LocalDate.parse(data[0], formatter);
			LocalDate endDate = LocalDate.parse(data[1], formatter);
			return checkIfIsoCodeIsValid(isoCodeInput, data[2]) &&
					checkIfStartDateIsBeforeOrEqualEndDate(startDateInput, endDateInput) &&
					startDateInput.isEqual(startDate) &&
					endDateInput.isAfter(endDate);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkIfStartDateIsEqualAndEndDateIsBefore(LocalDate startDateInput, LocalDate endDateInput, String isoCodeInput, String[] data)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDate = LocalDate.parse(data[0], formatter);
			LocalDate endDate = LocalDate.parse(data[1], formatter);
			return checkIfIsoCodeIsValid(isoCodeInput, data[2]) &&
					checkIfStartDateIsBeforeOrEqualEndDate(startDateInput, endDateInput) &&
					startDateInput.isEqual(startDate) &&
					endDateInput.isBefore(endDate);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkIfStartDateIsAfterAndEndDateIsEqual(LocalDate startDateInput, LocalDate endDateInput, String isoCodeInput, String[] data)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDate = LocalDate.parse(data[0], formatter);
			LocalDate endDate = LocalDate.parse(data[1], formatter);
			return checkIfIsoCodeIsValid(isoCodeInput, data[2]) &&
					checkIfStartDateIsBeforeOrEqualEndDate(startDateInput, endDateInput) &&
					startDateInput.isAfter(startDate) &&
					endDateInput.isEqual(endDate);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkIfStartDateIsBeforeAndEndDateIsEqual(LocalDate startDateInput, LocalDate endDateInput, String isoCodeInput, String[] data)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDate = LocalDate.parse(data[0], formatter);
			LocalDate endDate = LocalDate.parse(data[1], formatter);
			return checkIfIsoCodeIsValid(isoCodeInput, data[2]) &&
					checkIfStartDateIsBeforeOrEqualEndDate(startDateInput, endDateInput) &&
					startDateInput.isBefore(startDate) &&
					endDateInput.isEqual(endDate);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkIfStartDateAndEndDateAreBefore(LocalDate startDateInput, LocalDate endDateInput, String isoCodeInput, String[] data)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDate = LocalDate.parse(data[0], formatter);
			LocalDate endDate = LocalDate.parse(data[1], formatter);
			return checkIfIsoCodeIsValid(isoCodeInput, data[2]) &&
					checkIfStartDateIsBeforeOrEqualEndDate(startDateInput, endDateInput) &&
					startDateInput.isBefore(startDate) &&
					endDateInput.isBefore(endDate);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkIfStartDateAndEndDateAreAfter(LocalDate startDateInput, LocalDate endDateInput, String isoCodeInput, String[] data)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDate = LocalDate.parse(data[0], formatter);
			LocalDate endDate = LocalDate.parse(data[1], formatter);
			return checkIfIsoCodeIsValid(isoCodeInput, data[2]) &&
					checkIfStartDateIsBeforeOrEqualEndDate(startDateInput, endDateInput) &&
					startDateInput.isAfter(startDate) &&
					endDateInput.isAfter(endDate);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkIfStartDateIsAfterAndEndDateIsBefore(LocalDate startDateInput, LocalDate endDateInput, String isoCodeInput, String[] data)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDate = LocalDate.parse(data[0], formatter);
			LocalDate endDate = LocalDate.parse(data[1], formatter);
			return checkIfIsoCodeIsValid(isoCodeInput, data[2]) &&
					checkIfStartDateIsBeforeOrEqualEndDate(startDateInput, endDateInput) &&
					startDateInput.isAfter(startDate) &&
					endDateInput.isBefore(endDate);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkIfStartDateIsBeforeAndEndDateIsAfter(LocalDate startDateInput, LocalDate endDateInput, String isoCodeInput, String[] data)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDate = LocalDate.parse(data[0], formatter);
			LocalDate endDate = LocalDate.parse(data[1], formatter);
			return checkIfIsoCodeIsValid(isoCodeInput, data[2]) &&
					checkIfStartDateIsBeforeOrEqualEndDate(startDateInput, endDateInput) &&
					startDateInput.isBefore(startDate) &&
					endDateInput.isAfter(endDate);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkIfDatesIntersectWithEachOther(LocalDate startDateInput, LocalDate endDateInput, String[] data)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDate = LocalDate.parse(data[0], formatter);
			LocalDate endDate = LocalDate.parse(data[1], formatter);
			return !startDateInput.isAfter(endDate) &&
					!endDateInput.isBefore(startDate);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
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
					String[] data = line.split(csvSplitBy);
					data[1] = data[1].replace(',', '.');

					if(data.length == 6)
					{
						continue;
					}
					else
					{
						String dataEntry = createDataEntry(LocalDate.parse(data[3], formatter), LocalDate.parse(data[4], formatter), data[2], Double.parseDouble(data[1]), formatter);
						tempDataEntries.add(dataEntry);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
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
		System.out.println();
	}

	public boolean checkIfInputForDisplayingIataExchangeRatesIsValid(LocalDate dateInput, String isoCodeInput, String[] data)
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDate = LocalDate.parse(data[0], formatter);
			LocalDate endDate = LocalDate.parse(data[1], formatter);
			return checkIfIsoCodeIsValid(isoCodeInput, data[2]) &&
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

	public boolean checkIfIsoCodeIsValid(String isoCodeInput, String data)
	{
		return isoCodeInput.equals(data);
	}

	public boolean checkIfStartDateIsBeforeOrEqualEndDate(LocalDate startDateInput, LocalDate endDateInput)
	{
		return startDateInput.isBefore(endDateInput) ||
				startDateInput.isEqual((endDateInput));
	}
}
