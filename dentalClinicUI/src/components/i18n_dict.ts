// src/i18n.ts
export type Lang = 'ar' | 'en' | 'hi'
export interface TStrings {
  langLabel: string;
  brand: string;
  nav: { home: string; services: string; doctors: string; pricing: string; contact: string };
  ui: { pickLanguage: string; chooseTheme: string; pickColor: string };
  langs: { ar: string; en: string; hi: string };
  themes: { emerald: string; sky: string; rose: string; violet: string; amber: string; dark: string };
  booking: { title: string; name: string; phone: string; date: string; service: string; confirm: string };
  svc: { checkupsT: string; checkupsD: string; whiteningT: string; whiteningD: string; orthoT: string; orthoD: string; implantsT: string; implantsD: string };
  doctors: { title: string; desc: string };
  services: { title: string; desc: string };
  pricing: { title: string; desc: string; monthSuffix: string };
  stats: { happy: string; years: string; rating: string };
  heroTitleA: string;
  heroTitleB: string;
  heroDesc: string;
  ctaBook: string;
  ctaView: string;
  dash: {
    medHistory: string; upcomingAppts: string; services: string;
    todaysAppts: string; patientRecords: string; quickNotes: string;
    userMgmt: string; dataMgmt: string; reports: string;
  };
  super: { settings: string; advanced: string; audit: string };
  roles: Record<string, string>;
  auth: {
    title: string; email: string; password: string; cancel: string; login: string; logout: string;
    startLogin: string; goDashboard: string; error: string;
  };
  footer: { city: string; contact: string; address: string; follow: string; rights: string };
  popular: string;
  bookNow: string;
  plans: Record<string, { name: string; features: string[] }>;
}

export const common = {
  monthSuffixMap: { ar: 'شهر', en: 'mo', hi: 'माह' } as Record<Lang, string>,
};

