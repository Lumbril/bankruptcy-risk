package com.example.application.entity;

import java.time.LocalDate;

public class FinancialIndicators {
    private String Enterprise;
    private Double netProfit;
    private Double amortization;
    private Double borrowedFunds;
    private Double currentAssets;
    private Double shortTermLiabilities;
    private Double longTermDuties;
    private Double equity;
    private Double netLoss;
    private Double accountsPayable;
    private Double accountsReceivable;
    private Double mostLiquidAssets;
    private Double volumeOfSales;
    private Double ownSourcesFinancing;
    private Double balanceCurrency;
    private Double revenue;
    private LocalDate date;

    public String getEnterprise() {
        return Enterprise;
    }

    public void setEnterprise(String enterprise) {
        Enterprise = enterprise;
    }

    public Double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(Double netProfit) {
        this.netProfit = netProfit;
    }

    public Double getAmortization() {
        return amortization;
    }

    public void setAmortization(Double amortization) {
        this.amortization = amortization;
    }

    public Double getBorrowedFunds() {
        return borrowedFunds;
    }

    public void setBorrowedFunds(Double borrowedFunds) {
        this.borrowedFunds = borrowedFunds;
    }

    public Double getCurrentAssets() {
        return currentAssets;
    }

    public void setCurrentAssets(Double currentAssets) {
        this.currentAssets = currentAssets;
    }

    public Double getShortTermLiabilities() {
        return shortTermLiabilities;
    }

    public void setShortTermLiabilities(Double shortTermLiabilities) {
        this.shortTermLiabilities = shortTermLiabilities;
    }

    public Double getLongTermDuties() {
        return longTermDuties;
    }

    public void setLongTermDuties(Double longTermDuties) {
        this.longTermDuties = longTermDuties;
    }

    public Double getEquity() {
        return equity;
    }

    public void setEquity(Double equity) {
        this.equity = equity;
    }

    public Double getNetLoss() {
        return netLoss;
    }

    public void setNetLoss(Double netLoss) {
        this.netLoss = netLoss;
    }

    public Double getAccountsPayable() {
        return accountsPayable;
    }

    public void setAccountsPayable(Double accountsPayable) {
        this.accountsPayable = accountsPayable;
    }

    public Double getAccountsReceivable() {
        return accountsReceivable;
    }

    public void setAccountsReceivable(Double accountsReceivable) {
        this.accountsReceivable = accountsReceivable;
    }

    public Double getMostLiquidAssets() {
        return mostLiquidAssets;
    }

    public void setMostLiquidAssets(Double mostLiquidAssets) {
        this.mostLiquidAssets = mostLiquidAssets;
    }

    public Double getVolumeOfSales() {
        return volumeOfSales;
    }

    public void setVolumeOfSales(Double volumeOfSales) {
        this.volumeOfSales = volumeOfSales;
    }

    public Double getOwnSourcesFinancing() {
        return ownSourcesFinancing;
    }

    public void setOwnSourcesFinancing(Double ownSourcesFinancing) {
        this.ownSourcesFinancing = ownSourcesFinancing;
    }

    public Double getBalanceCurrency() {
        return balanceCurrency;
    }

    public void setBalanceCurrency(Double balanceCurrency) {
        this.balanceCurrency = balanceCurrency;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
