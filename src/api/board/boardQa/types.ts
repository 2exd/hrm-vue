export interface BoardQaVO {
  /**
   * 问答记录ID
   */
  id: string | number;

  /**
   * 关联留言主表ID
   */
  boardId: string | number;

  /**
   * 消息类型（0-用户提问 1-板主回复）
   */
  qaType: number;

  /**
   * 问答内容
   */
  content: string;

  /**
   * 发送人ID（用户ID/管理员ID）
   */
  senderId: string | number;

  /**
   * 发送人姓名
   */
  senderName: string;

  /**
   * 发送时间
   */
  sendTime: string;

}

export interface BoardQaForm extends BaseEntity {
  /**
   * 问答记录ID
   */
  id?: string | number;

  /**
   * 关联留言主表ID
   */
  boardId?: string | number;

  /**
   * 消息类型（0-用户提问 1-板主回复）
   */
  qaType?: number;

  /**
   * 问答内容
   */
  content?: string;

  /**
   * 发送人ID（用户ID/管理员ID）
   */
  senderId?: string | number;

  /**
   * 发送人姓名
   */
  senderName?: string;

  /**
   * 发送时间
   */
  sendTime?: string;

}

export interface BoardQaQuery extends PageQuery {

  /**
   * 关联留言主表ID
   */
  boardId?: string | number;

  /**
   * 消息类型（0-用户提问 1-板主回复）
   */
  qaType?: number;

  /**
   * 问答内容
   */
  content?: string;

  /**
   * 发送人ID（用户ID/管理员ID）
   */
  senderId?: string | number;

  /**
   * 发送人姓名
   */
  senderName?: string;

  /**
   * 发送时间
   */
  sendTime?: string;

  /**
   * 日期范围参数
   */
  params?: any;
}
