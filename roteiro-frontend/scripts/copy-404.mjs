// O GitHub Pages não tem fallback de SPA: ao abrir/atualizar uma rota como
// /IdleLog/roteiros ele procura roteiros/index.html e devolve 404.
// Copiar o index.html para 404.html faz o React Router assumir a URL.
import { copyFileSync } from 'node:fs';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const raiz = resolve(dirname(fileURLToPath(import.meta.url)), '..');
const dist = resolve(raiz, 'dist');

copyFileSync(resolve(dist, 'index.html'), resolve(dist, '404.html'));
console.log('404.html criado a partir do index.html (fallback de rotas).');
