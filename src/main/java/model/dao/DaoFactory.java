package model.dao;

import db.DB;
import model.dao.impl.MonthlyPayerDaoJDBC;
import model.dao.impl.VehicleDaoJDBC;

public interface DaoFactory {
    public static VehicleDaoJDBC createVehicleDao(){
        return new VehicleDaoJDBC(DB.getConnection());
    }

    public static MonthlyPayerDaoJDBC createMonthlyPayerDao(){
        return new MonthlyPayerDaoJDBC(DB.getConnection());
    }
}
