<template>
  <div class="app-container">
     <el-row :gutter="20">
        <!--应用数据-->
        <el-col :span="24" :xs="24">
           <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
              <el-form-item label="应用名称" prop="ApplicationName">
                 <el-input
                    v-model="queryParams.ApplicationName"
                    placeholder="请输入应用名称"
                    clearable
                    style="width: 240px"
                    @keyup.enter="handleQuery"
                 />
              </el-form-item>
              <el-form-item label="状态" prop="status">
                 <el-select
                    v-model="queryParams.status"
                    placeholder="应用状态"
                    clearable
                    style="width: 240px"
                 >
                    <el-option
                       v-for="dict in sys_normal_disable"
                       :key="dict.value"
                       :label="dict.label"
                       :value="dict.value"
                    />
                 </el-select>
              </el-form-item>
               
              <el-form-item>
                 <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                 <el-button icon="Refresh" @click="resetQuery">重置</el-button>
              </el-form-item>
           </el-form>

           <el-row :gutter="10" class="mb8">
              <el-col :span="1.5">
                 <el-button
                    type="primary"
                    plain
                    icon="Plus"
                    @click="handleAdd"
                 >新增</el-button>
              </el-col>
              <el-col :span="1.5">
                 <el-button
                    type="success"
                    plain
                    icon="Edit"
                    :disabled="single"
                    @click="handleUpdate"
                    v-hasPermi="['system:Application:edit']"
                 >修改</el-button>
              </el-col>
              <el-col :span="1.5">
                 <el-button
                    type="danger"
                    plain
                    icon="Delete"
                    :disabled="multiple"
                    @click="handleDelete"
                    v-hasPermi="['system:Application:remove']"
                 >删除</el-button>
              </el-col>
             
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
           </el-row>

           <el-table v-loading="loading" :data="ApplicationList" @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="50" align="center" />
              <el-table-column label="图标" align="center" width="55px" prop="icon">
                <template slot-scope="scope">
                  <el-image
                    style="width: 35px;height: 35px;top: 5px;"
                    :src="showImg(scope.row.icon)"
                    fit="scale-down"
                    align="center"
                  >
                  </el-image>
                </template>
              </el-table-column> 
              <el-table-column label="应用名称" align="center" key="ApplicationName" prop="ApplicationName" v-if="columns[1].visible" :show-overflow-tooltip="true" />
              <el-table-column label="工程名称" align="center" key="nickName" prop="nickName" v-if="columns[2].visible" :show-overflow-tooltip="true" />
              <el-table-column label="应用地址" align="center" key="deptName" prop="dept.deptName" v-if="columns[3].visible" :show-overflow-tooltip="true" />
              <el-table-column label="生成" align="center" key="phonenumber" prop="phonenumber" v-if="columns[4].visible" width="120" />
               
              <el-table-column label="发布" align="center" key="phonenumber" prop="phonenumber" width="120" />
              <el-table-column label="功能" align="center" key="phonenumber" prop="phonenumber" width="120" />
              <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[6].visible" width="160">
                 <template #default="scope">
                    <span>{{ parseTime(scope.row.createTime) }}</span>
                 </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
                 <template #default="scope">
                    <el-tooltip content="修改" placement="top" v-if="scope.row.ApplicationId !== 1">
                       <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:Application:edit']"></el-button>
                    </el-tooltip>
                    <el-tooltip content="删除" placement="top" v-if="scope.row.ApplicationId !== 1">
                       <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:Application:remove']"></el-button>
                    </el-tooltip>
                    <el-tooltip content="重置密码" placement="top" v-if="scope.row.ApplicationId !== 1">
                        <el-button link type="primary" icon="Key" @click="handleResetPwd(scope.row)" v-hasPermi="['system:Application:resetPwd']"></el-button>
                    </el-tooltip>
                    <el-tooltip content="分配角色" placement="top" v-if="scope.row.ApplicationId !== 1">
                       <el-button link type="primary" icon="CircleCheck" @click="handleAuthRole(scope.row)" v-hasPermi="['system:Application:edit']"></el-button>
                    </el-tooltip>
                 </template>
              </el-table-column>
           </el-table>
           <pagination
              v-show="total > 0"
              :total="total"
              v-model:page="queryParams.pageNum"
              v-model:limit="queryParams.pageSize"
              @pagination="getList"
           />
        </el-col>
     </el-row>

     <!-- 添加或修改应用配置对话框 -->
     <el-dialog :title="title" v-model="open" width="900px" append-to-body>
        <el-form :model="form" :rules="rules" ref="ApplicationRef" label-width="80px">
           <el-row>
              <el-col :span="12">
                 <el-form-item label="应用昵称" prop="nickName">
                    <el-input v-model="form.nickName" placeholder="请输入应用昵称" maxlength="30" />
                 </el-form-item>
              </el-col>
              <el-col :span="12">
                 <el-form-item label="归属部门" prop="deptId">
                    <el-tree-select
                       v-model="form.deptId"
                       :data="deptOptions"
                       :props="{ value: 'id', label: 'label', children: 'children' }"
                       value-key="id"
                       placeholder="请选择归属部门"
                       check-strictly
                    />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="12">
                 <el-form-item label="手机号码" prop="phonenumber">
                    <el-input v-model="form.phonenumber" placeholder="请输入手机号码" maxlength="11" />
                 </el-form-item>
              </el-col>
              <el-col :span="12">
                 <el-form-item label="邮箱" prop="email">
                    <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="12">
                 <el-form-item v-if="form.ApplicationId == undefined" label="应用名称" prop="ApplicationName">
                    <el-input v-model="form.ApplicationName" placeholder="请输入应用名称" maxlength="30" />
                 </el-form-item>
              </el-col>
              <el-col :span="12">
                 <el-form-item v-if="form.ApplicationId == undefined" label="应用密码" prop="password">
                    <el-input v-model="form.password" placeholder="请输入应用密码" type="password" maxlength="20" show-password />
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="12">
                 <el-form-item label="应用性别">
                    <el-select v-model="form.sex" placeholder="请选择">
                       <el-option
                          v-for="dict in sys_Application_sex"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                       ></el-option>
                    </el-select>
                 </el-form-item>
              </el-col>
              <el-col :span="12">
                 <el-form-item label="状态">
                    <el-radio-group v-model="form.status">
                       <el-radio
                          v-for="dict in sys_normal_disable"
                          :key="dict.value"
                          :label="dict.value"
                       >{{ dict.label }}</el-radio>
                    </el-radio-group>
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="12">
                 <el-form-item label="岗位">
                    <el-select v-model="form.postIds" multiple placeholder="请选择">
                       <el-option
                          v-for="item in postOptions"
                          :key="item.postId"
                          :label="item.postName"
                          :value="item.postId"
                          :disabled="item.status == 1"
                       ></el-option>
                    </el-select>
                 </el-form-item>
              </el-col>
              <el-col :span="12">
                 <el-form-item label="角色">
                    <el-select v-model="form.roleIds" multiple placeholder="请选择">
                       <el-option
                          v-for="item in roleOptions"
                          :key="item.roleId"
                          :label="item.roleName"
                          :value="item.roleId"
                          :disabled="item.status == 1"
                       ></el-option>
                    </el-select>
                 </el-form-item>
              </el-col>
           </el-row>
           <el-row>
              <el-col :span="24">
                 <el-form-item label="备注">
                    <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
                 </el-form-item>
              </el-col>
           </el-row>
        </el-form>
        <template #footer>
           <div class="dialog-footer">
              <el-button type="primary" @click="submitForm">确 定</el-button>
              <el-button @click="cancel">取 消</el-button>
           </div>
        </template>
     </el-dialog>

     <!-- 应用导入对话框 -->
     <el-dialog :title="upload.title" v-model="upload.open" width="400px" append-to-body>
        <el-upload
           ref="uploadRef"
           :limit="1"
           accept=".xlsx, .xls"
           :headers="upload.headers"
           :action="upload.url + '?updateSupport=' + upload.updateSupport"
           :disabled="upload.isUploading"
           :on-progress="handleFileUploadProgress"
           :on-success="handleFileSuccess"
           :auto-upload="false"
           drag
        >
           <el-icon class="el-icon--upload"><upload-filled /></el-icon>
           <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
           <template #tip>
              <div class="el-upload__tip text-center">
                 <div class="el-upload__tip">
                    <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的应用数据
                 </div>
                 <span>仅允许导入xls、xlsx格式文件。</span>
                 <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
              </div>
           </template>
        </el-upload>
        <template #footer>
           <div class="dialog-footer">
              <el-button type="primary" @click="submitFileForm">确 定</el-button>
              <el-button @click="upload.open = false">取 消</el-button>
           </div>
        </template>
     </el-dialog>
  </div>
