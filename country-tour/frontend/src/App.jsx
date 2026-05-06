import { useState, useEffect } from 'react'
import './App.css'

const COUNTRIES = [
  { code: 'BG', name: 'Bulgaria' },
  { code: 'GR', name: 'Greece' },
  { code: 'MK', name: 'Macedonia' },
  { code: 'RO', name: 'Romania' },
  { code: 'SR', name: 'Serbia' },
  { code: 'TR', name: 'Turkey' },
]

const CURRENCIES = ['EUR', 'USD', 'GBP', 'TRY', 'MKD', 'RSD', 'RON']

const DEFAULT_FORM = {
  country: 'BG',
  currency: 'EUR',
  budgetPerCountry: 100,
  totalBudget: 1200,
}

export default function App() {
  const [user, setUser] = useState(undefined) // undefined = loading, null = not authed

  useEffect(() => {
    fetch(import.meta.env.VITE_API_URL + '/me', { credentials: 'include' })
      .then(res => res.ok ? res.json() : null)
      .then(data => setUser(data))
      .catch(() => setUser(null))
  }, [])

  if (user === undefined) {
    return <div className="page"><p className="loading">Loading…</p></div>
  }

  const oauth2Endpoint = import.meta.env.VITE_API_URL + '/oauth2/authorization/google'

  if (!user) {
    return <LoginPage oauth2Endpoint={oauth2Endpoint} />
  }

  return <TourPlanner user={user} onLogout={() => setUser(null)} />
}

function LoginPage({ oauth2Endpoint }) {
  return (
    <div className="page page--centered">
      <div className="card login-card">
        <div className="login-icon">✈</div>
        <h1>Country Tour Planner</h1>
        <p>Plan your budget tour across neighbouring countries with live currency conversion.</p>
        <a href={oauth2Endpoint} className="btn-google">
          <GoogleIcon />
          Sign in with Google
        </a>
      </div>
    </div>
  )
}

