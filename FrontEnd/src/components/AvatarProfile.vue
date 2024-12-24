<template>
  <div>
    <div class="offcanvas offcanvas-end" tabindex="-1" id="Id2" data-bs-scroll="true"
         aria-labelledby="staticBackdropLabel">
      <div class="offcanvas-header">
        <div class="profile-container">
          <div class="avatar-wrapper">
            <div class="avatar">
              {{ memberprofile.username ? memberprofile.username.charAt(0).toUpperCase() : 'U' }}
            </div>
          </div>
          <div class="user-info">
            <h5 class="username">{{ memberprofile.username }}</h5>
            <div class="points">
              <i class="fas fa-coins"></i>
              <span>{{ memberprofile.userpoint }} 點</span>
            </div>
          </div>
        </div>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="offcanvas"
                aria-label="Close"></button>
      </div>
      <div class="offcanvas-body">
        <ul class="nav flex-column">
          <li class="nav-item">
            <router-link to="/membercenter/index" @click="closeOffcanvas" class="nav-link">
              <i class="bi bi-person-fill"></i>
              <span>會員中心</span>
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/membercenter/profile" @click="closeOffcanvas" class="nav-link">
              <i class="bi bi-person-square"></i>
              <span>個人資料</span>
            </router-link>
          </li>
          <li class="nav-item logout-item">
            <router-link to="/" @click="logoutOffcanvas" class="nav-link logout-link">
              <i class="bi bi-box-arrow-right"></i>
              <span>登出</span>
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import Offcanvas from 'bootstrap/js/dist/offcanvas';
import { useUserStore } from '@/stores/userStore'
import { mapState, mapActions } from 'pinia'

export default {
  name: 'MemberMenu',
  data() {
    return {
      offcanvas: null,
    }
  },
  computed: {
    ...mapState(useUserStore, ['memberprofile']),
  },
  methods: {
    ...mapActions(useUserStore, ['clearLoggedIn']),
    openOffcanvas() {
      const offcanvasElement = document.getElementById('Id2');
      this.offcanvas = new Offcanvas(offcanvasElement);
      this.offcanvas.show();
    },
    closeOffcanvas() {
      this.offcanvas.hide();
    },
    logoutOffcanvas() {
      this.offcanvas.hide();
      this.clearLoggedIn();
    }
  }
};
</script>

<style scoped>
.offcanvas {
  width: 300px;
  background-color: #ffffff;
  box-shadow: -4px 0 12px rgba(0, 0, 0, 0.15);
}

.offcanvas-header {
  background: linear-gradient(135deg, #222527 0%, #1a1a1a 100%);
  color: white;
  padding: 1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.profile-container {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.avatar-wrapper {
  padding: 3px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
}

.avatar {
  width: 48px;
  height: 48px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: 600;
  color: white;
  text-transform: uppercase;
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.2);
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.username {
  margin: 0;
  font-size: 1.1rem;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.points {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  opacity: 0.9;
}

.points i {
  color: #ffd700;
  text-shadow: 0 0 5px rgba(255, 215, 0, 0.5);
}

.offcanvas-body {
  padding: 1rem 0;
}

.nav-item {
  margin: 0.25rem 0;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.75rem 1.5rem;
  color: #333;
  transition: all 0.3s ease;
  font-weight: 500;
}

.nav-link i {
  width: 20px;
  text-align: center;
  font-size: 1.1rem;
  color: #2c3e50;
}

.nav-link:hover {
  background-color: #f8f9fa;
  color: #2c3e50;
  padding-left: 1.75rem;
}

.logout-item {
  margin-top: 1rem;
  border-top: 1px solid #eee;
}

.logout-link {
  color: #dc3545;
}

.logout-link i {
  color: #dc3545;
}

.logout-link:hover {
  background-color: #fff5f5;
  color: #dc3545;
}

.btn-close-white {
  opacity: 0.8;
  transition: opacity 0.3s ease;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
}

.btn-close-white:hover {
  opacity: 1;
}
</style>
