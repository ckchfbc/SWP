<%-- 
    Document   : resetPWD
    Created on : 5 October, 2024, 00:38:46
    Author     : thaii
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Copy this from here to title when creating a new JSP, add other elements above <title> if needed -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/a611f8fd5b.js" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/a611f8fd5b.js" crossorigin="anonymous"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link rel="stylesheet" type="text/css" href="css/font.css"/>
        <link rel="icon" href="${host}/ImageController/logo.png" type="image/x-icon">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <title>Reset Password</title>
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
                color: inherit; /* Preserve color */
            }

            .btn-dark {
                background-color: #050B20;
            }
        </style>
    </head>
    <body>
        <script>
            function validateForm() {
                var OTPResult = document.getElementById('verificationResult').value; // Get the value of the hidden input
                if (OTPResult !== 'Success') { // Check if the value is not 'Success'
                    var alertOTP = document.getElementById("alertOTP");

                    if (alertOTP) {
                        alertOTP.classList.remove('d-none'); // Display OTP message
                        alertOTP.classList.add('d-block');
                    }
                    return false; // Prevent form submission
                }

                console.log("Start validation form");
                var email = document.getElementById("email").value;
                var password = document.getElementById("password").value;
                var confirmPassword = document.getElementById("confirmPassword").value;
                var emailError = document.getElementById("emailError");
                var passwordError = document.getElementById("passwordError");
                var confirmError = document.getElementById("confirmError");

                // Reset error messages
                emailError.textContent = "";
                passwordError.textContent = "";
                confirmError.textContent = "";

                // Validate email format
                var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(email)) {
                    emailError.textContent = "Invalid email.";
                    event.preventDefault();
                    return false; // Prevent form submission
                }

                // Validate password
                var passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_])[A-Za-z\d\W_]{6,32}$/;
                if (!passwordRegex.test(password)) {
                    passwordError.textContent = "Password must be 6-32 characters, with at least 1 uppercase letter, 1 number, and 1 special character.";
                    event.preventDefault();
                    return false; // Prevent form submission
                }

                // Check password match
                if (password !== confirmPassword) {
                    confirmError.textContent = "Passwords do not match.";
                    event.preventDefault();
                    return false; // Prevent form submission
                }

                return true; // Allow submission if valid
            }

            function checkPasswordsMatch() {
                var password = document.getElementById('password').value;
                var confirmPassword = document.getElementById('confirmPassword').value;
                var confirmError = document.getElementById('confirmError');
                var resetBtn = document.querySelector("button[name='resetPWDBtn']");

                if (password !== confirmPassword) {
                    confirmError.textContent = "Passwords do not match.";
                    resetBtn.disabled = true; // Disable button when passwords do not match
                } else {
                    confirmError.textContent = ""; // Clear error message if passwords match
                    resetBtn.disabled = false; // Enable button if passwords match
                }
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
                        if (response === true) {
                            $('#otpInput').show();
                            $('#sendOtpButton').hide();
                            $('#notificationOtp').hide();
                            $('#otpSend').removeClass('mb-3');
                            Swal.fire({
                                icon: 'success',
                                title: 'Success',
                                text: 'OTP has been sent successfully!'
                            });
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Error',
                                text: 'An error occurred or the email is not registered!'
                            });
                        }
                    },
                    error: function (xhr, status, error) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: "An error occurred: " + error
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
                                icon: 'success',
                                title: 'Verification Successful',
                                text: 'Your OTP has been verified successfully!',
                            });
                        } else {
                            $('#otpInput').hide();
                            $('#sendOtpButton').show();
                            $('#notificationOtp').show();
                            $('#otpSend').addClass('mb-3');
                            Swal.fire({
                                icon: 'error',
                                title: 'Verification Failed',
                                text: 'OTP is incorrect or has expired. Please try again.',
                            });
                        }
                    },
                    error: function (xhr, status, error) {
                        alert("Error: " + error);
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

                // Create countdown
                var interval = setInterval(function () {
                    countdown--;
                    sendOtpButton.innerText = "Wait " + countdown + " seconds";

                    // Check if otpSend does not have mb-3 class then stop the countdown
                    if (!otpSend.classList.contains('mb-3') || !alertOTP.classList.contains('d-none')) {
                        clearInterval(interval);
                        sendOtpButton.disabled = false;
                        sendOtpButton.innerText = "Send OTP";
                        return; // Exit the function
                    }

                    // When 60 seconds is up, enable the button again
                    if (countdown <= 0) {
                        clearInterval(interval);
                        sendOtpButton.disabled = false;
                        sendOtpButton.innerText = "Send OTP";
                    }
                }, 1000); // Update every second
            }

            function checkPasswords() {
                var password = document.getElementById('password').value;
                var confirmPassword = document.getElementById('confirmPassword').value;
                var confirmError = document.getElementById('confirmError');

                if (password !== confirmPassword) {
                    confirmError.textContent = "Passwords do not match.";
                    return false;
                } else {
                    confirmError.textContent = "";
                    return true;
                }
            }
        </script>
        <div class="container-fluid" style="background-color: white;">
            <div class="container pt-4 pb-4">
                <div class="text-center" style="font-size: 35px;">
                    <a class="logo" href="home">StudyMate</a>
                </div>
                <div class="pt-4 pb-4 text-center fs-5 fw-bolder">Reset Your Password</div>
                <div class="row d-flex justify-content-center">
                    <div class="col-lg-6 col-sm-6 p-3 border rounded-3 shadow-sm p-4">
                        <form id="resetForm" action="resetPWD" method="POST" onsubmit="return validateForm();">
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" name="email" placeholder="Email" required>
                                <p id="emailError" class="text-danger"></p>
                            </div>
                            <div id="otpSend" class="mb-3">
                                <button type="button" onclick="sendOtp(); disableButtonWithCountdown();" id="sendOtpButton" class="btn btn-dark form-control">Send OTP</button>
                            </div>
                            <div id="notificationOtp" class="text-muted text-center mb-3">You will receive an OTP via email.</div>
                            <input type="hidden" id="verificationResult" name="verificationResult" value="">
                            <div id="otpInput" class="mb-3" style="display: none;">
                                <input type="text" class="form-control mb-2" id="otp" placeholder="OTP" aria-describedby="verifyBtn">
                                <div class="input-group-append">
                                    <button class="btn btn-dark form-control" id="verifyBtn" onclick="verifyOtp();" type="button">Verify OTP</button>
                                </div>
                            </div>
                            <div id="alertOTP" class="text-danger d-none text-center mb-2" role="alert">Please verify your OTP.</div>
                            <div id="OTPSuccess" class="alert alert-success mb-2" style="display:none;" role="alert">OTP verification successful!</div>
                            <div class="mb-3">
                                <label for="password" class="form-label">New Password</label>
                                <input type="password" class="form-control" id="password" name="password" placeholder="Password" onchange="checkPasswords()" required>
                                <i class="far fa-eye" id="icon" style="cursor: pointer; position: absolute; margin-left: -30px; margin-top: 10px;" onclick="togglePassword()"></i>
                                <p id="passwordError" class="text-danger"></p>
                            </div>
                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label">Confirm New Password</label>
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="Confirm Password" onchange="checkPasswords()" required>
                                <p id="confirmError" class="text-danger"></p>
                            </div>
                            <button type="submit" name="resetPWDBtn" class="btn btn-dark form-control">Reset Password</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
