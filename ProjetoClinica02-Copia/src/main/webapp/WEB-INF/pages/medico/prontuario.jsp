<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Prontuário do Paciente</title>
    <style>
        body { background: #f8f9fa; font-family: "Segoe UI", sans-serif; margin: 0; padding: 0; }
        header { background-color: #4fa7d8; padding: 16px 24px; color: #fff; display: flex; justify-content: space-between; align-items: center; }
        header h1 { margin: 0; font-size: 20px; }
        nav a { color: white; text-decoration: none; font-weight: bold; margin-left: 8px; }
        .container { display: flex; justify-content: center; padding: 40px 16px; }
        .card { background-color: #fff; border-radius: 12px; box-shadow: 0 8px 20px rgba(0,0,0,0.1); width: 100%; max-width: 1000px; padding: 24px; }
        .table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        .table th, .table td { padding: 12px; border-bottom: 1px solid #eee; }
        .table th { background-color: #f0f0f0; }
    </style>
</head>
<body>

<header>
    <h1>Clínica</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/medico?action=meusPacientes">Voltar</a>
    </nav>
</header>

<div class="container">
    <div class="card">
        <h2>Prontuário do Paciente </h2>
        <p><strong>Nome:</strong> ${paciente.nome}</p>
        <p><strong>CPF:</strong> ${paciente.cpf}</p>
        <p><strong>Histórico:</strong> ${paciente.historico}</p>

        <h3>Consultas:</h3>
        <c:if test="${empty consultas}">
            <p style="color: #777; font-style: italic;">Nenhuma consulta encontrada.</p>
        </c:if>
        <c:if test="${not empty consultas}">
            <table class="table">
                <thead>
                <tr>
                    <th>Data</th>
                    <th>Tipo</th>
                    <th>Status</th>
                    <th>Observação</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="c" items="${consultas}">
                    <tr>
                        <td><c:out value="${c.dataHorario}" /></td>
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
