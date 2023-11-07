<template>
  <!--
    flink任务配置 功能列表

    @author luoxiaodong
    @since 1.0.0
  -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="jobName">
        <el-input
          v-model="queryParams.jobName"
          placeholder="请输入任务名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="开启" prop="isOpen">
        <el-select  v-model="queryParams.isOpen" placeholder="请选择开启"  clearable size="small" filterable  @keyup.enter.native="handleQuery">
          <el-option
            v-for="item in isOpenOptions"
            :key="item.id"
            :label="item.label"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="任务类型" prop="jobType">
        <el-select  v-model="queryParams.jobType" placeholder="请选择任务类型"  clearable size="small" filterable  @keyup.enter.native="handleQuery">
          <el-option
            v-for="item in jobTypeOptions"
            :key="item.id"
            :label="item.label"
            :value="item.id">
          </el-option>
        </el-select>
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
            @click="handleSqlSteam"
        >SQL流任务</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <router-link :to="{name:getRouteTaskName('create','SQL_BATCH'), params:{flag:'create', context: queryContent(), data:{jobTypeEnum:'SQL_BATCH'}}}" class="wl-append">-->
<!--          <el-button-->
<!--            type="primary"-->
<!--            plain-->
<!--            icon="el-icon-plus"-->
<!--            size="mini"-->
<!--          >SQL批任务</el-button>-->
<!--        </router-link>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleJAR"
        >JAR任务</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="JobConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务类型" align="left" prop="jobType"  width="100">
        <template  #default="scope">
          <span>{{ jobTypeTrans(scope.row.jobType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务名称" align="left" prop="jobName" :show-overflow-tooltip="true" />
      <el-table-column label="运行模式"  :show-overflow-tooltip="true"  align="left" prop="deployModeEnum" width="160"/>
      <el-table-column label="Flink任务Id"  :show-overflow-tooltip="true"  align="left" prop="jobId"  >
        <template  #default="scope">
          <el-link v-if="scope.row.jobId" :href="scope.row.flinkRunUrl" target="_blank" :underline="false">{{ scope.row.jobId }}</el-link>
        </template>
      </el-table-column>
<!--      <el-table-column prop="cron" :show-overflow-tooltip="true" label="调度时间" min-width="90" align="center">-->
<!--        <template  #default="scope">-->
<!--          <span>{{ scope.row.cron }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="报警模式" :show-overflow-tooltip="true" width="90" align="left">
        <template  #default="scope">
          <el-link v-if="scope.row.alarmStrs" :underline="false">{{ scope.row.alarmStrs }}</el-link><!-- 辅助 -->
        </template>
      </el-table-column>
      <el-table-column label="开启" align="left" prop="isOpen" width="70px" >
        <template  #default="scope">
          <el-switch v-model="scope.row.isOpen" :active-value="1" :inactive-value="0" active-color="#005CD4" @change="changeIsOpen($event, scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="状态" width="70px" align="left" prop="status" >
        <template  #default="scope">
          <el-tag v-if="scope.row.status===-2||scope.row.status==='UNKNOWN'" type="info" size="mini">{{ getStatusDesc(scope.row.status) }}</el-tag>
          <el-tag v-else-if="scope.row.status===-1||scope.row.status==='FAIL'" type="danger" size="mini">{{ getStatusDesc(scope.row.status) }}</el-tag>
          <el-tag v-else-if="scope.row.status===0||scope.row.status==='STOP'" type="warning" size="mini">{{ getStatusDesc(scope.row.status) }}</el-tag>
          <el-tag v-else-if="scope.row.status===1||scope.row.status==='RUN'" type="success" size="mini">{{ getStatusDesc(scope.row.status) }}</el-tag>
          <el-tag v-else-if="scope.row.status===2||scope.row.status==='STARTING'" size="mini">{{ getStatusDesc(scope.row.status) }}</el-tag>
          <el-tag v-else-if="scope.row.status===3||scope.row.status==='SUCCESS'" type="success" size="mini">{{ getStatusDesc(scope.row.status) }}</el-tag>
          <el-tag v-else type="info" size="mini">{{ getStatusDesc(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="日志" width="110" >
        <template  #default="scope">
          <router-link v-if="scope.row.lastRunLogId!==null" :to="{name:'ViewTaskLogDetail', params:{flag:'tasklist', context:queryContent(), data:{id:scope.row.lastRunLogId}}}" style="margin-right: 5px"  >
            <el-link :underline="false">
              <el-button
                type="text"
                icon="el-icon-message"
                size="mini">详情
              </el-button>
            </el-link>
          </router-link>
          <router-link :to="{name:'JobRunLog', params:{flag:'tasklist', context:queryContent(), jobId:scope.row.jobId, jobConfigId:scope.row.id}}" style="margin-right: 5px"  >
            <el-link :underline="false">
              <el-button
                type="text"
                icon="el-icon-chat-line-square"
                size="mini">历史
              </el-button>
            </el-link>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="savepoint" label="保存状态" width="150" >
        <template  #default="scope">
          <el-link v-if="scope.row.jobTypeEnum==='SQL_STREAMING'" type="primary"  @click.native="doRecoverSavePoint(scope.row)" style="margin-right: 5px" :underline="false">
            <el-button
              type="text"
              icon="el-icon-sell"
              size="mini">恢复
            </el-button>
            </el-link>
          <el-link v-if="scope.row.jobTypeEnum==='SQL_STREAMING'" type="success" icon="el-icon-sold-out" @click.native="savePoint(scope.row)" style="margin-right: 5px" :underline="false">
            <el-button
              type="text"
              icon="el-icon-sell"
              size="mini">备份
            </el-button>
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="添加时间"   prop="createTime" width="160">
        <template  #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="版本信息" width="80" align="center">
        <template  #default="scope">
          <router-link :to="{name:'JobHistory', params:{flag:'tasklist', context:queryContent(), jobConfigId:scope.row.id}}">
            <el-link type="info" class="fl-version-text" :underline="false"><span class="fl-version-span">[{{ scope.row.version }}]</span>{{ scope.row.jobDesc }}</el-link>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="275" align="center" class-name="small-padding fixed-width">
        <template  #default="scope">
          <el-link v-if="scope.row.isOpen===1&&scope.row.status!=='RUN'"  @click.native="startTask(scope.row)" style="margin-right: 2px" :underline="false">
            <el-button
              type="text"
              icon="el-icon-video-play"
              size="mini">启动
            </el-button>
          </el-link>

          <el-link v-if="scope.row.isOpen===1&&scope.row.status==='RUN'"  @click.native="stopTask(scope.row)" style="margin-right: 5px" :underline="false">
            <el-button
              type="text"
              icon="el-icon-switch-button"
              size="mini">停止
            </el-button>
          </el-link>

          <router-link :to="{name:getRouteTaskName('view',scope.row.jobTypeEnum), params:{flag:'view', context:queryContent(), data:getRowData(scope.row) }}" style="margin-right: 2px">
            <el-link :underline="false">
              <el-button
                type="text"
                icon="el-icon-view"
                size="mini">查看
              </el-button>
            </el-link>
          </router-link>

          <router-link v-if="scope.row.isOpen===0" :to="{name:getRouteTaskName('update',scope.row.jobTypeEnum), params:{flag:'update', context:queryContent(), data:getRowData(scope.row) }}" style="margin-right: 2px">
            <el-link :underline="false">
              <el-button
                type="text"
                icon="el-icon-edit-outline"
                size="mini">修改
              </el-button>
            </el-link>
          </router-link>

          <el-link @click.native="copyConfig(scope.row.id)" style="margin-right: 2px" :underline="false">
            <el-button
              type="text"
              icon="el-icon-document-copy"
              size="mini">复制
            </el-button>
          </el-link>

          <el-link v-if="scope.row.isOpen===0"  @click.native="handleDelete(scope.row)" style="margin-right: 2px" :underline="false">
            <el-button
              type="text"
              icon="el-icon-delete"
              size="mini">删除
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

    <!-- 恢复任务弹出窗口-->
    <recover-save-point ref="recoverSavePoint" />

  </div>
</template>

<script setup name="JobManage">
import {
  listJobConfig,
  getJobConfig,
  delJobConfig,
  addConfig,
  changeJobConfigField,
  changeStatusJobConfig,
  editConfig,
  exportJobConfig, openTask, closeTask, startTask, stopTask, startSavepoint, copyConfig, savePoint
} from "@/api/flink/JobConfig";
import  searchParam  from "@/api/Search/searchform";
import  Condition  from "@/api/Search/Condition";
import { ref, reactive, onMounted} from 'vue';
import { useRouter, useRoute } from 'vue-router';
import {parseTime} from "@/utils/ruoyi";
import RecoverSavePoint from '@/views/flink/components/recover'
const router=useRouter() ;
const route=useRoute()   ;

const { proxy } = getCurrentInstance();

// 遮罩层
const loading = ref(true);

// 选中数组
const ids = ref([]);

// 选中名称数组
const jobNames = ref([]);

// 非单个禁用
const single = ref(true);

// 非多个禁用
const multiple = ref(true);

// 状态
const statusOptions = ref([]);

// 显示搜索条件
const showSearch = ref(true);

// flink任务配置表格数据
const JobConfigList = ref([]);

// 总条数
const total = ref(0);

// 弹出层标题
const title = ref("");

// 是否显示弹出层
const open = ref(false);

// 搜索参数
const searchParams = ref([]);

//是否开启
const isOpenOptions = ref([
     {id: 0, label: "关闭"},
     {id: 1, label: "开启"}
]);

//任务类型
const jobTypeOptions = ref([
    {id: 0, label: "SQL流任务"},
    {id: 1, label: "JAR包"}
]);

const data = reactive({

  // 表单参数
  form: {},

  // 查询参数
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    jobName: null,
    jobDesc: null,
    deployMode: null,
    flinkRunConfig: null,
    flinkSql: null,
    flinkCheckpointConfig: null,
    jobId: null,
    isOpen: null,
    status: null,
    cron: null,
    extJarPath: null,
    lastStartTime: null,
    lastRunLogId: null,
    version: null,
    jobType: null,
    customArgs: null,
    customMainClass: null,
    customJarUrl: null,
    hasDelete: 0 ,
  },

  // 查询参数配置对象
  queryParamsConfig: {
    jobName: Condition.like(),
    isOpen: Condition.eq(),
    jobType: Condition.eq(),
    jobDesc: Condition.like(),
    deployMode: Condition.like(),
    flinkRunConfig: Condition.like(),
    flinkSql: Condition.like(),
    flinkCheckpointConfig: Condition.like(),
    jobId: Condition.like(),
    status: Condition.like(),
    cron: Condition.like(),
    extJarPath: Condition.like(),
    lastStartTime: Condition.like(),
    lastRunLogId: Condition.like(),
    version: Condition.like(),
    customArgs: Condition.like(),
    customMainClass: Condition.like(),
    customJarUrl: Condition.like(),
    hasDelete: Condition.eq()
  },

  // 表单校验
  rules: {
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
    jobId: [
      { required: true, message: "运行后的任务id不能为空", trigger: "blur" }
    ],
    isOpen: [
      { required: true, message: "1:开启 0: 关闭不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "1:运行中 0: 停止中 -1:运行失败不能为空", trigger: "blur" }
    ],
    cron: [
      { required: true, message: "批任务定时调度 如 0/20 * * * * ? 表示每20秒 执行任务 不能为空", trigger: "blur" }
    ],
    extJarPath: [
      { required: true, message: "udf地址已经连接器jar 如http://xxx.xxx.com/flink-streaming-udf.jar不能为空", trigger: "blur" }
    ],
    lastStartTime: [
      { required: true, message: "最后一次启动时间不能为空", trigger: "blur" }
    ],
    lastRunLogId: [
      { required: true, message: "最后一次日志不能为空", trigger: "blur" }
    ],
    version: [
      { required: true, message: "更新版本号 用于乐观锁不能为空", trigger: "blur" }
    ],
    jobType: [
      { required: true, message: "任务类型 0:sql 1:自定义jar不能为空", trigger: "blur" }
    ],
    customArgs: [
      { required: true, message: "启动jar可能需要使用的自定义参数不能为空", trigger: "blur" }
    ],
    customMainClass: [
      { required: true, message: "程序入口类不能为空", trigger: "blur" }
    ],
    customJarUrl: [
      { required: true, message: "自定义jar的http地址 如:http://ccblog.cn/xx.jar不能为空", trigger: "blur" }
    ]
  }

});


const {form, queryParams, queryParamsConfig, rules  } = toRefs(data);


onMounted(() => {

})

/** 查询flink任务配置列表 */
function getList() {
    searchParams.value = searchParam(queryParamsConfig.value, queryParams.value);
    loading.value = true;
    listJobConfig(searchParams.value).then(response => {
      JobConfigList.value = response.rows;
      total.value = response.total;
      loading.value= false;
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
        jobName: null,
        jobDesc: null,
        deployMode: null,
        flinkRunConfig: null,
        flinkSql: null,
        flinkCheckpointConfig: null,
        jobId: null,
        isOpen: null,
        status: null,
        cron: null,
        extJarPath: null,
        lastStartTime: null,
        lastRunLogId: null,
        version: null,
        jobType: null,
        customArgs: null,
        customMainClass: null,
        customJarUrl: null
      };
      proxy.resetForm("JobManageRef");
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
      ids.value = selection.map(item => item.id);
      jobNames.value = selection.map(item => item.jobName + ' ');
      single.value = selection.length!==1 ;
      multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
      reset();
      open.value = true;
      title.value = "添加flink任务配置";
}

/** 修改按钮操作 */
function handleUpdate(row) {
      reset();
      const jobName = row.id || ids.value
      getJobConfig(jobName).then(response => {
        form.value = response.data;
        open.value = true;
        title.value = "修改flink任务配置";
      });
}


/** 提交按钮 */
function submitForm() {
        proxy.$refs["form"].validate(valid => {
        if (valid) {
          if (form.value.id != null) {
            editConfig(form.value).then(response => {
              proxy.$modal.msgSuccess("修改成功");
              open.value = false;
              getList();
            });
          } else {
            addConfig(form.value).then(response => {
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
      let jobNameList = row.jobName || jobNames.value;
      //避免弹出窗数据太长，只显示前15条数据
      if ( jobNameList.length > 15 ) {
        jobNameList = jobNameList.slice(0,15);
      }

      proxy.$modal.confirm('是否确认删除任务名称为"' + jobNameList + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delJobConfig(jobIds);
        }).then(() => {
          getList();
          proxy.$modal.msgSuccess("删除成功");
        }).catch(error => {

        })
}

/** 状态修改 **/
function handleStatusChange(row) {
      return changeStatusJobConfig(row.id, row.status).then(response=>{
        if(response.code == 200){
          proxy.$modal.msgSuccess("操作成功");
        }
      });
}

/** 修改字段状态 **/
function chanageFile(value , filed , id){
      return changeJobConfigField(value , filed , id).then(response =>{
        if(response.code == 200){
          proxy.$modal.msgSuccess("操作成功");
        }
      }) ;
}

/** 导出按钮操作 */
function handleExport() {
      const queryParams = queryParams.value;
      proxy.$modal.confirm('是否确认导出所有flink任务配置数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportJobConfig(queryParams);
        }).then(response => {
           proxy.download(response.msg);
        })
}

/** 任务状态 */
function getStatusDesc(status) {
      switch (status) {
        case -2: return '未知'
        case -1: return '失败'
        case 0: return '停止'
        case 1: return '运行中'
        case 2: return '启动中'
        case 3: return '提交成功'
        case 'UNKNOWN': return '未知'
        case 'FAIL': return '失败'
        case 'STOP': return '停止'
        case 'RUN': return '运行中'
        case 'STARTING': return '启动中'
        case 'SUCCESS': return '提交成功'
        default: return ''
      }
}

/**开启或关闭任务 */
 function changeIsOpen(callback, row) {
      const text = (callback === 1) ? '开启' : '关闭'
      const { id, jobName } = row
      proxy.$modal.confirm(`是否${text}[${jobName}]`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (callback === 1) {
          loading.value = true
          openTask(id).then(response => {
            loading.value = false
            const { code, msg, data } = response
            if (code !== 200 ) {
              row.isOpen = (callback === 1) ? 0 : 1
              proxy.$modal.$message({ type: 'error', message: (msg || '请求数据异常！') })
              return
            }
            proxy.$modal.$message({ type: 'success', message: `开启[${jobName}]成功！` })
          }).catch(error => {
            loading.value = false
            row.isOpen = (callback === 1) ? 0 : 1
          })
        } else if (callback === 0) {
          loading.value = true
          closeTask(id).then(response => {
            loading.value = false
            const { code, msg, data } = response
            if (code !== 200 ) {
              row.isOpen = (callback === 1) ? 0 : 1
              proxy.$modal.$message({ type: 'error', message: (msg || '请求数据异常！') })
              return
            }
            proxy.$modal.$message({ type: 'success', message: `关闭[${jobName}]成功！` })
          }).catch(error => {
            loading.value = false
            row.isOpen = (callback === 1) ? 0 : 1
          })
        }
      }).catch(action => {
        row.isOpen = (callback === 1) ? 0 : 1
        // proxy.$modal.$message({ type: 'warning', message: '取消' })
      })
  }

/**    */
 function getRouteTaskName(flag, jobTypeEnum) {
      switch (flag) {
        case 'create':
          switch (jobTypeEnum) {
            case 'SQL_STREAMING': return 'CreateSqlStreamingTask'
            case 'SQL_BATCH': return 'CreateSqlBatchTask'
            case 'JAR': return 'CreateJarTask'
            default: return ''
          }
        case 'update':
          switch (jobTypeEnum) {
            case 'SQL_STREAMING': return 'UpdateSqlStreamingTask'
            case 'SQL_BATCH': return 'UpdateSqlBatchTask'
            case 'JAR': return 'UpdateJarTask'
            default: return ''
          }
        case 'view':
          switch (jobTypeEnum) {
            case 'SQL_STREAMING': return 'ViewSqlStreamingTask'
            case 'SQL_BATCH': return 'ViewSqlBatchTask'
            case 'JAR': return 'ViewJarTask'
            default: return ''
          }
        default: return ''
      }
    }

/**    */
function queryContent() {
      //  经过验证，返回后，暂时无法复原查询条件
      // let content ={
      //   "count": total.value,
      //   "currentPage": queryParams.value.pageNum,
      //   "pageSize": queryParams.value.pageSize,
      //   "jobName": queryParams.value.jobName,
      //   "jobType": queryParams.value.jobType,
      //   "isOpen": queryParams.value.isOpen
      // }
      let content = {} ;
      return  JSON.stringify(content);
}

/** 启动任务 */
function startJobTask(row) {
      this.loading = true
      const { id, jobName } = row
      startTask(id).then(response => {
        this.loading = false
        const { code, msg, data } = response

        if (code !== 200 ) {s
          proxy.$modal.$message({ type: 'error', message: (msg || '请求数据异常！') })
          return
        }
        this.handleQuery()
        proxy.$modal.$message({ type: 'info', message: `正在启动[${jobName}]，稍后请刷新!或到任务日志中查看启动日志` })
      }).catch(error => {
        this.loading = false
      })
}

/** 停止任务 */
function stopJobTask(row) {
      const { id, jobName } = row
      proxy.$modal.confirm(`确定要停止任务【${jobName}】吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        stopTask(id).then(response => {
          this.loading = false
          const { code, msg, data } = response
          if (code !== 200 ) {
            proxy.$modal.$message({ type: 'error', message: (msg || '请求数据异常！') })
            return
          }
          this.handleQuery()
          proxy.$modal.$message({ type: 'success', message: `正在停止任务【${jobName}】，稍后请刷新！` })
        }).catch(error => {
          this.loading = false
          // proxy.$modal.$message({ type: 'error', message: '请求异常！' })
          // console.log(error)
        })
      })
}

/**备份任务状态 */
 function saveJobPoint(row) {
      const { id, jobName } = row
      proxy.$modal.confirm(`确定要手动执行[${jobName}]savePoint吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.loading = true
        savePoint(id).then(response => {
          this.loading = false
          const { code, msg, data } = response
          if (code !== 200 || !success) {
            proxy.$modal.$message({ type: 'error', message: (msg || '请求数据异常！') })
            return
          }
          proxy.$modal.$message({ type: 'success', message: `手动执行[${jobName}]savePoint成功！` })
        }).catch(error => {
          this.loading = false
        })
      })
}

/**复制  */
function copyJobConfig(row) {
      const { id, jobName } = row
      this.loading = true
      copyConfig(id).then(response => {
        this.loading = false
        const { code, msg, data } = response
        if (code !== 200 ) {
          proxy.$modal.$message({ type: 'error', message: (message || '请求数据异常！') })
          return
        }
        this.handleQuery()
        proxy.$modal.$message({ type: 'success', message: `复制[${jobName}]成功！` })
      }).catch(error => {
        this.loading = false
        // proxy.$modal.$message({ type: 'error', message: '请求异常！' })
        // console.log(error)
      })
}

/**从SavePoint中恢复任务  */
function doRecoverSavePoint(row) {
      this.$refs.recoverSavePoint.openDialog(row)
}



function jobTypeTrans(value) {
      if (value === null || value === '') {
        return 'SQL流任务';
      }
      switch (value) {
        case 0:
          return "SQL流任务";
        case 1:
          return "JAR包";
        case 2:
          return "SQL批任务";
        default:
          return "SQL流任务";
      }
}

function queryData(jobType){
  let data ={
    "jobTypeEnum":jobType
  }
  return  JSON.stringify(data);

}

function getRowData(rowData){
  return  JSON.stringify(rowData);
}

function handleSqlSteam(){
  router.push({
    name:getRouteTaskName('create','SQL_STREAMING'),
    params:{
      flag:'create',
      context:queryContent(),
      data:queryData("SQL_STREAMING")
    }
  })
}

function handleJAR(){
  router.push({
    name:getRouteTaskName('create','JAR'),
    params:{
      flag:'create',
      context:queryContent(),
      data:queryData("JAR")
    }
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
