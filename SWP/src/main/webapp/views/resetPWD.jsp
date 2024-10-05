<%-- 
    Document   : resetPWD
    Created on : 5 thg 10, 2024, 00:38:46
    Author     : thaii
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- AE copy từ đây tới title nếu tạo jsp mới thêm các thể khác thì thêm trên <title> -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/a611f8fd5b.js" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/a611f8fd5b.js" crossorigin="anonymous"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link rel="stylesheet" type="text/css" href="css/font.css"/>
        <link rel="icon" href="${host}/ImageController/logo.png" type="image/x-icon">
        <title>ResetPass</title>
        <%
            String host = request.getRequestURI();
        %>
        <style>
            @font-face {
                font-family: 'Kirsty'; /* Your font name */
                src: url('../fonts/kirsty rg.otf') format('opentype'); /* Path to your font */
                font-weight: normal;
                font-style: normal;
            }

            .logo  {
                font-family: 'Kirsty', sans-serif;
                color: #050B20;
            }

            a:hover {
                color: inherit; /* Giữ nguyên màu */
            }

            .btn-dark{
                background-color: #050B20;
            }
        </style>
    </head>
    <body>
        <script>
            function validateForm() {
                var OTPResult = document.getElementById('verificationResult').value; // Lấy giá trị của input hidden
                if (OTPResult !== 'Success') { // Kiểm tra nếu giá trị không phải 'Success'                 
                    var alertOTP = document.getElementById("alertOTP");

                    // Kiểm tra nếu thẻ không null và thao tác với class
                    if (alertOTP) {
                        alertOTP.classList.remove('d-none'); // Xóa class d-none để hiển thị thông báo
                        alertOTP.classList.add('d-block'); // Thêm class d-block để hiển thị block
                    }

                    return false; // Dừng thực hiện
                } else {
                    // Thực hiện hành động khác nếu OTPResult là 'Success'
                    if (alertOTP) {
                        alertOTP.classList.remove('d-block'); // Xóa class d-block nếu có
                        alertOTP.classList.add('d-none'); // Thêm class d-none để ẩn thông báo
                    }
                    console.log("OTP xác nhận thành công.");
                }

                console.log("Start validation form")
                var email = document.getElementById("email").value;
                var password = document.getElementById("password").value;
                var emailError = document.getElementById("emailError");
                var passwordError = document.getElementById("passwordError");

                // Reset thông báo lỗi
                emailError.textContent = "";
                passwordError.textContent = "";

                // Regular expression kiểm tra định dạng email
                var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(email)) {
                    emailError.textContent = "Email không hợp lệ.";
                    console.log("Email không hợp lệ");
                    return false; // Dừng form submit
                }

                // Regular expression kiểm tra mật khẩu
                var passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_])[A-Za-z\d\W_]{6,32}$/;
                if (!passwordRegex.test(password)) {
                    passwordError.textContent = "Mật khẩu phải có từ 6-32 ký tự, ít nhất 1 chữ in hoa, 1 số và 1 ký tự đặc biệt.";
                    console.log("Password không hợp lệ");
                    return false; // Dừng form submit
                }

                console.log("Form validation passed"); // Form hợp lệ
                document.getElementById("alertOTP").innerHTML = "";
                return true;
            }
            function togglePassword() {
                var passwordField = document.getElementById("password");
                var icon = document.getElementById("icon");

                if (passwordField.type === "password") {
                    passwordField.type = "text";
                    icon.classList.remove("fa-eye");
                    icon.classList.add("fa-eye-slash");
                } else {
                    passwordField.type = "password";
                    icon.classList.remove("fa-eye-slash");
                    icon.classList.add("fa-eye");
                }
            }

            function sendOtp() {
                const email = $('#email').val();
                $.ajax({
                    type: "POST",
                    url: "/SendOtpServlet",
                    data: {email: email},
                    success: function (response) {
                        $('#otpInput').show();
                        $('#sendOtpButton').hide();
                        $('#notificationOtp').hide();
                    },
                    error: function (xhr, status, error) {
                        alert("Lỗi: " + error);
                    }
                });
            }

            function verifyOtp() {
                const otp = $('#otp').val();
                $.ajax({
                    type: "POST",
                    url: "VerifyOtpServlet",
                    data: {otp: otp},
                    success: function () {
                        $('#verificationResult').val('Success');
                        $('#otpInput').hide();
                        $('#OTPSuccess').css('display', 'block');
                    },
                    error: function (xhr, status, error) {
                        alert("Lỗi: " + error);
                    }
                });
            }

            function disableButtonWithCountdown() {
                var sendOtpButton = document.getElementById("sendOtpButton");
                var countdown = 60;

                // Vô hiệu hóa nút
                sendOtpButton.disabled = true;

                // Cập nhật nội dung nút với thời gian đếm ngược
                sendOtpButton.innerText = "Chờ " + countdown + " giây";

                // Tạo bộ đếm ngược
                var interval = setInterval(function () {
                    countdown--;
                    sendOtpButton.innerText = "Chờ " + countdown + " giây";

                    // Khi hết 60 giây, kích hoạt lại nút
                    if (countdown <= 0) {
                        clearInterval(interval);
                        sendOtpButton.disabled = false;
                        sendOtpButton.innerText = "Send OTP";
                    }
                }, 1000); // Cập nhật mỗi giây
            }
            function checkPasswords() {
                var password = document.getElementById('password').value;
                var confirmPassword = document.getElementById('confirmPassword').value;
                var confirmError = document.getElementById('confirmError');

                if (password !== confirmPassword) {
                    confirmError.textContent = "Mật khẩu không khớp.";
                } else {
                    confirmError.textContent = ""; // Xóa thông báo lỗi nếu mật khẩu khớp
                }
            }
            function showNoti() {
                document.getElementById('notificationOtp').removeAttribute('hidden');
            }

            function offNoti() {
                document.getElementById('notificationOtp').hidden;
            }
        </script>       

        <div class="container-fluid vh-100 m-0 p-0">
            <div class="row h-100">
                <div class="col-lg-6 d-flex justify-content-center align-items-center">
                    <div class="w-100" style="max-width: 400px;">
                        <a class="logo text-decoration-none" href="/"><h1 class="mb-5">DriveAura</h1></a>   
                        <div class="alert alert-warning d-none" id="alertOTP">Please verify OTP.</div>
                        <c:if test="${not empty message}">
                            <div class="alert alert-warning">${message}</div>
                        </c:if>
                        <h2 class="mb-3">Reset Password</h2>
                        <form onsubmit="return validateForm()" action="/ResetPasswordController" method="POST">
                            <div class="form-group mb-3">
                                <label for="email" class="form-label">Email address</label>
                                <input name="emailTxt" required type="email" class="form-control" id="email" placeholder="Enter your email">
                                <span id="emailError" class="text-danger"></span> <!-- Thêm thẻ này để hiển thị lỗi -->
                            </div>
                            <label for="password" class="form-label">Password</label>
                            <div class="mb-3 d-flex form-group">
                                <input required type="password" class="form-control" id="password" name="pwdTxt" placeholder="Enter your password">
                                <button type="button" class="form-control btn btn-outline-dark" id="showPassword" onclick="togglePassword()" style="width: 50px;">
                                    <i class="fa-solid fa-eye p-0 m-0" id="icon"></i>                                    
                                </button>                               
                            </div>
                            <span id="passwordError" class="text-danger mb-3"></span> <!-- Thêm thẻ này để hiển thị lỗi -->                           

                            <div class="form-group mb-3">
                                <label for="confirmPassword">Xác nhận mật khẩu:</label>
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required oninput="checkPasswords()">
                                <small id="confirmError" class="text-danger"></small>
                            </div>

                            <!-- Bắt đầu phần OTP -->
                            <div class="d-flex mb-3 align-items-center">
                                <button type="button" class="btn btn-outline-primary me-3" id="sendOtpButton" onclick="showNoti();
                                        sendOtp();
                                        disableButtonWithCountdown()">Send OTP</button>
                                <p class="text-danger" hidden id="notificationOtp">Please wait a few seconds.</p>
                            </div>
                            <div id="otpInput" style="display: none;" class="mb-3">
                                <label for="otp" class="form-label">Enter your code</label>
                                <div class="mb-3">
                                    <input type="text" class="form-control" id="otp" name="otp">
                                </div>
                                <button type="button" class="btn btn-success" onclick="verifyOtp()">Verification</button>
                            </div>
                            <p style="display: none;" class="text-success mb-3 fw-bold" id="OTPSuccess">OTP authentication successful!</p>
                            <input hidden id="verificationResult" class="mt-3" name="OTPResult" value=""/>
                            <!-- Kết thúc phần OTP -->

                            <button type="submit" class="btn btn-dark" name="resetPWDBtn">Đặt lại mật khẩu</button>
                        </form>
                    </div>
                </div>
                <div class="col-lg-6 d-none d-lg-block" style="background-image: url('${host}/ImageController/loginImage.jpg'); background-size: cover; background-position: center;"></div>
            </div>
        </div>
    </body>
</html>
