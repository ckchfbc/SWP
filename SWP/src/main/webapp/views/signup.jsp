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
        <link rel="stylesheet" type="text/css" href="css/font.css"/>
        <link rel="icon" href="${host}/ImageController/logo.png" type="image/x-icon">
        <title>Sign Up Page</title>
        <title>JSP Page</title>
    </head>
    <body>
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
                        <h2 class="fw-bold">Get Started Now</h2>
                        <!-- Hiện thông báo có tài khoản rồi -->
                        <c:if test="${not empty message}">
                            <div class="alert alert-warning">${message}</div>
                        </c:if>
                        <%
                            session.removeAttribute("message");
                        %>
                        <form action="/LoginController" method="POST">
                            <div class="mb-3">
                                <label for="name" class="form-label">Name</label>
                                <input name="nameTxt" required type="text" class="form-control" id="name" placeholder="Enter your name">
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Email address</label>
                                <input name="emailTxt" required type="email" class="form-control" id="email" placeholder="Enter your email">
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input name="pwdTxt" required type="password" class="form-control" id="password" placeholder="Enter your password">
                            </div>
                            <div class="form-check mb-3">
                                <input type="checkbox" class="form-check-input" id="terms" name="agreeBox">
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
