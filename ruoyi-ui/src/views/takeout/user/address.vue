<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="收货人" prop="receiverName">
            <el-input
               v-model="queryParams.receiverName"
               placeholder="请输入收货人"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="联系电话" prop="receiverPhone">
            <el-input
               v-model="queryParams.receiverPhone"
               placeholder="请输入联系电话"
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
         <el-col :span="1.5">
            <el-button
               type="primary"
               plain
               icon="Plus"
               @click="handleAdd"
            >新增</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="addressList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="地址编号" align="center" prop="addressId" width="100" />
         <el-table-column label="收货人" align="center" prop="receiverName" width="110" />
         <el-table-column label="联系电话" align="center" prop="receiverPhone" width="140" />
         <el-table-column label="省" align="center" prop="province" width="100" />
         <el-table-column label="市" align="center" prop="city" width="100" />
         <el-table-column label="区" align="center" prop="district" width="100" />
         <el-table-column label="详细地址" align="center" prop="detailAddress" :show-overflow-tooltip="true" />
         <el-table-column label="默认" align="center" prop="isDefault" width="80">
            <template #default="scope">
               <el-tag :type="scope.row.isDefault == 1 ? 'success' : 'info'" size="small">
                  {{ scope.row.isDefault == 1 ? '是' : '否' }}
               </el-tag>
            </template>
         </el-table-column>
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
            <template #default="scope">
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
               <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
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

      <!-- 添加或修改地址对话框 -->
      <el-dialog :title="title" v-model="open" width="680px" append-to-body>
         <el-form ref="addressRef" :model="form" :rules="rules" label-width="100px">
            <el-row>
               <el-col :span="12">
                  <el-form-item label="收货人" prop="receiverName">
                     <el-input v-model="form.receiverName" placeholder="请输入收货人" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="联系电话" prop="receiverPhone">
                     <el-input v-model="form.receiverPhone" placeholder="请输入联系电话" />
                  </el-form-item>
               </el-col>
               <el-col :span="8">
                  <el-form-item label="省" prop="province">
                     <el-input v-model="form.province" placeholder="请输入省" />
                  </el-form-item>
               </el-col>
               <el-col :span="8">
                  <el-form-item label="市" prop="city">
                     <el-input v-model="form.city" placeholder="请输入市" />
                  </el-form-item>
               </el-col>
               <el-col :span="8">
                  <el-form-item label="区" prop="district">
                     <el-input v-model="form.district" placeholder="请输入区" />
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="详细地址" prop="detailAddress">
                     <el-input v-model="form.detailAddress" type="textarea" placeholder="请输入详细地址" />
                  </el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="是否默认" prop="isDefault">
                     <el-switch v-model="form.isDefault" :active-value="1" :inactive-value="0" active-text="是" inactive-text="否" />
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

<script setup name="TakeoutUserAddress">
import { listUserAddress, getUserAddress, addUserAddress, updateUserAddress, delUserAddress } from "@/api/takeout/user"

const { proxy } = getCurrentInstance()

const addressList = ref([])
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
    receiverName: undefined,
    receiverPhone: undefined
  },
  rules: {
    receiverName: [{ required: true, message: "收货人不能为空", trigger: "blur" }],
    receiverPhone: [{ required: true, message: "联系电话不能为空", trigger: "blur" }],
    province: [{ required: true, message: "省不能为空", trigger: "blur" }],
    city: [{ required: true, message: "市不能为空", trigger: "blur" }],
    district: [{ required: true, message: "区不能为空", trigger: "blur" }],
    detailAddress: [{ required: true, message: "详细地址不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询我的地址列表 */
function getList() {
  loading.value = true
  listUserAddress(queryParams.value).then(response => {
    addressList.value = response.rows || response.data || []
    total.value = response.total || addressList.value.length
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
    addressId: undefined,
    receiverName: undefined,
    receiverPhone: undefined,
    province: undefined,
    city: undefined,
    district: undefined,
    detailAddress: undefined,
    isDefault: 0
  }
  proxy.resetForm("addressRef")
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
  ids.value = selection.map(item => item.addressId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加地址"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const addressId = row.addressId || ids.value
  getUserAddress(addressId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改地址"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["addressRef"].validate(valid => {
    if (valid) {
      if (form.value.addressId != undefined) {
        updateUserAddress(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addUserAddress(form.value).then(() => {
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
  const addressIds = row.addressId || ids.value
  proxy.$modal.confirm('是否确认删除地址编号为"' + addressIds + '"的数据项？').then(function() {
    return delUserAddress(addressIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

getList()
</script>
