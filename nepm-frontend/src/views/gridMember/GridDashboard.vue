<template>
  <div class="dashboard">
    <Header />

    <el-container class="main-container">
      <el-aside width="260px">
        <el-menu
            :default-active="activeMenu"
            @select="handleMenuSelect"
            class="side-menu"
            background-color="#0a2b3e"
            text-color="#b8d4e8"
            active-text-color="#40c4ff"
        >
          <div class="logo-area">
            <el-icon class="logo-icon"><Grid /></el-icon>
            <span class="logo-text">智慧环境云</span>
          </div>
          <el-menu-item index="dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="myTasks">
            <el-icon><List /></el-icon>
            <span>我的任务</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-main>
        <!-- 首页内容 -->
        <div v-show="activeMenu === 'dashboard'">
          <div class="page-header">
            <div class="header-title">
              <el-icon class="title-icon"><DataAnalysis /></el-icon>
              <h2>网格员工作台</h2>
            </div>
            <p class="subtitle">实时掌握任务状态，守护环境质量</p>
          </div>

          <el-row :gutter="24" class="stats-row">
            <el-col :xs="24" :sm="8">
              <el-card class="stat-card clickable" shadow="hover" @click="showTasksByStatus(1)">
                <template #header>
                  <div class="card-header">
                    <el-icon class="header-icon pending"><Clock /></el-icon>
                    <span>待处理任务</span>
                  </div>
                </template>
                <div class="stat-value pending">{{ pendingCount }}</div>
                <div class="stat-desc">点击查看详情</div>
              </el-card>
            </el-col>
            <el-col :xs="24" :sm="8">
              <el-card class="stat-card clickable" shadow="hover" @click="showTasksByStatus(2)">
                <template #header>
                  <div class="card-header">
                    <el-icon class="header-icon completed"><CircleCheck /></el-icon>
                    <span>已完成任务</span>
                  </div>
                </template>
                <div class="stat-value completed">{{ completedCount }}</div>
                <div class="stat-desc">点击查看详情</div>
              </el-card>
            </el-col>
            <el-col :xs="24" :sm="8">
              <el-card class="stat-card" shadow="hover">
                <template #header>
                  <div class="card-header">
                    <el-icon class="header-icon area-icon"><Location /></el-icon>
                    <span>负责区域</span>
                  </div>
                </template>
                <div class="stat-value area-text">{{ areaName }}</div>
                <div class="stat-desc">您的网格管辖区域</div>
              </el-card>
            </el-col>
          </el-row>

          <div class="tips-card">
            <el-alert
                title="工作提示"
                type="info"
                :closable="false"
                show-icon
            >
              <template #default>
                <ul>
                  <li>请及时处理待检测任务，确保数据准确性</li>
                  <li>检测时请认真填写 SO2、CO、PM2.5 等关键指标</li>
                  <li>完成检测后系统会自动更新 AQI 数据</li>
                </ul>
              </template>
            </el-alert>
          </div>
        </div>

        <!-- 我的任务内容 -->
        <div v-show="activeMenu === 'myTasks'">
          <div class="page-header">
            <div class="header-title">
              <el-icon class="title-icon"><List /></el-icon>
              <h2>{{ currentTaskStatus === 1 ? '待处理任务' : '已完成任务' }}</h2>
            </div>
            <p class="subtitle">
              {{ currentTaskStatus === 1 ? '以下是需要您现场检测的任务列表' : '以下是您已完成的检测记录' }}
            </p>
          </div>

          <el-card class="task-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span> 任务列表</span>
                <div class="header-actions">
                  <el-radio-group v-model="currentTaskStatus" @change="onStatusChange" size="small">
                    <el-radio-button :label="1">待处理</el-radio-button>
                    <el-radio-button :label="2">已完成</el-radio-button>
                  </el-radio-group>
                  <el-tag type="info" effect="plain" class="info-tag">
                    共 {{ list.length }} 条记录
                  </el-tag>
                </div>
              </div>
            </template>

            <div v-if="loading" class="loading-container">
              <el-icon class="is-loading" :size="40"><Loading /></el-icon>
              <span>加载中...</span>
            </div>

            <div v-else-if="list.length === 0" class="empty-container">
              <el-empty :description="currentTaskStatus === 1 ? '暂无待处理任务' : '暂无已完成任务'" />
            </div>

            <div v-else class="task-list">
              <div
                  v-for="item in list"
                  :key="item.afId"
                  class="task-item"
              >
                <div class="task-main">
                  <div class="level-badge" :style="getLevelColor(item.estimatedGrade)">
                    {{ getLevelText(item.estimatedGrade) }}
                  </div>
                  <div class="task-info">
                    <div class="location">
                      <el-icon><Location /></el-icon>
                      <span>{{ item.gridProvince?.provinceName || '' }} {{ item.gridCity?.cityName || '' }}</span>
                    </div>
                    <div class="address">{{ item.address }}</div>
                    <div class="meta">
                      <el-tag size="small" type="warning" effect="plain">
                        {{ getGradeName(item.estimatedGrade) }}
                      </el-tag>
                      <span class="date">{{ currentTaskStatus === 1 ? (item.assignDate || '2022-09-13') : (item.confirmDate || '2022-09-13') }}</span>
                    </div>
                  </div>
                </div>
                <el-button
                    v-if="currentTaskStatus === 1"
                    type="primary"
                    class="detect-btn"
                    @click="openDetectDialog(item)"
                >
                  <el-icon><EditPen /></el-icon>
                  去检测
                </el-button>
                <el-tag v-else type="success" effect="plain" size="large">
                  <el-icon><CircleCheck /></el-icon>
                  已完成
                </el-tag>
              </div>
            </div>
          </el-card>
        </div>
      </el-main>
    </el-container>

    <!-- 检测弹窗 -->
    <el-dialog
        v-model="detectDialogVisible"
        title="空气质量检测"
        width="700px"
        :close-on-click-modal="false"
    >
      <el-form :model="detectForm" label-width="120px" :rules="detectRules" ref="detectFormRef">
        <el-divider content-position="left">任务信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="反馈者">
              <el-input :value="currentTask?.supervisor?.realName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input :value="currentTask?.telId" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="反馈地址">
          <el-input :value="`${currentTask?.gridProvince?.provinceName} ${currentTask?.gridCity?.cityName} ${currentTask?.address}`" disabled />
        </el-form-item>
        <el-form-item label="反馈描述">
          <el-input :value="currentTask?.information" type="textarea" :rows="2" disabled />
        </el-form-item>
        <el-form-item label="预估等级">
          <el-tag :type="getTagType(currentTask?.estimatedGrade)" size="large">
            {{ getGradeName(currentTask?.estimatedGrade) }}
          </el-tag>
        </el-form-item>

        <el-divider content-position="left">检测数据</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="SO2浓度" prop="so2Value">
              <el-input-number v-model="detectForm.so2Value" :min="0" :max="2620" style="width: 100%" />
              <span class="unit">μg/m³</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="CO浓度" prop="coValue">
              <el-input-number v-model="detectForm.coValue" :min="0" :max="150" style="width: 100%" />
              <span class="unit">mg/m³</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="PM2.5浓度" prop="spmValue">
              <el-input-number v-model="detectForm.spmValue" :min="0" :max="500" style="width: 100%" />
              <span class="unit">μg/m³</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注">
              <el-input v-model="detectForm.remarks" placeholder="选填" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <el-button @click="detectDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitDetect" :loading="submitLoading">
          提交检测
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import { useUserStore } from '@/stores/user'
import { getFeedbackByGmId, confirmFeedback } from '@/api/aqiFeedback'
import { getAllProvinces } from '@/api/gridProvince'
import { getCitiesByProvinceId } from '@/api/gridCity'
import { ElMessage } from 'element-plus'
import {
  HomeFilled,
  List,
  Grid,
  DataAnalysis,
  Clock,
  CircleCheck,
  Location,
  Loading,
  EditPen
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

// 当前激活的菜单项
const activeMenu = ref('dashboard')

// 统计数据
const pendingCount = ref(0)
const completedCount = ref(0)
const areaName = ref('加载中...')

// 任务列表
const list = ref([])
const loading = ref(false)
const currentTaskStatus = ref(1) // 1: 待处理, 2: 已完成

// 检测弹窗
const detectDialogVisible = ref(false)
const currentTask = ref(null)
const detectFormRef = ref(null)
const submitLoading = ref(false)

// 检测表单
const detectForm = ref({
  afId: null,
  so2Value: null,
  coValue: null,
  spmValue: null,
  remarks: ''
})

// 表单验证规则
const detectRules = {
  so2Value: [{ required: true, message: '请输入SO2浓度', trigger: 'blur' }],
  coValue: [{ required: true, message: '请输入CO浓度', trigger: 'blur' }],
  spmValue: [{ required: true, message: '请输入PM2.5浓度', trigger: 'blur' }]
}

// 打开检测弹窗
const openDetectDialog = (item) => {
  currentTask.value = item
  detectForm.value = {
    afId: item.afId,
    so2Value: null,
    coValue: null,
    spmValue: null,
    remarks: ''
  }
  detectDialogVisible.value = true
}

// 提交检测
const handleSubmitDetect = async () => {
  if (!detectFormRef.value) return

  await detectFormRef.value.validate(async (valid) => {
    if (!valid) return

    submitLoading.value = true
    try {
      const user = userStore.userInfo
      const now = new Date()
      const confirmDate = now.toISOString().split('T')[0]
      const confirmTime = now.toTimeString().split(' ')[0]

      await confirmFeedback({
        afId: detectForm.value.afId,
        so2Value: detectForm.value.so2Value,
        coValue: detectForm.value.coValue,
        spmValue: detectForm.value.spmValue,
        remarks: detectForm.value.remarks,
        confirmDate,
        confirmTime,
        gmId: user.gmId,
        state: 2
      })

      ElMessage.success('检测数据提交成功')
      detectDialogVisible.value = false
      loadMyTasks()
      loadStats()
    } catch (err) {
      ElMessage.error('提交失败，请重试')
      console.error(err)
    } finally {
      submitLoading.value = false
    }
  })
}

// 点击统计卡片显示对应任务
const showTasksByStatus = (status) => {
  currentTaskStatus.value = status
  activeMenu.value = 'myTasks'
  loadMyTasks()
}

// 切换任务状态
const onStatusChange = (status) => {
  loadMyTasks()
}

// 加载统计数据
const loadStats = async () => {
  try {
    const user = userStore.userInfo
    if (!user || !user.gmId) {
      ElMessage.warning('请重新登录')
      return
    }

    const pendingList = await getFeedbackByGmId({
      gmId: user.gmId,
      state: 1
    })
    pendingCount.value = pendingList?.length || 0

    const completedList = await getFeedbackByGmId({
      gmId: user.gmId,
      state: 2
    })
    completedCount.value = completedList?.length || 0

    // 根据 provinceId 和 cityId 查询省市区名称
    if (user.provinceId && user.cityId) {
      try {
        // 获取所有省份
        const provinces = await getAllProvinces()
        const province = provinces?.find(p => p.provinceId === user.provinceId)

        // 获取该省份下的城市
        const cities = await getCitiesByProvinceId({ provinceId: user.provinceId })
        const city = cities?.find(c => c.cityId === user.cityId)

        if (province && city) {
          areaName.value = `${province.provinceName} ${city.cityName}`
        } else if (province) {
          areaName.value = province.provinceName
        } else {
          areaName.value = user.gmName || '未设置'
        }
      } catch (err) {
        console.error('加载区域信息失败', err)
        areaName.value = user.gmName || '未设置'
      }
    } else {
      areaName.value = user.gmName || '未设置'
    }
  } catch (err) {
    ElMessage.error('统计数据加载失败')
    console.error(err)
  }
}

// 加载任务列表
const loadMyTasks = async () => {
  try {
    const gmId = userStore.userInfo?.gmId
    if (!gmId) {
      ElMessage.warning('未获取到网格员信息，请重新登录')
      return
    }

    loading.value = true
    const res = await getFeedbackByGmId({
      gmId: gmId,
      state: currentTaskStatus.value
    })

    list.value = res || []
  } catch (err) {
    ElMessage.error('加载任务失败')
    console.error(err)
  } finally {
    loading.value = false
  }
}

// 处理菜单选择
const handleMenuSelect = (index) => {
  activeMenu.value = index
  if (index === 'myTasks') {
    loadMyTasks()
  } else if (index === 'dashboard') {
    loadStats()
  }
}

// 等级文字
function getLevelText(grade) {
  const map = { 1: '一', 2: '二', 3: '三', 4: '四', 5: '五', 6: '六' }
  return map[grade] || '-'
}

// 等级名称
function getGradeName(grade) {
  const map = {
    1: '一级 优',
    2: '二级 良',
    3: '三级 轻度污染',
    4: '四级 中度污染',
    5: '五级 重度污染',
    6: '六级 严重污染'
  }
  return map[grade] || '-'
}

// 等级颜色
function getLevelColor(grade) {
  const color = ['#02E300', '#FFFF00', '#FF7E00', '#FE0000', '#98004B', '#7E0123']
  return { backgroundColor: color[grade - 1] || '#fff', color: grade <= 2 ? '#333' : '#fff' }
}

// 标签类型
function getTagType(grade) {
  if (!grade) return 'info'
  if (grade <= 2) return 'success'
  if (grade <= 4) return 'warning'
  return 'danger'
}

onMounted(() => {
  loadStats()
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

.stats-row {
  margin-bottom: 24px;
}

.stat-card {
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid rgba(64, 196, 255, 0.2);
}

.stat-card.clickable {
  cursor: pointer;
}

.stat-card.clickable:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 28px rgba(0, 80, 100, 0.15);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 80, 100, 0.12);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #0a2b3e;
}

.header-icon {
  font-size: 20px;
}

.header-icon.pending {
  color: #e6a23c;
}

.header-icon.completed {
  color: #67c23a;
}

.header-icon.area-icon {
  color: #409eff;
}

.stat-value {
  text-align: center;
  font-size: 48px;
  font-weight: bold;
  margin: 16px 0 8px;
}

.stat-value.pending {
  color: #e6a23c;
}

.stat-value.completed {
  color: #67c23a;
}

.stat-value.area-text {
  color: #409eff;
  font-size: 28px;
}

.stat-desc {
  text-align: center;
  color: #8aa3b3;
  font-size: 13px;
}

.tips-card {
  margin-top: 24px;
}

:deep(.el-alert) {
  border-radius: 16px;
  background: linear-gradient(135deg, #eef7fc 0%, #e0f0f8 100%);
  border: 1px solid rgba(64, 196, 255, 0.3);
}

:deep(.el-alert__title) {
  color: #0a4b6e;
  font-weight: 600;
}

:deep(.el-alert ul) {
  margin: 8px 0 0 0;
  padding-left: 20px;
}

:deep(.el-alert li) {
  color: #3a7b9a;
  line-height: 1.8;
  font-size: 13px;
}

/* 任务列表样式 */
.task-card {
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0, 80, 100, 0.08);
  transition: all 0.3s ease;
  border: 1px solid rgba(64, 196, 255, 0.2);
}

.task-card:hover {
  box-shadow: 0 12px 28px rgba(0, 80, 100, 0.12);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
  color: #0a2b3e;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.info-tag {
  background: linear-gradient(135deg, #e8f4f8 0%, #d4edf5 100%);
  border-color: #40c4ff;
  color: #2c8fc9;
}

.loading-container,
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  color: #5a9ab5;
}

.loading-container .el-icon {
  margin-bottom: 16px;
}

.task-list {
  border-radius: 8px;
  overflow: hidden;
}

.task-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  border-bottom: 1px solid #e8f0f5;
  transition: all 0.3s ease;
}

