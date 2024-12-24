<template>
    <div>
        <div class="offcanvas offcanvas-end" tabindex="-1" id="Id2" data-bs-scroll="true"
            aria-labelledby="staticBackdropLabel">
            <div class="offcanvas-header">
                <div class="d-flex flex-column">
                    <h5 class="offcanvas-title" id="staticBackdropLabel">
                        Welcome! {{ memberprofile.username }}
                    </h5>
                    <h5 class="offcanvas-title" id="staticBackdropLabel">
                        紅利點數 {{ memberprofile.userpoint }}
                    </h5>
                </div>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="offcanvas"
                    aria-label="Close"></button>
            </div>
            <div class="offcanvas-body">
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <router-link to="/membercenter/index" @click="closeOffcanvas" class="nav-link">
                            <span>會員中心</span>
                        </router-link>
                    </li>
                    <li class="nav-item">
                        <router-link to="/membercenter/profile" @click="closeOffcanvas" class="nav-link">
                            <span>個人資料</span>
                        </router-link>
                    </li>
                    <li class="nav-item">
                        <router-link to="/" @click="logoutOffcanvas" class="nav-link">
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
    background-color: #f8f9fa;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.offcanvas-header {
    background-color: #343a40;
    color: white;
}

.offcanvas-body {
    padding: 0;
}

.nav-link {
    padding: 10px 15px;
    color: #343a40;
}

.nav-link:hover {
    background-color: #f1f1f1;
}
.offcanvas-title{
    font-size: 17px
}
</style>