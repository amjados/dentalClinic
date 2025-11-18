// src/App.tsx
// Main UI for DentalClinic — imports i18n and display data
import { useMemo, useState, useEffect, useRef } from 'react'
import { dict, type TStrings, type Lang, common } from './i18n_dict'
import { displayData } from './i18n_display_data'
import { getEnv } from '../utils/env';


export type Role = 'guest' | 'patient' | 'doctor' | 'admin' | 'super'

type Theme = 'emerald' | 'sky' | 'rose' | 'violet' | 'amber' | 'dark'

function NavLink({ children, href = '#' }: { children: React.ReactNode; href?: string }) {
    return (
        <a href={href} className="px-3 py-2 rounded-lg hover:bg-white/10 transition">
            {children}
        </a>
    )
}

function Stat({ label, value }: { label: string; value: string }) {
    return (
        <div className="text-center">
            <div className="text-3xl font-bold">{value}</div>
            <div className="text-sm text-white/70">{label}</div>
        </div>
    )
}

function ServiceCard({ icon, title, desc }: { icon: string; title: string; desc: string }) {
    return (
        <div className="bg-white rounded-2xl p-6 shadow-sm border border-slate-100 hover:shadow-md transition">
            <div className="text-3xl mb-3">{icon}</div>
            <h3 className="font-semibold text-slate-800 mb-2">{title}</h3>
            <p className="text-slate-500 text-sm leading-6">{desc}</p>
        </div>
    )
}

