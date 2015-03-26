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
                                        <th>Column heading</th>
                                        <th>Column heading</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${utilisateur}" var="utilisateur">
                                        <tr>
                                            <td><c:out value="${utilisateur.idUtilisateur}"/></td>
                                            <td><c:out value="${utilisateur.nomUtilisateur}"/></td>
                                            <td><c:out value="${utilisateur.prenomUtilisateur}"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table> 
                        </div><!-- /example -->
                    </div>
                </div>
            </div>
<%@ include file="/WEB-INF/layout/footer.jsp" %>
