import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { BoardQaVO, BoardQaForm, BoardQaQuery } from '@/api/board/boardQa/types';

/**
 * 查询留言板多轮问答日志列表
 * @param query
 * @returns {*}
 */

export const listBoardQa = (query?: BoardQaQuery): AxiosPromise<BoardQaVO[]> => {
  return request({
    url: '/board/boardQa/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询留言板多轮问答日志详细
 * @param id
 */
export const getBoardQa = (id: string | number): AxiosPromise<BoardQaVO> => {
  return request({
    url: '/board/boardQa/' + id,
    method: 'get'
  });
};

/**
 * 新增留言板多轮问答日志
 * @param data
 */
export const addBoardQa = (data: BoardQaForm) => {
  return request({
    url: '/board/boardQa',
    method: 'post',
    data: data
  });
};

/**
 * 修改留言板多轮问答日志
 * @param data
 */
export const updateBoardQa = (data: BoardQaForm) => {
  return request({
    url: '/board/boardQa',
    method: 'put',
    data: data
  });
};

/**
 * 删除留言板多轮问答日志
 * @param id
 */
export const delBoardQa = (id: string | number | Array<string | number>) => {
  return request({
    url: '/board/boardQa/' + id,
    method: 'delete'
  });
};
