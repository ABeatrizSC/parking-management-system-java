package model.dao.impl;

import db.DB;
import db.DbException;
import enums.AccessType;
import enums.SlotType;
import enums.VehicleCategory;
import model.dao.ParkingSpaceDao;
import model.dao.VehicleDao;
import model.entities.ParkingSpace;
import model.entities.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static model.dao.DaoFactory.createVehicleDao;

public class ParkingSpaceDaoJDBC implements ParkingSpaceDao {
    private Connection conn;

    public ParkingSpaceDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(ParkingSpace parkingSpace) {
        PreparedStatement st = null;

        Boolean isOccupied = parkingSpace.getIsOccupied();
        byte occupiedValue = (byte) (isOccupied ? 1 : 0);

        try {
            st = conn.prepareStatement(
                    "INSERT INTO parkingSpace "
                            + "(isOccupied, slotType) "
                            + "VALUES "
                            + "(?, ?)",
                    st.RETURN_GENERATED_KEYS);

            st.setByte(1, occupiedValue);
            st.setString(2, parkingSpace.getType().toString());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    parkingSpace.setId(id);
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
    public void update(ParkingSpace parkingSpace) {
        PreparedStatement st = null;

        Boolean isOccupied = parkingSpace.getIsOccupied();
        byte occupiedValue = (byte) (isOccupied ? 1 : 0);

        try {
            st = conn.prepareStatement(
                    "UPDATE parkingspace "
                            + "SET isOccupied = ?, slotType = ?, vehicle_id = ? "
                            + "WHERE Id = ?");

            st.setByte(1, occupiedValue);
            st.setString(2, parkingSpace.getType().toString());
            try {
                st.setInt(3, parkingSpace.getVehicle().getId());
            } catch (Exception e) {
                st.setNull(3, java.sql.Types.INTEGER);
            }

            st.setInt(4, parkingSpace.getId());

            int rowsAffected = st.executeUpdate();
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public ParkingSpace findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM parkingspace WHERE Id = ?");

            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()) {
                ParkingSpace parkingSpace = instantiateParkingSpace(rs);
                return parkingSpace;
            }
            System.out.println("No Parking Space was found with this ID");
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
    public int[] findByVehicle(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT id FROM parkingSpace WHERE vehicle_id = ?"
            );
            st.setInt(1, id);

            rs = st.executeQuery();

            List<Integer> parkingSpaces = new ArrayList<>();
            while (rs.next()) {
                parkingSpaces.add(rs.getInt("id"));
            }

            return parkingSpaces.stream().mapToInt(i -> i).toArray();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public ParkingSpace instantiateParkingSpace(ResultSet rs) throws SQLException {
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setId(rs.getInt("id"));
        parkingSpace.setIsOccupied(rs.getBoolean("isOccupied"));
        parkingSpace.setType(SlotType.valueOf(rs.getString("slotType")));
        int vehicleId = rs.getInt("vehicle_id");
        if (rs.wasNull()) {
            parkingSpace.setVehicle(null);
        } else {
            VehicleDao vehicleDao = createVehicleDao();
            Vehicle vehicle = vehicleDao.findById(vehicleId);
            parkingSpace.setVehicle(vehicle);
        }
        return parkingSpace;
    }
}
