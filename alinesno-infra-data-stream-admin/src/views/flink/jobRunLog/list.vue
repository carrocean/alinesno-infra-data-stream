<template>
  <!--
    运行任务日志 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
    <el-page-header @back="handleBack()"  :content=linkTitle v-show="showLinkTitle"  class="back"></el-page-header>
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务" prop="jobConfigId">
        <el-select  v-model="queryParams.jobConfigId" placeholder="请选择任务"  clearable  filterable  style="width: 300px" @keyup.enter.native="handleQuery">
          <el-option
            v-for="item in JobNameList"
            :key="item.id"
            :label="item.jobName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="任务状态" prop="jobStatus">
        <el-select  v-model="queryParams.jobStatus" placeholder="任务状态"  clearable  filterable  @keyup.enter.native="handleQuery">
          <el-option
            v-for="item in jobStatusList"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JobRunLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务名称" align="left" prop="jobName" />
      <el-table-column label="运行模式" align="left" prop="deployMode" />
      <el-table-column label="Flink任务Id" align="left" prop="jobId" />
      <el-table-column label="任务状态" align="left" prop="jobStatus" width="80px"  >
        <template  #default="scope">
          <el-tag v-if="scope.row.jobStatus===-2||scope.row.jobStatus==='UNKNOWN'" type="info" size="mini">{{ getStatusDesc(scope.row.jobStatus) }}</el-tag>
          <el-tag v-else-if="scope.row.jobStatus===-1||scope.row.jobStatus==='FAIL'" type="danger" size="mini">{{ getStatusDesc(scope.row.jobStatus) }}</el-tag>
          <el-tag v-else-if="scope.row.jobStatus===0||scope.row.jobStatus==='STOP'" type="warning" size="mini">{{ getStatusDesc(scope.row.jobStatus) }}</el-tag>
          <el-tag v-else-if="scope.row.jobStatus===1||scope.row.jobStatus==='RUN'" type="success" size="mini">{{ getStatusDesc(scope.row.jobStatus) }}</el-tag>
          <el-tag v-else-if="scope.row.jobStatus===2||scope.row.jobStatus==='STARTING'" size="mini">{{ getStatusDesc(scope.row.jobStatus) }}</el-tag>
          <el-tag v-else-if="scope.row.jobStatus===3||scope.row.jobStatus==='SUCCESS'" type="success" size="mini">{{ getStatusDesc(scope.row.jobStatus) }}</el-tag>
          <el-tag v-else type="info" size="mini">{{ getStatusDesc(scope.row.jobStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="启动时间" align="left" prop="startTime" width="170">
        <template  #default="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="left" prop="endTime" width="170">
        <template  #default="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center" class-name="small-padding fixed-width">
        <template  #default="scope">
<!--          <router-link :to="{name:'ViewTaskLogDetail', params:{flag:'loglist', context:queryContent(),  data:getRowData(scope.row)}}" >-->
          <router-link :to="{name:'ViewTaskLogDetail', params:{flag:'loglist', context:queryContent(),  data:scope.row.id}}" >
            <el-link style="margin-right: 5px" :underline="false">
              <el-button
                type="text"
                icon="Message"
                size="mini">详情
              </el-button>
            </el-link>
          </router-link>
          <el-button
            size="mini"
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row) " style="margin-right: 5px"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


  </div>
</template>

<script  setup  name="JobRunLog">
import {
  listJobRunLog,
  getJobRunLog,
  delJobRunLog,
  addJobRunLog,
  changeJobRunLogField,
  changeStatusJobRunLog,
  updateJobRunLog,
  exportJobRunLog } from "@/api/flink/JobRunLog";
import  Condition  from "@/api/Search/Condition";
import  searchParam  from "@/api/Search/searchform";
import {parseTime} from "@/utils/ruoyi";
import {listJobConfig} from "@/api/flink/JobConfig";

const { proxy } = getCurrentInstance();

// 遮罩层
const loading = ref(true);

// 选中数组
const ids = ref([]);

// 选中数组
const jobNames = ref([]);

// 非单个禁用
const single = ref(true);

// 非多个禁用
const multiple = ref(true);

// 状态
const statusOptions = ref([]);

// 显示搜索条件
const showSearch = ref(true);

// 总条数
const total = ref(0);

// 运行任务日志表格数据
const JobRunLogList = ref([]);

// 弹出层标题
const title = ref("");

// 是否显示弹出层
const open = ref(false);

// 搜索参数
const searchParams = ref([]);

const searchParamTem = ref([]);

const JobNameList = ref([]);

const linkTitle = ref("");

const showLinkTitle = ref(false);

const backFlag = ref(false);

const count = ref(0);

const currentPage = ref(0);

const pageSize = ref(0);

//任务状态
const jobStatusList= ref([
  { value: "UNKNOWN", label: "未知" },
  { value: "FAIL", label: "失败" },
  { value: "STOP", label: "停止" },
  { value: "RUN", label: "运行中" },
  { value: "STARTING", label: "启动中" },
  { value: "SUCCESS", label: "提交成功" }
]);

const data = reactive({
  // 表单参数
  form: {},

  // 查询参数
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    jobConfigId: null,
    jobName: null,
    jobDesc: null,
    deployMode: null,
    jobId: null,
    localLog: null,
    runIp: null,
    remoteLogUrl: null,
    startTime: null,
    endTime: null,
    jobStatus: null
  },

  // 查询参数配置对象
  queryParamsConfig: {
    jobConfigId: Condition.eq(),
    jobName: Condition.like(),
    jobDesc: Condition.like(),
    deployMode: Condition.like(),
    jobId: Condition.like(),
    localLog: Condition.like(),
    runIp: Condition.like(),
    remoteLogUrl: Condition.like(),
    startTime: Condition.eq(),
    endTime: Condition.eq(),
    jobStatus: Condition.eq(),
  },

  // 表单校验
  rules: {
    jobConfigId: [
      { required: true, message: "任务状态不能为空", trigger: "blur" }
    ],
    jobName: [
      { required: true, message: "任务名称不能为空", trigger: "blur" }
    ],
    jobDesc: [
      { required: true, message: "任务描述不能为空", trigger: "blur" }
    ],
    deployMode: [
      { required: true, message: "提交模式: standalone 、yarn 、yarn-session 不能为空", trigger: "blur" }
    ],
    jobId: [
      { required: true, message: "运行后的任务id不能为空", trigger: "blur" }
    ],
    localLog: [
      { required: true, message: "启动时本地日志不能为空", trigger: "blur" }
    ],
    runIp: [
      { required: true, message: "任务运行所在的机器不能为空", trigger: "blur" }
    ],
    remoteLogUrl: [
      { required: true, message: "远程日志url的地址不能为空", trigger: "blur" }
    ],
    startTime: [
      { required: true, message: "启动时间不能为空", trigger: "blur" }
    ],
    endTime: [
      { required: true, message: "启动时间不能为空", trigger: "blur" }
    ],
    jobStatus: [
      { required: true, message: "任务状态不能为空", trigger: "blur" }
    ]
  },

  ParamsTem: {
    pageNum : 1,
    pageSize: 1000,
    hasStatus : 0,
    hasDelete : 0,
    modelName: null,
  },

  ParamsConfigTem:{
    hasStatus :  Condition.eq(),
    modelName: Condition.like()
  },

  curParams: {
    flag: '', // tasklist
    data: {},
    context: '' // 父页面传递过来的参加，返回时带给父页面恢复上下文
  }

});

const {form, queryParams, queryParamsConfig, rules, ParamsTem, ParamsConfigTem, curParams } = toRefs(data);

onMounted(() => {
  // 在组件挂载后执行的逻辑
  linkTitle.value = proxy.$route.meta.title;
  showLinkTitle.value = false ;
  getJobNameList();
  if (proxy.$route.name === 'jobRunLog') {
    const params = proxy.$route.params ;
    if (params) {
      queryParams.value.jobId = (params.jobId) ? params.jobId : '';
      queryParams.value.jobConfigId = (params.jobConfigId) ? params.jobConfigId : '';
      queryParams.value.jobName = (params.jobName) ? params.jobName : '';
      if (params.currentPage) { // 恢复分页状态
        count.value = params.count ;
        currentPage.value = params.currentPage ;
        pageSize.value = params.pageSize ;
      }
      if (params.flag === 'tasklist') { // 保存由任务列表跳转过来的状态
        backFlag.value = true ;
        curParams.value.flag = params.flag ;
        curParams.value.data = params.data ;
        curParams.value.context = params.context ;
        showLinkTitle.value = true ;
      }
      if (params.parentContent) { // 详情回退后，保存继续回退到任务列表的状态
        backFlag.value = true ;
        curParams.value.context = params.parentContent ;
      }
    }
  };
  handleQuery() ;
})

function getRowData(rowData){
  return  JSON.stringify(rowData);
}


function handleBack() { // 返回
  if (curParams.value.flag === 'loglist') {
    proxy.$router.replace({ name: 'jobRunLog', params: curParams.value.context }) ;
  } else if (curParams.value.flag === 'tasklist') {
    proxy.$router.replace({ name: 'jobManage', params: curParams.value.context }) ;
  }
}

/** 查询运行任务日志列表 */
function getList() {
    searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
    loading.value = true;
    listJobRunLog(searchParams.value).then(response => {
      JobRunLogList.value = response.rows;
      total.value = response.total;
      loading.value = false;
    });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    jobConfigId: null,
    jobName: null,
    jobDesc: null,
    deployMode: null,
    jobId: null,
    localLog: null,
    runIp: null,
    remoteLogUrl: null,
    startTime: null,
    endTime: null,
    jobStatus: null
  };
  proxy.resetForm("form");
}

