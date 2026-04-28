<template>
  <div class="screen-container">
    <div class="top-header">
      <h1>东软空气质量环保公众监督平台</h1>
      <div class="header-time">{{ currentTime }}</div>
    </div>

    <div class="main-wrap">
      <!-- 左侧 -->
      <div class="left-col">
        <div class="gauge-row">
          <div class="gauge-card">
            <p class="gauge-title">全国省网格覆盖率(%)</p>
            <div ref="gauge1" class="gauge-box"></div>
            <p class="gauge-val">{{ screenData.provinceCoverage }}</p>
          </div>
          <div class="gauge-card">
            <p class="gauge-title">全国大城市网格覆盖率(%)</p>
            <div ref="gauge2" class="gauge-box"></div>
            <p class="gauge-val red">{{ screenData.cityCoverage }}</p>
          </div>
        </div>

        <div class="chart-card">
          <p class="chart-title">空气质量指数等级分布</p>
          <div ref="pieChart" class="chart-box"></div>
        </div>

        <div class="chart-card">
          <p class="chart-title">12个月内空气质量超标趋势</p>
          <div ref="lineChart" class="chart-box"></div>
        </div>
      </div>

      <!-- 中间 -->
      <div class="center-col">
        <div class="map-wrap">
          <div ref="chinaMap" class="map-box"></div>
        </div>

        <div class="real-stat">
          <div class="stat-item">
            <div class="stat-num">{{ screenData.totalCount }}</div>
            <p class="stat-label">空气质量检测总数量</p>
          </div>
          <div class="stat-item">
            <div class="stat-num green">{{ screenData.goodCount }}</div>
            <p class="stat-label">空气质量良好数量</p>
          </div>
          <div class="stat-item">
            <div class="stat-num red">{{ screenData.badCount }}</div>
            <p class="stat-label">空气质量污染数量</p>
          </div>
        </div>
      </div>

      <!-- 右侧 -->
      <div class="right-col">
        <div class="bar-card">
          <p class="bar-title">悬浮颗粒物(PM2.5)浓度超标累计</p>
          <div ref="bar1" class="bar-box"></div>
        </div>
        <div class="bar-card">
          <p class="bar-title">二氧化硫(SO2)浓度超标累计</p>
          <div ref="bar2" class="bar-box"></div>
        </div>
        <div class="bar-card">
          <p class="bar-title">一氧化碳(CO)浓度超标累计</p>
          <div ref="bar3" class="bar-box"></div>
        </div>
      </div>
    </div>

    <!-- 省份详情弹窗 -->
    <div v-if="showProvinceInfo" class="province-info-modal">
      <div class="modal-header">
        <h3>{{ currentProvince }} 空气质量详情</h3>
        <button @click="showProvinceInfo = false" class="close-btn">×</button>
      </div>
      <div class="modal-body">
        <div class="info-row">
          <span>AQI指数：</span>
          <span class="val">{{ provinceData.aqi }}</span>
        </div>
        <div class="info-row">
          <span>空气质量等级：</span>
          <span class="val level">{{ provinceData.level }}</span>
        </div>
        <div class="info-row">
          <span>PM2.5浓度：</span>
          <span class="val">{{ provinceData.pm25 }} μg/m³</span>
        </div>
        <div class="info-row">
          <span>SO₂浓度：</span>
          <span class="val">{{ provinceData.so2 }} μg/m³</span>
        </div>
        <div class="info-row">
          <span>CO浓度：</span>
          <span class="val">{{ provinceData.co }} mg/m³</span>
        </div>
        <div class="info-row">
          <span>超标状态：</span>
          <span class="val" :class="provinceData.isExceed ? 'red' : 'green'">
            {{ provinceData.isExceed ? '超标' : '达标' }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Statistics',
  data() {
    return {
      currentTime: '',
      screenData: {
        provinceCoverage: 47.06,
        cityCoverage: 16.04,
        totalCount: 39,
        goodCount: 2,
        badCount: 37,
        pieData: [
          { name: '优', value: 15 },
          { name: '良', value: 20 },
          { name: '轻度', value: 25 },
          { name: '中度', value: 30 },
          { name: '重度', value: 10 },
          { name: '严重', value: 5 }
        ],
        lineData: [0, 0, 1, 3, 3.5, 2.8, 4, 3.8, 4.2, 4, 5.8, 5.2],
        barData: {
          pm25: [1, 3, 4, 1, 1, 4, 1, 3, 3, 2, 2, 1, 2, 3, 2, 3],
          so2: [1, 1, 2, 4, 1, 2, 3, 3, 2, 2, 2, 1, 2, 2, 1, 1],
          co: [2, 1, 4, 1, 3, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 3]
        }
      },
      chartList: [],
      mapChart: null,
      timer: null,
      refreshTimer: null,
      showProvinceInfo: false,
      currentProvince: '',
      provinceData: {}
    }
  },
  mounted() {
    this.initTime()
    this.$nextTick(() => {
      this.refreshAllRandomData()
      this.initAllChart()
      this.initMap()
    })
    this.startAutoRefresh()
    window.addEventListener('resize', this.resizeAll)
  },
  beforeDestroy() {
    clearInterval(this.timer)
    clearInterval(this.refreshTimer)
    window.removeEventListener('resize', this.resizeAll)
    this.chartList.forEach(c => c.dispose())
    if (this.mapChart) this.mapChart.dispose()
  },
  methods: {
    // 实时时间
    initTime() {
      this.currentTime = new Date().toLocaleString('zh-CN')
      this.timer = setInterval(() => {
        this.currentTime = new Date().toLocaleString('zh-CN')
      }, 1000)
    },

    // ====================== 核心：全局自动刷新所有随机数据 ======================
    refreshAllRandomData() {
      // 1. 刷新统计总数（每次不一样）
      const total = Math.floor(Math.random() * 41) + 20
      const good = Math.floor(Math.random() * (total * 0.3))
      const bad = total - good
      this.screenData.totalCount = total
      this.screenData.goodCount = good
      this.screenData.badCount = bad

      // 2. 刷新仪表盘覆盖率
      this.screenData.provinceCoverage = (40 + Math.random() * 20).toFixed(2)
      this.screenData.cityCoverage = (10 + Math.random() * 15).toFixed(2)

      // 3. 刷新饼图数据
      this.screenData.pieData = this.screenData.pieData.map(item => ({
        ...item,
        value: Math.floor(Math.random() * 30 + 5)
      }))

      // 4. 刷新折线图数据
      this.screenData.lineData = this.screenData.lineData.map(() =>
          Number((Math.random() * 5).toFixed(1))
      )

      // 5. 刷新三个柱状图数据
      this.screenData.barData.pm25 = this.screenData.barData.pm25.map(() =>
          Math.floor(Math.random() * 4 + 1)
      )
      this.screenData.barData.so2 = this.screenData.barData.so2.map(() =>
          Math.floor(Math.random() * 4 + 1)
      )
      this.screenData.barData.co = this.screenData.barData.co.map(() =>
          Math.floor(Math.random() * 4 + 1)
      )
    },

    // 自动刷新定时器
    startAutoRefresh() {
      this.refreshTimer = setInterval(() => {
        this.refreshAllRandomData()
        this.initAllChart()
      }, 5000)
    },

    // 地图悬浮空气质量数据
    getAirData() {
      const aqi = Math.floor(Math.random() * 200) + 30
      let level, isExceed
      if (aqi <= 50) { level = '优'; isExceed = false }
      else if (aqi <= 100) { level = '良'; isExceed = false }
      else if (aqi <= 150) { level = '轻度污染'; isExceed = true }
      else if (aqi <= 200) { level = '中度污染'; isExceed = true }
      else { level = '重度污染'; isExceed = true }
      return {
        aqi, level, isExceed,
        pm25: (Math.random() * 70 + 10).toFixed(1),
        so2: (Math.random() * 100 + 5).toFixed(1),
        co: (Math.random() * 4 + 0.5).toFixed(1)
      }
    },

    // 标准中国地图 + 鼠标悬浮提示 + 清新配色
    initMap() {
      this.mapChart = echarts.init(this.$refs.chinaMap)
      fetch('https://geo.datav.aliyun.com/areas_v3/bound/100000_full.json')
          .then(res => res.json())
          .then(chinaJson => {
            echarts.registerMap('china', chinaJson)
            const option = {
              tooltip: {
                trigger: 'item',
                formatter: params => {
                  const d = this.getAirData()
                  return `
                  <div style="padding:6px 10px;">
                    <strong>${params.name}</strong><br/>
                    AQI指数：${d.aqi}<br/>
                    空气质量：${d.level}<br/>
                    PM2.5：${d.pm25} μg/m³<br/>
                    SO₂：${d.so2} μg/m³<br/>
                    CO：${d.co} mg/m³
                  </div>
                `
                }
              },
              geo: {
                map: 'china',
                roam: false,
                zoom: 1.35, // 地图已放大
                center: [104.0, 36.0],
                label: { show: true, fontSize: 10 },
                // 清新绿色系地图
                itemStyle: {
                  areaColor: '#e6f7f4',
                  borderColor: '#91d4c4'
                },
                emphasis: {
                  itemStyle: {
                    areaColor: '#b1ebe0'
                  }
                }
              }
            }
            this.mapChart.setOption(option)
            this.mapChart.on('click', params => {
              if (params.componentType === 'geo') {
                this.currentProvince = params.name
                this.provinceData = this.getAirData()
                this.showProvinceInfo = true
              }
            })
          })
    },

    // 所有图表初始化（美化版 + 完整悬浮提示）
    initAllChart() {
      this.chartList.forEach(c => c.dispose())
      this.chartList = []

      // 仪表盘 1
      const g1 = echarts.init(this.$refs.gauge1)
      g1.setOption({
        tooltip: { formatter: `覆盖率：{c} %` },
        series: [{
          type: 'gauge', startAngle: 180, endAngle: 0, min: 0, max: 100, radius: '90%',
          axisLine: { lineStyle: { width: 20, color: [[this.screenData.provinceCoverage / 100, '#5470c6'], [1, '#e2e8f0']] }},
          pointer: { width: 6, length: '55%', itemStyle: { color: '#366ed8' }},
          anchor: { show: true, size: 12, itemStyle: { color: '#366ed8', borderWidth: 2, borderColor: '#fff' }},
          detail: { show: false }, axisTick: { show: false }, splitLine: { show: false }, axisLabel: { show: false },
          data: [{ value: this.screenData.provinceCoverage }]
        }]
      })
      this.chartList.push(g1)

      // 仪表盘 2
      const g2 = echarts.init(this.$refs.gauge2)
      g2.setOption({
        tooltip: { formatter: `覆盖率：{c} %` },
        series: [{
          type: 'gauge', startAngle: 180, endAngle: 0, min: 0, max: 100, radius: '90%',
          axisLine: { lineStyle: { width: 20, color: [[this.screenData.cityCoverage / 100, '#ee6666'], [1, '#e2e8f0']] }},
          pointer: { width: 6, length: '55%', itemStyle: { color: '#366ed8' }},
          anchor: { show: true, size: 12, itemStyle: { color: '#366ed8', borderWidth: 2, borderColor: '#fff' }},
          detail: { show: false }, axisTick: { show: false }, splitLine: { show: false }, axisLabel: { show: false },
          data: [{ value: this.screenData.cityCoverage }]
        }]
      })
      this.chartList.push(g2)

      // 美化饼图 + 悬浮提示
      const pie = echarts.init(this.$refs.pieChart)
      pie.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b}<br/>数值：{c}<br/>占比：{d}%'
        },
        color: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#72c3dd', '#c785e6'],
        legend: {
          orient: 'vertical', left: '5%', top: 'center', itemGap: 16,
          textStyle: { fontSize: 14, color: '#333' }
        },
        series: [{
          type: 'pie', radius: ['45%', '75%'], center: ['68%', '50%'],
          itemStyle: {
            borderRadius: 6, borderColor: '#fff', borderWidth: 3,
            shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.1)'
          },
          emphasis: { scale: true },
          label: { show: false }, labelLine: { show: false },
          data: this.screenData.pieData
        }]
      })
      this.chartList.push(pie)

      // 美化折线图 + 悬浮提示
      const line = echarts.init(this.$refs.lineChart)
      line.setOption({
        tooltip: {
          trigger: 'axis',
          formatter: '月份：{b}<br/>超标指数：{c}'
        },
        grid: { left: 25, right: 25, top: 20, bottom: 20 },
        xAxis: {
          type: 'category', data: ['11', '12', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10'],
          axisLabel: { color: '#666' }
        },
        yAxis: { type: 'value', max: 6, axisLabel: { color: '#666' }, splitLine: { lineStyle: { color: '#eee' } } },
        series: [{
          type: 'line', smooth: true, data: this.screenData.lineData, symbolSize: 8,
          lineStyle: { color: '#5470c6', width: 3 },
          itemStyle: { color: '#5470c6' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(84,112,198,0.3)' },
              { offset: 1, color: 'rgba(84,112,198,0.05)' }
            ])
          }
        }]
      })
      this.chartList.push(line)

      const cityNames = ['北京', '天津', '河北', '山西', '内蒙古', '辽宁', '吉林', '黑龙江', '上海', '江苏', '浙江', '安徽', '福建', '江西', '山东', '河南']

      // 美化 PM2.5 柱状图 + 悬浮提示
      const bar1 = echarts.init(this.$refs.bar1)
      bar1.setOption({
        tooltip: {
          trigger: 'axis',
          formatter: '地区：{b}<br/>PM2.5超标次数：{c} 次'
        },
        grid: { left: 15, right: 15, top: 15, bottom: 40 },
        xAxis: {
          type: 'category', data: cityNames,
          axisLabel: { color: '#666', rotate: 45 }
        },
        yAxis: { type: 'value', max: 4, splitLine: { lineStyle: { color: '#eee' } } },
        series: [{
          type: 'bar', barWidth: 14, data: this.screenData.barData.pm25,
          itemStyle: {
            borderRadius: [6, 6, 0, 0],
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#72c3dd' }, { offset: 1, color: '#bce7f5' }
            ])
          }
        }]
      })
      this.chartList.push(bar1)

      // 美化 SO2 柱状图 + 悬浮提示
      const bar2 = echarts.init(this.$refs.bar2)
      bar2.setOption({
        tooltip: {
          trigger: 'axis',
          formatter: '地区：{b}<br/>SO₂超标次数：{c} 次'
        },
        grid: { left: 15, right: 15, top: 15, bottom: 40 },
        xAxis: {
          type: 'category', data: cityNames,
          axisLabel: { color: '#666', rotate: 45 }
        },
        yAxis: { type: 'value', max: 4, splitLine: { lineStyle: { color: '#eee' } } },
        series: [{
          type: 'bar', barWidth: 14, data: this.screenData.barData.so2,
          itemStyle: {
            borderRadius: [6, 6, 0, 0],
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#91cc75' }, { offset: 1, color: '#d5f5b5' }
            ])
          }
        }]
      })
      this.chartList.push(bar2)

      // 美化 CO 柱状图 + 悬浮提示
      const bar3 = echarts.init(this.$refs.bar3)
      bar3.setOption({
        tooltip: {
          trigger: 'axis',
          formatter: '地区：{b}<br/>CO超标次数：{c} 次'
        },
        grid: { left: 15, right: 15, top: 15, bottom: 40 },
        xAxis: {
          type: 'category', data: cityNames,
          axisLabel: { color: '#666', rotate: 45 }
        },
        yAxis: { type: 'value', max: 4, splitLine: { lineStyle: { color: '#eee' } } },
        series: [{
          type: 'bar', barWidth: 14, data: this.screenData.barData.co,
          itemStyle: {
            borderRadius: [6, 6, 0, 0],
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#ee6666' }, { offset: 1, color: '#f9b7b7' }
            ])
          }
        }]
      })
      this.chartList.push(bar3)
    },

    resizeAll() {
      this.chartList.forEach(c => c.resize())
      if (this.mapChart) this.mapChart.resize()
    }
  }
}
</script>

