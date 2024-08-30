package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.MonthlyPayerDao;
import model.dao.VehicleDao;
import model.entities.MonthlyPayer;
import model.entities.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static model.dao.DaoFactory.createVehicleDao;

public class MonthlyPayerDaoJDBC implements MonthlyPayerDao {
    private Connection conn;

    public MonthlyPayerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(MonthlyPayer monthlyPayer) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO monthlypayer "
                            + "(licensePlate, valuePerMonth, vehicle_id) "
                            + "VALUES "
                            + "(?, ?, ?)",
                    st.RETURN_GENERATED_KEYS);

            st.setString(1, monthlyPayer.getLicensePlate());
            st.setDouble(2, monthlyPayer.getValuePerMonth());
            st.setInt(3, monthlyPayer.getVehicle().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    monthlyPayer.setId(id);
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
    public MonthlyPayer findByLicensePlate(String licensePlate) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT * FROM monthlypayer WHERE licensePLate = ? ORDER BY id"
            );
            st.setString(1, licensePlate);
            rs = st.executeQuery();

            if (rs.next()) {
                MonthlyPayer monthlyPayer = instantiateMonthlyPayer(rs);
                return monthlyPayer;
            }
            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private MonthlyPayer instantiateMonthlyPayer(ResultSet rs) throws SQLException {
        MonthlyPayer monthlyPayer = new MonthlyPayer();
        monthlyPayer.setId(rs.getInt("id"));
        monthlyPayer.setLicensePlate(rs.getString("licensePlate"));
        int vehicleId = rs.getInt("vehicle_id");

        VehicleDao vehicleDao = createVehicleDao();
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        monthlyPayer.setVehicle(vehicle);

        return monthlyPayer;
    }
}
