package test;

import main.IataExchangeRateApplication;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IataExchangeRateApplicationTest {
    IataExchangeRateApplication app = new IataExchangeRateApplication();

    @Test
    public void checkBoolean1()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String startDateInput = "10.12.2010";
        String endDateInput = "09.01.2011";
        String isoInput = "USD";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfStartDateAndEndDateAreEqual(LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), isoInput, data),
                "Test successful!");
    }

    @Test
    public void checkBoolean2()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String startDateInput = "10.12.2010";
        String endDateInput = "10.01.2011";
        String isoInput = "USD";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfStartDateIsEqualAndEndDateIsAfter(LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), isoInput, data),
                "Test successful!");
    }

    @Test
    public void checkBoolean3()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String startDateInput = "10.12.2010";
        String endDateInput = "08.01.2011";
        String isoInput = "USD";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfStartDateIsEqualAndEndDateIsBefore(LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), isoInput, data),
                "Test successful!");
    }

    @Test
    public void checkBoolean4()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String startDateInput = "11.12.2010";
        String endDateInput = "09.01.2011";
        String isoInput = "USD";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfStartDateIsAfterAndEndDateIsEqual(LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), isoInput, data),
                "Test successful!");
    }

    @Test
    public void checkBoolean5()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String startDateInput = "09.12.2010";
        String endDateInput = "09.01.2011";
        String isoInput = "USD";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfStartDateIsBeforeAndEndDateIsEqual(LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), isoInput, data),
                "Test successful!");
    }

    @Test
    public void checkBoolean6()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String startDateInput = "09.12.2010";
        String endDateInput = "08.01.2011";
        String isoInput = "USD";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfStartDateAndEndDateAreBefore(LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), isoInput, data),
                "Test successful!");
    }

    @Test
    public void checkBoolean7()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String startDateInput = "11.12.2010";
        String endDateInput = "10.01.2011";
        String isoInput = "USD";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfStartDateAndEndDateAreAfter(LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), isoInput, data),
                "Test successful!");
    }

    @Test
    public void checkBoolean8()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String startDateInput = "11.12.2010";
        String endDateInput = "08.01.2011";
        String isoInput = "USD";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfStartDateIsAfterAndEndDateIsBefore(LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), isoInput, data),
                "Test successful!");
    }

    @Test
    public void checkBoolean9()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String startDateInput = "09.12.2010";
        String endDateInput = "10.01.2011";
        String isoInput = "USD";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfStartDateIsBeforeAndEndDateIsAfter(LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), isoInput, data),
                "Test successful!");
    }

    @Test
    public void checkBoolean10()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String startDateInput = "25.12.2010";
        String endDateInput = "26.12.2010";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfDatesIntersectWithEachOther(LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), data),
                "Test successful!");
    }

    @Test
    public void checkBoolean11()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String dateInput = "28.12.2010";
        String isoInput = "USD";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfInputForDisplayingIataExchangeRatesIsValid(LocalDate.parse(dateInput, formatter), isoInput, data),
                "Test successful!");
    }

    @Test
    public void checkBoolean12()
    {
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String isoInput = "USD";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfIsoCodeIsValid(isoInput, data[2]),
                "Test successful!");
    }

    @Test
    public void checkBoolean13()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String startDateInput = "29.12.2010";
        String endDateInput = "29.12.2010";
        assertTrue(app.checkIfStartDateIsBeforeOrEqualEndDate(LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter)),
                "Test successful!");
    }

    @Test
    public void checkWhetherCreatedDataEntryIsEqualsTestEntry()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353,";
        String startDateInput = "10.12.2010";
        String endDateInput = "09.01.2011";
        String isoCodeInput = "USD";
        String exchangeValueInput = "1.3353";
        String data[] = dataEntry.split(",");
        assertEquals(app.createDataEntry(LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), isoCodeInput,
                Double.parseDouble(exchangeValueInput), formatter),
                dataEntry);
    }
}