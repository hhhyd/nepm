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
            <el-form-item>
              <el-button type="primary" @click="handleSearch">查询</el-button>
              <el-button @click="resetQuery">清空</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-table :data="statisticsList" v-loading="loading" stripe border>
          <el-table-column prop="id" label="编号" width="80" align="center" />
          <el-table-column label="所在省" width="100">
            <template #default="{ row }">{{ row.gridProvince?.provinceName || '-' }}</template>
          </el-table-column>
          <el-table-column label="所在市" width="100">
            <template #default="{ row }">{{ row.gridCity?.cityName || '-' }}</template>
          </el-table-column>
          <el-table-column prop="address" label="详细地址" />
          <el-table-column label="AQI等级" width="100">
            <template #default="{ row }">
              <el-tag :type="getLevelType(row.aqiId)">{{ getLevelText(row.aqiId) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="confirmDate" label="确认日期" width="120" />
          <el-table-column prop="confirmTime" label="确认时间" width="120" />
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" link @click="handleViewDetail(row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
            v-model:current-page="queryForm.pageNum"
            :page-size="queryForm.maxPageNum"
            :total="totalRow"
            layout="total, prev, pager, next"
            @current-change="loadStatisticsList"
            style="margin-top: 20px; justify-content: center"
        />
      </el-main>
    </el-container>

    <el-dialog v-model="detailVisible" title="确认AQI数据详情" width="700px">
      <el-descriptions v-if="currentDetail" border :column="2">
        <el-descriptions-item label="检测编号">{{ currentDetail.id }}</el-descriptions-item>
        <el-descriptions-item label="所在省">{{ currentDetail.gridProvince?.provinceName }}</el-descriptions-item>
        <el-descriptions-item label="所在市">{{ currentDetail.gridCity?.cityName }}</el-descriptions-item>
        <el-descriptions-item label="详细地址" :span="2">{{ currentDetail.address }}</el-descriptions-item>

        <el-descriptions-item label="SO₂浓度">{{ currentDetail.so2Value }} μg/m³</el-descriptions-item>
        <el-descriptions-item label="SO₂等级">
          <el-tag>{{ getLevelText(currentDetail.so2Level) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="CO浓度">{{ currentDetail.coValue }} μg/m³</el-descriptions-item>
        <el-descriptions-item label="CO等级">
          <el-tag>{{ getLevelText(currentDetail.coLevel) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="PM2.5浓度">{{ currentDetail.spmValue }} μg/m³</el-descriptions-item>
        <el-descriptions-item label="PM2.5等级">
          <el-tag>{{ getLevelText(currentDetail.spmLevel) }}</el-tag>
        </el-descriptions-item>

        <el-descriptions-item label="AQI等级" :span="2">
          <el-tag :type="getLevelType(currentDetail.aqiId)">{{ getLevelText(currentDetail.aqiId) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="确认时间" :span="2">{{ currentDetail.confirmDate }} {{ currentDetail.confirmTime }}</el-descriptions-item>
        <el-descriptions-item label="网格员">{{ currentDetail.gridMember?.gmName }}</el-descriptions-item>
        <el-descriptions-item label="反馈者">{{ currentDetail.fdId }}</el-descriptions-item>
        <el-descriptions-item label="反馈信息" :span="2">{{ currentDetail.information }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">返回</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import { getStatisticsPage } from '@/api/statistics'
import { getAllProvinces } from '@/api/gridProvince'
import { getCitiesByProvinceId } from '@/api/gridCity'
import { ElMessage } from 'element-plus'

const levelMap = { 1: '优', 2: '良', 3: '轻度污染', 4: '中度污染', 5: '重度污染', 6: '严重污染' }
const getLevelText = (level) => levelMap[level] || '未知'
const getLevelType = (level) => [1, 2].includes(level) ? 'success' : [3, 4].includes(level) ? 'warning' : 'danger'

const loading = ref(false)
const statisticsList = ref([])
const totalRow = ref(0)
const provinceList = ref([])
const cityList = ref([])

const queryForm = reactive({
  pageNum: 1,
  maxPageNum: 10,
  provinceId: null,
  cityId: null
})

const detailVisible = ref(false)
const currentDetail = ref(null)

const loadStatisticsList = async () => {
  loading.value = true
  try {
    const res = await getStatisticsPage(queryForm)
    statisticsList.value = res.list || []
    totalRow.value = res.totalRow || 0
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryForm.pageNum = 1
  loadStatisticsList()
}

const resetQuery = () => {
  queryForm.provinceId = null
  queryForm.cityId = null
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
  currentDetail.value = row
  detailVisible.value = true
}

onMounted(async () => {
  try {
    provinceList.value = await getAllProvinces() || []
    loadStatisticsList()
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
