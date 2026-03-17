<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card class="tw-relative" shadow="hover">
          <el-form ref="queryFormRef" :inline="true" :model="queryParams">
            <!--            <el-form-item label="系统用户ID" prop="userId">-->
            <!--              <el-input v-model="queryParams.userId" placeholder="请输入系统用户ID" clearable @keyup.enter="handleQuery" />-->
            <!--            </el-form-item>-->
            <el-form-item label="员工姓名" prop="employeeName">
              <el-input v-model="queryParams.employeeName" clearable placeholder="请输入员工姓名" @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="queryParams.idCard" clearable placeholder="请输入身份证号" @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="电话号码" prop="phone">
              <el-input v-model="queryParams.phone" clearable placeholder="请输入电话号码" @keyup.enter="handleQuery" />
            </el-form-item>
            <!--            <el-form-item label="家庭住址" prop="homeAddress">-->
            <!--              <el-input v-model="queryParams.homeAddress" placeholder="请输入家庭住址" clearable @keyup.enter="handleQuery" />-->
            <!--            </el-form-item>-->
            <!--            <el-form-item label="紧急联系人姓名" prop="emergencyContact">-->
            <!--              <el-input v-model="queryParams.emergencyContact" placeholder="请输入紧急联系人姓名" clearable @keyup.enter="handleQuery" />-->
            <!--            </el-form-item>-->
            <!--            <el-form-item label="紧急联系人电话" prop="emergencyContactPhone">-->
            <!--              <el-input v-model="queryParams.emergencyContactPhone" placeholder="请输入紧急联系人电话" clearable @keyup.enter="handleQuery" />-->
            <!--            </el-form-item>-->
            <!--            <el-form-item label="银行卡号" prop="bankCard">-->
            <!--              <el-input v-model="queryParams.bankCard" placeholder="请输入银行卡号" clearable @keyup.enter="handleQuery" />-->
            <!--            </el-form-item>-->
            <!--            <el-form-item label="银行卡行号" prop="bankBranchCode">-->
            <!--              <el-input v-model="queryParams.bankBranchCode" placeholder="请输入银行卡行号" clearable @keyup.enter="handleQuery" />-->
            <!--            </el-form-item>-->
            <!--            <el-form-item label="开户行名称" prop="bankName">-->
            <!--              <el-input v-model="queryParams.bankName" placeholder="请输入开户行名称" clearable @keyup.enter="handleQuery" />-->
            <!--            </el-form-item>-->
            <!--            <el-form-item label="日薪" prop="dailySalary">-->
            <!--              <el-input v-model="queryParams.dailySalary" clearable placeholder="请输入日薪" @keyup.enter="handleQuery" />-->
            <!--            </el-form-item>-->
            <!--            <el-form-item label="月薪" prop="monthlySalary">-->
            <!--              <el-input v-model="queryParams.monthlySalary" clearable placeholder="请输入月薪" @keyup.enter="handleQuery" />-->
            <!--            </el-form-item>-->
            <el-form-item label="签订时间" style="width: 308px">
              <el-date-picker
                v-model="dateRangeContractSignTime"
                :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
                end-placeholder="结束日期"
                range-separator="-"
                start-placeholder="开始日期"
                type="daterange"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
            <el-form-item label="合同编码" prop="contractCode">
              <el-input v-model="queryParams.contractCode" clearable placeholder="请输入劳动合同编码" @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="到期时间" style="width: 308px">
              <el-date-picker
                v-model="dateRangeContractExpireTime"
                :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
                end-placeholder="结束日期"
                range-separator="-"
                start-placeholder="开始日期"
                type="daterange"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
            <el-form-item label="退场时间" style="width: 308px">
              <el-date-picker
                v-model="dateRangeExitTime"
                :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
                end-placeholder="结束日期"
                range-separator="-"
                start-placeholder="开始日期"
                type="daterange"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </el-form>
          <div class="tw-absolute tw-bottom-4 tw-right-4 tw-flex tw-gap-1">
            <el-button icon="Search" type="primary" @click="handleQuery">搜索 </el-button>
            <el-button icon="Refresh" @click="resetQuery">重置 </el-button>
          </div>
        </el-card>
      </div>
    </transition>

    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button v-hasPermi="['hrm:employeeRoster:add']" icon="Plus" plain type="primary" @click="handleAdd">新增 </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['hrm:employeeRoster:edit']" :disabled="single" icon="Edit" plain type="success" @click="handleUpdate()"
              >修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['hrm:employeeRoster:remove']" :disabled="multiple" icon="Delete" plain type="danger" @click="handleDelete()"
              >删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['hrm:employeeRoster:export']" icon="Download" plain type="warning" @click="handleExport">导出 </el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="employeeRosterList" border @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="55" />
        <!--        <el-table-column label="主键ID" align="center" prop="id" v-if="true" />-->
        <!--        <el-table-column label="系统用户ID" align="center" prop="userId" />-->
        <el-table-column align="center" label="员工姓名" prop="employeeName" />
        <el-table-column align="center" label="身份证号" prop="idCard" width="200" />
        <el-table-column align="center" label="电话号码" prop="phone" width="150" />
        <el-table-column align="center" label="家庭住址" prop="homeAddress" width="200">
          <template v-slot="scope">
            <el-tooltip :content="scope?.row?.homeAddress || '无'" class="item" effect="dark" placement="top-start">
              <div class="whitespace-nowrap overflow-hidden text-ellipsis w-full cursor-pointer">
                {{ scope?.row?.homeAddress || '无' }}
              </div>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column align="center" label="紧急联系人" prop="emergencyContact" width="100" />
        <el-table-column align="center" label="紧急联系人电话" prop="emergencyContactPhone" width="150" />
        <!--        <el-table-column label="银行卡号" align="center" prop="bankCard" />-->
        <!--        <el-table-column label="银行卡行号" align="center" prop="bankBranchCode" />-->
        <!--        <el-table-column label="开户行名称" align="center" prop="bankName" />-->
        <!--        <el-table-column label="日薪" align="center" prop="dailySalary" />-->
        <!--        <el-table-column label="月薪" align="center" prop="monthlySalary" />-->
        <el-table-column align="center" label="劳动合同编码" prop="contractCode" width="150" />
        <el-table-column align="center" label="劳动合同签订时间" prop="contractSignTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.contractSignTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="劳动合同到期时间" prop="contractExpireTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.contractExpireTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="退场时间" prop="exitTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.exitTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="备注" prop="remark" />
        <el-table-column align="center" label="附件" prop="files" width="80">
          <template #default="scope">
            <el-tag v-if="scope.row.files && scope.row.files.length > 0" type="success">
              {{ scope.row.files.length }}个
            </el-tag>
            <el-tag v-else type="info">无</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" class-name="small-padding fixed-width" fixed="right" label="操作">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['hrm:employeeRoster:edit']" icon="Edit" link type="primary" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['hrm:employeeRoster:remove']" icon="Delete" link type="primary" @click="handleDelete(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" v-model:limit="queryParams.pageSize" v-model:page="queryParams.pageNum" :total="total" @pagination="getList" />
    </el-card>
    <!-- 添加或修改员工花名册对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" append-to-body width="960px">
      <el-form ref="employeeRosterFormRef" :model="form" :rules="rules" label-width="150px">
        <!--        <el-form-item label="系统用户ID" prop="userId">-->
        <!--          <el-input v-model="form.userId" placeholder="请输入系统用户ID" />-->
        <!--        </el-form-item>-->
        <el-form-item label="员工姓名" prop="employeeName">
          <el-input v-model="form.employeeName" placeholder="请选择员工" @click="handleSelectClick('delegateName')" />
        </el-form-item>

        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="电话号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话号码" />
        </el-form-item>
        <el-form-item label="家庭住址" prop="homeAddress">
          <el-input v-model="form.homeAddress" placeholder="请输入家庭住址" />
        </el-form-item>
        <el-form-item label="紧急联系人姓名" prop="emergencyContact">
          <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人姓名" />
        </el-form-item>
        <el-form-item label="紧急联系人电话" prop="emergencyContactPhone">
          <el-input v-model="form.emergencyContactPhone" placeholder="请输入紧急联系人电话" />
        </el-form-item>
        <el-form-item label="银行卡号" prop="bankCard">
          <el-input v-model="form.bankCard" placeholder="请输入银行卡号" />
        </el-form-item>
        <el-form-item label="银行卡行号" prop="bankBranchCode">
          <el-input v-model="form.bankBranchCode" placeholder="请输入银行卡行号" />
        </el-form-item>
        <el-form-item label="开户行名称" prop="bankName">
          <el-input v-model="form.bankName" placeholder="请输入开户行名称" />
        </el-form-item>
        <el-form-item label="日薪" prop="dailySalary">
          <el-input v-model="form.dailySalary" placeholder="请输入日薪" />
        </el-form-item>
        <el-form-item label="月薪" prop="monthlySalary">
          <el-input v-model="form.monthlySalary" placeholder="请输入月薪" />
        </el-form-item>
        <el-form-item label="劳动合同签订时间" prop="contractSignTime">
          <el-date-picker
            v-model="form.contractSignTime"
            clearable
            placeholder="请选择劳动合同签订时间"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="劳动合同编码" prop="contractCode">
          <el-input v-model="form.contractCode" placeholder="请输入劳动合同编码" />
        </el-form-item>
        <el-form-item label="劳动合同到期时间" prop="contractExpireTime">
          <el-date-picker
            v-model="form.contractExpireTime"
            clearable
            placeholder="请选择劳动合同到期时间"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="退场时间" prop="exitTime">
          <el-date-picker v-model="form.exitTime" clearable placeholder="请选择退场时间" type="datetime" value-format="YYYY-MM-DD HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入内容" type="textarea" />
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button v-hasPermi="['system:oss:upload']" type="primary" plain icon="Upload" @click="handleFile">上传文件</el-button>
        </el-col>
      </el-row>

      <!-- 已上传文件列表 -->
      <div v-if="fileList.length > 0" class="file-list-container">
        <div class="file-list-title">已上传文件：</div>
        <el-table :data="fileList" border size="small" style="width: 100%">
          <el-table-column label="文件名" prop="originalName" show-overflow-tooltip />
<!--          <el-table-column label="上传人" prop="createByName" width="100" />-->
          <el-table-column label="上传时间" prop="createTime" width="150" />
          <el-table-column label="操作" width="150" align="center">
            <template #default="scope">
              <el-button link type="primary" size="small" @click="handlePreview(scope.row)">预览</el-button>
              <el-button 
                v-if="scope.row.ossId" 
                link 
                type="primary" 
                size="small" 
                @click="handleDownload(scope.row)"
              >下载</el-button>
              <el-button link type="danger" size="small" @click="handleDeleteFile(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-else class="file-list-empty">
        <el-empty description="暂无附件" :image-size="60" />
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定 </el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <PersonnelSelection ref="personnelSelectionRef" @confirm="handleConfirm" />

    <!-- 添加或修改OSS对象存储对话框 -->
    <el-dialog v-model="ossDialog.visible" :title="ossDialog.title" width="500px" append-to-body>
      <el-form ref="ossFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件名">
          <commonFileUpload v-model="ossForm.file" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="ossConfirm">确 定</el-button>
          <el-button @click="ossCancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 文件预览对话框 -->
    <el-dialog v-model="previewDialog.visible" :title="previewDialog.title" width="80%" top="5vh" append-to-body destroy-on-close class="file-preview-dialog">
      <div class="preview-container">
        <!-- 图片预览 -->
        <div v-if="isImage(previewFile?.fileSuffix)" class="preview-image">
          <img :src="previewFile?.url" style="max-width: 100%; max-height: 70vh; display: block; margin: 0 auto;" />
        </div>
        
        <!-- PDF 预览 -->
        <div v-else-if="isPdf(previewFile?.fileSuffix)" class="preview-pdf">
          <iframe :src="previewFile?.url" width="100%" style="border: none;"></iframe>
        </div>
        
        <!-- Office 文档预览（提示下载查看） -->
        <div v-else-if="isOffice(previewFile?.fileSuffix)" class="preview-office">
          <el-empty description="Office 文档请下载后查看">
            <template #default>
              <p style="color: #909399; margin: 10px 0;">文件名：{{ previewFile?.originalName }}</p>
              <p style="color: #909399; margin: 5px 0; font-size: 12px;">支持格式：Word、Excel、PPT</p>
              <el-button v-if="previewFile?.ossId" type="primary" @click="handleDownload(previewFile)">
                <el-icon><Download /></el-icon> 下载文档
              </el-button>
            </template>
          </el-empty>
        </div>
        
        <!-- 文本文件预览 -->
        <div v-else-if="isText(previewFile?.fileSuffix)" class="preview-text">
          <pre style="max-height: 70vh; overflow: auto; background: #f5f7fa; padding: 15px; border-radius: 4px;">{{ previewContent }}</pre>
        </div>
        
        <!-- 不支持的文件类型 -->
        <div v-else class="preview-unsupported">
          <el-empty description="暂不支持该文件类型的预览">
            <template #default>
              <p style="color: #909399; margin: 10px 0;">文件类型：{{ previewFile?.fileSuffix }}</p>
              <el-button v-if="previewFile?.ossId" type="primary" @click="handleDownload(previewFile)">下载文件查看</el-button>
            </template>
          </el-empty>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" name="EmployeeRoster" setup>
import { addEmployeeRoster, delEmployeeRoster, getEmployeeRoster, listEmployeeRoster, updateEmployeeRoster } from '@/api/hrm/employeeRoster';
import { EmployeeRosterForm, EmployeeRosterQuery, EmployeeRosterVO, EmployeeFile } from '@/api/hrm/employeeRoster/types';
import { parseTime } from '@/utils/ruoyi';
import { reactive, ref } from 'vue';
import PersonnelSelection from '@/views/hrm/common/personnelSelection.vue';
import { OssForm, OssQuery } from "@/api/system/oss/types";

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const employeeRosterList = ref<EmployeeRosterVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const dateRangeContractSignTime = ref<[DateModelType, DateModelType]>(['', '']);
const dateRangeContractExpireTime = ref<[DateModelType, DateModelType]>(['', '']);
const dateRangeExitTime = ref<[DateModelType, DateModelType]>(['', '']);

const queryFormRef = ref<ElFormInstance>();
const employeeRosterFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: EmployeeRosterForm = {
  id: undefined,
  userId: undefined,
  employeeName: undefined,
  idCard: undefined,
  phone: undefined,
  homeAddress: undefined,
  emergencyContact: undefined,
  emergencyContactPhone: undefined,
  bankCard: undefined,
  bankBranchCode: undefined,
  bankName: undefined,
  dailySalary: undefined,
  monthlySalary: undefined,
  contractSignTime: undefined,
  contractCode: undefined,
  contractExpireTime: undefined,
  exitTime: undefined,
  remark: undefined,
  file: undefined
};
const data = reactive<PageData<EmployeeRosterForm, EmployeeRosterQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userId: undefined,
    employeeName: undefined,
    idCard: undefined,
    phone: undefined,
    homeAddress: undefined,
    emergencyContact: undefined,
    emergencyContactPhone: undefined,
    bankCard: undefined,
    bankBranchCode: undefined,
    bankName: undefined,
    dailySalary: undefined,
    monthlySalary: undefined,
    contractCode: undefined,
    params: {
      contractSignTime: undefined,
      contractExpireTime: undefined,
      exitTime: undefined
    }
  },
  rules: {
    id: [{ required: true, message: '主键ID不能为空', trigger: 'blur' }],
    userId: [
      {
        required: true,
        message: '系统用户ID不能为空',
        trigger: 'blur'
      }
    ],
    employeeName: [
      {
        required: true,
        message: '员工姓名不能为空',
        trigger: 'blur'
      }
    ],
    idCard: [{ required: true, message: '身份证号不能为空', trigger: 'blur' }],
    contractSignTime: [
      {
        required: true,
        message: '劳动合同签订时间不能为空',
        trigger: 'blur'
      }
    ],
    contractCode: [
      {
        required: true,
        message: '劳动合同编码不能为空',
        trigger: 'blur'
      }
    ],
    contractExpireTime: [
      {
        required: true,
        message: '劳动合同到期时间不能为空',
        trigger: 'blur'
      }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询员工花名册列表 */
const getList = async () => {
  loading.value = true;
  queryParams.value.params = {};
  proxy?.addDateRange(queryParams.value, dateRangeContractSignTime.value, 'ContractSignTime');
  proxy?.addDateRange(queryParams.value, dateRangeContractExpireTime.value, 'ContractExpireTime');
  proxy?.addDateRange(queryParams.value, dateRangeExitTime.value, 'ExitTime');
  const res = await listEmployeeRoster(queryParams.value);
  employeeRosterList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};

/** 取消按钮 */
const cancel = () => {
  reset();
  fileList.value = [];
  dialog.visible = false;
};

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  employeeRosterFormRef.value?.resetFields();
};

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};

