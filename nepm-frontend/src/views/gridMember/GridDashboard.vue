<template>
  <div class="dashboard">
    <Header />

    <el-container class="main-container">
      <el-aside width="200px">
        <el-menu
            :default-active="$route.path"
            router
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b"
        >
          <el-menu-item index="/gridMember/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/gridMember/myTasks">
            <el-icon><Document /></el-icon>
            <span>我的任务</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-main>
        <h2>网格员工作台</h2>

        <el-row :gutter="20" style="margin-top: 20px">
          <el-col :span="8">
            <el-card shadow="hover">
              <template #header>待处理任务</template>
              <h3>{{ pendingCount }}</h3>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover">
              <template #header>已完成任务</template>
              <h3>{{ completedCount }}</h3>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover">
              <template #header>负责区域</template>
              <h3>{{ userStore.userInfo?.gmName }}</h3>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const pendingCount = ref(0)
const completedCount = ref(0)

onMounted(() => {
  userStore.initUser()
})
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
}

.main-container {
  min-height: calc(100vh - 60px);
}

.el-aside {
  background-color: #545c64;
}

.el-main {
  padding: 20px;
}
</style>
