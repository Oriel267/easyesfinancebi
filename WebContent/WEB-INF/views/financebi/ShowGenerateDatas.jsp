<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../include/header.jsp">
	<jsp:param value="Creation organisation" name="title" />
</jsp:include>
<head>
<link href="/easyessoft/dist/css/jquery.dataTables.css" rel="stylesheet">
<script type="text/javascript"
	src="/easyessoft/dist/js/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="/easyessoft/js/dataTableConfiguration.js"></script>
<script type="text/javascript"
	src="/easyessoft/dist/js/dataTables.bootstrap.js"></script>
</head>
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h1 class="boderline">Liste des données generées</h1>
			</div>
			<div class="panel-body">
				<div class="box-content">
					<table id="table_id" class="display">
						<thead>
							<tr>
								<td>Période d'analyse</td>
								<td>Nom de l'organisme</td>
								<td>Type d'organisme</td>
								<td>Service rattachés</td>
								<td>Type Acte</td>
								<td>Nom de l'acte</td>
								<td>Taux de remboursement moyen</td>
								<td>Nombre d'actes effectués</td>
								<td>CA actes</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="element" items="${listfact}">
								<tr>
									<td>${element.date.month}<%out.println(" / ");%>${element.date.year}</td>
									<td>${element.orga.name}</td>
									<td>${element.orgatype.label}</td>
									<td>${element.serviceType.serviceTypeDecription}</td>
									<td>${element.typeAct.label}</td>
									<td>${element.ccam.nameAct}</td>
									<td>${element.factActRefund}</td>
									<td>${element.nbAct}</td>
									<td>${element.caAct}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="../include/footer.jsp" /> 