/** 重置按钮操作 */
const resetQuery = () => {
  dateRangeContractSignTime.value = ['', ''];
  dateRangeContractExpireTime.value = ['', ''];
  dateRangeExitTime.value = ['', ''];
  queryFormRef.value?.resetFields();
  handleQuery();
};

/** 多选框选中数据 */
const handleSelectionChange = (selection: EmployeeRosterVO[]) => {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  fileList.value = [];
  dialog.visible = true;
  dialog.title = '添加员工花名册';
};

/** 修改按钮操作 */
const handleUpdate = async (row?: EmployeeRosterVO) => {
  reset();
  const _id = row?.id || ids.value[0];
  const res = await getEmployeeRoster(_id);
  console.log('详情接口返回:', res);
  // 获取实际的业务数据（在 res.data 中）
  const resData = res.data || res;
  Object.assign(form.value, resData);
  // 加载文件列表 - 优先使用列表传入的数据，否则使用详情接口返回的数据
  fileList.value = row?.files || resData.files || [];
  console.log('文件列表:', fileList.value);
  dialog.visible = true;
  dialog.title = '修改员工花名册';
};

/** 提交按钮 */
const submitForm = () => {
  employeeRosterFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateEmployeeRoster(form.value).finally(() => (buttonLoading.value = false));
      } else {
        await addEmployeeRoster(form.value).finally(() => (buttonLoading.value = false));
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

/** 删除按钮操作 */
const handleDelete = async (row?: EmployeeRosterVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除员工花名册编号为"' + _ids + '"的数据项？').finally(() => (loading.value = false));
  await delEmployeeRoster(_ids);
  proxy?.$modal.msgSuccess('删除成功');
  await getList();
};

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'hrm/employeeRoster/export',
    {
      ...queryParams.value
    },
    `employeeRoster_${new Date().getTime()}.xlsx`
  );
};

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
  form.value.idCard = selectedUser.idCard;
  form.value.phone = selectedUser.phonenumber;

  activeSelectField.value = ''; // 清空记录
  personnelSelectionRef.value.drawer = false;
};

