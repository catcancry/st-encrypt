import CryptoJS from 'crypto-js';
import jsrsasign from 'jsrsasign';
import BMF from 'browser-md5-file'; 


/* 
 * crypto-js进行AES加密,安装: npm i crypto-js
 * jsrsasign进行rsa加密,安装: npm i jsrsasign
 * browser-md5-file进行文件md5,安装: npm i browser-md5-file
*/

const StClientUtil = {};

/**
 * 生成aeskey
 */
StClientUtil.createAesKey = ()=> {
  const key = []
  for (let i = 0; i < 16; i++) {
    const num = Math.floor(Math.random() * 26)
    const charStr = String.fromCharCode(97 + num)
    key.push(charStr.toUpperCase())
  }
  const result = key.join('')
  return result
}

/**
 * AES加密
 * 转Utf8编码: CryptoJS.enc.Utf8.parse();
 * 转Base64: CryptoJS.enc.Base64.stringify();
 * @param data 需要加密的数据
 * @param key 密钥
 * @returns 加密后的数据
 */
StClientUtil.encodeAES = (data, key)=> {
  if (typeof data !== 'string') {
    data = JSON.stringify(data)
  }
  // AES加密
  // const result = CryptoJS.AES.encrypt(data, CryptoJS.enc.Utf8.parse(key), {
  //   iv: CryptoJS.enc.Utf8.parse('1234567812345678'), // 向量。使用CBC模式时，需要使用向量。
  //   mode: CryptoJS.mode.CBC,
  //   padding: CryptoJS.pad.ZeroPadding // 偏移量。使用非补码方式时，需要使用ZeroPadding。默认为PKCS5Padding。
  // })
  const result = CryptoJS.AES.encrypt(data, CryptoJS.enc.Utf8.parse(key), {
    mode: CryptoJS.mode.ECB
  })
  // base64转码
  return CryptoJS.enc.Base64.stringify(result.ciphertext)
}

/**
 * AES解密
 * @param data 需要解密的数据
 * @param key 密钥
 * @returns 解密后的数据
 */
StClientUtil.decodeAES = (data, key)=> {
  if (typeof data !== 'string') {
    data = JSON.stringify(data)
  }
  // const result = CryptoJS.AES.decrypt(data, CryptoJS.enc.Utf8.parse(key), {
  //   iv: CryptoJS.enc.Utf8.parse('1234567812345678'), // 向量。使用CBC模式时，需要使用向量。
  //   mode: CryptoJS.mode.CBC,
  //   padding: CryptoJS.pad.ZeroPadding // 偏移量。使用非补码方式时，需要使用ZeroPadding。默认为PKCS5Padding。
  // })
  const result = CryptoJS.AES.decrypt(data, CryptoJS.enc.Utf8.parse(key), {
	mode: CryptoJS.mode.ECB,
  })
  // 转为utf-8编码
  return CryptoJS.enc.Utf8.stringify(result)
}

/**
 * RSA加密
 * @param data 需要加密的数据
 * @param key 密钥
 * @returns 加密后的数据
 */
StClientUtil.encodeRSA = (data, key)=> {
  if (typeof data !== 'string') {
    data = JSON.stringify(data)
  }		
  let pk = "-----BEGIN PUBLIC KEY-----\n" + key + "\n-----END PUBLIC KEY-----";
  let pub = new  jsrsasign.KEYUTIL.getKey(pk);
  let enc = jsrsasign.KJUR.crypto.Cipher.encrypt(data,pub);
  return enc;
}

/**
 * RSA解密
 * @param data 需要解密的数据
 * @param key 密钥
 * @returns 解密后的数据
 */
StClientUtil.decodeRSA = (data, key) => {
  if (typeof data !== 'string') {
    data = JSON.stringify(data)
  }		
  let pk = "-----BEGIN PRIVATE  KEY-----\n" + key + "\n-----END PRIVATE  KEY-----";
  let prv = jsrsasign.KEYUTIL.getKey(pk);
  return jsrsasign.KJUR.crypto.Cipher.decrypt(data,prv);
}

/**
 * 签名,MD5withRSA 签名并转换成hex编码
 * @param data 需要签名的数据
 * @param key SHA1签名的密钥
 */
