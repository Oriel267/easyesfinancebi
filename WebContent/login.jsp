<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="WEB-INF/views/include/header.jsp">
	<jsp:param name="title" value="EasyEs Soft Accès" />
</jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div class="container-fluid">
		<div id="page-login" class="row">
			<div
				class="col-xs-12 col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3">
				<div class="text-right">
					<a href="#page_register.html" class="txt-default">Demander un
						compte</a> 
				</div>
				<div class="box">
					<div class="box-content">
						<div class="text-center">
							<h3 class="page-header">EasyesSoft Accès</h3>
							<span class="version">Version : 1.0</span>
						</div>
						<form id="f" class="login-form" action="j_spring_security_check"
							method="post">
							<div class="form-group">
								<label for="j_username" class="control-label">Identifiant
									:</label> <input type="text" class="form-control" name="j_username"
									value="" />
							</div>
							<div class="form-group">
								<label for="j_password" class="control-label">Mot de
									passe :</label> <input type="password" class="form-control" size="15"
									name="j_password" />
							</div>
							<div id="connexion">
								<button type="submit" class="bouton" name="" value="">
									<span class="btn-action-secondaire-xl"><span><span>Connexion</span></span></span>
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>