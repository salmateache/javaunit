public class HelloServlet {
      @WebServlet("/hello")
      public class HelloServlet extends HttpServlet {

            @Override
            protected void doGet(HttpServletRequest request,
                        HttpServletResponse response)
                        throws ServletException, IOException {
                  response.getWriter().write("Hello, World!");
            }
      }
}