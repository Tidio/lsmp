<%@ include file="/WEB-INF/layout/header.jsp" %>

            <div class="bs-docs-section">

                <div class="row">
                    <div class="col-lg-12">
                        <div class="page-header">
                            <h1 id="tables">${message}</h1>
                        </div>

                        <div class="bs-component">
                            <table class="table table-striped table-hover ">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Libellé</th>
                                        <th>Description</th>
                                        <th>Date de création</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${infrastructure}" var="infrastructure">
                                        <tr>
                                            <td><c:out value="${infrastructure.libelleInfrastructure}"/></td>
                                            <td><c:out value="${infrastructure.descriptionInfrastructure}"/></td>
                                            <td><c:out value="${infrastructure.dateCreationInfrastructure}"/></td>
                                            <td><button class="btn-default">Modifier</button>&nbsp;<button class="btn-danger">Supprimer</button></td>
                                            
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table> 
                        </div><!-- /example -->
                    </div>
                </div>
            </div>
<%@ include file="/WEB-INF/layout/footer.jsp" %>
