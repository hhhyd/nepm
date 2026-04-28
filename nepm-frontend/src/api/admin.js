import request from './request'

export const adminLogin = (data) => {
  return request({
    url: '/admins/getAdminsByCodeByPass',
    method: 'post',
    data
  })
}
