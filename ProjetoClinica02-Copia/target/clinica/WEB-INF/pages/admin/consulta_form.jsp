<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>
        <c:choose>
            <c:when test="${not empty consulta.id}">Editar Consulta</c:when>
            <c:otherwise>Nova Consulta</c:otherwise>
        </c:choose>
    </title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background: #f8f9fa;
            font-family: "Segoe UI", sans-serif;
        }

        .container {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 40px 16px;
            min-height: 100vh;
        }

        .card {
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 600px;
            overflow: hidden;
        }

        .card-header {
            background-color: #4fa7d8;
            padding: 20px;
            color: #fff;
            text-align: center;
        }

        .card-body {
            padding: 24px;
        }

        .form-label {
            font-weight: 600;
            color: #333;
        }

        .form-control,
        .form-select {
            width: 100%;
            padding: 10px 14px;
            font-size: 1rem;
            border: 1px solid #ccc;
            border-radius: 8px;
            margin-bottom: 16px;
            box-sizing: border-box;
        }

        .form-control:focus,
        .form-select:focus {
            outline: none;
            border-color: #4fa7d8;
            box-shadow: 0 0 0 2px rgba(79, 167, 216, 0.2);
        }

        .btn-primary {
            background-color: #4fa7d8;
            border: none;
            color: #fff;
            padding: 10px 20px;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #3d91c2;
        }

        .btn-secondary {
            margin-left: 10px;
            padding: 10px 20px;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: bold;
            background-color: #dee2e6;
            color: #333;
            border: none;
            cursor: pointer;
        }

        .alert {
            background-color: #f8d7da;
            color: #842029;
            padding: 12px;
            border-radius: 6px;
            margin: 20px;
            position: relative;
        }

        .btn-close {
            position: absolute;
            right: 12px;
            top: 10px;
            background: none;
            border: none;
            font-size: 1.2rem;
            color: #842029;
            cursor: pointer;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="card">
        <div class="card-header">
            <h4>
                <c:choose>
                    <c:when test="${not empty consulta.id}">Editar Consulta</c:when>
                    <c:otherwise>Nova Consulta</c:otherwise>
                </c:choose>
            </h4>
        </div>
        <div class="card-body">
            <c:if test="${not empty error}">
                <div class="alert">
                    <strong>Erro:</strong> ${error}
                    <button type="button" class="btn-close" onclick="this.parentElement.style.display='none';">&times;</button>
                </div>
            </c:if>

            <form action="<c:url value='/admin/consulta'/>" method="post">
                <c:choose>
                    <c:when test="${not empty consulta.id}">
                        <input type="hidden" name="action" value="update"/>
                        <input type="hidden" name="id" value="${consulta.id}"/>
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="action" value="create"/>
                    </c:otherwise>
                </c:choose>

                <label class="form-label">Status</label>
                <input type="text" class="form-control" name="status"
                       value="${consulta.status}" required/>

                <label class="form-label">Observação</label>
                <textarea class="form-control" name="observacao" rows="3">${consulta.pbservacao}</textarea>

                <label class="form-label">Tipo de Consulta</label>
                <input type="text" class="form-control" name="tipoConsulta"
                       value="${consulta.tipoConsulta}" required/>

                <label class="form-label">Data e Hora</label>
                <input type="datetime-local" class="form-control" name="dataHorario"
                       value="${dataHorarioValue}" required/>

                <label class="form-label">Médico</label>
                <select class="form-select" name="medicoId" required>
                    <option value="" disabled ${empty consulta ? 'selected' : ''}>-- Selecione --</option>
                    <c:forEach var="m" items="${medicos}">
                        <option value="${m.id}"
                            ${consulta.medico != null && consulta.medico.id == m.id ? 'selected' : ''}>
                                ${m.nome} (CRM: ${m.crm})
                        </option>
                    </c:forEach>
                </select>

                <label class="form-label">Paciente</label>
                <select class="form-select" name="pacienteId" required>
                    <option value="" disabled ${empty consulta ? 'selected' : ''}>-- Selecione --</option>
                    <c:forEach var="p" items="${pacientes}">
                        <option value="${p.id}"
                            ${consulta.paciente != null && consulta.paciente.id == p.id ? 'selected' : ''}>
                                ${p.nome} (CPF: ${p.cpf})
                        </option>
                    </c:forEach>
                </select>

                <div>
                    <button type="submit" class="btn-primary">
                        <c:choose>
                            <c:when test="${not empty consulta.id}">Atualizar</c:when>
                            <c:otherwise>Salvar</c:otherwise>
                        </c:choose>
                    </button>
                    <button type="button" class="btn-secondary"
                            onclick="window.location.href='<c:url value='/admin/consulta'/>'">
                        Cancelar
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
