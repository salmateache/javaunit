package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/livraison")
public class LigneBonLivraisonServlet extends HttpServlet {

    private final String URL = "jdbc:mysql://localhost:3306/ci4_test";
    private final String USER = "root";
    private final String PASSWORD = "";

    public int insert(String libelle, int quantite) throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false); // Désactive autocommit pour mieux contrôler
            String sql = "INSERT INTO ligneBonCommande (libelle, quantite) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, libelle);
            stmt.setInt(2, quantite);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                conn.commit(); // Commit final
                return generatedId;
            } else {
                conn.rollback(); // rollback si pas d'ID
                throw new SQLException("No ID generated.");
            }
        } catch (SQLException e) {
            if (conn != null) conn.rollback(); // En cas d'erreur, rollback
            throw e;
        } finally {
            if (conn != null) conn.close(); // Toujours fermer la connexion
        }
    }

    public LigneBon findById(int id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT libelle, quantite FROM ligneBonCommande WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new LigneBon(rs.getString("libelle"), rs.getInt("quantite"));
            }
            return null;
        }
    }

    // Classe interne pour représenter une ligne
    static class LigneBon {
        String libelle;
        int quantite;

        LigneBon(String libelle, int quantite) {
            this.libelle = libelle;
            this.quantite = quantite;
        }

        public int getQuantite() {
            return quantite;
        }

        public String getLibelle() {
            return libelle;
        }
    }
}
