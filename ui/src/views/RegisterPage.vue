<template>
  <div class="surf-app">
    <div class="ocean-bg">
      <div class="wave wave-1"></div>
      <div class="wave wave-2"></div>
      <div class="wave wave-3"></div>
      <div class="foam-particles">
        <span v-for="i in 18" :key="i" class="foam" :style="foamStyle(i)"></span>
      </div>
    </div>
    <div class="surf-deco">
      <div class="board board-1">🏄</div>
      <div class="board board-2">🌊</div>
      <div class="board board-3">☀️</div>
    </div>
    <div class="register-wrapper">
      <div class="card" :class="{ 'card--success': success, 'card--shake': shake }">
        <div class="card__header">
          <div class="logo">
            <span class="logo__wave">〜</span>
            <span class="logo__text">WAVECRAFT</span>
            <span class="logo__wave">〜</span>
          </div>
          <p class="card__tagline">Ride the perfect wave. Gear up.</p>
        </div>
        <transition name="fade">
          <div v-if="success" class="success-screen">
            <div class="success-icon">🤙</div>
            <h2>Ти в справі!</h2>
            <p>Акаунт <strong>{{ form.username }}</strong> створено.<br>Готуйся до наступної сесії.</p>
            <button class="btn btn--outline" @click="resetForm">Ще один акаунт</button>
          </div>
        </transition>
        <transition name="fade">
          <form v-if="!success" class="form" @submit.prevent="handleSubmit" novalidate>
            <h2 class="form__title">Створити акаунт</h2>
            <div class="field" :class="{ 'field--error': errors.username, 'field--ok': touched.username && !errors.username }">
              <label class="field__label" for="username">Ім'я користувача</label>
              <div class="field__input-wrap">
                <span class="field__icon">🤙</span>
                <input id="username" v-model.trim="form.username" type="text" class="field__input"
                       placeholder="наприклад: wavehunter42" autocomplete="username"
                       @blur="validateField('username')" @input="clearError('username')" />
                <span v-if="touched.username && !errors.username" class="field__check">✓</span>
              </div>
              <transition name="slide-down"><p v-if="errors.username" class="field__error">{{ errors.username }}</p></transition>
            </div>
            <div class="field" :class="{ 'field--error': errors.password, 'field--ok': touched.password && !errors.password }">
              <label class="field__label" for="password">Пароль</label>
              <div class="field__input-wrap">
                <span class="field__icon">🔒</span>
                <input id="password" v-model="form.password" :type="showPassword ? 'text' : 'password'" class="field__input"
                       placeholder="мінімум 8 символів" autocomplete="new-password"
                       @blur="validateField('password')" @input="clearError('password'); checkPasswordStrength()" />
                <button type="button" class="field__toggle" @click="showPassword = !showPassword" tabindex="-1">
                  {{ showPassword ? '🙈' : '👁️' }}
                </button>
              </div>
              <div v-if="form.password" class="strength-bar">
                <div class="strength-bar__track">
                  <div class="strength-bar__fill" :style="{ width: strengthWidth }" :class="'strength-bar__fill--' + strengthLevel"></div>
                </div>
                <span class="strength-bar__label">{{ strengthLabel }}</span>
              </div>
              <transition name="slide-down"><p v-if="errors.password" class="field__error">{{ errors.password }}</p></transition>
            </div>
            <div class="field" :class="{ 'field--error': errors.confirm, 'field--ok': touched.confirm && !errors.confirm }">
              <label class="field__label" for="confirm">Підтвердіть пароль</label>
              <div class="field__input-wrap">
                <span class="field__icon">🔐</span>
                <input id="confirm" v-model="form.confirm" :type="showPassword ? 'text' : 'password'" class="field__input"
                       placeholder="повторіть пароль" autocomplete="new-password"
                       @blur="validateField('confirm')" @input="clearError('confirm')" />
                <span v-if="touched.confirm && !errors.confirm" class="field__check">✓</span>
              </div>
              <transition name="slide-down"><p v-if="errors.confirm" class="field__error">{{ errors.confirm }}</p></transition>
            </div>
            <label class="checkbox-wrap">
              <input type="checkbox" v-model="form.terms" class="checkbox-wrap__input" />
              <span class="checkbox-wrap__box"><span v-if="form.terms" class="checkbox-wrap__check">✓</span></span>
              <span class="checkbox-wrap__label">
                Приймаю <a href="#" class="link">умови користування</a> та&nbsp;<a href="#" class="link">політику конфіденційності</a>
              </span>
            </label>
            <transition name="slide-down"><p v-if="errors.terms" class="field__error field__error--bare">{{ errors.terms }}</p></transition>
            <transition name="slide-down"><div v-if="serverError" class="server-error">⚠️ {{ serverError }}</div></transition>
            <button type="submit" class="btn btn--primary" :disabled="loading">
              <span v-if="!loading">Зареєструватися 🏄</span>
              <span v-else class="loading-dots"><span></span><span></span><span></span></span>
            </button>
            <p class="form__footer">Вже є акаунт? <router-link to="/login" class="link">Увійти</router-link></p>
          </form>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RegisterPage',
  data() {
    return {
      form: { username: '', password: '', confirm: '', terms: false },
      errors: {}, touched: {},
      showPassword: false, loading: false, success: false, shake: false,
      serverError: '', passwordStrength: 0,
    }
  },
  computed: {
    strengthWidth() { return `${this.passwordStrength * 25}%` },
    strengthLevel() { return ['', 'weak', 'fair', 'good', 'strong'][this.passwordStrength] || '' },
    strengthLabel() { return ['', 'Слабкий', 'Середній', 'Хороший', 'Надійний'][this.passwordStrength] || '' },
  },
  methods: {
    foamStyle(i) {
      return { left: `${(i * 5.5 + Math.random() * 3) % 100}%`, animationDelay: `${(i * 0.37).toFixed(2)}s`,
        animationDuration: `${3 + (i % 4)}s`, width: `${4 + (i % 6)}px`, height: `${4 + (i % 6)}px` }
    },
    checkPasswordStrength() {
      const p = this.form.password; let s = 0
      if (p.length >= 8) s++; if (/[A-Z]/.test(p)) s++
      if (/[0-9]/.test(p)) s++; if (/[^A-Za-z0-9]/.test(p)) s++
      this.passwordStrength = s
    },
    validateField(field) {
      this.touched[field] = true
      const { username, password, confirm } = this.form
      if (field === 'username') {
        if (!username) this.errors.username = "Введіть ім'я користувача"
        else if (username.length < 3) this.errors.username = 'Мінімум 3 символи'
        else if (!/^[a-zA-Z0-9_]+$/.test(username)) this.errors.username = 'Тільки латиниця, цифри та _'
        else delete this.errors.username
      }
      if (field === 'password') {
        if (!password) this.errors.password = 'Введіть пароль'
        else if (password.length < 8) this.errors.password = 'Мінімум 8 символів'
        else delete this.errors.password
      }
      if (field === 'confirm') {
        if (!confirm) this.errors.confirm = 'Підтвердіть пароль'
        else if (confirm !== password) this.errors.confirm = 'Паролі не співпадають'
        else delete this.errors.confirm
      }
    },
    clearError(field) { delete this.errors[field] },
    validateAll() {
      ['username', 'password', 'confirm'].forEach(f => this.validateField(f))
      if (!this.form.terms) this.errors.terms = 'Необхідно прийняти умови'
      else delete this.errors.terms
      return Object.keys(this.errors).length === 0
    },
    async handleSubmit() {
      this.serverError = ''
      if (!this.validateAll()) { this.triggerShake(); return }
      this.loading = true
      try {
        const res = await fetch('/api/auth/register', {
          method: 'POST', headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ username: this.form.username, password: this.form.password }),
        })
        if (!res.ok) { const d = await res.json().catch(() => ({})); throw new Error(d.message || 'Помилка сервера') }
        this.success = true
        localStorage.setItem('username', this.form.username)
        this.$router.push('/shop')
      } catch (e) { this.serverError = e.message; this.triggerShake() }
      finally { this.loading = false }
    },
    triggerShake() { this.shake = true; setTimeout(() => { this.shake = false }, 600) },
    resetForm() {
      this.form = { username: '', password: '', confirm: '', terms: false }
      this.errors = {}; this.touched = {}; this.success = false
      this.serverError = ''; this.passwordStrength = 0
    },
  },
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:wght@300;400;500;600&display=swap');
*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

