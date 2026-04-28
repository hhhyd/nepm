import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: null,
    userType: null // 'admin', 'supervisor', 'gridMember'
  }),
  
  getters: {
    isLoggedIn: (state) => !!state.userInfo
  },
  
  actions: {
    setLoginInfo(user, type) {
      this.userInfo = user
      this.userType = type
      localStorage.setItem('user', JSON.stringify({
        ...user,
        userType: type
      }))
    },
    
    logout() {
      this.userInfo = null
      this.userType = null
      localStorage.removeItem('user')
    },
    
    initUser() {
      const user = localStorage.getItem('user')
      if (user) {
        const userData = JSON.parse(user)
        this.userInfo = userData
        this.userType = userData.userType
      }
    }
  }
})
