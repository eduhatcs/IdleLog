import { useMemo, useState } from 'react';
import { api, carregarRoteirosComCusto } from '../api.js';
import { useAsync } from '../hooks.js';
import { Alert, Empty, Field, Loading, PageHeader, Panel } from '../components/ui.jsx';
import { baixarArquivo, brl, fmtData, fmtMin } from '../utils.js';

const esc = (v) => `"${String(v ?? '').replace(/"/g, '""')}"`;

export default function Relatorios() {
  const { data, loading, error } = useAsync(carregarRoteirosComCusto);
  const motoristas = useAsync(() => api.list('motoristas'));
  const [f, setF] = useState({ de: '', ate: '', motoristaId: '' });
  const [msg, setMsg] = useState({ kind: 'error', text: '' });
  const set = (k, v) => setF((x) => ({ ...x, [k]: v }));

  const linhas = useMemo(() => (data ?? []).filter((r) =>
    (!f.de || r.data >= f.de) && (!f.ate || r.data <= f.ate) &&
    (!f.motoristaId || String(r.motoristaId) === f.motoristaId)), [data, f]);

  const invalido = f.de && f.ate && f.de > f.ate;

  // RF12 — exporta o que está filtrado na tela (E1: sem dados não gera arquivo)
  const exportarFiltrado = () => {
    const cab = ['ID', 'Data', 'Motorista', 'Distância (km)', 'Tempo parado (min)', 'Combustível (R$)', 'Parada (R$)', 'Total (R$)', 'Jornada excedida'];
    const corpo = linhas.map(({ id, data, motoristaNome, custo: c }) => [
      id, data, motoristaNome, c.distanciaTotalKm, c.tempoTotalParadoMinutos,
      c.custoCombustivel, c.custoTempoParado, c.custoTotalEstimado, c.jornadaExcedida ? 'SIM' : 'NAO',
    ]);
    const csv = '\uFEFF' + [cab, ...corpo].map((l) => l.map(esc).join(';')).join('\n');
    baixarArquivo(csv, 'relatorio_roteiros_filtrado.csv');
  };

  const exportarCompleto = async () => {
    try { baixarArquivo(await api.baixarCsvBackend(), 'relatorio_roteiros.csv'); }
    catch (err) { setMsg({ kind: 'error', text: err.message }); }
  };

  return (
    <>
      <PageHeader title="Relatórios" subtitle="Exporte os roteiros com tempos e custos em CSV (abre no Excel).">
        <button className="btn" onClick={exportarCompleto}>Exportar tudo (servidor)</button>
      </PageHeader>
      <Alert kind={msg.kind}>{msg.text}</Alert>

      <Panel title="Exportar com filtros">
        <div className="form-grid">
          <Field label="De"><input type="date" value={f.de} onChange={(e) => set('de', e.target.value)} /></Field>
          <Field label="Até"><input type="date" value={f.ate} onChange={(e) => set('ate', e.target.value)} /></Field>
          <Field label="Motorista">
            <select value={f.motoristaId} onChange={(e) => set('motoristaId', e.target.value)}>
              <option value="">Todos</option>
              {motoristas.data?.map((m) => <option key={m.id} value={m.id}>{m.nome}</option>)}
            </select>
          </Field>
          <div className="form-actions">
            <button className="btn primary" disabled={invalido || linhas.length === 0} onClick={exportarFiltrado}>
              Baixar CSV ({linhas.length})
            </button>
          </div>
        </div>
        {invalido && <Alert>A data inicial não pode ser depois da final.</Alert>}
      </Panel>

      <Panel title="Pré-visualização">
        {loading && <Loading />}
        <Alert>{error}</Alert>
        {data && linhas.length === 0 && <Empty>Nenhum roteiro encontrado para esses filtros.</Empty>}
        {linhas.length > 0 && (
          <div className="table-wrap">
            <table>
              <thead><tr><th>Data</th><th>Motorista</th><th className="r">Parado</th><th className="r">Combustível</th><th className="r">Parada</th><th className="r">Total</th><th>Jornada</th></tr></thead>
              <tbody>
                {linhas.map((r) => (
                  <tr key={r.id}>
                    <td>{fmtData(r.data)}</td><td>{r.motoristaNome}</td>
                    <td className="r">{fmtMin(r.custo.tempoTotalParadoMinutos)}</td>
                    <td className="r">{brl(r.custo.custoCombustivel)}</td>
                    <td className="r">{brl(r.custo.custoTempoParado)}</td>
                    <td className="r"><strong>{brl(r.custo.custoTotalEstimado)}</strong></td>
                    <td>{r.custo.jornadaExcedida ? <span className="tag warn">Excedida</span> : 'Dentro do limite'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </Panel>
    </>
  );
}
