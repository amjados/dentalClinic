// src/displayData.ts
import type { Lang } from './i18n_dict'
import { dict } from './i18n_dict'

export const displayData = {
  doctors: {
    ar: [
      { name: 'د. ماريا خان', role: 'تجميل الأسنان', img: 'https://images.unsplash.com/photo-1607746882042-944635dfe10e?q=80&w=600&auto=format&fit=crop', tags: ['تبييض', 'فينير'] },
      { name: 'د. أحمد صالح', role: 'تقويم الأسنان', img: 'https://images.unsplash.com/photo-1551836022-d5d88e9218df?q=80&w=600&auto=format&fit=crop', tags: ['تقويم شفاف', 'بريسز'] },
      { name: 'د. لارا يوسف', role: 'زراعة الأسنان', img: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?q=80&w=600&auto=format&fit=crop', tags: ['زرعات', 'جراحة'] },
      { name: 'د. عمر نصار', role: 'أسنان الأطفال', img: 'https://images.unsplash.com/photo-1547425260-76bcadfb4f2c?q=80&w=600&auto=format&fit=crop', tags: ['أطفال', 'وقائي'] },
    ],
    en: [
      { name: 'Dr. Maria Khan', role: 'Cosmetic Dentist', img: 'https://images.unsplash.com/photo-1607746882042-944635dfe10e?q=80&w=600&auto=format&fit=crop', tags: ['Whitening', 'Veneers'] },
      { name: 'Dr. Ahmed Saleh', role: 'Orthodontist', img: 'https://images.unsplash.com/photo-1551836022-d5d88e9218df?q=80&w=600&auto=format&fit=crop', tags: ['Aligners', 'Braces'] },
      { name: 'Dr. Lara Youssef', role: 'Implantologist', img: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?q=80&w=600&auto=format&fit=crop', tags: ['Implants', 'Surgery'] },
      { name: 'Dr. Omar Nassar', role: 'Pediatric Dentist', img: 'https://images.unsplash.com/photo-1547425260-76bcadfb4f2c?q=80&w=600&auto=format&fit=crop', tags: ['Kids', 'Preventive'] },
    ],
    hi: [
      { name: 'डॉ. मारिया खान', role: 'कॉस्मेटिक डेंटिस्ट', img: 'https://images.unsplash.com/photo-1607746882042-944635dfe10e?q=80&w=600&auto=format&fit=crop', tags: ['व्हाइटनिंग', 'विनियर्स'] },
      { name: 'डॉ. अहमद सालेह', role: 'ऑर्थोडॉन्टिस्ट', img: 'https://images.unsplash.com/photo-1551836022-d5d88e9218df?q=80&w=600&auto=format&fit=crop', tags: ['एलाइनर्स', 'ब्रेसेस'] },
      { name: 'डॉ. लारा यूसुफ', role: 'इम्प्लांटोलॉजिस्ट', img: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?q=80&w=600&auto=format&fit=crop', tags: ['इम्प्लांट्स', 'सर्जरी'] },
      { name: 'डॉ. ओमर नास्सार', role: 'पीडियाट्रिक डेंटिस्ट', img: 'https://images.unsplash.com/photo-1547425260-76bcadfb4f2c?q=80&w=600&auto=format&fit=crop', tags: ['बच्चे', 'निवारक'] },
    ],
  },
  pricingPlans: {
    ar: [
      { name: dict.ar.plans.p1.name, amount: 199, perMonth: false, features: dict.ar.plans.p1.features },
      { name: dict.ar.plans.p2.name, amount: 799, perMonth: false, features: dict.ar.plans.p2.features },
      { name: dict.ar.plans.p3.name, amount: 299, perMonth: true, features: dict.ar.plans.p3.features },
    ],
    en: [
      { name: dict.en.plans.p1.name, amount: 199, perMonth: false, features: dict.en.plans.p1.features },
      { name: dict.en.plans.p2.name, amount: 799, perMonth: false, features: dict.en.plans.p2.features },
      { name: dict.en.plans.p3.name, amount: 299, perMonth: true, features: dict.en.plans.p3.features },
    ],
    hi: [
      { name: dict.hi.plans.p1.name, amount: 199, perMonth: false, features: dict.hi.plans.p1.features },
      { name: dict.hi.plans.p2.name, amount: 799, perMonth: false, features: dict.hi.plans.p2.features },
      { name: dict.hi.plans.p3.name, amount: 299, perMonth: true, features: dict.hi.plans.p3.features },
    ],
  },
  services: {
    ar: [
      { icon: '🪥', title: dict.ar.svc.checkupsT, desc: dict.ar.svc.checkupsD },
      { icon: '✨', title: dict.ar.svc.whiteningT, desc: dict.ar.svc.whiteningD },
      { icon: '🦷', title: dict.ar.svc.implantsT, desc: dict.ar.svc.implantsD },
      { icon: '😬', title: dict.ar.svc.orthoT, desc: dict.ar.svc.orthoD },
    ],
    en: [
      { icon: '🪥', title: dict.en.svc.checkupsT, desc: dict.en.svc.checkupsD },
      { icon: '✨', title: dict.en.svc.whiteningT, desc: dict.en.svc.whiteningD },
      { icon: '🦷', title: dict.en.svc.implantsT, desc: dict.en.svc.implantsD },
      { icon: '😬', title: dict.en.svc.orthoT, desc: dict.en.svc.orthoD },
    ],
    hi: [
      { icon: '🪥', title: dict.hi.svc.checkupsT, desc: dict.hi.svc.checkupsD },
      { icon: '✨', title: dict.hi.svc.whiteningT, desc: dict.hi.svc.whiteningD },
      { icon: '🦷', title: dict.hi.svc.implantsT, desc: dict.hi.svc.implantsD },
      { icon: '😬', title: dict.hi.svc.orthoT, desc: dict.hi.svc.orthoD },
    ],
  },
} as const

  // Dev sanity checks
  ; (['ar', 'en', 'hi'] as Lang[]).forEach(L => {
    const docs = (displayData as any).doctors[L]
    const plans = (displayData as any).pricingPlans[L]
    const svcs = (displayData as any).services[L]
    console.assert(Array.isArray(docs) && docs.length === 4, `${L}: doctors length != 4`)
    console.assert(Array.isArray(plans) && plans.length === 3, `${L}: plans length != 3`)
    console.assert(Array.isArray(svcs) && svcs.length === 4, `${L}: services length != 4`)
  })
