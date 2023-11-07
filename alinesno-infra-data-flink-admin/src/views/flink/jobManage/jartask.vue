<template>
  <div v-loading="loading" :class="'fl-jartask-container fl-task-edit'+(isReadOnly?' fl-task-edit__isRead':'')">
    <el-page-header @back="handleBack()"  :content=title class="back"></el-page-header>
    <el-card shadow="false" class="box-card" style="margin-top: 20px;">
      <el-form ref="taskform" :model="form" :rules="rules" :disabled="isReadOnly" label-width="100px" size="small">
      <el-row v-if="curParams.flag==='history'">
        <el-col :span="10">
          <el-form-item label="版本号" prop="version">
            <el-input v-model="form.version" />
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item label="备份时间" prop="createTime" label-width="100px">
            <el-input :value="formatDateTime(form.createTime)" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="任务编号" prop="id">
            <el-input v-model="form.id" placeholder="任务编号" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="21">
          <el-form-item label="运行配置" prop="flinkRunConfig">
            <el-input v-model="form.flinkRunConfig" placeholder="请输入任务提交所需的资源参数如： -p 2  -Dtaskmanager.numberOfTaskSlots=2 -Dyarn.application.queue=default" maxlength="512" show-word-limit/>
          </el-form-item>
        </el-col>

        <el-col :span="3">
          <el-form-item label="参数说明" label-width="100px">
