package model.dao.impl;

import db.DB;
import db.DbException;
import enums.AccessType;
import enums.VehicleCategory;
import model.dao.VehicleDao;
import model.entities.Gate;
import model.entities.Vehicle;

import java.sql.*;

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
    public void updateSelectionedGate(Vehicle vehicle, Integer selectionedGate) {
        PreparedStatement st = null;
        String gateType;
        try {
            if(Gate.GateType.ENTRANCE.getGateNumbers().contains(selectionedGate)) {
                st = conn.prepareStatement(
                        "UPDATE vehicles "
                                + "SET entranceGate = ? "
                                + "WHERE Id = ? "
                );
            } else {
                st = conn.prepareStatement(
                        "UPDATE vehicles "
                                + "SET exitGate = ? "
                                + "WHERE Id = ? "
                );
            }

            st.setInt(1, vehicle.getEntranceGate());
            st.setInt(2, vehicle.getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected < 0) {
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
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Vehicle findByLicensePlate(String licensePlate) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT vehicles.id, vehicles.category, vehicles.accessType, vehicles.slotSize, vehicles.entranceGatesAvailable, vehicles.exitGatesAvailable " +
                            "FROM vehicles " +
                            "JOIN monthlyPayer ON vehicles.id = monthlyPayer.vehicle_id " +
                            "WHERE monthlyPayer.licensePlate = ? " +
                            "UNION " +
                            "SELECT vehicles.id, vehicles.category, vehicles.accessType, vehicles.slotSize, vehicles.entranceGatesAvailable, vehicles.exitGatesAvailable " +
                            "FROM vehicles " +
                            "JOIN deliveryTruck ON vehicles.id = deliveryTruck.vehicle_id " +
                            "WHERE deliveryTruck.licensePlate = ?");

            st.setString(1, licensePlate);
            st.setString(2, licensePlate);

            rs = st.executeQuery();

            if (rs.next()) {
                Vehicle vehicle = instantiateVehicle(rs);
                return vehicle;
            }
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void finalizeAccess(Vehicle vehicle) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE vehicles "
                            + "SET entranceGate = ?, exitGate = ? "
                            + "WHERE Id = ? "
            );

            st.setNull(1, java.sql.Types.INTEGER);
            st.setNull(2, java.sql.Types.INTEGER);
            st.setInt(3, vehicle.getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected < 0) {
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

    public Vehicle instantiateVehicle(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getInt("id"));
        vehicle.setCategory(VehicleCategory.valueOf(rs.getString("category")));
        vehicle.setAccessType(AccessType.valueOf(rs.getString("accessType")));
        vehicle.setSlotSize(rs.getInt("slotSize"));

        return vehicle;
    }
}