//? 生命周期
onMounted(() => {
  getList();
});

//? 文件上传相关
const fileList = ref<EmployeeFile[]>([]);
const ossDialog = reactive<DialogOption>({
  visible: false,
  title: ''
});
const type = ref(0);
const ossFormRef = ref<ElFormInstance>();
const initOssFormData = {
  file: undefined
};
const ossForm = ref({ ...initOssFormData });

//? 文件预览相关
const previewDialog = reactive<DialogOption>({
  visible: false,
  title: '文件预览'
});
const previewFile = ref<EmployeeFile | null>(null);
const previewContent = ref<string>('');

// 图片类型
const imageTypes = ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.webp', '.svg'];
// PDF类型
const pdfTypes = ['.pdf'];
// Office类型
const officeTypes = ['.doc', '.docx', '.xls', '.xlsx', '.ppt', '.pptx'];
// 文本类型
const textTypes = ['.txt', '.md', '.json', '.xml', '.html', '.htm', '.css', '.js', '.ts', '.vue'];

/** 判断是否为图片 */
const isImage = (suffix?: string) => {
  if (!suffix) return false;
  return imageTypes.includes(suffix.toLowerCase());
};

/** 判断是否为PDF */
const isPdf = (suffix?: string) => {
  if (!suffix) return false;
  return pdfTypes.includes(suffix.toLowerCase());
};

