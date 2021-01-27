package AktienStuff;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;

public class Equity {

    private Date date;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal adjusted_close;
    private long volume;
    private BigDecimal dividend_amount;
    private BigDecimal split_coefficient;

    public Equity(JSONObject json, Date date) {
        this.setDate(date);
        this.setOpen(new BigDecimal(json.get("1. open").toString()));
        this.setHigh(new BigDecimal(json.get("2. high").toString()));
        this.setLow(new BigDecimal(json.get("3. low").toString()));
        this.setClose(new BigDecimal(json.get("4. close").toString()));
        this.setAdjusted_close(new BigDecimal(json.get("5. adjusted close").toString()));
        this.setVolume(Long.parseLong(json.get("6. volume").toString()));
        this.setDividend_amount(new BigDecimal(json.get("7. dividend amount").toString()));
        this.setSplit_coefficient(new BigDecimal(json.get("8. split coefficient").toString()));
    }

    public Equity(Date date, BigDecimal open, BigDecimal close, BigDecimal high, BigDecimal low, BigDecimal adjusted_close, long volume, BigDecimal dividend_amount, BigDecimal split_coefficient) {
        this.setDate(date);
        this.setOpen(open);
        this.setClose(close);
        this.setAdjusted_close(adjusted_close);
        this.setHigh(high);
        this.setLow(low);
        this.setVolume(volume);
        this.setDividend_amount(dividend_amount);
        this.setSplit_coefficient(split_coefficient);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getAdjusted_close() {
        return adjusted_close;
    }

    public void setAdjusted_close(BigDecimal adjusted_close) {
        this.adjusted_close = adjusted_close;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public BigDecimal getDividend_amount() {
        return dividend_amount;
    }

    public void setDividend_amount(BigDecimal dividend_amount) {
        this.dividend_amount = dividend_amount;
    }

    public BigDecimal getSplit_coefficient() {
        return split_coefficient;
    }

    public void setSplit_coefficient(BigDecimal split_coefficient) {
        this.split_coefficient = split_coefficient;
    }




}
