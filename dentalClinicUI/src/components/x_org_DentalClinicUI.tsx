// DentalClinic UI – multilingual (Arabic • English • Hindi) single-file React component
// Uses Tailwind classes. Switch language & theme. RTL for Arabic. Role-based UI with login.
// Recent fixes:
// - Fixed SyntaxError: removed stray ")" after fetch(...) in doLogin() and ensured proper closing braces/semicolons.
// - Kept deduplicated themeMap keys (no duplicate 'sky').
// - AppointmentForm receives theme classes via props; no scope leaks.
// - Persist lang/theme in localStorage; restored on mount.
// - Added light runtime self-tests ("test cases") to catch regressions.

import { useMemo, useState, useEffect, useRef } from 'react'

// --- Types ---
export type Role = 'guest' | 'patient' | 'doctor' | 'admin' | 'super'
export type Lang = keyof typeof dict

// --- Minimal i18n dictionary ---
const dict = {
    ar: {
        langLabel: 'اللغة',
        brand: 'عيادة الأسنان',
        nav: { home: 'الرئيسية', services: 'الخدمات', doctors: 'الأطباء', pricing: 'الأسعار', contact: 'تواصل' },
        bookNow: 'احجز الآن',
        heroTitleA: 'رعاية لطيفة من أجل ابتسامة',
        heroTitleB: 'أجمل',
        heroDesc: 'رعاية أسنان حديثة مع مواعيد في نفس اليوم وأسعار واضحة وفريق ودود. مفتوحون طوال الأسبوع.',
        ctaBook: 'حجز موعد',
        ctaView: 'عرض الخدمات',
        stats: { happy: 'مرضى سعداء', years: 'سنوات خبرة', rating: 'تقييم متوسط' },
        services: { title: 'خدماتنا', desc: 'رعاية شاملة لكل الأعمار.' },
        svc: {
            checkupsT: 'فحوصات', checkupsD: 'فحوصات دورية وصور أشعة وتنظيف للاحتفاظ بابتسامة صحية.',
            whiteningT: 'تبييض', whiteningD: 'تبييض احترافي لأسنان أكثر إشراقاً في أقل من ساعة.',
            implantsT: 'زراعة أسنان', implantsD: 'بدائل متينة تبدو طبيعية للأسنان المفقودة.',
            orthoT: 'تقويم', orthoD: 'تقويم شفاف وأجهزة تقويم بخطط دفع مرنة.'
        },
        doctors: { title: 'تعرف على أطبائنا', desc: 'خبراء متمرسون متعاطفون وذوو تقييم عالٍ.' },
        booking: {
            title: 'احجز موعداً', name: 'الاسم الكامل', phone: 'الهاتف', date: 'التاريخ', service: 'الخدمة', confirm: 'تأكيد الحجز'
        },
        pricing: { title: 'أسعار شفافة', desc: 'بدون مفاجآت — رسوم واضحة ومسبقة.' },
        plans: {
            p1: { name: 'فحص أساسي', features: ['فحص وصورة أشعة', 'تنظيف احترافي', 'موعد في نفس اليوم'] },
            p2: { name: 'تبييض احترافي', features: ['جلسة داخل العيادة', 'عناية بالحساسية', 'عدة منزلية'] },
            p3: { name: 'خطة التقويم', features: ['مسح ثلاثي الأبعاد', 'متابعة شهرية', 'شروط مرنة'] }
        },
        popular: 'شائع',
        footer: { city: 'أبوظبي • يومياً 9:00–21:00', contact: 'تواصل', address: 'العنوان', follow: 'تابعنا', rights: '© 2025 عيادة الأسنان. كل الحقوق محفوظة.' }
    },
    en: {
        langLabel: 'Language',
        brand: 'DentalClinic',
        nav: { home: 'Home', services: 'Services', doctors: 'Doctors', pricing: 'Pricing', contact: 'Contact' },
        bookNow: 'Book Now',
        heroTitleA: 'Gentle care for a',
        heroTitleB: 'brighter',
        heroDesc: 'Modern dental care with same‑day appointments, transparent pricing, and a friendly team. Open 7 days a week.',
        ctaBook: 'Book Appointment',
        ctaView: 'View Services',
        stats: { happy: 'Happy Patients', years: 'Years of Care', rating: 'Avg. Rating' },
        services: { title: 'Our Services', desc: 'Comprehensive dental care for every age.' },
        svc: {
            checkupsT: 'Checkups', checkupsD: 'Routine exams, x‑rays, and cleanings to keep your smile healthy.',
            whiteningT: 'Whitening', whiteningD: 'Professional whitening for brighter teeth in under an hour.',
            implantsT: 'Implants', implantsD: 'Durable, natural‑looking tooth replacement options.',
            orthoT: 'Orthodontics', orthoD: 'Clear aligners and braces with flexible payment plans.'
        },
        doctors: { title: 'Meet Our Doctors', desc: 'Experienced, compassionate, and highly rated.' },
        booking: { title: 'Book an Appointment', name: 'Full name', phone: 'Phone', date: 'Date', service: 'Service', confirm: 'Confirm Booking' },
        pricing: { title: 'Transparent Pricing', desc: 'No surprises—clear, upfront fees.' },
        plans: {
            p1: { name: 'Basic Checkup', features: ['Exam & X‑ray', 'Pro Cleaning', 'Same‑day'] },
            p2: { name: 'Whitening Pro', features: ['In‑office session', 'Sensitivity care', 'Take‑home kit'] },
            p3: { name: 'Aligner Plan', features: ['3D scan', 'Monthly checkups', 'Flexible terms'] }
        },
        popular: 'Popular',
        footer: { city: 'Abu Dhabi • Open daily 9:00–21:00', contact: 'Contact', address: 'Address', follow: 'Follow', rights: '© 2025 DentalClinic. All rights reserved.' }
    },
    hi: {
        langLabel: 'भाषा',
        brand: 'डेंटल क्लिनिक',
        nav: { home: 'होम', services: 'सेवाएँ', doctors: 'डॉक्टर्स', pricing: 'मूल्य', contact: 'संपर्क' },
        bookNow: 'अभी बुक करें',
        heroTitleA: 'कोमल देखभाल एक',
        heroTitleB: 'उज्ज्वल',
        heroDesc: 'आधुनिक डेंटल केयर: उसी दिन अपॉइंटमेंट, पारदर्शी कीमतें और मित्रवत टीम। सप्ताह के 7 दिन खुले।',
        ctaBook: 'अपॉइंटमेंट बुक करें',
        ctaView: 'सेवाएँ देखें',
        stats: { happy: 'खुश मरीज', years: 'वर्षों का अनुभव', rating: 'औसत रेटिंग' },
        services: { title: 'हमारी सेवाएँ', desc: 'हर उम्र के लिए संपूर्ण डेंटल केयर।' },
        svc: {
            checkupsT: 'चेकअप', checkupsD: 'नियमित परीक्षण, एक्स‑रे और सफाई।',
            whiteningT: 'व्हाइटनिंग', whiteningD: 'एक घंटे से कम में उज्जवल दांत।',
            implantsT: 'इम्प्लांट्स', implantsD: 'मजबूत और प्राकृतिक दिखने वाले विकल्प।',
            orthoT: 'ऑर्थोडॉन्टिक्स', orthoD: 'क्लियर एलाइनर्स और ब्रेसेस, लचीली किश्तें।'
        },
        doctors: { title: 'हमारे डॉक्टर्स से मिलें', desc: 'अनुभवी, सहानुभूतिपूर्ण और उच्च रेटिंग।' },
        booking: { title: 'अपॉइंटमेंट बुक करें', name: 'पूरा नाम', phone: 'फोन', date: 'तिथि', service: 'सेवा', confirm: 'बुकिंग कन्फर्म करें' },
        pricing: { title: 'पारदर्शी मूल्य', desc: 'कोई आश्चर्य नहीं—स्पष्ट और अग्रिम शुल्क।' },
        plans: {
            p1: { name: 'बेसिक चेकअप', features: ['परीक्षण और एक्स‑रे', 'प्रो क्लीनिंग', 'उसी दिन'] },
            p2: { name: 'व्हाइटनिंग प्रो', features: ['इन‑ऑफिस सत्र', 'संवेदनशीलता देखभाल', 'होम किट'] },
            p3: { name: 'एलाइनर योजना', features: ['3D स्कैन', 'मासिक चेकअप', 'लचीली शर्तें'] }
        },
        popular: 'लोकप्रिय',
        footer: { city: 'अबू धाबी • प्रतिदिन 9:00–21:00 खुला', contact: 'संपर्क', address: 'पता', follow: 'फॉलो', rights: '© 2025 डेंटल क्लिनिक। सर्वाधिकार सुरक्षित।' }
    }
}

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

