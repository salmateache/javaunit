public class HelloServlet {
    @Test
    public void testHelloServlet_doGet_WritesHelloWorld() throws ServletException, IOException {
        // Création des mocks pour HttpServletRequest et HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mock du PrintWriter pour capturer la sortie
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // Instanciation et appel de la servlet
        HelloServlet servlet = new HelloServlet();
        servlet.doGet(request, response);

        // Vérification
        verify(response).getWriter();
        verify(writer).write("Hello, World!");
    }
}