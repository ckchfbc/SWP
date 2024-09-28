<%-- 
    Document   : homepage2
    Created on : 27 thg 9, 2024, 16:14:14
    Author     : thaii
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/a611f8fd5b.js" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/a611f8fd5b.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" type="text/css" href="css/font.css"/>
        <title>JSP Page</title>
        <style>
            .card {
                transition: transform 0.3s ease, box-shadow 0.3s ease;
            }

            .card:hover {
                transform: translateY(-10px);
                box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
            }

            .card-img-top {
                height: 200px;
                width: 150px;
                object-fit: cover;
            }

            a{
                text-decoration: none;
            }

            #searchForm {
                display: none;
            }
        </style>
    </head>
    <body>
        <!-- Navigation -->
        <nav class="navbar navbar-expand-md navbar-light bg-white position-fixed w-100 p-0 p-1 top-0 start-0">
            <div class="container d-flex p-0">
                <a class="navbar-brand" href="#"><h1>DriveAura</h1></a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav ms-auto align-items-center-md">
                        <li class="nav-item">
                            <a class="nav-link" href="#">Product</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Event</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Brand</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Zalo</a>
                        </li>                        
                        <li class="nav-item">
                            <a class="nav-link" href="#">Login</a>
                        </li>
                        <li class="nav-item align-items-center">
                            <a class="nav-link" href="#" id="searchButton"><i class="fa-solid fa-magnifying-glass"></i></a>
                            <script>
                                // Khi nhấp vào nút tìm kiếm, hiển thị hộp tìm kiếm
                                document.getElementById('searchButton').addEventListener('click', function () {
                                    const searchForm = document.getElementById('searchForm');
                                    // Chỉ hiển thị nếu nó đang ẩn
                                    if (searchForm.style.display === 'none' || searchForm.style.display === '') {
                                        searchForm.style.display = 'block';
                                    } else {
                                        searchForm.style.display = 'none';
                                    }
                                });
                            </script>
                        </li>
                    </ul>
                </div>
            </div>            
        </nav>
        <!-- tìm kiếm -->
        <section class="d-flex justify-content-end mt-5">
            <form id="searchForm" class="p-3 w-50">
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="Nhập từ khóa tìm kiếm..." aria-label="Search">
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="submit">Tìm</button>
                    </div>
                </div>
            </form>
        </section>
        <!-- Hero Section -->
        <section class="d-flex justify-content-center align-items-center text-center bg-dark text-white" style="height: 80vh; background-image: url('path/to/your/hero-image.jpg'); background-size: cover; background-position: center;">
            <div class="container">
                <h1 class="display-4">Find Your Perfect Vehicle Online</h1>
                <p class="lead">The World's Largest Used Car Dealership</p>
            </div>
        </section>

        <!-- Best Seller Cars -->
        <section class="container my-5">
            <h2 class="text-center mb-4">The Best Seller Cars</h2>

            <!-- Nav Tabs -->
            <ul class="nav nav-tabs justify-content-center" id="myTab" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="home-tab" data-bs-toggle="tab" data-bs-target="#home" type="button" role="tab" aria-controls="home" aria-selected="true">Sedan</button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="profile-tab" data-bs-toggle="tab" data-bs-target="#profile" type="button" role="tab" aria-controls="profile" aria-selected="false">SUV</button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="contact-tab" data-bs-toggle="tab" data-bs-target="#contact" type="button" role="tab" aria-controls="contact" aria-selected="false">Convertible</button>
                </li>
            </ul>

            <!-- Tab Content -->
            <div class="tab-content" id="myTabContent">
                <!-- Sedan Tab -->
                <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                    <div class="row mt-4">
                        <!-- Car Card 1 -->
                        <div class="col-md-3 mb-4">
                            <div class="card">
                                <img src="./images/VF9.jpg" class="card-img-top" alt="Car Image">
                                <div class="card-body">
                                    <h5 class="card-title">Toyota Camry New</h5>
                                    <p class="card-text">$40,000</p>
                                    <a href="#" class="btn btn-primary">View Details</a>
                                    <a href="#" class="btn btn-primary">Buy</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- SUV Tab -->
                <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                    <div class="row mt-4">
                        <!-- Car Card 2 -->
                        <div class="col-md-3 mb-4">
                            <div class="card">
                                <img src="./images/VF9.jpg" class="card-img-top" alt="Car Image">
                                <div class="card-body">
                                    <h5 class="card-title">Ranger Black – 2021</h5>
                                    <p class="card-text">$165,000</p>
                                    <a href="#" class="btn btn-primary">View Details</a>
                                    <a href="#" class="btn btn-primary">Buy</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Convertible Tab -->
                <div class="tab-pane fade" id="contact" role="tabpanel" aria-labelledby="contact-tab">
                    <div class="row mt-4">
                        <!-- Car Card 3 -->
                        <div class="col-md-3 mb-4">
                            <div class="card">
                                <img src="./images/VF9.jpg" class="card-img-top" alt="Car Image">
                                <div class="card-body">
                                    <h5 class="card-title">Ranger White – 2022</h5>
                                    <p class="card-text">$25,000</p>
                                    <a href="#" class="btn btn-primary">View Details</a>
                                    <a href="#" class="btn btn-primary">Buy</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Why Choose Us -->
        <section class="bg-dark text-white py-5">
            <div class="container">
                <h2 class="text-center mb-5">Why Choose Us?</h2>
                <div class="row text-center">
                    <div class="col-md-3">
                        <div class="mb-2"><i class="fa-solid fa-dollar-sign text-white" style="font-size: 2rem;"></i></div>
                        <h5>Special Financing Offers</h5>
                        <p>Our stress-free finance department can help you save money.</p>
                    </div>
                    <div class="col-md-3">
                        <div class="mb-2"><i class="fa-solid fa-shield text-white" style="font-size: 2rem;"></i></div>
                        <h5>Trusted Car Dealership</h5>
                        <p>We provide transparent and reliable services.</p>
                    </div>
                    <div class="col-md-3">
                        <div class="mb-2"><i class="fa-solid fa-tag text-white" style="font-size: 2rem;"></i></div>
                        <h5>Transparent Pricing</h5>
                        <p>No hidden fees or surprises.</p>
                    </div>
                    <div class="col-md-3">
                        <div class="mb-2"><i class="fa-solid fa-screwdriver-wrench text-white" style="font-size: 2rem;"></i></div>
                        <h5>Expert Car Service</h5>
                        <p>Our certified experts keep your car in top condition.</p>
                    </div>
                </div>
            </div>
        </section>
        <!-- New Car Section -->
        <section class="container my-5">
            <h2 class="text-center mb-4">The New Cars</h2>

            <div class="row">
                <!-- Car Card 1 -->
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <img src="./images/VF9.jpg" class="card-img-top" alt="Toyota Camry New">
                        <div class="card-body">
                            <h5 class="card-title">Toyota Camry New</h5>
                            <p class="card-text">$40,000</p>
                            <a href="#" class="btn btn-primary">View Details</a>
                            <a href="#" class="btn btn-primary">Buy</a>
                        </div>
                    </div>
                </div>
                <!-- Car Card 2 -->
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <img src="./images/VF9.jpg" class="card-img-top" alt="Ranger Black">
                        <div class="card-body">
                            <h5 class="card-title">Ranger Black – 2021</h5>
                            <p class="card-text">$165,000</p>
                            <a href="#" class="btn btn-primary">View Details</a>
                            <a href="#" class="btn btn-primary">Buy</a>
                        </div>
                    </div>
                </div>
                <!-- Car Card 3 -->
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <img src="./images/VF9.jpg" class="card-img-top" alt="Ranger White">
                        <div class="card-body">
                            <h5 class="card-title">Ranger White – 2022</h5>
                            <p class="card-text">$25,000</p>
                            <a href="#" class="btn btn-primary">View Details</a>
                            <a href="#" class="btn btn-primary">Buy</a>
                        </div>
                    </div>
                </div>
                <!-- Car Card 4 -->
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <img src="./images/VF9.jpg" class="card-img-top" alt="Ford Explorer 2023">
                        <div class="card-body">
                            <h5 class="card-title">Ford Explorer 2023</h5>
                            <p class="card-text">$35,000</p>
                            <a href="#" class="btn btn-primary">View Details</a>
                            <a href="#" class="btn btn-primary">Buy</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Footer -->
        <footer class="bg-dark text-white py-4">
            <div class="container">
                <div class="row">
                    <div class="col-md-3">
                        <h5>Company</h5>
                        <ul class="list-unstyled">
                            <li><a href="#" class="text-white">About Us</a></li>
                            <li><a href="#" class="text-white">Blog</a></li>
                            <li><a href="#" class="text-white">Services</a></li>
                        </ul>
                    </div>
                    <div class="col-md-3">
                        <h5>Quick Links</h5>
                        <ul class="list-unstyled">
                            <li><a href="#" class="text-white text-decoration-none">Get in Touch</a></li>
                            <li><a href="#" class="text-white">Help Center</a></li>
                            <li><a href="#" class="text-white">Live Chat</a></li>
                        </ul>
                    </div>
                    <div class="col-md-3">
                        <h5>Our Brands</h5>
                        <ul class="list-unstyled">
                            <li><a href="#" class="text-white">Toyota</a></li>
                            <li><a href="#" class="text-white">Porsche</a></li>
                            <li><a href="#" class="text-white">BMW</a></li>
                        </ul>
                    </div>
                    <div class="col-md-3">
                        <h5>Vehicle Types</h5>
                        <ul class="list-unstyled">
                            <li><a href="#" class="text-white">Sedan</a></li>
                            <li><a href="#" class="text-white">SUV</a></li>
                            <li><a href="#" class="text-white">Convertible</a></li>
                        </ul>
                    </div>
                </div>
                <hr class="bg-light">
                <p class="text-center">© 2024 example.com. All rights reserved.</p>
            </div>
        </footer>
    </body>
</html>