function DoctorCard({ name, role, img, tags = [] }: { name: string; role: string; img: string; tags?: ReadonlyArray<string> }) {
    return (
        <div className="bg-white rounded-2xl p-5 shadow-sm border border-slate-100">
            <div className="flex items-center gap-4">
                <img src={img} alt={name} className="w-16 h-16 rounded-full object-cover" />
                <div>
                    <div className="font-semibold text-slate-800">{name}</div>
                    <div className="text-sm text-slate-500">{role}</div>
                    <div className="mt-2 flex flex-wrap gap-2">
                        {tags.map((t, i) => (
                            <span key={i} className="text-xs px-2 py-1 rounded-full bg-slate-100 text-slate-600">{t}</span>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    )
}

function AppointmentForm({ t, primaryBg, primaryHover }: { t: TStrings; primaryBg: string; primaryHover: string }) {
    const [form, setForm] = useState({ name: '', phone: '', date: '', service: t.booking.service })
    const inputCls = 'w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-emerald-300'
    return (
        <form className="bg-white rounded-2xl p-6 shadow-sm border border-slate-100">
            <h3 className="text-lg font-semibold text-slate-800 mb-4">{t.booking.title}</h3>
            <div className="grid md:grid-cols-2 gap-4">
                <input className={inputCls} placeholder={t.booking.name} value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} />
                <input className={inputCls} placeholder={t.booking.phone} value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} />
                <input type="date" className={inputCls} value={form.date} onChange={e => setForm({ ...form, date: e.target.value })} />
                <select className={inputCls} value={form.service} onChange={() => setForm({ ...form, service: t.booking.service })}>
                    <option>{t.svc.checkupsT}</option>
                    <option>{t.svc.whiteningT}</option>
                    <option>{t.svc.orthoT}</option>
                    <option>{t.svc.implantsT}</option>
                </select>
            </div>
            <button type="button" className={`mt-4 w-full md:w-auto px-5 py-2.5 rounded-xl ${primaryBg} text-white ${primaryHover}`}>
                {t.booking.confirm}
            </button>
        </form>
    )
}

function LanguageSwitcher({ lang, setLang, t }: { lang: Lang; setLang: (l: Lang) => void; t: TStrings }) {
    const [open, setOpen] = useState(false)
    const popRef = useRef<HTMLDivElement | null>(null)
    const btnRef = useRef<HTMLButtonElement | null>(null)

    const items: { key: Lang; label: string; abbr: string }[] = [
        { key: 'ar', label: t.langs.ar, abbr: 'AR' },
        { key: 'en', label: t.langs.en, abbr: 'EN' },
        { key: 'hi', label: t.langs.hi, abbr: 'HI' },
    ]

    useEffect(() => {
        function onDocClick(e: MouseEvent) {
            if (!open) return
            const target = e.target as Node
            if (popRef.current && !popRef.current.contains(target) && btnRef.current && !btnRef.current.contains(target)) {
                setOpen(false)
            }
        }
        function onEsc(e: KeyboardEvent) { if (e.key === 'Escape') setOpen(false) }
        document.addEventListener('mousedown', onDocClick)
        document.addEventListener('keydown', onEsc)
        return () => { document.removeEventListener('mousedown', onDocClick); document.removeEventListener('keydown', onEsc) }
    }, [open])

    const globeIcon = (
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 21c4.971 0 9-4.029 9-9s-4.029-9-9-9-9 4.029-9 9 4.029 9 9 9Z" stroke="currentColor" strokeWidth="1.5" />
            <path d="M3 12h18M12 3c-2.5 2.8-3.75 6-3.75 9S9.5 18.2 12 21m0-18c2.5 2.8 3.75 6 3.75 9S14.5 18.2 12 21" stroke="currentColor" strokeWidth="1.5" />
        </svg>
    )

    return (
        <div className="relative z-[700]">
            <button ref={btnRef} aria-haspopup="dialog" aria-expanded={open}
                onClick={() => setOpen(v => !v)}
                className={`w-9 h-9 grid place-items-center rounded-xl border border-white/30 bg-white/10 hover:bg-white/20 ${open ? 'ring-2 ring-white/70' : ''}`}
                title={t.langLabel}>
                {globeIcon}
            </button>

            {open && (
                <div ref={popRef} role="dialog" className={`absolute ${lang === 'ar' ? 'right-0' : 'left-0'} mt-2 z-[1000] w-44 rounded-2xl bg-white/95 backdrop-blur border border-white/40 p-3 shadow-xl`}
                    style={{ WebkitBackdropFilter: 'blur(6px)' }}>
                    <div className="text-xs font-medium text-slate-700 mb-2">
                        {t.ui.pickLanguage}
                    </div>
                    <div className="grid grid-cols-2 gap-2">
                        {items.map(it => (
                            <button key={it.key}
                                onClick={() => { setLang(it.key); setOpen(false) }}
                                className={`relative h-10 rounded-xl border ${lang === it.key ? 'border-slate-700 ring-2 ring-slate-400' : 'border-slate-200'} bg-slate-50 text-slate-700 hover:bg-slate-100`}
                                title={it.label}
                                aria-pressed={lang === it.key}
                            >
                                <span className="text-xs font-semibold">{it.abbr}</span>
                                {lang === it.key && (
                                    <span className="absolute inset-0 grid place-items-center text-slate-700 text-sm">✓</span>
                                )}
                            </button>
                        ))}
                        <div className="h-10 rounded-xl border border-transparent" aria-hidden />
                    </div>
                </div>
            )}
        </div>
    )
}

function ThemeSwitcher({ t, dir, theme, setTheme }: { t: TStrings; dir: 'rtl' | 'ltr'; theme: Theme; setTheme: (th: Theme) => void }) {
    const [open, setOpen] = useState(false)
    const popRef = useRef<HTMLDivElement | null>(null)
    const btnRef = useRef<HTMLButtonElement | null>(null)

    const items: { key: Theme; label: string; dot: string }[] = [
        { key: 'emerald', label: t.themes.emerald, dot: 'bg-emerald-500' },
        { key: 'sky', label: t.themes.sky, dot: 'bg-sky-500' },
        { key: 'rose', label: t.themes.rose, dot: 'bg-rose-500' },
        { key: 'violet', label: t.themes.violet, dot: 'bg-violet-500' },
        { key: 'amber', label: t.themes.amber, dot: 'bg-amber-500' },
        { key: 'dark', label: t.themes.dark, dot: 'bg-slate-700' },
    ]

    useEffect(() => {
        function onDocClick(e: MouseEvent) {
            if (!open) return
            const target = e.target as Node
            if (popRef.current && !popRef.current.contains(target) && btnRef.current && !btnRef.current.contains(target)) {
                setOpen(false)
            }
        }
        function onEsc(e: KeyboardEvent) { if (e.key === 'Escape') setOpen(false) }
        document.addEventListener('mousedown', onDocClick)
        document.addEventListener('keydown', onEsc)
        return () => { document.removeEventListener('mousedown', onDocClick); document.removeEventListener('keydown', onEsc) }
    }, [open])

    return (
        <div className="relative z-[700]">
            <button ref={btnRef} aria-haspopup="dialog" aria-expanded={open}
                onClick={() => setOpen(v => !v)}
                className={`w-9 h-9 grid place-items-center rounded-xl border border-white/30 bg-white/10 hover:bg-white/20 ${open ? 'ring-2 ring-white/70' : ''}`}
                title={t.ui.chooseTheme}>
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M12 3c-4.97 0-9 3.582-9 8 0 4.418 4.03 8 9 8h1.5a2.5 2.5 0 0 0 2.5-2.5c0-.69-.28-1.316-.73-1.77a2.5 2.5 0 0 1 1.77-4.27H17a4 4 0 0 0 4-4C21 5.015 16.97 3 12 3Z" stroke="currentColor" strokeWidth="1.5" />
                    <circle cx="7.5" cy="9" r="1.25" fill="currentColor" />
                    <circle cx="12" cy="7.5" r="1.25" fill="currentColor" />
                    <circle cx="9.75" cy="12.25" r="1.25" fill="currentColor" />
                </svg>
            </button>

            {open && (
                <div ref={popRef} role="dialog" className={`absolute ${dir === 'rtl' ? 'right-0' : 'left-0'} mt-2 z-[1000] w-52 rounded-2xl bg-white/95 backdrop-blur border border-white/40 p-3 shadow-xl`}
                    style={{ WebkitBackdropFilter: 'blur(6px)' }}>
                    <div className="text-xs font-medium text-slate-700 mb-2">{t.ui.pickColor}</div>
                    <div className="grid grid-cols-2 gap-2">
                        {items.slice(0, 4).map(it => (
                            <button key={it.key}
                                onClick={() => { setTheme(it.key); setOpen(false) }}
                                className={`relative h-10 rounded-xl border ${theme === it.key ? 'border-slate-700 ring-2 ring-slate-400' : 'border-slate-200'} ${it.dot} focus:outline-none`}
                                title={it.label}
                                aria-pressed={theme === it.key}
                            >
                                {theme === it.key && (
                                    <span className="absolute inset-0 grid place-items-center text-white text-sm">✓</span>
                                )}
                            </button>
                        ))}
                    </div>
                </div>
            )}
        </div>
    )
}

export default function DentalClinicUI() {
    const [lang, setLang] = useState<Lang>('ar')
    const [role, setRole] = useState<Role>('guest')
    const [loginOpen, setLoginOpen] = useState(false)
    const [theme, setTheme] = useState<Theme>('emerald')
    const [doctors, setDoctors] = useState<any[]>([])
    const [loadingDoctors, setLoadingDoctors] = useState(false)
    const t = dict[lang]

    useEffect(() => {
        try {
            const raw = localStorage.getItem('auth')
            if (raw) {
                const auth = JSON.parse(raw)
                if (auth?.user?.role) setRole(auth.user.role as Role)
            }
            const savedTheme = localStorage.getItem('theme') as Theme | null
            if (savedTheme) setTheme(savedTheme)
            const savedLang = localStorage.getItem('lang') as Lang | null
            if (savedLang) setLang(savedLang)
        } catch { }
    }, [])

    // Fetch doctors from backend
    useEffect(() => {
        const fetchDoctors = async () => {
            setLoadingDoctors(true)
            try {
                const API = getEnv('VITE_API_URL', '')
                const response = await fetch(`${API}/api/dentists`)
                if (response.ok) {
                    const data = await response.json()
                    setDoctors(data)
                } else {
                    console.error('Failed to fetch doctors:', response.status)
                }
            } catch (error) {
                console.error('Error fetching doctors:', error)
            } finally {
                setLoadingDoctors(false)
            }
        }
        fetchDoctors()
    }, [])

    const dir = useMemo(() => lang === 'ar' ? 'rtl' : 'ltr', [lang])
    const align = dir === 'rtl' ? 'text-right' : 'text-left'

    // THEME: single source of truth
    const themeMap: Record<Theme, { grad: string; bg: string; hover: string; text: string; accent: string }> = {
        emerald: { grad: 'from-emerald-700 to-emerald-600', bg: 'bg-emerald-600', hover: 'hover:bg-emerald-700', text: 'text-emerald-700', accent: 'text-emerald-600' },
        sky: { grad: 'from-sky-700 to-sky-600', bg: 'bg-sky-600', hover: 'hover:bg-sky-700', text: 'text-sky-700', accent: 'text-sky-600' },
        rose: { grad: 'from-rose-700 to-rose-600', bg: 'bg-rose-600', hover: 'hover:bg-rose-700', text: 'text-rose-700', accent: 'text-rose-600' },
        violet: { grad: 'from-violet-700 to-violet-600', bg: 'bg-violet-600', hover: 'hover:bg-violet-700', text: 'text-violet-700', accent: 'text-violet-600' },
        amber: { grad: 'from-amber-700 to-amber-600', bg: 'bg-amber-600', hover: 'hover:bg-amber-700', text: 'text-amber-700', accent: 'text-amber-600' },
        dark: { grad: 'from-slate-900 to-slate-800', bg: 'bg-slate-800', hover: 'hover:bg-slate-700', text: 'text-slate-900', accent: 'text-indigo-400' },
    }

    const themeGrad = themeMap[theme].grad
    const primaryBg = themeMap[theme].bg
    const primaryHover = themeMap[theme].hover
    const primaryText = themeMap[theme].text
    const accentText = themeMap[theme].accent

    useEffect(() => { localStorage.setItem('theme', theme) }, [theme])
    useEffect(() => { localStorage.setItem('lang', lang) }, [lang])

    function LoginModal() {
        const [email, setEmail] = useState('')
        const [password, setPassword] = useState('')

        async function doLogin() {
            try {
                //const API = (import.meta as any).env?.VITE_API_URL || '';
                const API = getEnv('VITE_API_URL', '');

                const res = await fetch(`${API}/api/auth/login`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    credentials: API ? 'include' : 'same-origin',
                    body: JSON.stringify({ email, password })
                });
                if (!res.ok) throw new Error('HTTP ' + res.status)
                const data = await res.json()
                localStorage.setItem('auth', JSON.stringify(data))
                setRole((data?.user?.role ?? 'guest') as Role)
                setLoginOpen(false)
            } catch (e) {
                alert(dict[lang].auth.error)
            }
        }

        if (!loginOpen) return null
        return (
            <div className="fixed inset-0 z-50 grid place-items-center bg-black/40 p-4">
                <div dir={dir} className={`w-full max-w-md bg-white text-slate-800 rounded-2xl p-6 ${align}`}>
                    <div className="text-xl font-semibold mb-4">{t.auth.title}</div>
                    <div className="space-y-3">
                        <input className="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-emerald-300" placeholder={t.auth.email} value={email} onChange={e => setEmail(e.target.value)} />
                        <input type="password" className="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-emerald-300" placeholder={t.auth.password} value={password} onChange={e => setPassword(e.target.value)} />
                    </div>
                    <div className="mt-5 flex gap-3 justify-end">
                        <button className="px-4 py-2 rounded-xl border border-slate-200" onClick={() => setLoginOpen(false)}>{t.auth.cancel}</button>
                        <button className={`px-4 py-2 rounded-xl ${primaryBg} text-white`} onClick={doLogin}>{t.auth.login}</button>
                    </div>
                    <p className="mt-3 text-xs text-slate-500">* Role is selected by the backend and stored with the user record.</p>
                </div>
            </div>
        )
    }

    function Dashboard() {
        const cardsCls = 'rounded-2xl border border-slate-200 p-5 bg-white text-slate-800'

        if (role === 'patient') return (
            <section className="max-w-6xl mx-auto px-4 py-10 grid md:grid-cols-3 gap-6">
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{t.dash.medHistory}</div>
                    <ul className="text-sm space-y-2">
                        <li>2025-07-12 — Whitening — Dr. Maria</li>
                        <li>2025-04-03 — Checkup — Dr. Omar</li>
                        <li>2024-12-19 — X‑ray — Dr. Ahmed</li>
                    </ul>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{t.dash.upcomingAppts}</div>
                    <p className="text-sm">2025-10-20 10:30 — Cleaning</p>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{t.dash.services}</div>
                    <p className="text-sm">{t.services.desc}</p>
                </div>
            </section>
        )

        if (role === 'doctor') return (
            <section className="max-w-6xl mx-auto px-4 py-10 grid md:grid-cols-3 gap-6">
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{t.dash.todaysAppts}</div>
                    <ul className="text-sm space-y-2">
                        <li>09:00 — Ali K. — Checkup</li>
                        <li>10:30 — Sara M. — Whitening</li>
                        <li>12:00 — Omar N. — Follow‑up</li>
                    </ul>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{t.dash.patientRecords}</div>
                    <p className="text-sm">#A‑1023, #B‑3381, #C‑9920…</p>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{t.dash.quickNotes}</div>
                    <textarea className="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-emerald-300" placeholder="…" />
                </div>
            </section>
        )

        if (role === 'admin') return (
            <section className="max-w-6xl mx-auto px-4 py-10 grid md:grid-cols-3 gap-6">
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{t.dash.userMgmt}</div>
                    <p className="text-sm">Create / deactivate users, assign roles.</p>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{t.dash.dataMgmt}</div>
                    <p className="text-sm">Import / export EMR data.</p>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{t.dash.reports}</div>
                    <p className="text-sm">KPI, appointments, revenue.</p>
                </div>
            </section>
        )

        if (role === 'super') return (
            <section className="max-w-6xl mx-auto px-4 py-10 grid md:grid-cols-3 gap-6">
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{t.super.settings}</div>
                    <p className="text-sm">Branding, locales, access policies.</p>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{t.super.advanced}</div>
                    <p className="text-sm">Role matrices, audit scopes.</p>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{t.super.audit}</div>
                    <p className="text-sm">System‑wide activity stream.</p>
                </div>
            </section>
        )

        return null
    }

    // Runtime sanity checks
    useEffect(() => {
        const mm = common.monthSuffixMap
            ; (['ar', 'en', 'hi'] as Lang[]).forEach(L => {
                const d = (dict as any)[L]
                console.assert(d?.pricing?.monthSuffix === mm[L], `${L}: month suffix mismatch`)
                console.assert(d?.footer?.city && d?.footer?.rights, `${L}: footer strings missing`)
            })
            ; (['ar', 'en', 'hi'] as Lang[]).forEach(L => {
                const docs = (displayData as any).doctors[L]
                const plans = (displayData as any).pricingPlans[L]
                const svcs = (displayData as any).services[L]
                console.assert(docs?.length === 4 && plans?.length === 3 && svcs?.length === 4, `${L}: displayData counts off`)
            })
    }, [])

    return (

        <div dir={dir} className={`min-h-screen bg-gradient-to-b ${themeGrad} text-white ${align}`}>
            {/* Header */}
            <header className="sticky top-0 z-[500] isolate backdrop-blur supports-[backdrop-filter]:bg-white/10 bg-white/10 border-b border-white/10">
                <div className="max-w-6xl mx-auto px-4 py-3 flex items-center justify-between">
                    <div className="flex items-center gap-2">
                        <div className={`w-9 h-9 rounded-xl bg-white ${primaryText} grid place-items-center font-bold`}>DC</div>
                        <div className="font-semibold tracking-wide">{t.brand}</div>
                    </div>
                    <nav className="hidden md:flex items-center gap-1">
                        <NavLink>{t.nav.home}</NavLink>
                        <NavLink>{t.nav.services}</NavLink>
                        <NavLink>{t.nav.doctors}</NavLink>
                        <NavLink>{t.nav.pricing}</NavLink>
                        <NavLink>{t.nav.contact}</NavLink>
                    </nav>
                    <div className="flex items-center gap-3">
                        <span className="px-2 py-1 rounded-lg bg-white/15 text-xs">{t.roles[role]}</span>
                        <LanguageSwitcher lang={lang} setLang={setLang} t={t} />
                        <ThemeSwitcher t={t} dir={dir as 'rtl' | 'ltr'} theme={theme} setTheme={setTheme} />
                        {role === 'guest' ? (
                            <button className={`px-4 py-2 bg-white ${primaryText} rounded-xl font-medium hover:shadow`} onClick={() => setLoginOpen(true)}>
                                {t.auth.login}
                            </button>
                        ) : (
                            <button className="px-4 py-2 bg-white/20 text-white rounded-xl font-medium hover:bg-white/30" onClick={() => { localStorage.removeItem('auth'); setRole('guest') }}>
                                {t.auth.logout}
                            </button>
                        )}
                    </div>
                </div>
            </header>

            {/* Hero */}
            <section className="max-w-6xl mx-auto px-4 pt-12 pb-16">
                <div className={`grid md:grid-cols-2 gap-10 items-center ${dir === 'rtl' ? 'md:[direction:rtl]' : ''}`}>
                    <div>
                        <h1 className="text-4xl md:text-5xl font-extrabold leading-tight">
                            {t.heroTitleA} <span className="underline decoration-white/50">{t.heroTitleB}</span> {lang === 'en' ? 'smile' : lang === 'hi' ? 'मुस्कान' : 'للابتسامة'}
                        </h1>
                        <p className="mt-4 text-white/80 max-w-prose">{t.heroDesc}</p>
                        <div className="mt-6 flex flex-wrap gap-3">
                            {role === 'guest' ? (
                                <button className={`px-5 py-3 rounded-xl bg-white ${primaryText} font-semibold hover:shadow`} onClick={() => setLoginOpen(true)}>{t.auth.startLogin}</button>
                            ) : (
                                <button className={`px-5 py-3 rounded-xl bg-white ${primaryText} font-semibold hover:shadow`} onClick={() => window.scrollTo({ top: document.body.scrollHeight / 3, behavior: 'smooth' })}>{t.auth.goDashboard}</button>
                            )}
                            <button className="px-5 py-3 rounded-xl border border-white/30 font-semibold hover:bg-white/10">{t.ctaView}</button>
                        </div>
                        <div className="mt-8 grid grid-cols-3 gap-6">
                            <Stat label={t.stats.happy} value="12k+" />
                            <Stat label={t.stats.years} value="15" />
                            <Stat label={t.stats.rating} value="4.9★" />
                        </div>
                    </div>
                    <div className="relative">
                        <div className="absolute -inset-4 pointer-events-none bg-white/10 rounded-3xl blur-lg" />
                        <img className="relative rounded-3xl shadow-xl border border-white/20" src="https://images.unsplash.com/photo-1588776814546-1ffcf47267a5?q=80&w=1600&auto=format&fit=crop" alt="Dental care" />
                    </div>
                </div>
            </section>

            {/* Doctors + Booking */}
            <section className="bg-slate-50 text-slate-800">
                <div className="max-w-6xl mx-auto px-4 py-16 grid md:grid-cols-5 gap-8">
                    <div className="md:col-span-3">
                        <h2 className="text-2xl md:text-3xl font-bold">{t.doctors.title}</h2>
                        <p className="text-slate-500 mt-2">{t.doctors.desc}</p>
                        <div className="grid sm:grid-cols-2 gap-6 mt-6">
                            {loadingDoctors ? (
                                <div className="col-span-2 text-center py-8 text-slate-500">Loading doctors...</div>
                            ) : doctors.length > 0 ? (
                                doctors.map((doctor) => {
                                    const fullName = doctor.person ? `${doctor.person.firstName || ''} ${doctor.person.lastName || ''}`.trim() : 'Unknown'
                                    const role = doctor.specialization || doctor.positionTitle || 'Dentist'
                                    const tags = doctor.specialization ? doctor.specialization.split(',').map((s: string) => s.trim()).slice(0, 2) : []
                                    // Generate a placeholder image or use a default
                                    const img = `https://ui-avatars.com/api/?name=${encodeURIComponent(fullName)}&background=random&size=128`

                                    return (
                                        <DoctorCard
                                            key={doctor.id}
                                            name={fullName}
                                            role={role}
                                            img={img}
                                            tags={tags}
                                        />
                                    )
                                })
                            ) : (
                                displayData.doctors[lang].map((d, idx) => (
                                    <DoctorCard key={idx} name={d.name} role={d.role} img={d.img} tags={d.tags} />
                                ))
                            )}
                        </div>
                    </div>
                    <div className="md:col-span-2">
                        <AppointmentForm t={t} primaryBg={primaryBg} primaryHover={primaryHover} />
                    </div>
                </div>
            </section>

            {/* Services */}
            <section className="bg-white text-slate-800">
                <div className="max-w-6xl mx-auto px-4 py-16">
                    <h2 className="text-2xl md:text-3xl font-bold">{t.services.title}</h2>
                    <p className="text-slate-500 mt-2">{t.services.desc}</p>
                    <div className="grid md:grid-cols-4 gap-6 mt-8">
                        {displayData.services[lang].map((s: any, i: number) => (
                            <ServiceCard key={i} icon={s.icon} title={s.title} desc={s.desc} />
                        ))}
                    </div>
                </div>
            </section>

            {/* Pricing */}
            <section className="bg-white text-slate-800">
                <div className="max-w-6xl mx-auto px-4 py-16">
                    <h2 className="text-2xl md:text-3xl font-bold">{t.pricing.title}</h2>
                    <p className="text-slate-500 mt-2">{t.pricing.desc}</p>
                    <div className="grid md:grid-cols-3 gap-6 mt-8">
                        {displayData.pricingPlans[lang].map((p, i) => (
                            <div key={i} className="rounded-2xl border border-slate-200 p-6 bg-slate-50">
                                <div className={`text-sm ${accentText} font-semibold`}>{t.popular}</div>
                                <div className="mt-1 text-xl font-bold">{p.name}</div>
                                <div className="mt-2 text-3xl font-extrabold text-slate-900">
                                    {`AED ${p.amount}${p.perMonth ? `/${t.pricing.monthSuffix}` : ''}`}
                                </div>
                                <ul className="mt-4 space-y-2 text-sm text-slate-600">
                                    {p.features.map((f: string, j: number) => <li key={j}>• {f}</li>)}
                                </ul>
                                <button className={`mt-5 w-full px-4 py-2.5 rounded-xl ${primaryBg} text-white ${primaryHover}`}>{t.bookNow}</button>
                            </div>
                        ))}
                    </div>
                </div>
            </section>

            {role !== 'guest' && <Dashboard />}

            {/* Footer */}
            <footer className="bg-slate-900">
                <div className="max-w-6xl mx-auto px-4 py-10 text-slate-300 grid md:grid-cols-4 gap-8">
                    <div>
                        <div className="text-white font-semibold">{t.brand}</div>
                        <p className="text-sm mt-2 text-slate-400">{t.footer.city}</p>
                    </div>
                    <div>
                        <div className="text-white font-semibold mb-2">{t.footer.contact}</div>
                        <p className="text-sm">+971 2 555 1234</p>
                        <p className="text-sm">info@dentalclinic.ae</p>
                    </div>
                    <div>
                        <div className="text-white font-semibold mb-2">{t.footer.address}</div>
                        <p className="text-sm">Corniche Rd, Abu Dhabi, UAE</p>
                    </div>
                    <div>
                        <div className="text-white font-semibold mb-2">{t.footer.follow}</div>
                        <div className="flex gap-3 text-white/80 text-sm">
                            <a href="#">Facebook</a>
                            <a href="#">Instagram</a>
                            <a href="#">X</a>
                        </div>
                    </div>
                </div>
                <div className="border-t border-white/10 text-center text-white/60 text-sm py-4">{t.footer.rights}</div>
            </footer>

            {loginOpen && <LoginModal />}
        </div >
    )
}
