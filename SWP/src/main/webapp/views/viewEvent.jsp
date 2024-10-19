<%-- 
    Document   : viewEvent
    Created on : 18 thg 10, 2024, 01:37:29
    Author     : thaii
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Event/title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <style>
            .event-card {
                margin: 20px 0;
                transition: transform 0.3s;
            }

            .event-card:hover {
                transform: scale(1.05);
                box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
            }
        </style>      
    </head>
    <body>

        <div class="container mt-5">
            <h2 class="mb-4">Upcoming Events</h2>
            <div class="row">
                <div class="col-md-4">
                    <div class="card event-card">
                        <img src="https://placehold.co/400x200" class="card-img-top" alt="Event Image">
                        <div class="card-body">
                            <h5 class="card-title">Event Title 1</h5>
                            <p class="card-text">Description of the event goes here. It provides an overview of what the event is about.</p>
                            <p><strong>Date:</strong> 2024-10-21</p>
                            <p><strong>Time:</strong> 14:00 - 17:00</p>
                            <a href="#" class="btn btn-primary">View Details</a>
                            <a href="#" class="btn btn-success">Join Event</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card event-card">
                        <img src="https://placehold.co/400x200" class="card-img-top" alt="Event Image">
                        <div class="card-body">
                            <h5 class="card-title">Event Title 2</h5>
                            <p class="card-text">Description of the event goes here. It provides an overview of what the event is about.</p>
                            <p><strong>Date:</strong> 2024-10-25</p>
                            <p><strong>Time:</strong> 10:00 - 12:00</p>
                            <a href="#" class="btn btn-primary">View Details</a>
                            <a href="#" class="btn btn-success">Join Event</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card event-card">
                        <img src="https://placehold.co/400x200" class="card-img-top" alt="Event Image">
                        <div class="card-body">
                            <h5 class="card-title">Event Title 3</h5>
                            <p class="card-text">Description of the event goes here. It provides an overview of what the event is about.</p>
                            <p><strong>Date:</strong> 2024-11-01</p>
                            <p><strong>Time:</strong> 18:00 - 20:00</p>
                            <a href="#" class="btn btn-primary">View Details</a>
                            <a href="#" class="btn btn-success">Join Event</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    </body>
</html>
