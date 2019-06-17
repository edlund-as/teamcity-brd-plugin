<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="message" scope="request" type="java.lang.String"/>

<c:if test="${not empty message}">
${message}
</c:if>
