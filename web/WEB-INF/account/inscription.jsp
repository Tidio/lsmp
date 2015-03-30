<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ include file="/WEB-INF/layout/header.jsp" %>
<div class="row">
    <div class="col-lg-12">
        <div class="page-header">
            <h1 id="forms">Création de compte:</h1>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-6">
        <div class="well bs-component">
            <form:form class="form-horizontal" action="${pageContext.request.contextPath}/account/inscription" modelAttribute="newUtilisateur" method="POST">
                <fieldset>
                    <legend>Legend</legend>
                    <div class="form-group">
                        <label for="inputNom" class="col-lg-3 control-label">Nom</label>
                        <div class="col-lg-9">
                            <form:input path="nomUtilisateur" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputPrenom" class="col-lg-3 control-label">Pr&eacute;nom</label>
                        <div class="col-lg-9">
                            <form:input path="prenomUtilisateur" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputOrganisation" class="col-lg-3 control-label">Organisation</label>
                        <div class="col-lg-9">
                            <form:input path="organisationUtilisateur" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputIdentifiant" class="col-lg-3 control-label">Identifiant</label>
                        <div class="col-lg-9">
                            <form:input path="identifiantUtilisateur" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputEmail" class="col-lg-3 control-label">Email</label>
                        <div class="col-lg-9">
                            <form:input type="email" path="mailUtilisateur" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputPassword" class="col-lg-3 control-label">Mot de passe</label>
                        <div class="col-lg-9">
                            <form:password path="mdpUtilisateur" cssClass="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputPassword" class="col-lg-3 control-label">Repeter le mot de passe</label>
                        <div class="col-lg-9">
                            <input type="password" class="form-control" id="inputPassword" placeholder="Repeat Password">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">Type utilisateur</label>
                        <div class="col-lg-9">
                            <c:forEach items="${typeUtilisateurs}" var="typeUtilisateur">
                                <div class="radio">
                                    <label>
                                        <form:radiobutton path="typeUtilisateur.idTypeUtilisateur" value="${typeUtilisateur.idTypeUtilisateur}" />
                                        <c:out value="${typeUtilisateur.libelle}"/>
                                    </label>
                                </div>
                            </c:forEach>
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