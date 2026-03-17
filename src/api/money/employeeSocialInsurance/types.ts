export interface EmployeeSocialInsuranceVO {
  /**
   * 主键ID
   */
  id: string | number;

  /**
   * 备注
   */
  remark: string;

  /**
   * 员工ID（关联sys_user.user_id）
   */
  userId: string | number;

  /**
   * 员工姓名
   */
  employeeName: string;

  /**
   * 所属部门ID
   */
  deptId: string | number;

  /**
   * 所属部门名称
   */
  deptName: string;

  /**
   * 五险一金所属月份（格式：YYYYMM）
   */
  insuranceMonth: string;

  /**
   * 养老保险缴纳基数
   */
  pensionBase: number;

  /**
   * 养老保险-个人缴纳
   */
  pensionPersonal: number;

  /**
   * 养老保险-单位缴纳
   */
  pensionCompany: number;

  /**
   * 医疗保险缴纳基数
   */
  medicalBase: number;

  /**
   * 医疗保险-个人缴纳
   */
  medicalPersonal: number;

  /**
   * 医疗保险-单位缴纳
   */
  medicalCompany: number;

  /**
   * 失业保险缴纳基数
   */
  unemploymentBase: number;

  /**
   * 失业保险-个人缴纳
   */
  unemploymentPersonal: number;

  /**
   * 失业保险-单位缴纳
   */
  unemploymentCompany: number;

  /**
   * 累计专项附加扣除-子女教育费
   */
  childrenEducation: number;

  /**
   * 累计专项附加扣除-继续教育费
   */
  continuingEducation: number;

  /**
   * 累计专项附加扣除-住房贷款
   */
  housingLoan: number;

  /**
   * 累计专项附加扣除-住房租金
   */
  housingRent: number;

  /**
   * 累计专项附加扣除-赡养老人
   */
  supportElderly: number;

  /**
   * 累计专项附加扣除-婴幼儿专项
   */
  infantCare: number;

  /**
   * 扣款（罚款）
   */
  deductionPenalty: number;

  /**
   * 扣款（借支）
   */
  deductionLoan: number;

}

export interface EmployeeSocialInsuranceForm extends BaseEntity {
  /**
   * 主键ID
   */
  id?: string | number;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 员工ID（关联sys_user.user_id）
   */
  userId?: string | number;

  /**
   * 员工姓名
   */
  employeeName?: string;

  /**
   * 所属部门ID
   */
  deptId?: string | number;

  /**
   * 所属部门名称
   */
  deptName?: string;

  /**
   * 五险一金所属月份（格式：YYYYMM）
   */
  insuranceMonth?: string;

  /**
   * 养老保险缴纳基数
   */
  pensionBase?: number;

  /**
   * 养老保险-个人缴纳
   */
  pensionPersonal?: number;

  /**
   * 养老保险-单位缴纳
   */
  pensionCompany?: number;

  /**
   * 医疗保险缴纳基数
   */
  medicalBase?: number;

  /**
   * 医疗保险-个人缴纳
   */
  medicalPersonal?: number;

  /**
   * 医疗保险-单位缴纳
   */
  medicalCompany?: number;

  /**
   * 失业保险缴纳基数
   */
  unemploymentBase?: number;

  /**
   * 失业保险-个人缴纳
   */
  unemploymentPersonal?: number;

  /**
   * 失业保险-单位缴纳
   */
  unemploymentCompany?: number;

  /**
   * 累计专项附加扣除-子女教育费
   */
  childrenEducation?: number;

  /**
   * 累计专项附加扣除-继续教育费
   */
  continuingEducation?: number;

  /**
   * 累计专项附加扣除-住房贷款
   */
  housingLoan?: number;

  /**
   * 累计专项附加扣除-住房租金
   */
  housingRent?: number;

  /**
   * 累计专项附加扣除-赡养老人
   */
  supportElderly?: number;

  /**
   * 累计专项附加扣除-婴幼儿专项
   */
  infantCare?: number;

  /**
   * 扣款（罚款）
   */
  deductionPenalty?: number;

  /**
   * 扣款（借支）
   */
  deductionLoan?: number;

}

export interface EmployeeSocialInsuranceQuery extends PageQuery {
  /**
   * 员工姓名
   */
  employeeName?: string;

  /**
   * 所属部门名称
   */
  deptName?: string;

  /**
   * 五险一金所属月份（格式：YYYYMM）
   */
  insuranceMonth?: string;

  /**
   * 日期范围参数
   */
  params?: any;
}
