<template>
  <div class="app-container">
    <el-card shadow="hover">
      <div slot="header"><span>AQI检测上报</span></div>
      <div class="feedback-card">
        <div class="feedback-row">
          <span class="label">反馈用户</span>
          <span class="value">{{ feedbackUser }}</span>
        </div>
        <div class="feedback-row">
          <span class="label">网格</span>
          <span class="value">{{ queryData.province }} {{ queryData.city }}</span>
        </div>
        <div class="feedback-row">
          <span class="label">详细地址</span>
          <span class="value">{{ queryData.address }}</span>
        </div>
        <div class="feedback-row">
          <span class="label">预估等级</span>
          <span class="value">{{ getEstimateLevel(levelMark) }}</span>
        </div>
        <div class="feedback-row">
          <span class="label">信息描述</span>
          <span class="value">{{ feedbackDesc }}</span>
        </div>
      </div>
      <div class="input-section">
        <div class="input-row">
          <span class="input-label">SO2浓度</span>
          <el-input v-model.number="form.so2" placeholder="请输入浓度值" />
          <span class="unit">ug/m3</span>
          <div class="level-box" :style="so2Color">{{ so2Level }}</div>
        </div>
        <div class="input-row">
          <span class="input-label">CO浓度</span>
          <el-input v-model.number="form.co" placeholder="请输入浓度值" />
          <span class="unit">ug/m3</span>
          <div class="level-box" :style="coColor">{{ coLevel }}</div>
        </div>
        <div class="input-row">
          <span class="input-label">PM2.5浓度</span>
          <el-input v-model.number="form.pm25" placeholder="请输入浓度值" />
          <span class="unit">ug/m3</span>
          <div class="level-box" :style="pm25Color">{{ pm25Level }}</div>
        </div>
      </div>
      <div class="result-row">
        <div class="result-box" :style="aqiColor">
          实测AQI等级：{{ aqiLevel }}
        </div>
        <el-button type="primary" class="submit-btn" @click="submit" :loading="submitting">
          提交实测数据
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { saveStatistics } from '@/api/statistics'
import { confirmFeedback } from '@/api/aqiFeedback'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const submitting = ref(false)

// 路由参数
const queryData = reactive({
  province: '',
  city: '',
  address: '',
  afId: ''
})
const feedbackUser = ref('')
const feedbackDesc = ref('')
const levelMark = ref('')

// 检测表单
const form = reactive({
  so2: null,
  co: null,
  pm25: null
})

// 等级与颜色
const so2Level = ref('-')
const coLevel = ref('-')
const pm25Level = ref('-')
const so2Color = ref('')
const coColor = ref('')
const pm25Color = ref('')
const aqiLevel = ref('暂未检测')
const aqiColor = ref('')

// 监听路由
watch(() => route.query, (newVal) => {
  queryData.province = newVal.province || ''
  queryData.city = newVal.city || ''
  queryData.address = newVal.address || ''
  queryData.afId = newVal.afId || ''
  levelMark.value = newVal.levelMark || ''
  feedbackUser.value = newVal.userName || ''
  feedbackDesc.value = newVal.remark || ''
}, { immediate: true })

// 监听输入计算等级
watch([() => form.so2, () => form.co, () => form.pm25], () => {
  calcAqi()
}, { deep: true })

// 预估等级文本
function getEstimateLevel(mark) {
  const map = {
    1: '一级 优', 2: '二级 良', 3: '三级 轻度污染',
    4: '四级 中度污染',5: '五级 重度污染',6: '六级 严重污染'
  }
  return map[mark] || '暂无预估等级'
}

function getLevelText(level) {
  const map = {1: '一',2: '二',3: '三',4: '四',5: '五',6: '六'}
  return map[level] || '-'
}

function getAqiName(level) {
  const map = {
    1: '一级 优',2: '二级 良',3: '三级 轻度污染',
    4: '四级 中度污染',5: '五级 重度污染',6: '六级 严重污染'
  }
  return map[level] || '暂未检测'
}

function getColor(level) {
  const map = {
    1: '#02E300',2: '#FFFF00',3: '#FF7E00',
    4: '#FE0000',5: '#98004B',6: '#7E0123'
  }
  return map[level] || '#fff'
}

