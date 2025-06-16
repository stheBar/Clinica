<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <title>Meu Prontuário</title>
  <style>
    body { margin: 0; padding: 0; background: #f8f9fa; font-family: "Segoe UI", sans-serif; }
    header { background-color: #4fa7d8; padding: 16px 24px; color: #fff; display: flex; justify-content: space-between; align-items: center; }
    header h1 { margin: 0; font-size: 20px; }
    nav a { color: white; text-decoration: none; font-weight: bold; margin-left: 8px; }
    .container { display: flex; justify-content: center; padding: 40px 16px; }
    .card { background-color: #fff; border-radius: 12px; box-shadow: 0 8px 20px rgba(0,0,0,0.1); width: 100%; max-width: 1000px; padding: 24px; }
    .table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    .table th, .table td { padding: 12px; border-bottom: 1px solid #eee; }
    .table th { background-color: #f0f0f0; }
    .alert { background-color: #fff3cd; color: #856404; padding: 12px; border-radius: 6px; border: 1px solid #ffeeba; }
  </style>
</head>
<body>

<header>
  <h1>Clínica</h1>
  <nav>
    <a href="${pageContext.request.contextPath}/paciente">Voltar</a>
  </nav>
</header>

<div class="container">
  <div class="card">
    <h2>Meu Prontuário</h2>
    <p><strong>Nome:</strong> ${sessionScope.usuarioLogado.nome}</p>
    <p><strong>CPF:</strong> ${sessionScope.usuarioLogado.cpf}</p>
    <p><strong>Histórico:</strong> ${sessionScope.usuarioLogado.historico}</p>

    <c:if test="${empty consultas}">
      <div class="alert">Nenhuma consulta registrada.</div>
    </c:if>

    <h2>Minhas Consultas</h2>

    <c:if test="${not empty consultas}">
      <table class="table">
        <thead>
        <tr>
          <th>Data</th>
          <th>Médico</th>
          <th>Tipo</th>
          <th>Status</th>
          <th>Observação</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="c" items="${consultas}">
          <tr>
            <td><c:out value="${c.dataHorario}" /></td>
            <td><c:out value="${c.medico.nome}" /></td>
            <td><c:out value="${c.tipoConsulta}" /></td>
            <td><c:out value="${c.status}" /></td>
            <td><c:out value="${c.pbservacao}" /></td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:if>
  </div>
</div>

</body>
</html>
