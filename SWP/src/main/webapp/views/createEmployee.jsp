<%-- 
    Document   : employeeCreate
    Created on : 21 Oct 2024
    Author     : thaii
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Employee</title>
        <link
            href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"
            rel="stylesheet">
        <style>
            #alertError {
                display: none;
            }
        </style>       
    </head>
    <body>
        <div class="container mt-5">
            <h2>Create Employee</h2>
            <form id="employeeForm" method="POST" action="/EmployeeController/create">
                <div class="mb-3">
                    <label for="employeeName" class="form-label">Employee Name:</label>
                    <input type="text" class="form-control" id="employeeName" name="employee_name" required>
                </div>
                <div class="mb-3">
                    <label for="employeeEmail" class="form-label">Email:</label>
                    <input type="email" class="form-control" id="employeeEmail" name="employee_email" required>
                </div>
                <div class="mb-3">
                    <label for="employeePassword" class="form-label">Password:</label>
                    <input type="password" class="form-control" id="employeePassword" name="employee_password" required>
                </div>
                <div class="mb-3">
                    <label for="employeePhone" class="form-label">Phone Number:</label>
                    <input type="text" class="form-control" id="employeePhone" name="employee_phone" required>
                </div>
                <!-- Alert Section -->
                <div class="alert alert-warning alert-dismissible fade d-none" role="alert" id="alertError">
                    <p id="alertTxt" class="p-0 m-0"></p>
                    <button type="button" class="btn-close" aria-label="Close" onclick="hideAlert()"></button>
                </div>

                <button type="submit" class="btn btn-primary" id="createEmployee" name="createEmployee">Submit</button>
            </form>
        </div>

        <script>
            document.getElementById('employeeForm').addEventListener('submit', function (event) {
                const employeeName = document.getElementById('employeeName').value;
                const employeeEmail = document.getElementById('employeeEmail').value;
                const employeePassword = document.getElementById('employeePassword').value;
                const employeePhone = document.getElementById('employeePhone').value;

                this.submit();  // Submit the form after processing
            });

        </script>
    </body>
</html>
