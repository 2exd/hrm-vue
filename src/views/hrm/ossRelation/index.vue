<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['hrm:ossRelation:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['hrm:ossRelation:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['hrm:ossRelation:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['hrm:ossRelation:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="ossRelationList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键ID" align="center" prop="id" v-if="true" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="业务类型" align="center" prop="businessType" />
        <el-table-column label="业务表主键ID" align="center" prop="businessId" />
        <el-table-column label="OSS文件主键ID" align="center" prop="ossId" />
        <el-table-column label="操作" align="center" fixed="right"  class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['hrm:ossRelation:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['hrm:ossRelation:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改通用业务-OSS文件关联对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="ossRelationFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="业务表主键ID" prop="businessId">
          <el-input v-model="form.businessId" placeholder="请输入业务表主键ID" />
        </el-form-item>
        <el-form-item label="OSS文件主键ID" prop="ossId">
          <el-input v-model="form.ossId" placeholder="请输入OSS文件主键ID" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="OssRelation" lang="ts">
import { listOssRelation, getOssRelation, delOssRelation, addOssRelation, updateOssRelation } from '@/api/hrm/ossRelation';
import { OssRelationVO, OssRelationQuery, OssRelationForm } from '@/api/hrm/ossRelation/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const ossRelationList = ref<OssRelationVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const ossRelationFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: OssRelationForm = {
  id: undefined,
  remark: undefined,
  businessType: undefined,
  businessId: undefined,
  ossId: undefined
}
const data = reactive<PageData<OssRelationForm, OssRelationQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    params: {
    }
  },
  rules: {
    id: [
      { required: true, message: "主键ID不能为空", trigger: "blur" }
    ],
    businessType: [
      { required: true, message: "业务类型不能为空", trigger: "change" }
    ],
    businessId: [
      { required: true, message: "业务表主键ID不能为空", trigger: "blur" }
    ],
    ossId: [
      { required: true, message: "OSS文件主键ID不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询通用业务-OSS文件关联列表 */
const getList = async () => {
  loading.value = true;
  const res = await listOssRelation(queryParams.value);
  ossRelationList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
}

/** 表单重置 */
const reset = () => {
  form.value = {...initFormData};
  ossRelationFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: OssRelationVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加通用业务-OSS文件关联";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: OssRelationVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getOssRelation(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改通用业务-OSS文件关联";
}

/** 提交按钮 */
const submitForm = () => {
  ossRelationFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateOssRelation(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addOssRelation(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: OssRelationVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除通用业务-OSS文件关联编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delOssRelation(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('hrm/ossRelation/export', {
    ...queryParams.value
  }, `ossRelation_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
