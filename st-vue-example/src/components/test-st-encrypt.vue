<script setup>
	import {
		ref
	} from 'vue'
	import stClientUtil from "@/util/st-encrypt-sdk.js";
	import {
		apiGetUserInfo,
		apiGetUserInfoByGet,
		apiGetUserInfoByPost,
		apiGetUserInfoNoEncrypt,
		upLoadFile
	} from '@/apis/user-api'

	defineProps({
		msg: String
	})

	const count = ref(0)

	//带参数
	apiGetUserInfo({
		t: 'tdddddd',
		appId: 'appIdddddddd',
		auth: 'authddddddd',
		test:"test12345",
		data:"测试中文@%*&=@&！@#￥@#%#……&（&）（*&……#！"
	}).then((res) => {
		console.log("apiGetUserInfo加密请求(带参数)->最终请求结果:",res)
	})
	//不带参数
	apiGetUserInfo(null).then((res) => {
		console.log("apiGetUserInfo加密请求(不带参数)->最终请求结果:",res)
	})
	//get 请求,带参数请求
	apiGetUserInfoByGet({
		t: 'tdddddd',
		appId: 'appIdddddddd',
		auth: 'authddddddd',
		test:"test12345",
		data:"测试中文@%*&=@&！@#￥@#%#……&（&）（*&……#！"
	}).then((res) => {
		console.log("apiGetUserInfoByGet加密请求(带参数)->最终请求结果:",res)
	})
	//get请求不带参数 覆盖null,{},""等情况
	apiGetUserInfoByGet(null).then((res) => {
		console.log("apiGetUserInfoByGet加密请求(不带参数)->最终请求结果:",res)
	})

	//post请求,带参数请求
	apiGetUserInfoByPost({
		t: 'tdddddd',
		appId: 'appIdddddddd',
		auth: 'authddddddd',
		test: "test12345",
		data: "测试中文@%*&=@&！@#￥@#%#……&（&）（*&……#！",
		"3435f9b053e7b11c71805373c02acea7": "3435f9b053e7b11c71805373c02acea7"
	}).then((res) => {
		console.log("apiGetUserInfoByPost加密请求(带参数)->最终请求结果:", res)
	})

	apiGetUserInfoNoEncrypt({
		userID: '10001',
		userName: 'Mike',
	}).then((res) => {
		console.log("apiGetUserInfoNoEncrypt未加密请求->最终请求结果:",res)
	})
	
	let chooseFile =  (e) => {
		const file = e.target.files[0]
		console.info("原始文件：",file);
		if (!file) {
			return
		}
		const aeskey = stClientUtil.createAesKey();
		console.info("文件加密key:",aeskey)
		//对原文进行md5,保证文件完整性
		stClientUtil.md5File(file,(res)=>{
			if(res.code == 0){
				console.info("文件签名完成：",res)
				//对文件进行加密
				stClientUtil.enFile(aeskey,file,(enFile)=>{
					console.info("文件加密完成",enFile)
					const fd = new FormData()//创建FormData对象,
					fd.append('f', enFile)
					fd.append("st_upload_mode","2")//文件加密模式,对整个文件进行加密
					fd.append(res.md5,res.md5);
					fd.append("test","测试大陆上开发巨大上库了！@#！#！￥@！")//测试中文和符号
					fd.append("data","测试中文@%*&=@&！@#￥@#%#……&（&）（*&……#！")//测试中文和符号
					//调用上传
					//开始上传文件
					upLoadFile(aeskey,fd).then(res=>{
						console.info("上传结果:",res)
					})
					
				})
				
			}else{
				console.info("文件签名进度:",res.process);
			}
		})
		
	}
	
	
	let chooseFile2 =  (e) => {
		const file = e.target.files[0]
		console.info("原始文件：",file);
		if (!file) {
			return
		}
		//对原文进行md5,保证文件完整性
		stClientUtil.md5File(file,(res)=>{
			if(res.code == 0){
				console.info("文件签名完成：",res)
				const fd = new FormData()//创建FormData对象,
				fd.append('f', file)
				fd.append(res.md5,res.md5);
				fd.append("test","测试大陆上开发巨大上库了！@#！#！￥@！")//测试中文和符号
				fd.append("data","测试中文@%*&=@&！@#￥@#%#……&（&）（*&……#！")//测试中文和符号
				upLoadFile(null,fd).then(res=>{
					console.info("上传结果:",res)
				})				
			}else{
				console.info("文件签名进度:",res.process);
			}
		})
		
	}
	

</script>

<template>

	<div @click="test">{{msg}}</div>

	<input type="file" @change="chooseFile" />
	
	<input type="file" @change="chooseFile2"  />

</template>

<style scoped>
	a {
		color: #42b983;
	}
</style>
