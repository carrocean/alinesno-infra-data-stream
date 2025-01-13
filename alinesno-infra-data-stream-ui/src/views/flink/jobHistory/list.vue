<template>
  <!--
    flink任务配置历史变更 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
    <el-page-header @back="handleBack()"  :content=linkTitle v-show="showLinkTitle"  class="back"></el-page-header>
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务" prop="jobConfigId">
        <el-select  v-model="queryParams.jobConfigId" placeholder="请选择任务"  clearable  filterable style="width: 480px" @keyup.enter.native="handleQuery">
          <el-option
            v-for="item in JobNameList"
            :key="item.id"
            :label="item.jobName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="任务名称" prop="jobName">
        <el-input
          v-model="queryParams.jobName"
          placeholder="请输入任务名称"
          clearable
          
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="Plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="Edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
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

    <el-table v-loading="loading" :data="JobConfigHistoryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务名称" align="left" prop="jobName" show-overflow-tooltip/>
      <el-table-column label="版本号" align="left" prop="version" width="100" show-overflow-tooltip/>
      <el-table-column label="版本描述" align="left" prop="jobDesc" show-overflow-tooltip/>
      <el-table-column label="运行模式" align="left" prop="deployMode" width="170" />
      <el-table-column label="详情" width="80" align="center">
        <template  #default="scope">
          <router-link :to="{name: getRouteTaskName(scope.row.jobTypeEnum), params:{flag:'history', context:queryContent(), data:getRowData(scope.row) }}">
            <el-link :underline="false">
              <el-button
                type="text"
                icon="Message"
                size="mini">详情
              </el-button>
            </el-link>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="添加时间" align="center" prop="createTime" width="170">
        <template  #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" align="center" class-name="small-padding fixed-width">
        <template  #default="scope">
          <el-button
            size="mini"
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row)"
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

    <!-- 添加或修改flink任务配置历史变更对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body>
      <el-form ref="JobConfigHistoryRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="job_config主表Id" prop="jobConfigId">
          <el-input v-model="form.jobConfigId" placeholder="请输入job_config主表Id" />
        </el-form-item>
        <el-form-item label="任务名称" prop="jobName">
          <el-input v-model="form.jobName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务描述" prop="jobDesc">
          <el-input v-model="form.jobDesc" placeholder="请输入任务描述" />
        </el-form-item>
        <el-form-item label="提交模式: standalone 、yarn 、yarn-session " prop="deployMode">
          <el-input v-model="form.deployMode" placeholder="请输入提交模式: standalone 、yarn 、yarn-session " />
        </el-form-item>
        <el-form-item label="flink运行配置" prop="flinkRunConfig">
          <el-input v-model="form.flinkRunConfig" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="sql语句" prop="flinkSql">
          <el-input v-model="form.flinkSql" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="checkPoint配置" prop="flinkCheckpointConfig">
          <el-input v-model="form.flinkCheckpointConfig" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="udf地址及连接器jar 如http://xxx.xxx.com/flink-streaming-udf.jar" prop="extJarPath">
          <el-input v-model="form.extJarPath" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="更新版本号" prop="version">
          <el-input v-model="form.version" placeholder="请输入更新版本号" />
        </el-form-item>
        <el-form-item label="批任务定时调度 如 0/20 * * * * ? 表示每20秒 执行任务 " prop="cron">
          <el-input v-model="form.cron" placeholder="请输入批任务定时调度 如 0/20 * * * * ? 表示每20秒 执行任务 " />
        </el-form-item>
        <el-form-item label="任务类型 0:sql 1:自定义jar" prop="jobType">
          <el-input v-model="form.jobType" placeholder="请输入任务类型 0:sql 1:自定义jar" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="JobConfigHistory">
import {
  listJobConfigHistory,
  getJobConfigHistory,
  delJobConfigHistory,
  addJobConfigHistory,
  changeJobConfigHistoryField,
  changeStatusJobConfigHistory,
  updateJobConfigHistory,
  exportJobConfigHistory } from "@/api/flink/JobConfigHistory";

import { listJobConfig } from "@/api/flink/JobConfig";
import  searchParam  from "@/api/Search/searchform";
import  Condition  from "@/api/Search/Condition";
import {parseTime} from "@/utils/ruoyi";

const { proxy } = getCurrentInstance();

// 遮罩层
const loading = ref(false);

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

const count = ref(0);

// 总条数
const total = ref(0);

// flink任务配置历史变更表格数据
const JobConfigHistoryList = ref([]);

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

