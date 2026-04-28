<template>
  <el-header class="header">
    <div class="header-left">
      <h2>全国环境监测网格化平台</h2>
    </div>
    
    <div class="header-right">
      <span>欢迎，{{ userName }}</span>
      <el-button type="primary" link @click="handleLogout">
        退出登录
      </el-button>
    </div>
  </el-header>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const userName = computed(() => {
  const user = userStore.userInfo
  if (!user) return ''
  
  switch (userStore.userType) {
    case 'admin':
      return user.adminCode
    case 'supervisor':
      return user.realName
    case 'gridMember':
      return user.gmName
    default:
      return ''
  }
})

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #409EFF;
  color: white;
  padding: 0 20px;
}

.header-left h2 {
  margin: 0;
  font-size: 20px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-right span {
  color: white;
}
</style>
