import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { api } from '../api.js';
import { useAuth } from '../auth.jsx';
import { useAsync } from '../hooks.js';
import { Alert, Field, Kpi, Loading, PageHeader, Panel } from '../components/ui.jsx';
import { brl, comSegundos, fmtData, fmtDataHora, fmtMin, num } from '../utils.js';

export default function RoteiroDetalhe() {
  const { id } = useParams();
  const { sessao, ehGerente } = useAuth();
  const { data, loading, error, reload } = useAsync(async () => {
    const [roteiro, custo, pedidos] = await Promise.all([
      api.get('roteiros', id),
      api.custoRoteiro(id),
      ehGerente ? api.list('pedidos') : Promise.resolve([]),
    ]);
    return { roteiro, custo, pedidos };
  }, [id]);

  const [msg, setMsg] = useState({ kind: 'error', text: '' });
  const [ponto, setPonto] = useState({ endereco: '', dataHoraChegada: '', dataHoraSaida: '', pedidoId: '' });
  const [saidas, setSaidas] = useState({});

  if (loading && !data) return <Loading />;
  if (error) return <Alert>{error}</Alert>;
  const { roteiro, custo, pedidos } = data;

  // RNF04: motorista só acessa o próprio roteiro
  if (!ehGerente && roteiro.motoristaId !== sessao.id) {
    return <Alert>Este roteiro pertence a outro motorista.</Alert>;
  }

  const proximaOrdem = roteiro.pontos.length + 1;
  const setP = (k, v) => setPonto((p) => ({ ...p, [k]: v }));

  const adicionar = async (e) => {
    e.preventDefault();
    try {
      await api.adicionarPonto(id, {
        endereco: ponto.endereco,
        ordem: proximaOrdem,
        dataHoraChegada: comSegundos(ponto.dataHoraChegada),
        dataHoraSaida: ponto.dataHoraSaida ? comSegundos(ponto.dataHoraSaida) : null,
        pedidoId: ponto.pedidoId ? Number(ponto.pedidoId) : null,
      });
      setMsg({ kind: 'success', text: `Ponto ${proximaOrdem} adicionado.` });
      setPonto({ endereco: '', dataHoraChegada: '', dataHoraSaida: '', pedidoId: '' });
      reload();
    } catch (err) { setMsg({ kind: 'error', text: err.message }); }
  };

  const registrarSaida = async (p) => {
    try {
      await api.registrarSaida(id, p.id, comSegundos(saidas[p.id]));
      setMsg({ kind: 'success', text: `Saída do ponto ${p.ordem} registrada.` });
      reload();
    } catch (err) { setMsg({ kind: 'error', text: err.message }); }
  };

  return (
    <>
      <PageHeader title={`Roteiro de ${fmtData(roteiro.data)}`} subtitle={`Motorista: ${roteiro.motoristaNome}`}>
        <Link className="btn" to="/roteiros">Voltar</Link>
      </PageHeader>
      <Alert kind={msg.kind}>{msg.text}</Alert>

      {custo.jornadaExcedida && (
        <Alert kind="warn">A jornada deste roteiro passou do limite de {num(custo.jornadaMaximaHoras)} h.</Alert>
      )}

      <div className="kpis">
        <Kpi label="Tempo parado" value={fmtMin(custo.tempoTotalParadoMinutos)} tone="stop" />
        <Kpi label="Custo de combustível" value={brl(custo.custoCombustivel)} />
        <Kpi label="Custo do tempo parado" value={brl(custo.custoTempoParado)} />
        <Kpi label="Custo total estimado" value={brl(custo.custoTotalEstimado)} tone="strong" />
      </div>
      <p className="muted small">
        {num(custo.distanciaTotalKm)} km ÷ {num(custo.rendimentoKmLitro)} km/l = {num(custo.consumoLitros, 2)} l
        a {brl(custo.precoCombustivel)}/l; {num(custo.tempoTotalParadoHoras, 2)} h paradas a {brl(custo.custoHoraParada)}/h.
      </p>

      <Panel title="Pontos do roteiro">
        {roteiro.pontos.length === 0 ? (
          <p className="empty">Nenhum ponto ainda. O ponto 1 é a partida e não conta tempo parado.</p>
        ) : (
          <div className="table-wrap">
            <table>
              <thead><tr><th>Ordem</th><th>Endereço</th><th>Chegada</th><th>Saída</th><th className="r">Parado</th></tr></thead>
              <tbody>
                {roteiro.pontos.map((p) => (
                  <tr key={p.id}>
                    <td>{p.ordem}{p.ordem === 1 && <span className="tag">Partida</span>}</td>
                    <td>{p.endereco}</td>
                    <td>{fmtDataHora(p.dataHoraChegada)}</td>
                    <td>
                      {p.dataHoraSaida ? fmtDataHora(p.dataHoraSaida) : (
                        <span className="inline-form">
                          <input type="datetime-local" aria-label={`Saída do ponto ${p.ordem}`}
                            value={saidas[p.id] ?? ''} onChange={(e) => setSaidas((s) => ({ ...s, [p.id]: e.target.value }))} />
                          <button className="btn small" disabled={!saidas[p.id]} onClick={() => registrarSaida(p)}>
                            Registrar saída
                          </button>
                        </span>
                      )}
                    </td>
                    <td className="r">{p.ordem === 1 ? '—' : p.tempoParadoMinutos != null ? <span className="stop">{fmtMin(p.tempoParadoMinutos)}</span> : 'em andamento'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </Panel>

      <Panel title={`Adicionar ponto ${proximaOrdem}`}>
        <form className="form-grid" onSubmit={adicionar}>
          <Field label="Endereço">
            <input required value={ponto.endereco} onChange={(e) => setP('endereco', e.target.value)} />
          </Field>
          <Field label="Chegada">
            <input type="datetime-local" required value={ponto.dataHoraChegada} onChange={(e) => setP('dataHoraChegada', e.target.value)} />
          </Field>
          <Field label="Saída" hint={proximaOrdem === 1 ? 'Na partida, informe a saída se já saiu.' : 'Deixe em branco se ainda está parado.'}>
            <input type="datetime-local" value={ponto.dataHoraSaida} onChange={(e) => setP('dataHoraSaida', e.target.value)} />
          </Field>
          {ehGerente && (
            <Field label="Pedido (opcional)">
              <select value={ponto.pedidoId} onChange={(e) => setP('pedidoId', e.target.value)}>
                <option value="">Sem pedido</option>
                {pedidos.map((p) => <option key={p.id} value={p.id}>#{p.id} — {p.clienteNome}</option>)}
              </select>
            </Field>
          )}
          <div className="form-actions"><button className="btn primary">Adicionar ponto</button></div>
        </form>
      </Panel>
    </>
  );
}
