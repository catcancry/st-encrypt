// 导入axios实例
import httpRequest from '@/util/request.js'

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
