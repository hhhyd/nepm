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
        <h2>反馈信息管理</h2>
        
        <el-table :data="feedbackList" style="width: 100%; margin-top: 20px" v-loading="loading">
          <el-table-column prop="afId" label="编号" width="80" />
          <el-table-column label="监督员" width="120">
            <template #default="{ row }">
              {{ row.supervisor?.realName || row.telId }}
            </template>
          </el-table-column>
          <el-table-column label="省份" width="100">
            <template #default="{ row }">
              {{ row.gridProvince?.provinceName || row.provinceId }}
            </template>
          </el-table-column>
          <el-table-column label="城市" width="100">
            <template #default="{ row }">
              {{ row.gridCity?.cityName || row.cityId }}
            </template>
          </el-table-column>
          <el-table-column prop="address" label="地址" />
          <el-table-column prop="information" label="反馈内容" />
          <el-table-column label="预估等级" width="100">
            <template #default="{ row }">
              <el-tag :type="getLevelType(row.estimatedGrade)">
                {{ getLevelText(row.estimatedGrade) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.state === 0 ? 'info' : row.state === 1 ? 'warning' : 'success'">
                {{ row.state === 0 ? '未指派' : row.state === 1 ? '已指派' : '已确认' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button
                v-if="row.state === 0"
                type="primary"
                size="small"
                @click="handleAssign(row)"
              >
                指派
              </el-button>
              <el-button
                v-else
                type="text"
                size="small"
                @click="handleView(row)"
              >
                查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <el-pagination
          v-model:current-page="pageNum"
          :page-size="maxPageNum"
          :total="totalRow"
          layout="total, prev, pager, next"
          @current-change="handlePageChange"
          style="margin-top: 20px; justify-content: center"
        />
      </el-main>
    </el-container>
    
    <!-- 指派对话框 -->
    <el-dialog v-model="assignDialogVisible" title="指派网格员" width="500px">
      <el-form :model="assignForm">
        <el-form-item label="选择网格员">
          <el-select v-model="assignForm.gmId" placeholder="请选择网格员" style="width: 100%">
            <el-option
              v-for="item in gridMembers"
              :key="item.gmId"
              :label="item.gmName"
              :value="item.gmId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmAssign" :loading="assignLoading">
          确认指派
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import { getFeedbackPage, assignFeedback } from '@/api/aqiFeedback'
import { getGridMembersByProvince } from '@/api/gridMember'
import { getAllProvinces } from '@/api/gridProvince'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const feedbackList = ref([])
const pageNum = ref(1)
const maxPageNum = 10
const totalRow = ref(0)

const assignDialogVisible = ref(false)
const assignLoading = ref(false)
const assignForm = ref({
  afId: null,
  gmId: null
})
const gridMembers = ref([])
const currentFeedback = ref(null)

const levelMap = {
  1: '优', 2: '良', 3: '轻度污染',
  4: '中度污染', 5: '重度污染', 6: '严重污染'
}

const getLevelText = (level) => levelMap[level] || '未知'

const getLevelType = (level) => {
  const typeMap = {
    1: 'success', 2: 'success', 3: 'warning',
    4: 'warning', 5: 'danger', 6: 'danger'
  }
  return typeMap[level] || 'info'
}

const loadFeedbackList = async () => {
  loading.value = true
  try {
    const result = await getFeedbackPage({
      pageNum: pageNum.value,
      maxPageNum: maxPageNum
    })
    
    if (result) {
      feedbackList.value = result.list || []
      totalRow.value = result.totalRow || 0
    }
  } catch (error) {
    ElMessage.error('加载反馈列表失败')
  } finally {
    loading.value = false
  }
}

const handlePageChange = () => {
  loadFeedbackList()
}

const handleAssign = async (row) => {
  currentFeedback.value = row
  
  try {
    const provinces = await getAllProvinces()
    if (provinces && provinces.length > 0) {
      const members = await getGridMembersByProvince({
        provinceId: row.provinceId
      })
      gridMembers.value = members || []
    }
    
    assignForm.value = {
      afId: row.afId,
      gmId: null
    }
    assignDialogVisible.value = true
  } catch (error) {
    ElMessage.error('加载网格员列表失败')
  }
}

const handleConfirmAssign = async () => {
  if (!assignForm.value.gmId) {
    ElMessage.warning('请选择网格员')
    return
  }
  
  assignLoading.value = true
  try {
    const result = await assignFeedback({
      afId: assignForm.value.afId,
      gmId: assignForm.value.gmId,
      state: 1
    })
    
    if (result > 0) {
      ElMessage.success('指派成功')
      assignDialogVisible.value = false
      loadFeedbackList()
    }
  } catch (error) {
    ElMessage.error('指派失败')
  } finally {
    assignLoading.value = false
  }
}

const handleView = (row) => {
  ElMessage.info('查看详情功能待完善')
}

onMounted(() => {
  loadFeedbackList()
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
