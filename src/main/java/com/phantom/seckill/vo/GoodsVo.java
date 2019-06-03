package com.phantom.seckill.vo;

import com.phantom.seckill.entity.Goods;


import java.math.BigDecimal;
import java.util.Date;


public class GoodsVo extends Goods {
    private BigDecimal miusaPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

    public BigDecimal getMiusaPrice() {
        return miusaPrice;
    }

    public void setMiusaPrice(BigDecimal miusaPrice) {
        this.miusaPrice = miusaPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return "GoodsVo{" +
                "miusaPrice=" + miusaPrice +
                ", stockCount=" + stockCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
