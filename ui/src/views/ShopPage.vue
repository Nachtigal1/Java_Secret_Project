<template>
  <div class="shop">
    <!-- Ocean background -->
    <div class="ocean-bg">
      <div class="wave wave-1"></div>
      <div class="wave wave-2"></div>
    </div>

    <!-- ── NAVBAR ── -->
    <header class="navbar">
      <div class="navbar__logo">
        <span class="logo__wave">〜</span>
        <span class="logo__text">WAVECRAFT</span>
        <span class="logo__wave">〜</span>
      </div>

      <nav class="navbar__links">
        <a href="#" class="nav-link nav-link--active">Магазин</a>
        <a href="#" class="nav-link">Доски</a>
        <a href="#" class="nav-link">Гидрокостюмы</a>
        <a href="#" class="nav-link">Аксессуары</a>
      </nav>

      <div class="navbar__actions">
        <!-- Cart -->
        <button class="action-btn" @click="cartOpen = !cartOpen">
          <span class="action-btn__icon">🛒</span>
          <span v-if="cartCount > 0" class="action-btn__badge">{{ cartCount }}</span>
        </button>

        <!-- Account -->
        <div class="account-menu" ref="accountRef">
          <button class="action-btn action-btn--account" @click="accountOpen = !accountOpen">
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
              <a href="#" class="account-dropdown__item">👤 Профиль</a>
              <a href="#" class="account-dropdown__item">📦 Мои заказы</a>
              <a href="#" class="account-dropdown__item">⚙️ Настройки</a>
              <hr class="account-dropdown__divider" />
              <a href="/login" class="account-dropdown__item account-dropdown__item--danger">🚪 Выйти</a>
            </div>
          </transition>
        </div>
      </div>
    </header>

    <!-- ── CART SIDEBAR ── -->
    <transition name="slide-right">
      <div v-if="cartOpen" class="cart-overlay" @click.self="cartOpen = false">
        <div class="cart-sidebar">
          <div class="cart-sidebar__header">
            <h3>Корзина 🛒</h3>
            <button class="cart-close" @click="cartOpen = false">✕</button>
          </div>
          <div v-if="cart.length === 0" class="cart-empty">
            <span>🌊</span>
            <p>Корзина пуста</p>
            <small>Добавь снаряжение для следующей сессии</small>
          </div>
          <div v-else class="cart-items">
            <div v-for="item in cart" :key="item.id" class="cart-item">
              <span class="cart-item__emoji">{{ item.emoji }}</span>
              <div class="cart-item__info">
                <p class="cart-item__name">{{ item.name }}</p>
                <p class="cart-item__price">{{ item.price }} ₴</p>
              </div>
              <div class="cart-item__qty">
                <button @click="decreaseQty(item)">−</button>
                <span>{{ item.qty }}</span>
                <button @click="item.qty++">+</button>
              </div>
              <button class="cart-item__remove" @click="removeFromCart(item)">✕</button>
            </div>
          </div>
          <div v-if="cart.length > 0" class="cart-sidebar__footer">
            <div class="cart-total">
              <span>Итого:</span>
              <span class="cart-total__price">{{ cartTotal }} ₴</span>
            </div>
            <button class="btn btn--primary">Оформить заказ 🤙</button>
          </div>
        </div>
      </div>
    </transition>

    <!-- ── HERO ── -->
    <section class="hero">
      <div class="hero__content">
        <p class="hero__sub">Новая коллекция 2026</p>
        <h1 class="hero__title">ОСЕДЛАЙ<br><span class="hero__accent">ИДЕАЛЬНУЮ</span><br>ВОЛНУ</h1>
        <p class="hero__desc">Профессиональное сёрф-снаряжение для тех, кто живёт океаном</p>
        <button class="btn btn--primary btn--lg">Смотреть каталог ↓</button>
      </div>
      <div class="hero__deco">
        <div class="hero__board">🏄‍♂️</div>
      </div>
    </section>

    <!-- ── WEATHER CAROUSEL ── -->
    <section class="weather-section">
      <div class="weather-section__header">
        <h2 class="section-title">🌊 Волны сегодня</h2>
        <p class="section-sub">Условия для сёрфинга по всему миру</p>
      </div>
      <div class="weather-carousel" ref="carousel">
        <button class="carousel-arrow carousel-arrow--left" @click="scrollCarousel(-1)">‹</button>
        <div class="weather-track" ref="track">
          <div
              v-for="spot in weatherSpots"
              :key="spot.city"
              class="weather-card"
              :class="'weather-card--' + spot.condition"
          >
            <div class="weather-card__flag">{{ spot.flag }}</div>
            <div class="weather-card__city">{{ spot.city }}</div>
            <div class="weather-card__country">{{ spot.country }}</div>
            <div class="weather-card__icon">{{ spot.icon }}</div>
            <div class="weather-card__temp">{{ spot.temp }}°C</div>
            <div class="weather-card__wind">💨 {{ spot.wind }} км/ч</div>
            <div class="weather-card__waves">🌊 {{ spot.waveHeight }}м</div>
            <div class="weather-card__rating">
              <span v-for="i in 5" :key="i" :class="i <= spot.rating ? 'star--on' : 'star--off'">★</span>
            </div>
            <div class="weather-card__badge" :class="'badge--' + spot.condition">{{ spot.label }}</div>
          </div>
        </div>
        <button class="carousel-arrow carousel-arrow--right" @click="scrollCarousel(1)">›</button>
      </div>
    </section>

    <!-- ── PRODUCTS ── -->
    <section class="products-section">
      <div class="products-section__header">
        <h2 class="section-title">Снаряжение</h2>
        <!-- Filter tabs -->
        <div class="filter-tabs">
          <button
              v-for="tab in tabs"
              :key="tab.id"
              class="filter-tab"
              :class="{ 'filter-tab--active': activeTab === tab.id }"
              @click="activeTab = tab.id"
          >{{ tab.label }}</button>
        </div>
      </div>

      <div class="products-grid">
        <div
            v-for="product in filteredProducts"
            :key="product.id"
            class="product-card"
            @mouseenter="product.hovered = true"
            @mouseleave="product.hovered = false"
        >
          <div class="product-card__img">
            <span class="product-card__emoji">{{ product.emoji }}</span>
            <div v-if="product.badge" class="product-card__badge">{{ product.badge }}</div>
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
              <button class="btn-add" @click="addToCart(product)">
                {{ product.hovered ? 'В корзину 🛒' : '+' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ── FOOTER ── -->
    <footer class="footer">
      <div class="footer__logo">
        <span class="logo__wave">〜</span>
        <span class="logo__text">WAVECRAFT</span>
        <span class="logo__wave">〜</span>
      </div>
      <p class="footer__copy">© 2026 WAVECRAFT. Ride the perfect wave.</p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'ShopPage',

  data() {
    return {
      username: localStorage.getItem('username') || 'Серфер',
      cartOpen: false,
      accountOpen: false,
      activeTab: 'all',
      cart: [],

      tabs: [
        { id: 'all', label: '🌊 Все' },
        { id: 'boards', label: '🏄 Доски' },
        { id: 'wetsuits', label: '🤿 Костюмы' },
        { id: 'accessories', label: '⚡ Аксессуары' },
      ],

      weatherSpots: [
        { city: 'Балі', country: 'Індонезія', flag: '🇮🇩', icon: '☀️', temp: 29, wind: 18, waveHeight: 2.1, rating: 5, condition: 'epic', label: 'ЭПИК' },
        { city: 'Назаре', country: 'Португалія', flag: '🇵🇹', icon: '🌤️', temp: 17, wind: 32, waveHeight: 8.5, rating: 5, condition: 'epic', label: 'МОНСТР' },
        { city: 'Гаваї', country: 'США', flag: '🇺🇸', icon: '🌊', temp: 26, wind: 22, waveHeight: 3.2, rating: 4, condition: 'good', label: 'ДОБРЕ' },
        { city: 'Джеффрис Бей', country: 'ПАР', flag: '🇿🇦', icon: '⛅', temp: 19, wind: 15, waveHeight: 1.8, rating: 4, condition: 'good', label: 'ДОБРЕ' },
        { city: 'Hossegor', country: 'Франція', flag: '🇫🇷', icon: '🌬️', temp: 14, wind: 28, waveHeight: 2.8, rating: 4, condition: 'good', label: 'ДОБРЕ' },
        { city: 'Маврикій', country: 'Маврикій', flag: '🇲🇺', icon: '☀️', temp: 27, wind: 12, waveHeight: 1.2, rating: 3, condition: 'ok', label: 'НОРМАЛЬНО' },
        { city: 'Одеса', country: 'Україна', flag: '🇺🇦', icon: '🌥️', temp: 8, wind: 20, waveHeight: 0.6, rating: 2, condition: 'poor', label: 'СЛАБО' },
        { city: 'Перт', country: 'Австралія', flag: '🇦🇺', icon: '☀️', temp: 24, wind: 16, waveHeight: 1.5, rating: 3, condition: 'ok', label: 'НОРМАЛЬНО' },
      ],

      products: [
        { id: 1, name: 'Shortboard Pro X', category: 'Дошки', emoji: '🏄', desc: 'Карбонова дошка для досвідчених сёрферів', price: 24500, oldPrice: 28000, badge: 'ХІТ', tag: 'boards', hovered: false },
        { id: 2, name: 'Longboard Classic', category: 'Дошки', emoji: '🌊', desc: 'Класика жанру для новачків і профі', price: 18900, badge: null, tag: 'boards', hovered: false },
        { id: 3, name: 'Fish Twin 5\'8"', category: 'Дошки', emoji: '🐟', desc: 'Ідеальна для маленьких хвиль', price: 15600, oldPrice: 17000, badge: '-8%', tag: 'boards', hovered: false },
        { id: 4, name: 'Гідрокостюм 5/4mm', category: 'Костюми', emoji: '🤿', desc: 'Для холодної води, гнучкий неопрен', price: 8900, badge: 'НОВИНКА', tag: 'wetsuits', hovered: false },
        { id: 5, name: 'Springsuit 3/2mm', category: 'Костюми', emoji: '🏊', desc: 'Літній костюм для теплих вод', price: 5400, badge: null, tag: 'wetsuits', hovered: false },
        { id: 6, name: 'Лиш FCS II', category: 'Аксесуари', emoji: '🔗', desc: 'Надійний шнур для будь-якої дошки', price: 890, badge: null, tag: 'accessories', hovered: false },
        { id: 7, name: 'Вакс Cold Water', category: 'Аксесуари', emoji: '🕯️', desc: 'Віск для холодної води 3 шт', price: 320, badge: null, tag: 'accessories', hovered: false },
        { id: 8, name: 'Рюкзак Boardbag', category: 'Аксесуари', emoji: '🎒', desc: 'Захисний чохол для дошки 7\'0"', price: 2100, oldPrice: 2500, badge: '-16%', tag: 'accessories', hovered: false },
      ],
    }
  },

  computed: {
    filteredProducts() {
      if (this.activeTab === 'all') return this.products
      return this.products.filter(p => p.tag === this.activeTab)
    },
    cartCount() {
      return this.cart.reduce((sum, i) => sum + i.qty, 0)
    },
    cartTotal() {
      return this.cart.reduce((sum, i) => sum + i.price * i.qty, 0).toLocaleString()
    },
  },

  methods: {
    addToCart(product) {
      const existing = this.cart.find(i => i.id === product.id)
      if (existing) {
        existing.qty++
      } else {
        this.cart.push({ ...product, qty: 1 })
      }
      this.cartOpen = true
    },
    removeFromCart(item) {
      this.cart = this.cart.filter(i => i.id !== item.id)
    },
    decreaseQty(item) {
      if (item.qty > 1) item.qty--
      else this.removeFromCart(item)
    },
    scrollCarousel(dir) {
      this.$refs.track.scrollBy({ left: dir * 300, behavior: 'smooth' })
    },
    handleClickOutside(e) {
      if (this.$refs.accountRef && !this.$refs.accountRef.contains(e.target)) {
        this.accountOpen = false
      }
    },
  },

  mounted() {
    document.addEventListener('click', this.handleClickOutside)
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside)
  },
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:ital,wght@0,300;0,400;0,500;0,600;1,300&display=swap');

:root {
  --ocean-deep: #03203C;
  --ocean-mid: #0A3D62;
  --ocean-teal: #0D7377;
  --wave-cyan: #14BDAC;
  --wave-light: #48E5D9;
  --foam: #E8FFF9;
  --sand: #FFF8F0;
  --sunset: #FF7043;
  --text-primary: #E8FFF9;
  --text-muted: #7EC8CC;
  --card-bg: rgba(3, 32, 60, 0.75);
  --radius: 18px;
}

*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

.shop {
  min-height: 100vh;
  font-family: 'DM Sans', sans-serif;
  color: var(--text-primary);
  background: var(--ocean-deep);
  overflow-x: hidden;
}

/* ── Ocean BG ── */
.ocean-bg {
  position: fixed;
  inset: 0;
  background: linear-gradient(160deg, #001A2E 0%, #03203C 40%, #0A3D62 80%, #0D7377 100%);
  z-index: 0;
  pointer-events: none;
}
.wave {
  position: absolute;
  bottom: 0; left: -10%;
  width: 120%;
  border-radius: 50% 50% 0 0;
  opacity: 0.1;
  animation: wave-roll linear infinite;
}
.wave-1 { height: 180px; background: #14BDAC; animation-duration: 9s; }
.wave-2 { height: 120px; background: #48E5D9; animation-duration: 13s; animation-delay: -4s; }
@keyframes wave-roll {
  0%   { transform: translateX(0); }
  50%  { transform: translateX(-4%); }
  100% { transform: translateX(0); }
}

/* ── NAVBAR ── */
.navbar {
  position: sticky;
  top: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  height: 68px;
  background: rgba(3, 32, 60, 0.88);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(20, 189, 172, 0.15);
}

.navbar__logo {
  display: flex;
  align-items: center;
  gap: 8px;
}
.logo__text {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.8rem;
  letter-spacing: 0.14em;
  color: var(--wave-light);
  text-shadow: 0 0 20px rgba(72,229,217,0.4);
}
.logo__wave {
  color: var(--wave-cyan);
  font-size: 1.2rem;
  animation: wave-bounce 2s ease-in-out infinite;
}
.logo__wave:last-child { animation-delay: -1s; }
@keyframes wave-bounce {
  0%,100% { transform: scaleX(1); }
  50% { transform: scaleX(1.3); }
}

.navbar__links {
  display: flex;
  gap: 32px;
}
.nav-link {
  font-size: 0.85rem;
  font-weight: 500;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--text-muted);
  text-decoration: none;
  transition: color 0.2s;
  padding-bottom: 2px;
  border-bottom: 2px solid transparent;
}
.nav-link:hover, .nav-link--active {
  color: var(--wave-light);
  border-bottom-color: var(--wave-cyan);
}

.navbar__actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.action-btn {
  position: relative;
  background: rgba(20,189,172,0.1);
  border: 1px solid rgba(20,189,172,0.25);
  border-radius: 10px;
  padding: 8px 14px;
  color: var(--foam);
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  font-family: 'DM Sans', sans-serif;
  font-size: 0.88rem;
  transition: background 0.2s, border-color 0.2s;
}
.action-btn:hover {
  background: rgba(20,189,172,0.2);
  border-color: var(--wave-cyan);
}
.action-btn__badge {
  position: absolute;
  top: -6px; right: -6px;
  background: var(--sunset);
  color: white;
  font-size: 0.65rem;
  font-weight: 700;
  width: 18px; height: 18px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* ── Account dropdown ── */
.account-menu { position: relative; }
.account-dropdown {
  position: absolute;
  top: calc(100% + 10px);
  right: 0;
  width: 220px;
  background: rgba(3, 32, 60, 0.96);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(20,189,172,0.2);
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 12px 40px rgba(0,0,0,0.4);
  z-index: 200;
}
.account-dropdown__header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}
.account-avatar {
  width: 40px; height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--wave-cyan), var(--ocean-teal));
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.2rem;
  color: var(--ocean-deep);
  flex-shrink: 0;
}
.account-dropdown__username {
  font-weight: 600;
  font-size: 0.9rem;
  color: var(--foam);
}
.account-dropdown__role {
  font-size: 0.75rem;
  color: var(--text-muted);
}
.account-dropdown__divider {
  border: none;
  border-top: 1px solid rgba(20,189,172,0.15);
  margin: 10px 0;
}
.account-dropdown__item {
  display: block;
  padding: 8px 10px;
  border-radius: 8px;
  font-size: 0.85rem;
  color: var(--text-muted);
  text-decoration: none;
  transition: background 0.15s, color 0.15s;
}
.account-dropdown__item:hover {
  background: rgba(20,189,172,0.1);
  color: var(--foam);
}
.account-dropdown__item--danger:hover {
  background: rgba(255,107,107,0.1);
  color: #FF6B6B;
}

/* ── CART SIDEBAR ── */
.cart-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.5);
  z-index: 300;
  display: flex;
  justify-content: flex-end;
}
.cart-sidebar {
  width: 380px;
  height: 100%;
  background: rgba(3, 32, 60, 0.97);
  backdrop-filter: blur(20px);
  border-left: 1px solid rgba(20,189,172,0.2);
  display: flex;
  flex-direction: column;
  padding: 28px;
}
.cart-sidebar__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.cart-sidebar__header h3 {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.5rem;
  letter-spacing: 0.08em;
  color: var(--wave-light);
}
.cart-close {
  background: none;
  border: none;
  color: var(--text-muted);
  font-size: 1.1rem;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background 0.15s;
}
.cart-close:hover { background: rgba(255,255,255,0.05); }

