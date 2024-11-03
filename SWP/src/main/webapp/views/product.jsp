<%@page import="Models.BrandModel"%>
<%@page import="Models.FuelModel"%>
<%@page import="Models.CarModel_Model"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Car Filter</title>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <style>
            .zoom-img {
                transition: transform 0.3s ease; /* Thêm hiệu ứng chuyển đổi */
            }

            .card:hover .zoom-img {
                transform: scale(0.9); /* Zoom out ảnh khi hover */
            }
        </style>
        <script>
            $(document).ready(function () {
                // Fetch initial car data with default filter (0,0,0)
                fetchCars(0, 0, 0);

                // Event listeners for dropdowns
                $('#brand, #fuel, #model').change(function () {
                    updateCars();
                });

                function updateCars() {
                    var brandId = $('#brand').val();
                    var fuelId = $('#fuel').val();
                    var modelId = $('#model').val();
                    console.log('Updating cars with:', {brandId, fuelId, modelId}); // Log the filter values
                    fetchCars(brandId, fuelId, modelId);
                }

                function fetchCars(brandId, fuelId, modelId) {
                    console.log('Fetching cars with filters:', {brandId, fuelId, modelId}); // Log the data sent to the server
                    $.ajax({
                        type: 'POST',
                        url: '/ProductController', // Your servlet mapping
                        data: {brand_id: brandId, fuel_id: fuelId, model_id: modelId},
                        dataType: 'json',
                        success: function (cars) {
                            console.log('Received car data:', cars); // Log the response data
                            renderCarList(cars);
                        },
                        error: function () {
                            alert('Error fetching car data');
                        }
                    });
                }

                function renderCarList(cars) {
                    var carList = $('#carList');
                    carList.empty(); // Clear the existing list
                    if (cars.length > 0) {
                        $.each(cars, function (index, car) {
                            var formattedPrice = parseFloat(car.price).toLocaleString('en-US', {style: 'currency', currency: 'VND'});
                            carList.append('<div class="col-xl-3 col-md-6 mb-4"><a class="text-decoration-none text-dark" href="/CarController/View/' + car.car_id + '">' +
                                    '<div class="card pb-3">' +
                                    '<img src="/ImageController/c/' + car.first_car_image_id + '" class="card-img-top zoom-img" alt="' + car.car_name + '" style="height: 250px;">' +
                                    '<div class="card-body">' +
                                    '<h5 class="card-title">' + car.car_name + '</h5>' +
                                    '<p class="card-text">' + formattedPrice + '</p>' +
                                    '<p class="card-text"> inStock </p>' +
                                    '</div>' +
                                    '</div>' +
                                    '</a></div>');

                        });
                    } else {
                        carList.append('<li>No cars found</li>');
                    }
                }
            });
        </script>

    </head>
    <body>
        <%
            Cookie[] cookies = request.getCookies();
            boolean isAdmin = false;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("admin")) {
                        isAdmin = true;
                        break;
                    }
                }
            }
            if (isAdmin) {
                response.sendRedirect("/AdminController/Dashboard");
                return; // Ensure to return after redirect
            }
        %>
        <!-- Navigation -->
        <nav class="shadow-sm rounded navbar navbar-expand-md navbar-light bg-white position-fixed top-0 start-0 w-100 m-0 p-0" style="z-index: 1;">
            <div class="container">
                <a class="navbar-brand" href="/"><h1>DriveAura</h1></a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="/ProductController/Views">Product</a>                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/HomePageController/Event">Event</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Brand</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Zalo</a>
                        </li>                        
                        <!-- Nút logOut cho customer -->

                        <%
                            // Lấy danh sách cookies từ request
                            String userEmail = null;
                            String role = null;

                            // Duyệt qua các cookies và kiểm tra cookie "userEmail"
                            if (cookies != null) {
                                for (Cookie cookie : cookies) {
                                    if (cookie.getName().equals("userEmail")) {
                                        userEmail = cookie.getValue(); // Lấy giá trị email từ cookie
                                    }
                                    if (cookie.getName().equals("role")) {
                                        role = cookie.getValue();
                                    }
                                }
                            }

                            // Kiểm tra nếu cookie "userEmail" tồn tại
                            if ((userEmail != null) && (role.equals("customer"))) {
                        %>
                        <!-- Hiển thị nút nếu là customer -->
                        <li class="nav-item">
                            <a class="border rounded-circle btn btn-outline-dark text-center" href="/CustomerController/Profile" title="Profile">
                                <i class="fa-solid fa-user"></i>
                            </a>
                        </li>                        <input hidden value="<%= userEmail%>" id="userEmail">    
                        <%
                        } else {
                            if ((userEmail != null) && (role.equals("employee"))) {
                        %>
                        <!-- Hiển thị nút nếu là employee -->
                        <li class="nav-item">
                            <a class="border rounded-circle btn btn-outline-dark text-center" href="/EmployeeController/Profile" title="Profile">
                                <i class="fa-solid fa-user"></i>
                            </a>
                        </li>                        <input hidden id="role" value="<%= role%>">
                        <%
                        } else {
                        %>
                        <!-- Hiển thị thông báo nếu không có cookie -->
                        <li class="nav-item">                            
                            <a class="nav-link" href="${host}/HomePageController/Login">Login</a>
                        </li>
                        <%
                                }
                            }
                        %>
                        <!-- Nút tìm kiếm -->
                        <li class="nav-item">
                            <a class="nav-link" href="#" id="searchButton" data-bs-toggle="modal" data-bs-target="#searchModal"><i class="fa-solid fa-magnifying-glass"></i></a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <br><br><br><br>
        <h2>Filter Products</h2>
        <label for="brand">Brand:</label>
        <select name="brand_id" id="brand">
            <option value="0">Select a Brand</option>
            <%
                List<BrandModel> brands = (List<BrandModel>) request.getAttribute("brands");
                if (brands != null) {
                    for (BrandModel brand : brands) {
            %>
            <option value="<%= brand.getBrand_id()%>"><%= brand.getBrand_name()%></option>
            <%
                    }
                }
            %>
        </select>
        <br><br>

        <label for="fuel">Fuel Type:</label>
        <select name="fuel_id" id="fuel">
            <option value="0">Select a Fuel Type</option>
            <%
                List<FuelModel> fuels = (List<FuelModel>) request.getAttribute("fuels");
                if (fuels != null) {
                    for (FuelModel fuel : fuels) {
            %>
            <option value="<%= fuel.getFuel_id()%>"><%= fuel.getFuel_name()%></option>
            <%
                    }
                }
            %>
        </select>
        <br><br>

        <label for="model">Model:</label>
        <select name="model_id" id="model">
            <option value="0">Select a Model</option>
            <%
                List<CarModel_Model> models = (List<CarModel_Model>) request.getAttribute("models");
                if (models != null) {
                    for (CarModel_Model model : models) {
            %>
            <option value="<%= model.getModel_id()%>"><%= model.getModel_name()%></option>
            <%
                    }
                }
            %>
        </select>
        <br><br>

        <h2>Car List</h2>
        <ul class="row justify-content-center" id="carList">
            <!-- Car list will be populated here -->
        </ul>
    </body>
</html>
