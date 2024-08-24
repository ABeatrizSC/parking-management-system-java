package org.example;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or

import db.DB;

import java.sql.Connection;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Connection conn = DB.getConnection();
        System.out.print("Rodou");
        DB.closeConnection();

    }
}