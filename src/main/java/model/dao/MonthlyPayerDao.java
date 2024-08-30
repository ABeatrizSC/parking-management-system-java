package model.dao;

import model.entities.MonthlyPayer;
import model.entities.Vehicle;

import java.sql.ResultSet;

public interface MonthlyPayerDao {
    void insert(MonthlyPayer monthlyPayer);
    MonthlyPayer findByLicensePlate(String licensePlate);
}
