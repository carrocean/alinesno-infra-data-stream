import request from '@/utils/request'

/**
 * flink任务配置 接口文件
 *
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口配置项
var prefix = '/api/infra/data/flink/jobConfig/' ;
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
    addConfig: prefix + "addConfig",
    deleteByID: prefix + "deleteByID",
    open: prefix + "open",
    close: prefix + "close",
    editConfig: prefix + "editConfig",
    start: prefix + "start",
    stop: prefix + "stop",
    copyConfig: prefix + "copyConfig",
    checkfSql: prefix + "checkfSql",
    jobTypeCnt: prefix + "jobTypeCnt",
    autoInspectionStat: prefix + "autoInspectionStat",
    inspectionCount: prefix + "inspectionCount",
    JobStatusStat: prefix + "JobStatusStat",
    savepoint: prefix + "savepoint",
    querySavePointList10: prefix + "querySavePointList10",
    addSavepoint: prefix + "addSavepoint",
}

// 查询flink任务配置列表
export function listJobConfig(query , data) {
  return request({
    url: managerUrl.datatables ,
    method: 'post',
    params: query ,
    data: data
  })
}

// 查询flink任务配置详细
export function getJobConfig(id) {
  return request({
    url: managerUrl.detailUrl + '/' + id ,
    method: 'get'
  })
}

// 删除flink任务配置
export function delJobConfig(id) {
  return request({
    url: managerUrl.deleteByID + '?id=' + id ,
    method: 'delete'
  })
}

// 导出flink任务配置
export function exportJobConfig(query) {
  return request({
    url: managerUrl.exportUrl,
    method: 'get',
    params: query
  })
}

// 状态flink任务配置修改
export function changeStatusJobConfig(id , status) {
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

// 修改flink任务配置单个字段值
export function changeJobConfigField(value , field , id){
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


//添加方法
/**
 * 新增任务
 * @param {任务对象} data
 * jobName: my_online
 * @returns
 */
export function addConfig(data) {
  return request({
    url: managerUrl.addConfig,
    method: 'post',
    data: data
  })
}

/**
 * 修改任务
 * @param {任务对象} data
 * @returns
 */
export function editConfig(data) {
  return request({
    url: managerUrl.editConfig,
    method: 'post',
    data: data
  })
}

/**
 * 查询历史版本列表
 * @param {*} data (jobConfigId,jobName)
 * @returns
 */
export function getTaskHistory(data) {
  return request({
    url: '/jobConfigHistoryPage',
    method: 'post',
    data: data
  })
}

/**
 * 查询历史版本详情
 * @param {*} id
 * @returns
 */
export function getTaskHistoryDetail(id) {
  return request({
    url: '/jobConfigHistoryDetail',
    method: 'post',
    data: { id: id }
  })
}


/**
 * 开启任务
 * @param {任务编号} id
 * @returns
 */
export function openTask(id) {
  return request({
    url: managerUrl.open + '?id='+id,
    method: 'post'
  })
}

/**
 * 关闭任务
 * @param {任务编号} id
 * @returns
 */
export function closeTask(id) {
  return request({
    url: managerUrl.close+ '?id='+id,
    method: 'post'
  })
}

/**
 * 启动任务
 * @param {任务编号} id
 * @returns
 */
export function startTask(id) {
  return request({
    url: managerUrl.start+ '?id='+id,
    method: 'post'
  })
}

/**
 * 停止任务
 * @param {任务编号} id
 * @param {备份编号} savepointId
 * @returns
 */
export function stopTask(id) {
  return request({
    url: managerUrl.stop+ '?id='+id,
    method: 'post'
  })
}

/**
 * 从savepoint备份地址中启动任务
 * @param {任务编号} id
 * @param {备份编号} savepointId
 * @returns
 */
export function startSavepoint(id, savepointId) {
  return request({
    url: managerUrl.start+ '?id='+id +"&savepointId="+savepointId,
    method: 'post'
  })
}

/**
 * 备份
 * @param {任务编号} id
 * @returns
 */
export function savePoint(id) {
  return request({
    url: managerUrl.savepoint+ '?id='+id ,
    method: 'post'
  })
}

/**
 * 复制任务
 * @param {任务编号} id
 * @returns
 */
export function copyConfig(id) {
  return request({
    url: managerUrl.copyConfig+'?id='+id,
    method: 'post'
  })
}

/**
 * 预校验SQL
 * @param {Flink SQL} flinkSql
 * @returns
 */
export function checkfSql(flinkSql) {
  return request({
    url: managerUrl.checkfSql+"?flinkSql="+flinkSql,
    method: 'post'
  })
}


// 仪表盘统计任务数量
export function jobTypeCnt() {
  return request({
    url: managerUrl.jobTypeCnt,
    method: 'post'
  })
}

export function getAutoInspectionStat() {
  return request({
    url: managerUrl.autoInspectionStat,
    method: 'get'
  })
}

// 仪表盘统计最近一月执行情况
export function getInspectionCount() {
  return request({
    url: managerUrl.inspectionCount,
    method: 'get'
  })
}

// 仪表盘统计作业状态
export function getJobStatusStat() {
  return request({
    url: managerUrl.JobStatusStat,
    method: 'get'
  })
}

/**
 * 查询SavePoint历史列表（最近10条）
 * @param {任务编号} jobConfigId
 * @returns
 */
export function querySavePointList10(jobConfigId) {
  return request({
    url: managerUrl.querySavePointList10+"?jobConfigId="+jobConfigId,
    method: 'post'
  })
}

/**
 * 手动增加savepoint保存地址
 * @param {任务编号} jobConfigId
 * @param {savepoint路径} savepointPath
 * @returns
 */
export function addSavepoint(jobConfigId, savepointPath) {
  return request({
    url: managerUrl.addSavepoint+"?jobConfigId="+jobConfigId+"&savepointPath="+savepointPath,
    method: 'post'
  })
}

