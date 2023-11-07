<template>
  <!--
    告警辅助配置 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="job_config主表id" prop="jobId">
        <el-input
          v-model="queryParams.jobId"
          placeholder="请输入job_config主表id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型 1:钉钉告警 2:url回调 3:异常自动拉起任务" prop="type">
        <el-input
          v-model="queryParams.type"
          placeholder="请输入类型 1:钉钉告警 2:url回调 3:异常自动拉起任务"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="更新版本号  " prop="version">
        <el-input
          v-model="queryParams.version"
          placeholder="请输入更新版本号  "
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JobAlarmConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="job_config主表id" align="left" prop="jobId" />
      <el-table-column label="类型 1:钉钉告警 2:url回调 3:异常自动拉起任务" align="left" prop="type" />
      <el-table-column label="更新版本号  " align="left" prop="version" />

      <el-table-column label="状态" prop="hasStatus">
        <template  #default="scope">
        <el-switch
            v-model="scope.row.hasStatus"
            :active-value="0"
            :inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
         </template>
      </el-table-column>
      <el-table-column label="添加时间" align="center" prop="addTime" width="170">
        <template  #default="scope">
          <span>{{ parseDatetime(scope.row.addTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
        <template  #default="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
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

    <!-- 添加或修改告警辅助配置对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body>
      <el-form ref="JobAlarmConfigRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="job_config主表id" prop="jobId">
          <el-input v-model="form.jobId" placeholder="请输入job_config主表id" />
        </el-form-item>
        <el-form-item label="类型 1:钉钉告警 2:url回调 3:异常自动拉起任务" prop="type">
          <el-input v-model="form.type" placeholder="请输入类型 1:钉钉告警 2:url回调 3:异常自动拉起任务" />
        </el-form-item>
        <el-form-item label="更新版本号  " prop="version">
          <el-input v-model="form.version" placeholder="请输入更新版本号  " />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script  setup name="JobAlarmConfig">
import {
  listJobAlarmConfig,
  getJobAlarmConfig,
  delJobAlarmConfig,
  addJobAlarmConfig,
  changeJobAlarmConfigField,
  changeStatusJobAlarmConfig,
  updateJobAlarmConfig,
  exportJobAlarmConfig } from "@/api/flink/JobAlarmConfig";
import  Condition  from "@/api/Search/Condition";
const { proxy } = getCurrentInstance();


// 遮罩层
const loading = ref(false);

// 选中数组
const ids = ref([]);

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

// 告警辅助配置表格数据
const JobAlarmConfigList = ref([]);

// 弹出层标题
const title = ref("");

// 是否显示弹出层
const open = ref(false);

// 搜索参数
const searchParams = ref([]);

const data = reactive({
  // 查询参数
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    jobId: null,
    type: null,
    version: null
  },

  // 查询参数配置对象
  queryParamsConfig: {
    jobId: Condition.like(),
    type: Condition.like(),
    version: Condition.like(),
  },

  // 表单参数
  form: {},

   // 表单校验
  rules: {
    jobId: [
      { required: true, message: "job_config主表id不能为空", trigger: "blur" }
    ],
    type: [
      { required: true, message: "类型 1:钉钉告警 2:url回调 3:异常自动拉起任务不能为空", trigger: "blur" }
    ],
    version: [
      { required: true, message: "更新版本号  不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, queryParamsConfig, form, rules } = toRefs(data);

/** 查询告警辅助配置列表 */
function getList() {
    searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
    loading.value = true;
    listJobAlarmConfig(searchParams.value).then(response => {
      JobAlarmConfigList.value = response.rows;
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
    jobId: null,
    type: null,
    version: null
  };
  proxy.resetForm("JobAlarmConfigRef");
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
  single.value = selection.length!==1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加告警辅助配置";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const jobId = row.id || ids.value
  getJobAlarmConfig(jobId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改告警辅助配置";
  });
}

/** 提交按钮 */
function submitForm() {
  this.$refs["JobAlarmConfigRef"].validate(valid => {
    if (valid) {
      if ( form.value.id != null ) {
        updateJobAlarmConfig(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addJobAlarmConfig(form.value).then(response => {
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
  const jobIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除告警辅助配置编号为"' + jobIds + '"的数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return delJobAlarmConfig(jobIds);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
}

/** 状态修改 **/
function handleStatusChange(row) {
  return changeStatusJobAlarmConfig(row.id, row.status).then(response=>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  });
}

/** 修改字段状态 **/
function chanageFile(value , filed , id){
  return changeJobAlarmConfigField(value , filed , id).then(response =>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  }) ;
}

/** 导出按钮操作 */
function handleExport() {
  const queryParams = queryParams.value;
  proxy.$modal.confirm('是否确认导出所有告警辅助配置数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return exportJobAlarmConfig(queryParams);
    }).then(response => {
    proxy.download(response.msg);
    })
}

/** created */

  getList();
  // 查询公共状态
  // getDicts("has_status").then(response => {
  //   statusOptions.value = response.data;
  // });

/** created */


</script>
