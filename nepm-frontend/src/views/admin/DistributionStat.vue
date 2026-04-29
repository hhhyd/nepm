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
          <template #header>AQI空气质量指数级别分布统计</template>
          <div ref="chartRef" style="height: 500px; width: 100%"></div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import Header from '@/components/Header.vue'
import { getAqiDistributeTotalStatis } from '@/api/statistics'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const loading = ref(false)
const chartRef = ref(null)
let chartInstance = null

const initChart = async () => {
  loading.value = true
  try {
    const data = await getAqiDistributeTotalStatis()

    console.log('AQI分布统计数据:', data)

    if (!data || data.length === 0) {
      ElMessage.warning('暂无统计数据')
      return
    }

    if (!chartRef.value) {
      console.error('图表容器未找到')
      return
    }

    if (chartInstance) {
      chartInstance.dispose()
    }

    chartInstance = echarts.init(chartRef.value)

    const option = {
      title: { text: 'AQI级别分布统计', left: 'center', top: 10 },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        top: 50
      },
      series: [{
        name: 'AQI级别',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}: {d}%'
        },
        emphasis: {
          label: { show: true, fontSize: 20, fontWeight: 'bold' }
        },
        data: data.map(item => ({
          name: item.aqiExplain || '未知',
          value: item.total || 0
        }))
      }]
    }

    chartInstance.setOption(option)

  } catch (e) {
    console.error('加载图表失败:', e)
    ElMessage.error('加载统计数据失败: ' + e.message)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  setTimeout(() => {
    initChart()
  }, 100)

  window.addEventListener('resize', handleResize)
})

const handleResize = () => {
  if (chartInstance) {
    chartInstance.resize()
  }
}

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})
</script>

<style scoped>
.dashboard { min-height: 100vh; }
.main-container { min-height: calc(100vh - 60px); }
.el-aside { background-color: #545c64; }
.el-main { padding: 20px; }
</style>
