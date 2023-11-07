import request from '@/utils/request'

/**
 * 告警发送情况日志 接口文件
 *
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口配置项
var prefix = '/api/infra/data/flink/alartLog/' ;
var managerUrl = {
    datatables : prefix +"datatables" ,
    createUrl: prefix + 'add' ,
    saveUrl: prefix + 'save' ,
    updateUrl: prefix +"modify" ,
    statusUrl: prefix +"changeStatus" ,
    cleanUrl: prefix + "clean",
    detailUrl: prefix +"detail",
    removeUrl: prefix + "delete" ,
    exportUrl: prefix + "exportExcel",
    changeField: prefix + "changeField",
    logErrorInfo: prefix + "logErrorInfo"
}

// 查询告警发送情况日志列表
export function listAlartLog(query , data) {
  return request({
    url: managerUrl.datatables ,
    method: 'post',
    params: query ,
    data: data
  })
}

// 查询告警发送情况日志详细
export function getAlartLog(id) {
  return request({
    url: managerUrl.detailUrl + '/' + id ,
    method: 'get'
  })
}

// 新增告警发送情况日志
export function addAlartLog(data) {
  return request({
    url: managerUrl.saveUrl,
    method: 'post',
    data: data
  })
}

// 修改告警发送情况日志
export function updateAlartLog(data) {
  return request({
    url: managerUrl.updateUrl,
    method: 'put',
    data: data
  })
}

// 删除告警发送情况日志
export function delAlartLog(id) {
  return request({
    url: managerUrl.removeUrl + '/' + id ,
    method: 'delete'
  })
}

// 导出告警发送情况日志
export function exportAlartLog(query) {
  return request({
    url: managerUrl.exportUrl,
    method: 'get',
    params: query
  })
}

// 状态告警发送情况日志修改
export function changeStatusAlartLog(id , status) {
  const data = {
    id ,
    status
  }
  return request({
    url: managerUrl.statusUrl,
    method: 'put',
    data: data
  })
}

// 修改告警发送情况日志单个字段值
export function changeAlartLogField(value , field , id){
  const data = {
    value ,
    field ,
    id
  }
  return request({
    url: managerUrl.changeField ,
    method: 'post',
    data: data
  })
}


/**
 * 查看错误日志详情
 * @param {*} id
 * @returns
 */
export function logErrorInfo(id) {
  return request({
    url: managerUrl.logErrorInfo+"?id="+id,
    method: 'post'
  })
}