/** 搜索按钮操作 */
function handleQuery() {
  // 获取参数
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryForm");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  jobNames.value = selection.map(item => item.jobName + ' ');
  single.value = selection.length!==1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加运行任务日志";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const jobConfigId = row.id || ids.value;
  getJobRunLog(jobConfigId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改运行任务日志";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["form"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateJobRunLog(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addJobRunLog(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const jobConfigIds = row.id || ids.value ;
  let jobNameList = row.jobName || jobNames.value ;
  //避免弹出窗数据太长，只显示前15条数据
  if ( Array.isArray(jobNameList) && jobNameList.length > 15 ) {
    jobNameList = jobNameList.slice(0,15) ;
  }

  proxy.$confirm('是否确认删除运行任务日志，任务名称为"' + jobNameList + '"的数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return delJobRunLog(jobConfigIds) ;
    }).then(() => {
      getList() ;
      proxy.$modal.msgSuccess("删除成功") ;
    }).catch(error => {

    })
}

/** 状态修改 **/
function handleStatusChange(row) {
  return changeStatusJobRunLog(row.id, row.status).then(response=>{
    if( response.code == 200 ){
      proxy.$modal.msgSuccess("操作成功");
    }
  });
}

/** 修改字段状态 **/
function chanageFile(value , filed , id){
  return changeJobRunLogField(value , filed , id).then(response =>{
    if( response.code == 200 ){
      proxy.$modal.msgSuccess("操作成功");
    }
  }) ;
}

