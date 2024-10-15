<%-- 
    Document   : index
    Created on : Oct 8, 2024, 4:46:13 PM
    Author     : counh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <!-- CSS Links -->
        <link
            href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"
            rel="stylesheet">
        <link
            href="https://cdn.datatables.net/1.13.1/css/dataTables.bootstrap5.min.css"
            rel="stylesheet">
        <link
            href="https://cdn.datatables.net/responsive/2.4.1/css/responsive.bootstrap5.min.css"
            rel="stylesheet">
    </head>

    <body ng-controller>

        <!-- JavaScript Links -->
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <script
        src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
        <script
        src="https://cdn.datatables.net/1.13.1/js/jquery.dataTables.min.js"></script>
        <script
        src="https://cdn.datatables.net/1.13.1/js/dataTables.bootstrap5.min.js"></script>
        <script
        src="https://cdn.datatables.net/responsive/2.4.1/js/dataTables.responsive.min.js"></script>
        <script
        src="https://cdn.datatables.net/responsive/2.4.1/js/responsive.bootstrap5.min.js"></script>

        <!-- DataTable Initialization -->
        <script>
            $(document).ready(function () {
                // Gọi AJAX để lấy dữ liệu sự kiện
                $.ajax({
                    url: "/EventController", // URL của Servlet
                    type: "POST", // Phương thức HTTP POST
                    data: {fetchData: "true"},
                    dataType: "json", // Định dạng dữ liệu trả về là JSON
                    success: function (events) {
                        // Initialize DataTables after receiving data
                        var table = $('#eventsTable').DataTable({
                            responsive: true, // Bật chế độ responsive
                            data: events, // Load the data directly into DataTable
                            columns: [
                                {data: 'event_id'},
                                {data: 'event_name'},
                                {
                                    data: null,
                                    render: function (row) {
                                        return '<button class="btn btn-link" data-bs-toggle="modal" data-bs-target="#detailsModal' + row.event_id + '">View Details</button>';
                                    }
                                },
                                {
                                    data: null,
                                    render: function (row) {
                                        return '<button class="btn btn-link" data-bs-toggle="modal" data-bs-target="#imageModal' + row.event_id + '">View Image</button>';
                                    }
                                },
                                {data: 'date_start'},
                                {data: 'date_end'},
                                {
                                    data: null,
                                    render: function (row) {
                                        return '<a target="_blank" href="/EventController/Edit/' + row.event_id + '" class="btn btn-primary me-2">Edit</a>' +
                                                (row.event_status ? '<button class="btn btn-danger">Disable</button>' : '<button class="btn btn-success">Active</button>');
                                    }
                                }
                            ]
                        });

                        // Create modals for image and details
                        $.each(events, function (index, event) {
                            // Create modal for images
                            var imageModal = '<div class="modal fade" id="imageModal' + event.event_id + '" tabindex="-1" aria-labelledby="imageModalLabel' + event.event_id + '" aria-hidden="true">' +
                                    '<div class="modal-dialog modal-lg">' +
                                    '<div class="modal-content">' +
                                    '<div class="modal-header">' +
                                    '<h5 class="modal-title" id="imageModalLabel' + event.event_id + '">' + event.event_name + '</h5>' +
                                    '<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>' +
                                    '</div>' +
                                    '<div class="modal-body">' +
                                    '<img src="/ImageController/b/' + event.event_id + '" alt="' + event.event_name + '" class="img-fluid">' +
                                    '</div>' +
                                    '</div>' +
                                    '</div>' +
                                    '</div>';

                            // Create modal for details
                            var detailsModal = '<div class="modal fade" id="detailsModal' + event.event_id + '" tabindex="-1" aria-labelledby="detailsModalLabel' + event.event_id + '" aria-hidden="true">' +
                                    '<div class="modal-dialog modal-lg">' +
                                    '<div class="modal-content">' +
                                    '<div class="modal-header">' +
                                    '<h5 class="modal-title" id="detailsModalLabel' + event.event_id + '">' + event.event_name + '</h5>' +
                                    '<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>' +
                                    '</div>' +
                                    '<div class="modal-body">' +
                                    event.event_details +
                                    '</div>' +
                                    '</div>' +
                                    '</div>' +
                                    '</div>';

                            // Append modals to the body
                            $('body').append(imageModal);
                            $('body').append(detailsModal);
                        });
                    },
                    error: function (xhr, status, error) {
                        console.error("Có lỗi xảy ra: " + error);
                    }
                });

                console.log("Trang web đã được tải!");
            });

//            $(document).ready(function () {
//                var table = new DataTable('#eventsTable', {
//                    responsive: true
//                });
//            });
        </script>

        <!-- Table Structure -->
        <table id="eventsTable" class="table table-striped nowrap w-100" style="width: 100%;">
            <thead>
                <tr>
                    <th>Event ID</th>
                    <th>Event Name</th>
                    <th>Event Details</th>
                    <th>Image</th>
                    <th>Date Start</th>
                    <th>Date End</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody id="eventBody">
                <!-- Dữ liệu sẽ được chèn vào đây -->
                <tr>
                    <td>First name</td>
                    <td>Last name</td>
                    <td>Position</td>
                    <td>Office</td>
                    <td>Age</td>
                    <td>Start date</td>
                    <td>Salary</td>
                </tr>

            </tbody>
        </table>
    </body>
</html>
</tbody>
</table>
</body>
</html>