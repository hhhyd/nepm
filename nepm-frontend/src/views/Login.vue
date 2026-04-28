<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2>全国环境监测网格化平台</h2>
      </template>
      
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item label="用户类型">
          <el-select v-model="loginForm.userType" placeholder="请选择用户类型">
            <el-option label="系统管理员" value="admin" />
            <el-option label="公众监督员" value="supervisor" />
            <el-option label="网格员" value="gridMember" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="账号" prop="account">
          <el-input v-model="loginForm.account" placeholder="请输入账号" />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
        
        <el-form-item>
          <el-button type="text" @click="$router.push('/register')">
            公众监督员注册
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { adminLogin } from '@/api/admin'
import { supervisorLogin } from '@/api/supervisor'
import { gridMemberLogin } from '@/api/gridMember'

const router = useRouter()
const userStore = useUserStore()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = ref({
  userType: 'admin',
  account: '',
  password: ''
})

const rules = {
  account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    
    try {
      let response
      let userData
      
      switch (loginForm.value.userType) {
        case 'admin':
          response = await adminLogin({
            adminCode: loginForm.value.account,
            password: loginForm.value.password
          })
          if (response && response.adminId) {
            userData = response
          }
          break
          
        case 'supervisor':
          response = await supervisorLogin({
            telId: loginForm.value.account,
            password: loginForm.value.password
          })
          if (response && response.telId) {
            userData = response
          }
          break
          
        case 'gridMember':
          response = await gridMemberLogin({
            gmCode: loginForm.value.account,
            password: loginForm.value.password
          })
          if (response && response.gmId) {
            userData = response
          }
          break
      }
      
      if (userData) {
        userStore.setLoginInfo(userData, loginForm.value.userType)
        ElMessage.success('登录成功')
        
        // 根据用户类型跳转不同页面
        switch (loginForm.value.userType) {
          case 'admin':
            router.push('/admin/dashboard')
            break
          case 'supervisor':
            router.push('/supervisor/dashboard')
            break
          case 'gridMember':
            router.push('/gridMember/dashboard')
            break
        }
      } else {
        ElMessage.error('账号或密码错误')
      }
    } catch (error) {
      ElMessage.error('登录失败，请检查网络')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 450px;
}

.login-card h2 {
  text-align: center;
  margin: 0;
  color: #409EFF;
}
</style>
