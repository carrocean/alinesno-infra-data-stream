<!-- 恢复SavePoint -->
<template>
  <el-dialog
    v-loading.fullscreen.lock="loading"
    :title="title"
    :visible.sync="visible"
    :close-on-click-modal="false"
    :modal-append-to-body="false"
    class="wl-dialog"
    width="800px"
    @close="doClose()"
  >
    <span class="wl-title">只显示最近10次备份(每一小时自动备份或者停止任务的时候备份)</span>
    <el-popover placement="top-start" trigger="hover">
      <div class="wl-popover">
        <p>备注1：如果sql语句发生变更或者业务逻辑发生变化，此时从savepoint恢复可能导致数据结果错误</p>
        <p>备注2：yarn模式下和集群模式下统一目录是(必须绑定hdfs)：hdfs:///flink/savepoint/flink-pipeline/</p>
        <p>备注3：LOCAL模式本地模式下保存在flink客户端的根目录下</p>
        <p>备注4：hdfs:///flink/savepoint/flink-pipeline/ 建议提前创建好</p>
      </div>
      <span slot="reference">
        <i class="el-icon-info" />
      </span>
    </el-popover>
    <el-form ref="RecoverSavePointRef" :model="form" :inline="true" :rules="rules" style="padding-top: 10px;">
      <el-form-item prop="savepoint">
        <el-input ref="inputSavePoint" v-model="form.savepoint" placeholder="手动增加SavePoint地址" class="wl-input" @blur="blurSavePoint()" />
      </el-form-item>
      <el-form-item>
        <el-button @click="addPerSavepoint()">手动添加</el-button>
      </el-form-item>
    </el-form>
    <!-- 列表 -->
    <el-table :data="list" :header-cell-style="{background:'#f4f4f5','text-align':'center'}" class="wl-table" highlight-current-row border @current-change="handleCurrentChange">
      <el-table-column width="50">
        <template  #default="scope">
          <el-radio v-model="radioIndex" :label="scope.$index" @change.native="getCurrentRow(scope.$index)" />
        </template>
      </el-table-column>
<!--      <el-table-column prop="id" :show-overflow-tooltip="true" label="备份编号" width="80" align="center" />-->
      <el-table-column prop="savepointPath" :show-overflow-tooltip="true" label="备份地址" />
      <el-table-column prop="backupTime" :show-overflow-tooltip="true" label="备份时间" width="170" />
    </el-table>
    <span slot="footer" class="dialog-footer">
      <el-button @click="doCancel()">关 闭</el-button>
      <el-button type="primary" @click="doConfirm()">恢复任务</el-button>
    </span>
  </el-dialog>
</template>

<script  setup name="RecoverSavePoint">
import { querySavePointList10, addSavepoint,startSavepoint } from "@/api/flink/JobConfig";

import { ref, reactive, onMounted} from 'vue';
import { useRouter, useRoute } from 'vue-router';
const { proxy } = getCurrentInstance();
const router=useRouter() ;
const route=useRoute()   ;

const loading = ref(false);
const visible = ref(false);
const title = ref("恢复任务运行");
const list = ref([]);
const currentRow = ref();
const radioIndex = ref(false);

const data = reactive({
      form: { // 基本设置表单
        taskid: '',
        savepoint: ''
      },
      rules: {
        savepoint: [{ required: true, message: '请输入savepoint地址', trigger: 'change' }]
      },
      task: {},
});


const { form, rules, task } = toRefs(data);


function openDialog(task) {
  visible.value = true
  list.value = []
  form.value.savepoint = ''
  form.value.taskid = task.id
  task.value = task
  title.value = `恢复任务运行[${task.jobName}]`
  currentRow.value = null
  radioIndex.value = false
  querySavePointList(task.id)
}

function querySavePointList(id) { // 查询SavePoint历史列表（最近10条）
  loading.value = true
  radioIndex.value = false
  querySavePointList10(id).then(response => {
    loading.value = false
    proxy.$refs.RecoverSavePointRef.clearValidate()
    const { code, msg, data } = response
    if ( code !== 200 ) {
      proxy.$modal.$message({ type: 'error', message: (msg || '请求数据异常!') })
      return
    }
    list.value = (data && data.data) ? data.data : []
    loading.value = false
  }).catch(error => {
    loading.value = false
  })
}

function addPerSavepoint() {
  proxy.$refs.RecoverSavePointRef.validate(valid => {
    if (valid) {
      loading.value = true
      const jobConfigId = form.value.taskid
      const savepointPath = form.value.savepoint
      addSavepoint(jobConfigId, savepointPath).then(response => {
        loading.value = false
        const {  code, msg, data } = response
        if ( code !== 200 ) {
          proxy.$modal.$message({ type: 'error', message: (msg || '请求数据异常!') })
          return
        }
        list.value = (data && data.data) ? data.data : []
        querySavePointList(jobConfigId)
      }).catch(error => {
        loading.value = false
      })
    } else {
      proxy.$refs.inputSavePoint.focus()
    }
  })
}

function blurSavePoint() {
  this.$refs.RecoverSavePointRef.clearValidate()
}

function handleCurrentChange(row) { // 选中行
  if (row) {
    currentRow.value = row
    const index = list.value.findIndex(item => item.id == currentRow.id.value)
    if (index > -1) {
      radioIndex.value = index
    }
  }
}

function getCurrentRow(index) { // 设置单选框索引
  radioIndex.value = index
}

function doConfirm() {
  if ( !currentRow.value ) {
    proxy.$modal.$message({ type: 'error', message: '请选择备份记录!' })
    return
  }
  const id = form.value.taskid
  const savepointId = currentRow.value.id
  const savepointPath = currentRow.value.savepointPath
  proxy.$modal.confirm(`是否恢复任务[备份：${savepointPath}]！`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    loading.value = true
    startSavepoint(id, savepointId).then(response => {
      loading.value = false
      const { code, msg, data } = response
      if ( code !== 200 ) {
        proxy.$modal.$message({ type: 'error', message: (msg || '请求数据异常!') })
        return
      }
      proxy.$modal.$message({ type: 'info', message: `正在恢复任务[备份：${savepointId}]，稍后请刷新!` })
      this.$parent.getTasks()
      this.visible = false
    }).catch(error => {
      loading.value = false
      // console.log(error)
    })
  })
}

function doCancel() {
  visible.value = false
}

function doClose() {
  list.value = []
  form.value.savepoint = ''
  form.value.taskid = ''
  task.value = null
  title.value = ''
  currentRow.value = null
  radioIndex.value = false
}

</script>

<style scoped>
  .wl-input {
    width: 660px;
  }
  .wl-title {
    font-size: 16px;
    font-weight: 600;
    cursor: default;
    padding-right: 2px;
  }
  .wl-popover {
    line-height: 8px;
    color: red;
  }
  .wl-popover p {
    line-height: 8px;
  }
  .wl-dialog >>> .el-dialog__body {
    padding: 5px 20px !important;
  }
  .wl-dialog >>> .el-radio {
    margin-left: 6px;
  }
  .wl-dialog >>> .el-radio .el-radio__label {
    display: none;
  }
</style>
