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
<%@ page import="manageryzy.workerWrapper.Wrapper" %>
<%@ page import="org.json.JSONObject" %>
<%
    if (Simulator.getmSimulator() == null) {
        Simulator.setmSimulator(new SimulatorServer(request.getSession().getServletContext().getRealPath("/") + "car.txt"));
    }

    if (Wrapper.getWrapper() == null) {
        Wrapper.setWrapper(new Wrapper());
    }

    Boolean isLogin = (Boolean) session.getAttribute("isLogin");
    String method = request.getParameter("method");

    if (isLogin == null) {
        isLogin = false;
    }
    if (method == null) {
        method = "";
    }

    switch (method) {
        case "check":
            if (isLogin) {
                try {
                    out.write("{\"code\":0,\"res\":" + Wrapper.getWrapper().check() + "}");
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    out.close();
                }
            } else {
                out.write("{\"code\":-1}");
                out.close();
            }
            break;
        case "select":
            if (isLogin) {
                String car = request.getParameter("car");

                try {
                    out.write("{\"code\":0,\"res\":" + Wrapper.getWrapper().select(car) + "}");
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    out.write("{\"code\":-2}");
                    out.close();
                }
            } else {
                out.write("{\"code\":-1}");
                out.close();
            }
            break;
        case "simulate":
            if (isLogin) {
                try {
                    String command = request.getParameter("cmd");
                    if (command == null) {
                        JSONObject object = new JSONObject();
                        object.put("code", 0);
                        object.put("run", Simulator.getmSimulator().isRunning());
                        object.put("time", Simulator.getmSimulator().getT());

                        out.write(object.toString());
                        out.close();
                        return;
                    } else {
                        switch (command) {
                            case "step":
                                Simulator.getmSimulator().step();
                                break;
                            case "run":
                                Simulator.getmSimulator().run();
                                break;
                            case "pause":
                                Simulator.getmSimulator().pause();
                                break;
                        }
                    }

                    out.write("{\"code\":0}");
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    out.write("{\"code\":-2}");
                    out.close();
                }
            } else {
                out.write("{\"code\":-1}");
                out.close();
            }
            break;
        case "login":
            String name = request.getParameter("name");
            String pass = request.getParameter("pwd");

            if (name != null && pass != null && name.equals("admin") && pass.equals("password")) {
                session.setAttribute("isLogin", true);
                out.write("{\"code\":0}");
                out.close();
            } else {
                out.write("{\"code\":-1}");
                out.close();
            }

            break;
        case "logout":
            session.invalidate();
            out.write("{\"code\":0}");
            out.close();
            break;
        default:
            out.write("{\"code\":-5}");
            out.close();
    }
%>
