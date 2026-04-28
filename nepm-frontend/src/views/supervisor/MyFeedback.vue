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
          <el-menu-item index="/supervisor/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/supervisor/myFeedback">
            <el-icon><Document /></el-icon>
            <span>我的反馈</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-main>
        <h2>我的反馈记录</h2>
        
        <el-table :data="feedbackList" style="width: 100%; margin-top: 20px" v-loading="loading">
          <el-table-column prop="afId" label="编号" width="80" />
          <el-table-column label="省份" width="100">
            <template #default="{ row }">
              {{ row.gridProvince?.provinceName || row.provinceId }}
            </template>
          </el-table-column>
          <el-table-column label="城市" width="100">
            <template #default="{ row }">
              {{ row.gridCity?.cityName || row.cityId }}
            </template>
          </el-table-column>
          <el-table-column prop="address" label="地址" />
          <el-table-column prop="information" label="反馈内容" />
          <el-table-column label="预估等级" width="100">
            <template #default="{ row }">
              <el-tag :type="getLevelType(row.estimatedGrade)">
                {{ getLevelText(row.estimatedGrade) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.state === 0 ? 'info' : row.state === 1 ? 'warning' : 'success'">
                {{ row.state === 0 ? '未指派' : row.state === 1 ? '已指派' : '已确认' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="反馈时间" width="180">
            <template #default="{ row }">
              {{ row.afDate }} {{ row.afTime }}
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import { useUserStore } from '@/stores/user'
import { getFeedbackListByTelId } from '@/api/aqiFeedback'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const loading = ref(false)
const feedbackList = ref([])

const levelMap = {
  1: '优', 2: '良', 3: '轻度污染',
  4: '中度污染', 5: '重度污染', 6: '严重污染'
}

const getLevelText = (level) => levelMap[level] || '未知'

const getLevelType = (level) => {
  const typeMap = {
    1: 'success', 2: 'success', 3: 'warning',
    4: 'warning', 5: 'danger', 6: 'danger'
  }
  return typeMap[level] || 'info'
}

const loadFeedbackList = async () => {
  loading.value = true
  try {
    const result = await getFeedbackListByTelId({
      telId: userStore.userInfo?.telId
    })
    feedbackList.value = result || []
  } catch (error) {
    ElMessage.error('加载反馈列表失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadFeedbackList()
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
