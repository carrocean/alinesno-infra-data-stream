<template>
  <!--
    savepoint备份地址 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-form-item label="任务名称" prop="jobConfigId">
        <el-select  v-model="queryParams.jobConfigId" placeholder="请选择任务名称"  clearable  filterable  @keyup.enter.native="handleQuery" style="width: 480px">
          <el-option
            v-for="item in JobNameList"
            :key="item.id"
            :label="item.jobName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="备份时间" prop="backupTime">-->
<!--        <el-date-picker-->
<!--          clearable-->
<!--          -->
<!--          v-model="queryParams.backupTime"-->
<!--          range="backupTime"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="选择备份时间">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="Search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

<!--    <el-row :gutter="10" class="mb8">-->
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
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="Delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
<!--    </el-row>-->

    <el-table v-loading="loading" :data="SavepointBackupList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务" align="left" prop="jobConfigNameLabel"  :show-overflow-tooltip="true"/>
      <el-table-column label="地址" align="left" prop="savepointPath" :show-overflow-tooltip="true"/>
      <el-table-column label="备份时间" align="left" prop="backupTime" width="180">
        <template  #default="scope">
          <span>{{ parseTime(scope.row.backupTime) }}</span>
        </template>
      </el-table-column>

<!--      <el-table-column label="状态" prop="hasStatus">-->
<!--        <template  #default="scope">-->
<!--        <el-switch-->
<!--            v-model="scope.row.hasStatus"-->
<!--            :active-value="0"-->
<!--            :inactive-value="1"-->
<!--            @change="handleStatusChange(scope.row)"-->
<!--          ></el-switch>-->
<!--         </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="添加时间" align="center" prop="addTime" width="170">-->
<!--        <template  #default="scope">-->
<!--          <span>{{ parseDatetime(scope.row.addTime) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="操作" width="80" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="Edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--          >修改</el-button>-->
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

    <!-- 添加或修改savepoint备份地址对话框 -->
    <el-dialog :title="title"  v-model="open"  width="800px" append-to-body>
      <el-form ref="savepointBackupRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="备份时间" prop="jobConfigId">
          <el-input v-model="form.jobConfigId" placeholder="请输入备份时间" />
        </el-form-item>
        <el-form-item label="地址" prop="savepointPath">
          <el-input v-model="form.savepointPath" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="备份时间" prop="backupTime">
          <el-date-picker clearable 
            v-model="form.backupTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择备份时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup  name="SavepointBackup">
import {
  listSavepointBackup,
  getSavepointBackup,
  delSavepointBackup,
  addSavepointBackup,
  changeSavepointBackupField,
  changeStatusSavepointBackup,
  updateSavepointBackup,
  exportSavepointBackup } from "@/api/flink/SavepointBackup";
import  Condition  from "@/api/Search/Condition";
import  searchParam  from "@/api/Search/searchform";
import {parseTime} from "@/utils/ruoyi";
import {listJobConfig} from "@/api/flink/JobConfig";
import { onMounted } from 'vue'

const { proxy } = getCurrentInstance();

// 遮罩层
const loading = ref(true);

// 选中数组
const ids = ref([]);

 //
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

// savepoint备份地址表格数据
const SavepointBackupList = ref([]);

// 弹出层标题
const title = ref("");

// 是否显示弹出层
const open = ref(false);

// 搜索参数
const searchParams = ref([]);

const searchParamTem = ref([]);

const JobNameList = ref([]);

const data = reactive({
  // 表单参数
  form: {},

  // 查询参数
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    jobConfigId: null,
    savepointPath: null,
    backupTime: null
  },

  // 查询参数配置对象
  queryParamsConfig: {
    jobConfigId: Condition.eq(),
    savepointPath: Condition.like(),
    backupTime: Condition.like(),
  },

  // 表单校验
  rules: {
    jobConfigId: [
      { required: true, message: "备份时间不能为空", trigger: "blur" }
    ],
    savepointPath: [
      { required: true, message: "地址不能为空", trigger: "blur" }
    ],
    backupTime: [
      { required: true, message: "备份时间不能为空", trigger: "blur" }
    ]
  },

  ParamsTem: {
    pageNum : 1,
    pageSize: 1000,
    hasStatus : 0,
    modelName: null,
  },

  ParamsConfigTem:{
    hasStatus :  Condition.eq(),
    modelName: Condition.like()
  },

});

const {form, queryParams, queryParamsConfig,  rules, ParamsTem, ParamsConfigTem } = toRefs(data);


onMounted(() => {
  // 在组件挂载后执行的逻辑
  getJobNameList();
})

//增加任务清单下拉框功能
function getJobNameList() {
    searchParamTem.value = searchParam(ParamsConfigTem.value, ParamsTem.value);
    listJobConfig(searchParamTem.value).then(response => {
      JobNameList.value = response.rows;
    });
}

/** 查询savepoint备份地址列表 */
function getList() {
    searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
    loading.value = true;
  listSavepointBackup(searchParams.value).then(response => {
    SavepointBackupList.value = response.rows;
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
    jobConfigNameLabel: null,
    jobConfigId: null,
    savepointPath: null,
    backupTime: null
  };
  proxy.resetForm("savepointBackupRef");
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
  jobNames.value = selection.map(item => item.jobConfigNameLabel + ' ');
  single.value = selection.length!==1 ;
  multiple.value = !selection.length ;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加savepoint备份地址";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const jobConfigId = row.id || ids.value
  getSavepointBackup(jobConfigId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改savepoint备份地址";
  });
}

/** 提交按钮 */
function submitForm() {
  this.$refs["savepointBackupRef"].validate(valid => {
    if (valid) {
      if ( form.value.id != null ) {
        updateSavepointBackup(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addSavepointBackup(form.value).then(response => {
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
  let jobNameList =  row.jobConfigNameLabel || jobNames.value;
  //避免弹出窗数据太长，只显示前15条数据
  if ( jobNameList.length > 15 ) {
    jobNameList = jobNameList.slice(0,15);
  }

  proxy.$modal.$confirm('是否确认删除任务名称为"' + jobNameList + '"的备份记录?请在删除前到服务器删除对应的备份文件!', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return delSavepointBackup(jobConfigIds);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(error => {

    })
}

/** 状态修改 **/
function handleStatusChange(row) {
  return changeStatusSavepointBackup(row.id, row.status).then(response=>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  });
}

/** 修改字段状态 **/
function chanageFile(value , filed , id){
  return changeSavepointBackupField(value , filed , id).then(response =>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  }) ;
}

/** 导出按钮操作 */
function handleExport() {
  const queryParams = this.queryParams;
  proxy.$modal.$confirm('是否确认导出所有savepoint备份地址数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return exportSavepointBackup(queryParams);
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
