import request from '@/utils/request'

// 查询商家订单列表
export function listMerchantOrder(query) {
  return request({
    url: '/takeout/merchant/order/list',
    method: 'get',
    params: query
  })
}

// 查询商家订单详细
export function getMerchantOrder(orderId) {
  return request({
    url: '/takeout/merchant/order/' + orderId,
    method: 'get'
  })
}

// 商家接单
export function acceptMerchantOrder(orderId) {
  return request({
    url: '/takeout/merchant/order/' + orderId + '/accept',
    method: 'post'
  })
}

// 商家出餐
export function markReadyMerchantOrder(orderId) {
  return request({
    url: '/takeout/merchant/order/' + orderId + '/markReady',
    method: 'post'
  })
}
