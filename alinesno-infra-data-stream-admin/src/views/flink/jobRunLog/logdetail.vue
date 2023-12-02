<template>
  <div v-loading="loading" class="fl-logdetail-container">
    <el-page-header @back="handleBack()"  :content=title  class="back"></el-page-header>
    <el-card shadow="false" class="box-card" style="margin-top: 20px;">
    <el-form ref="form" :model="form" :disabled="false" label-width="70px"  class="fl-log">
      <el-row>
        <el-col :span="10">
          <el-form-item label="运行状态" prop="jobStatus">
            <el-input v-model="form.jobStatus" placeholder="运行状态" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item label="Flink客户端日志" prop="clinetJobUrl" label-width="120px">
<!--            <el-link type="primary" target="_blank" :href="form.clinetJobUrl" :underline="false">{{ form.clinetJobUrl }}</el-link>-->
            <el-link type="primary" target="_blank"  @click="showClinetJobInfo()"   :underline="false">查看日志</el-link>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item label="运行模式" prop="deployMode">
            <el-input v-model="form.deployMode" placeholder="运行模式" disabled />
          </el-form-item>
        </el-col>
<!--        <el-col :span="14">-->
<!--          <el-form-item label="Flink集群日志" prop="remoteLogUrl" label-width="120px">-->
<!--            <el-link type="primary" target="_blank" :href="form.remoteLogUrl" :underline="false">{{ form.remoteLogUrl }}</el-link>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="日志内容" prop="localLog">
            <codemirror ref="cm" :value="form.localLog" :options="cmOptions" class="fl-codemirror" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row class="fl-button-row">
        <el-col :span="24">
          <el-form-item label="">
            <el-button type="primary" @click="getLogDetail()">刷新</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    </el-card>
  </div>
</template>

<script  setup  name="LogDetail">
import { CodeMirror, codemirror } from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/shell/shell.js'
import { logDetail,getClinetJobInfo } from '@/api/flink/JobRunLog'
import {onMounted} from "vue";

const { proxy } = getCurrentInstance();

const loading = ref(false);

const title = ref("");

const logid = ref("");

const data = reactive({
  form: {},

  curParams: {
    flag: '',
    data: {},
    context: '' // 父页面传递过来的参加，返回时带给父页面恢复上下文
  },

  cmOptions: {
    value: '',
    mode: 'text/x-sh', // flink/x-fsql, text/x-mysql, text/x-sh
    theme: 'default', // solarized light,base16-light,cobalt,default,mbo,cobalt
    readOnly: true,
    tabSize: 4,
    line: true,
    lineNumbers: true,
    extraKeys: { 'Ctrl': 'autocomplete' } // 自定义快捷键
  }

});

const { form, curParams, cmOptions } = toRefs(data);

onMounted(() => {
  // 在组件挂载后执行的逻辑
  title.value = proxy.$route.meta.title ;
  const params = proxy.$route.params ;
  curParams.value.flag = params.flag ;
  curParams.value.context = params.context ;
  curParams.value.data = params.data ;
  logid.value = params.data.id ;
  getLogDetail() ;
})


function handleBack() { // 返回
  if ( curParams.value.flag === 'loglist' ) {
    proxy.$router.replace({ name: 'jobRunLog', params: curParams.value.context }) ;
  } else if (this.params.flag === 'tasklist') {
    proxy.$router.replace({ name: 'jobManage', params: curParams.value.context }) ;
  }
}

function getLogDetail() { // 查询日志详情
  loading.value = true ;
  logDetail(logid.value).then(response => {
    loading.value = false ;
    const { code, msg, data } = response ;
    if (code !== 200 ) {
      proxy.$modal.msgError(msg || '请求数据异常！') ;
      return
    }
    form.value = data || {} ;
  }).catch(error => {
    loading.value = false ;
  })
}

function showClinetJobInfo(){
  getClinetJobInfo().then( res =>{
    let win ;
    win = window.open() ;
    win.document.write (res) ;
  }).catch(error => {
  })
}

</script>

<style scoped>
  .back{
    margin-top: 20px;
    font-size: 15px;
    color: #303133;
  }
  .fl-logdetail-container {
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
  .fl-codemirror {
    border: 1px solid#C0C4CC;
  }
  .fl-log >>> .el-form-item {
    margin-bottom: 10px!important;
  }
  .fl-button-row >>> .el-form-item {
    margin-bottom: 0px!important;
  }
 .fl-log >>> .CodeMirror {
    height: calc(100vh - 205px);
    line-height : 150%;
    font-family: monospace,Helvetica Neue,Helvetica,Arial,sans-serif;
    font-size: 13px;
    background: #f5f7fa;
  }
</style>
