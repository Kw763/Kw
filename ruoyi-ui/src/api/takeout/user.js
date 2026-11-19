import request from '@/utils/request'

// 查询我的订单列表
export function listUserOrders(query) {
  return request({
    url: '/takeout/user/orders',
    method: 'get',
    params: query
  })
}

// 查询我的订单详细
export function getUserOrder(orderId) {
  return request({
    url: '/takeout/user/orders/' + orderId,
    method: 'get'
  })
}

// 模拟支付
export function mockPayOrder(orderId) {
  return request({
    url: '/takeout/user/orders/' + orderId + '/mockPay',
    method: 'post'
  })
}

// 查询我的地址列表
export function listUserAddress(query) {
  return request({
    url: '/takeout/user/address',
    method: 'get',
    params: query
  })
}

// 查询我的地址详细
export function getUserAddress(addressId) {
  return request({
    url: '/takeout/user/address/' + addressId,
    method: 'get'
  })
}

// 新增我的地址
export function addUserAddress(data) {
  return request({
    url: '/takeout/user/address',
    method: 'post',
    data: data
  })
}

// 修改我的地址
export function updateUserAddress(data) {
  return request({
    url: '/takeout/user/address',
    method: 'put',
    data: data
  })
}

// 删除我的地址
export function delUserAddress(addressId) {
  return request({
    url: '/takeout/user/address/' + addressId,
    method: 'delete'
  })
}
