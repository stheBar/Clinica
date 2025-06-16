<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Área do Paciente</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background: #f8f9fa;
            font-family: "Segoe UI", sans-serif;
        }

        header {
            background-color: #4fa7d8;
            padding: 16px 24px;
            color: #fff;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        header h1 {
            margin: 0;
            font-size: 20px;
        }

        .container {
            display: flex;
            justify-content: center;
            align-items: flex-start;
            padding: 40px 16px;
        }

        .card {
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 1000px;
            overflow: hidden;
        }

        .card-header {
            background-color: #4fa7d8;
            padding: 20px;
            color: #fff;
        }

        .card-body {
            padding: 24px;
        }

        .card-footer {
            padding: 16px;
            background-color: #f0f0f0;
            text-align: center;
            font-size: 0.9rem;
        }

        .alert {
            background-color: #fff3cd;
            color: #856404;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 16px;
            border: 1px solid #ffeeba;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
            border-radius: 8px;
            overflow: hidden;
            margin-top: 16px;
        }

        .table th, .table td {
            padding: 12px 16px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        .table th {
            background-color: #f0f0f0;
        }
    </style>
</head>
<body>

<header>
    <h1>Clínica</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/paciente?action=prontuario"
           style="color: white; text-decoration: none; font-weight: bold;">Meu Prontuário</a>
    </nav>
</header>

<div class="container">
    <div class="card">
        <div class="card-header">
            <h2>Minhas Consultas</h2>
        </div>
        <div class="card-body">
            <p>Olá <strong><c:out value="${sessionScope.usuarioLogado.nome}" /></strong>, aqui você pode acompanhar suas consultas agendadas.</p>

            <c:if test="${empty consultas}">
                <div class="alert">
                    Você ainda não possui consultas agendadas.
                </div>
            </c:if>

            <c:if test="${not empty consultas}">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Data e Hora</th>
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
        <div class="card-footer">
            Área do Paciente - Consultas
        </div>
    </div>
</div>

</body>
</html>
