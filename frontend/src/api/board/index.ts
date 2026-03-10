import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { BoardVO, BoardForm, BoardQuery } from '@/api/board/types';

/**
 * 查询留言板列表
 * @param query
 * @returns {*}
 */

export const listBoard = (query?: BoardQuery): AxiosPromise<BoardVO[]> => {
  return request({
    url: '/board/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询留言板详细
 * @param id
 */
export const getBoard = (id: string | number): AxiosPromise<BoardVO> => {
  return request({
    url: '/board/' + id,
    method: 'get'
  });
};

/**
 * 新增留言板
 * @param data
 */
export const addBoard = (data: BoardForm) => {
  return request({
    url: '/board',
    method: 'post',
    data: data
  });
};

/**
 * 修改留言板
 * @param data
 */
export const updateBoard = (data: BoardForm) => {
  return request({
    url: '/board',
    method: 'put',
    data: data
  });
};

/**
 * 删除留言板
 * @param id
 */
export const delBoard = (id: string | number | Array<string | number>) => {
  return request({
    url: '/board/' + id,
    method: 'delete'
  });
};
