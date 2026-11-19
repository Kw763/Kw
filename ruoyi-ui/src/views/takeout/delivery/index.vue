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

      <el-table v-loading="loading" :data="deliveryList">
         <el-table-column label="任务编号" align="center" prop="taskNo" width="180" :show-overflow-tooltip="true" />
         <el-table-column label="订单编号" align="center" prop="orderNo" width="180" :show-overflow-tooltip="true" />
         <el-table-column label="骑手" align="center" prop="riderName" width="120" />
         <el-table-column label="配送状态" align="center" prop="status" width="110">
            <template #default="scope">
               <dict-tag :options="takeout_delivery_status" :value="scope.row.status" />
            </template>
         </el-table-column>
         <el-table-column label="任务时间" align="center" prop="createTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="接单时间" align="center" prop="acceptTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.acceptTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="取餐时间" align="center" prop="pickupTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.pickupTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="送达时间" align="center" prop="deliverTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.deliverTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
            <template #default="scope">
               <el-button link type="primary" icon="Position" @click="handleAssign(scope.row)" v-hasPermi="['takeout:delivery:assign']">派单</el-button>
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

      <!-- 派单对话框 -->
      <el-dialog title="派单" v-model="assignOpen" width="500px" append-to-body>
         <el-form ref="assignRef" :model="assignForm" :rules="assignRules" label-width="100px">
            <el-form-item label="任务编号">
               <span>{{ assignForm.taskNo }}</span>
            </el-form-item>
            <el-form-item label="订单编号">
               <span>{{ assignForm.orderNo }}</span>
            </el-form-item>
            <el-form-item label="选择骑手" prop="riderId">
               <el-select v-model="assignForm.riderId" placeholder="请选择骑手" filterable style="width: 100%">
                  <el-option
                     v-for="rider in riderOptions"
                     :key="rider.riderId"
                     :label="rider.riderName + (rider.phone ? '（' + rider.phone + '）' : '')"
                     :value="rider.riderId"
                  />
               </el-select>
            </el-form-item>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitAssign">确 定</el-button>
               <el-button @click="assignOpen = false">取 消</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="TakeoutDelivery">
import { listDelivery, assignDelivery, availableRiders } from "@/api/takeout/delivery"

const { proxy } = getCurrentInstance()
const { takeout_delivery_status } = useDict("takeout_delivery_status")

const deliveryList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)

const assignOpen = ref(false)
const riderOptions = ref([])
const assignForm = reactive({
  taskId: undefined,
  taskNo: undefined,
  orderNo: undefined,
  riderId: undefined
})
const assignRules = {
  riderId: [{ required: true, message: "请选择骑手", trigger: "change" }]
}

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

/** 查询配送任务列表 */
function getList() {
  loading.value = true
  listDelivery(queryParams.value).then(response => {
    deliveryList.value = response.rows
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

/** 派单按钮操作 */
function handleAssign(row) {
  assignForm.taskId = row.taskId
  assignForm.taskNo = row.taskNo
  assignForm.orderNo = row.orderNo
  assignForm.riderId = undefined
  assignOpen.value = true
  availableRiders().then(response => {
    riderOptions.value = response.data || response.rows || []
  })
}

/** 提交派单 */
function submitAssign() {
  proxy.$refs["assignRef"].validate(valid => {
    if (valid) {
      assignDelivery(assignForm.taskId, assignForm.riderId).then(() => {
        proxy.$modal.msgSuccess("派单成功")
        assignOpen.value = false
        getList()
      })
    }
  })
}

getList()
</script>
