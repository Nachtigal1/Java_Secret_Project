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
      <div class="board board-1"></div>
      <div class="board board-2"></div>
      <div class="board board-3">☀</div>
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

        <!-- Success state -->
        <transition name="fade">
          <div v-if="success" class="success-screen">
            <div class="success-icon"></div>
            <h2>Ты в деле!</h2>
            <p>Аккаунт <strong>{{ form.username }}</strong> создан.<br>Готовься к следующей сессии.</p>
            <button class="btn btn--outline" @click="resetForm">Ещё один аккаунт</button>
          </div>
        </transition>

        <transition name="fade">
          <form v-if="!success" class="form" @submit.prevent="handleSubmit" novalidate>
            <h2 class="form__title">Создать аккаунт</h2>

            <div class="field" :class="{ 'field--error': errors.username, 'field--ok': touched.username && !errors.username }">
              <label class="field__label" for="username">Имя пользователя</label>
              <div class="field__input-wrap">
                <span class="field__icon"></span>
                <input
                    id="username"
                    v-model.trim="form.username"
                    type="text"
                    class="field__input"
                    placeholder="например: wavehunter42"
                    autocomplete="username"
                    @blur="validateField('username')"
                    @input="clearError('username')"
                />
                <span v-if="touched.username && !errors.username" class="field__check">✓</span>
              </div>
              <transition name="slide-down">
                <p v-if="errors.username" class="field__error">{{ errors.username }}</p>
              </transition>
            </div>

            <div class="field" :class="{ 'field--error': errors.password, 'field--ok': touched.password && !errors.password }">
              <label class="field__label" for="password">Пароль</label>
              <div class="field__input-wrap">
                <span class="field__icon"></span>
                <input
                    :id="'password'"
                    v-model="form.password"
                    :type="showPassword ? 'text' : 'password'"
                    class="field__input"
                    placeholder="минимум 8 символов"
                    autocomplete="new-password"
                    @blur="validateField('password')"
                    @input="clearError('password'); checkPasswordStrength()"
                />
                <button type="button" class="field__toggle" @click="showPassword = !showPassword" tabindex="-1">
                  {{ showPassword ? '' : '👁' }}
                </button>
              </div>
              <div v-if="form.password" class="strength-bar">
                <div class="strength-bar__track">
                  <div class="strength-bar__fill" :style="{ width: strengthWidth }" :class="'strength-bar__fill--' + strengthLevel"></div>
                </div>
                <span class="strength-bar__label">{{ strengthLabel }}</span>
              </div>
              <transition name="slide-down">
                <p v-if="errors.password" class="field__error">{{ errors.password }}</p>
              </transition>
            </div>

            <div class="field" :class="{ 'field--error': errors.confirm, 'field--ok': touched.confirm && !errors.confirm }">
              <label class="field__label" for="confirm">Подтвердите пароль</label>
              <div class="field__input-wrap">
                <span class="field__icon">🔐</span>
                <input
                    id="confirm"
                    v-model="form.confirm"
                    :type="showPassword ? 'text' : 'password'"
                    class="field__input"
                    placeholder="повторите пароль"
                    autocomplete="new-password"
                    @blur="validateField('confirm')"
                    @input="clearError('confirm')"
                />
                <span v-if="touched.confirm && !errors.confirm" class="field__check">✓</span>
              </div>
              <transition name="slide-down">
                <p v-if="errors.confirm" class="field__error">{{ errors.confirm }}</p>
              </transition>
            </div>

            <label class="checkbox-wrap">
              <input type="checkbox" v-model="form.terms" class="checkbox-wrap__input" />
              <span class="checkbox-wrap__box">
                <span v-if="form.terms" class="checkbox-wrap__check">✓</span>
              </span>
              <span class="checkbox-wrap__label">
                Принимаю <a href="#" class="link">условия пользования</a> и&nbsp;<a href="#" class="link">политику конфиденциальности</a>
              </span>
            </label>
            <transition name="slide-down">
              <p v-if="errors.terms" class="field__error field__error--bare">{{ errors.terms }}</p>
            </transition>

            <transition name="slide-down">
              <div v-if="serverError" class="server-error">⚠️ {{ serverError }}</div>
            </transition>

            <button type="submit" class="btn btn--primary" :disabled="loading">
              <span v-if="!loading">Зарегистрироваться 🏄</span>
              <span v-else class="loading-dots">
                <span></span><span></span><span></span>
              </span>
            </button>

            <p class="form__footer">
              Уже есть аккаунт? <router-link to="/login" class="link">Войти</router-link>
            </p>
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
      errors: {},
      touched: {},
      showPassword: false,
      loading: false,
      success: false,
      shake: false,
      serverError: '',
      passwordStrength: 0,
    }
  },

  computed: {
    strengthWidth() {
      return `${this.passwordStrength * 25}%`
    },
    strengthLevel() {
      return ['', 'weak', 'fair', 'good', 'strong'][this.passwordStrength] || ''
    },
    strengthLabel() {
      return ['', 'Слабый', 'Средний', 'Хороший', 'Надёжный'][this.passwordStrength] || ''
    },
  },

  methods: {
    foamStyle(i) {
      return {
        left: `${(i * 5.5 + Math.random() * 3) % 100}%`,
        animationDelay: `${(i * 0.37).toFixed(2)}s`,
        animationDuration: `${3 + (i % 4)}s`,
        width: `${4 + (i % 6)}px`,
        height: `${4 + (i % 6)}px`,
      }
    },

    checkPasswordStrength() {
      const p = this.form.password
      let score = 0
      if (p.length >= 8) score++
      if (/[A-Z]/.test(p)) score++
      if (/[0-9]/.test(p)) score++
      if (/[^A-Za-z0-9]/.test(p)) score++
      this.passwordStrength = score
    },

    validateField(field) {
      this.touched[field] = true
      const { username, password, confirm, terms } = this.form

      if (field === 'username') {
        if (!username) this.errors.username = 'Введите имя пользователя'
        else if (username.length < 3) this.errors.username = 'Минимум 3 символа'
        else if (!/^[a-zA-Z0-9_]+$/.test(username)) this.errors.username = 'Только латиница, цифры и _'
        else delete this.errors.username
      }

      if (field === 'password') {
        if (!password) this.errors.password = 'Введите пароль'
        else if (password.length < 8) this.errors.password = 'Минимум 8 символов'
        else delete this.errors.password
      }

      if (field === 'confirm') {
        if (!confirm) this.errors.confirm = 'Подтвердите пароль'
        else if (confirm !== password) this.errors.confirm = 'Пароли не совпадают'
        else delete this.errors.confirm
      }
    },

    clearError(field) {
      delete this.errors[field]
    },

    validateAll() {
      ['username', 'password', 'confirm'].forEach(f => this.validateField(f))
      if (!this.form.terms) this.errors.terms = 'Необходимо принять условия'
      else delete this.errors.terms
      return Object.keys(this.errors).length === 0
    },

    async handleSubmit() {
      this.serverError = ''
      if (!this.validateAll()) {
        this.triggerShake()
        return
      }

      this.loading = true
      try {
        const response = await fetch('/api/users/register', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            username: this.form.username,
            password: this.form.password,
          }),
        })

        if (!response.ok) {
          const data = await response.json().catch(() => ({}))
          throw new Error(data.message || 'Ошибка сервера. Попробуйте позже.')
        }

        this.success = true
      } catch (e) {
        this.serverError = e.message
        this.triggerShake()
      } finally {
        this.loading = false
      }
    },

    triggerShake() {
      this.shake = true
      setTimeout(() => { this.shake = false }, 600)
    },

    resetForm() {
      this.form = { username: '', password: '', confirm: '', terms: false }
      this.errors = {}
      this.touched = {}
      this.success = false
      this.serverError = ''
      this.passwordStrength = 0
    },
  },
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:wght@300;400;500;600&display=swap');