/** 导出按钮操作 */
function handleExport() {
  const queryParams = queryParams.value ;
  proxy.$confirm('是否确认导出所有运行任务日志数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return exportJobRunLog(queryParams) ;
    }).then(response => {
      proxy.download(response.msg) ;
    })
}

//增加任务清单下拉框功能
function getJobNameList() {
  searchParamTem.value = searchParam(ParamsConfigTem.value, ParamsTem.value) ;
  listJobConfig(searchParamTem.value).then(response => {
    JobNameList.value = response.rows ;
  });
}

function getStatusDesc(status) { // 任务状态
  switch (status) {
    case '-2' : return '未知' ;
    case '-1': return '失败' ;
    case '0': return '停止' ;
    case '1': return '运行中' ;
    case '2': return '启动中' ;
    case '3': return '提交成功' ;
    case 'UNKNOWN': return '未知' ;
    case 'FAIL': return '失败' ;
    case 'STOP': return '停止' ;
    case 'RUN': return '运行中' ;
    case 'STARTING': return '启动中' ;
    case 'SUCCESS': return '提交成功' ;
    default: return '' ;
  }
}

function queryContent() {
  // return {
  //   count: count.value,
  //   currentPage: currentPage.value,
  //   pageSize: pageSize.value,
  //   jobId: queryParams.value.jobId,
  //   jobConfigId: queryParams.value.jobConfigId,
  //   jobName: queryParams.value.jobName,
  //   parentContent: curParams.value.context
  // }
  let content = {} ;
  return  JSON.stringify(content);
}



/** created */
// 查询公共状态
// getDicts("has_status").then(response => {
//   statusOptions.value = response.data;
// });

/** created */

</script>
<style>
  .back{
    font-size: 15px;
    color: #303133;
    margin-bottom: 20px;
  }
</style>
