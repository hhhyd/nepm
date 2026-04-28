import request from './request'

export const getCitiesByProvinceId = (data) => {
  return request({
    url: '/gridCity/listGridCityByProvinceId',
    method: 'post',
    data
  })
}