:root {
  --ocean-deep: #03203C;
  --ocean-mid: #0A3D62;
  --ocean-teal: #0D7377;
  --wave-cyan: #14BDAC;
  --wave-light: #48E5D9;
  --foam: #E8FFF9;
  --sand: #FFF3E0;
  --sunset: #FF7043;
  --sunset-glow: #FFAB91;
  --text-primary: #E8FFF9;
  --text-muted: #7EC8CC;
  --card-bg: rgba(3, 32, 60, 0.82);
  --field-bg: rgba(14, 61, 98, 0.6);
  --field-border: rgba(20, 189, 172, 0.3);
  --field-border-focus: #14BDAC;
  --error: #FF6B6B;
  --radius: 20px;
}

*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

.surf-app {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'DM Sans', sans-serif;
  color: var(--text-primary);
  overflow: hidden;
  position: relative;
  background: var(--ocean-deep);
}

.ocean-bg {
  position: fixed;
  inset: 0;
  background: linear-gradient(
      160deg,
      #001A2E 0%,
      #03203C 30%,
      #0A3D62 65%,
      #0D7377 100%
  );
  z-index: 0;
}

.wave {
  position: absolute;
  bottom: 0;
  left: -10%;
  width: 120%;
  border-radius: 50% 50% 0 0;
  opacity: 0.18;
  animation: wave-roll linear infinite;
}
.wave-1 { height: 220px; background: #14BDAC; animation-duration: 8s; }
.wave-2 { height: 170px; background: #0D7377; animation-duration: 11s; animation-delay: -3s; }
.wave-3 { height: 120px; background: #48E5D9; animation-duration: 6s; animation-delay: -1.5s; opacity: 0.10; }

@keyframes wave-roll {
  0%   { transform: translateX(0) scaleY(1); }
  50%  { transform: translateX(-5%) scaleY(1.06); }
  100% { transform: translateX(0) scaleY(1); }
}

/* ─── Foam particles ─── */
.foam-particles { position: absolute; inset: 0; pointer-events: none; }
.foam {
  position: absolute;
  bottom: -10px;
  border-radius: 50%;
  background: rgba(232, 255, 249, 0.55);
  animation: foam-rise ease-in infinite;
}
@keyframes foam-rise {
  0%   { transform: translateY(0) scale(1); opacity: 0.7; }
  80%  { opacity: 0.3; }
  100% { transform: translateY(-80vh) scale(0.4); opacity: 0; }
}

/* ─── Decorative floating emojis ─── */
.surf-deco { position: fixed; inset: 0; pointer-events: none; z-index: 1; }
.board { position: absolute; font-size: 2.4rem; opacity: 0.18; animation: float-drift ease-in-out infinite; }
.board-1 { top: 12%; left: 5%;  animation-duration: 7s; }
.board-2 { top: 65%; right: 8%; animation-duration: 9s; animation-delay: -2s; }
.board-3 { top: 30%; right: 3%; animation-duration: 6s; animation-delay: -4s; }
@keyframes float-drift {
  0%, 100% { transform: translateY(0) rotate(-5deg); }
  50%       { transform: translateY(-18px) rotate(5deg); }
}

/* ─── Card wrapper ─── */
.register-wrapper {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 480px;
  padding: 16px;
}

.card {
  background: var(--card-bg);
  backdrop-filter: blur(22px);
  -webkit-backdrop-filter: blur(22px);
  border-radius: var(--radius);
  border: 1px solid rgba(20, 189, 172, 0.22);
  box-shadow:
      0 8px 48px rgba(0,0,0,0.45),
      inset 0 1px 0 rgba(255,255,255,0.06);
  padding: 44px 40px 40px;
  animation: card-enter 0.7s cubic-bezier(.22,1,.36,1) both;
}

@media (max-width: 500px) {
  .card { padding: 32px 22px 28px; }
}

@keyframes card-enter {
  from { opacity: 0; transform: translateY(40px) scale(0.97); }
  to   { opacity: 1; transform: translateY(0) scale(1); }
}

.card--shake {
  animation: shake 0.55s cubic-bezier(.36,.07,.19,.97) both;
}
@keyframes shake {
  10%, 90% { transform: translateX(-3px); }
  20%, 80% { transform: translateX(5px); }
  30%, 50%, 70% { transform: translateX(-5px); }
  40%, 60% { transform: translateX(5px); }
}

/* ─── Header / Logo ─── */
.card__header { text-align: center; margin-bottom: 32px; }

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 6px;
}
.logo__text {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 2.6rem;
  letter-spacing: 0.16em;
  color: var(--wave-light);
  text-shadow: 0 0 30px rgba(72, 229, 217, 0.5);
}
.logo__wave {
  font-size: 1.6rem;
  color: var(--wave-cyan);
  animation: wave-bounce 2s ease-in-out infinite;
}
.logo__wave:last-child { animation-delay: -1s; }
@keyframes wave-bounce {
  0%, 100% { transform: scaleX(1); }
  50%       { transform: scaleX(1.3); }
}

.card__tagline {
  font-size: 0.82rem;
  letter-spacing: 0.22em;
  text-transform: uppercase;
  color: var(--text-muted);
  font-weight: 300;
}


.form__title {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.65rem;
  letter-spacing: 0.08em;
  color: var(--foam);
  margin-bottom: 24px;
}

.field { margin-bottom: 18px; }

.field__label {
  display: block;
  font-size: 0.77rem;
  font-weight: 600;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: var(--text-muted);
  margin-bottom: 7px;
}

.field__input-wrap {
  position: relative;
  display: flex;
  align-items: center;
}

.field__icon {
  position: absolute;
  left: 13px;
  font-size: 1rem;
  pointer-events: none;
  z-index: 1;
  line-height: 1;
}

.field__input {
  width: 100%;
  background: var(--field-bg);
  border: 1.5px solid var(--field-border);
  border-radius: 12px;
  padding: 13px 44px 13px 42px;
  color: var(--foam);
  font-family: 'DM Sans', sans-serif;
  font-size: 0.95rem;
  outline: none;
  transition: border-color 0.25s, box-shadow 0.25s, background 0.25s;
}
.field__input::placeholder { color: rgba(126, 200, 204, 0.45); }
.field__input:focus {
  border-color: var(--field-border-focus);
  background: rgba(14, 61, 98, 0.85);
  box-shadow: 0 0 0 3px rgba(20, 189, 172, 0.15), 0 0 18px rgba(20, 189, 172, 0.1);
}

.field--error .field__input {
  border-color: var(--error);
  box-shadow: 0 0 0 3px rgba(255, 107, 107, 0.13);
}
.field--ok .field__input { border-color: var(--wave-cyan); }

.field__check {
  position: absolute;
  right: 14px;
  color: var(--wave-cyan);
  font-size: 1rem;
  font-weight: 700;
}

.field__toggle {
  position: absolute;
  right: 12px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  line-height: 1;
  padding: 2px;
  opacity: 0.65;
  transition: opacity 0.2s;
}
.field__toggle:hover { opacity: 1; }

.field__error {
  font-size: 0.78rem;
  color: var(--error);
  margin-top: 5px;
  padding-left: 4px;
  display: flex;
  align-items: center;
  gap: 4px;
}
.field__error--bare { margin-top: 6px; }

.strength-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 7px;
}
.strength-bar__track {
  flex: 1;
  height: 4px;
  background: rgba(255,255,255,0.08);
  border-radius: 9px;
  overflow: hidden;
}
.strength-bar__fill {
  height: 100%;
  border-radius: 9px;
  transition: width 0.4s ease, background 0.4s;
}
.strength-bar__fill--weak  { background: #FF6B6B; }
.strength-bar__fill--fair  { background: #FFA94D; }
.strength-bar__fill--good  { background: #69DB7C; }
.strength-bar__fill--strong { background: var(--wave-cyan); }
.strength-bar__label { font-size: 0.72rem; color: var(--text-muted); min-width: 58px; }


.checkbox-wrap {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  cursor: pointer;
  margin-bottom: 6px;
  margin-top: 4px;
}
.checkbox-wrap__input { display: none; }
.checkbox-wrap__box {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  border: 1.5px solid var(--field-border);
  border-radius: 6px;
  background: var(--field-bg);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: border-color 0.2s, background 0.2s;
  margin-top: 1px;
}
.checkbox-wrap:hover .checkbox-wrap__box { border-color: var(--wave-cyan); }
.checkbox-wrap__check { color: var(--wave-cyan); font-size: 0.8rem; font-weight: 700; }
.checkbox-wrap__label { font-size: 0.83rem; color: var(--text-muted); line-height: 1.5; }

.link { color: var(--wave-light); text-decoration: none; border-bottom: 1px solid transparent; transition: border-color 0.2s; }
.link:hover { border-bottom-color: var(--wave-light); }

.server-error {
  background: rgba(255, 107, 107, 0.12);
  border: 1px solid rgba(255, 107, 107, 0.4);
  border-radius: 10px;
  padding: 10px 14px;
  font-size: 0.85rem;
  color: #FF6B6B;
  margin-bottom: 14px;
}

.btn {
  width: 100%;
  padding: 15px;
  border-radius: 14px;
  border: none;
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.15rem;
  letter-spacing: 0.12em;
  cursor: pointer;
  transition: transform 0.18s, box-shadow 0.18s, opacity 0.18s;
  margin-top: 18px;
}
.btn--primary {
  background: linear-gradient(135deg, var(--wave-cyan) 0%, var(--ocean-teal) 100%);
  color: var(--ocean-deep);
  box-shadow: 0 4px 20px rgba(20, 189, 172, 0.35);
}
.btn--primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 32px rgba(20, 189, 172, 0.5);
}
.btn--primary:active:not(:disabled) { transform: translateY(0); }
.btn--primary:disabled { opacity: 0.55; cursor: not-allowed; }

.btn--outline {
  background: transparent;
  border: 1.5px solid var(--wave-cyan);
  color: var(--wave-light);
  margin-top: 12px;
}
.btn--outline:hover { background: rgba(20,189,172,0.1); }

.loading-dots { display: flex; align-items: center; justify-content: center; gap: 6px; }
.loading-dots span {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: var(--ocean-deep);
  animation: dot-bounce 1.1s ease-in-out infinite;
}
.loading-dots span:nth-child(2) { animation-delay: 0.18s; }
.loading-dots span:nth-child(3) { animation-delay: 0.36s; }
@keyframes dot-bounce {
  0%, 80%, 100% { transform: scale(0.6); opacity: 0.5; }
  40% { transform: scale(1.1); opacity: 1; }
}

.form__footer { text-align: center; margin-top: 18px; font-size: 0.85rem; color: var(--text-muted); }

.success-screen { text-align: center; padding: 10px 0 8px; }
.success-icon { font-size: 3.5rem; margin-bottom: 14px; animation: wiggle 0.8s ease-in-out; }
@keyframes wiggle {
  0%,100%{ transform: rotate(0deg) scale(1); }
  25%{ transform: rotate(-12deg) scale(1.15); }
  75%{ transform: rotate(12deg) scale(1.15); }
}
.success-screen h2 {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 2rem;
  letter-spacing: 0.1em;
  color: var(--wave-light);
  margin-bottom: 10px;
}
.success-screen p { color: var(--text-muted); font-size: 0.92rem; line-height: 1.6; }
.success-screen strong { color: var(--foam); }

.fade-enter-active, .fade-leave-active { transition: opacity 0.35s, transform 0.35s; }
.fade-enter-from, .fade-leave-to { opacity: 0; transform: translateY(12px); }

.slide-down-enter-active { transition: all 0.25s ease; }
.slide-down-leave-active { transition: all 0.2s ease; }
.slide-down-enter-from, .slide-down-leave-to { opacity: 0; transform: translateY(-6px); max-height: 0; }
.slide-down-enter-to, .slide-down-leave-from { max-height: 40px; }
</style>