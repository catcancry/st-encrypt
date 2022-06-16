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
	// apiGetUserInfo({
	// 	t: 'tdddddd',
	// 	appId: 'appIdddddddd',
	// 	auth: 'authddddddd',
	// 	test:"test12345",
	// 	data:"测试中文@%*&=@&！@#￥@#%#……&（&）（*&……#！"
	// }).then((res) => {
	// 	console.log("apiGetUserInfo加密请求(带参数)->最终请求结果:",res)
	// })
	//不带参数
	// apiGetUserInfo(null).then((res) => {
	// 	console.log("apiGetUserInfo加密请求(不带参数)->最终请求结果:",res)
	// })
	//get 请求,带参数请求
	// apiGetUserInfoByGet({
	// 	t: 'tdddddd',
	// 	appId: 'appIdddddddd',
	// 	auth: 'authddddddd',
	// 	test:"test12345",
	// 	data:"测试中文@%*&=@&！@#￥@#%#……&（&）（*&……#！"
	// }).then((res) => {
	// 	console.log("apiGetUserInfoByGet加密请求(带参数)->最终请求结果:",res)
	// })
	// //get请求不带参数 覆盖null,{},""等情况
	// apiGetUserInfoByGet(null).then((res) => {
	// 	console.log("apiGetUserInfoByGet加密请求(不带参数)->最终请求结果:",res)
	// })

	//post请求,带参数请求
	// apiGetUserInfoByPost({
	// 	t: 'tdddddd',
	// 	appId: 'appIdddddddd',
	// 	auth: 'authddddddd',
	// 	test: "test12345",
	// 	data: "测试中文@%*&=@&！@#￥@#%#……&（&）（*&……#！",
	// 	"3435f9b053e7b11c71805373c02acea7": "3435f9b053e7b11c71805373c02acea7"
	// }).then((res) => {
	// 	console.log("apiGetUserInfoByPost加密请求(带参数)->最终请求结果:", res)
	// })

	// apiGetUserInfoNoEncrypt({
	// 	userID: '10001',
	// 	userName: 'Mike',
	// }).then((res) => {
	// 	console.log("apiGetUserInfoNoEncrypt未加密请求->最终请求结果:",res)
	// })
	
	

	console.info("md5:" + stClientUtil.md5("555555"))
	var fileData = ref({});
	let chooseFile = (e) => {
		const file = e.target.files[0]
		console.info(file);
		if (!file) {
			// 如果用户没有选择图片,只是点了文件上传这个按钮
			return
		}
		
		// readAsText(file, encoding)： 以纯文本形式读取文件， 读取到的文本保存在result属性中。 第二个参数代表编码格式。
		// readAsDataUrl(file)： 读取文件并且将文件以数据URI的形式保存在result属性中。
		// readAsBinaryString(file)： 读取文件并且把文件以字符串保存在result属性中。
		// readAsArrayBuffer(file)： 读取文件并且将一个包含文件内容的ArrayBuffer保存咋result属性中。
		// FileReader.abort()： 中止读取操作。 在返回时， readyState属性为DONE。
		// const reader = new FileReader();
		reader.onload = () => {
			const fileBase64 = reader.result;
			
			const fileType = reader.result.replace(/^data:image\/\w+;base64,/, "")
			console.info("读取完成:", reader.result.replace(/^data:image\/\w+;base64,/, ""));
			let key = 'THMXCMQCSLCOVODH'//stClientUtil.createAESBase64Key();
			console.info(stClientUtil.encodeAES(reader.result,key));
		}
		reader.readAsDataURL(file);

		console.info(file.name);
		const fd = new FormData() //创建FormData对象,
		////fd.append('photo', file) //这个photo是vuex中的,append第一个参数是规定要插入的内容,必传参数
		let tempFile = {name:"1.p2"};
		//第一个参数具体的内容,第二个参数文件明年初，第三个参数文件类型
		const ddd = new File([JSON.stringify(tempFile)], 'payapp_init_json.json', {type: 'application/json'});
		fd.append("photo2", ddd)
		upLoadFile(fd).then((res) => {
			console.log("upLoadFile加密请求(带参数)->最终请求结果:",res)
		})
		
	}
</script>

<template>

	<div @click="test">{{msg}}</div>

	<input type="file" @change="chooseFile" ref="fileData" />
	<span @click="fileData.click()">ddd</span>

</template>

<style scoped>
	a {
		color: #42b983;
	}
</style>
