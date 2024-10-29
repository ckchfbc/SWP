<%-- 
    Document   : listEvent
    Created on : 23 thg 10, 2024, 11:56:46
    Author     : thaii
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List Event</title>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/a611f8fd5b.js" crossorigin="anonymous"></script>
        <style>
            .card-img-top {
                height: 300px; /* Đặt chiều cao cố định */
                object-fit: cover; /* Cắt hình ảnh để vừa với kích thước mà không làm méo hình */
            }
            @font-face {
                font-family: 'Kirsty'; /* Your font name */
                src: url('../fonts/kirsty rg.otf') format('opentype'); /* Path to your font */
                font-weight: normal;
                font-style: normal;
            }
            .navbar-brand  {
                font-family: 'Kirsty', sans-serif;
                color: #050B20;
            }
        </style>
    </head>
    <body>
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

        <div class="container mt-5 pt-5">
            <h1 class="text-center">Event List</h1>
            <div class="row" id="eventCards">
                <!-- Thẻ sự kiện sẽ được thêm ở đây -->
            </div>

            <!-- Nút "More" để hiển thị thêm sự kiện -->
            <div class="text-center mt-3">
                <button id="loadMoreButton" class="btn btn-primary">More</button>
                <button id="showLessButton" class="btn btn-secondary" style="display: none;">Show Less</button>
            </div>

        </div>       

        <script>
            let eventsDisplayed = 0; // Số sự kiện đã hiển thị
            const eventsPerPage = 2; // Số sự kiện muốn hiển thị mỗi lần
            let allEvents = []; // Biến để lưu trữ toàn bộ dữ liệu sự kiện từ server

// Hàm để gọi Ajax lấy dữ liệu sự kiện từ server
            function loadEvents() {
                $.ajax({
                    url: '/EventController', // Endpoint servlet lấy dữ liệu sự kiện
                    method: 'POST',
                    data: {getEventList: "true"},
                    dataType: "json",
                    success: function (data) {
                        allEvents = data; // Gán dữ liệu sự kiện vào biến allEvents
                        displayEventCards(); // Hiển thị thẻ sự kiện đầu tiên
                    },
                    error: function (xhr, status, error) {
                        console.log("Lỗi khi lấy dữ liệu sự kiện:", error);
                    }
                });
            }

// Hàm để hiển thị sự kiện thành thẻ (cards)
            function displayEventCards() {
                const eventCardsContainer = document.getElementById('eventCards');
                // Hiển thị 2 sự kiện mỗi lần
                for (let i = eventsDisplayed; i < eventsDisplayed + eventsPerPage && i < allEvents.length; i++) {
                    const event = allEvents[i];
                    const eventCard =
                            '<div class="col-6 mb-4">' + // Mỗi thẻ chiếm 6 cột (tức là 2 thẻ trên một hàng)
                            '<div class="card">' +
                            '<img src="/ImageController/b/' + event.event_id + '" class="card-img-top" alt="' + event.event_name + '">' +
                            '<div class="card-body">' +
                            '<h5 class="card-title">' + event.event_name + '</h5>' +
                            '<div class="d-flex justify-content-between">' + // Sử dụng flexbox để bố trí các phần tử
                            '<p class="card-text"><small class="text-muted">Start: ' + formatDate(event.date_start) + '</small></p>' +
                            '<p class="card-text"><small class="text-muted">End: ' + formatDate(event.date_end) + '</small></p>' +
                            '</div>' +
                            '<a href="/EventController/Views/' + event.event_id + '" class="btn btn-primary">View Event</a>' +
                            '</div>' +
                            '</div>' +
                            '</div>';

                    eventCardsContainer.insertAdjacentHTML('beforeend', eventCard);
                }

                // Cập nhật số sự kiện đã hiển thị
                eventsDisplayed += eventsPerPage;

                // Ẩn nút "More" nếu đã hiển thị hết sự kiện
                if (eventsDisplayed >= allEvents.length) {
                    document.getElementById('loadMoreButton').style.display = 'none';
                }
                // Hiện nút "Show Less" nếu đã hiển thị nhiều sự kiện
                if (eventsDisplayed > eventsPerPage) {
                    document.getElementById('showLessButton').style.display = 'inline-block';
                }
            }

            // Khi trang được tải, gọi Ajax để lấy dữ liệu sự kiện
            loadEvents();

            // Thêm sự kiện cho nút "More"
            document.getElementById('loadMoreButton').addEventListener('click', displayEventCards);

            // Add event listener for the "Show Less" button
            document.getElementById('showLessButton').addEventListener('click', function () {
                const eventCardsContainer = document.getElementById('eventCards');
                // Calculate the total number of cards currently displayed
                const totalCards = eventCardsContainer.children.length;
                // Define how many cards to remove
                const cardsToRemove = eventsPerPage;
                // Calculate the new displayed count, ensuring it doesn't go below zero
                const newDisplayedCount = Math.max(eventsDisplayed - cardsToRemove, 0);

                // Remove cards from the end
                for (let i = totalCards - 1; i >= newDisplayedCount; i--) {
                    eventCardsContainer.removeChild(eventCardsContainer.children[i]);
                }

                eventsDisplayed = newDisplayedCount; // Update the number of displayed events

                // Show the "More" button if exactly 2 events are displayed
                if (eventsDisplayed === 2) {
                    document.getElementById('loadMoreButton').style.display = 'inline-block';
                    document.getElementById('showLessButton').style.display = 'none';
                }

                // Hide the "Show Less" button if no events are left
                if (eventsDisplayed === 0) {
                    document.getElementById('showLessButton').style.display = 'none';
                }
            });

            // Hàm định dạng ngày tháng
            function formatDate(dateString) {
                // Tách chuỗi ngày thành các phần
                const parts = dateString.split('-');
                // Đảm bảo rằng chúng ta có 3 phần
                if (parts.length === 3) {
                    const year = parts[0];
                    const month = parts[1];
                    const day = parts[2];
                    // Trả về định dạng dd-mm-yyyy
                    return day + '-' + month + '-' + year;
                }
                return dateString; // Trả về giá trị gốc nếu không phải định dạng đúng
            }
        </script>
    </body>
</html>
