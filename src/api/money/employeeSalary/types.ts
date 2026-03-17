export interface EmployeeSalaryVO {
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
   * 基薪
   */
  basicSalary: number;

  /**
   * 月绩效工资
   */
  monthlyPerformanceSalary: number;

  /**
   * 证书费
   */
  certificateFee: number;

  /**
   * 岗位工资
   */
  postSalary: number;

  /**
   * 加班工资
   */
  overtimeSalary: number;

  /**
   * 通讯补助
   */
  communicationSubsidy: string | number;

  /**
   * 交通补助
   */
  trafficSubsidy: string | number;

  /**
   * 餐补/误餐
   */
  mealSubsidy: string | number;

  /**
   * 其他加款
   */
  otherAdditional: number;

  /**
   * 薪资月份（格式：YYYYMM）
   */
  salaryMonth: string;

  /**
   * 应领合计（自动计算）
   */
  totalSalary: number;

}

export interface EmployeeSalaryForm extends BaseEntity {
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
   * 基薪
   */
  basicSalary?: number;

  /**
   * 月绩效工资
   */
  monthlyPerformanceSalary?: number;

  /**
   * 证书费
   */
  certificateFee?: number;

  /**
   * 岗位工资
   */
  postSalary?: number;

  /**
   * 加班工资
   */
  overtimeSalary?: number;

  /**
   * 通讯补助
   */
  communicationSubsidy?: string | number;

  /**
   * 交通补助
   */
  trafficSubsidy?: string | number;

  /**
   * 餐补/误餐
   */
  mealSubsidy?: string | number;

  /**
   * 其他加款
   */
  otherAdditional?: number;

  /**
   * 薪资月份（格式：YYYYMM）
   */
  salaryMonth?: string;

  /**
   * 应领合计（自动计算）
   */
  totalSalary?: number;

}

export interface EmployeeSalaryQuery extends PageQuery {

  /**
   * 员工姓名
   */
  employeeName?: string;

  /**
   * 所属部门名称
   */
  deptName?: string;

  /**
   * 薪资月份（格式：YYYYMM）
   */
  salaryMonth?: string;

  /**
   * 日期范围参数
   */
  params?: any;
}