.cart-empty {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: var(--text-muted);
}
.cart-empty span { font-size: 3rem; }
.cart-empty p { font-size: 1rem; font-weight: 500; color: var(--foam); }
.cart-empty small { font-size: 0.8rem; text-align: center; }

.cart-items { flex: 1; overflow-y: auto; display: flex; flex-direction: column; gap: 12px; }
.cart-item {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(14,61,98,0.5);
  border: 1px solid rgba(20,189,172,0.15);
  border-radius: 12px;
  padding: 12px;
}
.cart-item__emoji { font-size: 1.8rem; }
.cart-item__info { flex: 1; }
.cart-item__name { font-size: 0.85rem; font-weight: 600; color: var(--foam); }
.cart-item__price { font-size: 0.78rem; color: var(--wave-cyan); }
.cart-item__qty {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.85rem;
}
.cart-item__qty button {
  background: rgba(20,189,172,0.15);
  border: 1px solid rgba(20,189,172,0.3);
  color: var(--foam);
  width: 24px; height: 24px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
}
.cart-item__remove {
  background: none;
  border: none;
  color: var(--text-muted);
  cursor: pointer;
  font-size: 0.8rem;
  padding: 4px;
  border-radius: 4px;
  transition: color 0.15s;
}
.cart-item__remove:hover { color: #FF6B6B; }

.cart-sidebar__footer {
  margin-top: 20px;
  border-top: 1px solid rgba(20,189,172,0.15);
  padding-top: 20px;
}
.cart-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
  font-size: 0.9rem;
  color: var(--text-muted);
}
.cart-total__price {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.4rem;
  color: var(--wave-light);
}

