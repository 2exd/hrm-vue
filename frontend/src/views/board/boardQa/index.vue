<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="关联留言主表ID" prop="boardId">
              <el-input v-model="queryParams.boardId" placeholder="请输入关联留言主表ID" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="发送人ID" prop="senderId">
              <el-input v-model="queryParams.senderId" placeholder="请输入发送人ID" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="发送人姓名" prop="senderName">
              <el-input v-model="queryParams.senderName" placeholder="请输入发送人姓名" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="发送时间" prop="sendTime">
              <el-date-picker clearable
                v-model="queryParams.sendTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择发送时间"
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['board:boardQa:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['board:boardQa:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['board:boardQa:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['board:boardQa:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="boardQaList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="问答记录ID" align="center" prop="id" v-if="true" />
        <el-table-column label="关联留言主表ID" align="center" prop="boardId" />
        <el-table-column label="消息类型" align="center" prop="qaType" />
        <el-table-column label="问答内容" align="center" prop="content" />
        <el-table-column label="发送人ID" align="center" prop="senderId" />
        <el-table-column label="发送人姓名" align="center" prop="senderName" />
        <el-table-column label="发送时间" align="center" prop="sendTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right"  class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['board:boardQa:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['board:boardQa:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改留言板多轮问答日志对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="boardQaFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联留言主表ID" prop="boardId">
          <el-input v-model="form.boardId" placeholder="请输入关联留言主表ID" />
        </el-form-item>
        <el-form-item label="问答内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="发送人ID" prop="senderId">
          <el-input v-model="form.senderId" placeholder="请输入发送人ID" />
        </el-form-item>
        <el-form-item label="发送人姓名" prop="senderName">
          <el-input v-model="form.senderName" placeholder="请输入发送人姓名" />
        </el-form-item>
        <el-form-item label="发送时间" prop="sendTime">
          <el-date-picker clearable
            v-model="form.sendTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择发送时间">
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

<script setup name="BoardQa" lang="ts">
import { listBoardQa, getBoardQa, delBoardQa, addBoardQa, updateBoardQa } from '@/api/board/boardQa';
import { BoardQaVO, BoardQaQuery, BoardQaForm } from '@/api/board/boardQa/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const boardQaList = ref<BoardQaVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const boardQaFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: BoardQaForm = {
  id: undefined,
  boardId: undefined,
  qaType: undefined,
  content: undefined,
  senderId: undefined,
  senderName: undefined,
  sendTime: undefined,
}
const data = reactive<PageData<BoardQaForm, BoardQaQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    boardId: undefined,
    qaType: undefined,
    content: undefined,
    senderId: undefined,
    senderName: undefined,
    sendTime: undefined,
    params: {
    }
  },
  rules: {
    id: [
      { required: true, message: "问答记录ID不能为空", trigger: "blur" }
    ],
    boardId: [
      { required: true, message: "关联留言主表ID不能为空", trigger: "blur" }
    ],
    qaType: [
      { required: true, message: "消息类型不能为空", trigger: "change" }
    ],
    content: [
      { required: true, message: "问答内容不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询留言板多轮问答日志列表 */
const getList = async () => {
  loading.value = true;
  const res = await listBoardQa(queryParams.value);
  boardQaList.value = res.rows;
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
  boardQaFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: BoardQaVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加留言板多轮问答日志";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: BoardQaVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getBoardQa(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改留言板多轮问答日志";
}

/** 提交按钮 */
const submitForm = () => {
  boardQaFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateBoardQa(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addBoardQa(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: BoardQaVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除留言板多轮问答日志编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delBoardQa(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('board/boardQa/export', {
    ...queryParams.value
  }, `boardQa_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
