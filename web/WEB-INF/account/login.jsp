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
            <form id="form" action="<c:url value='/login.do'/>" method="POST">
                <c:if test="${not empty param.err}">
                    <div class="alert alert-danger alert-dismissible">
                        <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
                    </div>
                </c:if>
                <c:if test="${not empty param.out}">
                    <div class="alert alert-danger alert-dismissible">
                        You've logged out successfully.
                    </div>
                </c:if>
                <c:if test="${not empty param.time}">
                    <div class="alert alert-danger alert-dismissible">
                        You've been logged out due to inactivity.
                    </div>
                </c:if>
                <fieldset>
                    <legend>Legend</legend>
                    <div class="form-group">
                        <label for="inputIdentifiant" class="col-lg-3 control-label">Identifiant</label>
                        <div class="col-lg-9">
                            <input type="text" name="username" value="" class="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputPassword" class="col-lg-3 control-label">Mot de passe</label>
                        <div class="col-lg-9">
                            <input type="password" name="password" value="" class="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-10 col-lg-offset-2">
                            <button type="reset" class="btn btn-default">Effacer</button>
                            <button type="submit" class="btn btn-primary">Connexion</button>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/layout/footer.jsp" %>