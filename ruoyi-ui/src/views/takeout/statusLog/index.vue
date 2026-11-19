<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="业务类型" prop="bizType">
            <el-input
               v-model="queryParams.bizType"
               placeholder="请输入业务类型"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="业务ID" prop="bizId">
            <el-input
               v-model="queryParams.bizId"
               placeholder="请输入业务ID"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="操作" prop="operatorName">
            <el-input
               v-model="queryParams.operatorName"
               placeholder="请输入操作"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
         </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="statusLogList">
         <el-table-column label="日志编号" align="center" prop="logId" width="100" />
         <el-table-column label="业务类型" align="center" prop="bizType" width="130" />
         <el-table-column label="业务ID" align="center" prop="bizId" width="120" />
         <el-table-column label="操作" align="center" prop="operatorName" width="140" />
         <el-table-column label="原状态" align="center" prop="fromStatus" width="110" />
         <el-table-column label="新状态" align="center" prop="toStatus" width="110" />
         <el-table-column label="操作人" align="center" prop="operatorName" width="140" />
         <el-table-column label="操作时间" align="center" prop="createTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
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
   </div>
</template>

<script setup name="TakeoutStatusLog">
import { listStatusLog } from "@/api/takeout/statusLog"

const { proxy } = getCurrentInstance()

const statusLogList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    bizType: undefined,
    bizId: undefined,
    operatorName: undefined
  }
})

const { queryParams } = toRefs(data)

/** 查询状态日志列表 */
function getList() {
  loading.value = true
  listStatusLog(queryParams.value).then(response => {
    statusLogList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

getList()
</script>
