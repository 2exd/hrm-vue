export interface EmployeeRosterVO {
  /**
   * 主键ID
   */
  id: string | number;

  /**
   * 系统用户ID（关联sys_user.user_id）
   */
  userId: string | number;

  /**
   * 员工姓名
   */
  employeeName: string;

  /**
   * 身份证号（唯一）
   */
  idCard: string | number;

  /**
   * 电话号码
   */
  phone: string;

  /**
   * 家庭住址
   */
  homeAddress: string;

  /**
   * 紧急联系人姓名
   */
  emergencyContact: string;

  /**
   * 紧急联系人电话
   */
  emergencyContactPhone: string;

  /**
   * 银行卡号
   */
  bankCard: string;

  /**
   * 银行卡行号
   */
  bankBranchCode: string;

  /**
   * 开户行名称
   */
  bankName: string;

  /**
   * 日薪
   */
  dailySalary: number;

  /**
   * 月薪
   */
  monthlySalary: number;

  /**
   * 劳动合同签订时间（进场时间）
   */
  contractSignTime: string;

  /**
   * 劳动合同编码
   */
  contractCode: string;

  /**
   * 劳动合同到期时间
   */
  contractExpireTime: string;

  /**
   * 退场时间
   */
  exitTime: string;

  /**
   * 备注
   */
  remark: string;

}

export interface EmployeeRosterForm extends BaseEntity {
  /**
   * 主键ID
   */
  id?: string | number;

  /**
   * 系统用户ID（关联sys_user.user_id）
   */
  userId?: string | number;

  /**
   * 员工姓名
   */
  employeeName?: string;

  /**
   * 身份证号（唯一）
   */
  idCard?: string | number;

  /**
   * 电话号码
   */
  phone?: string;

  /**
   * 家庭住址
   */
  homeAddress?: string;

  /**
   * 紧急联系人姓名
   */
  emergencyContact?: string;

  /**
   * 紧急联系人电话
   */
  emergencyContactPhone?: string;

  /**
   * 银行卡号
   */
  bankCard?: string;

  /**
   * 银行卡行号
   */
  bankBranchCode?: string;

  /**
   * 开户行名称
   */
  bankName?: string;

  /**
   * 日薪
   */
  dailySalary?: number;

  /**
   * 月薪
   */
  monthlySalary?: number;

  /**
   * 劳动合同签订时间（进场时间）
   */
  contractSignTime?: string;

  /**
   * 劳动合同编码
   */
  contractCode?: string;

  /**
   * 劳动合同到期时间
   */
  contractExpireTime?: string;

  /**
   * 退场时间
   */
  exitTime?: string;

  /**
   * 备注
   */
  remark?: string;

}

export interface EmployeeRosterQuery extends PageQuery {

  /**
   * 系统用户ID（关联sys_user.user_id）
   */
  userId?: string | number;

  /**
   * 员工姓名
   */
  employeeName?: string;

  /**
   * 身份证号（唯一）
   */
  idCard?: string | number;

  /**
   * 电话号码
   */
  phone?: string;

  /**
   * 家庭住址
   */
  homeAddress?: string;

  /**
   * 紧急联系人姓名
   */
  emergencyContact?: string;

  /**
   * 紧急联系人电话
   */
  emergencyContactPhone?: string;

  /**
   * 银行卡号
   */
  bankCard?: string;

  /**
   * 银行卡行号
   */
  bankBranchCode?: string;

  /**
   * 开户行名称
   */
  bankName?: string;

  /**
   * 日薪
   */
  dailySalary?: number;

  /**
   * 月薪
   */
  monthlySalary?: number;

  /**
   * 劳动合同签订时间（进场时间）
   */
  contractSignTime?: string;

  /**
   * 劳动合同编码
   */
  contractCode?: string;

  /**
   * 劳动合同到期时间
   */
  contractExpireTime?: string;

  /**
   * 退场时间
   */
  exitTime?: string;

  /**
   * 日期范围参数
   */
  params?: any;
}
