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
        <!-- 查询条件区 -->
        <el-card shadow="never" style="margin-bottom: 20px;">
          <el-form :inline="true" :model="queryForm" label-width="80px">
            <el-form-item label="省区域">
              <el-select v-model="queryForm.provinceId" placeholder="请选择" @change="onProvinceChange" clearable style="width: 160px">
                <el-option v-for="item in provinceList" :key="item.provinceId" :label="item.provinceName" :value="item.provinceId" />
              </el-select>
            </el-form-item>
            <el-form-item label="市区域">
              <el-select v-model="queryForm.cityId" placeholder="请选择" clearable style="width: 160px">
                <el-option v-for="item in cityList" :key="item.cityId" :label="item.cityName" :value="item.cityId" />
              </el-select>
            </el-form-item>
            <el-form-item label="预估等级">
              <el-select v-model="queryForm.estimatedGrade" placeholder="请选择" clearable style="width: 140px">
                <el-option v-for="item in aqiLevelList" :key="item.aqiId" :label="item.aqiExplain" :value="item.aqiId" />
              </el-select>
            </el-form-item>
            <el-form-item label="反馈日期">
              <el-date-picker v-model="queryForm.afDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 160px" />
            </el-form-item>
            <el-form-item label="是否指派">
              <el-radio-group v-model="queryForm.state">
                <el-radio :label="0">未指派</el-radio>
                <el-radio :label="1">已指派</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">查询</el-button>
              <el-button type="success" @click="handleQueryAll">查询全部</el-button>
              <el-button @click="resetQuery">清空</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <!-- 数据表格 -->
        <el-table :data="feedbackList" v-loading="loading" stripe border>
          <el-table-column prop="afId" label="编号" width="80" align="center" />
          <el-table-column label="反馈者姓名" width="120">
            <template #default="{ row }">{{ row.supervisor?.realName || '-' }}</template>
          </el-table-column>
          <el-table-column label="所在省" width="100">
            <template #default="{ row }">{{ row.gridProvince?.provinceName || '-' }}</template>
          </el-table-column>
          <el-table-column label="所在市" width="100">
            <template #default="{ row }">{{ row.gridCity?.cityName || '-' }}</template>
          </el-table-column>
          <el-table-column label="预估等级" width="120">
            <template #default="{ row }">
              <el-tag :type="getLevelType(row.estimatedGrade)">{{ getLevelText(row.estimatedGrade) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="afDate" label="反馈日期" width="120" />
          <el-table-column prop="afTime" label="反馈时间" width="120" />
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" link @click="handleViewDetail(row)">查看详情</el-button>
              <el-button v-if="row.state === 0" type="success" link @click="handleAssign(row)">指派网格员</el-button>
              <el-button v-else type="warning" link @click="handleReassign(row)">重新指派</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
            v-model:current-page="queryForm.pageNum"
            :page-size="queryForm.maxPageNum"
            :total="totalRow"
            layout="total, prev, pager, next"
            @current-change="loadFeedbackList"
            style="margin-top: 20px; justify-content: center"
        />
      </el-main>
    </el-container>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="公众监督数据详情" width="600px">
      <el-descriptions v-if="currentFeedback" border :column="1">
        <el-descriptions-item label="反馈信息编号">{{ currentFeedback.afId }}</el-descriptions-item>
        <el-descriptions-item label="反馈者信息">
          {{ currentFeedback.supervisor?.realName }} ({{ currentFeedback.supervisor?.sex === 1 ? '男' : '女' }}, {{ currentFeedback.supervisor?.birthday }})
        </el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentFeedback.telId }}</el-descriptions-item>
        <el-descriptions-item label="反馈地址">{{ currentFeedback.gridProvince?.provinceName }} {{ currentFeedback.gridCity?.cityName }} {{ currentFeedback.address }}</el-descriptions-item>
        <el-descriptions-item label="反馈描述">{{ currentFeedback.information }}</el-descriptions-item>
        <el-descriptions-item label="预估等级">
          <el-tag>{{ getLevelText(currentFeedback.estimatedGrade) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="反馈时间">{{ currentFeedback.afDate }} {{ currentFeedback.afTime }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag v-if="currentFeedback.state === 0" type="info">未指派</el-tag>
          <el-tag v-else-if="currentFeedback.state === 1" type="warning">已指派</el-tag>
          <el-tag v-else type="success">已确认</el-tag>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentFeedback.state > 0" label="已指派网格员">
          {{ currentFeedback.gridMember?.gmName }} ({{ currentFeedback.gridMember?.tel }})
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">返回</el-button>
      </template>
    </el-dialog>

    <!-- 指派对话框 -->
    <el-dialog v-model="assignVisible" :title="isReassign ? '重新指派网格员' : '指派网格员'" width="600px">
      <el-form :model="assignForm" label-width="100px">
        <el-alert title="请根据监督信息所在区域选择网格员，原则上优先本地指派。" type="info" :closable="false" style="margin-bottom: 20px" />

        <el-form-item label="是否异地指派">
          <el-switch v-model="assignForm.isRemote" />
        </el-form-item>

        <template v-if="!assignForm.isRemote">
          <el-form-item label="省区域">
            <el-input :value="localFeedback?.gridProvince?.provinceName" disabled />
          </el-form-item>
          <el-form-item label="市区域">
            <el-input :value="localFeedback?.gridCity?.cityName" disabled />
          </el-form-item>
          <el-form-item label="选择网格员">
            <el-select v-model="assignForm.gmId" placeholder="请选择当地网格员" style="width: 100%">
              <el-option v-for="m in localMembers" :key="m.gmId" :label="`${m.gmName} (${m.state === 0 ? '在岗' : '忙碌'})`" :value="m.gmId" />
            </el-select>
          </el-form-item>
        </template>

        <template v-else>
          <el-form-item label="目标省份">
            <el-select v-model="assignForm.provinceId" placeholder="请选择" @change="onAssignProvinceChange" style="width: 100%">
              <el-option v-for="p in provinceList" :key="p.provinceId" :label="p.provinceName" :value="p.provinceId" />
            </el-select>
          </el-form-item>
          <el-form-item label="目标城市">
            <el-select v-model="assignForm.cityId" placeholder="请选择" @change="onAssignCityChange" style="width: 100%">
              <el-option v-for="c in assignCityList" :key="c.cityId" :label="c.cityName" :value="c.cityId" />
            </el-select>
          </el-form-item>
          <el-form-item label="选择网格员">
            <el-select v-model="assignForm.gmId" placeholder="请选择异地网格员" style="width: 100%">
              <el-option v-for="m in remoteMembers" :key="m.gmId" :label="`${m.gmName} (${m.state === 0 ? '在岗' : '忙碌'})`" :value="m.gmId" />
            </el-select>
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <el-button @click="assignVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmAssign" :loading="assignLoading">确认指派</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import { getFeedbackPage, assignFeedback } from '@/api/aqiFeedback'
import { getAllProvinces } from '@/api/gridProvince'
import { getCitiesByProvinceId } from '@/api/gridCity'
import { getGridMembersByProvince } from '@/api/gridMember'
import { ElMessage } from 'element-plus'

const levelMap = { 1: '优', 2: '良', 3: '轻度污染', 4: '中度污染', 5: '重度污染', 6: '严重污染' }
const getLevelText = (level) => levelMap[level] || '未知'
const getLevelType = (level) => [1, 2].includes(level) ? 'success' : [3, 4].includes(level) ? 'warning' : 'danger'

const loading = ref(false)
const feedbackList = ref([])
const totalRow = ref(0)
const provinceList = ref([])
const cityList = ref([])
const aqiLevelList = ref([
  { aqiId: 1, aqiExplain: '优' }, { aqiId: 2, aqiExplain: '良' }, { aqiId: 3, aqiExplain: '轻度污染' },
  { aqiId: 4, aqiExplain: '中度污染' }, { aqiId: 5, aqiExplain: '重度污染' }, { aqiId: 6, aqiExplain: '严重污染' }
])

const queryForm = reactive({
  pageNum: 1,
  maxPageNum: 10,
  provinceId: null,
  cityId: null,
  estimatedGrade: null,
  afDate: null,
  state: 0
})

const detailVisible = ref(false)
const currentFeedback = ref(null)

const assignVisible = ref(false)
const isReassign = ref(false)
const assignLoading = ref(false)
const localFeedback = ref(null)
const localMembers = ref([])

const assignForm = reactive({
  afId: null,
  gmId: null,
  isRemote: false,
  provinceId: null,
  cityId: null
})
const assignCityList = ref([])
const remoteMembers = ref([])

const loadFeedbackList = async () => {
  loading.value = true
  try {
    const res = await getFeedbackPage(queryForm)
    feedbackList.value = res.list || []
    totalRow.value = res.totalRow || 0
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}
const handleSearch = () => {
  queryForm.pageNum = 1
  loadFeedbackList()
}
const handleQueryAll = () => {
  queryForm.pageNum = 1
  queryForm.state = null
  loadFeedbackList()
}
const resetQuery = () => {
  queryForm.provinceId = null
  queryForm.cityId = null
  queryForm.estimatedGrade = null
  queryForm.afDate = null
  queryForm.state = 0
  cityList.value = []
  handleSearch()
}

const onProvinceChange = async (val) => {
  queryForm.cityId = null
  cityList.value = []
  if (val) {
    try {
      const res = await getCitiesByProvinceId({ provinceId: val })
      cityList.value = res || []
    } catch (e) {}
  }
}

const handleViewDetail = (row) => {
  currentFeedback.value = row
  detailVisible.value = true
}

const handleAssign = (row) => {
  isReassign.value = false
  initAssignDialog(row)
}

const handleReassign = (row) => {
  isReassign.value = true
  initAssignDialog(row)
}

const initAssignDialog = async (row) => {
  localFeedback.value = row
  assignForm.afId = row.afId
  assignForm.gmId = null
  assignForm.isRemote = false
  assignForm.provinceId = null
  assignForm.cityId = null
  localMembers.value = []

  try {
    const res = await getGridMembersByProvince({ provinceId: row.provinceId })
    localMembers.value = (res || []).filter(m => m.cityId === row.cityId)
  } catch (e) {}

  assignVisible.value = true
}

const onAssignProvinceChange = async (val) => {
  assignForm.cityId = null
  remoteMembers.value = []
  assignCityList.value = []
  if (val) {
    try {
      const res = await getCitiesByProvinceId({ provinceId: val })
      assignCityList.value = res || []
    } catch (e) {}
  }
}

const onAssignCityChange = async (val) => {
  assignForm.gmId = null
  remoteMembers.value = []
  if (val && assignForm.provinceId) {
    try {
      const res = await getGridMembersByProvince({ provinceId: assignForm.provinceId })
      remoteMembers.value = (res || []).filter(m => m.cityId === val && m.state === 0)
    } catch (e) {}
  }
}

const handleConfirmAssign = async () => {
  if (!assignForm.gmId) return ElMessage.warning('请选择网格员')
  assignLoading.value = true
  try {
    await assignFeedback({
      afId: assignForm.afId,
      gmId: assignForm.gmId,
      state: 1
    })
    ElMessage.success('指派成功')
    assignVisible.value = false
    loadFeedbackList()
  } catch (e) {
    ElMessage.error('操作失败')
  } finally {
    assignLoading.value = false
  }
}

onMounted(async () => {
  try {
    provinceList.value = await getAllProvinces() || []
    loadFeedbackList()
  } catch (e) {
    ElMessage.error('初始化数据失败')
  }
})
</script>

<style scoped>
.dashboard { min-height: 100vh; }
.main-container { min-height: calc(100vh - 60px); }
.el-aside { background-color: #545c64; }
.el-main { padding: 20px; }
</style>
