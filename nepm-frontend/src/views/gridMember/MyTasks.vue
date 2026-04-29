<template>
  <div class="worker-task" style="padding: 20px;">
    <h2>我的任务</h2>
    <div v-if="loading">加载中...</div>
    <div v-else-if="list.length === 0">暂无检测任务</div>
    <div
        v-else
        v-for="item in list"
        :key="item.afId"
        class="task-item"
    >
      <div class="level-tag" :style="getLevelColor(item.estimatedGrade)">
        {{ getLevelText(item.estimatedGrade) }}
      </div>
      <div class="task-info">
        <h3 style="margin:0">{{ item.address }}</h3>
        <p class="detail">{{ item.information }}</p>
      </div>
      <el-button type="primary" @click="goDetect(item)">
        去检测
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getFeedbackByGmId } from '@/api/aqiFeedback'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const list = ref([])
const loading = ref(true)

// 加载网格员自己的任务（真实接口）
const loadMyTasks = async () => {
  try {
    const gmId = userStore.userInfo?.gmId
    if (!gmId) {
      ElMessage.warning('未获取到网格员信息，请重新登录')
      loading.value = false
      return
    }

    const res = await getFeedbackByGmId({
      gmId: gmId,
      state: 1 // 只加载已指派未完成的
    })

    list.value = res || []
  } catch (err) {
    ElMessage.error('加载任务失败')
    console.error(err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadMyTasks()
})

// 跳检测页，传真实 afId
const goDetect = (item) => {
  router.push({
    path: '/gridMember/detect',
    query: {
      afId: item.afId,
      province: item.gridProvince?.provinceName || '',
      city: item.gridCity?.cityName || '',
      address: item.address,
      levelMark: item.estimatedGrade,
      userName: item.telId,
      remark: item.information
    }
  })
}

// 等级文字
function getLevelText(grade) {
  const map = { 1: '一', 2: '二', 3: '三', 4: '四', 5: '五', 6: '六' }
  return map[grade] || '-'
}

// 等级颜色
function getLevelColor(grade) {
  const color = ['#02E300', '#FFFF00', '#FF7E00', '#FE0000', '#98004B', '#7E0123']
  return { backgroundColor: color[grade - 1] || '#fff', color: '#fff' }
}
</script>

<style scoped>
.task-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #eee;
  margin-top: 10px;
}
.level-tag {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: bold;
  margin-right: 20px;
}
.task-info {
  flex: 1;
}
.detail {
  margin: 5px 0 0;
  font-size: 13px;
  color: #999;
}
</style>