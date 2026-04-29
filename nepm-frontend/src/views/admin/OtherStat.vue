<template>
  <div class="dashboard">
    <Header />
    <el-container class="main-container">
      <el-aside width="220px">
        <el-menu :default-active="$route.path" router background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
          <el-menu-item index="/admin/dashboard"><el-icon><HomeFilled /></el-icon><span>首页</span></el-menu-item>

          <el-sub-menu index="supervision">
            <template #title><el-icon><Document /></el-icon><span>公众监督数据管理</span></template>
            <el-menu-item index="/admin/feedback">公众监督数据列表</el-menu-item>
            <el-menu-item index="/admin/aqilist">确认AQI数据列表</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="statistics">
            <template #title><el-icon><DataAnalysis /></el-icon><span>统计数据管理</span></template>
            <el-menu-item index="/admin/provincestat">省分组检查统计</el-menu-item>
            <el-menu-item index="/admin/distributionstat">AQI指数分布统计</el-menu-item>
            <el-menu-item index="/admin/trendstat">AQI指数趋势统计</el-menu-item>
            <el-menu-item index="/admin/otherstat">其它数据统计</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <el-main>
        <el-card v-loading="loading">
          <template #header>其它数据统计</template>

          <el-row :gutter="20" style="margin-top: 20px">
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>总监测数</template>
                <h3 style="text-align: center; color: #409EFF; font-size: 32px; margin: 10px 0;">{{ stats.total }}</h3>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>优良天数</template>
                <h3 style="text-align: center; color: #67C23A; font-size: 32px; margin: 10px 0;">{{ stats.good }}</h3>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>省份覆盖率</template>
                <h3 style="text-align: center; color: #E6A23C; font-size: 32px; margin: 10px 0;">{{ stats.provinceCoverage }}%</h3>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>城市覆盖率</template>
                <h3 style="text-align: center; color: #F56C6C; font-size: 32px; margin: 10px 0;">{{ stats.cityCoverage }}%</h3>
              </el-card>
            </el-col>
          </el-row>

          <el-table :data="provinceStats" style="width: 100%; margin-top: 30px" stripe>
            <el-table-column prop="provinceName" label="省份" />
            <el-table-column prop="totalCount" label="监测总数" />
            <el-table-column prop="goodCount" label="优良天数" />
            <el-table-column label="优良率">
              <template #default="{ row }">
                {{ row.totalCount ? ((row.goodCount / row.totalCount) * 100).toFixed(2) : '0.00' }}%
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import {
  getAqiCount,
  getAqiGoodCount,
  getProvinceCoverage,
  getCityCoverage,
  getProvinceItemTotalStatis
} from '@/api/statistics'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const stats = ref({
  total: 0,
  good: 0,
  provinceCoverage: 0,
  cityCoverage: 0
})
const provinceStats = ref([])

const loadData = async () => {
  loading.value = true
  try {
    const [total, good, province, city, provinceData] = await Promise.all([
      getAqiCount(),
      getAqiGoodCount(),
      getProvinceCoverage(),
      getCityCoverage(),
      getProvinceItemTotalStatis()
    ])

    stats.value = {
      total: total || 0,
      good: good || 0,
      provinceCoverage: province || 0,
      cityCoverage: city || 0
    }
    provinceStats.value = provinceData || []
  } catch (e) {
    ElMessage.error('加载统计数据失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dashboard { min-height: 100vh; }
.main-container { min-height: calc(100vh - 60px); }
.el-aside { background-color: #545c64; }
.el-main { padding: 20px; }
</style>
