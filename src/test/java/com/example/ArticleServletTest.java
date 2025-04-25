package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleServletTest {

    private ArticleServlet servlet;

    @BeforeEach
    public void setup() {
        servlet = new ArticleServlet(); // Initialisation de la servlet pour chaque test
    }

    @Test
    public void testInsertAndRetrieveArticle() throws SQLException {
        // Insertion d'un article valide
        int id = servlet.insert("ART123", 10);
        assertTrue(id > 0, "L'ID généré doit être > 0");

        // Récupération et vérification
        ArticleServlet.Article article = servlet.findById(id);
        assertNotNull(article, "L'article doit être récupéré");
        assertEquals("ART123", article.getRef(), "La référence doit correspondre");
        assertEquals(10,      article.getQte(), "La quantité doit correspondre");
    }

    @Test
    public void testQuantiteIsNotZero() {
        // On s'attend à une exception si qte ≤ 0
        SQLException ex = assertThrows(SQLException.class, () -> {
            servlet.insert("ART456", 0);
        });
        assertTrue(ex.getMessage().contains("Quantité"), "Doit indiquer que la quantité est invalide");
    }

    @Test
    public void testRefUniqueConstraint() throws SQLException {
        // 1ère insertion réussie
        int id1 = servlet.insert("ART789", 5);
        assertTrue(id1 > 0);

        // 2ᵉ insertion avec la même ref → exception
        assertThrows(SQLException.class, () -> {
            servlet.insert("ART789", 7);
        }, "Référence dupliquée doit lever SQLException");
    }
}
