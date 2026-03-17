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
                v-model="dateRangeInsuranceMonth"
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['money:employeeSocialInsurance:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['money:employeeSocialInsurance:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['money:employeeSocialInsurance:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['money:employeeSocialInsurance:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="employeeSocialInsuranceList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="姓名" align="center" prop="employeeName" />
        <el-table-column label="部门" align="center" prop="deptName" />
        <el-table-column label="月份" align="center" prop="insuranceMonth">
          <template #default="scope">
            <span>{{ parseTime(scope.row.insuranceMonth, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="养老保险-个人缴纳" align="center" prop="pensionPersonal" width="180" />
        <el-table-column label="养老保险-单位缴纳" align="center" prop="pensionCompany" width="180" />
        <el-table-column label="医疗保险-个人缴纳" align="center" prop="medicalPersonal" width="180" />
        <el-table-column label="医疗保险-单位缴纳" align="center" prop="medicalCompany" width="180" />
        <el-table-column label="失业保险-个人缴纳" align="center" prop="unemploymentPersonal" width="180" />
        <el-table-column label="失业保险-单位缴纳" align="center" prop="unemploymentCompany" width="180" />
<!--        <el-table-column label="工伤保险-单位缴纳" align="center" prop="injuryCompany" />-->
<!--        <el-table-column label="生育保险-单位缴纳" align="center" prop="maternityCompany" />-->
<!--        <el-table-column label="住房公积金-个人缴纳" align="center" prop="housingFundPersonal" />-->
<!--        <el-table-column label="住房公积金-单位缴纳" align="center" prop="housingFundCompany" />-->
        <el-table-column label="累计专项附加扣除-子女教育费" align="center" prop="childrenEducation" width="210" />
        <el-table-column label="累计专项附加扣除-继续教育费" align="center" prop="continuingEducation" width="210" />
        <el-table-column label="累计专项附加扣除-住房贷款" align="center" prop="housingLoan" width="210" />
        <el-table-column label="累计专项附加扣除-住房租金" align="center" prop="housingRent" width="210" />
        <el-table-column label="累计专项附加扣除-赡养老人" align="center" prop="supportElderly" width="210" />
        <el-table-column label="累计专项附加扣除-婴幼儿专项" align="center" prop="infantCare" width="210" />
        <el-table-column label="操作" align="center" fixed="right"  class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['money:employeeSocialInsurance:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['money:employeeSocialInsurance:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改员工五险一金及专项附加扣除对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="960px" append-to-body>
      <el-form ref="employeeSocialInsuranceFormRef" :model="form" :rules="rules" label-width="210px">

        <el-form-item label="姓名" prop="employeeName">
          <el-input v-model="form.employeeName" placeholder="请选择员工" @click="handleSelectClick('delegateName')" />
        </el-form-item>
        <el-form-item label="月份" prop="insuranceMonth">
          <el-date-picker clearable
            v-model="form.insuranceMonth"
            type="month"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择五险一金所属月份">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="养老保险-个人缴纳" prop="pensionPersonal">
          <el-input v-model="form.pensionPersonal" placeholder="请输入养老保险-个人缴纳" />
        </el-form-item>
        <el-form-item label="养老保险-单位缴纳" prop="pensionCompany">
          <el-input v-model="form.pensionCompany" placeholder="请输入养老保险-单位缴纳" />
        </el-form-item>
        <el-form-item label="医疗保险-个人缴纳" prop="medicalPersonal">
          <el-input v-model="form.medicalPersonal" placeholder="请输入医疗保险-个人缴纳" />
        </el-form-item>
        <el-form-item label="医疗保险-单位缴纳" prop="medicalCompany">
          <el-input v-model="form.medicalCompany" placeholder="请输入医疗保险-单位缴纳" />
        </el-form-item>
        <el-form-item label="失业保险-个人缴纳" prop="unemploymentPersonal">
          <el-input v-model="form.unemploymentPersonal" placeholder="请输入失业保险-个人缴纳" />
        </el-form-item>
        <el-form-item label="失业保险-单位缴纳" prop="unemploymentCompany">
          <el-input v-model="form.unemploymentCompany" placeholder="请输入失业保险-单位缴纳" />
        </el-form-item>
<!--        <el-form-item label="工伤保险-单位缴纳" prop="injuryCompany">-->
<!--          <el-input v-model="form.injuryCompany" placeholder="请输入工伤保险-单位缴纳" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="生育保险-单位缴纳" prop="maternityCompany">-->
<!--          <el-input v-model="form.maternityCompany" placeholder="请输入生育保险-单位缴纳" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="住房公积金-个人缴纳" prop="housingFundPersonal">-->
<!--          <el-input v-model="form.housingFundPersonal" placeholder="请输入住房公积金-个人缴纳" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="住房公积金-单位缴纳" prop="housingFundCompany">-->
<!--          <el-input v-model="form.housingFundCompany" placeholder="请输入住房公积金-单位缴纳" />-->
<!--        </el-form-item>-->
        <el-form-item label="累计专项附加扣除-子女教育费" prop="childrenEducation">
          <el-input v-model="form.childrenEducation" placeholder="请输入累计专项附加扣除-子女教育费" />
        </el-form-item>
        <el-form-item label="累计专项附加扣除-继续教育费" prop="continuingEducation">
          <el-input v-model="form.continuingEducation" placeholder="请输入累计专项附加扣除-继续教育费" />
        </el-form-item>
        <el-form-item label="累计专项附加扣除-住房贷款" prop="housingLoan">
          <el-input v-model="form.housingLoan" placeholder="请输入累计专项附加扣除-住房贷款" />
        </el-form-item>
        <el-form-item label="累计专项附加扣除-住房租金" prop="housingRent">
          <el-input v-model="form.housingRent" placeholder="请输入累计专项附加扣除-住房租金" />
        </el-form-item>
        <el-form-item label="累计专项附加扣除-赡养老人" prop="supportElderly">
          <el-input v-model="form.supportElderly" placeholder="请输入累计专项附加扣除-赡养老人" />
        </el-form-item>
        <el-form-item label="累计专项附加扣除-婴幼儿专项" prop="infantCare">
          <el-input v-model="form.infantCare" placeholder="请输入累计专项附加扣除-婴幼儿专项" />
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

<script setup name="EmployeeSocialInsurance" lang="ts">
import { listEmployeeSocialInsurance, getEmployeeSocialInsurance, delEmployeeSocialInsurance, addEmployeeSocialInsurance, updateEmployeeSocialInsurance } from '@/api/money/employeeSocialInsurance';
import { EmployeeSocialInsuranceVO, EmployeeSocialInsuranceQuery, EmployeeSocialInsuranceForm } from '@/api/money/employeeSocialInsurance/types';
import PersonnelSelection from "@/views/hrm/common/PersonnelSelection.vue";

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const employeeSocialInsuranceList = ref<EmployeeSocialInsuranceVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const dateRangeInsuranceMonth = ref<[DateModelType, DateModelType]>(['', '']);

const queryFormRef = ref<ElFormInstance>();
const employeeSocialInsuranceFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: EmployeeSocialInsuranceForm = {
  id: undefined,
  remark: undefined,
  userId: undefined,
  employeeName: undefined,
  deptId: undefined,
  deptName: undefined,
  insuranceMonth: undefined,
  pensionPersonal: undefined,
  pensionCompany: undefined,
  medicalPersonal: undefined,
  medicalCompany: undefined,
  unemploymentPersonal: undefined,
  unemploymentCompany: undefined,
  injuryCompany: undefined,
  maternityCompany: undefined,
  housingFundPersonal: undefined,
  housingFundCompany: undefined,
  childrenEducation: undefined,
  continuingEducation: undefined,
  housingLoan: undefined,
  housingRent: undefined,
  supportElderly: undefined,
  infantCare: undefined
}
const data = reactive<PageData<EmployeeSocialInsuranceForm, EmployeeSocialInsuranceQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    deptName: undefined,
    params: {
      insuranceMonth: undefined,
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
    insuranceMonth: [
      { required: true, message: "五险一金所属月份不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询员工五险一金及专项附加扣除列表 */
const getList = async () => {
  loading.value = true;
  queryParams.value.params = {};
  proxy?.addDateRange(queryParams.value, dateRangeInsuranceMonth.value, 'InsuranceMonth');
  const res = await listEmployeeSocialInsurance(queryParams.value);
  employeeSocialInsuranceList.value = res.rows;
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
  employeeSocialInsuranceFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
const resetQuery = () => {
  dateRangeInsuranceMonth.value = ['', ''];
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: EmployeeSocialInsuranceVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加员工五险一金及专项附加扣除";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: EmployeeSocialInsuranceVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getEmployeeSocialInsurance(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改员工五险一金及专项附加扣除";
}

/** 提交按钮 */
const submitForm = () => {
  employeeSocialInsuranceFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateEmployeeSocialInsurance(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addEmployeeSocialInsurance(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: EmployeeSocialInsuranceVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除员工五险一金及专项附加扣除编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delEmployeeSocialInsurance(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('money/employeeSocialInsurance/export', {
    ...queryParams.value
  }, `employeeSocialInsurance_${new Date().getTime()}.xlsx`)
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
