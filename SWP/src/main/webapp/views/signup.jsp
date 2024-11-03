<%-- 
    Document   : signup
    Created on : 28 Sep, 2024, 15:47:51
    Author     : thaii
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Copy this from here to title if creating a new JSP, add other tags above <title> if needed -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/a611f8fd5b.js" crossorigin="anonymous"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link rel="stylesheet" type="text/css" href="css/font.css"/>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.14.4/dist/sweetalert2.all.min.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.14.4/dist/sweetalert2.min.css">
        <link rel="icon" href="${host}/ImageController/a/logo.png" type="image/x-icon">
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
                color: inherit; /* Keep color unchanged */
            }

            .btn-dark{
                background-color: #050B20;
            }
        </style>
        <script>
            function validateForm() {
                var OTPResult = document.getElementById('verificationResult').value; // Get value from hidden input
                if (OTPResult !== 'Success') { // Check if value is not 'Success'
                    alert("Please verify OTP"); // Print OTPResult value
                    return false; // Stop execution
                } else {
                    // Other actions if OTPResult is 'Success'
                    console.log("OTP verification successful.");
                }

                var email = document.getElementById("email").value;
                var password = document.getElementById("password").value;
                var emailError = document.getElementById("emailError");
                var passwordError = document.getElementById("passwordError");

                // Reset error messages
                emailError.textContent = "";
                passwordError.textContent = "";

                // Regular expression to check email format
                var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(email)) {
                    emailError.textContent = "Invalid email.";
                    console.log("Invalid email");
                    return false; // Stop form submit
                }

                // Regular expression to check password
                var passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_])[A-Za-z\d\W_]{6,32}$/;
                if (!passwordRegex.test(password)) {
                    passwordError.textContent = "Password must be 6-32 characters, at least 1 uppercase letter, 1 number, and 1 special character.";
                    console.log("Invalid password");
                    return false; // Stop form submit
                }

                console.log("Form validation passed"); // Form is valid
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

            function hideAlert() {
                var alertElement = document.getElementById('alertOTP');
                alertElement.classList.add('d-none');
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

                        // SweetAlert success notification
                        Swal.fire({
                            icon: 'success',
                            title: 'Success',
                            text: 'OTP sent successfully!'
                        });
                    },
                    error: function (xhr, status, error) {
                        $('#alertOTP').removeClass('d-none');
                        $('#notificationOtp').attr('hidden', true);

                        // SweetAlert error notification
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: 'Unable to send OTP. Please try again later.'
                        });
                    }

                });
            }

            function verifyOtp() {
                const otp = $('#otp').val();
                $.ajax({
                    type: "POST",
                    url: "/VerifyOtpServlet",
                    data: {otp: otp},
                    success: function (response) {
                        if (response === true) {
                            $('#verificationResult').val('Success');
                            $('#otpInput').hide();
                            $('#OTPSuccess').css('display', 'block');

                            Swal.fire({
                                title: 'Verification Successful!',
                                text: 'Your OTP has been verified successfully.',
                                icon: 'success',
                                confirmButtonText: 'OK'
                            });
                        } else {
                            $('#otpInput').hide();
                            $('#sendOtpButton').show();
                            $('#otpSend').addClass('mb-3');

                            Swal.fire({
                                title: 'Verification Failed',
                                text: 'The OTP is incorrect. Please try again.',
                                icon: 'error',
                                confirmButtonText: 'OK'
                            });
                        }
                    },
                    error: function (xhr, status, error) {
                        Swal.fire({
                            title: 'Error',
                            text: 'An error occurred during OTP verification. Please try again.',
                            icon: 'error',
                            confirmButtonText: 'OK'
                        });
                    }
                });
            }

            function disableButtonWithCountdown() {
                var sendOtpButton = document.getElementById("sendOtpButton");
                var otpSend = document.getElementById("otpSend");
                var alertOTP = document.getElementById("alertOTP");
                var countdown = 60;

                // Disable button
                sendOtpButton.disabled = true;

                // Update button text with countdown
                sendOtpButton.innerText = "Wait " + countdown + " seconds";

                // Create countdown timer
                var interval = setInterval(function () {
                    countdown--;
                    sendOtpButton.innerText = "Wait " + countdown + " seconds";

                    // Stop timer if otpSend doesn't have 'mb-3' class
                    if (!otpSend.classList.contains('mb-3') || !alertOTP.classList.contains('d-none')) {
                        clearInterval(interval);
                        sendOtpButton.disabled = false;
                        sendOtpButton.innerText = "Send OTP";
                        return; // Exit function
                    }

                    // Enable button after 60 seconds
                    if (countdown <= 0) {
                        clearInterval(interval);
                        sendOtpButton.disabled = false;
                        sendOtpButton.innerText = "Send OTP";
                    }
                }, 1000); // Update every second
            }

        </script>
        <%
            Cookie[] cookies = request.getCookies();
            // Iterate through cookies and check for "userEmail" cookie; if found, redirect to homepage
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
                <div class="w-100" style="max-width: 450px;">
                    <h1 class="fw-bold mb-4 logo"><a href="/" class="text-decoration-none">Your Website Name</a></h1>
                    <h2 class="fw-bold mb-4">Create Your Account</h2>
                    <form action="SignUpServlet" method="post" onsubmit="return validateForm();">
                        <div class="form-group mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email" placeholder="Your email">
                            <div class="text-danger" id="emailError"></div>
                        </div>
                        <div class="form-group mb-3">
                            <label for="password" class="form-label">Password</label>
                            <div class="input-group">
                                <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                                <button type="button" class="btn btn-outline-secondary" onclick="togglePassword()">
                                    <i class="fas fa-eye" id="icon"></i>
                                </button>
                            </div>
                            <div class="text-danger" id="passwordError"></div>
                        </div>
                        <div class="form-group" id="otpSend">
                            <button type="button" class="btn btn-dark btn-block mb-3" id="sendOtpButton" onclick="sendOtp(); disableButtonWithCountdown();">Send OTP</button>
                            <p class="text-muted" id="notificationOtp" hidden>An OTP will be sent to your email.</p>
                        </div>
                        <div class="form-group d-none" id="alertOTP">
                            <p class="text-danger">Error occurred while sending OTP. Please try again later.</p>
                        </div>
                        <div class="form-group d-none" id="OTPSuccess">
                            <p class="text-success">OTP verified successfully.</p>
                        </div>
                        <div class="form-group mb-3" id="otpInput" style="display: none;">
                            <label for="otp" class="form-label">Enter OTP</label>
                            <input type="text" class="form-control" id="otp" name="otp" placeholder="One-time password">
                            <button type="button" class="btn btn-dark btn-block mt-3" onclick="verifyOtp()">Verify OTP</button>
                        </div>
                        <input type="hidden" id="verificationResult" value="">

                        <button type="submit" class="btn btn-dark btn-block mt-3">Sign Up</button>
                    </form>
                    <p class="mt-4 text-muted">Already have an account? <a href="signin.jsp" class="text-decoration-none">Sign in here</a></p>
                </div>
            </div>
            <!-- Right side with background -->
            <div class="col-lg-6 d-none d-lg-block bg-dark">
                <!-- Background and image elements here -->
            </div>
        </div>
    </div>
</html>
