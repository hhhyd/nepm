import request from './request'

export const supervisorLogin = (data) => {
  return request({
    url: '/supervisor/getSupervisorByIdByPass',
    method: 'post',
    data
  })
}

export const supervisorRegister = (data) => {
  return request({
    url: '/supervisor/saveSupervisor',
    method: 'post',
    data
  })
}

export const checkPhone = (data) => {
  return request({
    url: '/supervisor/getSupervisorById',
    method: 'post',
    data
  })
}
