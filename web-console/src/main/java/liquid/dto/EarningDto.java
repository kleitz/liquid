package liquid.dto;

import java.io.Serializable;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/25/13
 * Time: 11:39 PM
 */
public class EarningDto implements Serializable {
    private long salesPrice;

    private long distyPrice;

    private long grandTotal;

    private long grossMargin;

    private long salesProfit;

    private long distyProfit;

    public long getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(long salesPrice) {
        this.salesPrice = salesPrice;
    }

    public long getDistyPrice() {
        return distyPrice;
    }

    public void setDistyPrice(long distyPrice) {
        this.distyPrice = distyPrice;
    }

    public long getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(long grandTotal) {
        this.grandTotal = grandTotal;
    }

    public long getGrossMargin() {
        return grossMargin;
    }

    public void setGrossMargin(long grossMargin) {
        this.grossMargin = grossMargin;
    }

    public long getSalesProfit() {
        return salesProfit;
    }

    public void setSalesProfit(long salesProfit) {
        this.salesProfit = salesProfit;
    }

    public long getDistyProfit() {
        return distyProfit;
    }

    public void setDistyProfit(long distyProfit) {
        this.distyProfit = distyProfit;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("EarningDto{");
        sb.append("salesPrice=").append(salesPrice);
        sb.append(", distyPrice=").append(distyPrice);
        sb.append(", grandTotal=").append(grandTotal);
        sb.append(", grossMargin=").append(grossMargin);
        sb.append(", salesProfit=").append(salesProfit);
        sb.append(", distyProfit=").append(distyProfit);
        sb.append('}');
        return sb.toString();
    }
}