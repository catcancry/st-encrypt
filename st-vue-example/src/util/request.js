import axios from 'axios'
import stClientUtil from "./st-encrypt-sdk.js";

//配置参数,从环境变量中读取
const baser_url = import.meta.env.VITE_APP_BASE_API;
const publicKey = import.meta.env.VITE_PUBLIC_KEY;
const appId = import.meta.env.VITE_APP_ID;
const appAuth = import.meta.env.VITE_APP_AUTH;

// 创建一个 axios 实例
const service = axios.create({
	baseURL: baser_url, // 所有的请求地址前缀部分
	timeout: 60000, // 请求超时时间毫秒
	withCredentials: true, // 异步请求携带cookie
	headers: {
		// 设置后端需要的传参类型
		'Content-Type': 'application/json',
		'token': 'your token',
		'X-Requested-With': 'XMLHttpRequest',
	},
})

// 添加请求拦截器
service.interceptors.request.use(
	function (config) {
		// 在发送请求之前做些什么
		if(config.encrypt){//从请求配置中读取是否需要加密
			let aesKey = stClientUtil.createAESBase64Key();
			config.aesKey = aesKey;//保存本次请求的加密key
			if(config.method == 'get'){
				console.info(config)
				config.params = stClientUtil.encrypt(publicKey,aesKey,new Date().getTime(),appId,appAuth,config.params,true);
			}else{
				config.data = stClientUtil.encrypt(publicKey,aesKey,new Date().getTime(),appId,appAuth,config.data,true);
			}	
		}
		return config
	},
	function (error) {
		// 对请求错误做些什么
		console.log("error:",error)
		return Promise.reject(error)
	}
)

// 添加响应拦截器
service.interceptors.response.use(
	function (response) {
		// 2xx 范围内的状态码都会触发该函数。
		// 对响应数据做点什么
		// dataAxios 是 axios 返回数据中的 data
		const dataAxios = response.data
		//解密请求结果
		if(response.config.encrypt && dataAxios.success){//对请求结果进行解密
			let aesKey = response.config.aesKey;
			let data = stClientUtil.dencrypt(publicKey,aesKey,dataAxios)
			if(!data.signVerify){ ///验签未通过时处理
				
			}
			return data.data;
		}
		const code = dataAxios.reset
		return dataAxios
	},
	function (error) {
		// 超出 2xx 范围的状态码都会触发该函数。
		// 对响应错误做点什么
		console.log(error)
		return Promise.reject(error)
	}
)
export default service