import request from '@/utils/request'

/**
 * 运行任务日志 接口文件
 *
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口配置项
var prefix = '/api/infra/data/flink/jobRunLog/' ;
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
    logDetail: prefix + "logDetail",
    jobTypeRunCnt: prefix + "jobTypeRunCnt",
    coordinateData: prefix + "coordinateData",
    jobRunStat: prefix + "jobRunStat",
    ClinetJobInfo : "/log/getFlinkLocalJobLog"
}

// 查询运行任务日志列表
export function listJobRunLog(query , data) {
  return request({
    url: managerUrl.datatables ,
    method: 'post',
    params: query ,
    data: data
  })
}

// 查询运行任务日志详细
export function getJobRunLog(id) {
  return request({
    url: managerUrl.detailUrl + '/' + id ,
    method: 'get'
  })
}

// 新增运行任务日志
export function addJobRunLog(data) {
  return request({
    url: managerUrl.saveUrl,
    method: 'post',
    data: data
  })
}

// 修改运行任务日志
export function updateJobRunLog(data) {
  return request({
    url: managerUrl.updateUrl,
    method: 'put',
    data: data
  })
}

// 删除运行任务日志
export function delJobRunLog(id) {
  return request({
    url: managerUrl.removeUrl + '/' + id ,
    method: 'delete'
  })
}

// 导出运行任务日志
export function exportJobRunLog(query) {
  return request({
    url: managerUrl.exportUrl,
    method: 'get',
    params: query
  })
}

// 状态运行任务日志修改
export function changeStatusJobRunLog(id , status) {
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

// 修改运行任务日志单个字段值
export function changeJobRunLogField(value , field , id){
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
 * 查询日志详情
 * @param {日志编号} logid
 * @returns
 */
export function logDetail(logid) {
  return request({
    url: managerUrl.logDetail+"?logid="+logid,
    method: 'post',
    data: {
      logid: logid
    }
  })
}

/**
 * 查询日志详情
 * @param {日志编号} logid
 * @returns
 */
export function jobTypeRunCnt() {
  return request({
    url: managerUrl.jobTypeRunCnt,
    method: 'post'
  })
}


// 查询近一个月每天的执行情况
export function getCoordinateData() {
  return request({
    url: managerUrl.coordinateData,
    method: 'get'
  })
}

// 查询运行统计
export function getJobRunStat() {
  return request({
    url: managerUrl.jobRunStat,
    method: 'get'
  })
}


export function getClinetJobInfo(){
  return request({
    url: managerUrl.ClinetJobInfo ,
    method: 'get'
  })
}
