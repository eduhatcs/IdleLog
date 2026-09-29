export const brl = (v) =>
  new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(Number(v ?? 0));

export const num = (v, d = 1) =>
  new Intl.NumberFormat('pt-BR', { maximumFractionDigits: d }).format(Number(v ?? 0));

export function fmtMin(total) {
  const m = Math.round(Number(total ?? 0));
  const h = Math.floor(m / 60);
  const r = m % 60;
  if (h === 0) return `${r} min`;
  return `${h} h ${String(r).padStart(2, '0')} min`;
}

// Datas vêm como "2026-09-28"; formatamos sem passar por Date (evita fuso).
export const fmtData = (iso) => {
  if (!iso) return '';
  const [y, m, d] = iso.slice(0, 10).split('-');
  return `${d}/${m}/${y}`;
};

export const fmtDataHora = (iso) => {
  if (!iso) return '—';
  const [data, hora] = iso.split('T');
  return `${fmtData(data)} ${hora.slice(0, 5)}`;
};

export const comSegundos = (dt) => (dt && dt.length === 16 ? dt + ':00' : dt);

export function baixarArquivo(conteudo, nome, tipo = 'text/csv;charset=utf-8') {
  const blob = conteudo instanceof Blob ? conteudo : new Blob([conteudo], { type: tipo });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = nome;
  a.click();
  URL.revokeObjectURL(url);
}
