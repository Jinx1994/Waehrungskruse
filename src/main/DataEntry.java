package main;

public class DataEntry {

    private String isoCode;
    private double exchangeValue;
    private String startDate;
    private String endDate;

    public DataEntry(String isoCode, String startDate, String endDate, double exchangeValue)
    {
        this.isoCode = isoCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.exchangeValue = exchangeValue;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public String getIsoCode()
    {
        return this.isoCode;
    }

    public String getStartDate()
    {
        return this.startDate;
    }

    public String getEndDate()
    {
        return this.endDate;
    }

    public double getExchangeValue()
    {
        return this.exchangeValue;
    }
}