export const dict: Record<Lang, TStrings> = {
  ar: {
    langLabel: 'اللغة',
    brand: 'عيادة الأسنان',
    nav: { home: 'الرئيسية', services: 'الخدمات', doctors: 'الأطباء', pricing: 'الأسعار', contact: 'اتصل بنا' },
    bookNow: 'احجز الآن',
    heroTitleA: 'رعاية لطيفة من أجل',
    heroTitleB: 'ابتسامة أكثر إشراقًا',
    heroDesc: 'العناية السنية الحديثة: مواعيد في نفس اليوم، أسعار شفافة وفريق ودود. مفتوح 7 أيام في الأسبوع.',
    ctaBook: 'احجز موعدًا',
    ctaView: 'عرض الخدمات',
    stats: { happy: 'مرضى سعداء', years: 'سنوات من الرعاية', rating: 'متوسط التقييم' },
    services: { title: 'خدماتنا', desc: 'رعاية أسنان شاملة لكل الأعمار.' },
    svc: {
      checkupsT: 'فحوصات', checkupsD: 'فحوصات دورية وأشعة وتنظيف للحفاظ على ابتسامتك.',
      whiteningT: 'تبييض', whiteningD: 'تبييض احترافي لأسنان أكثر بياضًا في أقل من ساعة.',
      implantsT: 'زرعات', implantsD: 'خيارات متينة وطبيعية لتعويض الأسنان.',
      orthoT: 'تقويم الأسنان', orthoD: 'تقويم شفاف وبريسز مع خطط دفع مرنة.'
    },
    doctors: { title: 'تعرف على أطبائنا', desc: 'خبرة وتعاطف وتقييمات عالية.' },
    booking: { title: 'احجز موعدًا', name: 'الاسم الكامل', phone: 'الهاتف', date: 'التاريخ', service: 'الخدمة', confirm: 'تأكيد الحجز' },
    pricing: { title: 'أسعار شفافة', desc: 'بدون مفاجآت — رسوم واضحة ومسبقة.', monthSuffix: 'شهر' },
    plans: {
      p1: { name: 'فحص أساسي', features: ['فحص وأشعة', 'تنظيف احترافي', 'نفس اليوم'] },
      p2: { name: 'تبييض برو', features: ['جلسة داخل العيادة', 'عناية بالحساسية', 'طقم منزلي'] },
      p3: { name: 'خطة التقويم', features: ['مسح ثلاثي الأبعاد', 'متابعة شهرية', 'شروط مرنة'] }
    },
    popular: 'شائع',
    ui: { pickLanguage: 'اختر اللغة', chooseTheme: 'اختيار الألوان', pickColor: 'اختر اللون' },
    langs: { ar: 'العربية', en: 'English', hi: 'हिंदी' },
    themes: { emerald: 'أخضر', sky: 'سماوي', rose: 'وردي', violet: 'بنفسجي', amber: 'كهرماني', dark: 'داكن' },
    roles: { guest: 'زائر', patient: 'مريض', doctor: 'طبيب', admin: 'مدير', super: 'مشرف عام' },
    auth: { title: 'تسجيل الدخول', email: 'البريد الإلكتروني', password: 'كلمة المرور', login: 'دخول', cancel: 'إلغاء', error: 'فشل تسجيل الدخول', startLogin: 'ابدأ بتسجيل الدخول', goDashboard: 'اذهب إلى لوحة التحكم', logout: 'خروج' },
    super: { settings: 'إعدادات النظام', advanced: 'الصلاحيات المتقدمة', audit: 'سجل التدقيق' },
    dash: { medHistory: 'السجل الطبي', upcomingAppts: 'المواعيد القادمة', services: 'الخدمات', todaysAppts: 'مواعيد اليوم', patientRecords: 'سجل المرضى', quickNotes: 'ملاحظات سريعة', userMgmt: 'إدارة المستخدمين', dataMgmt: 'إدارة البيانات', reports: 'لوحة التقارير' },
    footer: { city: 'أبوظبي • يوميًا 9:00–21:00', contact: 'تواصل', address: 'العنوان', follow: 'تابعنا', rights: '© 2025 عيادة الأسنان. جميع الحقوق محفوظة.' }
  },
  en: {
    langLabel: 'Language',
    brand: 'DentalClinic',
    nav: { home: 'Home', services: 'Services', doctors: 'Doctors', pricing: 'Pricing', contact: 'Contact' },
    bookNow: 'Book Now',
    heroTitleA: 'Gentle care for a',
    heroTitleB: 'brighter',
    heroDesc: 'Modern dental care with same-day appointments, transparent pricing, and a friendly team. Open 7 days a week.',
    ctaBook: 'Book Appointment',
    ctaView: 'View Services',
    stats: { happy: 'Happy Patients', years: 'Years of Care', rating: 'Avg. Rating' },
    services: { title: 'Our Services', desc: 'Comprehensive dental care for every age.' },
    svc: {
      checkupsT: 'Checkups', checkupsD: 'Routine exams, x-rays, and cleanings to keep your smile healthy.',
      whiteningT: 'Whitening', whiteningD: 'Professional whitening for brighter teeth in under an hour.',
      implantsT: 'Implants', implantsD: 'Durable, natural-looking tooth replacement options.',
      orthoT: 'Orthodontics', orthoD: 'Clear aligners and braces with flexible payment plans.'
    },
    doctors: { title: 'Meet Our Doctors', desc: 'Experienced, compassionate, and highly rated.' },
    booking: { title: 'Book an Appointment', name: 'Full name', phone: 'Phone', date: 'Date', service: 'Service', confirm: 'Confirm Booking' },
    pricing: { title: 'Transparent Pricing', desc: 'No surprises—clear, upfront fees.', monthSuffix: 'mo' },
    plans: {
      p1: { name: 'Basic Checkup', features: ['Exam & X-ray', 'Pro Cleaning', 'Same-day'] },
      p2: { name: 'Whitening Pro', features: ['In-office session', 'Sensitivity care', 'Take-home kit'] },
      p3: { name: 'Aligner Plan', features: ['3D scan', 'Monthly checkups', 'Flexible terms'] }
    },
    popular: 'Popular',
    ui: { pickLanguage: 'Pick a language', chooseTheme: 'Choose theme', pickColor: 'Pick a color' },
    langs: { ar: 'العربية', en: 'English', hi: 'हिंदी' },
    themes: { emerald: 'Emerald', sky: 'Sky', rose: 'Rose', violet: 'Violet', amber: 'Amber', dark: 'Dark' },
    roles: { guest: 'Guest', patient: 'Patient', doctor: 'Doctor', admin: 'Admin', super: 'Super User' },
    auth: { title: 'Login', email: 'Email', password: 'Password', login: 'Login', cancel: 'Cancel', error: 'Login failed', startLogin: 'Start by Logging in', goDashboard: 'Go to Dashboard', logout: 'Logout' },
    super: { settings: 'System Configuration', advanced: 'Advanced Permissions', audit: 'Audit Log' },
    dash: { medHistory: 'Medical History', upcomingAppts: 'Upcoming Appointments', services: 'Services', todaysAppts: "Today's Appointments", patientRecords: 'Patient Records', quickNotes: 'Quick Notes', userMgmt: 'User Management', dataMgmt: 'Data Management', reports: 'Reports' },
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
      checkupsT: 'चेकअप', checkupsD: 'नियमित परीक्षण, एक्स-रे और सफाई।',
      whiteningT: 'व्हाइटनिंग', whiteningD: 'एक घंटे से कम में उज्जवल दांत।',
      implantsT: 'इम्प्लांट्स', implantsD: 'मजबूत और प्राकृतिक दिखने वाले विकल्प।',
      orthoT: 'ऑर्थोडॉन्टिक्स', orthoD: 'क्लियर एलाइनर्स और ब्रेसेस, लचीली किश्तें।'
    },
    doctors: { title: 'हमारे डॉक्टर्स से मिलें', desc: 'अनुभवी, सहानुभूतिपूर्ण और उच्च रेटिंग।' },
    booking: { title: 'अपॉइंटमेंट बुक करें', name: 'पूरा नाम', phone: 'फोन', date: 'तिथि', service: 'सेवा', confirm: 'बुकिंग कन्फर्म करें' },
    pricing: { title: 'पारदर्शी मूल्य', desc: 'कोई आश्चर्य नहीं—स्पष्ट और अग्रिम शुल्क।', monthSuffix: 'माह' },
    plans: {
      p1: { name: 'बेसिक चेकअप', features: ['परीक्षण और एक्स-रे', 'प्रो क्लीनिंग', 'उसी दिन'] },
      p2: { name: 'व्हाइटनिंग प्रो', features: ['इन-ऑफिस सत्र', 'संवेदनशीलता देखभाल', 'होम किट'] },
      p3: { name: 'एलाइनर योजना', features: ['3D स्कैन', 'मासिक चेकअप', 'लचीली शर्तें'] }
    },
    popular: 'लोकप्रिय',
    ui: { pickLanguage: 'भाषा चुनें', chooseTheme: 'थीम चुनें', pickColor: 'रंग चुनें' },
    langs: { ar: 'العربية', en: 'English', hi: 'हिंदी' },
    themes: { emerald: 'पन्ना', sky: 'आसमानी', rose: 'गुलाबी', violet: 'बैंगनी', amber: 'अंबर', dark: 'डार्क' },
    roles: { guest: 'अतिथि', patient: 'मरीज़', doctor: 'डॉक्टर', admin: 'प्रशासक', super: 'सुपर यूज़र' },
    auth: { title: 'लॉगिन', email: 'ईमेल', password: 'पासवर्ड', login: 'लॉगिन', cancel: 'रद्द करें', error: 'लॉगिन विफल', startLogin: 'लॉगिन से शुरू करें', goDashboard: 'डैशबोर्ड पर जाएँ', logout: 'लॉगआउट' },
    super: { settings: 'सिस्टम सेटिंग्स', advanced: 'उन्नत अनुमतियाँ', audit: 'ऑडिट लॉग' },
    dash: { medHistory: 'मेडिकल इतिहास', upcomingAppts: 'आगामी अपॉइंटमेंट', services: 'सेवाएँ', todaysAppts: 'आज की अपॉइंटमेंट', patientRecords: 'मरीज रिकॉर्ड', quickNotes: 'त्वरित नोट्स', userMgmt: 'यूज़र प्रबंधन', dataMgmt: 'डेटा प्रबंधन', reports: 'रिपोर्ट्स' },
    footer: { city: 'अबू धाबी • प्रतिदिन 9:00–21:00 खुला', contact: 'संपर्क', address: 'पता', follow: 'फॉलो', rights: '© 2025 डेंटल क्लिनिक। सर्वाधिकार सुरक्षित।' }
  }
} as const

//export type TStrings = (typeof dict)['en']
