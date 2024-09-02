package model.dao;

import db.DB;
import model.dao.impl.*;

public interface DaoFactory {
    public static VehicleDaoJDBC createVehicleDao(){
        return new VehicleDaoJDBC(DB.getConnection());
    }

    public static MonthlyPayerDaoJDBC createMonthlyPayerDao(){
        return new MonthlyPayerDaoJDBC(DB.getConnection());
    }

    public static DeliveryTruckDaoJDBC createDeliveryTruckDao(){
        return new DeliveryTruckDaoJDBC(DB.getConnection());
    }

    public static TicketDaoJDBC createTicketDao(){
        return new TicketDaoJDBC(DB.getConnection());
    }

    public static ParkingSpaceDao createParkingSpaceDao(){
        return new ParkingSpaceDaoJDBC(DB.getConnection());
    }
}
