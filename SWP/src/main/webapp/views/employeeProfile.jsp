<%-- 
    Document   : employeeProfile
    Created on : 28 thg 10, 2024, 20:31:13
    Author     : thaii
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/a611f8fd5b.js" crossorigin="anonymous"></script>
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <title>Profile User</title>
        <style>
            @font-face {
                font-family: 'Kirsty';
                src: url('../fonts/KirstyRg-Regular.woff2') format('woff2'),
                    url('../fonts/KirstyRg-Regular.woff') format('woff');
                font-weight: normal;
                font-style: normal;
                font-display: swap;
            }

            .navbar-brand  {
                font-family: 'Kirsty', sans-serif;
                color: #050B20;
            }
            body {
                background-color: #f8f9fa;
            }
            .profile-img {
                width: 100px;
                height: 100px;
                object-fit: cover;
                border-radius: 50%;
                border: 3px solid #fff;
                box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
            }
            .card {
                border-radius: 10px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }
            .btn-custom {
                border-radius: 50px;
            }

        </style>
    </head>
    <body class="container-fluid">
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
                            <a class="nav-link" href="#">Product</a>
                        </li>
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
                            Cookie[] cookies = request.getCookies();

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
                        <a class="border rounded-circle btn btn-outline-dark text-center" href="/CustomerController/Profile"><i class="fa-solid fa-user"></i></a>
                        <input hidden value="<%= userEmail%>" id="userEmail">    
                        <input hidden id="role" value="<%= role%>">
                        <%
                        } else {
                            if ((userEmail != null) && (role.equals("employee"))) {
                        %>
                        <!-- Hiển thị nút nếu là employee -->
                        <a class="border rounded-circle btn btn-outline-dark text-center" href="/EmployeeController/Profile"><i class="fa-solid fa-user"></i></a>
                        <input hidden id="role" value="<%= role%>">
                        <input hidden value="<%= userEmail%>" id="userEmail">  
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

        <!-- Modal tìm kiếm -->
        <div class="modal fade p-0 m-0" id="searchModal" tabindex="-1" aria-labelledby="searchModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="searchModalLabel">Tìm Kiếm</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="searchForm">
                            <div class="input-group">
                                <input type="text" class="form-control" placeholder="Nhập từ khóa tìm kiếm..." aria-label="Search">
                                <button class="btn btn-outline-secondary" type="submit">Tìm</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>


        <div class="container-fluid w-100 pt-5 mt-5">
            <div class="row justify-content-center">
                <div class="col-lg-10">
                    <div class="card overflow-hidden">
                        <div class="row g-0 row-bordered row-border-light">
                            <div
                                class="col-md-3 bg-light d-flex flex-column align-items-center p-4">
                                <div id="avarta">
                                    <!-- Avarta -->
                                </div>                                
                                <div class="dropdown w-100 mt-3">
                                    <button
                                        class="btn btn-outline-primary btn-custom w-100 dropdown-toggle"
                                        type="button" id="dropdownMenuButton"
                                        data-bs-toggle="dropdown"
                                        aria-expanded="false">
                                        More Options
                                    </button>
                                    <ul class="dropdown-menu"
                                        aria-labelledby="dropdownMenuButton">
                                        <li><a class="dropdown-item"
                                               href="/EmployeeController/Order">View List
                                                Order</a></li>
                                        <li><a class="dropdown-item"
                                               href="#">Chat with
                                                Employee</a></li>
                                    </ul>
                                </div>
                            </div>
                            <div class="col-md-9">
                                <div class="tab-content p-4">
                                    <div class="tab-pane fade active show"
                                         id="account-general">                                        
                                        <hr class="border-light m-0">
                                        <div class="card-body">                                          
                                            <div class="row justify-content-evenly">
                                                <div
                                                    class="alert alert-warning mt-3">
                                                    * Address, ID number and Phone Number not necessary when you do not buy a car.<br>                                                    
                                                </div>
                                                <div class="mb-3 col-sm-5">
                                                    <label
                                                        class="form-label">Name</label>
                                                    <input type="text" id="name"
                                                           class="form-control"
                                                           placeholder="Name" required>
                                                </div>
                                                <div class="mb-3 col-sm-5">
                                                    <label
                                                        class="form-label">E-mail</label>
                                                    <input type="text" id="email"
                                                           class="form-control"
                                                           placeholder="E-mail" required>
                                                </div>
                                                <div class="mb-3 col-sm-5"> 
                                                    <label
                                                        class="form-label">Employee Id</label>
                                                    <input type="text" id="empId"
                                                           class="form-control"
                                                           placeholder="Employee Id">
                                                </div>
                                                <div class="mb-3 col-sm-5">
                                                    <label
                                                        class="form-label">Phone number</label>
                                                    <input type="text" id="phone_number"
                                                           class="form-control"
                                                           placeholder="Phone number">
                                                </div>                                            
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row justify-content-md-end mt-4 mb-4">
                        <div class="col-12 col-md-6 col-lg-4 d-flex justify-content-end">
                            <form action="/LoginController" method="POST">
                                <button type="submit" class="btn btn-secondary btn-custom" name="logOut">Log Out</button>
                            </form>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <script>
            $(document).ready(function () {
                // Lấy userEmail từ cookie
                var userEmail = document.getElementById('userEmail').value;
                // Nếu tìm thấy userEmail, gửi AJAX request để lấy thông tin người dùng
                $.ajax({
                    url: '/EmployeeController', // Đường dẫn đến API hoặc servlet xử lý theo email
                    type: 'POST', // Sử dụng phương thức GET
                    data: {getInforEmployee: userEmail}, // Gửi email dưới dạng tham số
                    dataType: 'json', // Định dạng dữ liệu trả về là JSON
                    success: function (user) {
                        // Điền các giá trị vào các input tương ứng
                        $('#name').val(user.name);
                        console.log(user);
                        $('#email').val(user.email);

                        var img = '<img src="/ImageController/a/avarta.png" alt="Profile Picture" class="profile-img mb-3">';
                        $('#avarta').append(img);

                        var phone = '********' + user.phoneNumber.slice(-2);
                        $('#phone_number').val(phone);


                        $('#empId').val(user.employeeId);
                    },
                    error: function (xhr, status, error) {
                        console.error("Lỗi khi lấy thông tin người dùng:", error);
                    }
                });
            });

        </script>
    </body>
</html>
