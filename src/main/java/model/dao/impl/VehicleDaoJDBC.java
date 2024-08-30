package model.dao.impl;

import db.DB;
import db.DbException;
import enums.AccessType;
import enums.VehicleCategory;
import model.dao.VehicleDao;
import model.entities.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleDaoJDBC implements VehicleDao {

    private Connection conn;

    public VehicleDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Vehicle vehicle) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO vehicles "
                            + "(category, slotSize, accessType, entranceGatesAvailable, exitGatesAvailable) "
                            + "VALUES "
                            + "(?, ?, ?, ?, ?)",
                    st.RETURN_GENERATED_KEYS);

            st.setString(1, vehicle.getCategory().toString());
            st.setInt(2, vehicle.getSlotSize());
            st.setString(3, vehicle.getAccessType().toString());
            st.setString(4, vehicle.getCategory().getEntranceGates());
            st.setString(5, vehicle.getCategory().getExitGates());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    vehicle.setId(id);
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
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM vehicles WHERE Id = ?");

            st.setInt(1, id);

            st.executeUpdate();
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Vehicle findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM vehicles WHERE Id = ?");

            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()) {
                Vehicle vehicle = instantiateVehicle(rs);
                return vehicle;
            }
            System.out.println("No vehicle was found with this ID");
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    public Vehicle instantiateVehicle(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getInt("id"));
        vehicle.setCategory(VehicleCategory.valueOf(rs.getString("category")));
        vehicle.setAccessType(AccessType.valueOf(rs.getString("accesstype")));

        return vehicle;
    }
}