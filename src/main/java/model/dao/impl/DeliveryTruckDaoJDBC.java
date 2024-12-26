package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DeliveryTruckDao;
import model.dao.VehicleDao;
import model.entities.DeliveryTruck;
import model.entities.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static model.dao.DaoFactory.createVehicleDao;

public class DeliveryTruckDaoJDBC implements DeliveryTruckDao {
    private Connection conn;

    public DeliveryTruckDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(DeliveryTruck deliveryTruck) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO deliverytruck "
                            + "(licensePlate, vehicle_id) "
                            + "VALUES "
                            + "(?, ?)",
                    st.RETURN_GENERATED_KEYS);

            st.setString(1, deliveryTruck.getLicensePlate());
            st.setInt(2, deliveryTruck.getVehicle().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    deliveryTruck.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public DeliveryTruck findDeliveryTruckByLicensePlate(String licensePlate) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT * FROM deliverytruck WHERE licensePLate = ? ORDER BY id"
            );
            st.setString(1, licensePlate);
            rs = st.executeQuery();

            if (rs.next()) {
                DeliveryTruck deliveryTruck = instantiateDeliveryTruck(rs);
                return deliveryTruck;
            }
            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private DeliveryTruck instantiateDeliveryTruck(ResultSet rs) throws SQLException {
        DeliveryTruck deliveryTruck = new DeliveryTruck();
        deliveryTruck.setId(rs.getInt("id"));
        deliveryTruck.setLicensePlate(rs.getString("licensePlate"));
        int vehicleId = rs.getInt("vehicle_id");

        VehicleDao vehicleDao = createVehicleDao();
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        deliveryTruck.setVehicle(vehicle);

        return deliveryTruck;
    }
}
