import { createRouter, createWebHistory } from 'vue-router'
import RegisterPage from '../views/RegisterPage.vue'
import LoginPage from '../views/LoginPage.vue'
import ShopPage from '../views/ShopPage.vue'


const routes = [
    { path: '/', redirect: '/login' },
    { path: '/login', component: LoginPage },
    { path: '/register', component: RegisterPage },
    { path: '/shop', component: ShopPage }
]

export default createRouter({
    history: createWebHistory(),
    routes
})