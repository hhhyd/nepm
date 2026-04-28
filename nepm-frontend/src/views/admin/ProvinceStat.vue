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
          <template #header>省分组检查统计</template>

          <div style="margin-bottom: 20px; text-align: center;">
            <el-radio-group v-model="chartType" @change="initChart">
              <el-radio-button label="so2">SO₂超标统计</el-radio-button>
              <el-radio-button label="co">CO超标统计</el-radio-button>
              <el-radio-button label="spm">PM2.5超标统计</el-radio-button>
              <el-radio-button label="aqi">AQI超标统计</el-radio-button>
            </el-radio-group>
          </div>

          <div ref="chartRef" class="chart-container" v-show="!loading"></div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import Header from '@/components/Header.vue'
import { getProvinceItemTotalStatis } from '@/api/statistics'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const loading = ref(false)
const chartRef = ref(null)
let chartInstance = null
const chartType = ref('so2')

const fieldMap = {
  so2: { field: 'so2Total', label: 'SO₂超标次数', color: '#E6A23C' },
  co: { field: 'coTotal', label: 'CO超标次数', color: '#409EFF' },
  spm: { field: 'spmTotal', label: 'PM2.5超标次数', color: '#F56C6C' },
  aqi: { field: 'aqiTotal', label: 'AQI超标次数', color: '#909399' }
}

const initChart = async () => {
  loading.value = true

  try {
    const data = await getProvinceItemTotalStatis()
    console.log('省分组统计数据:', data)

    if (data && data.length > 0) {
      console.log('第一条完整数据:', JSON.stringify(data[0], null, 2))
    }

    if (!data || !Array.isArray(data) || data.length === 0) {
      ElMessage.warning('暂无统计数据')
      return
    }

    await nextTick()

    setTimeout(() => {
      if (!chartRef.value) {
        console.error('图表容器未找到')
        return
      }

      if (chartInstance) {
        chartInstance.dispose()
        chartInstance = null
      }

      chartInstance = echarts.init(chartRef.value)

      const currentField = fieldMap[chartType.value]
      const provinceNames = data.map(i => i.provinceName || '未知')
      const values = data.map(i => {
        const val = i[currentField.field] || i[currentField.field.toLowerCase()] || 0
        return Number(val) || 0
      })

      console.log(`使用字段: ${currentField.field}, 数据:`, values)

      const option = {
        title: {
          text: `各省${currentField.label}统计`,
          left: 'center',
          top: 10,
          textStyle: { fontSize: 16 }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '15%',
          top: 70,
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: provinceNames,
          axisLabel: {
            rotate: 45,
            interval: 0,
            fontSize: 11
          }
        },
        yAxis: {
          type: 'value',
          name: '次数',
          nameTextStyle: { fontSize: 12 }
        },
        series: [{
          name: currentField.label,
          type: 'bar',
          barWidth: '50%',
          data: values,
          itemStyle: {
            color: currentField.color,
            borderRadius: [4, 4, 0, 0]
          },
          label: {
            show: true,
            position: 'top',
            fontSize: 11
          }
        }]
      }

      chartInstance.setOption(option, true)

      setTimeout(() => {
        chartInstance.resize()
      }, 100)

    }, 300)

  } catch (e) {
    console.error('加载图表失败:', e)
    ElMessage.error('加载统计数据失败: ' + (e.message || e))
  } finally {
    setTimeout(() => {
      loading.value = false
    }, 100)
  }
}

onMounted(() => {
  initChart()

  window.addEventListener('resize', () => {
    chartInstance?.resize()
  })
})

onUnmounted(() => {
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
.chart-container {
  width: 100%;
  height: 500px;
}
</style>
