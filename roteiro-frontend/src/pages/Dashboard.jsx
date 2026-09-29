import { useMemo, useState } from 'react';
import {
  Bar, BarChart, CartesianGrid, Legend, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis,
} from 'recharts';
import { api, carregarRoteirosComCusto } from '../api.js';
import { useAsync } from '../hooks.js';
import { Alert, Empty, Field, Kpi, Loading, PageHeader, Panel } from '../components/ui.jsx';
import { brl, fmtData, fmtMin, num } from '../utils.js';

const COR = { parado: '#C77D0A', combustivel: '#0F5C4D', total: '#16211D', grade: '#E1E6E3' };

const rotuloEixo = (chave, agrupar) =>
  agrupar === 'mes' ? `${chave.slice(5, 7)}/${chave.slice(0, 4)}` : fmtData(chave).slice(0, 5);

export default function Dashboard() {
  const { data, loading, error, reload } = useAsync(carregarRoteirosComCusto);
  const motoristas = useAsync(() => api.list('motoristas'));
  const [f, setF] = useState({ de: '', ate: '', motoristaId: '', agrupar: 'dia' });
  const set = (k, v) => setF((x) => ({ ...x, [k]: v }));

  const filtrados = useMemo(() => (data ?? []).filter((r) =>
    (!f.de || r.data >= f.de) && (!f.ate || r.data <= f.ate) &&
    (!f.motoristaId || String(r.motoristaId) === f.motoristaId)), [data, f]);

  // Agrupa por dia (YYYY-MM-DD) ou mês (YYYY-MM)
  const serie = useMemo(() => {
    const mapa = new Map();
    filtrados.forEach((r) => {
      const chave = f.agrupar === 'mes' ? r.data.slice(0, 7) : r.data;
      const a = mapa.get(chave) ?? { chave, parado: 0, combustivel: 0, paradaCusto: 0, total: 0, roteiros: 0 };
      a.parado += r.custo.tempoTotalParadoMinutos ?? 0;
      a.combustivel += Number(r.custo.custoCombustivel ?? 0);
      a.paradaCusto += Number(r.custo.custoTempoParado ?? 0);
      a.total += Number(r.custo.custoTotalEstimado ?? 0);
      a.roteiros += 1;
      mapa.set(chave, a);
    });
    return [...mapa.values()].sort((a, b) => a.chave.localeCompare(b.chave))
      .map((a) => ({ ...a, rotulo: rotuloEixo(a.chave, f.agrupar) }));
  }, [filtrados, f.agrupar]);

  const porMotorista = useMemo(() => {
    const mapa = new Map();
    filtrados.forEach((r) => mapa.set(r.motoristaNome, (mapa.get(r.motoristaNome) ?? 0) + (r.custo.tempoTotalParadoMinutos ?? 0)));
    return [...mapa].map(([nome, parado]) => ({ nome, parado })).sort((a, b) => b.parado - a.parado);
  }, [filtrados]);

  const tot = useMemo(() => filtrados.reduce((a, r) => ({
    parado: a.parado + (r.custo.tempoTotalParadoMinutos ?? 0),
    custo: a.custo + Number(r.custo.custoTotalEstimado ?? 0),
    km: a.km + Number(r.custo.distanciaTotalKm ?? 0),
    excedidas: a.excedidas + (r.custo.jornadaExcedida ? 1 : 0),
  }), { parado: 0, custo: 0, km: 0, excedidas: 0 }), [filtrados]);

  const invalido = f.de && f.ate && f.de > f.ate;
  const porDia = f.agrupar === 'dia';

  return (
    <>
      <PageHeader title="Dashboard" subtitle="Tempo parado e custo estimado dos roteiros, por dia, mês ou período.">
        <button className="btn" onClick={reload}>Atualizar</button>
      </PageHeader>

      <Panel>
        <div className="form-grid filters">
          <Field label="De"><input type="date" value={f.de} onChange={(e) => set('de', e.target.value)} /></Field>
          <Field label="Até"><input type="date" value={f.ate} onChange={(e) => set('ate', e.target.value)} /></Field>
          <Field label="Motorista">
            <select value={f.motoristaId} onChange={(e) => set('motoristaId', e.target.value)}>
              <option value="">Todos</option>
              {motoristas.data?.map((m) => <option key={m.id} value={m.id}>{m.nome}</option>)}
            </select>
          </Field>
          <Field label="Agrupar por">
            <div className="seg" role="radiogroup" aria-label="Agrupar por">
              {[['dia', 'Dia'], ['mes', 'Mês']].map(([v, l]) => (
                <button key={v} type="button" role="radio" aria-checked={f.agrupar === v}
                  className={f.agrupar === v ? 'on' : ''} onClick={() => set('agrupar', v)}>{l}</button>
              ))}
            </div>
          </Field>
          {(f.de || f.ate || f.motoristaId) && (
            <div className="form-actions">
              <button className="btn" onClick={() => setF({ de: '', ate: '', motoristaId: '', agrupar: f.agrupar })}>Limpar filtros</button>
            </div>
          )}
        </div>
        {invalido && <Alert>A data inicial não pode ser depois da final.</Alert>}
      </Panel>

      {loading && <Loading text="Calculando custos dos roteiros…" />}
      <Alert>{error}</Alert>

      {data && !invalido && (filtrados.length === 0 ? (
        <Panel><Empty>Nenhum roteiro nesse período. Ajuste os filtros ou crie roteiros na aba Roteiros.</Empty></Panel>
      ) : (
        <>
          <div className="kpis">
            <Kpi label="Roteiros" value={filtrados.length} />
            <Kpi label="Tempo parado" value={fmtMin(tot.parado)} tone="stop" />
            <Kpi label="Custo estimado" value={brl(tot.custo)} tone="strong" />
            <Kpi label="Jornadas excedidas" value={tot.excedidas} tone={tot.excedidas ? 'alert' : ''} />
          </div>
          <p className="muted small">{num(tot.km)} km percorridos no período.</p>

          <div className="charts">
            <Panel title={`Tempo parado por ${porDia ? 'dia' : 'mês'} (min)`}>
              <div className="chart">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={serie} margin={{ top: 8, right: 8, left: -12, bottom: 0 }}>
                    <CartesianGrid stroke={COR.grade} vertical={false} />
                    <XAxis dataKey="rotulo" tickLine={false} axisLine={false} fontSize={12} />
                    <YAxis tickLine={false} axisLine={false} fontSize={12} />
                    <Tooltip formatter={(v) => [fmtMin(v), 'Parado']} cursor={{ fill: '#F0F3F1' }} />
                    <Bar dataKey="parado" name="Parado" fill={COR.parado} radius={[3, 3, 0, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </Panel>

            <Panel title={`Custo estimado por ${porDia ? 'dia' : 'mês'} (R$)`}>
              <div className="chart">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={serie} margin={{ top: 8, right: 8, left: -4, bottom: 0 }}>
                    <CartesianGrid stroke={COR.grade} vertical={false} />
                    <XAxis dataKey="rotulo" tickLine={false} axisLine={false} fontSize={12} />
                    <YAxis tickLine={false} axisLine={false} fontSize={12} />
                    <Tooltip formatter={(v, n) => [brl(v), n]} cursor={{ fill: '#F0F3F1' }} />
                    <Legend iconType="square" wrapperStyle={{ fontSize: 12 }} />
                    <Bar dataKey="combustivel" name="Combustível" stackId="c" fill={COR.combustivel} />
                    <Bar dataKey="paradaCusto" name="Tempo parado" stackId="c" fill={COR.parado} radius={[3, 3, 0, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </Panel>

            {serie.length > 1 && (
              <Panel title="Evolução do custo total (R$)">
                <div className="chart short">
                  <ResponsiveContainer width="100%" height="100%">
                    <LineChart data={serie} margin={{ top: 8, right: 12, left: -4, bottom: 0 }}>
                      <CartesianGrid stroke={COR.grade} vertical={false} />
                      <XAxis dataKey="rotulo" tickLine={false} axisLine={false} fontSize={12} />
                      <YAxis tickLine={false} axisLine={false} fontSize={12} />
                      <Tooltip formatter={(v) => [brl(v), 'Total']} />
                      <Line type="monotone" dataKey="total" stroke={COR.total} strokeWidth={2} dot={{ r: 3 }} />
                    </LineChart>
                  </ResponsiveContainer>
                </div>
              </Panel>
            )}

            <Panel title="Tempo parado por motorista (min)">
              <div className="chart short">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={porMotorista} layout="vertical" margin={{ top: 4, right: 12, left: 8, bottom: 0 }}>
                    <CartesianGrid stroke={COR.grade} horizontal={false} />
                    <XAxis type="number" tickLine={false} axisLine={false} fontSize={12} />
                    <YAxis type="category" dataKey="nome" width={110} tickLine={false} axisLine={false} fontSize={12} />
                    <Tooltip formatter={(v) => [fmtMin(v), 'Parado']} cursor={{ fill: '#F0F3F1' }} />
                    <Bar dataKey="parado" fill={COR.parado} radius={[0, 3, 3, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </Panel>
          </div>
        </>
      ))}
    </>
  );
}
