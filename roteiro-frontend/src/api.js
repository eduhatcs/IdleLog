// Camada única de acesso à API REST do Spring Boot.
const BASE = import.meta.env.VITE_API_URL ?? '';

async function request(path, options = {}) {
  const res = await fetch(BASE + path, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  if (!res.ok) {
    let msg = `Erro ${res.status}`;
    try {
      const body = await res.json();
      // ApiError do backend: { messages: [...] }
      if (body.messages?.length) msg = body.messages.join('; ');
    } catch { /* corpo vazio */ }
    throw new Error(msg);
  }
  if (res.status === 204) return null;
  return res.json();
}

const json = (method, body) => ({ method, body: JSON.stringify(body) });

export const api = {
  list: (rec) => request(`/api/${rec}`),
  get: (rec, id) => request(`/api/${rec}/${id}`),
  create: (rec, body) => request(`/api/${rec}`, json('POST', body)),
  update: (rec, id, body) => request(`/api/${rec}/${id}`, json('PUT', body)),
  remove: (rec, id) => request(`/api/${rec}/${id}`, { method: 'DELETE' }),

  atualizarStatusPedido: (id, status) => request(`/api/pedidos/${id}/status`, json('PATCH', { status })),

  adicionarPonto: (roteiroId, body) => request(`/api/roteiros/${roteiroId}/pontos`, json('POST', body)),
  registrarSaida: (roteiroId, pontoId, dataHoraSaida) =>
    request(`/api/roteiros/${roteiroId}/pontos/${pontoId}/saida`, json('PATCH', { dataHoraSaida })),

  parametrizacao: () => request('/api/parametrizacao'),
  salvarParametrizacao: (body) => request('/api/parametrizacao', json('PUT', body)),
  custoRoteiro: (id) => request(`/api/parametrizacao/roteiros/${id}/custo`),

  // Baixa o CSV pronto do backend (RelatorioController)
  async baixarCsvBackend() {
    const res = await fetch(BASE + '/api/relatorios/roteiros/csv');
    if (!res.ok) throw new Error('Falha ao gerar o relatório no servidor.');
    return res.blob();
  },
};

// Busca todos os roteiros e o custo calculado de cada um.
export async function carregarRoteirosComCusto() {
  const roteiros = await api.list('roteiros');
  const custos = await Promise.all(roteiros.map((r) => api.custoRoteiro(r.id)));
  return roteiros.map((r, i) => ({ ...r, custo: custos[i] }));
}
