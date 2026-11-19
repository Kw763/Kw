import request from '@/utils/request'

// 查询配送任务列表
export function listDelivery(query) {
  return request({
    url: '/takeout/admin/delivery/list',
    method: 'get',
    params: query
  })
}

// 派单
export function assignDelivery(taskId, riderId) {
  return request({
    url: '/takeout/admin/delivery/' + taskId + '/assign',
    method: 'put',
    params: { riderId }
  })
}

// 查询可用骑手列表
export function availableRiders() {
  return request({
    url: '/takeout/admin/rider/available',
    method: 'get'
  })
}
