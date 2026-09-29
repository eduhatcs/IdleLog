import { useEffect, useState } from 'react';
import { api } from '../api.js';
import { useAsync } from '../hooks.js';
import { Alert, Field, Loading, PageHeader, Panel } from '../components/ui.jsx';

export default function Parametrizacao() {
  const { data, loading, error } = useAsync(() => api.parametrizacao());
  const [form, setForm] = useState(null);
  const [msg, setMsg] = useState({ kind: 'error', text: '' });

  useEffect(() => {
    if (data) setForm({
      precoCombustivel: data.precoCombustivel,
      kmPorLitroPadrao: data.kmPorLitroPadrao,
      jornadaMaximaHoras: data.jornadaMaximaHoras,
      custoHoraParada: data.custoHoraParada ?? 0,
    });
  }, [data]);

  const set = (k, v) => setForm((f) => ({ ...f, [k]: v }));

  const salvar = async (e) => {
    e.preventDefault();
    try {
      await api.salvarParametrizacao(Object.fromEntries(Object.entries(form).map(([k, v]) => [k, Number(v)])));
      setMsg({ kind: 'success', text: 'Parâmetros salvos. Os custos dos roteiros já usam os novos valores.' });
    } catch (err) { setMsg({ kind: 'error', text: err.message }); }
  };

  return (
    <>
      <PageHeader title="Parâmetros da frota" subtitle="Valores usados no cálculo de custo e na validação da jornada." />
      <Alert kind={msg.kind}>{msg.text}</Alert>
      <Alert>{error}</Alert>
      {loading && <Loading />}
      {form && (
        <Panel>
          <form className="form-grid" onSubmit={salvar}>
            <Field label="Preço do combustível (R$/l)">
              <input type="number" step="0.01" min="0.01" required value={form.precoCombustivel} onChange={(e) => set('precoCombustivel', e.target.value)} />
            </Field>
            <Field label="km/l padrão" hint="Sugerido quando o rendimento do motorista não é conhecido.">
              <input type="number" step="0.1" min="0.1" required value={form.kmPorLitroPadrao} onChange={(e) => set('kmPorLitroPadrao', e.target.value)} />
            </Field>
            <Field label="Jornada máxima (horas)">
              <input type="number" step="0.5" min="0.1" required value={form.jornadaMaximaHoras} onChange={(e) => set('jornadaMaximaHoras', e.target.value)} />
            </Field>
            <Field label="Custo da hora parada (R$/h)">
              <input type="number" step="0.01" min="0" required value={form.custoHoraParada} onChange={(e) => set('custoHoraParada', e.target.value)} />
            </Field>
            <div className="form-actions"><button className="btn primary">Salvar parâmetros</button></div>
          </form>
        </Panel>
      )}
    </>
  );
}
