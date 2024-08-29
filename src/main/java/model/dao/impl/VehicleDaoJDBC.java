package model.dao.impl;

import db.DB;
import db.DbException;
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
}
