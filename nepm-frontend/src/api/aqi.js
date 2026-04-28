import request from './request'

export const getAllAqi = () => {
  return request({
    url: '/aqi/listAqiAll',
    method: 'post'
  })
}