.task-item:last-child {
  border-bottom: none;
}

.task-item:hover {
  background: linear-gradient(135deg, #f0f8fc 0%, #e8f4f8 100%);
}

.task-main {
  display: flex;
  align-items: center;
  flex: 1;
}

.level-badge {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: bold;
  font-size: 18px;
  margin-right: 20px;
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.task-info {
  flex: 1;
}

.location {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
  font-size: 15px;
  color: #0a2b3e;
  font-weight: 600;
}

.location .el-icon {
  color: #40c4ff;
}

.address {
  font-size: 14px;
  color: #3a7b9a;
  margin-bottom: 8px;
}

.meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.date {
  color: #8aa3b3;
  font-size: 13px;
}

.detect-btn {
  flex-shrink: 0;
  margin-left: 20px;
  border-radius: 12px;
  padding: 12px 24px;
  background: linear-gradient(135deg, #40c4ff 0%, #2c8fc9 100%);
  border: none;
  transition: all 0.3s ease;
}

.detect-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 196, 255, 0.4);
}

.detect-btn .el-icon {
  margin-right: 6px;
}

/* 检测弹窗样式 */
.unit {
  margin-left: 8px;
  color: #909399;
  font-size: 13px;
}

:deep(.el-dialog__header) {
  border-bottom: 1px solid #e8f0f5;
  padding-bottom: 16px;
}

:deep(.el-divider__text) {
  color: #2c8fc9;
  font-weight: 600;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .el-main {
    padding: 20px 16px;
  }

  .page-header h2 {
    font-size: 22px;
  }

  .task-item {
    flex-direction: column;
    align-items: flex-start;
  }

  .detect-btn {
    margin-left: 0;
    margin-top: 16px;
    width: 100%;
  }

  .header-actions {
    flex-direction: column;
    gap: 8px;
  }
}
</style>
