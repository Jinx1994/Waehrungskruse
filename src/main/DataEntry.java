package main;

import java.time.LocalDate;

public class DataEntry {

    private String isoCode;
    private double exchangeValue;
    private LocalDate startDate;
    private LocalDate endDate;

    public DataEntry(String isoCode, LocalDate startDate, LocalDate endDate, double exchangeValue)
    {
        this.isoCode = isoCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.exchangeValue = exchangeValue;
    }

    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        this.endDate = endDate;
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
}
