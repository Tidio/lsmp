<%@ include file="/WEB-INF/layout/header.jsp" %>

<div class="row">
    <div class="col-lg-12">
        <div class="page-header">
            <h2>Bonjour ${sessionScope.utilisateur.prenomUtilisateur} ${sessionScope.utilisateur.nomUtilisateur}</h2>
        </div>
    </div>
</div>

<!-- Headings -->

<div class="row">
    <div class="col-lg-12">
        <div class="bs-component">
            <p class="lead">Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor.</p>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/layout/footer.jsp" %>
