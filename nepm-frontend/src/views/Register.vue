<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <h2>公众监督员注册</h2>
      </template>
      
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef">
        <el-form-item label="手机号" prop="telId">
          <el-input v-model="registerForm.telId" placeholder="请输入手机号" maxlength="11" />
        </el-form-item>
        
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="registerForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" show-password />
        </el-form-item>
        
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker
            v-model="registerForm.birthday"
            type="date"
            placeholder="选择出生日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="registerForm.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading" style="width: 100%">
            注册
          </el-button>
        </el-form-item>
        
        <el-form-item>
          <el-button type="text" @click="$router.push('/login')">
            返回登录
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
import { supervisorRegister, checkPhone } from '@/api/supervisor'

const router = useRouter()
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = ref({
  telId: '',
  realName: '',
  password: '',
  confirmPassword: '',
  birthday: '',
  sex: 1
})

const validatePass = (rule, value, callback) => {
  if (value !== registerForm.value.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  telId: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 3, max: 20, message: '密码长度3-20位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validatePass, trigger: 'blur' }
  ],
  birthday: [{ required: true, message: '请选择出生日期', trigger: 'change' }],
  sex: [{ required: true, message: '请选择性别', trigger: 'change' }]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    
    try {
      // 检查手机号是否已注册
      const checkResult = await checkPhone({ telId: registerForm.value.telId })
      if (checkResult && checkResult.telId) {
        ElMessage.warning('该手机号已注册')
        loading.value = false
        return
      }
      
      // 注册
      const result = await supervisorRegister({
        telId: registerForm.value.telId,
        realName: registerForm.value.realName,
        password: registerForm.value.password,
        birthday: registerForm.value.birthday,
        sex: registerForm.value.sex
      })
      
      if (result > 0) {
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      } else {
        ElMessage.error('注册失败')
      }
    } catch (error) {
      ElMessage.error('注册失败，请检查网络')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-card {
  width: 500px;
}

.register-card h2 {
  text-align: center;
  margin: 0;
  color: #409EFF;
}
</style>
