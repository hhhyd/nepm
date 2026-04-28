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
        <h2>提交空气质量反馈</h2>
        
        <el-card style="margin-top: 20px">
          <el-form :model="feedbackForm" label-width="100px">
            <el-form-item label="省份">
              <el-select v-model="feedbackForm.provinceId" placeholder="请选择省份" @change="handleProvinceChange" style="width: 100%">
                <el-option
                  v-for="item in provinces"
                  :key="item.provinceId"
                  :label="item.provinceName"
                  :value="item.provinceId"
                />
              </el-select>
            </el-form-item>
            
            <el-form-item label="城市">
              <el-select v-model="feedbackForm.cityId" placeholder="请选择城市" style="width: 100%">
                <el-option
                  v-for="item in cities"
                  :key="item.cityId"
                  :label="item.cityName"
                  :value="item.cityId"
                />
              </el-select>
            </el-form-item>
            
            <el-form-item label="详细地址">
              <el-input v-model="feedbackForm.address" type="textarea" :rows="3" placeholder="请输入详细地址" />
            </el-form-item>
            
            <el-form-item label="反馈内容">
              <el-input v-model="feedbackForm.information" type="textarea" :rows="4" placeholder="请描述空气质量问题" />
            </el-form-item>
            
            <el-form-item label="预估等级">
              <el-select v-model="feedbackForm.estimatedGrade" placeholder="请预估AQI等级" style="width: 100%">
                <el-option label="一级（优）" :value="1" />
                <el-option label="二级（良）" :value="2" />
                <el-option label="三级（轻度污染）" :value="3" />
                <el-option label="四级（中度污染）" :value="4" />
                <el-option label="五级（重度污染）" :value="5" />
                <el-option label="六级（严重污染）" :value="6" />
              </el-select>
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
                提交反馈
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import { useUserStore } from '@/stores/user'
import { saveAqiFeedback } from '@/api/aqiFeedback'
import { getAllProvinces } from '@/api/gridProvince'
import { getCitiesByProvinceId } from '@/api/gridCity'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const submitLoading = ref(false)

const feedbackForm = ref({
  telId: userStore.userInfo?.telId || '',
  provinceId: null,
  cityId: null,
  address: '',
  information: '',
  estimatedGrade: null
})

const provinces = ref([])
const cities = ref([])

const handleProvinceChange = async (provinceId) => {
  feedbackForm.value.cityId = null
  try {
    const result = await getCitiesByProvinceId({ provinceId })
    cities.value = result || []
  } catch (error) {
    ElMessage.error('加载城市列表失败')
  }
}

const handleSubmit = async () => {
  if (!feedbackForm.value.provinceId || !feedbackForm.value.cityId) {
    ElMessage.warning('请选择省份和城市')
    return
  }
  
  if (!feedbackForm.value.address || !feedbackForm.value.information) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  submitLoading.value = true
  
  try {
    const now = new Date()
    const data = {
      ...feedbackForm.value,
      afDate: now.toISOString().split('T')[0],
      afTime: now.toTimeString().split(' ')[0],
      state: 0
    }
    
    const result = await saveAqiFeedback(data)
    
    if (result > 0) {
      ElMessage.success('反馈提交成功')
      router.push('/supervisor/myFeedback')
    } else {
      ElMessage.error('提交失败')
    }
  } catch (error) {
    ElMessage.error('提交失败，请检查网络')
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
  }
}

onMounted(() => {
  loadProvinces()
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
