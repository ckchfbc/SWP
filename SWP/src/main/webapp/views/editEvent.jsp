<%-- 
    Document   : editEvent
    Created on : 12 thg 10, 2024, 00:42:36
    Author     : thaii
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link
            href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"
            rel="stylesheet">
        <!-- Quill Editor -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/quill@1.3.7/dist/quill.snow.css">
        <script src="https://cdn.jsdelivr.net/npm/quill@1.3.7/dist/quill.min.js"></script>
        <style>
            #editor-container {
                height: 200px;
            }
        </style>       
    </head>
    <body>
        <div class="container mt-5">
            <h2>Create Event</h2>
            <form action="EventController" method="post" enctype="multipart/form-data" id="eventForm">
                <div class="mb-3">
                    <label for="eventName" class="form-label">Event Name:</label>
                    <input type="text" class="form-control" id="eventName" name="event_name" required>
                </div>
                <div class="mb-3">
                    <label for="eventDetails" class="form-label">Event Details:</label>
                    <div id="editor-container"></div>                    
                    <input type="hidden" name="event_details" id="eventDetails">
                </div>
                <div class="mb-3">
                    <label for="eventImage" class="form-label">Upload Image:</label>
                    <input type="file" class="form-control" id="eventImage" name="event_image" required>
                </div>
                <div class="mb-3">
                    <label for="dateStart" class="form-label">Date Start:</label>
                    <input type="date" class="form-control" id="dateStart" name="date_start" required>
                </div>
                <div class="mb-3">
                    <label for="dateEnd" class="form-label">Date End:</label>
                    <input type="date" class="form-control" id="dateEnd" name="date_end" required>
                </div>
                <!-- Phần alert của bắt lỗi -->
                <div class="alert alert-warning alert-dismissible fade d-none show mb-3" role="alert" id="alertError">                    
                    <p id="alertTxt" class="p-0 m-0"></p>
                    <button type="button" class="btn-close" aria-label="Close" onclick="hideAlert()"></button>
                </div>
                <!-- Phần alert của bắt lỗi -->

                <button type="submit" class="btn btn-primary" name="createEvent">Submit</button>
            </form>
        </div>
        <script>
            var toolbarOptions = [
                ['bold', 'italic', 'underline', 'strike'], // toggled buttons
                ['blockquote', 'code-block'],

                [{'header': 1}, {'header': 2}], // custom button values
                [{'list': 'ordered'}, {'list': 'bullet'}],
                [{'script': 'sub'}, {'script': 'super'}], // superscript/subscript
                [{'indent': '-1'}, {'indent': '+1'}], // outdent/indent
                [{'direction': 'rtl'}], // text direction

                [{'size': ['small', false, 'large', 'huge']}], // custom dropdown
                [{'header': [1, 2, 3, 4, 5, 6, false]}],

                [{'color': []}, {'background': []}], // dropdown with defaults from theme
                [{'font': []}],
                [{'align': []}],

                ['link', 'image', 'video'], // link and media
                ['clean']                                         // remove formatting button
            ];

            var quill = new Quill('#editor-container', {
                modules: {
                    toolbar: toolbarOptions
                },
                theme: 'snow'
            });
            document.getElementById('eventForm').addEventListener('submit', function () {
                document.getElementById('eventDetails').value = quill.root.innerHTML;
            });

            document.getElementById('eventForm').addEventListener('submit', function (event) {
                // Lấy giá trị của các input
                const eventName = document.getElementById('eventName').value;
                const eventDetails = document.getElementById('eventDetails').value;
                const eventImage = document.getElementById('eventImage').files[0]; // Lấy file upload
                const dateStart = document.getElementById('dateStart').value;
                const dateEnd = document.getElementById('dateEnd').value;
                const today = new Date().toISOString().split('T')[0]; // Ngày hiện tại theo định dạng yyyy-mm-dd                
                // Kiểm tra độ dài của event_name (không quá 255 ký tự)

                if (eventName.length > 255) {
                    sendMessageError('Event Name cannot exceed 255 characters.');
                    event.preventDefault(); // Ngăn không cho submit form
                    return;
                }

                // Kiểm tra độ dài của event_details (tùy chỉnh theo giới hạn, ở đây là 5000 ký tự)
                if (eventDetails.length > 500000) {
                    sendMessageError('Event Details cannot exceed 500000 characters.');
                    event.preventDefault(); // Ngăn không cho submit form
                    return;
                }

                // Kiểm tra định dạng ảnh (chỉ cho phép JPEG, PNG, GIF)
                const validImageTypes = ['image/jpeg', 'image/png', 'image/gif'];
                if (eventImage && !validImageTypes.includes(eventImage.type)) {
                    sendMessageError('Please upload a valid image file (JPEG, PNG, or GIF).');
                    event.preventDefault(); // Ngăn không cho submit form
                    return;
                }

                // Kiểm tra ngày (dateStart không nhỏ hơn ngày hiện tại và dateEnd không nhỏ hơn dateStart)
                if (dateStart < today) {
                    sendMessageError('Start Date cannot be earlier than today.');
                    event.preventDefault(); // Ngăn không cho submit form
                    return;
                }

                if (dateEnd <= dateStart) {
                    sendMessageError('End Date must be greater than Start Date.');
                    event.preventDefault(); // Ngăn không cho submit form
                    return;
                }
            });

            function hideAlert() {
                var alertElement = document.getElementById('alertError');
                alertElement.classList.add('d-none');
            }

            function sendMessageError(mess) {
                const alertText = document.getElementById('alertTxt');
                const alertError = document.getElementById('alertError');
                alertError.classList.remove("d-none");
                alertText.innerHTML = mess;
            }
        </script>
    </body>
</html>