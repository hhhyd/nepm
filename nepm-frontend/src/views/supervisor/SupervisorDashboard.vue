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
            <el-icon class="title-icon"><EditPen /></el-icon>
            <h2>提交空气质量反馈</h2>
          </div>
          <p class="subtitle">提供准确的污染情况描述，帮助我们更好地守护蓝天</p>
        </div>
        
        <el-card class="feedback-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>📋 反馈表单</span>
              <el-tag type="info" effect="plain" class="info-tag">请如实填写</el-tag>
            </div>
          </template>
          
          <el-form 
            :model="feedbackForm" 
            label-width="120px" 
            class="feedback-form"
            label-position="top"
          >
            <el-row :gutter="24">
              <el-col :xs="24" :sm="12">
                <el-form-item label="省份" required>
                  <el-select 
                    v-model="feedbackForm.provinceId" 
                    placeholder="请选择省份" 
                    @change="handleProvinceChange" 
                    style="width: 100%"
                    size="large"
                    clearable
                  >
                    <el-option
                      v-for="item in provinces"
                      :key="item.provinceId"
                      :label="item.provinceName"
                      :value="item.provinceId"
                    >
                      <span>{{ item.provinceName }}</span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              
              <el-col :xs="24" :sm="12">
                <el-form-item label="城市" required>
                  <el-select 
                    v-model="feedbackForm.cityId" 
                    placeholder="请选择城市" 
                    style="width: 100%"
                    size="large"
                    clearable
                    :disabled="!feedbackForm.provinceId"
                  >
                    <el-option
                      v-for="item in cities"
                      :key="item.cityId"
                      :label="item.cityName"
                      :value="item.cityId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="24">
                <el-form-item label="详细地址" required>
                  <el-input 
                    v-model="feedbackForm.address" 
                    type="textarea" 
                    :rows="2" 
                    placeholder="请输入详细地址，如：XX街道XX号"
                    maxlength="200"
                    show-word-limit
                  />
                </el-form-item>
              </el-col>
              
              <el-col :span="24">
                <el-form-item label="反馈内容" required>
                  <el-input 
                    v-model="feedbackForm.information" 
                    type="textarea" 
                    :rows="4" 
                    placeholder="请详细描述空气质量问题，例如：有刺鼻气味、能见度低、扬尘严重等"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </el-col>

              <el-col :xs="24" :sm="12">
                <el-form-item label="预估AQI等级" required>
                  <el-select 
                    v-model="feedbackForm.estimatedGrade" 
                    placeholder="请根据感官预估等级" 
                    style="width: 100%"
                    size="large"
                    clearable
                  >
                    <el-option label="一级（优）- 空气清新，无任何不适" :value="1">
                      <span class="grade-item">
                        <el-tag type="success" size="small">一级</el-tag>
                        <span style="margin-left: 8px">优 - 空气清新，无任何不适</span>
                      </span>
                    </el-option>
                    <el-option label="二级（良）- 少数敏感人群稍有不适" :value="2">
                      <span class="grade-item">
                        <el-tag type="primary" size="small">二级</el-tag>
                        <span style="margin-left: 8px">良 - 少数敏感人群稍有不适</span>
                      </span>
                    </el-option>
                    <el-option label="三级（轻度污染）- 部分人群有轻微症状" :value="3">
                      <span class="grade-item">
                        <el-tag type="warning" size="small">三级</el-tag>
                        <span style="margin-left: 8px">轻度污染 - 部分人群有轻微症状</span>
                      </span>
                    </el-option>
                    <el-option label="四级（中度污染）- 多数人群有明显不适" :value="4">
                      <span class="grade-item">
                        <el-tag type="danger" size="small">四级</el-tag>
                        <span style="margin-left: 8px">中度污染 - 多数人群有明显不适</span>
                      </span>
                    </el-option>
                    <el-option label="五级（重度污染）- 健康人群受影响" :value="5">
                      <span class="grade-item">
                        <el-tag type="danger" size="small" effect="dark">五级</el-tag>
                        <span style="margin-left: 8px">重度污染 - 健康人群受影响</span>
                      </span>
                    </el-option>
                    <el-option label="六级（严重污染）- 严重污染，需防护" :value="6">
                      <span class="grade-item">
                        <el-tag type="danger" size="small" effect="dark">六级</el-tag>
                        <span style="margin-left: 8px">严重污染 - 严重污染，需防护</span>
                      </span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item>
              <div class="form-actions">
                <el-button @click="resetForm" size="large">重置</el-button>
                <el-button type="primary" @click="handleSubmit" :loading="submitLoading" size="large">
                  <el-icon><Promotion /></el-icon>
                  提交反馈
                </el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-card>

        <div class="tips-card">
          <el-alert
            title="温馨提示"
            type="info"
            :closable="false"
            show-icon
          >
            <template #default>
              <ul>
                <li>您的反馈将帮助环保部门及时发现问题区域</li>
                <li>请尽量提供准确的位置信息和详细的空气状况描述</li>
                <li>反馈提交后，我们会尽快核查并更新官方AQI数据</li>
              </ul>
            </template>
          </el-alert>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { HomeFilled, ChatDotRound, EditPen, WindPower, Promotion } from '@element-plus/icons-vue'
import Header from '@/components/Header.vue'
import { useUserStore } from '@/stores/user'
import { saveAqiFeedback } from '@/api/aqiFeedback'
import { getAllProvinces } from '@/api/gridProvince'
import { getCitiesByProvinceId } from '@/api/gridCity'

const router = useRouter()
const userStore = useUserStore()
const submitLoading = ref(false)

const feedbackForm = reactive({
  telId: '',
  provinceId: null,
  cityId: null,
  address: '',
  information: '',
  estimatedGrade: null
})