.surf-app { min-height: 100vh; display: flex; align-items: center; justify-content: center; font-family: 'DM Sans', sans-serif; color: #fff; overflow: hidden; position: relative; background: #03203C; }
.ocean-bg { position: fixed; inset: 0; background: linear-gradient(160deg,#001A2E 0%,#03203C 30%,#0A3D62 65%,#0D7377 100%); z-index: 0; }
.wave { position: absolute; bottom: 0; left: -10%; width: 120%; border-radius: 50% 50% 0 0; opacity: .18; animation: wave-roll linear infinite; }
.wave-1 { height: 220px; background: #14BDAC; animation-duration: 8s; }
.wave-2 { height: 170px; background: #0D7377; animation-duration: 11s; animation-delay: -3s; }
.wave-3 { height: 120px; background: #48E5D9; animation-duration: 6s; animation-delay: -1.5s; opacity: .10; }
@keyframes wave-roll { 0%{transform:translateX(0) scaleY(1)} 50%{transform:translateX(-5%) scaleY(1.06)} 100%{transform:translateX(0) scaleY(1)} }
.foam-particles { position: absolute; inset: 0; pointer-events: none; }
.foam { position: absolute; bottom: -10px; border-radius: 50%; background: rgba(232,255,249,.55); animation: foam-rise ease-in infinite; }
@keyframes foam-rise { 0%{transform:translateY(0) scale(1);opacity:.7} 80%{opacity:.3} 100%{transform:translateY(-80vh) scale(.4);opacity:0} }
.surf-deco { position: fixed; inset: 0; pointer-events: none; z-index: 1; }
.board { position: absolute; font-size: 2.4rem; opacity: .18; animation: float-drift ease-in-out infinite; }
.board-1 { top: 12%; left: 5%; animation-duration: 7s; }
.board-2 { top: 65%; right: 8%; animation-duration: 9s; animation-delay: -2s; }
.board-3 { top: 30%; right: 3%; animation-duration: 6s; animation-delay: -4s; }
@keyframes float-drift { 0%,100%{transform:translateY(0) rotate(-5deg)} 50%{transform:translateY(-18px) rotate(5deg)} }

.register-wrapper { position: relative; z-index: 10; width: 100%; max-width: 480px; padding: 16px; }
.card { background: rgba(3,32,60,.82); backdrop-filter: blur(22px); -webkit-backdrop-filter: blur(22px); border-radius: 20px; border: 1px solid rgba(20,189,172,.22); box-shadow: 0 8px 48px rgba(0,0,0,.45),inset 0 1px 0 rgba(255,255,255,.06); padding: 44px 40px 40px; animation: card-enter .7s cubic-bezier(.22,1,.36,1) both; }
@media(max-width:500px){.card{padding:32px 22px 28px}}
@keyframes card-enter { from{opacity:0;transform:translateY(40px) scale(.97)} to{opacity:1;transform:translateY(0) scale(1)} }
.card--shake { animation: shake .55s cubic-bezier(.36,.07,.19,.97) both; }
@keyframes shake { 10%,90%{transform:translateX(-3px)} 20%,80%{transform:translateX(5px)} 30%,50%,70%{transform:translateX(-5px)} 40%,60%{transform:translateX(5px)} }

.card__header { text-align: center; margin-bottom: 32px; }
.logo { display: flex; align-items: center; justify-content: center; gap: 10px; margin-bottom: 6px; }
.logo__text { font-family: 'Bebas Neue',sans-serif; font-size: 2.6rem; letter-spacing: .16em; color: #48E5D9; text-shadow: 0 0 30px rgba(72,229,217,.5); }
.logo__wave { font-size: 1.6rem; color: #14BDAC; animation: wave-bounce 2s ease-in-out infinite; }
.logo__wave:last-child { animation-delay: -1s; }
@keyframes wave-bounce { 0%,100%{transform:scaleX(1)} 50%{transform:scaleX(1.3)} }
.card__tagline { font-size: .82rem; letter-spacing: .22em; text-transform: uppercase; color: #7EC8CC; font-weight: 300; }

.form__title { font-family: 'Bebas Neue',sans-serif; font-size: 1.65rem; letter-spacing: .08em; color: #fff; margin-bottom: 24px; }
.field { margin-bottom: 18px; }
.field__label { display: block; font-size: .77rem; font-weight: 600; letter-spacing: .12em; text-transform: uppercase; color: #7EC8CC; margin-bottom: 7px; }
.field__input-wrap { position: relative; display: flex; align-items: center; }
.field__icon { position: absolute; left: 13px; font-size: 1rem; pointer-events: none; z-index: 1; line-height: 1; }
.field__input { width: 100%; background: rgba(14,61,98,.6); border: 1.5px solid rgba(20,189,172,.3); border-radius: 12px; padding: 13px 44px 13px 42px; color: #ffffff; font-family: 'DM Sans',sans-serif; font-size: .95rem; outline: none; transition: border-color .25s,box-shadow .25s,background .25s; }
.field__input::placeholder { color: rgba(126,200,204,.55); }
.field__input:focus { border-color: #14BDAC; background: rgba(14,61,98,.85); box-shadow: 0 0 0 3px rgba(20,189,172,.15),0 0 18px rgba(20,189,172,.1); }
.field--error .field__input { border-color: #FF6B6B; box-shadow: 0 0 0 3px rgba(255,107,107,.13); }
.field--ok .field__input { border-color: #14BDAC; }
.field__check { position: absolute; right: 14px; color: #14BDAC; font-size: 1rem; font-weight: 700; }
.field__toggle { position: absolute; right: 12px; background: none; border: none; cursor: pointer; font-size: 1rem; line-height: 1; padding: 2px; opacity: .65; transition: opacity .2s; }
.field__toggle:hover { opacity: 1; }
.field__error { font-size: .78rem; color: #FF6B6B; margin-top: 5px; padding-left: 4px; }
.field__error--bare { margin-top: 6px; }

.strength-bar { display: flex; align-items: center; gap: 10px; margin-top: 7px; }
.strength-bar__track { flex: 1; height: 4px; background: rgba(255,255,255,.08); border-radius: 9px; overflow: hidden; }
.strength-bar__fill { height: 100%; border-radius: 9px; transition: width .4s,background .4s; }
.strength-bar__fill--weak  { background: #FF6B6B; }
.strength-bar__fill--fair  { background: #FFA94D; }
.strength-bar__fill--good  { background: #69DB7C; }
.strength-bar__fill--strong { background: #14BDAC; }
.strength-bar__label { font-size: .72rem; color: #7EC8CC; min-width: 58px; }

.checkbox-wrap { display: flex; align-items: flex-start; gap: 12px; cursor: pointer; margin-bottom: 6px; margin-top: 4px; }
.checkbox-wrap__input { display: none; }
.checkbox-wrap__box { flex-shrink: 0; width: 20px; height: 20px; border: 1.5px solid rgba(20,189,172,.3); border-radius: 6px; background: rgba(14,61,98,.6); display: flex; align-items: center; justify-content: center; transition: border-color .2s; margin-top: 1px; }
.checkbox-wrap:hover .checkbox-wrap__box { border-color: #14BDAC; }
.checkbox-wrap__check { color: #14BDAC; font-size: .8rem; font-weight: 700; }
.checkbox-wrap__label { font-size: .83rem; color: rgba(255,255,255,.75); line-height: 1.5; }
.link { color: #48E5D9; text-decoration: none; border-bottom: 1px solid transparent; transition: border-color .2s; }
.link:hover { border-bottom-color: #48E5D9; }
.server-error { background: rgba(255,107,107,.12); border: 1px solid rgba(255,107,107,.4); border-radius: 10px; padding: 10px 14px; font-size: .85rem; color: #FF6B6B; margin-bottom: 14px; }

.btn { width: 100%; padding: 15px; border-radius: 14px; border: none; font-family: 'Bebas Neue',sans-serif; font-size: 1.15rem; letter-spacing: .12em; cursor: pointer; transition: transform .18s,box-shadow .18s,opacity .18s; margin-top: 18px; }
.btn--primary { background: linear-gradient(135deg,#14BDAC 0%,#0D7377 100%); color: #03203C; box-shadow: 0 4px 20px rgba(20,189,172,.35); }
.btn--primary:hover:not(:disabled) { transform: translateY(-2px); box-shadow: 0 8px 32px rgba(20,189,172,.5); }
.btn--primary:active:not(:disabled) { transform: translateY(0); }
.btn--primary:disabled { opacity: .55; cursor: not-allowed; }
.btn--outline { background: transparent; border: 1.5px solid #14BDAC; color: #48E5D9; margin-top: 12px; }
.btn--outline:hover { background: rgba(20,189,172,.1); }

.loading-dots { display: flex; align-items: center; justify-content: center; gap: 6px; }
.loading-dots span { width: 7px; height: 7px; border-radius: 50%; background: #03203C; animation: dot-bounce 1.1s ease-in-out infinite; }
.loading-dots span:nth-child(2) { animation-delay: .18s; }
.loading-dots span:nth-child(3) { animation-delay: .36s; }
@keyframes dot-bounce { 0%,80%,100%{transform:scale(.6);opacity:.5} 40%{transform:scale(1.1);opacity:1} }

.form__footer { text-align: center; margin-top: 18px; font-size: .85rem; color: rgba(255,255,255,.6); }

.success-screen { text-align: center; padding: 10px 0 8px; }
.success-icon { font-size: 3.5rem; margin-bottom: 14px; animation: wiggle .8s ease-in-out; }
@keyframes wiggle { 0%,100%{transform:rotate(0) scale(1)} 25%{transform:rotate(-12deg) scale(1.15)} 75%{transform:rotate(12deg) scale(1.15)} }
.success-screen h2 { font-family: 'Bebas Neue',sans-serif; font-size: 2rem; letter-spacing: .1em; color: #48E5D9; margin-bottom: 10px; }
.success-screen p { color: rgba(255,255,255,.7); font-size: .92rem; line-height: 1.6; }
.success-screen strong { color: #fff; }

.fade-enter-active,.fade-leave-active { transition: opacity .35s,transform .35s; }
.fade-enter-from,.fade-leave-to { opacity: 0; transform: translateY(12px); }
.slide-down-enter-active { transition: all .25s ease; }
.slide-down-leave-active { transition: all .2s ease; }
.slide-down-enter-from,.slide-down-leave-to { opacity: 0; transform: translateY(-6px); max-height: 0; }
.slide-down-enter-to,.slide-down-leave-from { max-height: 40px; }
</style>