<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="任务编号" prop="taskNo">
            <el-input
               v-model="queryParams.taskNo"
               placeholder="请输入任务编号"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="订单编号" prop="orderNo">
            <el-input
               v-model="queryParams.orderNo"
               placeholder="请输入订单编号"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="配送状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="配送状态" clearable style="width: 200px">
               <el-option
                  v-for="dict in takeout_delivery_status"
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
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="taskList">
         <el-table-column label="任务编号" align="center" prop="taskNo" width="180" :show-overflow-tooltip="true" />
         <el-table-column label="订单编号" align="center" prop="orderNo" width="180" :show-overflow-tooltip="true" />
         <el-table-column label="商家" align="center" prop="merchantName" :show-overflow-tooltip="true" />
         <el-table-column label="收货地址" align="center" prop="deliveryAddress" :show-overflow-tooltip="true" />
         <el-table-column label="配送状态" align="center" prop="status" width="110">
            <template #default="scope">
               <dict-tag :options="takeout_delivery_status" :value="scope.row.status" />
            </template>
         </el-table-column>
         <el-table-column label="任务时间" align="center" prop="createTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="接单时间" align="center" prop="acceptTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.acceptTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="取餐时间" align="center" prop="pickupTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.pickupTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="送达时间" align="center" prop="deliverTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.deliverTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="320" fixed="right">
            <template #default="scope">
               <el-button link type="success" icon="Check" @click="handleAction(scope.row, 'accept')">接单</el-button>
               <el-button link type="primary" icon="Location" @click="handleAction(scope.row, 'arrive')">到店</el-button>
               <el-button link type="warning" icon="Food" @click="handleAction(scope.row, 'pickup')">取餐</el-button>
               <el-button link type="success" icon="CircleCheck" @click="handleAction(scope.row, 'deliver')">送达</el-button>
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

<script setup name="TakeoutRiderDelivery">
import { listRiderTask, acceptRiderTask, arriveRiderTask, pickupRiderTask, deliverRiderTask } from "@/api/takeout/rider"

const { proxy } = getCurrentInstance()
const { takeout_delivery_status } = useDict("takeout_delivery_status")

const taskList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    taskNo: undefined,
    orderNo: undefined,
    status: undefined
  }
})

const { queryParams } = toRefs(data)

const actionMap = {
  accept: { fn: acceptRiderTask, name: "接单", confirm: "是否确认接单？" },
  arrive: { fn: arriveRiderTask, name: "到店", confirm: "是否确认已到店？" },
  pickup: { fn: pickupRiderTask, name: "取餐", confirm: "是否确认已取餐？" },
  deliver: { fn: deliverRiderTask, name: "送达", confirm: "是否确认已送达？" }
}

/** 查询骑手任务列表 */
function getList() {
  loading.value = true
  listRiderTask(queryParams.value).then(response => {
    taskList.value = response.rows || response.data || []
    total.value = response.total || taskList.value.length
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

/** 骑手操作 */
function handleAction(row, type) {
  const action = actionMap[type]
  proxy.$modal.confirm(action.confirm + '任务编号：' + row.taskNo).then(function() {
    return action.fn(row.taskId)
  }).then(() => {
    proxy.$modal.msgSuccess(action.name + "成功")
    getList()
  }).catch(() => {})
}

getList()
</script>
