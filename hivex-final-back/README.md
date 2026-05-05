# HiveX — Mini Corretora


## Como rodar localmente

### Backend
```powershell
# 1. Configure as variaveis (edite env.ps1 com suas credenciais)
# 2. Execute:
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\env.ps1
```

### Frontend
```powershell
npm install
npm start
# Abre em http://localhost:3000
```
---
## Variaveis de ambiente

| Variavel | Descricao |
|---|---|
| `DATABASE_URL` | URL JDBC do PostgreSQL |
| `DATABASE_USER` | Usuario do banco |
| `DATABASE_PASSWORD` | Senha do banco |
| `ALPHAVANTAGE_KEY` | Chave da API Alpha Vantage |
| `PORT` | Porta do servidor (Railway define automaticamente) |
| `REACT_APP_API_URL` | URL do backend (frontend) |
