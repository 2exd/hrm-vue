<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="员工姓名" prop="employeeName">
              <el-input v-model="queryParams.employeeName" placeholder="请输入员工姓名" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="部门名称" prop="deptName">
              <el-input v-model="queryParams.deptName" placeholder="请输入所属部门名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="薪资月份" style="width: 308px">
              <el-date-picker
                v-model="dateRangeSalaryMonth"
                value-format="YYYY-MM-DD HH:mm:ss"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['money:employeeSalary:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['money:employeeSalary:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['money:employeeSalary:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['money:employeeSalary:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="employeeSalaryList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
<!--        <el-table-column label="主键ID" align="center" prop="id" v-if="true" />-->
        <el-table-column label="员工姓名" align="center" prop="employeeName" />
        <el-table-column label="部门名称" align="center" prop="deptName" />
        <el-table-column label="薪资月份" align="center" prop="salaryMonth" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.salaryMonth, '{y}-{m}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="基薪" align="center" prop="basicSalary" />
        <el-table-column label="月绩效工资" align="center" prop="monthlyPerformanceSalary" />
        <el-table-column label="证书费" align="center" prop="certificateFee" />
        <el-table-column label="岗位工资" align="center" prop="postSalary" />
        <el-table-column label="加班工资" align="center" prop="overtimeSalary" />
        <el-table-column label="通讯补助" align="center" prop="communicationSubsidy" />
        <el-table-column label="交通补助" align="center" prop="trafficSubsidy" />
        <el-table-column label="餐补/误餐" align="center" prop="mealSubsidy" />
        <el-table-column label="其他加款" align="center" prop="otherAdditional" />

        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" fixed="right"  class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['money:employeeSalary:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['money:employeeSalary:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改员工薪资对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="960px" append-to-body>
      <el-form ref="employeeSalaryFormRef" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="员工姓名" prop="employeeName">
          <el-input v-model="form.employeeName" placeholder="请选择员工" @click="handleSelectClick('delegateName')" />
        </el-form-item>
        <el-form-item label="薪资月份" prop="salaryMonth">
          <el-date-picker clearable
                          v-model="form.salaryMonth"
                          type="month"
                          value-format="YYYY-MM-DD HH:mm:ss"
                          placeholder="请选择薪资月份">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="基薪" prop="basicSalary">
          <el-input v-model="form.basicSalary" placeholder="请输入基薪" />
        </el-form-item>
        <el-form-item label="月绩效工资" prop="monthlyPerformanceSalary">
          <el-input v-model="form.monthlyPerformanceSalary" placeholder="请输入月绩效工资" />
        </el-form-item>
        <el-form-item label="证书费" prop="certificateFee">
          <el-input v-model="form.certificateFee" placeholder="请输入证书费" />
        </el-form-item>
        <el-form-item label="岗位工资" prop="postSalary">
          <el-input v-model="form.postSalary" placeholder="请输入岗位工资" />
        </el-form-item>
        <el-form-item label="加班工资" prop="overtimeSalary">
          <el-input v-model="form.overtimeSalary" placeholder="请输入加班工资" />
        </el-form-item>
        <el-form-item label="通讯补助" prop="communicationSubsidy">
          <el-input v-model="form.communicationSubsidy" placeholder="请输入通讯补助" />
        </el-form-item>
        <el-form-item label="交通补助" prop="trafficSubsidy">
          <el-input v-model="form.trafficSubsidy" placeholder="请输入交通补助" />
        </el-form-item>
        <el-form-item label="餐补/误餐" prop="mealSubsidy">
          <el-input v-model="form.mealSubsidy" placeholder="请输入餐补/误餐" />
        </el-form-item>
        <el-form-item label="其他加款" prop="otherAdditional">
          <el-input v-model="form.otherAdditional" placeholder="请输入其他加款" />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>


    <PersonnelSelection ref="personnelSelectionRef" @confirm="handleConfirm" />
  </div>
</template>

<script setup name="EmployeeSalary" lang="ts">
import { listEmployeeSalary, getEmployeeSalary, delEmployeeSalary, addEmployeeSalary, updateEmployeeSalary } from '@/api/money/employeeSalary';
import { EmployeeSalaryVO, EmployeeSalaryQuery, EmployeeSalaryForm } from '@/api/money/employeeSalary/types';
import PersonnelSelection from "@/views/hrm/common/PersonnelSelection.vue";
import { ref } from "vue";

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const employeeSalaryList = ref<EmployeeSalaryVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const dateRangeSalaryMonth = ref<[DateModelType, DateModelType]>(['', '']);

const queryFormRef = ref<ElFormInstance>();
const employeeSalaryFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: EmployeeSalaryForm = {
  id: undefined,
  remark: undefined,
  userId: undefined,
  employeeName: undefined,
  deptId: undefined,
  deptName: undefined,
  basicSalary: undefined,
  monthlyPerformanceSalary: undefined,
  certificateFee: undefined,
  postSalary: undefined,
  overtimeSalary: undefined,
  communicationSubsidy: undefined,
  trafficSubsidy: undefined,
  mealSubsidy: undefined,
  otherAdditional: undefined,
  salaryMonth: undefined
}
const data = reactive<PageData<EmployeeSalaryForm, EmployeeSalaryQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    employeeName: undefined,
    deptName: undefined,
    params: {
      salaryMonth: undefined
    }
  },
  rules: {
    id: [
      { required: true, message: "主键ID不能为空", trigger: "blur" }
    ],
    userId: [
      { required: true, message: "员工ID不能为空", trigger: "blur" }
    ],
    employeeName: [
      { required: true, message: "员工姓名不能为空", trigger: "blur" }
    ],
    salaryMonth: [
      { required: true, message: "薪资月份不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询员工薪资列表 */
const getList = async () => {
  loading.value = true;
  queryParams.value.params = {};
  proxy?.addDateRange(queryParams.value, dateRangeSalaryMonth.value, 'SalaryMonth');
  const res = await listEmployeeSalary(queryParams.value);
  employeeSalaryList.value = res.rows;
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
  employeeSalaryFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
const resetQuery = () => {
  dateRangeSalaryMonth.value = ['', ''];
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: EmployeeSalaryVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加员工薪资";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: EmployeeSalaryVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getEmployeeSalary(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改员工薪资";
}

/** 提交按钮 */
const submitForm = () => {
  employeeSalaryFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateEmployeeSalary(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addEmployeeSalary(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: EmployeeSalaryVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除员工薪资编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delEmployeeSalary(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('money/employeeSalary/export', {
    ...queryParams.value
  }, `employeeSalary_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});

//? 自定义
const personnelSelectionRef = ref<any>();
const activeSelectField = ref<string>(''); // 记录当前激活的

const handleSelectClick = (field: string) => {
  activeSelectField.value = field; // 记录当前字段
  personnelSelectionRef.value.drawer = true; // 打开抽屉

  //? 绑定信息
};

//? 确认选择人员的处理
const handleConfirm = (selectedUser: any) => {
  console.log(selectedUser);

  //? 绑定选中的用户信息到表单
  form.value.userId = selectedUser.userId;
  form.value.employeeName = selectedUser.userName;
  form.value.deptId = selectedUser.deptId;
  form.value.deptName = selectedUser.deptName;

  activeSelectField.value = ''; // 清空记录
  personnelSelectionRef.value.drawer = false;
};
</script>