/* ── HERO ── */
.hero {
  position: relative;
  z-index: 1;
  min-height: 70vh;
  display: flex;
  align-items: center;
  padding: 80px 40px 60px;
  overflow: hidden;
}
.hero__content { max-width: 560px; }
.hero__sub {
  font-size: 0.78rem;
  letter-spacing: 0.3em;
  text-transform: uppercase;
  color: var(--wave-cyan);
  margin-bottom: 16px;
}
.hero__title {
  font-family: 'Bebas Neue', sans-serif;
  font-size: clamp(3.5rem, 8vw, 7rem);
  line-height: 0.95;
  letter-spacing: 0.04em;
  color: var(--foam);
  margin-bottom: 20px;
}
.hero__accent {
  color: var(--wave-cyan);
  text-shadow: 0 0 40px rgba(20,189,172,0.5);
}
.hero__desc {
  font-size: 1rem;
  color: var(--text-muted);
  line-height: 1.6;
  margin-bottom: 32px;
  font-style: italic;
}
.hero__deco {
  position: absolute;
  right: 5%;
  font-size: 14rem;
  opacity: 0.12;
  animation: float-drift 6s ease-in-out infinite;
  pointer-events: none;
}
@keyframes float-drift {
  0%,100% { transform: translateY(0) rotate(-5deg); }
  50% { transform: translateY(-20px) rotate(5deg); }
}

