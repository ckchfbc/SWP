<%-- 
    Document   : signup
    Created on : 28 thg 9, 2024, 15:47:51
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
        <title>Sign Up Page</title>
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
        <script>
            function validateForm() {
                var OTPResult = document.getElementById('verificationResult').value; // Lấy giá trị của input hidden
                if (OTPResult !== 'Success') { // Kiểm tra nếu giá trị không phải 'Success'
                    alert("Vui lòng xác thực OTP"); // In ra giá trị OTPResult
                    return false; // Dừng thực hiện
                } else {
                    // Thực hiện hành động khác nếu OTPResult là 'Success'
                    console.log("OTP xác nhận thành công.");
                }

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
            function showNoti() {
                document.getElementById('notificationOtp').removeAttribute('hidden');
            }

            function offNoti() {
                document.getElementById('notificationOtp').hidden;
            }

            function offMb3() {
                document.getElementById('sendOtpButton').className = document.getElementById('sendOtpButton').className.replace('mb-3', '').trim();
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
                        $('#otpSend').removeClass('mb-3');
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
                var otpSend = document.getElementById("otpSend"); // Giả sử id của thẻ là otpSend
                var countdown = 60;

                // Vô hiệu hóa nút
                sendOtpButton.disabled = true;

                // Cập nhật nội dung nút với thời gian đếm ngược
                sendOtpButton.innerText = "Chờ " + countdown + " giây";

                // Tạo bộ đếm ngược
                var interval = setInterval(function () {
                    countdown--;
                    sendOtpButton.innerText = "Chờ " + countdown + " giây";

                    // Kiểm tra nếu otpSend không có class mb-3 thì dừng bộ đếm
                    if (!otpSend.classList.contains('mb-3')) {
                        clearInterval(interval);
                        sendOtpButton.disabled = false;
                        sendOtpButton.innerText = "Send OTP";
                        return; // Thoát khỏi hàm
                    }

                    // Khi hết 60 giây, kích hoạt lại nút
                    if (countdown <= 0) {
                        clearInterval(interval);
                        sendOtpButton.disabled = false;
                        sendOtpButton.innerText = "Send OTP";
                    }
                }, 1000); // Cập nhật mỗi giây
            }

        </script>
        <%
            Cookie[] cookies = request.getCookies();
            // Duyệt qua các cookies và kiểm tra cookie "userEmail" nếu có thì quay về trang chủ
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("userEmail")) {
                        response.sendRedirect("/");
                        break;
                    }
                }
            }
        %>
    <div class="container-fluid vh-100">
        <div class="row h-100">
            <!-- Left side with form -->
            <div class="col-lg-6 d-flex justify-content-center align-items-center">
                <div class="w-100" style="max-width: 400px;">
                    <a class="logo text-decoration-none" href="/"><h1 class="mb-3">Welcome to  DriveAura</h1></a>   
                    <!-- Hiện thông báo có tài khoản rồi -->
                    <c:if test="${not empty message}">
                        <div class="alert alert-warning">${message}</div>
                    </c:if>
                    <%
                        session.removeAttribute("message");
                    %>
                    <form onsubmit="return validateForm()" action="/ResetPasswordController" method="POST">
                        <div class="mb-3">
                            <label for="name" class="form-label">Name</label>
                            <input name="nameTxt" required type="text" class="form-control" id="name" placeholder="Enter your name">
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email address</label>
                            <input name="emailTxt" required type="email" class="form-control" id="email" placeholder="Enter your email">
                            <span id="emailError" class="text-danger"></span> <!-- Thêm thẻ này để hiển thị lỗi -->
                        </div>
                        <label for="password" class="form-label">Password</label>
                        <div class="mb-3 d-flex">
                            <input required type="password" class="form-control" id="password" name="pwdTxt" placeholder="Enter your password">
                            <button type="button" class="form-control btn btn-outline-dark" id="showPassword" onclick="togglePassword()" style="width: 50px;">
                                <i class="fa-solid fa-eye p-0 m-0" id="icon"></i>
                            </button>
                        </div>
                        <span id="passwordError" class="text-danger mb-3"></span> <!-- Thêm thẻ này để hiển thị lỗi -->                           
                        <!-- Bắt đầu phần OTP -->
                        <div class="d-flex mb-3 align-items-center" id="otpSend">
                            <button type="button" class="btn btn-outline-primary me-3" id="sendOtpButton" onclick="showNoti();
                                    sendOtp();
                                    disableButtonWithCountdown()">Send OTP</button>
                            <p class="text-danger" hidden id="notificationOtp">Please wait a few seconds.</p>
                        </div>
                        <div id="otpInput" style="display: none;" class="mb-3">
                            <label for="otp" class="form-label">Enter your code</label>
                            <div class="mb-2">
                                <input type="text" class="form-control" id="otp" name="otp">
                            </div>
                            <button type="button" class="btn btn-success" onclick="verifyOtp()">Verification</button>
                        </div>
                        <p style="display: none;" class="text-success mb-3 fw-bold" id="OTPSuccess">OTP authentication successful!</p>
                        <input hidden id="verificationResult" class="mt-3" name="OTPResult" value=""/>
                        <!-- Kết thúc phần OTP -->
                        <div class="form-check mb-3">
                            <input required type="checkbox" class="form-check-input" id="terms" name="agreeBox">
                            <label class="form-check-label" for="terms">I agree to the <a href="#">terms & policy</a></label>
                        </div>
                        <button type="submit" class="btn btn-dark w-100 mb-3" name="signUpBtn">Sign Up</button>
                        <div class="text-center">Or</div>   
                        <a class="btn btn-outline-dark w-100 mt-3" href="https://accounts.google.com/o/oauth2/auth?scope=email profile openid&redirect_uri=http://localhost:8080/LoginController/SignUp&response_type=code&client_id=660845253786-djntvvn4rk8lnt6vmrbop3blvttdmrnm.apps.googleusercontent.com&approval_prompt=force&state=signup">
                            <img src="${host}/ImageController/logoGG.png" style="width: 20px;" alt="Google Logo"> 
                            Sign up with Google
                        </a>
                    </form>
                    <div class="text-center mt-3">
                        Have an account? <a href="/HomePageController/Login">Login</a>
                    </div>
                </div>
            </div>  
            <!-- Right side with background -->
            <div class="col-lg-6 d-none d-lg-block" style="background-image: url('${host}/ImageController/loginImage.jpg'); background-size: cover; background-position: center;"></div>
        </div>
    </div>
</body>
</html>
