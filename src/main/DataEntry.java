package main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataEntry {

    private LocalDate startDate;
    private LocalDate endDate;
    private String isoCode;
    private double exchangeValue;

    public DataEntry(LocalDate startDate, LocalDate endDate, String isoCode, double exchangeValue)
    {
        this.isoCode = isoCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.exchangeValue = exchangeValue;
    }

    public String getIsoCode()
    {
        return this.isoCode;
    }

    public LocalDate getStartDate()
    {
        return this.startDate;
    }

    public LocalDate getEndDate()
    {
        return this.endDate;
    }

    public double getExchangeValue()
    {
        return this.exchangeValue;
    }

    public String toString()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        StringBuilder s = new StringBuilder();
        s.append(this.startDate.format((formatter))).append(",");
        s.append(this.endDate.format((formatter))).append(",");
        s.append(this.isoCode).append(",");
        s.append(this.exchangeValue).append(",");

        return s.toString();
    }
}
