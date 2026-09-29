import { useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../api.js';
import { useAuth } from '../auth.jsx';
import { useAsync } from '../hooks.js';
import { Alert, Empty, Field, Loading, PageHeader, Panel } from '../components/ui.jsx';
import { fmtData, fmtMin, num } from '../utils.js';

const hoje = () => new Date().toISOString().slice(0, 10);

export default function Roteiros() {
  const { sessao, ehGerente } = useAuth();
  const roteiros = useAsync(() => api.list('roteiros'));
  const motoristas = useAsync(() => (ehGerente ? api.list('motoristas') : Promise.resolve([])), [ehGerente]);
  const [form, setForm] = useState({ motoristaId: '', data: hoje(), distanciaTotalKm: '' });
  const [msg, setMsg] = useState({ kind: 'error', text: '' });
  const set = (k, v) => setForm((f) => ({ ...f, [k]: v }));

  // RNF04: motorista só enxerga os próprios roteiros
  const lista = (roteiros.data ?? [])
    .filter((r) => ehGerente || r.motoristaId === sessao.id)
    .sort((a, b) => b.data.localeCompare(a.data));

  const criar = async (e) => {
    e.preventDefault();
    try {
      await api.create('roteiros', {
        motoristaId: Number(form.motoristaId),
        data: form.data,
        distanciaTotalKm: form.distanciaTotalKm === '' ? null : Number(form.distanciaTotalKm),
      });
      setMsg({ kind: 'success', text: 'Roteiro criado. Abra-o para adicionar os pontos.' });
      setForm({ motoristaId: '', data: hoje(), distanciaTotalKm: '' });
      roteiros.reload();
    } catch (err) { setMsg({ kind: 'error', text: err.message }); }
  };

  return (
    <>
      <PageHeader title={ehGerente ? 'Roteiros' : 'Meus roteiros'}
        subtitle={ehGerente ? 'Um roteiro por motorista e por dia.' : 'Abra um roteiro para registrar chegadas e saídas.'} />
      <Alert kind={msg.kind}>{msg.text}</Alert>

      {ehGerente && (
        <Panel title="Novo roteiro">
          <form className="form-grid" onSubmit={criar}>
            <Field label="Motorista">
              <select required value={form.motoristaId} onChange={(e) => set('motoristaId', e.target.value)}>
                <option value="">Selecione</option>
                {motoristas.data?.map((m) => <option key={m.id} value={m.id}>{m.nome}</option>)}
              </select>
            </Field>
            <Field label="Data">
              <input type="date" required value={form.data} onChange={(e) => set('data', e.target.value)} />
            </Field>
            <Field label="Distância total (km)" hint="Usada no cálculo do combustível.">
              <input type="number" min="0" step="0.1" value={form.distanciaTotalKm}
                onChange={(e) => set('distanciaTotalKm', e.target.value)} />
            </Field>
            <div className="form-actions"><button className="btn primary">Criar roteiro</button></div>
          </form>
        </Panel>
      )}

      <Panel>
        {roteiros.loading && <Loading />}
        <Alert>{roteiros.error}</Alert>
        {!roteiros.loading && lista.length === 0 && (
          <Empty>{ehGerente ? 'Nenhum roteiro criado ainda.' : 'Você ainda não tem roteiros atribuídos.'}</Empty>
        )}
        {lista.length > 0 && (
          <div className="table-wrap">
            <table>
              <thead><tr><th>Data</th><th>Motorista</th><th className="r">Distância</th><th className="r">Pontos</th><th className="r">Tempo parado</th><th /></tr></thead>
              <tbody>
                {lista.map((r) => (
                  <tr key={r.id}>
                    <td>{fmtData(r.data)}</td>
                    <td>{r.motoristaNome}</td>
                    <td className="r">{r.distanciaTotalKm != null ? `${num(r.distanciaTotalKm)} km` : '—'}</td>
                    <td className="r">{r.pontos.length}</td>
                    <td className="r"><span className="stop">{fmtMin(r.tempoTotalParadoMinutos)}</span></td>
                    <td className="row-actions"><Link to={`/roteiros/${r.id}`}>Abrir</Link></td>
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