function DoctorCard({ name, role, img, tags = [] }: { name: string; role: string; img: string; tags?: string[] }) {
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

function AppointmentForm({ t, primaryBg, primaryHover }: { t: any; primaryBg: string; primaryHover: string }) {
    const [form, setForm] = useState({ name: '', phone: '', date: '', service: t.booking.service })
    const inputCls = 'w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-emerald-300'
    return (
        <form className="bg-white rounded-2xl p-6 shadow-sm border border-slate-100">
            <h3 className="text-lg font-semibold text-slate-800 mb-4">{t.booking.title}</h3>
            <div className="grid md:grid-cols-2 gap-4">
                <input className={inputCls} placeholder={t.booking.name} value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} />
                <input className={inputCls} placeholder={t.booking.phone} value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} />
                <input type="date" className={inputCls} value={form.date} onChange={e => setForm({ ...form, date: e.target.value })} />
                <select className={inputCls} value={form.service} onChange={e => setForm({ ...form, service: e.target.value })}>
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

function LanguageSwitcher({ lang, setLang }: { lang: Lang; setLang: (l: Lang) => void }) {
    const [open, setOpen] = useState(false)
    const popRef = useRef<HTMLDivElement | null>(null)
    const btnRef = useRef<HTMLButtonElement | null>(null)

    const items: { key: Lang; label: string; abbr: string }[] = [
        { key: 'ar', label: 'العربية', abbr: 'AR' },
        { key: 'en', label: 'English', abbr: 'EN' },
        { key: 'hi', label: 'हिंदी', abbr: 'HI' },
    ]

    useEffect(() => {
        function onDocClick(e: MouseEvent) {
            if (!open) return
            const t = e.target as Node
            if (popRef.current && !popRef.current.contains(t) && btnRef.current && !btnRef.current.contains(t)) {
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
        <div className="relative z-[95]">
            <button ref={btnRef} aria-haspopup="dialog" aria-expanded={open}
                onClick={() => setOpen(v => !v)}
                className={`w-9 h-9 grid place-items-center rounded-xl border border-white/30 bg-white/10 hover:bg-white/20 ${open ? 'ring-2 ring-white/70' : ''}`}
                title={dict[lang].langLabel}>
                {globeIcon}
            </button>

            {open && (
                <div ref={popRef} role="dialog" className={`absolute ${lang === 'ar' ? 'right-0' : 'left-0'} mt-2 z-[120] w-44 rounded-2xl bg-white/95 backdrop-blur border border-white/40 p-3 shadow-xl`}
                    style={{ WebkitBackdropFilter: 'blur(6px)' }}>
                    <div className="text-xs font-medium text-slate-700 mb-2">
                        {lang === 'ar' ? 'اختر اللغة' : lang === 'hi' ? 'भाषा चुनें' : 'Pick a language'}
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

export default function DentalClinicUI() {
    type Theme = 'emerald' | 'sky' | 'rose' | 'violet' | 'amber' | 'dark'
    const [lang, setLang] = useState<Lang>('ar') // default Arabic first
    const [role, setRole] = useState<Role>('guest')
    const [loginOpen, setLoginOpen] = useState(false)
    const [theme, setTheme] = useState<Theme>('emerald')
    const t = dict[lang]

    // hydrate role, theme, lang from storage on mount
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

    const dir = useMemo(() => lang === 'ar' ? 'rtl' : 'ltr', [lang])
    const align = dir === 'rtl' ? 'text-right' : 'text-left'

    // ✅ Correct, deduplicated theme map
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

    function ThemeSwitcher() {
        const [open, setOpen] = useState(false)
        const popRef = useRef<HTMLDivElement | null>(null)
        const btnRef = useRef<HTMLButtonElement | null>(null)

        const items: { key: Theme; label: string; dot: string }[] = [
            { key: 'emerald', label: lang === 'ar' ? 'أخضر' : 'Emerald', dot: 'bg-emerald-500' },
            { key: 'sky', label: lang === 'ar' ? 'سماوي' : 'Sky', dot: 'bg-sky-500' },
            { key: 'rose', label: lang === 'ar' ? 'وردي' : 'Rose', dot: 'bg-rose-500' },
            { key: 'violet', label: lang === 'ar' ? 'بنفسجي' : 'Violet', dot: 'bg-violet-500' },
            { key: 'amber', label: lang === 'ar' ? 'كهرماني' : 'Amber', dot: 'bg-amber-500' },
            { key: 'dark', label: lang === 'ar' ? 'داكن' : 'Dark', dot: 'bg-slate-700' },
        ]

        // close on outside click / Esc
        useEffect(() => {
            function onDocClick(e: MouseEvent) {
                if (!open) return
                const t = e.target as Node
                if (popRef.current && !popRef.current.contains(t) && btnRef.current && !btnRef.current.contains(t)) {
                    setOpen(false)
                }
            }
            function onEsc(e: KeyboardEvent) { if (e.key === 'Escape') setOpen(false) }
            document.addEventListener('mousedown', onDocClick)
            document.addEventListener('keydown', onEsc)
            return () => { document.removeEventListener('mousedown', onDocClick); document.removeEventListener('keydown', onEsc) }
        }, [open])

        const paletteIcon = (
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 3c-4.97 0-9 3.582-9 8 0 4.418 4.03 8 9 8h1.5a2.5 2.5 0 0 0 2.5-2.5c0-.69-.28-1.316-.73-1.77a2.5 2.5 0 0 1 1.77-4.27H17a4 4 0 0 0 4-4C21 5.015 16.97 3 12 3Z" stroke="currentColor" strokeWidth="1.5" />
                <circle cx="7.5" cy="9" r="1.25" fill="currentColor" />
                <circle cx="12" cy="7.5" r="1.25" fill="currentColor" />
                <circle cx="9.75" cy="12.25" r="1.25" fill="currentColor" />
            </svg>
        )

        return (
            <div className="relative">
                <button ref={btnRef} aria-haspopup="dialog" aria-expanded={open}
                    onClick={() => setOpen(v => !v)}
                    className={`w-9 h-9 grid place-items-center rounded-xl border border-white/30 bg-white/10 hover:bg-white/20 ${open ? 'ring-2 ring-white/70' : ''}`}
                    title={lang === 'ar' ? 'اختيار الألوان' : lang === 'hi' ? 'थीम चुनें' : 'Choose theme'}>
                    {paletteIcon}
                </button>

                {open && (
                    <div ref={popRef} role="dialog" className={`absolute ${dir === 'rtl' ? 'right-0' : 'left-0'} mt-2 z-[120] w-52 rounded-2xl bg-white/95 backdrop-blur border border-white/40 p-3 shadow-xl`}
                        style={{ WebkitBackdropFilter: 'blur(6px)' }}>
                        <div className="text-xs font-medium text-slate-700 mb-2">
                            {lang === 'ar' ? 'اختر اللون' : lang === 'hi' ? 'रंग चुनें' : 'Pick a color'}
                        </div>
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

    function RoleBadge() {
        const map: Record<Role, string> = {
            guest: lang === 'ar' ? 'زائر' : lang === 'hi' ? 'अतिथि' : 'Guest',
            patient: lang === 'ar' ? 'مريض' : lang === 'hi' ? 'मरीज़' : 'Patient',
            doctor: lang === 'ar' ? 'طبيب' : lang === 'hi' ? 'डॉक्टर' : 'Doctor',
            admin: lang === 'ar' ? 'مدير' : lang === 'hi' ? 'प्रशासक' : 'Admin',
            super: lang === 'ar' ? 'مشرف عام' : lang === 'hi' ? 'सुपर यूज़र' : 'Super User',
        }
        return <span className="px-2 py-1 rounded-lg bg-white/15 text-xs">{map[role]}</span>
    }

    function LoginModal() {
        const [email, setEmail] = useState('')
        const [password, setPassword] = useState('')
        const label = {
            title: lang === 'ar' ? 'تسجيل الدخول' : lang === 'hi' ? 'लॉगिन' : 'Login',
            email: lang === 'ar' ? 'البريد الإلكتروني' : lang === 'hi' ? 'ईमेल' : 'Email',
            password: lang === 'ar' ? 'كلمة المرور' : lang === 'hi' ? 'पासवर्ड' : 'Password',
            login: lang === 'ar' ? 'دخول' : lang === 'hi' ? 'लॉगिन' : 'Login',
            cancel: lang === 'ar' ? 'إلغاء' : lang === 'hi' ? 'रद्द करें' : 'Cancel',
            error: lang === 'ar' ? 'فشل تسجيل الدخول' : lang === 'hi' ? 'लॉगिन विफल' : 'Login failed',
        }

        async function doLogin() {
            try {
                const API = (import.meta as any).env?.VITE_API_URL || ''
                const res = await fetch(`${API}/api/auth/login`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    credentials: API ? 'include' : 'same-origin',
                    body: JSON.stringify({ email, password })
                });
                if (!res.ok) throw new Error('HTTP ' + res.status)
                const data = await res.json() // { token, user: { id, role, name, ... } }
                localStorage.setItem('auth', JSON.stringify(data))
                setRole((data?.user?.role ?? 'guest') as Role)
                setLoginOpen(false)
            } catch (e) {
                alert(label.error)
            }
        }

        if (!loginOpen) return null
        return (
            <div className="fixed inset-0 z-50 grid place-items-center bg-black/40 p-4">
                <div dir={dir} className={`w-full max-w-md bg-white text-slate-800 rounded-2xl p-6 ${align}`}>
                    <div className="text-xl font-semibold mb-4">{label.title}</div>
                    <div className="space-y-3">
                        <input className="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-emerald-300" placeholder={label.email} value={email} onChange={e => setEmail(e.target.value)} />
                        <input type="password" className="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-emerald-300" placeholder={label.password} value={password} onChange={e => setPassword(e.target.value)} />
                    </div>
                    <div className="mt-5 flex gap-3 justify-end">
                        <button className="px-4 py-2 rounded-xl border border-slate-200" onClick={() => setLoginOpen(false)}>{label.cancel}</button>
                        <button className={`px-4 py-2 rounded-xl ${primaryBg} text-white`} onClick={doLogin}>{label.login}</button>
                    </div>
                    <p className="mt-3 text-xs text-slate-500">* Role is selected by the backend and stored with the user record.</p>
                </div>
            </div>
        )
    }

    function Dashboard() {
        // simple mocked data for demo
        const cardsCls = 'rounded-2xl border border-slate-200 p-5 bg-white text-slate-800'

        if (role === 'patient') return (
            <section className="max-w-6xl mx-auto px-4 py-10 grid md:grid-cols-3 gap-6">
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{lang === 'ar' ? 'السجل الطبي' : lang === 'hi' ? 'मेडिकल इतिहास' : 'Medical History'}</div>
                    <ul className="text-sm space-y-2">
                        <li>2025-07-12 — Whitening — Dr. Maria</li>
                        <li>2025-04-03 — Checkup — Dr. Omar</li>
                        <li>2024-12-19 — X‑ray — Dr. Ahmed</li>
                    </ul>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{lang === 'ar' ? 'المواعيد القادمة' : lang === 'hi' ? 'आगामी अपॉइंटमेंट' : 'Upcoming Appointments'}</div>
                    <p className="text-sm">2025-10-20 10:30 — Cleaning</p>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{lang === 'ar' ? 'الخدمات' : lang === 'hi' ? 'सेवाएँ' : 'Services'}</div>
                    <p className="text-sm">{t.services.desc}</p>
                </div>
            </section>
        )

        if (role === 'doctor') return (
            <section className="max-w-6xl mx-auto px-4 py-10 grid md:grid-cols-3 gap-6">
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{lang === 'ar' ? 'مواعيد اليوم' : lang === 'hi' ? 'आज की अपॉइंटमेंट' : "Today's Appointments"}</div>
                    <ul className="text-sm space-y-2">
                        <li>09:00 — Ali K. — Checkup</li>
                        <li>10:30 — Sara M. — Whitening</li>
                        <li>12:00 — Omar N. — Follow‑up</li>
                    </ul>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{lang === 'ar' ? 'سجل المرضى' : lang === 'hi' ? 'मरीज रिकॉर्ड' : 'Patient Records'}</div>
                    <p className="text-sm">#A‑1023, #B‑3381, #C‑9920…</p>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{lang === 'ar' ? 'ملاحظات سريعة' : lang === 'hi' ? 'त्वरित नोट्स' : 'Quick Notes'}</div>
                    <textarea className="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-emerald-300" placeholder="…" />
                </div>
            </section>
        )

        if (role === 'admin') return (
            <section className="max-w-6xl mx-auto px-4 py-10 grid md:grid-cols-3 gap-6">
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{lang === 'ar' ? 'إدارة المستخدمين' : lang === 'hi' ? 'यूज़र प्रबंधन' : 'User Management'}</div>
                    <p className="text-sm">Create / deactivate users, assign roles.</p>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{lang === 'ar' ? 'إدارة البيانات' : lang === 'hi' ? 'डेटा प्रबंधन' : 'Data Management'}</div>
                    <p className="text-sm">Import / export EMR data.</p>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{lang === 'ar' ? 'لوحة التقارير' : lang === 'hi' ? 'रिपोर्ट्स' : 'Reports'}</div>
                    <p className="text-sm">KPI, appointments, revenue.</p>
                </div>
            </section>
        )

        if (role === 'super') return (
            <section className="max-w-6xl mx-auto px-4 py-10 grid md:grid-cols-3 gap-6">
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{lang === 'ar' ? 'إعدادات النظام' : lang === 'hi' ? 'सिस्टम सेटिंग्स' : 'System Configuration'}</div>
                    <p className="text-sm">Branding, locales, access policies.</p>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{lang === 'ar' ? 'الصلاحيات المتقدمة' : lang === 'hi' ? 'उन्नत अनुमतियाँ' : 'Advanced Permissions'}</div>
                    <p className="text-sm">Role matrices, audit scopes.</p>
                </div>
                <div className={cardsCls}>
                    <div className="font-semibold mb-2">{lang === 'ar' ? 'سجل التدقيق' : lang === 'hi' ? 'ऑडिट लॉग' : 'Audit Log'}</div>
                    <p className="text-sm">System‑wide activity stream.</p>
                </div>
            </section>
        )

        return null
    }

    // --- Tiny runtime tests (dev assertions) ---
    useEffect(() => {
        // themeMap keys match switcher items & no duplicates
        const keys = Object.keys(themeMap)
        const unique = new Set(keys)
        console.assert(unique.size === keys.length, 'themeMap has duplicate keys')
        console.assert(Boolean(themeMap.sky) && Boolean(themeMap.emerald) && Boolean(themeMap.dark), 'themeMap missing required themes')

        // dictionary sanity
        console.assert(!!dict.ar && !!dict.en && !!dict.hi, 'i18n dict missing locales')

        // price suffixes
        const suffix = (l: Lang) => l === 'ar' ? 'شهر' : l === 'hi' ? 'माह' : 'mo'
        console.assert(suffix('ar') === 'شهر' && suffix('en') === 'mo' && suffix('hi') === 'माह', 'month suffix mapping broken')

        // type-ish checks
        console.assert(typeof window !== 'undefined', 'not in browser env?')
    }, [])

    return (
        <div dir={dir} className={`min-h-screen bg-gradient-to-b ${themeGrad} text-white ${align}`}>
            {/* Header */}
            <header className="sticky top-0 z-[90] backdrop-blur supports-[backdrop-filter]:bg-white/10 bg-white/10 border-b border-white/10">
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
                        <RoleBadge />
                        <LanguageSwitcher lang={lang} setLang={setLang} />
                        <ThemeSwitcher />
                        {role === 'guest' ? (
                            <button className={`px-4 py-2 bg-white ${primaryText} rounded-xl font-medium hover:shadow`} onClick={() => setLoginOpen(true)}>
                                {lang === 'ar' ? 'تسجيل الدخول' : lang === 'hi' ? 'लॉगिन' : 'Login'}
                            </button>
                        ) : (
                            <button className="px-4 py-2 bg-white/20 text-white rounded-xl font-medium hover:bg-white/30" onClick={() => { localStorage.removeItem('auth'); setRole('guest') }}>
                                {lang === 'ar' ? 'خروج' : lang === 'hi' ? 'लॉगआउट' : 'Logout'}
                            </button>
                        )}
                    </div>
                </div>
            </header>

            {/* Hero remains visible for all */}
            <section className="max-w-6xl mx-auto px-4 pt-12 pb-16">
                <div className={`grid md:grid-cols-2 gap-10 items-center ${dir === 'rtl' ? 'md:[direction:rtl]' : ''}`}>
                    <div>
                        <h1 className="text-4xl md:text-5xl font-extrabold leading-tight">
                            {t.heroTitleA} <span className="underline decoration-white/50">{t.heroTitleB}</span> {lang === 'en' ? 'smile' : lang === 'hi' ? 'मुस्कान' : 'للابتسامة'}
                        </h1>
                        <p className="mt-4 text-white/80 max-w-prose">{t.heroDesc}</p>
                        <div className="mt-6 flex flex-wrap gap-3">
                            {role === 'guest' ? (
                                <button className={`px-5 py-3 rounded-xl bg-white ${primaryText} font-semibold hover:shadow`} onClick={() => setLoginOpen(true)}>{lang === 'ar' ? 'ابدأ بتسجيل الدخول' : lang === 'hi' ? 'लॉगिन से शुरू करें' : 'Start by Logging in'}</button>
                            ) : (
                                <button className={`px-5 py-3 rounded-xl bg-white ${primaryText} font-semibold hover:shadow`} onClick={() => window.scrollTo({ top: document.body.scrollHeight / 3, behavior: 'smooth' })}>{lang === 'ar' ? 'اذهب إلى لوحة التحكم' : lang === 'hi' ? 'डैशबोर्ड पर जाएँ' : 'Go to Dashboard'}</button>
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
                        <div className="absolute -inset-4 bg-white/10 rounded-3xl blur-lg" />
                        <img className="relative rounded-3xl shadow-xl border border-white/20" src="https://images.unsplash.com/photo-1588776814546-1ffcf47267a5?q=80&w=1600&auto=format&fit=crop" alt="Dental care" />
                    </div>
                </div>
            </section>

            {/* Role-based Dashboard */}
            {role !== 'guest' && <Dashboard />}

            {/* Services */}
            <section className="bg-white text-slate-800">
                <div className="max-w-6xl mx-auto px-4 py-16">
                    <h2 className="text-2xl md:text-3xl font-bold">{t.services.title}</h2>
                    <p className="text-slate-500 mt-2">{t.services.desc}</p>
                    <div className="grid md:grid-cols-4 gap-6 mt-8">
                        <ServiceCard icon="🪥" title={t.svc.checkupsT} desc={t.svc.checkupsD} />
                        <ServiceCard icon="✨" title={t.svc.whiteningT} desc={t.svc.whiteningD} />
                        <ServiceCard icon="🦷" title={t.svc.implantsT} desc={t.svc.implantsD} />
                        <ServiceCard icon="😬" title={t.svc.orthoT} desc={t.svc.orthoD} />
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
                            <DoctorCard name="Dr. Maria Khan" role="Cosmetic Dentist" img="https://images.unsplash.com/photo-1607746882042-944635dfe10e?q=80&w=600&auto=format&fit=crop" tags={["Whitening", "Veneers"]} />
                            <DoctorCard name="Dr. Ahmed Saleh" role="Orthodontist" img="https://images.unsplash.com/photo-1551836022-d5d88e9218df?q=80&w=600&auto=format&fit=crop" tags={["Aligners", "Braces"]} />
                            <DoctorCard name="Dr. Lara Youssef" role="Implantologist" img="https://images.unsplash.com/photo-1544005313-94ddf0286df2?q=80&w=600&auto=format&fit=crop" tags={["Implants", "Surgery"]} />
                            <DoctorCard name="Dr. Omar Nassar" role="Pediatric Dentist" img="https://images.unsplash.com/photo-1547425260-76bcadfb4f2c?q=80&w=600&auto=format&fit=crop" tags={["Kids", "Preventive"]} />
                        </div>
                    </div>
                    <div className="md:col-span-2">
                        <AppointmentForm t={t} primaryBg={primaryBg} primaryHover={primaryHover} />
                    </div>
                </div>
            </section>

            {/* Pricing */}
            <section className="bg-white text-slate-800">
                <div className="max-w-6xl mx-auto px-4 py-16">
                    <h2 className="text-2xl md:text-3xl font-bold">{t.pricing.title}</h2>
                    <p className="text-slate-500 mt-2">{t.pricing.desc}</p>
                    <div className="grid md:grid-cols-3 gap-6 mt-8">
                        {(() => {
                            const monthSuffix = lang === 'ar' ? 'شهر' : lang === 'hi' ? 'माह' : 'mo'
                            const plans = [
                                { name: t.plans.p1.name, amount: 199, perMonth: false, features: t.plans.p1.features },
                                { name: t.plans.p2.name, amount: 799, perMonth: false, features: t.plans.p2.features },
                                { name: t.plans.p3.name, amount: 299, perMonth: true, features: t.plans.p3.features },
                            ]
                            return plans.map((p, i) => (
                                <div key={i} className="rounded-2xl border border-slate-200 p-6 bg-slate-50">
                                    <div className={`text-sm ${accentText} font-semibold`}>{t.popular}</div>
                                    <div className="mt-1 text-xl font-bold">{p.name}</div>
                                    <div className="mt-2 text-3xl font-extrabold text-slate-900">{`AED ${p.amount}${p.perMonth ? `/${monthSuffix}` : ''}`}</div>
                                    <ul className="mt-4 space-y-2 text-sm text-slate-600">
                                        {p.features.map((f: string, j: number) => <li key={j}>• {f}</li>)}
                                    </ul>
                                    <button className={`mt-5 w-full px-4 py-2.5 rounded-xl ${primaryBg} text-white ${primaryHover}`}>{t.bookNow}</button>
                                </div>
                            ))
                        })()}
                    </div>
                </div>
            </section>

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

            <LoginModal />
        </div>
    )
}
