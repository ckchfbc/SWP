/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.BrandDAO;
import DAOs.CarDAO;
import DAOs.FuelDAO;
import DAOs.InventoryDAO;
import DAOs.ModelDAO;
import DB.DBConnection;
import Models.BrandModel;
import Models.CarModel;
import Models.FuelModel;
import Models.ModelsCarModel;
import Models.newCarModel;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thaii
 */
@MultipartConfig(maxFileSize = 16177215) // 16MB
public class CarController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CarController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CarController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String host = request.getRequestURI();
        // tạo xe
        if (host.equals("/CarController/Create")) {
            request.getRequestDispatcher("/views/createCar.jsp").forward(request, response);
        }
        // coi ảnh xe
        if (host.startsWith("/CarController/ViewImage")) {
            request.getRequestDispatcher("/views/viewCarImage.jsp").forward(request, response);
        }

        //update ảnh xe
        if (host.startsWith("/CarController/UpdateCarImg/")) {
            String[] s = host.split("/");
            String id = s[s.length - 1];
            request.setAttribute("imgId", id);
            request.getRequestDispatcher("/views/updateCarImg.jsp").forward(request, response);
        }

        // Edit car
        if (host.startsWith("/CarController/Edit")) {
            request.getRequestDispatcher("/views/editCar.jsp").forward(request, response);
        }

        // Change status
        if (host.startsWith("/CarController/Status/")) {
            String[] s = host.split("/");
            String id = s[s.length - 1];
            CarDAO carDAO = new CarDAO();
            response.setContentType("text/html;charset=UTF-8");
            try ( PrintWriter out = response.getWriter()) {
                boolean isUdapte = carDAO.changeStatus(Integer.parseInt(id));
                if (isUdapte) {
                    out.println("<script>");
                    out.println("window.close();");
                    out.println("</script>");
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Views car information
        if (host.startsWith("/CarController/View/")) {
            String[] s = host.split("/");
            String id = s[s.length - 1];
//            request.setAttribute("car_id_infor", id);            
            request.getRequestDispatcher("/views/carViews.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Các thứ sử dụng xuyên suốt POST
        CarDAO carDAO = new CarDAO();
        BrandDAO brandDAO = new BrandDAO();
        ModelDAO modelDAO = new ModelDAO();
        FuelDAO fuelDAO = new FuelDAO();
        // End các thứ sử dụng xuyên suốt POST

        // Xử lý lấy data để show ra list
        if (request.getParameter("fetchData") != null) {
            if (request.getParameter("fetchData").equals("true")) {
                List<CarModel> cars = new ArrayList<>();
                List<BrandModel> brands = new ArrayList<>();
                List<ModelsCarModel> models = new ArrayList<>();
                List<FuelModel> fuels = new ArrayList<>();

                try {
                    cars = carDAO.getAllCars();
                    brands = brandDAO.getAllBrands();
                    models = modelDAO.getAllModels();
                    fuels = fuelDAO.getAllFuels();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Thiết lập kiểu phản hồi là JSON
                Map<String, Object> data = new HashMap<>();
                data.put("cars", cars);
                data.put("brands", brands);
                data.put("models", models);
                data.put("fuels", fuels);

                // Chuyển đổi dữ liệu thành JSON
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(data);

                // Gửi JSON phản hồi về client
                response.getWriter().write(jsonResponse);
            }
        }

        // Lấy cho create
        if (request.getParameter("getForCreate") != null) {
            if (request.getParameter("getForCreate").equals("true")) {
                List<BrandModel> brands = new ArrayList<>();
                List<ModelsCarModel> models = new ArrayList<>();
                List<FuelModel> fuels = new ArrayList<>();

                try {
                    brands = brandDAO.getAllBrands();
                    models = modelDAO.getAllModels();
                    fuels = fuelDAO.getAllFuels();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Thiết lập kiểu phản hồi là JSON
                Map<String, Object> data = new HashMap<>();
                data.put("brands", brands);
                data.put("models", models);
                data.put("fuels", fuels);

                // Chuyển đổi dữ liệu thành JSON
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(data);

                // Gửi JSON phản hồi về client
                response.getWriter().write(jsonResponse);
            }
        }
        // Tạo xe
        if (request.getParameter("createCar") != null) {
            String car_name = request.getParameter("car_name");
            String date_start = request.getParameter("date_start");
            String color = request.getParameter("color");
            BigDecimal price = new BigDecimal(request.getParameter("price")); // Nếu giá là DECIMAL(12,2)
            String description = request.getParameter("car_description");
            int brand_id = Integer.parseInt(request.getParameter("brand_name"));
            int fuel_id = Integer.parseInt(request.getParameter("fuel_name"));
            int model_id = Integer.parseInt(request.getParameter("model_name"));
            CarModel car = new CarModel(brand_id, model_id, car_name, date_start, color, price, fuel_id, true, description);
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            boolean isCreated = carDAO.createCar(car);
            if (isCreated) {
                // Up ảnh sau khi tạo car xong
                int car_id = carDAO.findCarIdByName(car);
                String sql = "INSERT INTO car_image (car_id, picture) "
                        + "values (?, ?)";
                try ( Connection conn = DBConnection.getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement(sql);

                    for (Part part : request.getParts()) {
                        // Chỉ xử lý nếu part là tệp và có tên là "images" (tên input file)
                        if (part.getName().equals("images") && part.getSize() > 0) {
                            InputStream inputStream = part.getInputStream();
                            stmt.setInt(1, car_id);
                            stmt.setBlob(2, inputStream);
                            stmt.executeUpdate();
                        }
                    }

                    InventoryDAO inventoryDAO = new InventoryDAO();

                    boolean isAddQuantity = inventoryDAO.addInventory(car_id, quantity);
                    if (!isAddQuantity) {
                        System.out.println("Lỗi addquantity");
                    }
                } catch (SQLException ex) {
                    System.out.println("ERROR: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

            response.setContentType("text/html;charset=UTF-8");
            try ( PrintWriter out = response.getWriter()) {
                out.println("<script>");
                out.println("window.close();");
                out.println("</script>");
            }
        }

        // Lấy imageIdList cho show ra
        if (request.getParameter("getCarImg") != null) {
            if (request.getParameter("getCarImg").equals("true")) {
                List<Integer> imageIdList = new ArrayList<>();
                int carId = Integer.parseInt(request.getParameter("carImageId"));
                String sql = "SELECT car_image_id FROM car_image where car_id = ?;";

                try ( Connection conn = DBConnection.getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, carId);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        int imageId = rs.getInt("car_image_id");
                        imageIdList.add(imageId);
                    }
                } catch (SQLException ex) {
                    System.out.println("ERROR: " + ex.getMessage());
                    ex.printStackTrace();
                }

                // Chuyển đổi dữ liệu thành JSON
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(imageIdList);
                // Gửi JSON phản hồi về client
                response.getWriter().write(jsonResponse);
            }
        }

        // Update car img
        if (request.getParameter("updateCarImgBtn") != null) {
            String imgId = request.getParameter("imageId");
            Part filePart = request.getPart("image");
            boolean isUpdated = carDAO.updateCarImage(filePart, Integer.parseInt(imgId));
            if (isUpdated) {
                response.setContentType("text/html;charset=UTF-8");
                try ( PrintWriter out = response.getWriter()) {
                    out.println("<script>");
                    out.println("window.close();");
                    out.println("</script>");
                }
            }
        }

        // Nếu fetchData có giá trị "true"
        if (request.getParameter("carId") != null) {
            int carId = Integer.parseInt(request.getParameter("carId"));
            CarModel car = null;
            try {
                car = carDAO.getCarById(carId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (car != null) {
                response.setContentType("application/json");
                response.getWriter().write(new Gson().toJson(car)); // Sử dụng Gson để chuyển đổi đối tượng thành JSON
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Trả về mã lỗi 404 nếu không tìm thấy sự kiện
                response.getWriter().write("{\"error\": \"Event not found\"}"); // Trả về thông báo lỗi dạng JSON
            }

        }

        // Edit Car
        if (request.getParameter("editCar") != null) {
            int car_id = Integer.parseInt(request.getParameter("car_id"));
            String car_name = request.getParameter("car_name");
            String date_start = request.getParameter("date_start");
            String color = request.getParameter("color");
            BigDecimal price = new BigDecimal(request.getParameter("price")); // Nếu giá là DECIMAL(12,2)
            String description = request.getParameter("car_description");
            int brand_id = Integer.parseInt(request.getParameter("brand_name"));
            int fuel_id = Integer.parseInt(request.getParameter("fuel_name"));
            int model_id = Integer.parseInt(request.getParameter("model_name"));
            CarModel car = new CarModel(brand_id, model_id, car_name, date_start, color, price, fuel_id, true, description);
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            boolean isEdited = carDAO.editCar(car, car_id);
            if (isEdited) {
                response.setContentType("text/html;charset=UTF-8");
                InventoryDAO inventoryDAO = new InventoryDAO();
                try {
                    boolean isUpdateInventory = inventoryDAO.editInventory(car_id, quantity);
                } catch (SQLException ex) {
                    Logger.getLogger(CarController.class.getName()).log(Level.SEVERE, null, ex);
                }
                try ( PrintWriter out = response.getWriter()) {
                    out.println("<script>");
                    out.println("window.close();");
                    out.println("</script>");
                }
            }

        }

        if (request.getParameter("car_id_details") != null) {
            int carId = Integer.parseInt(request.getParameter("car_id_details"));
            CarModel car = null;
            List<BrandModel> brands = new ArrayList<>();
            List<ModelsCarModel> models = new ArrayList<>();
            List<FuelModel> fuels = new ArrayList<>();

            try {
                car = carDAO.getCarById(carId);
                brands = brandDAO.getAllBrands();
                models = modelDAO.getAllModels();
                fuels = fuelDAO.getAllFuels();
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Internal server error\"}");
                return;
            }

            // Tạo một Map để lưu trữ dữ liệu
            Map<String, Object> jsonResponse = new HashMap<>();

            if (car != null) {
                // Thêm thông tin xe vào Map
                Map<String, Object> carData = new HashMap<>();
                carData.put("car_id", car.getCar_id());
                carData.put("car_name", car.getCar_name());
                carData.put("price", car.getPrice());
                carData.put("description", car.getDescription());
                carData.put("color", car.getColor());
                carData.put("quantity", car.getQuantity());
                carData.put("brand_id", car.getBrand_id());
                carData.put("model_id", car.getModel_id());
                carData.put("fuel_id", car.getFuel_id());

                jsonResponse.put("car", carData);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                jsonResponse.put("error", "Car not found");
            }

            // Lưu danh sách thương hiệu vào Map
            List<Map<String, Object>> brandList = new ArrayList<>();
            for (BrandModel brand : brands) {
                Map<String, Object> brandData = new HashMap<>();
                brandData.put("brand_id", brand.getBrand_id());
                brandData.put("brand_name", brand.getBrand_name());
                brandList.add(brandData);
            }
            jsonResponse.put("brands", brandList);

            // Lưu danh sách model vào Map
            List<Map<String, Object>> modelList = new ArrayList<>();
            for (ModelsCarModel model : models) {
                Map<String, Object> modelData = new HashMap<>();
                modelData.put("model_id", model.getModel_id());
                modelData.put("model_name", model.getModel_name());
                modelList.add(modelData);
            }
            jsonResponse.put("models", modelList);

            // Lưu danh sách loại nhiên liệu vào Map
            List<Map<String, Object>> fuelList = new ArrayList<>();
            for (FuelModel fuel : fuels) {
                Map<String, Object> fuelData = new HashMap<>();
                fuelData.put("fuel_id", fuel.getFuel_id());
                fuelData.put("fuel_name", fuel.getFuel_name());
                fuelList.add(fuelData);
            }
            jsonResponse.put("fuels", fuelList);

            // Gửi phản hồi JSON về client
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(jsonResponse));
        }

        if (request.getParameter("getRelatedCar") != null) {
            int brand_id = Integer.parseInt(request.getParameter("getRelatedCar"));
            System.out.println(brand_id);
            List<newCarModel> cars = new ArrayList<>();
            try {
                cars = carDAO.getRelatedCar(brand_id);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Chuyển đổi dữ liệu thành JSON
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(cars);

            // Gửi JSON phản hồi về client
            response.getWriter().write(jsonResponse);
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