/* ── WEATHER SECTION ── */
.weather-section {
  position: relative;
  z-index: 1;
  padding: 60px 40px;
}
.section-title {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 2.2rem;
  letter-spacing: 0.08em;
  color: var(--foam);
  margin-bottom: 6px;
}
.section-sub {
  font-size: 0.85rem;
  color: var(--text-muted);
  margin-bottom: 28px;
}
.weather-section__header { margin-bottom: 28px; }

.weather-carousel { position: relative; display: flex; align-items: center; }
.weather-track {
  display: flex;
  gap: 16px;
  overflow-x: auto;
  scroll-snap-type: x mandatory;
  scrollbar-width: none;
  padding: 10px 4px;
  flex: 1;
}
.weather-track::-webkit-scrollbar { display: none; }

.weather-card {
  flex-shrink: 0;
  width: 180px;
  background: rgba(14, 61, 98, 0.6);
  backdrop-filter: blur(14px);
  border-radius: 16px;
  border: 1px solid rgba(20,189,172,0.2);
  padding: 20px 16px;
  scroll-snap-align: start;
  transition: transform 0.25s, box-shadow 0.25s;
  cursor: pointer;
  text-align: center;
}
.weather-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 32px rgba(0,0,0,0.3);
  border-color: var(--wave-cyan);
}
.weather-card--epic { border-color: rgba(20,189,172,0.5); }
.weather-card--good { border-color: rgba(105,219,124,0.4); }
.weather-card--ok   { border-color: rgba(255,193,7,0.3); }
.weather-card--poor { border-color: rgba(255,107,107,0.3); }

