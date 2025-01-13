<template>
  <!--
    告警发送情况日志 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->

  <div class="app-container" style="padding:10px;background-color: #fff;">
    <div class="op-title" v-if="isHome">
      最近告警
    </div>
    <br v-if="isHome">
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
      <el-form-item label="状态" prop="status">
        <el-select  v-model="queryParams.status" placeholder="请选择状态"  clearable  filterable  @keyup.enter.native="handleQuery">
          <el-option
            v-for="item in logStatusOptions"
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

    <el-row :gutter="10" class="mb8" v-if="!isHome">
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="AlartLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" v-if="!isHome"/>
      <el-table-column label="任务名称" align="left" prop="jobName" width="280" show-overflow-tooltip v-if="!isHome"/>
      <el-table-column prop="alarMLogTypeEnum" :show-overflow-tooltip="true" label="告警类型" width="110" align="center" show-overflow-tooltip>
        <template  #default="scope">
          <span>{{ getAlarMLogType(scope.row.alarMLogTypeEnum) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="消息内容" align="left" prop="message" show-overflow-tooltip />
      <el-table-column prop="alarmLogStatusEnum" label="状态" width="80" align="center">
        <template  #default="scope">
          <el-tag v-if="scope.row.alarmLogStatusEnum==='FAIL'" type="danger" size="mini">{{ getAlarmLogStatus(scope.row.alarmLogStatusEnum) }}</el-tag>
          <el-tag v-else-if="scope.row.alarmLogStatusEnum==='SUCCESS'" type="success" size="mini">{{ getAlarmLogStatus(scope.row.alarmLogStatusEnum) }}</el-tag>
<!--          <el-tag v-else type="info" size="mini">{{ getStatusDesc(scope.row.alarmLogStatusEnum) }}</el-tag>-->
        </template>
      </el-table-column>
      <el-table-column label="告警时间" align="center" prop="editTime"  width="170">
        <template  #default="scope">
          <span>{{ parseTime(scope.row.editTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="left" class-name="small-padding fixed-width" v-if="!isHome">
        <template  #default="scope">
          <el-button
            size="mini"
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row)" style="margin-right: 5px"
          >删除</el-button>
          <el-link v-if="scope.row.status=='0'"  @click="getLogErrorInfo(scope.row)" :underline="false">
            <el-button
              type="text"
              icon="Message"
              size="mini" style="margin-right: 5px">详情
            </el-button>
          </el-link>
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

    <el-dialog title="错误详情" v-model="dialogFailLogVisible">
      <p>{{ failLog }}</p>
    </el-dialog>

  </div>
</template>

<script  setup name="AlartLog">
import {
  listAlartLog,
  getAlartLog,
  delAlartLog,
  addAlartLog,
  changeAlartLogField,
  changeStatusAlartLog,
  updateAlartLog,
  exportAlartLog,logErrorInfo } from "@/api/flink/AlartLog";
import  searchParam  from "@/api/Search/searchform";
import  Condition  from "@/api/Search/Condition";
import {listJobConfig} from "@/api/flink/JobConfig";
import {parseTime} from "@/utils/ruoyi";

const { proxy } = getCurrentInstance();

// 遮罩层
const loading = ref(false);

// 选中数组
const ids = ref([]);

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

// 告警发送情况日志表格数据
const AlartLogList = ref([]);

// 弹出层标题
const title = ref("");

// 是否显示弹出层
const open = ref(false);

//告警日志状态
const logStatusOptions= ref([
  { value: 0, label: "失败" },
  { value: 1, label: "成功" }
]);


const searchParamTem = ref([]);

const JobNameList = ref([]);

const dialogFailLogVisible = ref(false);

const failLog = ref("");

// 搜索参数
const searchParams = ref([]);


const data = reactive({
  props:{
    isHome:false
  },

  // 查询参数
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    jobConfigId: null,
    jobName: null,
    message: null,
    type: null,
    status: null,
    failLog: null
  },

  // 查询参数配置对象
  queryParamsConfig: {
    jobConfigId: Condition.like(),
    jobName: Condition.like(),
    message: Condition.like(),
    type: Condition.like(),
    status: Condition.eq(),
    failLog: Condition.like(),
  },

  // 表单参数
  form: {},

  // 表单校验
  rules: {
    jobConfigId: [
      { required: true, message: "job_config的id  如果0代表的是测试,不能为空", trigger: "blur" }
    ],
    jobName: [
      { required: true, message: "job_config的id  如果0代表的是测试,不能为空", trigger: "blur" }
    ],
    message: [
      { required: true, message: "消息内容不能为空", trigger: "blur" }
    ],
    type: [
      { required: true, message: "1:钉钉不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "1:成功 0:失败不能为空", trigger: "blur" }
    ],
    failLog: [
      { required: true, message: "失败原因不能为空", trigger: "blur" }
    ]
  },

  Params: {
    pageNum : 1,
    pageSize: 1000,
    // hasStatus : 0,
    // hasDelete : 0,
    // modelName: null,
  },

  ParamsConfig:{
    // hasStatus :  Condition.eq(),
    // modelName: Condition.like()
  },

});

const { props, queryParams, queryParamsConfig, form, rules, Params, ParamsConfig } = toRefs(data);

onMounted(() => {
  // 在组件挂载后执行的逻辑
  getJobNameList() ;
})



/** 查询告警发送情况日志列表 */
function getList() {
    searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
    loading.value = true;
    listAlartLog(searchParams.value).then(response => {
      AlartLogList.value = response.rows;
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
    message: null,
    type: null,
    status: null,
    failLog: null
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
  title.value = "添加告警发送情况日志";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const jobConfigId = row.id || ids.value ;
  getAlartLog(jobConfigId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改告警发送情况日志";
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
  proxy.$modal.confirm('是否确认删除告警信息,任务名称为"' + jobNameList + '"的数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return delAlartLog(jobConfigIds);
    }).then(() => {
      getList();
    proxy.$modal.msgSuccess("删除成功");
    }).catch(error => {

    })
}



//增加任务清单下拉框功能
function getJobNameList() {
  searchParamTem.value = searchParam(ParamsConfig.value, Params.value);
  listJobConfig(searchParamTem.value).then(response => {
    JobNameList.value = response.rows;
  });
}

function getAlarMLogType(type) {
  switch (type) {
    case 'DINGDING': return '钉钉' ;
    case 'CALLBACK_URL': return '自定义回调http' ;
    default: return '' ;
  }
}

function getAlarmLogStatus(status) { // 任务状态
  switch (status) {
    case 'FAIL': return '失败' ;
    case 'SUCCESS': return '成功' ;
    default: return '' ;
  }
}

function getLogErrorInfo(row) {
  failLog.value = row.failLog ;
  dialogFailLogVisible.value = true ;
}

/** created */

getList();
// 查询公共状态
// getDicts("has_status").then(response => {
//   statusOptions.value = response.data;
// });

/** created */

</script>

<style>
.op-title {
  font-size: 14px;
  line-height: 14px;
  font-weight: 600;
  margin-bottom: 10px;
}
</style>
