<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="商家名称" prop="merchantName">
            <el-input
               v-model="queryParams.merchantName"
               placeholder="请输入商家名称"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="联系电话" prop="contactPhone">
            <el-input
               v-model="queryParams.contactPhone"
               placeholder="请输入联系电话"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="营业状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="营业状态" clearable style="width: 200px">
               <el-option
                  v-for="dict in takeout_merchant_status"
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
         <el-col :span="1.5">
            <el-button
               type="primary"
               plain
               icon="Plus"
               @click="handleAdd"
               v-hasPermi="['takeout:merchant:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="success"
               plain
               icon="Edit"
               :disabled="single"
               @click="handleUpdate"
               v-hasPermi="['takeout:merchant:edit']"
            >修改</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['takeout:merchant:remove']"
            >删除</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="warning"
               plain
               icon="Download"
               @click="handleExport"
               v-hasPermi="['takeout:merchant:export']"
            >导出</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="merchantList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="商家编号" align="center" prop="merchantId" width="100" />
         <el-table-column label="商家名称" align="center" prop="merchantName" :show-overflow-tooltip="true" />
         <el-table-column label="店主用户ID" align="center" prop="ownerUserId" width="120" />
         <el-table-column label="联系电话" align="center" prop="contactPhone" width="140" />
         <el-table-column label="地址" align="center" prop="address" :show-overflow-tooltip="true" />
         <el-table-column label="营业状态" align="center" prop="status" width="110">
            <template #default="scope">
               <dict-tag :options="takeout_merchant_status" :value="scope.row.status" />
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
            <template #default="scope">
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['takeout:merchant:edit']">修改</el-button>
               <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['takeout:merchant:remove']">删除</el-button>
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

      <!-- 添加或修改商家对话框 -->
      <el-dialog :title="title" v-model="open" width="680px" append-to-body>
         <el-form ref="merchantRef" :model="form" :rules="rules" label-width="100px">
            <el-row>
               <el-col :span="12">
                  <el-form-item label="商家名称" prop="merchantName">
                     <el-input v-model="form.merchantName" placeholder="请输入商家名称" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="店主用户ID" prop="ownerUserId">
                     <el-input v-model="form.ownerUserId" placeholder="请输入店主用户ID" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="联系电话" prop="contactPhone">
                     <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="营业状态" prop="status">
                     <el-radio-group v-model="form.status">
                        <el-radio
                           v-for="dict in takeout_merchant_status"
                           :key="dict.value"
                           :value="dict.value"
                        >{{ dict.label }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="地址" prop="address">
                     <el-input v-model="form.address" type="textarea" placeholder="请输入地址" />
                  </el-form-item>
               </el-col>
            </el-row>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitForm">确 定</el-button>
               <el-button @click="cancel">取 消</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="TakeoutMerchant">
import { listMerchant, getMerchant, delMerchant, addMerchant, updateMerchant } from "@/api/takeout/merchant"

const { proxy } = getCurrentInstance()
const { takeout_merchant_status } = useDict("takeout_merchant_status")

const merchantList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    merchantName: undefined,
    contactPhone: undefined,
    status: undefined
  },
  rules: {
    merchantName: [{ required: true, message: "商家名称不能为空", trigger: "blur" }],
    contactPhone: [{ required: true, message: "联系电话不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询商家列表 */
function getList() {
  loading.value = true
  listMerchant(queryParams.value).then(response => {
    merchantList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    merchantId: undefined,
    merchantName: undefined,
    ownerUserId: undefined,
    contactPhone: undefined,
    address: undefined,
    status: "0"
  }
  proxy.resetForm("merchantRef")
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
  ids.value = selection.map(item => item.merchantId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加商家"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const merchantId = row.merchantId || ids.value
  getMerchant(merchantId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改商家"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["merchantRef"].validate(valid => {
    if (valid) {
      if (form.value.merchantId != undefined) {
        updateMerchant(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addMerchant(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const merchantIds = row.merchantId || ids.value
  proxy.$modal.confirm('是否确认删除商家编号为"' + merchantIds + '"的数据项？').then(function() {
    return delMerchant(merchantIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("takeout/admin/merchant/export", {
    ...queryParams.value
  }, `merchant_${new Date().getTime()}.xlsx`)
}

getList()
</script>