/** 判断是否为Office文档 */
const isOffice = (suffix?: string) => {
  if (!suffix) return false;
  return officeTypes.includes(suffix.toLowerCase());
};

/** 判断是否为文本文件 */
const isText = (suffix?: string) => {
  if (!suffix) return false;
  return textTypes.includes(suffix.toLowerCase());
};

/** 预览文件 */
const handlePreview = async (row: EmployeeFile) => {
  previewFile.value = row;
  previewDialog.title = `预览 - ${row.originalName}`;
  previewDialog.visible = true;
  
  // 如果是文本文件，加载内容
  if (isText(row.fileSuffix)) {
    try {
      const response = await fetch(row.url);
      previewContent.value = await response.text();
    } catch (error) {
      previewContent.value = '加载文件内容失败';
    }
  }
};

// OSS表单验证规则
const ossRules = reactive({
  file: [{ required: true, message: '文件不能为空', trigger: 'blur' }]
});
/** 文件按钮操作 */
const handleFile = () => {
  // ossReset();
  type.value = 0;
  ossDialog.visible = true;
  ossDialog.title = '上传文件';
  console.log("文件列表：", ossForm.value);
};

/** 确认按钮 */
function ossConfirm() {
  console.log("上传文件：", ossForm.value);
  ossDialog.visible = false;

  // 将上传的文件ID赋值给表单
  form.value.file = ossForm.value.file

  // 将新上传的文件添加到文件列表显示（通过uploadList缓存）
  // 实际文件列表会在保存后从后端重新加载
  proxy?.$modal.msgSuccess('文件上传成功，保存后生效');
}

