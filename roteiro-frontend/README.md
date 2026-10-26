# Roteiro Frontend

Interface web responsiva (RNF02) do MVP de monitoramento de tempo parado em roteiros.
React 18 + Vite + React Router + Recharts.

## Como rodar

1. Suba o backend (`docker compose up -d` e `mvn spring-boot:run` em `roteiro-backend`, porta 8080).
2. Nesta pasta:

```bash
npm install
npm run dev
```

Acesse http://localhost:5173. O Vite encaminha `/api` para `localhost:8080` (ver `vite.config.js`), então não precisa configurar CORS.

## Produção

`npm run build` gera `dist/` já com o base path `/IdleLog/`, adequado ao GitHub Pages do repositório.
Além do `index.html`, o script de build cria `dist/404.html` para o fallback das rotas do React Router.

O deploy é automático pelo workflow `.github/workflows/deploy-pages.yml` a cada push na `main`:
https://eduhatcs.github.io/IdleLog/

Para o Spring Boot servir o frontend na mesma origem, copie o conteúdo de `dist/` para
`roteiro-backend/src/main/resources/static/` (substitui o `index.html` antigo). Se a API estiver em outro
host, defina `VITE_API_URL` antes do build (no GitHub, como variável de Actions). Nesse caso o backend
precisa permitir CORS para `https://<usuario>.github.io`.

## Telas

| Tela | Requisito |
|---|---|
| Dashboard (dia/mês/período, motorista) | RF08 |
| Motoristas, Gerentes, Clientes, Pedidos | RF01, RF02, RF03 |
| Roteiros e detalhe (pontos, saída, custo) | RF04, RF05, RF06, RF09 |
| Parâmetros da frota | RF07, RF10 |
| Relatórios CSV | RF12 |
| Login por perfil (Gerente / Motorista) | RNF04 |
