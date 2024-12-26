package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.TicketDao;
import model.dao.VehicleDao;
import model.entities.Ticket;
import model.entities.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import static model.dao.DaoFactory.createVehicleDao;

public class TicketDaoJDBC implements TicketDao {
    private Connection conn;

    public TicketDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Ticket ticket) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO ticket "
                            + "(vehicle_id) "
                            + "VALUES "
                            + "(?)",
                    st.RETURN_GENERATED_KEYS);


            st.setInt(1, ticket.getVehicle().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    ticket.setId(id);
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
    public Ticket findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM ticket WHERE Id = ?");

            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()) {
                Ticket ticket = instantiateTicket(rs);
                return ticket;
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
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM ticket WHERE Id = ?");

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
    public void updateEntranceInformation(Vehicle vehicle, Ticket ticket, int[] intParkingSpaces) {
        PreparedStatement st = null;
        Time startHour = Time.valueOf(LocalTime.now());

        String parkingSpaces = Arrays.stream(intParkingSpaces)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));

        try {
            st = conn.prepareStatement(
                    "UPDATE ticket "
                            + "SET startHour = ?, parkingSpaces = ? "
                            + "WHERE Id = ? "
            );

            st.setTime(1, startHour);
            st.setString(2, parkingSpaces);
            st.setInt(3, ticket.getId());


            int rowsAffected = st.executeUpdate();

            if (rowsAffected < 0) {
                throw new DbException("Unexpected error! No rows affected!");
            } else {
                ticket.setStartHour(startHour.toLocalTime());
                ticket.setParkingSpaces(parkingSpaces);
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
    public void updateExitInformation(Ticket ticket) {
        PreparedStatement st = null;
        Time finishHour = Time.valueOf(LocalTime.now());

        try {
            st = conn.prepareStatement(
                    "UPDATE ticket "
                            + "SET finishHour = ?, totalValue = ? "
                            + "WHERE Id = ? "
            );

            st.setTime(1, finishHour);
            st.setDouble(2, ticket.getTotalValue());
            st.setInt(3, ticket.getId());


            int rowsAffected = st.executeUpdate();

            if (rowsAffected < 0) {
                throw new DbException("Unexpected error! No rows affected!");
            } else {
                ticket.setFinishHour(finishHour.toLocalTime());
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    public Ticket instantiateTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("id"));
        ticket.setParkingSpaces(rs.getString("parkingSpaces"));
        ticket.setStartHour(rs.getTime("startHour").toLocalTime());

        VehicleDao vehicleDao = createVehicleDao();
        int vehicleId = rs.getInt("vehicle_id");
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        ticket.setVehicle(vehicle);

        return ticket;
    }

}
