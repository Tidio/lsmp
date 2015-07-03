<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ include file="/WEB-INF/layout/header.jsp" %>
<div class="row">
    <div class="col-lg-12">
        <div class="page-header">
            <h1 id="forms">Création d'une infrastructure :</h1>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-6">
        <div class="well bs-component">
            <form:form class="form-horizontal" action="${pageContext.request.contextPath}/infrastructure/create" modelAttribute="newInfrastructure" method="POST">
                <fieldset>
                    <legend>Legend</legend>
                    <div class="form-group">
                        <label for="inputLibelle" class="col-lg-3 control-label">Libellé</label>
                        <div class="col-lg-9">
                            <form:input path="libelleInfrastructure" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputDescription" class="col-lg-3 control-label">Description</label>
                        <div class="col-lg-9">
                            <form:input path="descriptionInfrastructure" cssClass="form-control"/>
                        </div>
                    </div>
                   
                    <div class="form-group">
                        <div class="col-lg-10 col-lg-offset-2">
                            <button type="reset" class="btn btn-default">Effacer</button>
                            <button type="submit" class="btn btn-primary">Enregistrer</button>
                        </div>
                    </div>
                </fieldset>
            </form:form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/layout/footer.jsp" %>