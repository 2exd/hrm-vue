<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="留言人姓名/企业名称" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入留言人姓名/企业名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="手机号" prop="phonenumber">
              <el-input v-model="queryParams.phonenumber" placeholder="请输入手机号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="留言编号" prop="msgCode">
              <el-input v-model="queryParams.msgCode" placeholder="请输入留言编号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="处理部门" prop="handleDept">
              <el-input v-model="queryParams.handleDept" placeholder="请输入处理部门" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="处理人ID" prop="handleAdmin">
              <el-input v-model="queryParams.handleAdmin" placeholder="请输入处理人ID" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="提交成功时间" prop="submitTime">
              <el-date-picker clearable
                v-model="queryParams.submitTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择提交成功时间"
              />
            </el-form-item>
            <el-form-item label="等待处理开始时间" prop="waitHandleTime">
              <el-date-picker clearable
                v-model="queryParams.waitHandleTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择等待处理开始时间"
              />
            </el-form-item>
            <el-form-item label="电话回访时间" prop="phoneCallTime">
              <el-date-picker clearable
                v-model="queryParams.phoneCallTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择电话回访时间"
              />
            </el-form-item>
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['board:user:list']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['board:gov:update']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['board:gov:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['board:gov:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="boardList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键ID" align="center" prop="id" v-if="true" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="留言主体类型" align="center" prop="boardType" />
        <el-table-column label="留言人姓名/企业名称" align="center" prop="name" />
        <el-table-column label="手机号" align="center" prop="phonenumber" />
        <el-table-column label="留言内容" align="center" prop="content" />
        <el-table-column label="留言类型" align="center" prop="msgType" />
        <el-table-column label="留言编号" align="center" prop="msgCode" />
        <el-table-column label="处理部门" align="center" prop="handleDept" />
        <el-table-column label="处理人ID" align="center" prop="handleAdmin" />
        <el-table-column label="回复内容" align="center" prop="replyContent" />
        <el-table-column label="状态" align="center" prop="status" />
        <el-table-column label="提交成功时间" align="center" prop="submitTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.submitTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="等待处理开始时间" align="center" prop="waitHandleTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.waitHandleTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="电话回访时间" align="center" prop="phoneCallTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.phoneCallTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right"  class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:board:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:board:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改留言板对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="boardFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="留言人姓名/企业名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入留言人姓名/企业名称" />
        </el-form-item>
        <el-form-item label="手机号" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="留言内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="留言编号" prop="msgCode">
          <el-input v-model="form.msgCode" placeholder="请输入留言编号" />
        </el-form-item>
        <el-form-item label="处理部门" prop="handleDept">
          <el-input v-model="form.handleDept" placeholder="请输入处理部门" />
        </el-form-item>
        <el-form-item label="处理人ID" prop="handleAdmin">
          <el-input v-model="form.handleAdmin" placeholder="请输入处理人ID" />
        </el-form-item>
        <el-form-item label="回复内容">
          <editor v-model="form.replyContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="提交成功时间" prop="submitTime">
          <el-date-picker clearable
            v-model="form.submitTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择提交成功时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="等待处理开始时间" prop="waitHandleTime">
          <el-date-picker clearable
            v-model="form.waitHandleTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择等待处理开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="电话回访时间" prop="phoneCallTime">
          <el-date-picker clearable
            v-model="form.phoneCallTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择电话回访时间">
          </el-date-picker>
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

<script setup name="Board" lang="ts">
import { listBoard, getBoard, delBoard, addBoard, updateBoard } from '@/api/board';
import { BoardVO, BoardQuery, BoardForm } from '@/api/board/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const boardList = ref<BoardVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const boardFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: BoardForm = {
  id: undefined,
  remark: undefined,
  boardType: undefined,
  name: undefined,
  phonenumber: undefined,
  content: undefined,
  msgType: undefined,
  msgCode: undefined,
  handleDept: undefined,
  handleAdmin: undefined,
  replyContent: undefined,
  status: undefined,
  submitTime: undefined,
  waitHandleTime: undefined,
  phoneCallTime: undefined
}
const data = reactive<PageData<BoardForm, BoardQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    boardType: undefined,
    name: undefined,
    phonenumber: undefined,
    content: undefined,
    msgType: undefined,
    msgCode: undefined,
    handleDept: undefined,
    handleAdmin: undefined,
    replyContent: undefined,
    status: undefined,
    submitTime: undefined,
    waitHandleTime: undefined,
    phoneCallTime: undefined,
    params: {
    }
  },
  rules: {
    id: [
      { required: true, message: "主键ID不能为空", trigger: "blur" }
    ],
    boardType: [
      { required: true, message: "留言主体类型不能为空", trigger: "change" }
    ],
    name: [
      { required: true, message: "留言人姓名/企业名称不能为空", trigger: "blur" }
    ],
    phonenumber: [
      { required: true, message: "手机号不能为空", trigger: "blur" }
    ],
    content: [
      { required: true, message: "留言内容不能为空", trigger: "blur" }
    ],
    msgType: [
      { required: true, message: "留言类型不能为空", trigger: "change" }
    ],
    msgCode: [
      { required: true, message: "留言编号不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询留言板列表 */
const getList = async () => {
  loading.value = true;
  const res = await listBoard(queryParams.value);
  boardList.value = res.rows;
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
  boardFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: BoardVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加留言板";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: BoardVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getBoard(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改留言板";
}

/** 提交按钮 */
const submitForm = () => {
  boardFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateBoard(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addBoard(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: BoardVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除留言板编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delBoard(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('system/board/export', {
    ...queryParams.value
  }, `board_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
