<template>
  <!--
    web服务运行ip 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="1:运行 -1:停止 " prop="status">
        <el-input
          v-model="queryParams.status"
          placeholder="请输入1:运行 -1:停止 "
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最后心跳时间" prop="lastTime">
        <el-date-picker
          clearable
          size="small"
          v-model="queryParams.lastTime"
          range="lastTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择最后心跳时间">
        </el-date-picker>
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

    <el-table v-loading="loading" :data="IpStatusList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ip" align="left" prop="ip" />
      <el-table-column label="1:运行 -1:停止 " align="left" prop="status" />
      <el-table-column label="最后心跳时间" align="left" prop="lastTime" width="180">
        <template  #default="scope">
          <span>{{ parseTime(scope.row.lastTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>

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

    <!-- 添加或修改web服务运行ip对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body>
      <el-form ref="IpStatusRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="1:运行 -1:停止 " prop="status">
          <el-input v-model="form.status" placeholder="请输入1:运行 -1:停止 " />
        </el-form-item>
        <el-form-item label="最后心跳时间" prop="lastTime">
          <el-date-picker clearable size="small"
            v-model="form.lastTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择最后心跳时间">
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

<script  setup name="IpStatus">
import {
  listIpStatus,
  getIpStatus,
  delIpStatus,
  addIpStatus,
  changeIpStatusField,
  changeStatusIpStatus,
  updateIpStatus,
  exportIpStatus } from "@/api/flink/IpStatus";
import  searchParam  from "@/api/Search/searchform";
import  Condition  from "@/api/Search/Condition";
const { proxy } = getCurrentInstance();

// 遮罩层
const loading = ref(false);

// 选中数组
const ids = ref([]);

// 非单个禁用
const single = ref(true);

// 非多个禁用
const multiplee = ref(true);

// 状态
const statusOptions = ref([]);

// 显示搜索条件
const showSearche = ref(true);

// 总条数
const total = ref(0);

// web服务运行ip表格数据
const IpStatusList = ref([]);

// 弹出层标题
const title = ref("");

// 是否显示弹出层
const opene = ref(false);

// 搜索参数
const searchParams = ref([]);

const data = reactive({
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: null,
        lastTime: null
      },
      // 查询参数配置对象
      queryParamsConfig: {
        status: Condition.like(),
        lastTime: Condition.like(),
      },

      // 表单参数
      form: {},
      // 表单校验
      rules: {
        ip: [
          { required: true, message: "ip不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "1:运行 -1:停止 不能为空", trigger: "blur" }
        ],
        lastTime: [
          { required: true, message: "最后心跳时间不能为空", trigger: "blur" }
        ]
      }
});


const { queryParams, queryParamsConfig, form, rules } = toRefs(data);



/** 查询web服务运行ip列表 */
function getList() {
    searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
    loading.value = true;
    listIpStatus(searchParams.value).then(response => {
      IpStatusList.value = response.rows;
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
    ip: null,
    status: null,
    lastTime: null
  };
  proxy.resetForm("IpStatusRef");
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
  title.value = "添加web服务运行ip";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const ip = row.id || ids.value
  getIpStatus(ip).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改web服务运行ip";
  });
}

/** 提交按钮 */
function submitForm() {
  this.$refs["IpStatusRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateIpStatus(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addIpStatus(form.value).then(response => {
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
  const ips = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除web服务运行ip编号为"' + ips + '"的数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return delIpStatus(ips);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
}

/** 状态修改 **/
function handleStatusChange(row) {
  return changeStatusIpStatus(row.id, row.status).then(response=>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  });
}

/** 修改字段状态 **/
function chanageFile(value , filed , id){
  return changeIpStatusField(value , filed , id).then(response =>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  }) ;
}

/** 导出按钮操作 */
function handleExport() {
  const queryParams = queryParams.value;
  proxy.$modal.confirm('是否确认导出所有web服务运行ip数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return exportIpStatus(queryParams);
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
