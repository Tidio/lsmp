<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ include file="/WEB-INF/layout/header.jsp" %>
<div class="row">
    <div class="col-lg-12">
        <div class="page-header">
            <h1 id="forms">Connexion:</h1>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-6">
        <div class="well bs-component">
            <form:form class="form-horizontal" action="${pageContext.request.contextPath}/account/inscription" method="POST">
                <fieldset>
                    <legend>Legend</legend>
                    <div class="form-group">
                        <label for="inputIdentifiant" class="col-lg-3 control-label">Identifiant</label>
                        <div class="col-lg-9">
                            <form:input path="identifiantUtilisateur" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputPassword" class="col-lg-3 control-label">Mot de passe</label>
                        <div class="col-lg-9">
                            <form:password path="mdpUtilisateur" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-10 col-lg-offset-2">
                            <button type="reset" class="btn btn-default">Effacer</button>
                            <button type="submit" class="btn btn-primary">Connexion</button>
                        </div>
                    </div>
                </fieldset>
            </form:form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/layout/footer.jsp" %>