.weather-card__flag { font-size: 1.8rem; margin-bottom: 6px; }
.weather-card__city { font-family: 'Bebas Neue', sans-serif; font-size: 1.1rem; letter-spacing: 0.06em; color: var(--foam); }
.weather-card__country { font-size: 0.7rem; color: var(--text-muted); margin-bottom: 12px; }
.weather-card__icon { font-size: 2rem; margin-bottom: 4px; }
.weather-card__temp { font-family: 'Bebas Neue', sans-serif; font-size: 2rem; color: var(--wave-light); }
.weather-card__wind, .weather-card__waves { font-size: 0.75rem; color: var(--text-muted); margin-top: 4px; }
.weather-card__rating { margin-top: 10px; font-size: 0.85rem; }
.star--on { color: #FFD700; }
.star--off { color: rgba(255,255,255,0.2); }
.weather-card__badge {
  margin-top: 10px;
  display: inline-block;
  padding: 3px 10px;
  border-radius: 20px;
  font-size: 0.65rem;
  font-weight: 700;
  letter-spacing: 0.1em;
}
.badge--epic  { background: rgba(20,189,172,0.2); color: var(--wave-cyan); border: 1px solid var(--wave-cyan); }
.badge--good  { background: rgba(105,219,124,0.15); color: #69DB7C; border: 1px solid #69DB7C; }
.badge--ok    { background: rgba(255,193,7,0.15); color: #FFD700; border: 1px solid #FFD700; }
.badge--poor  { background: rgba(255,107,107,0.15); color: #FF6B6B; border: 1px solid #FF6B6B; }

.carousel-arrow {
  background: rgba(20,189,172,0.1);
  border: 1px solid rgba(20,189,172,0.3);
  color: var(--wave-light);
  width: 40px; height: 40px;
  border-radius: 50%;
  font-size: 1.4rem;
  cursor: pointer;
  flex-shrink: 0;
  transition: background 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}
.carousel-arrow:hover { background: rgba(20,189,172,0.25); }
.carousel-arrow--left { margin-right: 10px; }
.carousel-arrow--right { margin-left: 10px; }

/* ── PRODUCTS ── */
.products-section {
  position: relative;
  z-index: 1;
  padding: 40px 40px 80px;
}
.products-section__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 32px;
  flex-wrap: wrap;
  gap: 16px;
}

.filter-tabs { display: flex; gap: 8px; flex-wrap: wrap; }
.filter-tab {
  padding: 8px 18px;
  border-radius: 30px;
  border: 1px solid rgba(20,189,172,0.25);
  background: transparent;
  color: var(--text-muted);
  font-family: 'DM Sans', sans-serif;
  font-size: 0.82rem;
  cursor: pointer;
  transition: all 0.2s;
}
.filter-tab:hover { border-color: var(--wave-cyan); color: var(--foam); }
.filter-tab--active {
  background: linear-gradient(135deg, var(--wave-cyan), var(--ocean-teal));
  border-color: transparent;
  color: var(--ocean-deep);
  font-weight: 600;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 24px;
}

.product-card {
  background: rgba(14, 61, 98, 0.55);
  backdrop-filter: blur(14px);
  border: 1px solid rgba(20,189,172,0.18);
  border-radius: var(--radius);
  overflow: hidden;
  transition: transform 0.25s, box-shadow 0.25s, border-color 0.25s;
  animation: card-enter 0.5s ease both;
}
.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 50px rgba(0,0,0,0.35);
  border-color: rgba(20,189,172,0.45);
}
@keyframes card-enter {
  from { opacity: 0; transform: translateY(20px); }
  to   { opacity: 1; transform: translateY(0); }
}

.product-card__img {
  position: relative;
  height: 160px;
  background: linear-gradient(135deg, rgba(13,115,119,0.3), rgba(10,61,98,0.5));
  display: flex;
  align-items: center;
  justify-content: center;
}
.product-card__emoji { font-size: 5rem; filter: drop-shadow(0 4px 16px rgba(0,0,0,0.3)); }
.product-card__badge {
  position: absolute;
  top: 12px; right: 12px;
  background: var(--sunset);
  color: white;
  font-size: 0.68rem;
  font-weight: 700;
  letter-spacing: 0.08em;
  padding: 4px 10px;
  border-radius: 20px;
}

.product-card__body { padding: 18px; }
.product-card__category { font-size: 0.7rem; letter-spacing: 0.18em; text-transform: uppercase; color: var(--wave-cyan); margin-bottom: 4px; }
.product-card__name { font-family: 'Bebas Neue', sans-serif; font-size: 1.25rem; letter-spacing: 0.06em; color: var(--foam); margin-bottom: 6px; }
.product-card__desc { font-size: 0.8rem; color: var(--text-muted); line-height: 1.5; margin-bottom: 16px; }

.product-card__footer { display: flex; align-items: center; justify-content: space-between; }
.price-main { font-family: 'Bebas Neue', sans-serif; font-size: 1.4rem; color: var(--wave-light); }
.price-old { font-size: 0.8rem; color: var(--text-muted); text-decoration: line-through; margin-left: 8px; }

.btn-add {
  background: linear-gradient(135deg, var(--wave-cyan), var(--ocean-teal));
  border: none;
  color: var(--ocean-deep);
  font-family: 'DM Sans', sans-serif;
  font-weight: 600;
  font-size: 0.8rem;
  padding: 8px 16px;
  border-radius: 10px;
  cursor: pointer;
  transition: transform 0.18s, box-shadow 0.18s;
  white-space: nowrap;
}
.btn-add:hover { transform: scale(1.05); box-shadow: 0 4px 16px rgba(20,189,172,0.4); }

/* ── BUTTONS ── */
.btn {
  border: none;
  border-radius: 12px;
  font-family: 'Bebas Neue', sans-serif;
  letter-spacing: 0.1em;
  cursor: pointer;
  transition: transform 0.18s, box-shadow 0.18s;
}
.btn--primary {
  background: linear-gradient(135deg, var(--wave-cyan), var(--ocean-teal));
  color: var(--ocean-deep);
  padding: 12px 28px;
  font-size: 1rem;
  box-shadow: 0 4px 20px rgba(20,189,172,0.3);
}
.btn--primary:hover { transform: translateY(-2px); box-shadow: 0 8px 28px rgba(20,189,172,0.45); }
.btn--lg { padding: 16px 40px; font-size: 1.15rem; }

/* ── FOOTER ── */
.footer {
  position: relative;
  z-index: 1;
  text-align: center;
  padding: 40px;
  border-top: 1px solid rgba(20,189,172,0.1);
}
.footer__logo { display: flex; align-items: center; justify-content: center; gap: 8px; margin-bottom: 10px; }
.footer__copy { font-size: 0.78rem; color: var(--text-muted); letter-spacing: 0.1em; }

/* ── TRANSITIONS ── */
.dropdown-enter-active, .dropdown-leave-active { transition: opacity 0.2s, transform 0.2s; }
.dropdown-enter-from, .dropdown-leave-to { opacity: 0; transform: translateY(-8px); }

.slide-right-enter-active, .slide-right-leave-active { transition: opacity 0.3s; }
.slide-right-enter-from, .slide-right-leave-to { opacity: 0; }
.slide-right-enter-active .cart-sidebar, .slide-right-leave-active .cart-sidebar { transition: transform 0.3s; }
.slide-right-enter-from .cart-sidebar, .slide-right-leave-to .cart-sidebar { transform: translateX(100%); }
</style>