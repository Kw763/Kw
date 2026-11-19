import request from '@/utils/request'

// 查询订单列表
export function listOrder(query) {
  return request({
    url: '/takeout/admin/order/list',
    method: 'get',
    params: query
  })
}

// 查询订单详细
export function getOrder(orderId) {
  return request({
    url: '/takeout/admin/order/' + orderId,
    method: 'get'
  })
}
