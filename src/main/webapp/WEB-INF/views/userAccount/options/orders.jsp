<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>


	<center>
		<h2>Orders history</h2>
			<c:forEach items="${orders}" var="order">
			<table border="1" width="40%">
			
				<tr><td>price </td><td> <c:out value="${order.price}" /><br></td></tr>
				
				<tr><td>paymentMethod </td><td><c:out value="${order.paymentMethod}" /></td></tr>
			
				<tr><td>shipping Address </td><td><c:out value="${order.shippingAddress}" /></td></tr>
			
				<tr><td>billing Address</td><td><c:out value="${order.billingAddress}" /></td></tr>
			
				<tr><td> couponCodes</td><td><c:out value="${order.couponCodes.code}" /></td></tr>
			
				 <tr><td> realized</td><td><c:out value="${order.getIsRealized()}" /></td></tr> 
				
				<c:forEach items="${order.books}" var="book">
					<tr><td>Book </td> <td><c:out value="${book.name}" /><br></td></tr>
				</c:forEach>
			</table>
			
			<br>
			</c:forEach>
		</center>
</body>
</html>