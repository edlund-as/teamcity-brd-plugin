<%@
    taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@
    taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%@
    taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@
    taglib prefix="bs" tagdir="/WEB-INF/tags"

%>
<jsp:useBean id="urllist" scope="request" type="java.util.ArrayList"/>

<c:forEach items="${urllist}" var="url">
<iframe src="/repository/download/${url}?redirectSupported=false" allowtransparency="true" onload="this.style.height=(this.contentDocument.body.scrollHeight+45) +'px';" scrolling="no" style="width:100%;border:none;"></iframe>
</c:forEach>