StClientUtil.sign = (data, key ) => {
  if (typeof data !== 'string') {
    data = JSON.stringify(data)
  }		    
  let pk = "-----BEGIN PRIVATE  KEY-----\n" + key + "\n-----END PRIVATE  KEY-----";
  // RSA signature generation
  var rsaKey = new jsrsasign.KEYUTIL.getKey(pk);
  var sig = new jsrsasign.KJUR.crypto.Signature({"alg": "MD5withRSA"});
  sig.init(rsaKey);
  sig.updateString(data);
  return  jsrsasign.hextob64(signature.sign());
}

/**
 * 验证签名 
 * @param  data 签名内容
 * @param  sign 签名值
 * @param  key rsa公钥
 */
StClientUtil.signVerify = (data,sign, key) => {  
  if (typeof data !== 'string') {
	data = JSON.stringify(data)
  }
  let pk = "-----BEGIN PUBLIC KEY-----\n" + key + "\n-----END PUBLIC KEY-----";
  // RSA signature generation
  var rsaKey = new jsrsasign.KEYUTIL.getKey(pk);
  var sig = new jsrsasign.KJUR.crypto.Signature({"alg": "MD5withRSA"});
  sig.init(rsaKey);
  sig.updateString(data);
  return sig.verify(sign);
}

/**
 * 通过md5获取摘要信息
 * @param  data
 */
StClientUtil.md5 = (data)=>{
	if (typeof data !== 'string') {
	  data = JSON.stringify(data)
	}
	return CryptoJS.MD5(data).toString();
}

/**
 * 
 * @param {签名文件} file 
 * @param {回调函数 } fn {code:0,err:err,md5:md5,process:100}   0完成  1进行中
 */
StClientUtil.md5File = (file,fn)=>{
	const bmf = new BMF();
	 bmf.md5(file,(err, md5) => {
	      console.log('err:', err);
	      console.log('md5 string:', md5); 
		  fn&&fn({code:0,err:err,md5:md5,process:100});
	    },
	    progress => {
			fn&&fn({code:1,process:progress});
	    },
	  );
}



/**
 * 格式化日期
 * @param date 日期
 * @param formatType 格式化类型
 */
StClientUtil.format = (date, formatType = 'yyyy-MM-dd hh:mm:ss')=> {
  // eslint-disable-next-line no-extend-native
  Date.prototype.format =
    function(fmt) {
      const o = {
        'M+': this.getMonth() + 1, // 月份
        'd+': this.getDate(), // 日
        'h+': this.getHours(), // 小时
        'm+': this.getMinutes(), // 分
        's+': this.getSeconds(), // 秒
        'q+': Math.floor((this.getMonth() + 3) / 3), // 季度
        S: this.getMilliseconds() // 毫秒
      }
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (this.getFullYear() + '').substr(4 - RegExp.$1.length)
        )
      }
      for (const k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) {
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length === 1
              ? o[k]
              : ('00' + o[k]).substr(('' + o[k]).length)
          )
        }
      }
      return fmt
    }
  if (!date || !date.format) {
    return date
  }
  return date.format(formatType)
}


/**
 *  对文件内容进行base64编码，再进行aes加密
 * @param {加密结果} result 
 * @param {AES加密key} aesKey 
 * @param {加密文件} file 
 */
StClientUtil.enFile = (aesKey,file,fn)=>{
   //获取文件原名称
   const fileName = file.name;
   const reader = new FileReader();
   reader.onload = () => {
		 const fileBase64 = reader.result;
		 //获取文件类型
		 const fileContenType = reader.result.match(/^data:\w+\/\w+[-]?\w+/)[0].replace("data:","");
		 //删除base64前缀
		 const endata = StClientUtil.encodeAES(fileBase64.replace(/^data:\w+\/\w+[-]?\w+;base64,/, ""),aesKey );
		 //重新生成上传文件
		 fn&&fn(new File([endata],fileName, {type: fileContenType}))
	}
   // readAsText(file, encoding)： 以纯文本形式读取文件， 读取到的文本保存在result属性中。 第二个参数代表编码格式。
   // readAsDataURL(file)： 读取文件并且将文件以数据URI的形式保存在result属性中。
   // readAsBinaryString(file)： 读取文件并且把文件以字符串保存在result属性中。
   // readAsArrayBuffer(file)： 读取文件并且将一个包含文件内容的ArrayBuffer保存咋result属性中。
   // FileReader.abort()： 中止读取操作。 在返回时， readyState属性为DONE。
   reader.readAsDataURL(file);
}


