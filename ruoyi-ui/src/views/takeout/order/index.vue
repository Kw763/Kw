<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="订单编号" prop="orderNo">
            <el-input
               v-model="queryParams.orderNo"
               placeholder="请输入订单编号"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="商家名称" prop="merchantName">
            <el-input
               v-model="queryParams.merchantName"
               placeholder="请输入商家名称"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="订单状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="订单状态" clearable style="width: 200px">
               <el-option
                  v-for="dict in takeout_order_status"
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

      <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="订单编号" align="center" prop="orderNo" width="180" :show-overflow-tooltip="true" />
         <el-table-column label="商家名称" align="center" prop="merchantName" :show-overflow-tooltip="true" />
         <el-table-column label="订单状态" align="center" prop="status" width="110">
            <template #default="scope">
               <dict-tag :options="takeout_order_status" :value="scope.row.status" />
            </template>
         </el-table-column>
         <el-table-column label="支付金额" align="center" prop="totalAmount" width="120">
            <template #default="scope">
               <span>¥{{ Number(scope.row.totalAmount || 0).toFixed(2) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="收货人" align="center" prop="receiverName" width="110" />
         <el-table-column label="联系电话" align="center" prop="receiverPhone" width="140" />
         <el-table-column label="收货地址" align="center" prop="deliveryAddress" :show-overflow-tooltip="true" />
         <el-table-column label="下单时间" align="center" prop="createTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
            <template #default="scope">
               <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['takeout:order:query']">详情</el-button>
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

      <!-- 订单详情抽屉 -->
      <el-drawer title="订单详情" v-model="drawerOpen" direction="rtl" size="60%" append-to-body class="detail-drawer">
         <div v-loading="detailLoading" class="drawer-content">
            <template v-if="orderInfo.orderId != undefined">
               <h4 class="section-header">基本信息</h4>
               <el-row :gutter="20" class="mb8">
                  <el-col :span="12">
                     <div class="info-item"><label class="info-label">订单编号：</label><span class="info-value plaintext">{{ orderInfo.orderNo }}</span></div>
                  </el-col>
                  <el-col :span="12">
                     <div class="info-item"><label class="info-label">订单状态：</label><span class="info-value plaintext"><dict-tag :options="takeout_order_status" :value="orderInfo.status" /></span></div>
                  </el-col>
                  <el-col :span="12">
                     <div class="info-item"><label class="info-label">商家名称：</label><span class="info-value plaintext">{{ orderInfo.merchantName }}</span></div>
                  </el-col>
                  <el-col :span="12">
                     <div class="info-item"><label class="info-label">支付金额：</label><span class="info-value plaintext">¥{{ Number(orderInfo.totalAmount || 0).toFixed(2) }}</span></div>
                  </el-col>
                  <el-col :span="12">
                     <div class="info-item"><label class="info-label">支付状态：</label><span class="info-value plaintext"><dict-tag :options="takeout_payment_status" :value="orderInfo.payment?.status" /></span></div>
                  </el-col>
                  <el-col :span="12">
                     <div class="info-item"><label class="info-label">下单时间：</label><span class="info-value plaintext">{{ orderInfo.createTime }}</span></div>
                  </el-col>
                  <el-col :span="12">
                     <div class="info-item"><label class="info-label">支付时间：</label><span class="info-value plaintext">{{ orderInfo.payment?.paidTime }}</span></div>
                  </el-col>
               </el-row>

               <h4 class="section-header">收货信息</h4>
               <el-row :gutter="20" class="mb8">
                  <el-col :span="12">
                     <div class="info-item"><label class="info-label">收货人：</label><span class="info-value plaintext">{{ orderInfo.receiverName }}</span></div>
                  </el-col>
                  <el-col :span="12">
                     <div class="info-item"><label class="info-label">联系电话：</label><span class="info-value plaintext">{{ orderInfo.receiverPhone }}</span></div>
                  </el-col>
                  <el-col :span="24">
                     <div class="info-item full-width"><label class="info-label">收货地址：</label><span class="info-value plaintext">{{ orderInfo.deliveryAddress }}</span></div>
                  </el-col>
                  <el-col :span="24" v-if="orderInfo.remark">
                     <div class="info-item full-width"><label class="info-label">备注：</label><span class="info-value plaintext">{{ orderInfo.remark }}</span></div>
                  </el-col>
               </el-row>

               <h4 class="section-header">商品明细</h4>
               <el-table :data="orderInfo.items || []" border style="margin-bottom: 16px">
                  <el-table-column label="商品名称" align="center" prop="productName" :show-overflow-tooltip="true" />
                  <el-table-column label="单价(元)" align="center" prop="productPrice" width="120">
                     <template #default="scope">¥{{ Number(scope.row.productPrice || 0).toFixed(2) }}</template>
                  </el-table-column>
                  <el-table-column label="数量" align="center" prop="quantity" width="90" />
                  <el-table-column label="小计(元)" align="center" prop="subtotal" width="120">
                     <template #default="scope">¥{{ Number(scope.row.subtotal || 0).toFixed(2) }}</template>
                  </el-table-column>
               </el-table>

               <h4 class="section-header">状态日志</h4>
               <el-table :data="orderInfo.statusLogs || []" border>
                  <el-table-column label="操作" align="center" prop="operatorName" width="140" />
                  <el-table-column label="原状态" align="center" prop="fromStatus" width="120">
                     <template #default="scope">{{ selectDictLabel(takeout_order_status, scope.row.fromStatus) || scope.row.fromStatus }}</template>
                  </el-table-column>
                  <el-table-column label="新状态" align="center" prop="toStatus" width="120">
                     <template #default="scope">{{ selectDictLabel(takeout_order_status, scope.row.toStatus) || scope.row.toStatus }}</template>
                  </el-table-column>
                  <el-table-column label="操作人" align="center" prop="operatorName" width="140" />
                  <el-table-column label="操作时间" align="center" prop="createTime" width="180" />
               </el-table>
            </template>
         </div>
      </el-drawer>
   </div>
</template>

<script setup name="TakeoutOrder">
import { listOrder, getOrder } from "@/api/takeout/order"
import { listStatusLog } from "@/api/takeout/statusLog"

const { proxy } = getCurrentInstance()
const { takeout_order_status, takeout_payment_status } = useDict("takeout_order_status", "takeout_payment_status")

const orderList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const total = ref(0)

const drawerOpen = ref(false)
const detailLoading = ref(false)
const orderInfo = reactive({})

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderNo: undefined,
    merchantName: undefined,
    status: undefined
  }
})

const { queryParams } = toRefs(data)

/** 查询订单列表 */
function getList() {
  loading.value = true
  listOrder(queryParams.value).then(response => {
    orderList.value = response.rows
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

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.orderId)
}

/** 查看订单详情 */
function handleView(row) {
  drawerOpen.value = true
  detailLoading.value = true
  Object.keys(orderInfo).forEach(k => delete orderInfo[k])
  Promise.all([
    getOrder(row.orderId),
    listStatusLog({ bizType: 'ORDER', bizId: row.orderId })
  ]).then(([orderRes, logRes]) => {
    Object.assign(orderInfo, orderRes.data || {})
    orderInfo.statusLogs = logRes.rows || []
  }).finally(() => {
    detailLoading.value = false
  })
}

getList()
</script>
