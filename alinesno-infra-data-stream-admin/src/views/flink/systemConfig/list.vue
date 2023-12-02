<template>
  <!--
    系统配置 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
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
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
    </el-row>

    <el-table v-loading="loading" :data="SystemConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="配置项" align="left" prop="sysDesc"  width="250" />
      <el-table-column label="配置值" align="left" prop="sysVal" />
      <el-table-column label="修改时间" align="center" prop="updateTime" width="170">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            size="mini"
            type="text"
            icon="Edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
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

    <!-- 添加或修改系统配置对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body>
      <el-form ref="systemConfigRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="配置项" prop="sysDesc">
          <el-input v-model="form.sysDesc" placeholder="请输入配置项" readonly />
        </el-form-item>
        <el-form-item label="配置值" prop="sysVal" v-if="form.orderId == 3">
          <el-radio-group v-model="form.sysVal" class="myradiogroup"   @change="$forceUpdate()">
            <el-radio :label="'是'">是</el-radio>
            <el-radio :label="'否'">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="配置值" prop="sysVal" v-if="form.orderId != 3">
          <el-input v-model="form.sysVal"  :placeholder=EditPlaceholder />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup  name="SystemConfig">
import {
  listSystemConfig,
  getSystemConfig,
  delSystemConfig,
  addSystemConfig,
  changeSystemConfigField,
  changeStatusSystemConfig,
  updateSystemConfig,
  exportSystemConfig } from "@/api/flink/SystemConfig";
import  Condition  from "@/api/Search/Condition";
import  searchParam  from "@/api/Search/searchform";
import {parseTime} from "@/utils/ruoyi";

const { proxy } = getCurrentInstance();

// 遮罩层
const loading = ref(true);

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

// 系统配置表格数据
const SystemConfigList = ref([]);

// 弹出层标题
const title = ref("");

// 是否显示弹出层
const open = ref(false);

// 搜索参数
const searchParams = ref([]);


const EditPlaceholder = ref("请输入配置项值");


const data = reactive({
       // 表单参数
       form: {},

       // 查询参数
       queryParams: {
          pageNum: 1,
          pageSize: 10,
          sysKey: null,
          sysVal: null,
          sysType: null,
          sysDesc: null,
          orderId: null
        },

        // 查询参数配置对象
        queryParamsConfig: {
          sysKey: Condition.like(),
          sysVal: Condition.like(),
          sysType: Condition.like(),
          sysDesc: Condition.like()
        },

        // 表单校验
        rules: {
          sysKey: [
            { required: true, message: "配置项描述，key值不能为空", trigger: "blur" }
          ],
          sysVal: [
            { required: true, message: "配置项值不能为空", trigger: "blur" }
          ],
          sysType: [
            { required: true, message: "类型 如:sys  alarm不能为空", trigger: "blur" }
          ],
          sysDesc: [
            { required: true, message: "配置项描述不能为空", trigger: "blur" }
          ]
        }
     });

const {form, queryParams, queryParamsConfig,  rules } = toRefs(data);


/** 查询系统配置列表 */
function getList() {
      searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
      loading.value = true;
      listSystemConfig(searchParams.value).then(response => {
        SystemConfigList.value = response.rows;
        total.value = response.total;
        loading.value = false;
      });
}

/** 取消按钮 */
function cancel() {
      open.value = false;
      reset();
}

/** 表单重置 */
function reset() {
      form.value = {
        id: null,
        sysKey: null,
        sysVal: null,
        sysType: null,
        sysDesc: null,
        orderId: null
      };
  proxy.resetForm("systemConfigRef");
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

/** 多选框选中数据 */
function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.id) ;
    single.value = selection.length!==1 ;
    multiple.value = !selection.length ;
}

/** 新增按钮操作 */
function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加系统配置";
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const key = row.id || ids.value ;
    getSystemConfig(key).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改系统配置";

      if ( form.value.orderId == 1 ) {
        EditPlaceholder.value = "请输入flink安装目录"
      } else if ( form.value.orderId == 2 ) {
        EditPlaceholder.value = "请输入数据实时计算安装目录"
      } else if ( form.value.orderId == 3 ) {
        EditPlaceholder.value = "请选择是否开启保存点,默认开启"
      } else if ( form.value.orderId == 4 ) {
        EditPlaceholder.value = "提示:yarn-per-job 和 yarn-application模式必选 可选主备多个地址用;分隔 默认第一个是主,如："
      } else if ( form.value.orderId == 5 ) {
        EditPlaceholder.value = "提示:Flink Rest & web frontend 地址(Local Cluster模式),如："
      } else if ( form.value.orderId == 6 ) {
        EditPlaceholder.value = "提示:Flink Rest & web frontend HA 地址(Standalone Cluster模式支持HA，可以填写多个地址用;分隔),如："
      } else if ( form.value.orderId == 7 ) {
        EditPlaceholder.value = "钉钉告警webhook,如："
      } else if ( form.value.orderId == 8 ) {
        EditPlaceholder.value = "自定义回调webhook,如："
      }

    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs["systemConfigRef"].validate(valid => {
        if ( valid ) {
          if ( form.value.id != null ) {
            updateSystemConfig(form.value).then(response => {
              proxy.$modal.msgSuccess("修改成功");
              open.value = false;
              getList();
            });
          } else {
            addSystemConfig(form.value).then(response => {
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
    const keys = row.id || ids.value;
    proxy.$modal.$confirm('是否确认删除系统配置编号为"' + keys + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delSystemConfig(keys);
      }).then(() => {
        getList();
        proxy.$modal.msgSuccess("删除成功");
      }).catch(() => {});
}

/** 状态修改 **/
function handleStatusChange(row) {
      return changeStatusSystemConfig(row.id, row.status).then(response=>{
        if( response.code == 200 ){
          proxy.$modal.msgSuccess("操作成功");
        }
      });
}

/** 修改字段状态 **/
function chanageFile(value , filed , id){
      return changeSystemConfigField(value , filed , id).then(response =>{
        if(response.code == 200){
          proxy.$modal.msgSuccess("操作成功");
        }
      }) ;
}


/** 导出按钮操作 */
function handleExport() {
      const queryParams = queryParams.value;
      proxy.$modal.$confirm('是否确认导出所有系统配置数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportSystemConfig(queryParams);
        }).then(response => {
           proxy.download(response.msg);
        })
}

/** created */

getList();
// 查询公共状态
// proxy.getDicts("has_status").then(response => {
//   statusOptions.value = response.data;
// });

/** created */

</script>
