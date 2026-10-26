import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Em desenvolvimento, o Vite encaminha /api para o Spring Boot (porta 8080),
// evitando problemas de CORS sem precisar mexer no backend.
//
// No build de produção o site é publicado como projeto do GitHub Pages em
// https://<usuario>.github.io/IdleLog/, por isso o base path precisa ser
// /IdleLog/. Se o repositório for renomeado, ajuste o valor abaixo.
export default defineConfig(({ command }) => ({
  plugins: [react()],
  base: command === 'build' ? '/IdleLog/' : '/',
  server: {
    port: 5173,
    proxy: { '/api': 'http://localhost:8080' },
  },
}));