</template>

<script setup name="Application">
import { getToken } from "@/utils/auth";
import { 
   changeApplicationStatus, 
   listApplication, 
   resetApplicationPwd, 
   delApplication, 
   getApplication, 
   updateApplication, 
   addApplication, 
   deptTreeSelect } from "@/api/flink/Application";

const router = useRouter();
const { proxy } = getCurrentInstance();
// const { sys_normal_disable, sys_Application_sex } = proxy.useDict("sys_normal_disable", "sys_Application_sex");

const ApplicationList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const deptName = ref("");
const deptOptions = ref(undefined);
const initPassword = ref(undefined);
const postOptions = ref([]);
const roleOptions = ref([]);
/*** 应用导入参数 */
const upload = reactive({
 // 是否显示弹出层（应用导入）
 open: false,
 // 弹出层标题（应用导入）
 title: "",
 // 是否禁用上传
 isUploading: false,
 // 是否更新已经存在的应用数据
 updateSupport: 0,
 // 设置上传的请求头部
 headers: { Authorization: "Bearer " + getToken() },
 // 上传的地址
 url: import.meta.env.VITE_APP_BASE_API + "/system/Application/importData"
});
// 列显隐信息
const columns = ref([
 { key: 0, label: `应用编号`, visible: true },
 { key: 1, label: `应用名称`, visible: true },
 { key: 2, label: `应用昵称`, visible: true },
 { key: 3, label: `部门`, visible: true },
 { key: 4, label: `手机号码`, visible: true },
 { key: 5, label: `状态`, visible: true },
 { key: 6, label: `创建时间`, visible: true }
]);