const provinces = ref([])
const cities = ref([])

// 初始化用户信息
const initUserInfo = () => {
  if (userStore.userInfo?.telId) {
    feedbackForm.telId = userStore.userInfo.telId
  } else {
    const storedUser = localStorage.getItem('user-info')
    if (storedUser) {
      try {
        const user = JSON.parse(storedUser)
        feedbackForm.telId = user.telId || ''
      } catch (e) {
        console.error('解析用户信息失败', e)
      }
    }
  }
}

const handleProvinceChange = async (provinceId) => {
  if (!provinceId) {
    cities.value = []
    feedbackForm.cityId = null
    return
  }
  
  try {
    const result = await getCitiesByProvinceId({ provinceId })
    cities.value = result || []
    feedbackForm.cityId = null
  } catch (error) {
    ElMessage.error('加载城市列表失败')
    cities.value = []
  }
}

const resetForm = () => {
  feedbackForm.provinceId = null
  feedbackForm.cityId = null
  feedbackForm.address = ''
  feedbackForm.information = ''
  feedbackForm.estimatedGrade = null
  cities.value = []
  ElMessage.info('表单已重置')
}

const handleSubmit = async () => {
  if (!feedbackForm.provinceId) {
    ElMessage.warning('请选择省份')
    return
  }
  
  if (!feedbackForm.cityId) {
    ElMessage.warning('请选择城市')
    return
  }
  
  if (!feedbackForm.address || !feedbackForm.address.trim()) {
    ElMessage.warning('请填写详细地址')
    return
  }
  
  if (!feedbackForm.information || !feedbackForm.information.trim()) {
    ElMessage.warning('请填写反馈内容')
    return
  }
  
  if (!feedbackForm.estimatedGrade) {
    ElMessage.warning('请预估AQI等级')
    return
  }
  
  try {
    await ElMessageBox.confirm('确认提交该反馈吗？提交后将由管理员审核', '提交确认', {
      confirmButtonText: '确认提交',
      cancelButtonText: '取消',
      type: 'info'
    })
  } catch {
    return
  }
  
  submitLoading.value = true
  
  try {
    const now = new Date()
    const year = now.getFullYear()
    const month = String(now.getMonth() + 1).padStart(2, '0')
    const day = String(now.getDate()).padStart(2, '0')
    const hours = String(now.getHours()).padStart(2, '0')
    const minutes = String(now.getMinutes()).padStart(2, '0')
    const seconds = String(now.getSeconds()).padStart(2, '0')
    
    const data = {
      telId: feedbackForm.telId,
      provinceId: feedbackForm.provinceId,
      cityId: feedbackForm.cityId,
      address: feedbackForm.address.trim(),
      information: feedbackForm.information.trim(),
      estimatedGrade: feedbackForm.estimatedGrade,
      afDate: `${year}-${month}-${day}`,
      afTime: `${hours}:${minutes}:${seconds}`,
      state: 0
    }
    
    const result = await saveAqiFeedback(data)
    
    if (result && result > 0) {
      ElMessage.success('反馈提交成功，感谢您的参与！')
      resetForm()
      setTimeout(() => {
        router.push('/supervisor/myFeedback')
      }, 1500)
    } else {
      ElMessage.error('提交失败，请稍后重试')
    }
  } catch (error) {
    console.error('提交反馈错误:', error)
    ElMessage.error('提交失败，请检查网络连接')
  } finally {
    submitLoading.value = false
  }
}

const loadProvinces = async () => {
  try {
    const result = await getAllProvinces()
    provinces.value = result || []
  } catch (error) {
    ElMessage.error('加载省份列表失败')
    provinces.value = []
  }
}

onMounted(() => {
  initUserInfo()
  loadProvinces()
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

.feedback-card {
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0, 80, 100, 0.08);
  margin-bottom: 24px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  border: 1px solid rgba(64, 196, 255, 0.2);
}

.feedback-card:hover {
  transform: translateY(-2px);
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

.info-tag {
  background: linear-gradient(135deg, #e8f4f8 0%, #d4edf5 100%);
  border-color: #40c4ff;
  color: #2c8fc9;
}

.feedback-form {
  padding: 8px 0;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #1a5a7a;
  padding-bottom: 6px;
}

:deep(.el-textarea__inner),
:deep(.el-input__wrapper) {
  border-radius: 12px;
  transition: all 0.3s ease;
  box-shadow: 0 0 0 1px #cde4ec inset;
  background-color: #fff;
}

:deep(.el-textarea__inner:hover),
:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #40c4ff inset;
}

:deep(.el-textarea__inner:focus),
:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(64, 196, 255, 0.3) inset;
}

.grade-item {
  display: flex;
  align-items: center;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #d4eaf2;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #40c4ff 0%, #2c8fc9 100%);
  border: none;
  border-radius: 12px;
  padding: 12px 32px;
  font-weight: 500;
  transition: all 0.3s ease;
}

:deep(.el-button--primary:hover) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 196, 255, 0.4);
  background: linear-gradient(135deg, #5aceff 0%, #40a0d4 100%);
}

:deep(.el-button--default) {
  border-radius: 12px;
  padding: 12px 28px;
  transition: all 0.3s ease;
  border-color: #b8d8e8;
  color: #1a5a7a;
}

:deep(.el-button--default:hover) {
  transform: translateY(-2px);
  border-color: #40c4ff;
  color: #2c8fc9;
}

.tips-card {
  margin-top: 16px;
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

/* 响应式调整 */
@media (max-width: 768px) {
  .el-main {
    padding: 20px 16px;
  }
  
  .page-header h2 {
    font-size: 22px;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .form-actions .el-button {
    width: 100%;
  }
}
</style>