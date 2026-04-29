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
          <template #header>AQI空气质量指数超标趋势统计</template>
          <div ref="chartRef" class="chart-container" v-show="!loading"></div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import Header from '@/components/Header.vue'
import { getAqiTrendTotalStatis } from '@/api/statistics'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const loading = ref(false)
const chartRef = ref(null)
let chartInstance = null

const initChart = async () => {
  loading.value = true

  try {
    const data = await getAqiTrendTotalStatis()
    console.log('AQI趋势统计数据:', data)

    if (!data || !Array.isArray(data) || data.length === 0) {
      ElMessage.warning('暂无统计数据')
      return
    }

    await nextTick()

    setTimeout(() => {
      if (!chartRef.value) return

      if (chartInstance) {
        chartInstance.dispose()
        chartInstance = null
      }

      chartInstance = echarts.init(chartRef.value)

      // 注意：后端返回的字段名是 total 而不是 count
      const option = {
        title: {
          text: '近12个月AQI超标趋势',
          left: 'center',
          top: 10,
          textStyle: { fontSize: 16 }
        },
        tooltip: {
          trigger: 'axis',
          formatter: function(params) {
            let result = params[0].name + '<br/>'
            params.forEach(item => {
              result += item.marker + item.seriesName + ': ' + item.value + '<br/>'
            })
            return result
          }
        },
        legend: {
          data: ['超标数量'],
          top: 40
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: 85,
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: data.map(item => item.month),
          axisLabel: {
            rotate: 30,
            interval: 0
          }
        },
        yAxis: {
          type: 'value',
          name: '数量',
          nameTextStyle: { fontSize: 12 },
          splitLine: {
            lineStyle: { type: 'dashed' }
          }
        },
        series: [{
          name: '超标数量',
          type: 'line',
          smooth: true,
          symbol: 'circle',
          symbolSize: 8,
          lineStyle: { width: 3, color: '#F56C6C' },
          itemStyle: { color: '#F56C6C' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(245, 108, 108, 0.4)' },
              { offset: 1, color: 'rgba(245, 108, 108, 0.05)' }
            ])
          },
          // 修改处：使用 item.total 替代 item.count
          data: data.map(item => Number(item.total) || 0)
        }]
      }

      chartInstance.setOption(option, true)

      setTimeout(() => {
        chartInstance.resize()
        console.log('趋势图渲染完成')
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