/** 取消按钮 */
function ossCancel() {
  ossDialog.visible = false;
  ossReset();
}

/** 表单重置 */
function ossReset() {
  ossForm.value = { ...initOssFormData };
  ossFormRef.value?.resetFields();
}

/** 下载文件 */
const handleDownload = (row?: EmployeeFile | null) => {
  console.log('handleDownload 被调用，参数:', row);
  if (!row) {
    ElMessage.warning('文件信息为空，无法下载');
    return;
  }
  if (!row.ossId) {
    ElMessage.warning('文件缺少 ossId，无法下载');
    console.error('文件数据不完整:', row);
    return;
  }
  // 确保 ossId 是字符串，避免大数字精度丢失
  const ossId = String(row.ossId);
  console.log('开始下载文件，ossId:', ossId, '文件名:', row.originalName);
  proxy?.$download.oss(ossId);
};

/** 删除文件 */
const handleDeleteFile = async (row: EmployeeFile) => {
  await proxy?.$modal.confirm('是否确认删除文件"' + row.originalName + '"？');
  // 调用删除文件接口
  const { delOss } = await import('@/api/system/oss');
  await delOss(row.ossId);
  // 从列表中移除
  fileList.value = fileList.value.filter(item => item.id !== row.id);
  proxy?.$modal.msgSuccess('删除成功');
};
</script>

<style lang="scss" scoped>
.file-list-container {
  margin-top: 10px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;

  .file-list-title {
    font-weight: bold;
    margin-bottom: 10px;
    color: #606266;
  }
}

.file-list-empty {
  margin-top: 10px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.preview-container {
  min-height: 400px;
  
  .preview-image,
  .preview-pdf,
  .preview-office,
  .preview-text {
    width: 100%;
  }
  
  .preview-unsupported,
  .preview-office {
    padding: 40px 0;
  }
}

/* 预览对话框样式调整 */
.file-preview-dialog :deep(.el-dialog__body) {
  padding: 10px 20px;
}

/* PDF iframe 高度设置 */
.preview-pdf iframe {
  height: 70vh;
}
</style>
