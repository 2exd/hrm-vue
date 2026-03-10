import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { JtDeptVO, JtDeptForm, JtDeptQuery } from '@/api/board/jtDept/types';

/**
 * 查询金堂部门信息列表
 * @param query
 * @returns {*}
 */

export const listJtDept = (query?: JtDeptQuery): AxiosPromise<JtDeptVO[]> => {
  return request({
    url: '/board/dept/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询金堂部门信息详细
 * @param id
 */
export const getJtDept = (id: string | number): AxiosPromise<JtDeptVO> => {
  return request({
    url: '/board/dept/' + id,
    method: 'get'
  });
};

/**
 * 新增金堂部门信息
 * @param data
 */
export const addJtDept = (data: JtDeptForm) => {
  return request({
    url: '/board/dept',
    method: 'post',
    data: data
  });
};

/**
 * 修改金堂部门信息
 * @param data
 */
export const updateJtDept = (data: JtDeptForm) => {
  return request({
    url: '/board/dept',
    method: 'put',
    data: data
  });
};

/**
 * 删除金堂部门信息
 * @param id
 */
export const delJtDept = (id: string | number | Array<string | number>) => {
  return request({
    url: '/board/dept/' + id,
    method: 'delete'
  });
};
