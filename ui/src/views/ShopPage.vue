<template>
  <div class="shop">
    <div class="ocean-bg">
      <div class="wave wave-1"></div>
      <div class="wave wave-2"></div>
    </div>

    <header class="navbar">
      <div class="navbar__logo">
        <span class="logo__wave">〜</span>
        <span class="logo__text">WAVECRAFT</span>
        <span class="logo__wave">〜</span>
      </div>
      <nav class="navbar__links">
        <a href="#" class="nav-link nav-link--active">Магазин</a>
        <a href="#" class="nav-link">Дошки</a>
        <a href="#" class="nav-link">Гідрокостюми</a>
        <a href="#" class="nav-link">Аксесуари</a>
      </nav>
      <div class="navbar__actions">
        <button class="action-btn" @click="cartOpen = !cartOpen">
          <span class="action-btn__icon">🛒</span>
          <span v-if="cartCount > 0" class="action-btn__badge">{{ cartCount }}</span>
        </button>
        <div class="account-menu" ref="accountRef">
          <button class="action-btn" @click="accountOpen = !accountOpen">
            <span class="action-btn__icon">👤</span>
            <span class="action-btn__name">{{ username }}</span>
          </button>
          <transition name="dropdown">
            <div v-if="accountOpen" class="account-dropdown">
              <div class="account-dropdown__header">
                <div class="account-avatar">{{ username[0]?.toUpperCase() }}</div>
                <div>
                  <p class="account-dropdown__username">{{ username }}</p>
                  <p class="account-dropdown__role">Серфер 🏄</p>
                </div>
              </div>
              <hr class="account-dropdown__divider" />
              <a href="#" class="account-dropdown__item">👤 Профіль</a>
              <a href="#" class="account-dropdown__item">📦 Мої замовлення</a>
              <a href="#" class="account-dropdown__item">⚙️ Налаштування</a>
              <hr class="account-dropdown__divider" />
              <a href="/login" class="account-dropdown__item account-dropdown__item--danger">🚪 Вийти</a>
            </div>
          </transition>
        </div>
      </div>
    </header>

    <!-- Notification toast -->
    <transition name="fade">
      <div v-if="notification" class="notification-toast" :class="'notification-toast--' + notification.type">
        {{ notification.message }}
      </div>
    </transition>

    <transition name="slide-right">
      <div v-if="cartOpen" class="cart-overlay" @click.self="cartOpen = false">
        <div class="cart-sidebar">
          <div class="cart-sidebar__header">
            <h3>Кошик 🛒</h3>
            <button class="cart-close" @click="cartOpen = false">✕</button>
          </div>
          <div v-if="cart.length === 0" class="cart-empty">
            <span>🌊</span>
            <p>Кошик порожній</p>
            <small>Додай спорядження для наступної сесії</small>
          </div>
          <div v-else class="cart-items">
            <div v-for="item in cart" :key="item.id" class="cart-item">
              <div class="cart-item__img">
                <img v-if="item.image" :src="formatImage(item.image)" :alt="item.name" />
                <span v-else>📦</span>
              </div>
              <div class="cart-item__info">
                <p class="cart-item__name">{{ item.name }}</p>
                <p class="cart-item__price">{{ item.price }} ₴</p>
              </div>
              <div class="cart-item__qty">
                <button @click="decreaseQty(item)">−</button>
                <span>{{ item.qty }}</span>
                <button @click="increaseQty(item)">+</button>
              </div>
              <button class="cart-item__remove" @click="removeFromCart(item)">✕</button>
            </div>
          </div>
          <div v-if="cart.length > 0" class="cart-sidebar__footer">
            <div class="cart-total">
              <span>Разом:</span>
              <span class="cart-total__price">{{ cartTotal }} ₴</span>
            </div>
            <button class="btn btn--primary" @click="checkout">Оформити замовлення 🤙</button>
          </div>
        </div>
      </div>
    </transition>

    <section class="hero">
      <div class="hero__content">
        <p class="hero__sub">Нова колекція 2026</p>
        <h1 class="hero__title">ОСІДЛАЙ<br><span class="hero__accent">ІДЕАЛЬНУ</span><br>ХВИЛЮ</h1>
        <p class="hero__desc">Професійне сьорф-спорядження для тих, хто живе океаном</p>
        <button class="btn btn--primary btn--lg">Дивитись каталог ↓</button>
      </div>
      <div class="hero__deco">🏄‍♂️</div>
    </section>

    <section class="weather-section">
      <div class="weather-section__header">
        <h2 class="section-title">🌊 Хвилі сьогодні</h2>
        <p class="section-sub">Умови для серфінгу по всьому світу • оновлено щойно</p>
      </div>
      <div class="weather-carousel">
        <button class="carousel-arrow" @click="stepCarousel(-1)" :disabled="weatherLoading">‹</button>
        <div class="weather-viewport">
          <template v-if="weatherLoading">
            <div class="weather-track">
              <div v-for="i in 5" :key="i" class="weather-card weather-card--skeleton">
                <div class="sk sk--flag"></div><div class="sk sk--city"></div>
                <div class="sk sk--country"></div><div class="sk sk--icon"></div>
                <div class="sk sk--temp"></div><div class="sk sk--sm"></div><div class="sk sk--sm"></div>
              </div>
            </div>
          </template>
          <template v-else>
            <div class="weather-track" ref="track">
              <div class="weather-card" :class="'weather-card--' + weatherSpots[weatherSpots.length-1]?.condition">
                <div class="weather-card__inner" v-if="weatherSpots.length">
                  <div class="weather-card__flag">{{ weatherSpots[weatherSpots.length-1].flag }}</div>
                  <div class="weather-card__city">{{ weatherSpots[weatherSpots.length-1].city }}</div>
                  <div class="weather-card__country">{{ weatherSpots[weatherSpots.length-1].country }}</div>
                  <div class="weather-card__icon">{{ weatherSpots[weatherSpots.length-1].icon }}</div>
                  <div class="weather-card__temp">{{ weatherSpots[weatherSpots.length-1].temp }}°C</div>
                  <div class="weather-card__row">💨 {{ weatherSpots[weatherSpots.length-1].wind }} км/год</div>
                  <div class="weather-card__row">🌊 {{ weatherSpots[weatherSpots.length-1].waveHeight }}м</div>
                  <div class="weather-card__stars"><span v-for="i in 5" :key="i" :class="i <= weatherSpots[weatherSpots.length-1].rating ? 'star--on' : 'star--off'">★</span></div>
                  <div class="weather-card__badge" :class="'badge--' + weatherSpots[weatherSpots.length-1].condition">{{ weatherSpots[weatherSpots.length-1].label }}</div>
                </div>
              </div>
              <div v-for="(spot, idx) in weatherSpots" :key="idx" class="weather-card" :class="['weather-card--' + spot.condition, { 'is-active': idx === carouselIndex }]">
                <div class="weather-card__inner">
                  <div class="weather-card__flag">{{ spot.flag }}</div>
                  <div class="weather-card__city">{{ spot.city }}</div>
                  <div class="weather-card__country">{{ spot.country }}</div>
                  <div class="weather-card__icon">{{ spot.icon }}</div>
                  <div class="weather-card__temp">{{ spot.temp }}°C</div>
                  <div class="weather-card__row">💨 {{ spot.wind }} км/год</div>
                  <div class="weather-card__row">🌊 {{ spot.waveHeight }}м</div>
                  <div class="weather-card__stars"><span v-for="i in 5" :key="i" :class="i <= spot.rating ? 'star--on' : 'star--off'">★</span></div>
                  <div class="weather-card__badge" :class="'badge--' + spot.condition">{{ spot.label }}</div>
                </div>
              </div>
              <div class="weather-card" :class="'weather-card--' + weatherSpots[0]?.condition" v-if="weatherSpots.length">
                <div class="weather-card__inner">
                  <div class="weather-card__flag">{{ weatherSpots[0].flag }}</div>
                  <div class="weather-card__city">{{ weatherSpots[0].city }}</div>
                  <div class="weather-card__country">{{ weatherSpots[0].country }}</div>
                  <div class="weather-card__icon">{{ weatherSpots[0].icon }}</div>
                  <div class="weather-card__temp">{{ weatherSpots[0].temp }}°C</div>
                  <div class="weather-card__row">💨 {{ weatherSpots[0].wind }} км/год</div>
                  <div class="weather-card__row">🌊 {{ weatherSpots[0].waveHeight }}м</div>
                  <div class="weather-card__stars"><span v-for="i in 5" :key="i" :class="i <= weatherSpots[0].rating ? 'star--on' : 'star--off'">★</span></div>
                  <div class="weather-card__badge" :class="'badge--' + weatherSpots[0].condition">{{ weatherSpots[0].label }}</div>
                </div>
              </div>
            </div>
          </template>
        </div>
        <button class="carousel-arrow" @click="stepCarousel(1)" :disabled="weatherLoading">›</button>
      </div>
      <div v-if="!weatherLoading && weatherSpots.length" class="carousel-dots">
        <button v-for="(_, i) in weatherSpots" :key="i" class="carousel-dot" :class="{ 'carousel-dot--active': i === carouselIndex }" @click="jumpTo(i)"></button>
      </div>
    </section>

    <section class="products-section">
      <div class="products-section__header">
        <h2 class="section-title">Спорядження</h2>
        <div class="filter-tabs">
          <button v-for="tab in tabs" :key="tab.id" class="filter-tab" :class="{ 'filter-tab--active': activeTab === tab.id }" @click="activeTab = tab.id">{{ tab.label }}</button>
        </div>
      </div>
      <div class="products-grid">
        <div v-if="isAdmin" class="product-card add-card" @click="openAddModal">
          <div class="product-card__img add-card__img"><span class="add-plus">＋</span></div>
          <div class="product-card__body"><h3 class="add-card__label">Додати товар</h3></div>
        </div>
        <div v-for="product in filteredProducts" :key="product.id" class="product-card"
             @mouseenter="product.hovered = true" @mouseleave="product.hovered = false">
          <div class="product-card__img">
            <img v-if="product.image" :src="formatImage(product.image)" class="product-card__real-img" :alt="product.name" />
            <span v-else class="product-card__emoji">📦</span>
            <div v-if="product.badge" class="product-card__badge-new">{{ product.badge }}</div>
          </div>
          <div class="product-card__body">
            <p class="product-card__category">{{ product.category }}</p>
            <h3 class="product-card__name">{{ product.name }}</h3>
            <p class="product-card__desc">{{ product.desc }}</p>
            <div class="product-card__footer">
              <div class="product-card__price">
                <span class="price-main">{{ product.price }} ₴</span>
                <span v-if="product.oldPrice" class="price-old">{{ product.oldPrice }} ₴</span>
              </div>
              <button class="btn-add" @click="addToCart(product)" :disabled="addingToCart === product.id">
                <span v-if="addingToCart === product.id">⏳</span>
                <span v-else>{{ product.hovered ? 'До кошика 🛒' : '+' }}</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <footer class="footer">
      <div class="footer__logo">
        <span class="logo__wave">〜</span><span class="logo__text">WAVECRAFT</span><span class="logo__wave">〜</span>
      </div>
      <p class="footer__copy">© 2026 WAVECRAFT. Ride the perfect wave.</p>
    </footer>

    <transition name="fade">
      <div v-if="showAddModal" class="modal-overlay" @click.self="showAddModal = false">
        <div class="modal-content">
          <div class="modal-content__header">
            <h2 class="modal-content__title">Додати товар</h2>
            <button class="modal-content__close" @click="showAddModal = false">✕</button>
          </div>
          <form @submit.prevent="createProduct">
            <div class="input-group">
              <label>Назва товару</label>
              <input v-model="form.name" type="text" required placeholder="Напр: Дошка для серфінгу">
            </div>
            <div class="input-group">
              <label>Опис</label>
              <textarea v-model="form.description" required placeholder="Короткий опис товару..."></textarea>
            </div>
            <div class="input-row">
              <div class="input-group">
                <label>Ціна (₴)</label>
                <input v-model.number="form.price" type="number" step="0.01" required placeholder="0.00">
              </div>
              <div class="input-group">
                <label>Кількість</label>
                <input v-model.number="form.quantity" type="number" required placeholder="1">
              </div>
            </div>
            <div class="input-group">
              <label>Категорія</label>
              <select v-model="form.categoryId" required>
                <option value="1">🏄 Дошки</option>
                <option value="2">🤿 Костюми</option>
                <option value="3">⚡ Аксесуари</option>
              </select>
            </div>
            <div class="input-group">
              <label>Зображення</label>
              <div class="file-upload" @click="$refs.fileInput.click()">
                <input ref="fileInput" type="file" @change="handleFileChange" accept="image/*" style="display:none">
                <span class="file-upload__icon">📁</span>
                <span class="file-upload__text">{{ selectedFile ? selectedFile.name : 'Оберіть файл...' }}</span>
              </div>
            </div>
            <div class="modal-actions">
              <button type="button" class="btn-cancel" @click="showAddModal = false">Скасувати</button>
              <button type="submit" class="btn-submit" :disabled="isCreating">
                {{ isCreating ? 'Збереження...' : 'Зберегти товар 🤙' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
const SURF_SPOTS = [
  { city: 'Балі',         country: 'Індонезія',     flag: '🇮🇩', lat:  -8.6500, lng:  115.2167 },
  { city: 'Назаре',       country: 'Португалія',    flag: '🇵🇹', lat:  39.6014, lng:   -9.0700 },
  { city: 'Гаваї',        country: 'США',           flag: '🇺🇸', lat:  21.3069, lng: -157.8583 },
  { city: 'Джеффріс Бей', country: 'ПАР',           flag: '🇿🇦', lat: -34.0500, lng:   24.9300 },
  { city: 'Hossegor',     country: 'Франція',       flag: '🇫🇷', lat:  43.6653, lng:   -1.4167 },
  { city: 'Маврикій',     country: 'Маврикій',      flag: '🇲🇺', lat: -20.1609, lng:   57.4989 },
  { city: 'Перт',         country: 'Австралія',     flag: '🇦🇺', lat: -31.9505, lng:  115.8605 },
  { city: 'Одеса',        country: 'Україна',       flag: '🇺🇦', lat:  46.4825, lng:   30.7233 },
  { city: 'Маврі',        country: 'Нова Зеландія', flag: '🇳🇿', lat: -37.9717, lng:  176.9911 },
]

const CARD_W   = 196
const API_BASE = 'http://localhost:8082'
const CART_BASE = 'http://localhost:8083'

export default {
  name: 'ShopPage',

  data() {
    return {
      API_BASE: 'http://localhost:8083',
      isAdmin:  localStorage.getItem('role')?.toLowerCase() === 'admin',
      username: localStorage.getItem('username') || 'Серфер',
      userId:   localStorage.getItem('userId') || null,
      token:    localStorage.getItem('token')  || null,

      cartOpen:     false,
      accountOpen:  false,
      activeTab:    'all',
      showAddModal: false,
      isCreating:   false,
      addingToCart: null,
      notification: null,

      cart: [],

      products:     [],
      selectedFile: null,
      form: { name: '', description: '', price: '', categoryId: 1, quantity: 1 },

      weatherSpots:   [],
      weatherLoading: false,
      carouselIndex:  0,
      isAnimating:    false,
      autoPaused:     false,
      autoTimer:      null,

      tabs: [
        { id: 'all',         label: '🌊 Усі' },
        { id: 'boards',      label: '🏄 Дошки' },
        { id: 'wetsuits',    label: '🤿 Костюми' },
        { id: 'accessories', label: '⚡ Аксесуари' },
      ],
    }
  },

  computed: {
    filteredProducts() {
      if (this.activeTab === 'all') return this.products
      return this.products.filter(p => p.tag === this.activeTab)
    },
    cartCount() { return this.cart.reduce((s, i) => s + i.qty, 0) },
    cartTotal() { return this.cart.reduce((s, i) => s + i.price * i.qty, 0).toLocaleString() },

    authHeaders() {
      const h = { 'Content-Type': 'application/json' }
      if (this.token)  h['Authorization'] = `Bearer ${this.token}`
      if (this.userId) h['X-User']        = String(this.userId)
      return h
    },
  },

  methods: {


    applyTransform(idx, animated) {
      const track = this.$refs.track
      if (!track) return
      track.style.transition = animated ? 'transform 0.52s cubic-bezier(0.4,0,0.2,1)' : 'none'
      track.style.transform  = `translateX(-${(idx + 1) * CARD_W}px)`
    },

    stepCarousel(dir) {
      if (this.isAnimating || !this.weatherSpots.length) return
      this.isAnimating = true
      const n    = this.weatherSpots.length
      const next = this.carouselIndex + dir
      this.applyTransform(next, true)
      setTimeout(() => {
        if      (next < 0)  { this.carouselIndex = n - 1; this.applyTransform(this.carouselIndex, false) }
        else if (next >= n) { this.carouselIndex = 0;     this.applyTransform(this.carouselIndex, false) }
        else                { this.carouselIndex = next }
        this.isAnimating = false
      }, 540)
    },

    jumpTo(idx) {
      if (this.isAnimating) return
      this.isAnimating = true
      this.carouselIndex = idx
      this.applyTransform(idx, true)
      setTimeout(() => { this.isAnimating = false }, 540)
    },

    startAuto() {
      clearInterval(this.autoTimer)
      this.autoTimer = setInterval(() => { if (!this.autoPaused) this.stepCarousel(1) }, 3200)
    },

    async loadWeatherSpots() {
      this.weatherLoading = true
      const results = await Promise.allSettled(
          SURF_SPOTS.map(spot =>
              fetch(`/api/weather/openweather?lat=${spot.lat}&lng=${spot.lng}`)
                  .then(r => { if (!r.ok) throw new Error(r.status); return r.json() })
                  .then(data => ({ spot, data }))
                  .catch(() => ({ spot, data: null }))
          )
      )
      this.weatherSpots = results.map(r => {
        const { spot, data } = r.value
        if (!data) return this.fallbackSpot(spot)
        const wind       = Math.round((data.windSpeed || 0) * 3.6)
        const waveHeight = data.waveHeight || this.estimateWave(wind)
        const rating     = this.calcRating(wind, data.temperature, waveHeight)
        return { ...spot, temp: Math.round(data.temperature || 0), wind, waveHeight: (+waveHeight).toFixed(1),
          rating, icon: this.weatherIcon(wind), condition: this.getCondition(rating), label: this.getLabel(rating) }
      })
      this.weatherLoading = false
      this.$nextTick(() => {
        this.applyTransform(0, false)
        this.startAuto()
        const vp = this.$el.querySelector('.weather-viewport')
        if (vp) {
          vp.addEventListener('mouseenter', () => { this.autoPaused = true })
          vp.addEventListener('mouseleave', () => { this.autoPaused = false })
        }
      })
    },

    getCondition(r) { return r >= 5 ? 'epic' : r >= 4 ? 'good' : r >= 3 ? 'ok' : 'poor' },
    getLabel(r)     { return r >= 5 ? 'ЕПІК' : r >= 4 ? 'ДОБРЕ' : r >= 3 ? 'НОРМАЛЬНО' : 'СЛАБО' },
    fallbackSpot(spot) {
      return { ...spot, icon: '❓', temp: '--', wind: '--', waveHeight: '--', rating: 0, condition: 'poor', label: 'ОФЛАЙН' }
    },
    estimateWave(w) {
      if (w < 10) return 0.3; if (w < 20) return 0.8; if (w < 30) return 1.5
      if (w < 40) return 2.5; if (w < 55) return 4.0; return 7.0
    },
    calcRating(wind, temp, wave) {
      let s = 0
      if (wave >= 1 && wave <= 4) s += 2; else if (wave > 4) s += 1
      if (temp >= 20) s += 2; else if (temp >= 15) s += 1
      if (wind >= 10 && wind <= 35) s += 1
      return Math.min(5, Math.max(1, s))
    },
    weatherIcon(wind) {
      if (wind > 50) return '⛈️'; if (wind > 30) return '🌬️'
      if (wind > 15) return '🌤️'; return '☀️'
    },


    async loadProducts() {
      try {
        const res = await fetch(`${API_BASE}/api/products`)
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        const data = await res.json()
        this.products = data.map(p => ({
          id: p.id, name: p.name, desc: p.description, price: p.price, image: p.image,
          category: p.categoryName,
          tag: ({ 'Дошки': 'boards', 'Костюми': 'wetsuits', 'Аксесуари': 'accessories' })[p.categoryName] || 'all',
          badge: null, hovered: false,
        }))
      } catch (e) {
        console.error('Помилка завантаження товарів:', e)
        this.showNotification('Не вдалося завантажити товари', 'error')
      }
    },


    async addToCart(product) {
      if (this.addingToCart === product.id) return
      this.addingToCart = product.id
      try {
        const res = await fetch(`http://localhost:8083/api/carts/items`, {
          method: 'POST',
          headers: this.authHeaders,  // ← добавь скобки!
          body: JSON.stringify({ productId: product.id, quantity: 1 }),
        })
        if (!res.ok) {
          const err = await res.json().catch(() => ({}))
          throw new Error(err.message || `HTTP ${res.status}`)
        }
        const existing = this.cart.find(i => i.id === product.id)
        if (existing) existing.qty++
        else this.cart.push({ ...product, qty: 1 })
        this.cartOpen = true
        this.showNotification({ message: `${product.name} додано до кошика ✅`, type: 'success' })
      } catch (e) {
        console.error('addToCart:', e)
        this.showNotification({ message: 'Помилка кошика: ' + e.message, type: 'error' })
      } finally {
        this.addingToCart = null
      }
    },

    async increaseQty(item) {
      try {
        await fetch(`${CART_BASE}/api/carts/items`, {
          method: 'POST', headers: this.authHeaders,
          body: JSON.stringify({ productId: item.id, quantity: 1 }),
        })
      } catch (e) { /* optimistic */ }
      item.qty++
    },

    decreaseQty(item) {
      if (item.qty <= 1) this.removeFromCart(item)
      else item.qty--
    },

    removeFromCart(item) {
      this.cart = this.cart.filter(i => i.id !== item.id)
    },

    async checkout() {
      try {
        const res = await fetch(`http://localhost:8084/api/orders`, {
          method: 'POST',
          headers: this.authHeaders,
          body: JSON.stringify({ deliveryMethod: 'DELIVERY' }),
        })
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        this.cart = []
        this.cartOpen = false
        this.showNotification('Замовлення оформлено! 🤙 Скоро доставимо спорядження', 'success')
      } catch (e) {
        console.error('checkout:', e)
        this.showNotification('Помилка оформлення замовлення', 'error')
      }
    },

    // ─── NOTIFICATIONS ─────────────────────────────────────

    showNotification(message, type = 'success') {
      this.notification = { message, type }
      setTimeout(() => { this.notification = null }, 4000)
    },

    // ─── ADD PRODUCT ───────────────────────────────────────

    openAddModal()      { this.showAddModal = true },
    handleFileChange(e) { this.selectedFile = e.target.files[0] },

    async createProduct() {
      if (!this.selectedFile) { alert('Будь ласка, оберіть зображення!'); return }
      this.isCreating = true
      const fd = new FormData()
      fd.append('data', new Blob([JSON.stringify({
        name: this.form.name, description: this.form.description,
        price: this.form.price, quantity: this.form.quantity, categoryId: this.form.categoryId,
      })], { type: 'application/json' }))
      fd.append('file', this.selectedFile)
      try {
        const res = await fetch(`${API_BASE}/api/products`, {
          method:  'POST',
          headers: this.token ? { 'Authorization': `Bearer ${this.token}` } : {},
          body:    fd,
        })
        if (res.ok) {
          this.showAddModal = false
          this.form = { name: '', description: '', price: '', categoryId: 1, quantity: 1 }
          this.selectedFile = null
          await this.loadProducts()
          this.showNotification('Товар успішно додано! 🏄', 'success')
        } else {
          const err = await res.json().catch(() => ({}))
          this.showNotification('Помилка: ' + (err.message || res.status), 'error')
        }
      } catch (e) {
        this.showNotification('Мережева помилка', 'error')
      } finally {
        this.isCreating = false
      }
    },

    // ─── UTILS ─────────────────────────────────────────────

    formatImage(img) {
      if (!img) return ''
      if (img.startsWith('data:') || img.startsWith('http')) return img
      return `data:image/png;base64,${img.trim()}`
    },

    handleClickOutside(e) {
      if (this.$refs.accountRef && !this.$refs.accountRef.contains(e.target))
        this.accountOpen = false
    },
  },

  mounted() {
    document.addEventListener('click', this.handleClickOutside)
    this.loadProducts()
    this.loadWeatherSpots()


  },

  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside)
    clearInterval(this.autoTimer)
  },
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:ital,wght@0,300;0,400;0,500;0,600;1,300&display=swap');

:root {
  --ocean-deep: #03203C; --ocean-mid: #0A3D62; --ocean-teal: #0D7377;
  --wave-cyan: #14BDAC;  --wave-light: #48E5D9; --foam: #E8FFF9;
  --sunset: #FF7043;     --text-muted: #7EC8CC; --radius: 18px;
}
*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

.shop { min-height: 100vh; font-family: 'DM Sans', sans-serif; color: var(--foam); background: var(--ocean-deep); overflow-x: hidden; }

.ocean-bg { position: fixed; inset: 0; background: linear-gradient(160deg,#001A2E 0%,#03203C 40%,#0A3D62 80%,#0D7377 100%); z-index: 0; pointer-events: none; }
.wave { position: absolute; bottom: 0; left: -10%; width: 120%; border-radius: 50% 50% 0 0; opacity: .1; animation: wave-roll linear infinite; }
.wave-1 { height: 180px; background: #14BDAC; animation-duration: 9s; }
.wave-2 { height: 120px; background: #48E5D9; animation-duration: 13s; animation-delay: -4s; }
@keyframes wave-roll { 0%{transform:translateX(0)} 50%{transform:translateX(-4%)} 100%{transform:translateX(0)} }

.navbar { position: sticky; top: 0; z-index: 100; display: flex; align-items: center; justify-content: space-between; padding: 0 40px; height: 68px; background: rgba(3,32,60,.88); backdrop-filter: blur(20px); border-bottom: 1px solid rgba(20,189,172,.15); }
.navbar__logo { display: flex; align-items: center; gap: 8px; }
.logo__text { font-family: 'Bebas Neue',sans-serif; font-size: 1.8rem; letter-spacing: .14em; color: var(--wave-light); text-shadow: 0 0 20px rgba(72,229,217,.4); }
.logo__wave { color: var(--wave-cyan); font-size: 1.2rem; animation: wave-bounce 2s ease-in-out infinite; }
.logo__wave:last-child { animation-delay: -1s; }
@keyframes wave-bounce { 0%,100%{transform:scaleX(1)} 50%{transform:scaleX(1.3)} }
.navbar__links { display: flex; gap: 32px; }
.nav-link { font-size: .85rem; font-weight: 500; letter-spacing: .08em; text-transform: uppercase; color: var(--text-muted); text-decoration: none; padding-bottom: 2px; border-bottom: 2px solid transparent; transition: color .2s; }
.nav-link:hover,.nav-link--active { color: var(--wave-light); border-bottom-color: var(--wave-cyan); }
.navbar__actions { display: flex; align-items: center; gap: 12px; }
.action-btn { position: relative; background: rgba(20,189,172,.1); border: 1px solid rgba(20,189,172,.25); border-radius: 10px; padding: 8px 14px; color: var(--foam); cursor: pointer; display: flex; align-items: center; gap: 8px; font-family: 'DM Sans',sans-serif; font-size: .88rem; transition: background .2s,border-color .2s; }
.action-btn:hover { background: rgba(20,189,172,.2); border-color: var(--wave-cyan); }
.action-btn__badge { position: absolute; top: -6px; right: -6px; background: var(--sunset); color: #fff; font-size: .65rem; font-weight: 700; width: 18px; height: 18px; border-radius: 50%; display: flex; align-items: center; justify-content: center; }
.account-menu { position: relative; }
.account-dropdown { position: absolute; top: calc(100% + 10px); right: 0; width: 220px; background: rgba(3,32,60,.96); backdrop-filter: blur(20px); border: 1px solid rgba(20,189,172,.2); border-radius: 14px; padding: 16px; box-shadow: 0 12px 40px rgba(0,0,0,.4); z-index: 200; }
.account-dropdown__header { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; }
.account-avatar { width: 40px; height: 40px; border-radius: 50%; background: linear-gradient(135deg,var(--wave-cyan),var(--ocean-teal)); display: flex; align-items: center; justify-content: center; font-family: 'Bebas Neue',sans-serif; font-size: 1.2rem; color: var(--ocean-deep); flex-shrink: 0; }
.account-dropdown__username { font-weight: 600; font-size: .9rem; }
.account-dropdown__role { font-size: .75rem; color: var(--text-muted); }
.account-dropdown__divider { border: none; border-top: 1px solid rgba(20,189,172,.15); margin: 10px 0; }
.account-dropdown__item { display: block; padding: 8px 10px; border-radius: 8px; font-size: .85rem; color: var(--text-muted); text-decoration: none; transition: background .15s,color .15s; }
.account-dropdown__item:hover { background: rgba(20,189,172,.1); color: var(--foam); }
.account-dropdown__item--danger:hover { background: rgba(255,107,107,.1); color: #FF6B6B; }

/* ── NOTIFICATION ── */
.notification-toast { position: fixed; bottom: 28px; left: 50%; transform: translateX(-50%); z-index: 9999; padding: 12px 26px; border-radius: 50px; font-size: .88rem; font-weight: 600; backdrop-filter: blur(16px); white-space: nowrap; box-shadow: 0 8px 32px rgba(0,0,0,.35); }
.notification-toast--success { background: rgba(105,219,124,.2); border: 1px solid #69DB7C; color: #69DB7C; }
.notification-toast--error   { background: rgba(255,107,107,.2); border: 1px solid #FF6B6B; color: #FF6B6B; }

/* ── CART ── */
.cart-overlay { position: fixed; inset: 0; background: rgba(0,0,0,.5); z-index: 300; display: flex; justify-content: flex-end; }
.cart-sidebar { width: 380px; height: 100%; background: rgba(3,32,60,.97); backdrop-filter: blur(20px); border-left: 1px solid rgba(20,189,172,.2); display: flex; flex-direction: column; padding: 28px; }
.cart-sidebar__header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.cart-sidebar__header h3 { font-family: 'Bebas Neue',sans-serif; font-size: 1.5rem; letter-spacing: .08em; color: var(--wave-light); }
.cart-close { background: none; border: none; color: var(--text-muted); font-size: 1.1rem; cursor: pointer; padding: 4px 8px; border-radius: 6px; }
.cart-close:hover { background: rgba(255,255,255,.05); }
.cart-empty { flex: 1; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 10px; color: var(--text-muted); }
.cart-empty span { font-size: 3rem; }
.cart-empty p { font-weight: 500; color: var(--foam); }
.cart-items { flex: 1; overflow-y: auto; display: flex; flex-direction: column; gap: 12px; }
.cart-item { display: flex; align-items: center; gap: 12px; background: rgba(14,61,98,.5); border: 1px solid rgba(20,189,172,.15); border-radius: 12px; padding: 12px; }
.cart-item__img { width: 40px; height: 40px; border-radius: 8px; overflow: hidden; flex-shrink: 0; display: flex; align-items: center; justify-content: center; background: rgba(10,61,98,.5); font-size: 1.4rem; }
.cart-item__img img { width: 100%; height: 100%; object-fit: cover; }
.cart-item__info { flex: 1; }
.cart-item__name { font-size: .85rem; font-weight: 600; }
.cart-item__price { font-size: .78rem; color: var(--wave-cyan); }
.cart-item__qty { display: flex; align-items: center; gap: 8px; font-size: .85rem; }
.cart-item__qty button { background: rgba(20,189,172,.15); border: 1px solid rgba(20,189,172,.3); color: var(--foam); width: 24px; height: 24px; border-radius: 6px; cursor: pointer; font-size: 1rem; display: flex; align-items: center; justify-content: center; }
.cart-item__remove { background: none; border: none; color: var(--text-muted); cursor: pointer; padding: 4px; transition: color .15s; }
.cart-item__remove:hover { color: #FF6B6B; }
.cart-sidebar__footer { margin-top: 20px; border-top: 1px solid rgba(20,189,172,.15); padding-top: 20px; }
.cart-total { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; font-size: .9rem; color: var(--text-muted); }
.cart-total__price { font-family: 'Bebas Neue',sans-serif; font-size: 1.4rem; color: var(--wave-light); }

/* ── HERO ── */
.hero { position: relative; z-index: 1; min-height: 70vh; display: flex; align-items: center; padding: 80px 40px 60px; overflow: hidden; }
.hero__content { max-width: 560px; }
.hero__sub { font-size: .78rem; letter-spacing: .3em; text-transform: uppercase; color: var(--wave-cyan); margin-bottom: 16px; }
.hero__title { font-family: 'Bebas Neue',sans-serif; font-size: clamp(3.5rem,8vw,7rem); line-height: .95; color: var(--foam); margin-bottom: 20px; }
.hero__accent { color: var(--wave-cyan); text-shadow: 0 0 40px rgba(20,189,172,.5); }
.hero__desc { font-size: 1rem; color: var(--text-muted); line-height: 1.6; margin-bottom: 32px; font-style: italic; }
.hero__deco { position: absolute; right: 5%; font-size: 14rem; opacity: .12; animation: float-drift 6s ease-in-out infinite; pointer-events: none; }
@keyframes float-drift { 0%,100%{transform:translateY(0) rotate(-5deg)} 50%{transform:translateY(-20px) rotate(5deg)} }

/* ── WEATHER ── */
.weather-section { position: relative; z-index: 1; padding: 60px 40px 36px; }
.section-title { font-family: 'Bebas Neue',sans-serif; font-size: 2.2rem; letter-spacing: .08em; color: var(--foam); margin-bottom: 6px; }
.section-sub { font-size: .85rem; color: var(--text-muted); }
.weather-section__header { margin-bottom: 28px; }
.weather-carousel { display: flex; align-items: center; gap: 10px; }
.weather-viewport { flex: 1; overflow: hidden; position: relative; mask-image: linear-gradient(to right,transparent 0%,black 6%,black 94%,transparent 100%); -webkit-mask-image: linear-gradient(to right,transparent 0%,black 6%,black 94%,transparent 100%); }
.weather-track { display: flex; gap: 16px; padding: 12px 4px 16px; will-change: transform; }
.weather-card { flex-shrink: 0; width: 180px; background: rgba(14,61,98,.6); backdrop-filter: blur(14px); border-radius: 16px; border: 1px solid rgba(20,189,172,.2); transition: opacity .3s,transform .3s,box-shadow .3s,border-color .3s; opacity: .55; transform: scale(.94); cursor: pointer; user-select: none; }
.weather-card.is-active { opacity: 1; transform: scale(1.05); box-shadow: 0 18px 44px rgba(0,0,0,.45); border-color: rgba(20,189,172,.6) !important; }
.weather-card:hover { opacity: .85; }
.weather-card__inner { padding: 20px 16px; text-align: center; }
.weather-card--epic { border-color: rgba(20,189,172,.4); }
.weather-card--good { border-color: rgba(105,219,124,.35); }
.weather-card--ok   { border-color: rgba(255,193,7,.3); }
.weather-card--poor { border-color: rgba(255,107,107,.3); }
.weather-card__flag { font-size: 1.8rem; margin-bottom: 6px; }
.weather-card__city { font-family: 'Bebas Neue',sans-serif; font-size: 1.1rem; letter-spacing: .06em; color: var(--foam); }
.weather-card__country { font-size: .7rem; color: var(--text-muted); margin-bottom: 12px; }
.weather-card__icon { font-size: 2rem; margin-bottom: 4px; }
.weather-card__temp { font-family: 'Bebas Neue',sans-serif; font-size: 2rem; color: var(--wave-light); }
.weather-card__row { font-size: .75rem; color: var(--text-muted); margin-top: 4px; }
.weather-card__stars { margin-top: 10px; font-size: .85rem; }
.star--on { color: #FFD700; } .star--off { color: rgba(255,255,255,.2); }
.weather-card__badge { margin-top: 10px; display: inline-block; padding: 3px 10px; border-radius: 20px; font-size: .65rem; font-weight: 700; letter-spacing: .1em; }
.badge--epic { background: rgba(20,189,172,.2);  color: var(--wave-cyan); border: 1px solid var(--wave-cyan); }
.badge--good { background: rgba(105,219,124,.15);color: #69DB7C;          border: 1px solid #69DB7C; }
.badge--ok   { background: rgba(255,193,7,.15);  color: #FFD700;          border: 1px solid #FFD700; }
.badge--poor { background: rgba(255,107,107,.15);color: #FF6B6B;          border: 1px solid #FF6B6B; }
.weather-card--skeleton { opacity: 1; transform: none; pointer-events: none; }
.sk { border-radius: 6px; background: linear-gradient(90deg,rgba(20,189,172,.06) 0%,rgba(20,189,172,.16) 50%,rgba(20,189,172,.06) 100%); background-size: 200% 100%; animation: shimmer 1.6s ease-in-out infinite; margin: 6px auto; }
.sk--flag { width: 36px; height: 36px; border-radius: 50%; } .sk--city { width: 80%; height: 14px; } .sk--country { width: 60%; height: 10px; } .sk--icon { width: 40px; height: 40px; border-radius: 8px; } .sk--temp { width: 50%; height: 28px; } .sk--sm { width: 70%; height: 10px; }
@keyframes shimmer { 0%{background-position:200% 0} 100%{background-position:-200% 0} }
.carousel-dots { display: flex; justify-content: center; gap: 8px; margin-top: 18px; }
.carousel-dot { width: 8px; height: 8px; border-radius: 50%; border: none; background: rgba(20,189,172,.25); cursor: pointer; padding: 0; transition: all .3s; }
.carousel-dot--active { width: 24px; border-radius: 4px; background: var(--wave-cyan); }
.carousel-arrow { background: rgba(20,189,172,.1); border: 1px solid rgba(20,189,172,.3); color: var(--wave-light); width: 42px; height: 42px; border-radius: 50%; font-size: 1.5rem; cursor: pointer; flex-shrink: 0; display: flex; align-items: center; justify-content: center; transition: background .2s; }
.carousel-arrow:hover { background: rgba(20,189,172,.25); }
.carousel-arrow:disabled { opacity: .3; cursor: default; }

/* ── PRODUCTS ── */
.products-section { position: relative; z-index: 1; padding: 40px 40px 80px; }
.products-section__header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 32px; flex-wrap: wrap; gap: 16px; }
.filter-tabs { display: flex; gap: 8px; flex-wrap: wrap; }
.filter-tab { padding: 8px 18px; border-radius: 30px; border: 1px solid rgba(20,189,172,.25); background: transparent; color: var(--text-muted); font-family: 'DM Sans',sans-serif; font-size: .82rem; cursor: pointer; transition: all .2s; }
.filter-tab:hover { border-color: var(--wave-cyan); color: var(--foam); }
.filter-tab--active { background: linear-gradient(135deg,var(--wave-cyan),var(--ocean-teal)); border-color: transparent; color: var(--ocean-deep); font-weight: 600; }
.products-grid { display: grid; grid-template-columns: repeat(auto-fill,minmax(260px,1fr)); gap: 24px; }
.product-card { background: rgba(14,61,98,.55); backdrop-filter: blur(14px); border: 1px solid rgba(20,189,172,.18); border-radius: var(--radius); overflow: hidden; transition: transform .25s,box-shadow .25s,border-color .25s; animation: card-enter .5s ease both; }
.product-card:hover { transform: translateY(-8px); box-shadow: 0 20px 50px rgba(0,0,0,.35); border-color: rgba(20,189,172,.45); }
@keyframes card-enter { from{opacity:0;transform:translateY(20px)} to{opacity:1;transform:none} }
.product-card__img { position: relative; width: 100%; height: 180px; background: linear-gradient(135deg,rgba(13,115,119,.3),rgba(10,61,98,.5)); display: flex; align-items: center; justify-content: center; overflow: hidden; }
.product-card__real-img { width: 100%; height: 100%; object-fit: cover; }
.product-card__emoji { font-size: 5rem; filter: drop-shadow(0 4px 16px rgba(0,0,0,.3)); }
.product-card__badge-new { position: absolute; top: 12px; right: 12px; background: var(--sunset); color: #fff; font-size: .68rem; font-weight: 700; letter-spacing: .08em; padding: 4px 10px; border-radius: 20px; }
.product-card__body { padding: 18px; }
.product-card__category { font-size: .7rem; letter-spacing: .18em; text-transform: uppercase; color: var(--wave-cyan); margin-bottom: 4px; }
.product-card__name { font-family: 'Bebas Neue',sans-serif; font-size: 1.25rem; letter-spacing: .06em; color: var(--foam); margin-bottom: 6px; }
.product-card__desc { font-size: .8rem; color: var(--text-muted); line-height: 1.5; margin-bottom: 16px; }
.product-card__footer { display: flex; align-items: center; justify-content: space-between; }
.price-main { font-family: 'Bebas Neue',sans-serif; font-size: 1.4rem; color: var(--wave-light); }
.price-old { font-size: .8rem; color: var(--text-muted); text-decoration: line-through; margin-left: 8px; }
.btn-add { background: linear-gradient(135deg,var(--wave-cyan),var(--ocean-teal)); border: none; color: var(--ocean-deep); font-family: 'DM Sans',sans-serif; font-weight: 600; font-size: .8rem; padding: 8px 16px; border-radius: 10px; cursor: pointer; transition: transform .18s,box-shadow .18s; white-space: nowrap; }
.btn-add:hover:not(:disabled) { transform: scale(1.05); box-shadow: 0 4px 16px rgba(20,189,172,.4); }
.btn-add:disabled { opacity: .5; cursor: default; }
.add-card { cursor: pointer; border: 2px dashed rgba(20,189,172,.3) !important; background: rgba(20,189,172,.04) !important; }
.add-card:hover { border-color: var(--wave-cyan) !important; background: rgba(20,189,172,.1) !important; transform: translateY(-8px); }
.add-card__img { background: transparent !important; height: 160px; }
.add-plus { font-size: 4rem; color: rgba(20,189,172,.4); transition: all .2s; }
.add-card:hover .add-plus { color: var(--wave-cyan); transform: scale(1.15); display: inline-block; }
.add-card__label { font-family: 'Bebas Neue',sans-serif; font-size: 1.1rem; letter-spacing: .1em; color: var(--text-muted); text-align: center; }
.add-card:hover .add-card__label { color: var(--wave-cyan); }
.btn { border: none; border-radius: 12px; font-family: 'Bebas Neue',sans-serif; letter-spacing: .1em; cursor: pointer; transition: transform .18s,box-shadow .18s; }
.btn--primary { background: linear-gradient(135deg,var(--wave-cyan),var(--ocean-teal)); color: var(--ocean-deep); padding: 12px 28px; font-size: 1rem; box-shadow: 0 4px 20px rgba(20,189,172,.3); }
.btn--primary:hover { transform: translateY(-2px); box-shadow: 0 8px 28px rgba(20,189,172,.45); }
.btn--lg { padding: 16px 40px; font-size: 1.15rem; }

/* ── FOOTER ── */
.footer { position: relative; z-index: 1; text-align: center; padding: 40px; border-top: 1px solid rgba(20,189,172,.1); }
.footer__logo { display: flex; align-items: center; justify-content: center; gap: 8px; margin-bottom: 10px; }
.footer__copy { font-size: .78rem; color: var(--text-muted); letter-spacing: .1em; }

/* ── MODAL ── */
.modal-overlay { position: fixed; inset: 0; background: rgba(0,15,30,.85); backdrop-filter: blur(10px); display: flex; align-items: center; justify-content: center; z-index: 2000; }
.modal-content { background: linear-gradient(160deg,rgba(10,61,98,.98),rgba(3,32,60,.99)); width: 100%; max-width: 500px; border-radius: 24px; border: 1px solid rgba(20,189,172,.3); padding: 36px; box-shadow: 0 24px 60px rgba(0,0,0,.6); }
.modal-content__header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 28px; }
.modal-content__title { font-family: 'Bebas Neue',sans-serif; font-size: 2rem; letter-spacing: .08em; color: var(--wave-light); text-shadow: 0 0 20px rgba(72,229,217,.3); }
.modal-content__close { background: rgba(255,255,255,.05); border: 1px solid rgba(20,189,172,.2); color: var(--text-muted); width: 36px; height: 36px; border-radius: 10px; cursor: pointer; font-size: .95rem; display: flex; align-items: center; justify-content: center; transition: all .2s; }
.modal-content__close:hover { background: rgba(255,107,107,.1); border-color: rgba(255,107,107,.4); color: #FF6B6B; }
.input-group { margin-bottom: 16px; display: flex; flex-direction: column; gap: 6px; }
.input-row { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; }
.input-group label { font-size: .72rem; font-weight: 600; color: var(--wave-cyan); text-transform: uppercase; letter-spacing: .12em; }
.input-group input,.input-group textarea,.input-group select { background: rgba(255,255,255,.04); border: 1px solid rgba(20,189,172,.2); padding: 12px 14px; border-radius: 12px; color: var(--foam); font-family: 'DM Sans',sans-serif; font-size: .9rem; outline: none; transition: border-color .2s,background .2s; }
.input-group input:focus,.input-group textarea:focus,.input-group select:focus { border-color: var(--wave-cyan); background: rgba(20,189,172,.06); }
.input-group textarea { min-height: 90px; resize: vertical; }
.input-group select option { background: var(--ocean-mid); color: var(--foam); }
.file-upload { display: flex; align-items: center; gap: 10px; background: rgba(255,255,255,.04); border: 1px dashed rgba(20,189,172,.3); padding: 12px 14px; border-radius: 12px; cursor: pointer; transition: all .2s; }
.file-upload:hover { border-color: var(--wave-cyan); background: rgba(20,189,172,.06); }
.file-upload__icon { font-size: 1.1rem; }
.file-upload__text { font-size: .85rem; color: var(--text-muted); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.modal-actions { display: flex; justify-content: flex-end; align-items: center; gap: 12px; margin-top: 24px; padding-top: 20px; border-top: 1px solid rgba(20,189,172,.12); }
.btn-cancel { background: transparent; border: 1px solid rgba(20,189,172,.2); color: var(--text-muted); padding: 10px 22px; border-radius: 10px; font-family: 'DM Sans',sans-serif; font-size: .88rem; cursor: pointer; transition: all .2s; }
.btn-cancel:hover { border-color: rgba(20,189,172,.4); color: var(--foam); }
.btn-submit { background: linear-gradient(135deg,var(--wave-cyan),var(--ocean-teal)); border: none; color: var(--ocean-deep); padding: 10px 26px; border-radius: 10px; font-family: 'Bebas Neue',sans-serif; font-size: 1rem; letter-spacing: .08em; cursor: pointer; box-shadow: 0 4px 16px rgba(20,189,172,.3); transition: transform .18s,box-shadow .18s; }
.btn-submit:hover:not(:disabled) { transform: translateY(-2px); box-shadow: 0 8px 24px rgba(20,189,172,.45); }
.btn-submit:disabled { opacity: .5; cursor: default; }

/* ── TRANSITIONS ── */
.dropdown-enter-active,.dropdown-leave-active { transition: opacity .2s,transform .2s; }
.dropdown-enter-from,.dropdown-leave-to { opacity: 0; transform: translateY(-8px); }
.slide-right-enter-active,.slide-right-leave-active { transition: opacity .3s; }
.slide-right-enter-from,.slide-right-leave-to { opacity: 0; }
.slide-right-enter-active .cart-sidebar,.slide-right-leave-active .cart-sidebar { transition: transform .3s; }
.slide-right-enter-from .cart-sidebar,.slide-right-leave-to .cart-sidebar { transform: translateX(100%); }
.fade-enter-active,.fade-leave-active { transition: opacity .3s; }
.fade-enter-from,.fade-leave-to { opacity: 0; }
</style>