<!--            <span slot="label"><a href="https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/deployment/config/" style="color: blue" target="_blank">参数说明</a>-->
            <span slot="label">参数说明
              <el-popover placement="right" trigger="hover">
                <template v-if="form.deployModeEnum==='LOCAL'">
                  在LOCAL模式下无需配置
                </template>
                <template v-else-if="form.deployModeEnum==='YARN_PER'">
                  参数如 ： -p 2 -Dyarn.application.queue=default -Dtaskmanager.numberOfTaskSlots=2 -Djobmanager.memory.process.size=1024m  -Dtaskmanager.memory.process.size=2048m
                </template>
                <template v-else-if="form.deployModeEnum==='YARN_APPLICATION'">
                  参数如 ： -p 2 -Dyarn.application.queue=default -Dtaskmanager.numberOfTaskSlots=2 -Djobmanager.memory.process.size=1024m  -Dtaskmanager.memory.process.size=2048m
                </template>
                <template v-else-if="form.deployModeEnum==='STANDALONE'">
                  -d,--detached 如果存在，则以分离模式运行作业<br>
                  -p,--parallelism &lt;parallelism&gt; 运行程序的并行性。用于覆盖配置中指定的默认值的可选标志<br>
                  -s,--fromSavepoint &lt;savepointPath&gt; 从savepointPath中恢复任务(如：-s hdfs:///flink/savepoint-1537)<br>
                  其他运行参数可通过 flink -h查看
                </template>
                <template v-else>
                  请选择运行模式
                </template>
                <i slot="reference" class="el-icon-info" />
              </el-popover>
            </span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item label="任务名称" prop="jobName">
            <el-input v-model="form.jobName" placeholder="请输入任务名称" maxlength="64" show-word-limit/>
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item label="主类名" prop="customMainClass">
            <el-input v-model="form.customMainClass" placeholder="如：com.test.DemoTest" maxlength="128" show-word-limit/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="任务描述" prop="jobDesc">
            <el-input v-model="form.jobDesc" placeholder="请输入任务描述" maxlength="255" show-word-limit/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="21">
          <el-form-item label="Jar包地址" prop="customJarUrl">
            <el-input v-model="form.customJarUrl" placeholder="如：http://ccblog.cn/xx.jar 或者 xxx.jar" maxlength="128" show-word-limit/>
          </el-form-item>
        </el-col>
        <el-col :span="3">
          <el-form-item >
              <span slot="label"> <a href="/flink/flink/jarFile/list" style="color: red" target="_blank">二方库管理</a></span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item label="运行模式" prop="deployModeEnum">
            <el-select v-model="form.deployModeEnum" placeholder="请选择运行模式" class="fl-form-item">
              <el-option label="Local Cluster" value="LOCAL" />
<!--              <el-option label="Standalone Cluster" value="STANDALONE" />-->
<!--              <el-option label="YARN PER" value="YARN_PER" />-->
              <el-option label="YARN APPLICATION" value="YARN_APPLICATION" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item label="运行参数" prop="customArgs">
            <el-input v-model="form.customArgs" placeholder="如：--xxTestParma 100" maxlength="128" show-word-limit/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row class="fl-alarm-row">
        <el-col :xs="16" :sm="16" :md="14" :lg="12">
          <el-form-item label="告警配置" prop="alarmTypes">
            <el-checkbox-group v-model="form.alarmTypes">
              <el-checkbox :label="1">钉钉告警</el-checkbox>
<!--              <el-checkbox :label="2">Http回调告警</el-checkbox>-->
<!--              <el-checkbox :label="3">任务退出自动拉起</el-checkbox>-->
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :xs="4" :sm="4" :md="5" :lg="6">
          <el-form-item label="开启状态">
            <el-switch v-model="form.isOpen" :active-value="1" :inactive-value="0" active-color="#13ce66" disabled />
          </el-form-item>
        </el-col>
        <el-col :xs="4" :sm="4" :md="5" :lg="6">
          <el-form-item label="运行状态">
            <el-tag v-if="form.status===-2||form.status==='UNKNOWN'" type="info" size="mini">{{ getStatusDesc(form.status) }}</el-tag>
            <el-tag v-else-if="form.status===-1||form.status==='FAIL'" type="danger" size="mini">{{ getStatusDesc(form.status) }}</el-tag>
            <el-tag v-else-if="form.status===0||form.status==='STOP'" type="warning" size="mini">{{ getStatusDesc(form.status) }}</el-tag>
            <el-tag v-else-if="form.status===1||form.status==='RUN'" type="success" size="mini">{{ getStatusDesc(form.status) }}</el-tag>
            <el-tag v-else-if="form.status===2||form.status==='STARTING'" size="mini">{{ getStatusDesc(form.status) }}</el-tag>
            <el-tag v-else-if="form.status===3||form.status==='SUCCESS'" type="success" size="mini">{{ getStatusDesc(form.status) }}</el-tag>
            <el-tag v-else type="info" size="mini">{{ getStatusDesc(form.status) }}</el-tag>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row v-if="isReadOnly===false" class="fl-button-row">
        <el-col :span="24">
          <el-form-item label="">
            <el-button type="primary" @click="submitTask()">提 交</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    </el-card>
  </div>
</template>

<script setup name="JarTask">
import { addConfig, editConfig } from '@/api/flink/JobConfig';
import { ref, reactive, onMounted} from 'vue';
import { useRouter, useRoute } from 'vue-router';

const { proxy } = getCurrentInstance();

const loading = ref(false);

const isReadOnly = ref(false);

const title = ref("");

const data = reactive({
  curParams: {
    flag: '', // create,update,view
    data: {},
    context: '' // 父页面传递过来的参加，返回时带给父页面恢复上下文
  },

  form: {
    id: '',
    jobName: '',
    jobDesc: '',
    jobType: 1,
    deployModeEnum: '',
    flinkRunConfig: '',
    flinkSql: '',
    alarmTypes: [],
    customArgs: '',
    customMainClass: '',
    customJarUrl: '',
    isOpen: '',
    status: ''
  },

  rules: {
    jobName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
    jobDesc: [{ required: true, message: '请输入任务描述', trigger: 'blur' }],
    deployModeEnum: [{ required: true, message: '请选择运行模式', trigger: 'blur' }],
    customMainClass: [{ required: true, message: '请输入主类名', trigger: 'blur' }],
    customJarUrl: [{ required: true, message: '请输入Jar包地址', trigger: 'blur' }]
  }
});

const { form, curParams, rules } = toRefs(data);

onMounted(() => {
  //在组件挂载后执行的逻辑
  //获取当前路由
  let route = useRoute();

  //获取当前路由的标题
  title.value = route.meta.title;

  const params = route.params;

  //获取跳转过来时的参数
  console.log(params.flag);
  isReadOnly.value = !(params.flag === 'create' || params.flag === 'update')

  //保存上个界面传进来的标志、查询条件、数据
  curParams.flag = params.flag ;
  curParams.context = params.context ;
  curParams.data    = JSON.parse(params.data) ;

  if ( params.flag === 'update' || params.flag === 'view' || params.flag === 'history' ) {
    const task = curParams.data;
    form.value.id = task.id ? task.id : '';
    form.value.jobName = task.jobName ? task.jobName : '';
    form.value.jobDesc = task.jobDesc ? task.jobDesc : '';
    form.value.jobType = task.jobTypeEnum ? getJobType(task.jobTypeEnum) : 0;
    form.value.deployModeEnum = task.deployModeEnum ? task.deployModeEnum : '';
    form.value.flinkRunConfig = task.flinkRunConfig ? task.flinkRunConfig : '';
    form.value.alarmTypes = task.alarmTypes ? task.alarmTypes : [];
    form.value.customArgs = task.customArgs ? task.customArgs : '';
    form.value.customMainClass = task.customMainClass ? task.customMainClass : '';
    form.value.customJarUrl = task.customJarUrl ? task.customJarUrl : '';
    form.value.isOpen = task.isOpen ? task.isOpen : '';
    form.value.status = task.status ? task.status : '';
    form.value.version = task.version ? task.version : '';
    form.value.createTime = task.createTime ? task.createTime : '';
    form.value.creator = task.creator ? task.creator : '' ;
  }

})

function handleBack() { // 返回
  console.log( curParams.flag) ;
  const routerName = curParams.flag === 'history' ? 'JobHistory' : 'JobManage';
  proxy.$router.replace({ name: routerName, params: {context:curParams.context} });
}

function submitTask() { // 提交修改、新建表单
        proxy.$refs.taskform.validate((valid) => {
        if (valid) {
          const jobName = form.value.jobName
          const alarmTypes = form.value.alarmTypes.join(',')
          const data = {
            id: form.value.id,
            jobName: form.value.jobName,
            jobDesc: form.value.jobDesc,
            deployMode: form.value.deployModeEnum,
            flinkRunConfig: form.value.flinkRunConfig,
            jobType: form.value.jobType,
            alarmTypes: alarmTypes,
            customArgs: form.value.customArgs,
            customMainClass: form.value.customMainClass,
            customJarUrl: form.value.customJarUrl,
            extJarPath: form.value.extJarPath,
            creator: form.value.creator,
            operatorId: form.value.creator
          }
          if ( !form.value.id &&  curParams.flag === 'create'  ) {
            addConfig(data).then(response => {
              loading.value = false
              const { code, data, msg } = response
              if (code !== 200 ) {
                proxy.$modal.$message({ type: 'error', message: (msg || '请求数据异常！') })
                return
              }
              proxy.$modal.$message({ type: 'success', message: `新增任务[${jobName}]成功！` })
              this.$refs.backbutton.click()
            }).catch(error => {
              loading.value = false
            })
          } else if ( form.value.id && curParams.flag === 'update' ) {
            editConfig(data).then(response => {
              loading.value = false
              const { code, data, msg } = response
              if (code !== 200 ) {
                proxy.$modal.$message({ type: 'error', message: (msg || '请求数据异常！') })
                return
              }
              proxy.$modal.$message({ type: 'success', message: `修改任务[${jobName}]成功！` })
              this.$refs.backbutton.click()
            }).catch(error => {
              loading.value = false
            })
          }
        } else {
          return false
        }
      })
}

function formatDateTime(date) {
      return proxy.dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}

function getStatusDesc(status) { // 任务状态
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
        default: return status + '　'
      }
}

