package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// JSON-P imports
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import dao.StaffDao;
import dao.StaffDaoImpl;
import javax.json.JsonObject;
import model.Staff;

public class StaffServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private StaffDao staffDao;

    @Override
    public void init() throws ServletException {
        staffDao = new StaffDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null) {
                getAllStaff(request, response);
            } else if (action.equals("getStaff")) {
                getStaff(request, response);
            } else if (action.equals("search")) {
                searchStaff(request, response);
            } else if (action.equals("checkEmail")) {
                checkEmailExists(request, response);
            } else if (action.equals("checkNric")) {
                checkNricExists(request, response);
            } else if (action.equals("generateId")) {
                generateNextId(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
            }
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is required");
            return;
        }

        try {
            if (action.equals("add")) {
                addStaff(request, response);
            } else if (action.equals("update")) {
                updateStaff(request, response);
            } else if (action.equals("delete")) {
                deleteStaff(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
            }
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    private void getAllStaff(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Staff> staffList = staffDao.getAllStaff();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Staff staff : staffList) {
            arrayBuilder.add(createStaffJson(staff));
        }
        
        try (JsonWriter writer = Json.createWriter(response.getWriter())) {
            writer.writeArray(arrayBuilder.build());
        }
    }

    private void getStaff(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String staffId = request.getParameter("staffId");
        
        if (staffId == null || staffId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Staff ID is required");
            return;
        }
        
        Staff staff = staffDao.getStaffById(staffId);

        if (staff != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            try (JsonWriter writer = Json.createWriter(response.getWriter())) {
                writer.writeObject((JsonObject) createStaffJson(staff));
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Staff not found");
        }
    }

    private void searchStaff(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String keyword = request.getParameter("keyword");
        
        if (keyword == null) {
            keyword = "";
        }
        
        List<Staff> staffList = staffDao.searchStaff(keyword);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Staff staff : staffList) {
            arrayBuilder.add(createStaffJson(staff));
        }
        
        try (JsonWriter writer = Json.createWriter(response.getWriter())) {
            writer.writeArray(arrayBuilder.build());
        }
    }

    private void addStaff(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String staffId = request.getParameter("staffId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        
        if (staffId == null || name == null || email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Required parameters are missing");
            return;
        }
        
        Staff staff = new Staff();
        staff.setStaffId(staffId);
        staff.setName(name);
        staff.setNric(request.getParameter("nric"));
        staff.setEmail(email);
        staff.setPhone(request.getParameter("phone"));
        staff.setPassword(request.getParameter("password"));

        boolean success = staffDao.addStaff(staff);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\": " + success + "}");
    }

    private void updateStaff(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String staffId = request.getParameter("staffId");
        
        if (staffId == null || staffId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Staff ID is required");
            return;
        }
        
        Staff staff = new Staff();
        staff.setStaffId(staffId);
        staff.setName(request.getParameter("name"));
        staff.setNric(request.getParameter("nric"));
        staff.setEmail(request.getParameter("email"));
        staff.setPhone(request.getParameter("phone"));
        staff.setPassword(request.getParameter("password"));

        boolean success = staffDao.updateStaff(staff);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\": " + success + "}");
    }

    private void deleteStaff(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String staffId = request.getParameter("staffId");
        
        if (staffId == null || staffId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Staff ID is required");
            return;
        }
        
        boolean success = staffDao.deleteStaff(staffId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\": " + success + "}");
    }

    private void checkEmailExists(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String email = request.getParameter("email");
        
        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email parameter is required");
            return;
        }
        
        boolean exists = staffDao.checkEmailExists(email);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"exists\": " + exists + "}");
    }

    private void checkNricExists(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String nric = request.getParameter("nric");
        
        if (nric == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "NRIC parameter is required");
            return;
        }
        
        boolean exists = staffDao.checkNricExists(nric);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"exists\": " + exists + "}");
    }

    private void generateNextId(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String nextId = staffDao.generateNextStaffId();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"nextId\": \"" + nextId + "\"}");
    }
    
    private JsonObjectBuilder createStaffJson(Staff staff) {
        JsonObjectBuilder builder = Json.createObjectBuilder()
            .add("staffId", staff.getStaffId() != null ? staff.getStaffId() : "")
            .add("name", staff.getName() != null ? staff.getName() : "")
            .add("email", staff.getEmail() != null ? staff.getEmail() : "");
        
        // Add optional fields if they exist
        if (staff.getNric() != null) {
            builder.add("nric", staff.getNric());
        }
        if (staff.getPhone() != null) {
            builder.add("phone", staff.getPhone());
        }
        // Note: Do not include password in JSON response for security
        
        return builder;
    }
    
    private void handleError(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        response.getWriter().write(
            "{\"error\": \"" + e.getClass().getSimpleName() + 
            "\", \"message\": \"" + sanitizeJson(e.getMessage()) + "\"}"
        );
        
        e.printStackTrace();
    }
    
    private String sanitizeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}