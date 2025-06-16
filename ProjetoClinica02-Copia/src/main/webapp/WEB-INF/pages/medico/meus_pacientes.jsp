<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Meus Pacientes</title>
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

        nav a {
            color: white;
            text-decoration: none;
            font-weight: bold;
            margin-left: 8px;
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

        .table {
            width: 100%;
            border-collapse: collapse;
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

        .btn {
            padding: 6px 12px;
            border: none;
            border-radius: 6px;
            font-weight: bold;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
        }

        .btn-outline-primary {
            color: #0d6efd;
            border: 1px solid #0d6efd;
            background-color: transparent;
        }

        .btn-outline-primary:hover {
            background-color: #0d6efd;
            color: #fff;
        }

        .alert {
            background-color: #fff3cd;
            color: #856404;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 16px;
            border: 1px solid #ffeeba;
        }

        .table-empty {
            text-align: center;
            font-style: italic;
            color: #777;
        }
    </style>
</head>
<body>

<header>
    <h1>Clínica</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/medico">Voltar</a>
    </nav>
</header>

<div class="container">
    <div class="card">
        <div class="card-header">
            <h2>Meus Pacientes</h2>
        </div>
        <div class="card-body">
            <c:if test="${empty pacientes}">
                <div class="alert">
                    Nenhum paciente encontrado.
                </div>
            </c:if>

            <c:if test="${not empty pacientes}">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>CPF</th>
                        <th>Ações</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="p" items="${pacientes}">
                        <tr>
                            <td><c:out value="${p.nome}" /></td>
                            <td><c:out value="${p.cpf}" /></td>
                            <td>
                                <a class="btn btn-outline-primary"
                                   href="${pageContext.request.contextPath}/medico?action=prontuario&id=${p.id}">
                                    Acessar Prontuário
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
        <div class="card-footer">
            Painel do médico - Pacientes atendidos
        </div>
    </div>
</div>

</body>
</html>
