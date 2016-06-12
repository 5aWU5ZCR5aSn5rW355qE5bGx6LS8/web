<%--
  Created by IntelliJ IDEA.
  User: manag
  Date: 6/12/2016
  Time: 9:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="manageryzy.simulator.Simulator" %>
<%@ page import="manageryzy.simulator.SimulatorServer" %>
<%
    if (Simulator.getmSimulator() == null) {
        Simulator.setmSimulator(new SimulatorServer(request.getSession().getServletContext().getRealPath("/") + "car.txt"));
    }

    for (int i = 1; i < 100; i++) {
        Simulator.getmSimulator().update();
    }
%>
<html>
  <head>
    <title></title>
  </head>
  <body>

  </body>
</html>
