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
        <h2>数据统计分析</h2>

        <el-row :gutter="20" style="margin-top: 20px">
          <el-col :span="12">
            <el-card>
              <template #header>AQI 分布统计</template>
              <div id="aqiDistributeChart" style="height: 400px"></div>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card>
              <template #header>近12个月AQI趋势</template>
              <div id="aqiTrendChart" style="height: 400px"></div>
            </el-card>
          </el-col>
        </el-row>

        <el-card style="margin-top: 20px">
          <template #header>各省数据统计</template>
          <el-table :data="provinceStats" style="width: 100%">
            <el-table-column prop="provinceName" label="省份" />
            <el-table-column prop="totalCount" label="监测总数" />
            <el-table-column prop="goodCount" label="优良天数" />
            <el-table-column label="优良率">
              <template #default="{ row }">
                {{ ((row.goodCount / row.totalCount) * 100).toFixed(2) }}%
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import {
  getAqiDistributeTotalStatis,
  getAqiTrendTotalStatis,
  getProvinceItemTotalStatis
} from '@/api/statistics'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const router = useRouter()

onMounted(() => {
  router.replace('/admin/provincestat')
})
const provinceStats = ref([])
let aqiDistributeChart = null
let aqiTrendChart = null

const initCharts = async () => {
  try {
    // AQI 分布统计
    const distributeData = await getAqiDistributeTotalStatis()
    if (distributeData && distributeData.length > 0) {
      const chartDom = document.getElementById('aqiDistributeChart')
      if (chartDom) {
        aqiDistributeChart = echarts.init(chartDom)
        aqiDistributeChart.setOption({
          tooltip: { trigger: 'item' },
          series: [{
            type: 'pie',
            data: distributeData.map(item => ({
              name: item.aqiExplain,
              value: item.count
            }))
          }]
        })
      }
    }

    // AQI 趋势统计
    const trendData = await getAqiTrendTotalStatis()
    if (trendData && trendData.length > 0) {
      const chartDom = document.getElementById('aqiTrendChart')
      if (chartDom) {
        aqiTrendChart = echarts.init(chartDom)
        aqiTrendChart.setOption({
          tooltip: { trigger: 'axis' },
          xAxis: {
            type: 'category',
            data: trendData.map(item => item.month)
          },
          yAxis: { type: 'value' },
          series: [{
            type: 'line',
            data: trendData.map(item => item.count)
          }]
        })
      }
    }

    // 省份统计
    const provinceData = await getProvinceItemTotalStatis()
    provinceStats.value = provinceData || []

  } catch (error) {
    ElMessage.error('加载统计数据失败')
  }
}

onMounted(async () => {
  await nextTick()
  await initCharts()
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