/**
 *  加密上传文件请求 
 * @param {公钥} publicKey 
 * @param {AES加密key} aesKey 
 * @param {时间戳} t 
 * @param {appId} appId 
 * @param {auth} appAuth 
 * @param {上传文件表单 formData} data 
 * @param {是否需要动态信封} neeDynamicKey 
 */
StClientUtil.encryptFormData = (publicKey,aesKey,t,appId,appAuth,formData,neeDynamicKey)=>{
	//读取表单中的文件
	if(formData == null){
		return null;
	}
	let tempFormData = new FormData(); 
	let otherInfo = {};
	for(let key of formData){
		if( key != null && key.length == 2 && key[1] instanceof File){
			tempFormData.append(key[0], key[1])
		}else{
			otherInfo[key[0]] = key[1];
		}
	}
	let enOtherInfo = StClientUtil.encrypt(publicKey,aesKey,t,appId,appAuth,otherInfo,neeDynamicKey,false);
	if(neeDynamicKey){
		tempFormData.append("data",enOtherInfo.data);
		tempFormData.append("key",enOtherInfo.key);
	}else{
		tempFormData.append("data",enOtherInfo.data)
	}
	return tempFormData;
}

/**
 * 
 * @param  publicKey 公钥
 * @param  aesKey    aeskey
 * @param  t         时间戳
 * @param  appId     授权appId
 * @param  appAuth 	 授权appAuth
 * @param  data      待加密的数据
 * @param  neeDynamicKey  是否使用动态key,使用的话将会在请求参数中添加加密的动态key
 * @param  neeDynamicKey  是否使用对参数encodeURI,在get请求时使用
 */
StClientUtil.encrypt = (publicKey,aesKey,t,appId,appAuth,data,neeDynamicKey,needEncodeURI)=>{
	//验证加密数据是否为空
	if(data != null){
		//不是string类型则判断是否值
		if (typeof data !== 'string') {
		 if(Object.keys(data).length === 0 ){
		 	data = null;
		 }
		}else{
			if(data.trim() == null || data.trim() ==''){
				data = null;
			}
		}
	}
	//是否需要往后端传key
	if(neeDynamicKey == true){
		appId = appId == null ? "null":appId;
		appAuth = appAuth == null ? "null":appAuth;
		//按照格式拼接代码
		let keyTemp = [aesKey,t,appId,appAuth].join("###");
		//rsa加密key
		let keyTempEncrypt = StClientUtil.encodeRSA(keyTemp,publicKey);
		
		if(data == null){
			return {key:needEncodeURI?encodeURI(keyTempEncrypt):keyTempEncrypt}
		}else{
			//aes加密内容
			let dataTemp = data?StClientUtil.encodeAES(data,aesKey):null ;
			return {key:needEncodeURI?encodeURI(keyTempEncrypt):keyTempEncrypt ,data:needEncodeURI?encodeURI(dataTemp):dataTemp}
		}
	}else{
		if(data == null){
			return {};
		}else{
			let dataTemp = StClientUtil.encodeAES(data,aesKey) ;
			return {data:needEncodeURI?encodeURI(dataTemp):dataTemp}
		}
	}
}
/**
 * 
 * @param  publicKey 公钥
 * @param  aesKey    加密key
 * @param  data 	 后端响应的加密数据
 */
StClientUtil.dencrypt = (publicKey,aesKey,data)=>{
	//使用aes解密响应结果
	let dataTemp = StClientUtil.decodeAES(data.data,aesKey);	
	//验证签名是否正确
	let v = StClientUtil.signVerify(dataTemp,data.sign,publicKey) 
	return {signVerify:v,data:JSON.parse(dataTemp)};
}

export default StClientUtil