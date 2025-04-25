package com.example;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LigneBonLivraisonServletTest {

    LigneBonLivraisonServlet servlet;

    @BeforeEach
    public void setup() {
        servlet = new LigneBonLivraisonServlet();
    }

    @Test
    public void testQuantiteIsNotZero() throws SQLException {
        int id = servlet.insert("Produit Quantite Java2", 11);
        LigneBonLivraisonServlet.LigneBon ligne = servlet.findById(id);

        assertNotNull(ligne);
        assertTrue(ligne.getQuantite() != 0);
    }

    @Test
    public void testLibelleUnique() {
        assertThrows(SQLException.class, () -> {
            // Suppose que "Test Produit" existe déjà avec contrainte unique sur libelle
            servlet.insert("Test Produit", 8);
        });
    }
}
