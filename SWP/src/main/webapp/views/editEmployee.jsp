<%-- 
    Document   : editEmployee
    Created on : Oct 22, 2024, 9:34:23 AM
    Author     : counh
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Edit Employee</title>
    </head>
    <body>
        <h2>Edit Employee Details</h2>

        <!-- Form to edit employee details -->
        <form id="editEmployeeForm" action="EmployeeController" method="post" onsubmit="return validateForm()">
            <!-- Hidden field to trigger the update operation -->
            <input type="hidden" name="updateEmployee" value="true">

            <label for="employee_id">Employee ID:</label>
            <input type="text" id="employee_id" name="employee_id" readonly required
                   value="${employee.employeeId}"><br><br>

            <label for="employee_name">Name:</label>
            <input type="text" id="employee_name" name="employee_name" required
                   value="${employee.name}"><br><br>

            <label for="employee_email">Email:</label>
            <input type="email" id="employee_email" name="employee_email" required
                   pattern="[^@ \t\r\n]+@[^@ \t\r\n]+\.[^@ \t\r\n]+"
                   title="Please enter a valid email address with '@' symbol"
                   value="${employee.email}"><br><br>

            <label for="employee_password">New Password (optional):</label>
            <input type="password" id="employee_password" name="employee_password"><br><br>

            <label for="employee_phone">Phone:</label>
            <input type="text" id="employee_phone" name="employee_phone" pattern="\d{10}"
                   title="Please enter a valid 10-digit phone number" value="${employee.phoneNumber}"><br><br>

            <button type="submit">Update Employee</button>
        </form>

        <script>
            function validateForm() {
                // Kiểm tra email
                const email = document.getElementById("employee_email").value;
                const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailPattern.test(email)) {
                    alert("Please enter a valid email address with '@' symbol.");
                    return false;
                }

                // Kiểm tra số điện thoại
                const phone = document.getElementById("employee_phone").value;
                const phonePattern = /^\d{10}$/;
                if (!phonePattern.test(phone)) {
                    alert("Please enter a valid 10-digit phone number.");
                    return false;
                }

                // Kiểm tra mật khẩu (nếu có)
                const password = document.getElementById("employee_password").value;
                if (password) { // Chỉ kiểm tra nếu mật khẩu được nhập
                    const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;
                    if (!passwordPattern.test(password)) {
                        alert("Password must contain at least 6 characters, including one uppercase letter, one lowercase letter, one number, and one special character.");
                        return false;
                    }
                }

                // Nếu tất cả các điều kiện đều đúng, cho phép gửi form
                return true;
            }
        </script>
    </body>
</html>
