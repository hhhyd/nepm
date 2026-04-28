import request from './request'

export const getAllProvinces = () => {
  return request({
    url: '/gridProvince/listGridProvinceAll',
    method: 'post'
  })
}