const data = reactive({
  curParams: {
    flag: '', // tasklist
    data: {},
    context: '' // 父页面传递过来的参加，返回时带给父页面恢复上下文
  },

  // 查询参数
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    jobConfigId: null,
    jobName: null,
    jobDesc: null,
    deployMode: null,
    flinkRunConfig: null,
    flinkSql: null,
    flinkCheckpointConfig: null,
    extJarPath: null,
    version: null,
    cron: null,
    jobType: null
  },

  // 查询参数配置对象
  queryParamsConfig: {
    jobConfigId: Condition.eq(),
    jobName: Condition.like(),
    jobDesc: Condition.like(),
    deployMode: Condition.like(),
    flinkRunConfig: Condition.like(),
    flinkSql: Condition.like(),
    flinkCheckpointConfig: Condition.like(),
    extJarPath: Condition.like(),
    version: Condition.like(),
    cron: Condition.like(),
    jobType: Condition.like(),
  },

  // 表单参数
  form: {},

  // 表单校验
  rules: {
    jobConfigId: [
      { required: true, message: "job_config主表Id不能为空", trigger: "blur" }
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
    flinkRunConfig: [
      { required: true, message: "flink运行配置不能为空", trigger: "blur" }
    ],
    flinkSql: [
      { required: true, message: "sql语句不能为空", trigger: "blur" }
    ],
    flinkCheckpointConfig: [
      { required: true, message: "checkPoint配置不能为空", trigger: "blur" }
    ],
    extJarPath: [
      { required: true, message: "udf地址及连接器jar 如http://xxx.xxx.com/flink-streaming-udf.jar不能为空", trigger: "blur" }
    ],
    version: [
      { required: true, message: "更新版本号不能为空", trigger: "blur" }
    ],
    cron: [
      { required: true, message: "批任务定时调度 如 0/20 * * * * ? 表示每20秒 执行任务 不能为空", trigger: "blur" }
    ],
    jobType: [
      { required: true, message: "任务类型 0:sql 1:自定义jar不能为空", trigger: "blur" }
    ]
  },

  ParamsTem: {
    pageNum : 1,
    pageSize: 1000,
    // hasStatus : 0,
    // hasDelete : 0,
    // modelName: null,
  },

  ParamsConfigTem:{
    // hasStatus :  Condition.eq(),
    // modelName: Condition.like()
  }

});

const { curParams, queryParams, queryParamsConfig, rules, ParamsTem, ParamsConfigTem } = toRefs(data);

onMounted(() => {
  // 在组件挂载后执行的逻辑
  linkTitle.value = proxy.$route.meta.title;
  showLinkTitle.value = false ;
  getJobNameList();
  if ( proxy.$route.name === 'HistoryTask' ) {
    const params = proxy.$route.params;
    if ( params ) {
      queryParams.value.jobId = (params.jobId) ? params.jobId : '';
      queryParams.value.jobConfigId = (params.jobConfigId) ? params.jobConfigId : '';
      queryParams.value.jobName = (params.jobName) ? params.jobName : '';
      // if ( params.currentPage ) { // 恢复分页状态
      //   count.value = params.count ;
      //   queryParams.pageNum= params.currentPage;
      //   queryParams.pageSize = params.pageSize ;
      // }
      if ( params.flag === 'tasklist' ) { // 保存由任务列表跳转过来的状态
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
    handleQuery() ;
  } else {

    getList();
  }
})



function handleBack() { // 返回
      if ( curParams.value.flag === 'loglist' ) {
        proxy.$router.replace({ name: 'JobRunLog', params: curParams.value.context }) ;
      } else if ( curParams.value.flag === 'tasklist' ) {
        proxy.$router.replace({ name: 'JobManage', params: curParams.value.context }) ;
      }
}


/** 查询flink任务配置历史变更列表 */
function getList() {
    searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
    loading.value = true;
    listJobConfigHistory(searchParams.value).then(response => {
      JobConfigHistoryList.value = response.rows;
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
    flinkRunConfig: null,
    flinkSql: null,
    flinkCheckpointConfig: null,
    extJarPath: null,
    version: null,
    cron: null,
    jobType: null
  };
  proxy.resetForm("JobConfigHistoryRef");
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
  ids.value = selection.map(item => item.id) ;
  jobNames.value = selection.map(item => item.jobName + ' ') ;
  single.value = selection.length!==1 ;
  multiple.value = !selection.length ;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加flink任务配置历史变更";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const jobConfigId = row.id || ids.value
  getJobConfigHistory(jobConfigId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改flink任务配置历史变更";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["JobConfigHistoryRef"].validate(valid => {
    if ( valid ) {
      if ( form.value.id != null ) {
        updateJobConfigHistory(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addJobConfigHistory(form.value).then(response => {
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
  const jobConfigIds = row.id || ids.value;
  let jobNameList = row.jobName || jobNames.value;
  //避免弹出窗数据太长，只显示前15条数据
  if ( Array.isArray(jobNameList) && jobNameList.length > 15 ) {
    jobNameList = jobNameList.slice(0,15);
  }

  proxy.$modal.confirm('是否确认删除任务名称为"' + jobNameList + '"的数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return delJobConfigHistory(jobConfigIds);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(error => {

    })
}

/** 状态修改 **/
function handleStatusChange(row) {
  return changeStatusJobConfigHistory(row.id, row.status).then(response=>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  });
}

/** 修改字段状态 **/
function chanageFile(value , filed , id){
  return changeJobConfigHistoryField(value , filed , id).then(response =>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  }) ;
}

/** 导出按钮操作 */
function handleExport() {
  const queryParams = queryParams.value;
  proxy.$modal.confirm('是否确认导出所有flink任务配置历史变更数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return exportJobConfigHistory(queryParams);
    }).then(response => {
      proxy.download(response.msg);
    })
}

//增加任务清单下拉框功能
function getJobNameList() {
  searchParamTem.value = searchParam(ParamsConfigTem.value, ParamsTem.value);
  listJobConfig(searchParamTem.value).then(response => {
     JobNameList.value = response.rows;
  });
}

function getRouteTaskName(jobType) {
    switch (jobType) {
      case 'SQL_STREAMING': return 'ViewSqlStreamingTask' ;
      case 'SQL_BATCH': return 'ViewSqlBatchTask' ;
      case 'JAR': return 'ViewJarTask' ;
      default: return '' ;
    }
}

function queryContent() {
    // return {
    //   count: count.value,
    //   currentPage: currentPage.value,
    //   pageSize: pageSize.value,
    //   id: queryParams.value.jobConfigId,
    //   jobName: queryParams.value.jobName,
    //   parentContent: curParams.value.context
    // }
  return {}
}



function getRowData(rowData){
  return  JSON.stringify(rowData);
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
