<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="商家" prop="merchantId">
            <el-select v-model="queryParams.merchantId" placeholder="请选择商家" clearable filterable style="width: 200px">
               <el-option
                  v-for="item in merchantOptions"
                  :key="item.merchantId"
                  :label="item.merchantName"
                  :value="item.merchantId"
               />
            </el-select>
         </el-form-item>
         <el-form-item label="商品名称" prop="productName">
            <el-input
               v-model="queryParams.productName"
               placeholder="请输入商品名称"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="上架状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="上架状态" clearable style="width: 200px">
               <el-option
                  v-for="dict in takeout_product_status"
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
               v-hasPermi="['takeout:product:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="success"
               plain
               icon="Edit"
               :disabled="single"
               @click="handleUpdate"
               v-hasPermi="['takeout:product:edit']"
            >修改</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['takeout:product:remove']"
            >删除</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="warning"
               plain
               icon="Download"
               @click="handleExport"
               v-hasPermi="['takeout:product:export']"
            >导出</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="商品编号" align="center" prop="productId" width="100" />
         <el-table-column label="图片" align="center" prop="image" width="90">
            <template #default="scope">
               <el-image v-if="scope.row.image" :src="scope.row.image" fit="cover" style="width:50px;height:50px;border-radius:4px" :preview-src-list="[scope.row.image]" preview-teleported />
               <span v-else>-</span>
            </template>
         </el-table-column>
         <el-table-column label="商品名称" align="center" prop="productName" :show-overflow-tooltip="true" />
         <el-table-column label="商家名称" align="center" prop="merchantName" :show-overflow-tooltip="true" />
         <el-table-column label="价格(元)" align="center" prop="price" width="110">
            <template #default="scope">
               <span>¥{{ Number(scope.row.price || 0).toFixed(2) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="上架状态" align="center" prop="status" width="100">
            <template #default="scope">
               <dict-tag :options="takeout_product_status" :value="scope.row.status" />
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
            <template #default="scope">
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['takeout:product:edit']">修改</el-button>
               <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['takeout:product:remove']">删除</el-button>
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

      <!-- 添加或修改商品对话框 -->
      <el-dialog :title="title" v-model="open" width="680px" append-to-body>
         <el-form ref="productRef" :model="form" :rules="rules" label-width="100px">
            <el-row>
               <el-col :span="12">
                  <el-form-item label="所属商家" prop="merchantId">
                     <el-select v-model="form.merchantId" placeholder="请选择商家" filterable style="width: 100%">
                        <el-option
                           v-for="item in merchantOptions"
                           :key="item.merchantId"
                           :label="item.merchantName"
                           :value="item.merchantId"
                        />
                     </el-select>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="商品名称" prop="productName">
                     <el-input v-model="form.productName" placeholder="请输入商品名称" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="价格(元)" prop="price">
                     <el-input-number v-model="form.price" :min="0" :precision="2" :step="0.01" controls-position="right" style="width: 100%" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="上架状态" prop="status">
                     <el-radio-group v-model="form.status">
                        <el-radio
                           v-for="dict in takeout_product_status"
                           :key="dict.value"
                           :value="dict.value"
                        >{{ dict.label }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="图片地址" prop="image">
                     <el-input v-model="form.image" placeholder="请输入图片URL" />
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

<script setup name="TakeoutProduct">
import { listProduct, getProduct, delProduct, addProduct, updateProduct } from "@/api/takeout/product"
import { listMerchant } from "@/api/takeout/merchant"

const { proxy } = getCurrentInstance()
const { takeout_product_status } = useDict("takeout_product_status")

const productList = ref([])
const merchantOptions = ref([])
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
    merchantId: undefined,
    productName: undefined,
    status: undefined
  },
  rules: {
    merchantId: [{ required: true, message: "所属商家不能为空", trigger: "change" }],
    productName: [{ required: true, message: "商品名称不能为空", trigger: "blur" }],
    price: [{ required: true, message: "价格不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询商家下拉列表 */
function getMerchantOptions() {
  listMerchant({ pageNum: 1, pageSize: 1000 }).then(response => {
    merchantOptions.value = response.rows || []
  })
}

/** 查询商品列表 */
function getList() {
  loading.value = true
  listProduct(queryParams.value).then(response => {
    productList.value = response.rows
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
    productId: undefined,
    merchantId: undefined,
    productName: undefined,
    price: 0,
    image: undefined,
    status: "0"
  }
  proxy.resetForm("productRef")
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
  ids.value = selection.map(item => item.productId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加商品"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const productId = row.productId || ids.value
  getProduct(productId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改商品"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["productRef"].validate(valid => {
    if (valid) {
      if (form.value.productId != undefined) {
        updateProduct(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addProduct(form.value).then(() => {
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
  const productIds = row.productId || ids.value
  proxy.$modal.confirm('是否确认删除商品编号为"' + productIds + '"的数据项？').then(function() {
    return delProduct(productIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("takeout/admin/product/export", {
    ...queryParams.value
  }, `product_${new Date().getTime()}.xlsx`)
}

getMerchantOptions()
getList()
</script>