function TourPlanner({ user, onLogout }) {
  const [form, setForm] = useState(DEFAULT_FORM)
  const [result, setResult] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  function handleChange(e) {
    const { name, value } = e.target
    setForm(f => ({ ...f, [name]: value }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setLoading(true)
    setError(null)
    setResult(null)

    const params = new URLSearchParams({
      country: form.country,
      currency: form.currency,
      budgetPerCountry: form.budgetPerCountry,
      totalBudget: form.totalBudget,
    })

    try {
      const res = await fetch(`${import.meta.env.VITE_API_URL}/tour?${params}`)
      if (res.status === 401 || res.status === 403) {
        onLogout()
        return
      }
      if (!res.ok) {
        const text = await res.text()
        throw new Error(text || `Request failed: ${res.status}`)
      }
      setResult(await res.json())
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleLogout() {
    await fetch('/logout', { method: 'POST' })
    onLogout()
  }

  return (
    <div className="page">
      <header>
        <div className="header-left">
          <h1>Country Tour Planner</h1>
          <p>Calculate your tour budget across neighbouring countries</p>
        </div>
        <div className="header-right">
          {user.picture && <img src={user.picture} alt={user.name} className="avatar" referrerPolicy="no-referrer" />}
          <span className="user-name">{user.name}</span>
          <button className="btn-logout" onClick={handleLogout}>Sign out</button>
        </div>
      </header>

      <main>
        <section className="card form-card">
          <form onSubmit={handleSubmit}>
            <div className="field">
              <label htmlFor="country">Starting country</label>
              <select id="country" name="country" value={form.country} onChange={handleChange}>
                {COUNTRIES.map(c => (
                  <option key={c.code} value={c.code}>{c.name} ({c.code})</option>
                ))}
              </select>
            </div>

            <div className="field">
              <label htmlFor="currency">Budget currency</label>
              <select id="currency" name="currency" value={form.currency} onChange={handleChange}>
                {CURRENCIES.map(c => (
                  <option key={c} value={c}>{c}</option>
                ))}
              </select>
            </div>

            <div className="field">
              <label htmlFor="budgetPerCountry">Budget per country</label>
              <input
                id="budgetPerCountry"
                name="budgetPerCountry"
                type="number"
                min="1"
                step="any"
                required
                value={form.budgetPerCountry}
                onChange={handleChange}
              />
            </div>

            <div className="field">
              <label htmlFor="totalBudget">Total budget</label>
              <input
                id="totalBudget"
                name="totalBudget"
                type="number"
                min="1"
                step="any"
                required
                value={form.totalBudget}
                onChange={handleChange}
              />
            </div>

            <button type="submit" disabled={loading}>
              {loading ? 'Calculating…' : 'Plan tour'}
            </button>
          </form>
        </section>

        {error && (
          <section className="card error-card">
            <strong>Error</strong>
            <p>{error}</p>
          </section>
        )}

        {result && <Results data={result} />}
      </main>
    </div>
  )
}

function Results({ data }) {
  const countryName = COUNTRIES.find(c => c.code === data.startingCountry)?.name ?? data.startingCountry

  return (
    <section className="card results-card">
      <h2>Tour summary</h2>

      <div className="summary-grid">
        <Stat label="Starting country" value={`${countryName} (${data.startingCountry})`} />
        <Stat label="Currency" value={data.currency} />
        <Stat label="Total budget" value={fmt(data.totalBudget, data.currency)} />
        <Stat label="Per country" value={fmt(data.budgetPerCountry, data.currency)} />
        <Stat label="Neighbours" value={data.neighborCount} />
        <Stat label="Complete tours" value={data.completeTours} />
        <Stat label="Leftover" value={fmt(data.leftover, data.currency)} accent />
      </div>

      {data.countryBudgets?.length > 0 && (
        <>
          <h3>Country breakdown</h3>
          <table>
            <thead>
              <tr>
                <th>Country</th>
                <th>Local currency</th>
                <th className="num">Amount</th>
              </tr>
            </thead>
            <tbody>
              {data.countryBudgets.map(b => (
                <tr key={b.country}>
                  <td>{COUNTRIES.find(c => c.code === b.country)?.name ?? b.country}</td>
                  <td>{b.currency}</td>
                  <td className="num">{fmt(b.amount, b.currency)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}
    </section>
  )
}

function Stat({ label, value, accent }) {
  return (
    <div className={`stat${accent ? ' stat--accent' : ''}`}>
      <span className="stat-label">{label}</span>
      <span className="stat-value">{value}</span>
    </div>
  )
}

function GoogleIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 18 18" aria-hidden="true">
      <path fill="#4285F4" d="M16.51 8H8.98v3h4.3c-.18 1-.74 1.48-1.6 2.04v2.01h2.6a7.8 7.8 0 0 0 2.38-5.88c0-.57-.05-.66-.15-1.18z"/>
      <path fill="#34A853" d="M8.98 17c2.16 0 3.97-.72 5.3-1.94l-2.6-2a4.8 4.8 0 0 1-7.18-2.54H1.83v2.07A8 8 0 0 0 8.98 17z"/>
      <path fill="#FBBC05" d="M4.5 10.52a4.8 4.8 0 0 1 0-3.04V5.41H1.83a8 8 0 0 0 0 7.18l2.67-2.07z"/>
      <path fill="#EA4335" d="M8.98 4.18c1.17 0 2.23.4 3.06 1.2l2.3-2.3A8 8 0 0 0 1.83 5.4L4.5 7.49a4.77 4.77 0 0 1 4.48-3.3z"/>
    </svg>
  )
}

function fmt(amount, currency) {
  try {
    return new Intl.NumberFormat(undefined, {
      style: 'currency',
      currency,
      maximumFractionDigits: 2,
    }).format(amount)
  } catch {
    return `${amount.toFixed(2)} ${currency}`
  }
}
