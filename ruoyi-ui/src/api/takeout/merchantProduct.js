import request from '@/utils/request'

// 查询商家商品列表
export function listMerchantProduct(query) {
  return request({
    url: '/takeout/merchant/product/list',
    method: 'get',
    params: query
  })
}

// 查询商家商品详细
export function getMerchantProduct(productId) {
  return request({
    url: '/takeout/merchant/product/' + productId,
    method: 'get'
  })
}

// 新增商家商品
export function addMerchantProduct(data) {
  return request({
    url: '/takeout/merchant/product',
    method: 'post',
    data: data
  })
}

// 修改商家商品
export function updateMerchantProduct(data) {
  return request({
    url: '/takeout/merchant/product',
    method: 'put',
    data: data
  })
}

// 删除商家商品
export function delMerchantProduct(productId) {
  return request({
    url: '/takeout/merchant/product/' + productId,
    method: 'delete'
  })
}
