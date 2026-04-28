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
          <el-menu-item index="/admin/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/admin/feedback">
            <el-icon><Document /></el-icon>
            <span>反馈管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/statistics">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据统计</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-main>
        <h2>欢迎使用全国环境监测网格化平台</h2>
        
        <el-row :gutter="20" style="margin-top: 20px">
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>总监测数</template>
              <h3>{{ stats.total }}</h3>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>优良天数</template>
              <h3>{{ stats.good }}</h3>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>省份覆盖率</template>
              <h3>{{ stats.provinceCoverage }}%</h3>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>城市覆盖率</template>
              <h3>{{ stats.cityCoverage }}%</h3>
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
import { getAqiCount, getAqiGoodCount, getProvinceCoverage, getCityCoverage } from '@/api/statistics'
import { ElMessage } from 'element-plus'

const stats = ref({
  total: 0,
  good: 0,
  provinceCoverage: 0,
  cityCoverage: 0
})

const loadStats = async () => {
  try {
    const [total, good, province, city] = await Promise.all([
      getAqiCount(),
      getAqiGoodCount(),
      getProvinceCoverage(),
      getCityCoverage()
    ])
    
    stats.value = {
      total: total || 0,
      good: good || 0,
      provinceCoverage: province || 0,
      cityCoverage: city || 0
    }
  } catch (error) {
    ElMessage.error('加载统计数据失败')
  }
}

onMounted(() => {
  loadStats()
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
