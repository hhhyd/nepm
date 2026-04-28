<template>
  <div class="dashboard">
    <Header />
    
    <el-container class="main-container">
      <el-aside width="260px">
        <el-menu
          :default-active="$route.path"
          router
          class="side-menu"
          background-color="#0a2b3e"
          text-color="#b8d4e8"
          active-text-color="#40c4ff"
        >
          <div class="logo-area">
            <el-icon class="logo-icon"><WindPower /></el-icon>
            <span class="logo-text">智慧环境云</span>
          </div>
          <el-menu-item index="/supervisor/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/supervisor/myFeedback">
            <el-icon><ChatDotRound /></el-icon>
            <span>我的反馈</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-main>
        <div class="page-header">
          <div class="header-title">
            <el-icon class="title-icon"><Document /></el-icon>
            <h2>我的反馈记录</h2>
          </div>
          <p class="subtitle">查看您提交的所有空气质量反馈及其处理状态</p>
        </div>

        <el-card class="feedback-table-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <div class="header-left">
                <span class="card-title">📋 反馈列表</span>
                <el-badge :value="feedbackList.length" class="badge-count" :hidden="!feedbackList.length">
                  <span class="count-text">共</span>
                </el-badge>
              </div>
              <div class="header-right">
                <el-button 
                  type="primary" 
                  link 
                  @click="loadFeedbackList" 
                  :loading="loading"
                  :icon="Refresh"
                >
                  刷新
                </el-button>
              </div>
            </div>
          </template>
          
          <el-table 
            :data="feedbackList" 
            style="width: 100%" 
            v-loading="loading"
            class="custom-table"
            :header-cell-style="{ background: '#f0f8fc', color: '#0a4b6e', fontWeight: '600' }"
            stripe
            border
          >
            <el-table-column prop="afId" label="编号" width="80" align="center" />
            <el-table-column label="省份" width="120">
              <template #default="{ row }">
                <div class="location-cell">
                  <el-icon><Location /></el-icon>
                  <span>{{ row.gridProvince?.provinceName || row.provinceId }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="城市" width="120">
              <template #default="{ row }">
                <div class="location-cell">
                  <el-icon><OfficeBuilding /></el-icon>
                  <span>{{ row.gridCity?.cityName || row.cityId }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="address" label="地址" min-width="180" show-overflow-tooltip />
            <el-table-column prop="information" label="反馈内容" min-width="200" show-overflow-tooltip />
            <el-table-column label="预估等级" width="130" align="center">
              <template #default="{ row }">
                <el-tag :type="getLevelType(row.estimatedGrade)" effect="light" size="default" class="level-tag">
                  {{ getLevelText(row.estimatedGrade) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="处理状态" width="110" align="center">
              <template #default="{ row }">
                <el-tag :type="getStateType(row.state)" effect="dark" size="small" class="state-tag">
                  <span class="state-dot" :class="`state-dot-${row.state}`"></span>
                  {{ getStateText(row.state) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="反馈时间" width="170" align="center">
              <template #default="{ row }">
                <div class="time-cell">
                  <el-icon><Clock /></el-icon>
                  <span>{{ row.afDate }} {{ row.afTime }}</span>
                </div>
              </template>
            </el-table-column>
          </el-table>

          <div class="table-footer" v-if="feedbackList.length === 0 && !loading">
            <el-empty description="暂无反馈记录，快去提交第一条反馈吧~" :image-size="120">
              <el-button type="primary" @click="$router.push('/supervisor/dashboard')">
                去提交反馈
              </el-button>
            </el-empty>
          </div>
        </el-card>

        <div class="statistics-card">
          <el-row :gutter="20">
            <el-col :xs="12" :sm="6">
              <div class="stat-item">
                <div class="stat-icon blue-bg">
                  <el-icon><ChatDotRound /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-number">{{ statistics.total }}</div>
                  <div class="stat-label">总反馈数</div>
                </div>
              </div>
            </el-col>
            <el-col :xs="12" :sm="6">
              <div class="stat-item">
                <div class="stat-icon orange-bg">
                  <el-icon><Clock /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-number">{{ statistics.pending }}</div>
                  <div class="stat-label">待处理</div>
                </div>
              </div>
            </el-col>
            <el-col :xs="12" :sm="6">
              <div class="stat-item">
                <div class="stat-icon blue-bg">
                  <el-icon><Position /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-number">{{ statistics.assigned }}</div>
                  <div class="stat-label">处理中</div>
                </div>
              </div>
            </el-col>
            <el-col :xs="12" :sm="6">
              <div class="stat-item">
                <div class="stat-icon green-bg">
                  <el-icon><CircleCheck /></el-icon>
                </div>
                <div class="stat-info">
                  <div class="stat-number">{{ statistics.confirmed }}</div>
                  <div class="stat-label">已完成</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import { useUserStore } from '@/stores/user'
import { getFeedbackListByTelId } from '@/api/aqiFeedback'
import { ElMessage } from 'element-plus'
import { 
  HomeFilled, ChatDotRound, Document, WindPower, 
  Location, OfficeBuilding, Clock, Refresh, Position, CircleCheck 
} from '@element-plus/icons-vue'

const router = useRouter()
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

const getStateText = (state) => {
  const stateMap = { 0: '未指派', 1: '已指派', 2: '已确认' }
  return stateMap[state] || '未知'
}

const getStateType = (state) => {
  const typeMap = { 0: 'info', 1: 'warning', 2: 'success' }
  return typeMap[state] || 'info'
}

// 统计数据
const statistics = computed(() => {
  const total = feedbackList.value.length
  const pending = feedbackList.value.filter(item => item.state === 0).length
  const assigned = feedbackList.value.filter(item => item.state === 1).length
  const confirmed = feedbackList.value.filter(item => item.state === 2).length
  return { total, pending, assigned, confirmed }
})

const loadFeedbackList = async () => {
  loading.value = true
  try {
    const telId = userStore.userInfo?.telId || 
                  JSON.parse(localStorage.getItem('user-info') || '{}').telId
    if (!telId) {
      ElMessage.warning('请先登录')
      return
    }
    const result = await getFeedbackListByTelId({ telId })
    feedbackList.value = result || []
    if (feedbackList.value.length === 0) {
      ElMessage.info('暂无反馈记录')
    }
  } catch (error) {
    console.error('加载反馈列表失败:', error)
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
  background: linear-gradient(135deg, #e8f4f8 0%, #d9eef5 100%);
}

.main-container {
  min-height: calc(100vh - 60px);
}

/* 侧边栏样式 */
.el-aside {
  background: linear-gradient(180deg, #0a2b3e 0%, #0d3550 100%);
  border-right: 1px solid rgba(64, 196, 255, 0.15);
  box-shadow: 2px 0 12px rgba(0, 40, 60, 0.15);
}

.logo-area {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px 16px;
  margin-bottom: 16px;
  border-bottom: 1px solid rgba(64, 196, 255, 0.2);
}

.logo-icon {
  font-size: 32px;
  color: #40c4ff;
  margin-right: 12px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  background: linear-gradient(135deg, #40c4ff 0%, #7fd9ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: 2px;
}

.side-menu:not(.el-menu--collapse) {
  width: 100%;
  border-right: none;
}

.side-menu .el-menu-item {
  margin: 8px 16px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.side-menu .el-menu-item:hover {
  background: rgba(64, 196, 255, 0.15) !important;
  transform: translateX(4px);
}

.side-menu .el-menu-item.is-active {
  background: linear-gradient(135deg, #40c4ff 0%, #00b0f0 100%);
  color: white !important;
}

/* 主内容区 */
.el-main {
  padding: 28px 32px;
}

.page-header {
  margin-bottom: 24px;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.title-icon {
  font-size: 28px;
  color: #2c8fc9;
}

.page-header h2 {
  margin: 0;
  font-size: 26px;
  font-weight: 600;
  background: linear-gradient(135deg, #0a2b3e 0%, #1a5a7a 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  color: #5a9ab5;
  margin: 0 0 0 40px;
  font-size: 14px;
}

/* 统计卡片 */
.statistics-card {
  margin-bottom: 24px;
}

.stat-item {
  background: white;
  border-radius: 16px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 4px 12px rgba(0, 60, 80, 0.08);
  transition: all 0.3s ease;
  border: 1px solid rgba(64, 196, 255, 0.2);
}

.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 80, 100, 0.12);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.stat-icon.blue-bg {
  background: linear-gradient(135deg, #40c4ff 0%, #2c8fc9 100%);
}

.stat-icon.orange-bg {
  background: linear-gradient(135deg, #ff9800 0%, #e68a00 100%);
}

.stat-icon.green-bg {
  background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #0a2b3e;
  line-height: 1.2;
}

.stat-label {
  font-size: 13px;
  color: #7ab5ce;
  margin-top: 4px;
}

/* 表格卡片 */
.feedback-table-card {
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0, 80, 100, 0.08);
  border: 1px solid rgba(64, 196, 255, 0.2);
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #0a4b6e;
}

.badge-count {
  margin-left: 8px;
}

.count-text {
  font-size: 13px;
  color: #5a9ab5;
}

/* 表格样式 */
.custom-table {
  border-radius: 12px;
  overflow: hidden;
}

:deep(.custom-table .el-table__header-wrapper th) {
  background: #f0f8fc;
  color: #0a4b6e;
  font-weight: 600;
}

:deep(.custom-table .el-table__row:hover) {
  background: #f0f8fc;
  transition: background 0.3s;
}

.location-cell, .time-cell {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #2c8fc9;
}

.location-cell .el-icon, .time-cell .el-icon {
  font-size: 14px;
}

.level-tag {
  font-weight: 500;
  border-radius: 20px;
  padding: 4px 12px;
}

.state-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  border-radius: 20px;
  padding: 4px 12px;
}

.state-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  display: inline-block;
}

.state-dot-0 { background: #909399; }
.state-dot-1 { background: #e6a23c; }
.state-dot-2 { background: #67c23a; }

.table-footer {
  padding: 20px;
  text-align: center;
}

/* 响应式 */
@media (max-width: 768px) {
  .el-main {
    padding: 20px 16px;
  }
  
  .page-header h2 {
    font-size: 22px;
  }
  
  .stat-item {
    padding: 12px;
  }
  
  .stat-icon {
    width: 36px;
    height: 36px;
    font-size: 18px;
  }
  
  .stat-number {
    font-size: 20px;
  }
}
</style>