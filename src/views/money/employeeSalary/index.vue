<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="姓名" prop="employeeName">
              <el-input v-model="queryParams.employeeName" placeholder="请输入员工姓名" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="部门" prop="deptName">
              <el-input v-model="queryParams.deptName" placeholder="请输入所属部门名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="月份" style="width: 308px">
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
        <el-table-column label="姓名" align="center" prop="employeeName" />
        <el-table-column label="部门" align="center" prop="deptName" />
        <el-table-column label="月份" align="center" prop="salaryMonth" width="100">
          <template #default="scope">
            <span>{{ parseTime(scope.row.salaryMonth, '{y}-{m}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="当月总工资" align="center" width="120">
          <template #default="scope">
            <el-tag type="success" effect="dark">
              ¥ {{ calculateTotalSalary(scope.row).toFixed(2) }}
            </el-tag>
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
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="800px" append-to-body>
      <el-form ref="employeeSalaryFormRef" :model="form" :rules="rules" label-width="120px" class="salary-form">
        <!-- 基本信息 -->
        <div class="form-section">
          <div class="section-title">
            <el-icon><User /></el-icon>
            <span>基本信息</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="员工姓名" prop="employeeName">
                <el-input v-model="form.employeeName" placeholder="请选择员工" @click="isEdit ? null : handleSelectClick('delegateName')" readonly :disabled="isEdit">
                  <template #suffix>
                    <el-icon><Search /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="薪资月份" prop="salaryMonth">
                <el-date-picker
                  v-model="form.salaryMonth"
                  type="month"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  placeholder="请选择月份"
                  style="width: 100%"
                  :disabled="isEdit"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 基本薪资 -->
        <div class="form-section">
          <div class="section-title">
            <el-icon><Money /></el-icon>
            <span>基本薪资</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="基薪" prop="basicSalary">
                <el-input-number v-model="form.basicSalary" :precision="2" :min="0" :controls="false" placeholder="请输入基薪" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="绩效工资" prop="monthlyPerformanceSalary">
                <el-input-number v-model="form.monthlyPerformanceSalary" :precision="2" :min="0" :controls="false" placeholder="请输入绩效工资" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="岗位工资" prop="postSalary">
                <el-input-number v-model="form.postSalary" :precision="2" :min="0" :controls="false" placeholder="请输入岗位工资" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="证书费" prop="certificateFee">
                <el-input-number v-model="form.certificateFee" :precision="2" :min="0" :controls="false" placeholder="请输入证书费" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 加班及补助 -->
        <div class="form-section">
          <div class="section-title">
            <el-icon><Wallet /></el-icon>
            <span>加班及补助</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="加班工资" prop="overtimeSalary">
                <el-input-number v-model="form.overtimeSalary" :precision="2" :min="0" :controls="false" placeholder="请输入加班工资" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="通讯补助" prop="communicationSubsidy">
                <el-input-number v-model="form.communicationSubsidy" :precision="2" :min="0" :controls="false" placeholder="请输入通讯补助" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="交通补助" prop="trafficSubsidy">
                <el-input-number v-model="form.trafficSubsidy" :precision="2" :min="0" :controls="false" placeholder="请输入交通补助" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="餐补/误餐" prop="mealSubsidy">
                <el-input-number v-model="form.mealSubsidy" :precision="2" :min="0" :controls="false" placeholder="请输入餐补" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 其他 -->
        <div class="form-section">
          <div class="section-title">
            <el-icon><Plus /></el-icon>
            <span>其他</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="其他加款" prop="otherAdditional">
                <el-input-number v-model="form.otherAdditional" :precision="2" :min="0" :controls="false" placeholder="请输入其他加款" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="备注" prop="remark">
                <el-input v-model="form.remark" placeholder="请输入备注" maxlength="200" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 当月总工资（只读，自动计算） -->
        <div class="form-section total-salary-section">
          <div class="section-title">
            <el-icon><Coin /></el-icon>
            <span>当月总工资</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="当月总工资">
                <div class="total-salary-display">
                  <span class="amount">¥ {{ totalSalary }}</span>
                  <span class="formula"> = 基薪 + 绩效 + 岗位 + 证书费 + 加班 + 通讯 + 交通 + 餐补 + 其他</span>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
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
import { ref, computed } from "vue";
import { User, Money, Wallet, Plus, Search, Coin } from '@element-plus/icons-vue';

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
  basicSalary: 0,
  monthlyPerformanceSalary: 0,
  certificateFee: 0,
  postSalary: 0,
  overtimeSalary: 0,
  communicationSubsidy: 0,
  trafficSubsidy: 0,
  mealSubsidy: 0,
  otherAdditional: 0,
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

/** 是否为编辑模式 */
const isEdit = computed(() => !!form.value.id);

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

/** 计算总薪资（弹窗中使用） */
const totalSalary = computed(() => {
  const basic = Number(form.value.basicSalary) || 0;
  const performance = Number(form.value.monthlyPerformanceSalary) || 0;
  const certificate = Number(form.value.certificateFee) || 0;
  const post = Number(form.value.postSalary) || 0;
  const overtime = Number(form.value.overtimeSalary) || 0;
  const communication = Number(form.value.communicationSubsidy) || 0;
  const traffic = Number(form.value.trafficSubsidy) || 0;
  const meal = Number(form.value.mealSubsidy) || 0;
  const other = Number(form.value.otherAdditional) || 0;
  return (basic + performance + certificate + post + overtime + communication + traffic + meal + other).toFixed(2);
});

/** 计算列表行总薪资 */
const calculateTotalSalary = (row: EmployeeSalaryVO) => {
  // 如果后端已经返回了 totalSalary，直接使用
  if (row.totalSalary !== undefined && row.totalSalary !== null) {
    return Number(row.totalSalary);
  }
  // 否则前端计算
  const basic = Number(row.basicSalary) || 0;
  const performance = Number(row.monthlyPerformanceSalary) || 0;
  const certificate = Number(row.certificateFee) || 0;
  const post = Number(row.postSalary) || 0;
  const overtime = Number(row.overtimeSalary) || 0;
  const communication = Number(row.communicationSubsidy) || 0;
  const traffic = Number(row.trafficSubsidy) || 0;
  const meal = Number(row.mealSubsidy) || 0;
  const other = Number(row.otherAdditional) || 0;
  return basic + performance + certificate + post + overtime + communication + traffic + meal + other;
};

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

<style lang="scss" scoped>
.salary-form {
  .form-section {
    margin-bottom: 24px;
    padding: 16px;
    background-color: #f8f9fa;
    border-radius: 8px;
    border-left: 4px solid var(--el-color-primary);

    &:last-child {
      margin-bottom: 0;
    }

    .section-title {
      display: flex;
      align-items: center;
      font-size: 15px;
      font-weight: 600;
      color: var(--el-text-color-primary);
      margin-bottom: 16px;
      padding-bottom: 8px;
      border-bottom: 1px solid var(--el-border-color-lighter);

      .el-icon {
        margin-right: 8px;
        font-size: 18px;
        color: var(--el-color-primary);
      }
    }
  }

  :deep(.el-input-number .el-input__inner) {
    text-align: left;
  }

  :deep(.el-form-item) {
    margin-bottom: 18px;
  }
}

// 当月总工资显示区域
.total-salary-section {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  border-left: 4px solid #f5576c;

  .section-title {
    color: #fff;
    border-bottom: 1px solid rgba(255, 255, 255, 0.3);

    .el-icon {
      color: #fff;
    }
  }

  .total-salary-display {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    padding: 12px 16px;
    background: rgba(255, 255, 255, 0.95);
    border-radius: 8px;
    width: 100%;

    .amount {
      font-size: 32px;
      font-weight: bold;
      color: #f5576c;
      margin-bottom: 8px;
    }

    .formula {
      font-size: 12px;
      color: #909399;
    }
  }
}
</style>
