import request from '@/utils/request'

// 查询状态日志列表
export function listStatusLog(query) {
  return request({
    url: '/takeout/admin/statusLog/list',
    method: 'get',
    params: query
  })
}
