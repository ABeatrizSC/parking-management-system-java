package model.dao;

import model.entities.MonthlyPayer;

public interface MonthlyPayerDao {
    void insert(MonthlyPayer monthlyPayer);
    MonthlyPayer findMonthlyPayerByLicensePlate(String licensePlate);
}