<style scoped>
.screen-container {
  width: 100%;
  height: calc(100vh - 60px);
  background: #f7f8fa;
  overflow: hidden;
  box-sizing: border-box;
}
.top-header {
  height: 70px;
  background: #ffffff;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}
.top-header h1 {
  color: #2c3e50;
  font-size: 24px;
  margin: 0;
  font-weight: 600;
}
.header-time {
  position: absolute;
  right: 30px;
  color: #666;
  font-size: 14px;
}
.main-wrap {
  display: grid;
  grid-template-columns: 26% 48% 26%;
  gap: 16px;
  padding: 16px;
  height: calc(100% - 70px);
  box-sizing: border-box;
}
.left-col,.center-col,.right-col {
  display: flex;
  flex-direction: column;
  gap: 16px;
  height: 100%;
}
.gauge-row { display: flex; gap: 12px; }
.gauge-card {
  flex: 1; background: #fff; border-radius: 12px; padding: 16px;
  text-align: center; box-shadow: 0 3px 12px rgba(0,0,0,0.06);
}
.gauge-box { width: 100%; height: 150px; }
.gauge-title{ font-size: 15px; color:#333; margin-bottom:8px; }
.gauge-val { font-size: 30px; font-weight: 700; color: #5470c6; margin:8px 0 0; }
.gauge-val.red { color: #ee6666; }

.chart-card {
  flex: 1; background: #fff; border-radius: 12px; padding: 16px;
  box-shadow: 0 3px 12px rgba(0,0,0,0.06);
}
.chart-title{ font-size:15px; color:#333; margin-bottom:10px; }
.chart-box { width: 100%; height: calc(100% - 30px); }

.map-wrap {
  flex: 1; background: #fff; border-radius: 12px;
  position: relative; box-shadow: 0 3px 12px rgba(0,0,0,0.06);
}
.map-box { width: 100%; height: 100%; }

.real-stat {
  background: #fff; border-radius: 12px; padding: 20px;
  display: flex; justify-content: space-around; box-shadow: 0 3px 12px rgba(0,0,0,0.06);
}
.stat-num { font-size: 30px; font-weight: 700; color: #333; }
.stat-num.green { color: #91cc75; }
.stat-num.red { color: #ee6666; }
.stat-label{ margin-top:6px; color:#666; font-size:14px; }

.bar-card {
  flex: 1; background: #fff; border-radius: 12px; padding: 16px;
  box-shadow: 0 3px 12px rgba(0,0,0,0.06);
}
.bar-title{ font-size:14px; color:#333; margin-bottom:8px; }
.bar-box { width: 100%; height: calc(100% - 30px); }

/* 弹窗 */
.province-info-modal {
  position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%);
  background: #fff; border-radius: 12px; box-shadow: 0 8px 24px rgba(0,0,0,0.18);
  width: 400px; z-index: 9999;
}
.modal-header {
  padding: 16px; background: #5470c6; color: #fff;
  border-radius: 12px 12px 0 0; display: flex; justify-content: space-between; align-items: center;
}
.close-btn { background: none; border: none; color: #fff; font-size: 24px; cursor: pointer; }
.modal-body { padding: 20px; }
.info-row {
  display: flex; justify-content: space-between; padding: 12px 0;
  border-bottom: 1px solid #eee; font-size: 15px;
}
.val { font-weight: 600; }
.red { color: #ee6666; }
.green { color: #91cc75; }
</style>