function getJobType(jobTypeEnum) {
      switch (jobTypeEnum) {
        case 'SQL_STREAMING': return 0
        case 'SQL_BATCH': return 2
        case 'JAR': return 1
        default: return jobTypeEnum
      }
}


</script>

<style scoped>
  .back{
    margin-top: 20px;
    font-size: 15px;
    color: #303133;
  }
  .fl-jartask-container {
    margin: 0px 20px;
  }
  .fl-back {
    color: #303133;
    font-size: 14px;
    margin-left: -20px;
    cursor: pointer;
  }
  .fl-back:hover {
    color: #a2a6af;
  }
  .fl-task-edit {
    box-sizing: border-box;
    background: #fff;
    min-height: calc(100% - 40px);
    /*max-width: 1160px;*/
  }
  .fl-task-edit >>> label {
    font-weight: 500;
  }
  .fl-task-edit >>> .el-form-item {
    margin-bottom: 15px!important;
  }
  .fl-alarm-row >>> .el-form-item {
    margin-top: -10px!important;
    margin-bottom: 0px!important;
  }
  .fl-cm-row__isRead >>> .el-form-item {
    margin-bottom: 0px!important;
  }
  .fl-button-row >>> .el-form-item {
    margin-bottom: 0px!important;
  }
  .fl-form-item {
    width: 100%;
  }

  >>> .el-page-header__content{
    font-size: 14px;
  }
</style>
