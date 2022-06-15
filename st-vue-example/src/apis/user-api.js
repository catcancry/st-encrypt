// 导入axios实例
import httpRequest from '@/util/request.js'

// 获取用户信息 加密
export function apiGetUserInfoByGet(param) {
    return httpRequest({
		url: '/server/test/encrypt2',
		method: 'get',
		encrypt:true,
		params: param,
	})
}

// 获取用户信息 加密
export function apiGetUserInfoByPost(param) {
    return httpRequest({
		url: '/server/test/encrypt2',
		method: 'post',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
		encrypt:true,
		data: param,
	})
}

// 获取用户信息 加密
export function apiGetUserInfo(param) {
    return httpRequest({
		url: '/server/test/encrypt',
		method: 'post',
		data: param,
		encrypt:true
	})
}

// 获取用户信息 未加密
export function apiGetUserInfoNoEncrypt(param) {
    return httpRequest({
		url: '/server/test/noEncrypt',
		method: 'post',
		data: param,
		encrypt:false
	})
}
