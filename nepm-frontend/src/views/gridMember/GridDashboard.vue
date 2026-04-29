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
              <h3>{{ areaName }}</h3>
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
import { getFeedbackByGmId } from '@/api/aqiFeedback'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()

// 真实统计数据
const pendingCount = ref(0)
const completedCount = ref(0)
const areaName = ref('加载中...')

// 加载真实任务统计
const loadTaskCount = async () => {
  try {
    const user = userStore.userInfo
    if (!user || !user.gmId) {
      ElMessage.warning('请重新登录')
      return
    }

    // 1. 加载 待处理任务（state=1 已指派未完成）
    const pendingList = await getFeedbackByGmId({
      gmId: user.gmId,
      state: 1
    })
    pendingCount.value = pendingList?.length || 0

    // 2. 加载 已完成任务（state=2 已确认）
    const completedList = await getFeedbackByGmId({
      gmId: user.gmId,
      state: 2
    })
    completedCount.value = completedList?.length || 0

    // 3. 负责区域名称
    areaName.value = user.gmName || '未设置'

  } catch (err) {
    ElMessage.error('统计数据加载失败')
    console.error(err)
  }
}

onMounted(() => {
  userStore.initUser()
  loadTaskCount()
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
h3 {
  text-align: center;
  color: #409EFF;
  font-size: 32px;
  margin: 10px 0;
}
</style>