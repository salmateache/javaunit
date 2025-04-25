package com.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import java.sql.*;

@WebServlet("/article")
public class ArticleServlet extends HttpServlet {

    private final String URL = "jdbc:mysql://localhost:3306/ci4_test";
    private final String USER = "root";
    private final String PASSWORD = "";

    // Méthode d'insertion d'un article
    public int insert(String ref, int qte) throws SQLException {
        // 1) Vérifier la quantité
        if (qte <= 0) {
            throw new SQLException("Quantité ne peut pas être zéro ou négative");
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // 2) Vérifier l'unicité de la référence
            String checkSql = "SELECT COUNT(*) FROM article WHERE ref = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, ref);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Référence de l'article doit être unique");
                    }
                }
            }

            // 3) Insertion
            conn.setAutoCommit(false);
            String sql = "INSERT INTO article (ref, qte) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, ref);
                stmt.setInt(2, qte);
                int affected = stmt.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    throw new SQLException("Échec de l'insertion de l'article");
                }

                // 4) Récupérer l'ID généré
                try (ResultSet genKeys = stmt.getGeneratedKeys()) {
                    if (genKeys.next()) {
                        int id = genKeys.getInt(1);
                        conn.commit();
                        return id;
                    } else {
                        conn.rollback();
                        throw new SQLException("Aucun ID généré");
                    }
                }
            }
        }
    }

    // Méthode pour rechercher un article par son ID
    public Article findById(int id) throws SQLException {
        String sql = "SELECT id, ref, qte FROM article WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Article(
                        rs.getInt("id"),
                        rs.getString("ref"),
                        rs.getInt("qte")
                    );
                }
                return null;
            }
        }
    }

    // Classe interne pour représenter un article
    static class Article {
        private final int id;
        private final String ref;
        private final int qte;

        Article(int id, String ref, int qte) {
            this.id = id;
            this.ref = ref;
            this.qte = qte;
        }

        public int getId() { return id; }
        public String getRef() { return ref; }
        public int getQte() { return qte; }
    }
}
