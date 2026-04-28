import request from './request'

export const saveStatistics = (data) => {
  return request({
    url: '/statistics/saveStatistics',
    method: 'post',
    data
  })
}

export const getStatisticsPage = (data) => {
  return request({
    url: '/statistics/listStatisticsPage',
    method: 'post',
    data
  })
}

export const getStatisticsById = (data) => {
  return request({
    url: '/statistics/getStatisticsById',
    method: 'post',
    data
  })
}

export const getProvinceItemTotalStatis = () => {
  return request({
    url: '/statistics/listProvinceItemTotalStatis',
    method: 'post'
  })
}

export const getAqiDistributeTotalStatis = () => {
  return request({
    url: '/statistics/listAqiDistributeTotalStatis',
    method: 'post'
  })
}

export const getAqiTrendTotalStatis = () => {
  return request({
    url: '/statistics/listAqiTrendTotalStatis',
    method: 'post'
  })
}

export const getAqiCount = () => {
  return request({
    url: '/statistics/getAqiCount',
    method: 'post'
  })
}

export const getAqiGoodCount = () => {
  return request({
    url: '/statistics/getAqiGoodCount',
    method: 'post'
  })
}

export const getProvinceCoverage = () => {
  return request({
    url: '/statistics/getProvinceCoverage',
    method: 'post'
  })
}

export const getCityCoverage = () => {
  return request({
    url: '/statistics/getCityCoverage',
    method: 'post'
  })
}