const data = reactive({
 form: {},
 queryParams: {
   pageNum: 1,
   pageSize: 10,
   ApplicationName: undefined,
   phonenumber: undefined,
   status: undefined,
   deptId: undefined
 },
 rules: {
   ApplicationName: [{ required: true, message: "应用名称不能为空", trigger: "blur" }, { min: 2, max: 20, message: "应用名称长度必须介于 2 和 20 之间", trigger: "blur" }],
   nickName: [{ required: true, message: "应用昵称不能为空", trigger: "blur" }],
   password: [{ required: true, message: "应用密码不能为空", trigger: "blur" }, { min: 5, max: 20, message: "应用密码长度必须介于 5 和 20 之间", trigger: "blur" }],
   email: [{ type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
   phonenumber: [{ pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }]
 }
});

const { queryParams, form, rules } = toRefs(data);

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
 if (!value) return true;
 return data.label.indexOf(value) !== -1;
};
/** 根据名称筛选部门树 */
watch(deptName, val => {
 proxy.$refs["deptTreeRef"].filter(val);
});
/** 查询部门下拉树结构 */
function getDeptTree() {
 deptTreeSelect().then(response => {
   deptOptions.value = response.data;
 });
};
/** 查询应用列表 */
function getList() {
 loading.value = true;
 listApplication(proxy.addDateRange(queryParams.value, dateRange.value)).then(res => {
   loading.value = false;
   ApplicationList.value = res.rows;
   total.value = res.total;
 });
};
/** 节点单击事件 */
function handleNodeClick(data) {
 queryParams.value.deptId = data.id;
 handleQuery();
};
/** 搜索按钮操作 */
function handleQuery() {
 queryParams.value.pageNum = 1;
 getList();
};
/** 重置按钮操作 */
function resetQuery() {
 dateRange.value = [];
 proxy.resetForm("queryRef");
 queryParams.value.deptId = undefined;
 proxy.$refs.deptTreeRef.setCurrentKey(null);
 handleQuery();
};
/** 删除按钮操作 */
function handleDelete(row) {
 const ApplicationIds = row.ApplicationId || ids.value;
 proxy.$modal.confirm('是否确认删除应用编号为"' + ApplicationIds + '"的数据项？').then(function () {
   return delApplication(ApplicationIds);
 }).then(() => {
   getList();
   proxy.$modal.msgSuccess("删除成功");
 }).catch(() => {});
};
/** 导出按钮操作 */
function handleExport() {
 proxy.download("system/Application/export", {
   ...queryParams.value,
 },`Application_${new Date().getTime()}.xlsx`);
};
/** 应用状态修改  */
function handleStatusChange(row) {
 let text = row.status === "0" ? "启用" : "停用";
 proxy.$modal.confirm('确认要"' + text + '""' + row.ApplicationName + '"应用吗?').then(function () {
   return changeApplicationStatus(row.ApplicationId, row.status);
 }).then(() => {
   proxy.$modal.msgSuccess(text + "成功");
 }).catch(function () {
   row.status = row.status === "0" ? "1" : "0";
 });
};

  function showImg(imgSrc) {
      console.log("img src = " + imgSrc);
      if (imgSrc) {
        if (imgSrc.indexOf("http") == 0) {
          return imgSrc;
        }
        return require('@/asserts/icons/project/' + imgSrc);
      } else {
        return require('@/asserts/icons/project/12.svg');
      }
    };

/** 更多操作 */
function handleCommand(command, row) {
 switch (command) {
   case "handleResetPwd":
     handleResetPwd(row);
     break;
   case "handleAuthRole":
     handleAuthRole(row);
     break;
   default:
     break;
 }
};
/** 跳转角色分配 */
function handleAuthRole(row) {
 const ApplicationId = row.ApplicationId;
 router.push("/system/Application-auth/role/" + ApplicationId);
};
/** 重置密码按钮操作 */
function handleResetPwd(row) {
 proxy.$prompt('请输入"' + row.ApplicationName + '"的新密码', "提示", {
   confirmButtonText: "确定",
   cancelButtonText: "取消",
   closeOnClickModal: false,
   inputPattern: /^.{5,20}$/,
   inputErrorMessage: "应用密码长度必须介于 5 和 20 之间",
 }).then(({ value }) => {
   resetApplicationPwd(row.ApplicationId, value).then(response => {
     proxy.$modal.msgSuccess("修改成功，新密码是：" + value);
   });
 }).catch(() => {});
};
/** 选择条数  */
function handleSelectionChange(selection) {
 ids.value = selection.map(item => item.ApplicationId);
 single.value = selection.length != 1;
 multiple.value = !selection.length;
};
/** 导入按钮操作 */
function handleImport() {
 upload.title = "应用导入";
 upload.open = true;
};
/** 下载模板操作 */
function importTemplate() {
 proxy.download("system/Application/importTemplate", {
 }, `Application_template_${new Date().getTime()}.xlsx`);
};
/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
 upload.isUploading = true;
};
/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
 upload.open = false;
 upload.isUploading = false;
 proxy.$refs["uploadRef"].handleRemove(file);
 proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
 getList();
};
/** 提交上传文件 */
function submitFileForm() {
 proxy.$refs["uploadRef"].submit();
};
/** 重置操作表单 */
function reset() {
 form.value = {
   ApplicationId: undefined,
   deptId: undefined,
   ApplicationName: undefined,
   nickName: undefined,
   password: undefined,
   phonenumber: undefined,
   email: undefined,
   sex: undefined,
   status: "0",
   remark: undefined,
   postIds: [],
   roleIds: []
 };
 proxy.resetForm("ApplicationRef");
};
/** 取消按钮 */
function cancel() {
 open.value = false;
 reset();
};
/** 新增按钮操作 */
function handleAdd() {
 reset();
 open.value = true;

//  getApplication().then(response => {
//    postOptions.value = response.posts;
//    roleOptions.value = response.roles;
//    open.value = true;
//    title.value = "添加应用";
//    form.value.password = initPassword.value;
//  });

};
/** 修改按钮操作 */
function handleUpdate(row) {
 reset();
 const ApplicationId = row.ApplicationId || ids.value;
 getApplication(ApplicationId).then(response => {
   form.value = response.data;
   postOptions.value = response.posts;
   roleOptions.value = response.roles;
   form.value.postIds = response.postIds;
   form.value.roleIds = response.roleIds;
   open.value = true;
   title.value = "修改应用";
   form.password = "";
 });
};
/** 提交按钮 */
function submitForm() {
 proxy.$refs["ApplicationRef"].validate(valid => {
   if (valid) {
     if (form.value.ApplicationId != undefined) {
       updateApplication(form.value).then(response => {
         proxy.$modal.msgSuccess("修改成功");
         open.value = false;
         getList();
       });
     } else {
       addApplication(form.value).then(response => {
         proxy.$modal.msgSuccess("新增成功");
         open.value = false;
         getList();
       });
     }
   }
 });
};

// getDeptTree();
getList();
</script>
