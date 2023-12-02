<template>
  <div v-loading="loading" class="fl-logdetail-container">
    <el-page-header @back="handleBack()"  :content=title  class="back"></el-page-header>
    <el-card shadow="false" class="box-card" style="margin-top: 20px;">
    <el-form ref="logForm" :model="form" :disabled="false" label-width="70px"  class="fl-log">
      <el-row>
        <el-col :span="10">
          <el-form-item label="运行状态" prop="jobStatus">
            <el-input v-model="form.jobStatus" placeholder="运行状态" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item label="Flink客户端日志" prop="clinetJobUrl" label-width="120px">
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
            <el-input v-model="form.localLog" type="textarea" :autosize="{minRows:5}"  id="localLog" disabled />
<!--            <codemirror ref="cm" :value="form.localLog" :options="cmOptions" class="fl-codemirror" />-->
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
// import { CodeMirror, codemirror } from 'vue-codemirror'
// import 'codemirror/lib/codemirror.css'
// import 'codemirror/mode/shell/shell.js'
import { logDetail,getClinetJobInfo } from '@/api/flink/JobRunLog'
import {onMounted} from "vue";
import { useRouter, useRoute } from 'vue-router';

const { proxy } = getCurrentInstance();

const loading = ref(false);

const title = ref("");

const logid = ref("");

const data = reactive({
  form: {
    // id : null,
    // jobConfigId : null,
    // jobId : null,
    // jobName : null,
    // jobStatus : null,
    // localLog : null,
    // remoteLogUrl : null,
    // deployMode : null,
    // clinetJobUrl : null,
    // createTime : null,
    // editTime : null,
    // startTime : null,
    // endTime : null
  },

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
  //在组件挂载后执行的逻辑
  //获取当前路由
  let route = useRoute();
  // 在组件挂载后执行的逻辑
  //获取当前路由的标题
  title.value = route.meta.title;
  const params = route.params;
  //保存上个界面传进来的标志、查询条件、数据
  curParams.flag = params.flag ;
  curParams.context = params.context ;
  curParams.data    = JSON.parse(params.data) ;
  curParams.data    = params.data ;


  logid.value = curParams.data ;
  getLogDetail() ;
})


function handleBack() { // 返回
  if ( curParams.flag === 'loglist' ) {
    proxy.$router.replace({ name: 'JobRunLog' }) ;
  } else if (curParams.flag === 'tasklist') {
    proxy.$router.replace({ name: 'JobManage', params: curParams.context }) ;
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
