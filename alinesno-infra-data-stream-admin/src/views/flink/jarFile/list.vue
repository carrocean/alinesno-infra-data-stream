<template>
  <!--
    上传文件管理 功能列表
    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px"  @submit.native.prevent>
      <el-form-item label="文件名字" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入文件名字"
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
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Upload"
          size="mini"
          @click="handleUpload"
        >上传JAR文件</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="UploadFileList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="fileName" :show-overflow-tooltip="true" label="文件名" align="left" />
      <el-table-column prop="downloadJarHttp" :show-overflow-tooltip="true" label="http地址" align="left" />
      <el-table-column prop="createTimeStr" :show-overflow-tooltip="true" label="上传时间"  align="left" width="160"/>
      <el-table-column label="操作" width="250" align="center" class-name="small-padding fixed-width">
        <template  #default="scope">
          <el-button
            size="mini"
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row)" style="margin-right: 5px"
          >删除</el-button>
          <el-link @click.native="copyValue(scope.row.downloadJarHttp)" :underline="false">
            <el-button
              type="text"
              icon="el-icon-chat-line-square"
              size="mini" style="margin-right: 10px" >复制URL
            </el-button>
          </el-link>
          <el-link @click.native="copyValue(scope.row.fileName)" :underline="false">
            <el-button
              type="text"
              icon="el-icon-chat-line-square"
              size="mini" style="margin-right: 10px">复制文件名
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

    <!-- 添加或修改上传文件管理对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body>
      <el-form ref="UploadFileRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件名字" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入文件名字" />
        </el-form-item>
        <el-form-item label="文件路径" prop="filePath">
          <el-input v-model="form.filePath" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="1:jar" prop="type">
          <el-input v-model="form.type" placeholder="请输入1:jar" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 业务模型导入对话框 :file-list="fileList"   accept=".jar" -->
    <el-dialog :title="upload.title" v-model="upload.open" width="300px" append-to-body>
      <el-form>
        <el-form-item>
          <el-upload ref="upFileDialog"
                      action="#"
                     :limit="1"
                     :on-error="onErrorFile"
                     :on-success="onSuccessFile"
                     :http-request="uploadFiles"
                     drag>
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击选择文件</em></div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="uploadClose">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script  setup name="UploadFile">
import {
  listUploadFile,
  getUploadFile,
  delUploadFile,
  addUploadFile,
  changeUploadFileField,
  changeStatusUploadFile,
  updateUploadFile,
  exportUploadFile,uploadFiles } from "@/api/flink/UploadFile";

import  searchParam  from "@/api/Search/searchform";
import  Condition  from "@/api/Search/Condition";

import { ref, reactive } from 'vue';

import Clipboard from 'clipboard';
import {ElMessage} from "element-plus";

const { proxy } = getCurrentInstance();

const { MODE, VITE_APP_BASE_API} = import.meta.env


// 遮罩层
const loading = ref(false);

// 选中id数组
const ids = ref([]);

// 选中名称数组
const names = ref([]);

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

// 上传文件管理表格数据
const UploadFileList = ref([]);

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
    fileName: null,
    filePath: null,
    type: null
  },
  // 查询参数配置对象
  queryParamsConfig: {
    fileName: Condition.like(),
    filePath: Condition.like(),
    type: Condition.like(),
  },

  // 表单参数
  form: {},

  // 表单校验
  rules: {
    fileName: [
      { required: true, message: "文件名字不能为空", trigger: "blur" }
    ],
    filePath: [
      { required: true, message: "文件路径不能为空", trigger: "blur" }
    ],
    type: [
      { required: true, message: "1:jar不能为空", trigger: "blur" }
    ]
  },

  // 用户导入参数
  upload: {
    // 是否显示弹出层
    open: false,
    // 是否显示替换提示
    replace: false,
    // 弹出层标题
    title: "上传jar包",
    // 是否禁用上传
    isUploading: false,
    // 是否更新已经存在的用户数据
    updateSupport: 0,
    // 设置上传的请求头部
    // headers: { Authorization: "Bearer " + getToken() },
    //url: process.env.VUE_APP_BASE_API + "/api/alinesno-cloud-data-flink/UploadFile/upload"   // 文件上传地址接口
  }

});

const { queryParams, queryParamsConfig, form, rules, upload } = toRefs(data);


/** 查询上传文件管理列表 */
function getList() {
    searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
    loading.value = true;
    listUploadFile(searchParams.value).then(response => {
      UploadFileList.value = response.rows;
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
    fileName: null,
    filePath: null,
    type: null
  };
  proxy.resetForm("UploadFileRef");
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
  names.value = selection.map(item => item.fileName + ' ') ;
  single.value = selection.length!==1 ;
  multiple.value = !selection.length ;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加上传文件管理";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const fileName = row.id || ids.value
  getUploadFile(fileName).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改上传文件管理";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["UploadFileRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateUploadFile(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addUploadFile(form.value).then(response => {
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
  const fileIds = row.id || ids.value;
  let fileNames = row.fileName || names.value;
  //避免弹出窗数据太长，只显示前15条数据
  if ( fileNames.length > 15 ) {
    fileNames = fileNames.slice(0,15);
  }

  proxy.$modal.confirm('是否确认删除上传文件名称为"' + fileNames + '"的数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return delUploadFile(fileIds);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(error => {

    })
}

/** 状态修改 **/
function handleStatusChange(row) {
  return changeStatusUploadFile(row.id, row.status).then(response=>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  });
}

/** 修改字段状态 **/
function chanageFile(value , filed , id){
  return changeUploadFileField(value , filed , id).then(response =>{
    if(response.code == 200){
      proxy.$modal.msgSuccess("操作成功");
    }
  }) ;
}

/** 导出按钮操作 */
function handleExport() {
  const queryParams = queryParams.value;
  proxy.$modal.confirm('是否确认导出所有上传文件管理数据项?', "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(function() {
      return exportUploadFile(queryParams);
    }).then(response => {
       proxy.download(response.msg);
    })
}

function handleUpload(row){
  upload.value.isUploading = false;
  upload.value.open = true;
}


function copyValue(value) {
  const textarea = document.createElement('textarea');
  textarea.value = value;
  document.body.appendChild(textarea);
  textarea.select();
  document.execCommand('copy');
  document.body.removeChild(textarea);
  proxy.$modal.$message.success('复制成功');
}



// 文件上传失败钩子
function onErrorFile(){
  ElMessage.error('文件上传失败')
  proxy.$refs.upFileDialog.clearFiles(); //去掉文件列表
}

// 文件上传成功钩子
function onSuccessFile(){
  ElMessage.success('文件上传成功')
  proxy.$refs.upFileDialog.clearFiles(); //去掉文件列表
}

function uploadClose(){
  upload.value.open = false ;
  handleQuery();
}


/** created */

getList();
// 查询公共状态
// getDicts("has_status").then(response => {
//   statusOptions.value = response.data;
// });

/** created */


</script>
