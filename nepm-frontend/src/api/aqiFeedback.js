import request from './request'

export const saveAqiFeedback = (data) => {
  return request({
    url: '/aqiFeedback/saveAqiFeedback',
    method: 'post',
    data
  })
}

export const getFeedbackListByTelId = (data) => {
  return request({
    url: '/aqiFeedback/listAqiFeedbackByTelId',
    method: 'post',
    data
  })
}

export const getFeedbackPage = (data) => {
  return request({
    url: '/aqiFeedback/listAqiFeedbackPage',
    method: 'post',
    data
  })
}

export const getFeedbackById = (data) => {
  return request({
    url: '/aqiFeedback/getAqiFeedbackById',
    method: 'post',
    data
  })
}

export const assignFeedback = (data) => {
  return request({
    url: '/aqiFeedback/updateAqiFeedbackAssign',
    method: 'post',
    data
  })
}

export const getFeedbackByGmId = (data) => {
  return request({
    url: '/aqiFeedback/listAqiFeedbackByGmId',
    method: 'post',
    data
  })
}

export const confirmFeedback = (data) => {
  return request({
    url: '/aqiFeedback/updateAqiFeedbackState',
    method: 'post',
    data
  })
}
