export interface BoardVO {
  /**
   * 主键ID
   */
  id: string | number;

  /**
   * 备注
   */
  remark: string;

  /**
   * 留言主体类型（0：个人 1：企业）
   */
  boardType: string;

  /**
   * 留言人姓名/企业名称
   */
  name: string;

  /**
   * 手机号（限定长度，符合实际场景）
   */
  phonenumber: string;

  /**
   * 留言内容
   */
  content: string;

  /**
   * 留言类型（0-其他事项 1-业务咨询 2-建言献策 3-表扬感谢）
   */
  msgType: number;

  /**
   * 留言编号（唯一标识，如BOARD20260303001）
   */
  msgCode: string;

  /**
   * 处理部门
   */
  handleDept: number;

  /**
   * 处理人ID
   */
  handleAdmin: number;

  /**
   * 回复内容
   */
  replyContent: string;

  /**
   * 状态（0-待处理 1-处理中 2-已处理 3-已办结）
   */
  status: number;

  /**
   * 提交成功时间
   */
  submitTime: string;

  /**
   * 等待处理开始时间
   */
  waitHandleTime: string;

  /**
   * 电话回访时间
   */
  phoneCallTime: string;

}

export interface BoardForm extends BaseEntity {
  /**
   * 主键ID
   */
  id?: string | number;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 留言主体类型（0：个人 1：企业）
   */
  boardType?: string;

  /**
   * 留言人姓名/企业名称
   */
  name?: string;

  /**
   * 手机号（限定长度，符合实际场景）
   */
  phonenumber?: string;

  /**
   * 留言内容
   */
  content?: string;

  /**
   * 留言类型（0-其他事项 1-业务咨询 2-建言献策 3-表扬感谢）
   */
  msgType?: number;

  /**
   * 留言编号（唯一标识，如BOARD20260303001）
   */
  msgCode?: string;

  /**
   * 处理部门
   */
  handleDept?: number;

  /**
   * 处理人ID
   */
  handleAdmin?: number;

  /**
   * 回复内容
   */
  replyContent?: string;

  /**
   * 状态（0-待处理 1-处理中 2-已处理 3-已办结）
   */
  status?: number;

  /**
   * 提交成功时间
   */
  submitTime?: string;

  /**
   * 等待处理开始时间
   */
  waitHandleTime?: string;

  /**
   * 电话回访时间
   */
  phoneCallTime?: string;

}

export interface BoardQuery extends PageQuery {

  /**
   * 留言主体类型（0：个人 1：企业）
   */
  boardType?: string;

  /**
   * 留言人姓名/企业名称
   */
  name?: string;

  /**
   * 手机号（限定长度，符合实际场景）
   */
  phonenumber?: string;

  /**
   * 留言内容
   */
  content?: string;

  /**
   * 留言类型（0-其他事项 1-业务咨询 2-建言献策 3-表扬感谢）
   */
  msgType?: number;

  /**
   * 留言编号（唯一标识，如BOARD20260303001）
   */
  msgCode?: string;

  /**
   * 处理部门
   */
  handleDept?: number;

  /**
   * 处理人ID
   */
  handleAdmin?: number;

  /**
   * 回复内容
   */
  replyContent?: string;

  /**
   * 状态（0-待处理 1-处理中 2-已处理 3-已办结）
   */
  status?: number;

  /**
   * 提交成功时间
   */
  submitTime?: string;

  /**
   * 等待处理开始时间
   */
  waitHandleTime?: string;

  /**
   * 电话回访时间
   */
  phoneCallTime?: string;

  /**
   * 日期范围参数
   */
  params?: any;
}
