import request from './request'

export const gridMemberLogin = (data) => {
  return request({
    url: '/gridMember/getGridMemberByCodeByPass',
    method: 'post',
    data
  })
}

export const getGridMembersByProvince = (data) => {
  return request({
    url: '/gridMember/listGridMemberByProvinceId',
    method: 'post',
    data
  })
}
