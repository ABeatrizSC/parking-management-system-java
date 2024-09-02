package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.TicketDao;
import model.entities.Gate;
import model.entities.Ticket;
import model.entities.Vehicle;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.stream.Collectors;

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
    public void deleteById(Integer id) {

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
    public void updateExitInformation(Vehicle vehicle, Ticket ticket) {

    }

}
