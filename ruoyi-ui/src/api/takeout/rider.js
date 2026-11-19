import request from '@/utils/request'

// 查询骑手任务列表
export function listRiderTask(query) {
  return request({
    url: '/takeout/rider/tasks',
    method: 'get',
    params: query
  })
}

// 骑手接单
export function acceptRiderTask(taskId) {
  return request({
    url: '/takeout/rider/tasks/' + taskId + '/accept',
    method: 'post'
  })
}

// 骑手到店
export function arriveRiderTask(taskId) {
  return request({
    url: '/takeout/rider/tasks/' + taskId + '/arrive',
    method: 'post'
  })
}

// 骑手取餐
export function pickupRiderTask(taskId) {
  return request({
    url: '/takeout/rider/tasks/' + taskId + '/pickup',
    method: 'post'
  })
}

// 骑手送达
export function deliverRiderTask(taskId) {
  return request({
    url: '/takeout/rider/tasks/' + taskId + '/deliver',
    method: 'post'
  })
}
