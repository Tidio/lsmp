<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ include file="/WEB-INF/layout/header.jsp" %>
<div class="row">
    <div class="col-lg-12">
        <div class="page-header">
            <h1 id="forms">Création d'infrastructure :</h1>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-6">
        <div class="well bs-component">
            <form:form class="form-horizontal" action="${pageContext.request.contextPath}/infrastructure/add" modelAttribute="newInfrastructure" method="POST">
                <fieldset>
                    NIQUE 
                </fieldset>
            </form:form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/layout/footer.jsp" %>