// 计算等级
function calcSo2(val) {
  if (val <= 50) return 1
  if (val <= 150) return 2
  if (val <= 475) return 3
  if (val <= 800) return 4
  if (val <= 1600) return 5
  return 6
}
function calcCo(val) {
  if (val <= 5) return 1
  if (val <= 10) return 2
  if (val <= 35) return 3
  if (val <= 60) return 4
  if (val <= 90) return 5
  return 6
}
function calcPm25(val) {
  if (val <= 35) return 1
  if (val <= 75) return 2
  if (val <= 115) return 3
  if (val <= 150) return 4
  if (val <= 250) return 5
  return 6
}

// 计算AQI
function calcAqi() {
  const so2 = form.so2
  const co = form.co
  const pm25 = form.pm25
  const levels = []

  if (so2 != null) {
    const l = calcSo2(so2)
    so2Level.value = getLevelText(l)
    so2Color.value = { backgroundColor: getColor(l), color: '#fff' }
    levels.push(l)
  } else { so2Level.value = '-'; so2Color.value = '' }

  if (co != null) {
    const l = calcCo(co)
    coLevel.value = getLevelText(l)
    coColor.value = { backgroundColor: getColor(l), color: '#fff' }
    levels.push(l)
  } else { coLevel.value = '-'; coColor.value = '' }

  if (pm25 != null) {
    const l = calcPm25(pm25)
    pm25Level.value = getLevelText(l)
    pm25Color.value = { backgroundColor: getColor(l), color: '#fff' }
    levels.push(l)
  } else { pm25Level.value = '-'; pm25Color.value = '' }

  if (levels.length === 0) {
    aqiLevel.value = '暂未检测'
    aqiColor.value = ''
  } else {
    const max = Math.max(...levels)
    aqiLevel.value = getAqiName(max)
    aqiColor.value = { backgroundColor: getColor(max), color: '#fff' }
  }
}

// 🔥 真实提交：保存统计数据 + 完成反馈
const submit = async () => {
  if (!form.so2 || !form.co || !form.pm25) {
    ElMessage.warning('请填写完整检测数据')
    return
  }
  if (!queryData.afId) {
    ElMessage.warning('未获取到反馈ID')
    return
  }

  submitting.value = true

  try {
    const gmId = userStore.userInfo?.gmId
    const now = new Date()
    const confirmDate = now.toISOString().split('T')[0]
    const confirmTime = now.toTimeString().slice(0, 8)

    const so2Level = calcSo2(form.so2)
    const coLevel = calcCo(form.co)
    const spmLevel = calcPm25(form.pm25)
    const aqiId = Math.max(so2Level, coLevel, spmLevel)

    // 1. 保存检测数据到 statistics 表
    await saveStatistics({
      provinceId: userStore.userInfo?.provinceId || 1,
      cityId: userStore.userInfo?.cityId || 1,
      address: queryData.address,
      so2Value: form.so2,
      so2Level: so2Level,
      coValue: form.co,
      coLevel: coLevel,
      spmValue: form.pm25,
      spmLevel: spmLevel,
      aqiId: aqiId,
      confirmDate: confirmDate,
      confirmTime: confirmTime,
      gmId: gmId,
      fdId: feedbackUser.value,
      information: feedbackDesc.value,
      remarks: '网格员现场检测'
    })

    // 2. 把反馈状态改为 2（已确认完成）
    await confirmFeedback({
      afId: queryData.afId,
      state: 2
    })

    ElMessage.success('检测上报成功！')
    router.push('/gridMember/myTasks')
  } catch (err) {
    ElMessage.error('提交失败')
    console.error(err)
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.feedback-card {
  border: 1px solid #e6e6e6;
  border-radius: 8px;
  margin-bottom: 24px;
  background: #fafbfc;
}
.feedback-row {
  display: flex;
  padding: 14px 20px;
  border-bottom: 1px solid #f0f0f0;
  align-items: center;
}
.feedback-row:last-child { border-bottom: none; }
.label { width: 110px; font-weight: 600; color: #333; }
.value { flex: 1; color: #666; }
.input-section { margin-bottom: 24px; }
.input-row { display: flex; align-items: center; margin-bottom: 18px; }
.input-label { width: 110px; font-weight: 600; color: #333; }
.unit { margin: 0 12px; color: #999; }
.level-box { width: 40px; height: 40px; border: 1px solid #e6e6e6; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-weight: bold; }
.result-row { display: flex; align-items: center; gap: 16px; margin-top: 10px; }
.result-box { flex: 1; border: 1px solid #e6e6e6; border-radius: 8px; padding: 12px 20px; text-align: center; }
.submit-btn { height: 42px; padding: 0 24px; }
</style>