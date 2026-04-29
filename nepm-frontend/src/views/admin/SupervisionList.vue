<template>
  <div>
    <!-- 此页面与 FeedbackList.vue 功能相同，建议直接使用 FeedbackList -->
    <p>公众监督数据列表 - 请使用 FeedbackList.vue</p>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

onMounted(() => {
  router.replace('/admin/feedback')
})
</script>

<style scoped>
</style>
