<%--
This file is part of JuniperBotJ.

JuniperBotJ is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

JuniperBotJ is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with JuniperBotJ. If not, see <http://www.gnu.org/licenses/>.
--%>
<!DOCTYPE HTML>
<%@include file="/WEB-INF/template/include.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<tilesx:useAttribute id="sidebarVisible"    name="sidebarVisible"    classname="java.lang.String" ignore="true" scope="request" />
<tilesx:useAttribute id="breadcrumbVisible" name="breadcrumbVisible" classname="java.lang.String" ignore="true" scope="request" />
<tilesx:useAttribute id="userDetails"       name="userDetails"       classname="ru.caramel.juniperbot.security.model.DiscordUserDetails" ignore="true" scope="request" />
<spring:eval expression="@environment.getProperty('build.timestamp')" var="buildTimestamp" scope="request" />

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>
        <c:if test="${not empty serverName}"><c:out value="${serverName}"/> — </c:if>
        <tiles:insertAttribute name="title" ignore="true"/>
    </title>

    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <meta name="theme-color" content="#f39c12" />

    <link rel="stylesheet" media="screen" href="//fonts.googleapis.com/css?family=Open+Sans:300,400,700">
    <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap-dialog.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap-toggle.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/font-awesome.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/select2.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/ionicons.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/AdminLTE.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/skin-yellow-light.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/main.css?v=${buildTimestamp}"/>">

    <script type="text/javascript">
        <c:set var="req" value="${pageContext.request}" />
        <c:set var="url">${req.requestURL}</c:set>
        <c:set var="uri" value="${req.requestURI}" />
        var contextPath = '${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${req.contextPath}/';
        var serverId = '${serverId}';
    </script>

    <script type="text/javascript" src="<c:url value="/resources/js/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/bootstrap-dialog.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/bootstrap-toggle.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/select2.full.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/utils.js?v=${buildTimestamp}"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/script.js?v=${buildTimestamp}"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/dynamic-list.js?v=${buildTimestamp}"/>"></script>

    <link rel="icon" type="image/png" href="<c:url value="/resources/img/favicon-32x32.png"/>" sizes="32x32" />
    <link rel="icon" type="image/png" href="<c:url value="/resources/img/favicon-16x16.png"/>" sizes="16x16" />

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
</head>
<body class="hold-transition sidebar-mini skin-yellow-light <tiles:insertAttribute name="body-class" ignore="true" /> <c:if test="${not sidebarVisible}">no-sidebar</c:if>">
    <c:if test="${sidebarVisible}">
        <script type="text/javascript">
            (function () {
                if (Boolean(getStored('sidebar-toggle-collapsed'))) {
                    var body = document.getElementsByTagName('body')[0];
                    body.className = body.className + ' sidebar-collapse';
                }
            })();
        </script>
    </c:if>
    <div class="wrapper">
        <jsp:include page="header.jsp" />
        <c:if test="${sidebarVisible}">
            <jsp:include page="navigation/sidebar.jsp" />
        </c:if>
        <div class="content-wrapper ">
            <c:if test="${breadcrumbVisible}">
                <jsp:include page="navigation/breadcrumb.jsp" />
            </c:if>
            <section class="content">
                <jsp:include page="flash-messages.jsp" />
                <tiles:insertAttribute name="content" />
            </section>
        </div>
        <jsp:include page="footer.jsp" />
    </div>
    <script type="text/javascript" src="<c:url value="/resources/js/adminlte.min.js"/>"></script>
</body>
</html>