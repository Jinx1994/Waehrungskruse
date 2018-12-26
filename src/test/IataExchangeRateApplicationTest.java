package test;

import main.IataExchangeRateApplication;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IataExchangeRateApplicationTest {
    List<String> dataStructure = new ArrayList<>();
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
        assertTrue(app.checkIfStartDateAndEndDateAreEqual(isoInput, LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), data),
                "Test successful!");
    }

    public void checkBoolean2()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dataEntry = "10.12.2010,09.01.2011,USD,1.3353";
        String startDateInput = "10.12.2010";
        String endDateInput = "09.01.2011";
        String isoInput = "USD";
        String data[] = dataEntry.split(",");
        assertTrue(app.checkIfStartDateAndEndDateAreEqual(isoInput, LocalDate.parse(startDateInput, formatter), LocalDate.parse(endDateInput, formatter), data),
                "Test successful!");
    }
}