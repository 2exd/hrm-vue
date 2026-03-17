<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :inline="true" :model="queryParams">
            <el-form-item label="姓名" prop="employeeName">
              <el-input v-model="queryParams.employeeName" clearable placeholder="请输入员工姓名" @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="部门" prop="deptName">
              <el-input v-model="queryParams.deptName" clearable placeholder="请输入所属部门名称" @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="月份" style="width: 308px">
              <el-date-picker
                v-model="dateRangeInsuranceMonth"
                :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
                end-placeholder="结束日期"
                range-separator="-"
                start-placeholder="开始日期"
                type="daterange"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
            <el-form-item>
              <el-button icon="Search" type="primary" @click="handleQuery">搜索 </el-button>
              <el-button icon="Refresh" @click="resetQuery">重置 </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button v-hasPermi="['money:employeeSocialInsurance:add']" icon="Plus" plain type="primary" @click="handleAdd">新增 </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['money:employeeSocialInsurance:edit']" :disabled="single" icon="Edit" plain type="success" @click="handleUpdate()"
              >修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-hasPermi="['money:employeeSocialInsurance:remove']"
              :disabled="multiple"
              icon="Delete"
              plain
              type="danger"
              @click="handleDelete()"
              >删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['money:employeeSocialInsurance:export']" icon="Download" plain type="warning" @click="handleExport"
              >导出
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button icon="Setting" plain type="info" @click="configManageDialog.visible = true">比例配置管理 </el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table
        v-loading="loading"
        :data="employeeSocialInsuranceList"
        border
        stripe
        highlight-current-row
        :header-cell-style="{ background: '#f5f7fa', fontWeight: 600 }"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection" width="50" fixed="left" />
        <el-table-column align="center" label="姓名" prop="employeeName" min-width="80" show-overflow-tooltip />
        <el-table-column align="center" label="部门" prop="deptName" min-width="100" show-overflow-tooltip />
        <el-table-column align="center" label="月份" prop="insuranceMonth" min-width="90">
          <template #default="scope">
            <span>{{ parseTime(scope.row.insuranceMonth, '{y}-{m}') }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="个人缴纳" min-width="95">
          <template #default="scope">
            <el-tag effect="dark" size="small" type="success">¥{{ calculatePersonalTotal(scope.row).toFixed(2) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="单位缴纳" min-width="95">
          <template #default="scope">
            <el-tag effect="dark" size="small" type="warning">¥{{ calculateCompanyTotal(scope.row).toFixed(2) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="专项扣除" min-width="95">
          <template #default="scope">
            <el-tag effect="dark" size="small" type="info">¥{{ calculateDeductionTotal(scope.row).toFixed(2) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="养老保险" min-width="140">
          <el-table-column align="center" label="个人" prop="pensionPersonal" min-width="70">
            <template #default="scope">
              <span class="amount-text personal">¥{{ scope.row.pensionPersonal || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="单位" prop="pensionCompany" min-width="70">
            <template #default="scope">
              <span class="amount-text company">¥{{ scope.row.pensionCompany || 0 }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="医疗保险" min-width="140">
          <el-table-column align="center" label="个人" prop="medicalPersonal" min-width="70">
            <template #default="scope">
              <span class="amount-text personal">¥{{ scope.row.medicalPersonal || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="单位" prop="medicalCompany" min-width="70">
            <template #default="scope">
              <span class="amount-text company">¥{{ scope.row.medicalCompany || 0 }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="失业保险" min-width="140">
          <el-table-column align="center" label="个人" prop="unemploymentPersonal" min-width="70">
            <template #default="scope">
              <span class="amount-text personal">¥{{ scope.row.unemploymentPersonal || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="单位" prop="unemploymentCompany" min-width="70">
            <template #default="scope">
              <span class="amount-text company">¥{{ scope.row.unemploymentCompany || 0 }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" class-name="small-padding fixed-width" fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button
                v-hasPermi="['money:employeeSocialInsurance:edit']"
                icon="Edit"
                link
                type="primary"
                @click="handleUpdate(scope.row)"
              ></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button
                v-hasPermi="['money:employeeSocialInsurance:remove']"
                icon="Delete"
                link
                type="primary"
                @click="handleDelete(scope.row)"
              ></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" v-model:limit="queryParams.pageSize" v-model:page="queryParams.pageNum" :total="total" @pagination="getList" />
    </el-card>

    <!-- 添加或修改员工五险一金及专项附加扣除对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" append-to-body width="800px">
      <el-form ref="employeeSocialInsuranceFormRef" :model="form" :rules="rules" class="insurance-form" label-width="120px">
        <!-- 基本信息 -->
        <div class="form-section">
          <div class="section-title">
            <el-icon>
              <User />
            </el-icon>
            <span>基本信息</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="员工姓名" prop="employeeName">
                <el-input
                  v-model="form.employeeName"
                  :disabled="isEdit"
                  placeholder="请选择员工"
                  readonly
                  @click="isEdit ? null : handleSelectClick('delegateName')"
                >
                  <template #suffix>
                    <el-icon>
                      <Search />
                    </el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属月份" prop="insuranceMonth">
                <el-date-picker
                  v-model="form.insuranceMonth"
                  :disabled="isEdit"
                  placeholder="请选择月份"
                  style="width: 100%"
                  type="month"
                  value-format="YYYY-MM-DD HH:mm:ss"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 比例配置选择 -->
        <div class="form-section">
          <div class="section-title config">
            <el-icon>
              <Setting />
            </el-icon>
            <span>比例配置选择</span>
            <el-tag v-if="isEdit" size="small" style="margin-left: 10px" type="warning">修改时可切换配置重新计算</el-tag>
          </div>
          <el-form-item label="使用配置方案">
            <el-select v-model="selectedConfigId" placeholder="请选择比例配置方案" style="width: 100%" @change="onConfigChange">
              <el-option v-for="item in insuranceConfigStore.configOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
            <div v-if="selectedConfig" class="config-preview">
              <el-descriptions :column="3" border size="small">
                <el-descriptions-item label="养老个人"> {{ (selectedConfig.pensionPersonalRate * 100).toFixed(1) }}% </el-descriptions-item>
                <el-descriptions-item label="养老单位"> {{ (selectedConfig.pensionCompanyRate * 100).toFixed(1) }}% </el-descriptions-item>
                <el-descriptions-item label="医疗个人"> {{ (selectedConfig.medicalPersonalRate * 100).toFixed(1) }}% </el-descriptions-item>
                <el-descriptions-item label="医疗单位"> {{ (selectedConfig.medicalCompanyRate * 100).toFixed(1) }}% </el-descriptions-item>
                <el-descriptions-item label="失业个人"> {{ (selectedConfig.unemploymentPersonalRate * 100).toFixed(1) }}% </el-descriptions-item>
                <el-descriptions-item label="失业单位"> {{ (selectedConfig.unemploymentCompanyRate * 100).toFixed(1) }}% </el-descriptions-item>
              </el-descriptions>
            </div>
          </el-form-item>
        </div>

        <!-- 缴纳基数与金额 -->
        <div class="form-section">
          <div class="section-title base">
            <el-icon>
              <Money />
            </el-icon>
            <span>缴纳基数与金额</span>
          </div>

          <!-- 养老保险 -->
          <div class="insurance-item">
            <div class="insurance-header">
              <span class="insurance-name">养老保险</span>
              <span class="insurance-rate">
                个人{{ (currentRate.pensionPersonalRate * 100).toFixed(1) }}% / 单位{{ (currentRate.pensionCompanyRate * 100).toFixed(1) }}%
              </span>
            </div>
            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item label="缴纳基数">
                  <el-input-number
                    v-model="localForm.pensionBase"
                    :controls="false"
                    :min="0"
                    :precision="2"
                    placeholder="基数"
                    style="width: 100%"
                    @change="calculateInsurance('pension')"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="个人">
                  <div class="amount-display personal">
                    ¥
                    {{ Number(form.pensionPersonal || 0).toFixed(2) }}
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="单位">
                  <div class="amount-display company">
                    ¥
                    {{ Number(form.pensionCompany || 0).toFixed(2) }}
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <!-- 医疗保险 -->
          <div class="insurance-item">
            <div class="insurance-header">
              <span class="insurance-name">医疗保险</span>
              <span class="insurance-rate">
                个人{{ (currentRate.medicalPersonalRate * 100).toFixed(1) }}% / 单位{{ (currentRate.medicalCompanyRate * 100).toFixed(1) }}%
              </span>
            </div>
            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item label="缴纳基数">
                  <el-input-number
                    v-model="localForm.medicalBase"
                    :controls="false"
                    :min="0"
                    :precision="2"
                    placeholder="基数"
                    style="width: 100%"
                    @change="calculateInsurance('medical')"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="个人">
                  <div class="amount-display personal">
                    ¥
                    {{ Number(form.medicalPersonal || 0).toFixed(2) }}
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="单位">
                  <div class="amount-display company">
                    ¥
                    {{ Number(form.medicalCompany || 0).toFixed(2) }}
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <!-- 失业保险 -->
          <div class="insurance-item">
            <div class="insurance-header">
              <span class="insurance-name">失业保险</span>
              <span class="insurance-rate">
                个人{{ (currentRate.unemploymentPersonalRate * 100).toFixed(1) }}% / 单位{{ (currentRate.unemploymentCompanyRate * 100).toFixed(1) }}%
              </span>
            </div>
            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item label="缴纳基数">
                  <el-input-number
                    v-model="localForm.unemploymentBase"
                    :controls="false"
                    :min="0"
                    :precision="2"
                    placeholder="基数"
                    style="width: 100%"
                    @change="calculateInsurance('unemployment')"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="个人">
                  <div class="amount-display personal">
                    ¥
                    {{ Number(form.unemploymentPersonal || 0).toFixed(2) }}
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="单位">
                  <div class="amount-display company">
                    ¥
                    {{ Number(form.unemploymentCompany || 0).toFixed(2) }}
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <!-- 合计 -->
          <div class="insurance-total">
            <div class="total-item personal">
              <span>个人缴纳合计：</span>
              <span class="amount">¥ {{ personalTotal }}</span>
            </div>
            <div class="total-item company">
              <span>单位缴纳合计：</span>
              <span class="amount">¥ {{ companyTotal }}</span>
            </div>
          </div>
        </div>

        <!-- 专项附加扣除 -->
        <div class="form-section">
          <div class="section-title deduction">
            <el-icon>
              <Discount />
            </el-icon>
            <span>专项附加扣除</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="子女教育">
                <el-input-number
                  v-model="form.childrenEducation"
                  :controls="false"
                  :min="0"
                  :precision="2"
                  placeholder="子女教育费"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="继续教育">
                <el-input-number
                  v-model="form.continuingEducation"
                  :controls="false"
                  :min="0"
                  :precision="2"
                  placeholder="继续教育费"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="住房贷款">
                <el-input-number
                  v-model="form.housingLoan"
                  :controls="false"
                  :min="0"
                  :precision="2"
                  placeholder="住房贷款利息"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="住房租金">
                <el-input-number v-model="form.housingRent" :controls="false" :min="0" :precision="2" placeholder="住房租金" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="赡养老人">
                <el-input-number v-model="form.supportElderly" :controls="false" :min="0" :precision="2" placeholder="赡养老人" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="婴幼儿照护">
                <el-input-number v-model="form.infantCare" :controls="false" :min="0" :precision="2" placeholder="婴幼儿专项" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <div class="subtotal deduction-subtotal">
            <span>专项扣除合计：</span>
            <span class="amount">¥ {{ deductionTotal }}</span>
          </div>
        </div>

        <!-- 备注 -->
        <div class="form-section">
          <div class="section-title">
            <el-icon>
              <Document />
            </el-icon>
            <span>其他</span>
          </div>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" :rows="2" maxlength="200" placeholder="请输入备注" show-word-limit type="textarea" />
          </el-form-item>
        </div>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定 </el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 比例配置管理对话框 -->
    <el-dialog v-model="configManageDialog.visible" append-to-body title="比例配置管理" width="700px">
      <el-table :data="insuranceConfigStore.configList" border>
        <el-table-column label="配置名称" prop="name" />
        <el-table-column label="养老(个人/单位)" width="140">
          <template #default="scope">
            {{ (scope.row.pensionPersonalRate * 100).toFixed(1) }}% / {{ (scope.row.pensionCompanyRate * 100).toFixed(1) }}%
          </template>
        </el-table-column>
        <el-table-column label="医疗(个人/单位)" width="140">
          <template #default="scope">
            {{ (scope.row.medicalPersonalRate * 100).toFixed(1) }}% / {{ (scope.row.medicalCompanyRate * 100).toFixed(1) }}%
          </template>
        </el-table-column>
        <el-table-column label="失业(个人/单位)" width="140">
          <template #default="scope">
            {{ (scope.row.unemploymentPersonalRate * 100).toFixed(1) }}% / {{ (scope.row.unemploymentCompanyRate * 100).toFixed(1) }}%
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template #default="scope">
            <el-button link type="primary" @click="editConfig(scope.row)">编辑 </el-button>
            <el-button link type="danger" @click="deleteConfig(scope.row.id)">删除 </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 15px; text-align: right">
        <el-button type="primary" @click="addNewConfig">新增配置 </el-button>
      </div>
    </el-dialog>

    <!-- 编辑/新增配置对话框 -->
    <el-dialog v-model="configEditDialog.visible" :title="configEditDialog.title" append-to-body width="500px">
      <el-form :model="configEditForm" label-width="140px">
        <el-form-item label="配置名称">
          <el-input v-model="configEditForm.name" placeholder="如：成都-2026" />
        </el-form-item>
        <el-divider>养老保险</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="个人比例(%)">
              <el-input-number
                v-model="configEditForm.pensionPersonalRate"
                :controls="false"
                :max="100"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位比例(%)">
              <el-input-number v-model="configEditForm.pensionCompanyRate" :controls="false" :max="100" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider>医疗保险</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="个人比例(%)">
              <el-input-number
                v-model="configEditForm.medicalPersonalRate"
                :controls="false"
                :max="100"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位比例(%)">
              <el-input-number v-model="configEditForm.medicalCompanyRate" :controls="false" :max="100" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider>失业保险</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="个人比例(%)">
              <el-input-number
                v-model="configEditForm.unemploymentPersonalRate"
                :controls="false"
                :max="100"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位比例(%)">
              <el-input-number
                v-model="configEditForm.unemploymentCompanyRate"
                :controls="false"
                :max="100"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="saveConfigEdit">保 存 </el-button>
          <el-button @click="configEditDialog.visible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <PersonnelSelection ref="personnelSelectionRef" @confirm="handleConfirm" />
  </div>
</template>

<script lang="ts" name="EmployeeSocialInsurance" setup>
import {
  addEmployeeSocialInsurance,
  delEmployeeSocialInsurance,
  getEmployeeSocialInsurance,
  listEmployeeSocialInsurance,
  updateEmployeeSocialInsurance
} from '@/api/money/employeeSocialInsurance';
import { EmployeeSocialInsuranceForm, EmployeeSocialInsuranceQuery, EmployeeSocialInsuranceVO } from '@/api/money/employeeSocialInsurance/types';
import PersonnelSelection from '@/views/hrm/common/PersonnelSelection.vue';
import { type InsuranceRateConfig, useInsuranceConfigStore } from '@/store/modules/insuranceConfig';
import { computed, nextTick, reactive, ref } from 'vue';
import { Discount, Document, Money, Search, Setting, User } from '@element-plus/icons-vue';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const insuranceConfigStore = useInsuranceConfigStore();

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

// 本地表单数据（缴纳基数不存入后端）
const localForm = reactive({
  pensionBase: 0,
  medicalBase: 0,
  unemploymentBase: 0
});

// 当前选中的配置ID
const selectedConfigId = ref<string>('');

// 当前选中的配置
const selectedConfig = computed(() => {
  return insuranceConfigStore.configList.find((c) => c.id === selectedConfigId.value);
});

// 当前使用的比例（编辑时使用保存的比例，新增时使用选中的配置）
const currentRate = computed(() => {
  if (isEdit.value) {
    // 编辑时从store中找到与保存数据匹配的配置，或使用默认比例
    return selectedConfig.value || insuranceConfigStore.currentConfig;
  }
  // 新增时使用选中的配置
  return selectedConfig.value || insuranceConfigStore.currentConfig;
});

const initFormData: EmployeeSocialInsuranceForm = {
  id: undefined,
  remark: undefined,
  userId: undefined,
  employeeName: undefined,
  deptId: undefined,
  deptName: undefined,
  insuranceMonth: undefined,
  pensionBase: 0,
  pensionPersonal: 0,
  pensionCompany: 0,
  medicalBase: 0,
  medicalPersonal: 0,
  medicalCompany: 0,
  unemploymentBase: 0,
  unemploymentPersonal: 0,
  unemploymentCompany: 0,
  childrenEducation: 0,
  continuingEducation: 0,
  housingLoan: 0,
  housingRent: 0,
  supportElderly: 0,
  infantCare: 0
};

const data = reactive<PageData<EmployeeSocialInsuranceForm, EmployeeSocialInsuranceQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    deptName: undefined,
    params: {
      insuranceMonth: undefined
    }
  },
  rules: {
    id: [{ required: true, message: '主键ID不能为空', trigger: 'blur' }],
    userId: [{ required: true, message: '员工ID不能为空', trigger: 'blur' }],
    employeeName: [{ required: true, message: '员工姓名不能为空', trigger: 'blur' }],
    insuranceMonth: [{ required: true, message: '五险一金所属月份不能为空', trigger: 'blur' }]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 是否为编辑模式 */
const isEdit = computed(() => !!form.value.id);

/** 查询员工五险一金及专项附加扣除列表 */
const getList = async () => {
  loading.value = true;
  queryParams.value.params = {};
  proxy?.addDateRange(queryParams.value, dateRangeInsuranceMonth.value, 'InsuranceMonth');
  const res = await listEmployeeSocialInsurance(queryParams.value);
  employeeSocialInsuranceList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
};

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  localForm.pensionBase = 0;
  localForm.medicalBase = 0;
  localForm.unemploymentBase = 0;
  selectedConfigId.value = insuranceConfigStore.currentConfigId;
  employeeSocialInsuranceFormRef.value?.resetFields();
};

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};

/** 重置按钮操作 */
const resetQuery = () => {
  dateRangeInsuranceMonth.value = ['', ''];
  queryFormRef.value?.resetFields();
  handleQuery();
};

/** 多选框选中数据 */
const handleSelectionChange = (selection: EmployeeSocialInsuranceVO[]) => {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  selectedConfigId.value = insuranceConfigStore.currentConfigId;
  dialog.visible = true;
  dialog.title = '添加员工五险一金及专项附加扣除';
};

/** 修改按钮操作 */
const handleUpdate = async (row?: EmployeeSocialInsuranceVO) => {
  reset();
  const _id = row?.id || ids.value[0];
  const res = await getEmployeeSocialInsurance(_id);
  // 后端返回的数据结构是 { code, data, msg }
  const data = res.data;
  if (!data) {
    proxy?.$modal.msgError('获取数据失败');
    return;
  }
  // 赋值到表单
  Object.assign(form.value, {
    id: data.id,
    userId: data.userId,
    employeeName: data.employeeName,
    deptId: data.deptId,
    deptName: data.deptName,
    insuranceMonth: data.insuranceMonth,
    remark: data.remark,
    pensionPersonal: data.pensionPersonal,
    pensionCompany: data.pensionCompany,
    medicalPersonal: data.medicalPersonal,
    medicalCompany: data.medicalCompany,
    unemploymentPersonal: data.unemploymentPersonal,
    unemploymentCompany: data.unemploymentCompany,
    childrenEducation: data.childrenEducation,
    continuingEducation: data.continuingEducation,
    housingLoan: data.housingLoan,
    housingRent: data.housingRent,
    supportElderly: data.supportElderly,
    infantCare: data.infantCare
  });
  // 设置本地缴纳基数（后端可能没有返回，使用个人缴纳金额反推基数）
  const rate = insuranceConfigStore.getInsuranceRate;
  localForm.pensionBase = data.pensionBase || (data.pensionPersonal ? data.pensionPersonal / rate.pensionPersonalRate : 0);
  localForm.medicalBase = data.medicalBase || (data.medicalPersonal ? data.medicalPersonal / rate.medicalPersonalRate : 0);
  localForm.unemploymentBase = data.unemploymentBase || (data.unemploymentPersonal ? data.unemploymentPersonal / rate.unemploymentPersonalRate : 0);
  // 编辑时默认使用当前store的配置
  selectedConfigId.value = insuranceConfigStore.currentConfigId;
  dialog.visible = true;
  dialog.title = '修改员工五险一金及专项附加扣除';
};

/** 提交按钮 */
const submitForm = () => {
  employeeSocialInsuranceFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      // 将缴纳基数合并到提交数据中
      const submitData = {
        ...form.value,
        pensionBase: localForm.pensionBase,
        medicalBase: localForm.medicalBase,
        unemploymentBase: localForm.unemploymentBase
      };
      if (form.value.id) {
        await updateEmployeeSocialInsurance(submitData).finally(() => (buttonLoading.value = false));
      } else {
        await addEmployeeSocialInsurance(submitData).finally(() => (buttonLoading.value = false));
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

/** 删除按钮操作 */
const handleDelete = async (row?: EmployeeSocialInsuranceVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除员工五险一金及专项附加扣除编号为"' + _ids + '"的数据项？').finally(() => (loading.value = false));
  await delEmployeeSocialInsurance(_ids);
  proxy?.$modal.msgSuccess('删除成功');
  await getList();
};

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'money/employeeSocialInsurance/export',
    {
      ...queryParams.value
    },
    `employeeSocialInsurance_${new Date().getTime()}.xlsx`
  );
};

onMounted(() => {
  getList();
});

/** 计算保险金额 */
const calculateInsurance = (type: 'pension' | 'medical' | 'unemployment') => {
  const rate = currentRate.value;
  switch (type) {
    case 'pension':
      form.value.pensionPersonal = Number((localForm.pensionBase * rate.pensionPersonalRate).toFixed(2));
      form.value.pensionCompany = Number((localForm.pensionBase * rate.pensionCompanyRate).toFixed(2));
      break;
    case 'medical':
      form.value.medicalPersonal = Number((localForm.medicalBase * rate.medicalPersonalRate).toFixed(2));
      form.value.medicalCompany = Number((localForm.medicalBase * rate.medicalCompanyRate).toFixed(2));
      break;
    case 'unemployment':
      form.value.unemploymentPersonal = Number((localForm.unemploymentBase * rate.unemploymentPersonalRate).toFixed(2));
      form.value.unemploymentCompany = Number((localForm.unemploymentBase * rate.unemploymentCompanyRate).toFixed(2));
      break;
  }
};

/** 配置改变时重新计算 */
const onConfigChange = () => {
  const config = selectedConfig.value;
  if (!config) return;

  if (isEdit.value) {
    // 编辑模式：根据已有的缴纳金额反推基数，再用新配置计算新金额
    // 养老保险：根据当前个人缴纳金额反推基数
    if (form.value.pensionPersonal) {
      localForm.pensionBase = Number((form.value.pensionPersonal / config.pensionPersonalRate).toFixed(2));
    }
    // 医疗保险
    if (form.value.medicalPersonal) {
      localForm.medicalBase = Number((form.value.medicalPersonal / config.medicalPersonalRate).toFixed(2));
    }
    // 失业保险
    if (form.value.unemploymentPersonal) {
      localForm.unemploymentBase = Number((form.value.unemploymentPersonal / config.unemploymentPersonalRate).toFixed(2));
    }
    // 使用新配置重新计算金额
    nextTick(() => {
      calculateInsurance('pension');
      calculateInsurance('medical');
      calculateInsurance('unemployment');
    });
  } else {
    // 新增模式：根据基数计算金额
    calculateInsurance('pension');
    calculateInsurance('medical');
    calculateInsurance('unemployment');
  }
};

/** 个人缴纳合计 */
const personalTotal = computed(() => {
  const pension = Number(form.value.pensionPersonal) || 0;
  const medical = Number(form.value.medicalPersonal) || 0;
  const unemployment = Number(form.value.unemploymentPersonal) || 0;
  return (pension + medical + unemployment).toFixed(2);
});

/** 单位缴纳合计 */
const companyTotal = computed(() => {
  const pension = Number(form.value.pensionCompany) || 0;
  const medical = Number(form.value.medicalCompany) || 0;
  const unemployment = Number(form.value.unemploymentCompany) || 0;
  return (pension + medical + unemployment).toFixed(2);
});

/** 专项附加扣除合计 */
const deductionTotal = computed(() => {
  const children = Number(form.value.childrenEducation) || 0;
  const continuing = Number(form.value.continuingEducation) || 0;
  const loan = Number(form.value.housingLoan) || 0;
  const rent = Number(form.value.housingRent) || 0;
  const support = Number(form.value.supportElderly) || 0;
  const infant = Number(form.value.infantCare) || 0;
  return (children + continuing + loan + rent + support + infant).toFixed(2);
});

/** 计算列表行个人缴纳合计 */
const calculatePersonalTotal = (row: EmployeeSocialInsuranceVO) => {
  const pension = Number(row.pensionPersonal) || 0;
  const medical = Number(row.medicalPersonal) || 0;
  const unemployment = Number(row.unemploymentPersonal) || 0;
  return pension + medical + unemployment;
};

/** 计算列表行单位缴纳合计 */
const calculateCompanyTotal = (row: EmployeeSocialInsuranceVO) => {
  const pension = Number(row.pensionCompany) || 0;
  const medical = Number(row.medicalCompany) || 0;
  const unemployment = Number(row.unemploymentCompany) || 0;
  return pension + medical + unemployment;
};

/** 计算列表行专项扣除合计 */
const calculateDeductionTotal = (row: EmployeeSocialInsuranceVO) => {
  const children = Number(row.childrenEducation) || 0;
  const continuing = Number(row.continuingEducation) || 0;
  const loan = Number(row.housingLoan) || 0;
  const rent = Number(row.housingRent) || 0;
  const support = Number(row.supportElderly) || 0;
  const infant = Number(row.infantCare) || 0;
  return children + continuing + loan + rent + support + infant;
};

// 比例配置管理
const configManageDialog = reactive({
  visible: false
});

// 配置编辑对话框
const configEditDialog = reactive({
  visible: false,
  title: '',
  isEdit: false,
  editId: ''
});

// 配置编辑表单
const configEditForm = reactive({
  name: '',
  pensionPersonalRate: 8,
  pensionCompanyRate: 16,
  medicalPersonalRate: 2,
  medicalCompanyRate: 8,
  unemploymentPersonalRate: 0.5,
  unemploymentCompanyRate: 0.5
});

/** 新增配置 */
const addNewConfig = () => {
  configEditDialog.isEdit = false;
  configEditDialog.title = '新增配置';
  configEditForm.name = '';
  configEditForm.pensionPersonalRate = 8;
  configEditForm.pensionCompanyRate = 16;
  configEditForm.medicalPersonalRate = 2;
  configEditForm.medicalCompanyRate = 8;
  configEditForm.unemploymentPersonalRate = 0.5;
  configEditForm.unemploymentCompanyRate = 0.5;
  configEditDialog.visible = true;
};

/** 编辑配置 */
const editConfig = (config: InsuranceRateConfig) => {
  configEditDialog.isEdit = true;
  configEditDialog.title = '编辑配置';
  configEditDialog.editId = config.id;
  configEditForm.name = config.name;
  configEditForm.pensionPersonalRate = config.pensionPersonalRate * 100;
  configEditForm.pensionCompanyRate = config.pensionCompanyRate * 100;
  configEditForm.medicalPersonalRate = config.medicalPersonalRate * 100;
  configEditForm.medicalCompanyRate = config.medicalCompanyRate * 100;
  configEditForm.unemploymentPersonalRate = config.unemploymentPersonalRate * 100;
  configEditForm.unemploymentCompanyRate = config.unemploymentCompanyRate * 100;
  configEditDialog.visible = true;
};

/** 保存配置编辑 */
const saveConfigEdit = () => {
  const configData = {
    name: configEditForm.name,
    pensionPersonalRate: configEditForm.pensionPersonalRate / 100,
    pensionCompanyRate: configEditForm.pensionCompanyRate / 100,
    medicalPersonalRate: configEditForm.medicalPersonalRate / 100,
    medicalCompanyRate: configEditForm.medicalCompanyRate / 100,
    unemploymentPersonalRate: configEditForm.unemploymentPersonalRate / 100,
    unemploymentCompanyRate: configEditForm.unemploymentCompanyRate / 100
  };

  if (configEditDialog.isEdit) {
    insuranceConfigStore.updateConfig(configEditDialog.editId, configData);
  } else {
    insuranceConfigStore.addConfig(configData);
  }

  proxy?.$modal.msgSuccess('保存成功');
  configEditDialog.visible = false;
};

/** 删除配置 */
const deleteConfig = (id: string) => {
  const success = insuranceConfigStore.deleteConfig(id);
  if (!success) {
    proxy?.$modal.msgError('至少保留一个配置');
  }
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
.insurance-form {
  .form-section {
    margin-bottom: 20px;
    padding: 16px;
    background-color: #f8f9fa;
    border-radius: 8px;

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

      &.config {
        border-left: 4px solid var(--el-color-primary);
        padding-left: 12px;

        .el-icon {
          color: var(--el-color-primary);
        }
      }

      &.base {
        border-left: 4px solid var(--el-color-danger);
        padding-left: 12px;

        .el-icon {
          color: var(--el-color-danger);
        }
      }

      &.deduction {
        border-left: 4px solid var(--el-color-info);
        padding-left: 12px;

        .el-icon {
          color: var(--el-color-info);
        }
      }
    }

    .config-preview {
      margin-top: 10px;
      padding: 10px;
      background-color: #fff;
      border-radius: 4px;
    }

    .insurance-item {
      margin-bottom: 20px;
      padding: 15px;
      background-color: #fff;
      border-radius: 8px;
      border: 1px solid var(--el-border-color-lighter);

      &:last-child {
        margin-bottom: 0;
      }

      .insurance-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 1px dashed var(--el-border-color);

        .insurance-name {
          font-size: 16px;
          font-weight: 600;
          color: var(--el-text-color-primary);
        }

        .insurance-rate {
          font-size: 13px;
          color: var(--el-text-color-secondary);
          background-color: var(--el-fill-color-light);
          padding: 4px 10px;
          border-radius: 4px;
        }
      }
    }

    .amount-display {
      padding: 8px 12px;
      background-color: #fff;
      border: 1px solid var(--el-border-color);
      border-radius: 4px;
      font-size: 15px;
      font-weight: 600;
      text-align: right;

      &.personal {
        color: var(--el-color-success);
        background-color: var(--el-color-success-light-9);
      }

      &.company {
        color: var(--el-color-warning);
        background-color: var(--el-color-warning-light-9);
      }
    }

    .insurance-total {
      display: flex;
      justify-content: space-between;
      margin-top: 15px;
      padding: 12px 15px;
      background-color: #fff;
      border-radius: 8px;
      border: 1px solid var(--el-border-color-lighter);

      .total-item {
        display: flex;
        align-items: center;
        font-weight: 600;

        &.personal {
          color: var(--el-color-success);
        }

        &.company {
          color: var(--el-color-warning);
        }

        .amount {
          font-size: 20px;
          font-weight: bold;
          margin-left: 8px;
        }
      }
    }

    .subtotal {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      padding: 10px 16px;
      margin-top: 12px;
      border-radius: 6px;
      font-weight: 600;

      &.deduction-subtotal {
        background-color: var(--el-color-info-light-9);
        color: var(--el-color-info);
      }

      .amount {
        font-size: 18px;
        font-weight: bold;
        margin-left: 8px;
      }
    }
  }

  :deep(.el-input-number .el-input__inner) {
    text-align: left;
  }

  :deep(.el-form-item) {
    margin-bottom: 12px;
  }

  :deep(.el-form-item__label) {
    font-weight: 500;
  }
}

// 表格多行内容
.table-cell-multi {
  font-size: 12px;
  line-height: 1.6;

  div {
    padding: 2px 0;
  }
}

// 表格样式优化
:deep(.el-table) {
  font-size: 13px;

  .el-table__header-wrapper {
    th {
      font-weight: 600;
      color: var(--el-text-color-primary);
    }
  }

  .el-table__cell {
    padding: 8px 0;
  }

  // 金额文字样式
  .amount-text {
    font-size: 13px;
    font-weight: 500;

    &.personal {
      color: var(--el-color-success);
    }

    &.company {
      color: var(--el-color-warning);
    }
  }

  // 标签样式微调
  .el-tag {
    font-size: 12px;
    font-weight: 500;
  }
}

// 响应式：小屏幕时操作列按钮紧凑显示
@media screen and (max-width: 1400px) {
  :deep(.el-table) {
    .el-table__cell {
      padding: 6px 2px;
    }
  }
}
</style>
