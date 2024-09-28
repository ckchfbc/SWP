<%-- 
    Document   : homepage
    Created on : 25 thg 9, 2024, 13:30:47
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
        <title>JSP Page</title>
    </head>
    <body>
        <header class="container-fluid p-0">            
            <!-- Phan cua logo cac thu khac -->
            <nav class="navbar navbar-expand-lg bg-dark">
                <div class="container-fluid">
                    <a class="navbar-brand" href="#">
                        <img src="" alt="Logo">
                    </a>
                    <button class="navbar-toggler text-white" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon align-items-center d-flex justify-content-center"><i class="fa-solid fa-bars"></i></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarNav">
                        <ul class="navbar-nav me-auto">
                            <li class="nav-item">
                                <a class="nav-link text-white" href="#">San Pham</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link text-white" href="#">Su Kien</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link text-white" href="#">Tra Cuu Bao Hanh</a>
                            </li>
                        </ul>
                        <div class="navbar-nav ms-auto">
                            <a class="nav-item nav-link text-white" href="#"><i class="fa-solid fa-user-plus"></i> Đăng ký</a>
                            <a class="nav-item nav-link text-white" href="#"><i class="fa-solid fa-user"></i> Đăng nhập</a>
                            <a class="nav-item nav-link text-white" href="#"><i class="fa-solid fa-magnifying-glass"></i></a>
                        </div>
                    </div>
                </div>
            </nav>
        </header> 
        <!-- Phan nay cua Slider -->
        <div id="carouselExampleDark" class="carousel carousel-dark slide">
            <div class="carousel-indicators">
                <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
                <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="1" aria-label="Slide 2"></button>
                <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="2" aria-label="Slide 3"></button>
            </div>
            <div class="carousel-inner ">
                <div class="carousel-item active overflow-hidden" data-bs-interval="10000">
                    <img src="https://loremflickr.com/1000/300?random=1" class="d-block w-100 object-fit-cover" alt="...">
                    <div class="carousel-caption d-none d-md-block">
                        <h5>First slide label</h5>
                        <p>Some representative placeholder content for the first slide.</p>
                    </div>
                </div>
                <div class="carousel-item overflow-hidden" data-bs-interval="2000">
                    <img src="https://loremflickr.com/1000/300?random=2" class="d-block w-100 object-fit-cover" alt="...">
                    <div class="carousel-caption d-none d-md-block">
                        <h5>Second slide label</h5>
                        <p>Some representative placeholder content for the second slide.</p>
                    </div>
                </div>
                <div class="carousel-item overflow-hidden">
                    <img src="https://loremflickr.com/1000/300?random=3" class="d-block w-100 object-fit-cover" alt="...">
                    <div class="carousel-caption d-none d-md-block">
                        <h5>Third slide label</h5>
                        <p>Some representative placeholder content for the third slide.</p>
                    </div>
                </div>
            </div>
            <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Previous</span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Next</span>
            </button>
        </div>
        <!-- phan nay cua xe noi bat -->
        <div class="border border-4">
            <h1>Xe Ban Chay</h1>
            <div>
                
            </div>
        </div>
        <!-- Phan nay la cua su kien -->
        <div class="event container-fluid m-0 p-0">
            <div class=" bg-dark row">
                <div class="col-md-6">
                    <img class="w-100 h-75" src="https://loremflickr.com/300/300?random=4" alt="anh cua su kien">
                </div>
                <div class="col-md-5 text-white h-50 p-5 l">
                    <h1>TITLE</h1>
                    <h4>Noi dung kieu chung toi voi tam long chanh thanh nhat thi luon dem den cac ban nhung su kien voi nhung uu dai siu dep siu ngau siu ronadol</h4>
                    <button class="btn btn-outline-primary"><a href="" class="text-decoration-none">Kham pha <i class="fa-solid fa-arrow-right"></i></a></button>
                </div>
            </div>
        </div>
    </body>
</html>
