<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>Management Page</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />">
    <script src="<c:url value="/resources/jquery/jquery.min.js" />"></script>
    <script src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
</head>

<body>
    <div class="container">
        <div class="page-header">
            <h1>System Management</h1> </div>
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header"> <a class="navbar-brand" href="#">Pages</a> </div>
                <ul class="nav navbar-nav">
                    <li><a href="#">Home</a></li>
                    <li><a href="#">Settings</a></li>
                    <li class="active"><a href="#">Sales</a></li>
                    <li><a href="#">Products</a></li>
                    <li><a href="#">Add Receipt</a></li>
                </ul>
            </div>
        </nav>
 <div class="container">
            <h2>Sales</h2>
            <p>All sales from newest to oldest:</p>

 			<nav aria-label="Page Navigation">
 			  <ul class="pagination">
 			    <li class="page-item">
 			      <a class="page-link" href="#" aria-label="Previous">
 			        <span aria-hidden="true">&laquo;</span>
 			        <span class="sr-only">Previous</span>
 			      </a>
 			    </li>
 			    <li class="page-item"><a class="page-link" href="#">1</a></li>
 			    <li class="page-item"><a class="page-link" href="#">2</a></li>
 			    <li class="page-item"><a class="page-link" href="#">3</a></li>
 			    <li class="page-item"><a class="page-link" href="#">4</a></li>
 			    <li class="page-item"><a class="page-link" href="#">5</a></li>
 			    <li class="page-item">
 			      <a class="page-link" href="#" aria-label="Next">
 			        <span aria-hidden="true">&raquo;</span>
 			        <span class="sr-only">Next</span>
 			      </a>
 			    </li>
 			  </ul>
 			</nav>

            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Date and Time</th>
                        <th>Code</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Total</th>
                        <th>Controls</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>2/10/2017 6:43 PM</td>
                        <td>12345</td>
                        <td>$25</td>
                        <td>3</td>
                        <td>$75</td>
                        <td>
                            <form action="#" method="post">
                                <input type="hidden" name="action" value="actionName">
                                <a class="btn btn-info" onclick="parentNode.submit();" role="button"> <span class="glyphicon glyphicon-pencil"></span> Edit</a>
                                <a class="btn btn-danger" onclick="parentNode.submit();" role="button"> <span class="glyphicon glyphicon-trash"></span> Delete</a>
                            </form>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</body>

</html>