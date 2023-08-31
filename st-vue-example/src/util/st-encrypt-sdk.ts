import CryptoJS from 'crypto-js';
import jsrsasign from 'jsrsasign';
import BMF from 'browser-md5-file';

/*
 * crypto-js进行AES加密,安装: npm i crypto-js
 * jsrsasign进行rsa加密,安装: npm i jsrsasign
 * browser-md5-file进行文件md5,安装: npm i browser-md5-file
*/

/**
 * 后端响应的加密结果
 */
class StResponseBody {
  /**
   * 数据
   */
  data : string | null;
  /**
   * 签名
   */
  sign : string;

  constructor(data : string | null, sign : string) {
    this.data = data;
    this.sign = sign;
  }
}


/**
 * 前端解密后的结果
 */
class StResponseResult {
  /**
   * 数据
   */
  data : Object | null;
  /**
   * 验签结果
   */
  signVerify : boolean;

  constructor(data : string | null, signVerify : boolean) {
    this.data = data;
    this.signVerify = signVerify;
  }
}


/**
 * 前端发送给后端的加密数据对象
 */
class StResquestBody {
  /**
   * 签名
   */
  sign : string | null;
  /**
   * 数据
   */
  key : string | null;
  /**
   * 数据
   */
  data : string | null;

  constructor(sign : string | null, key : string | null, data : string | null) {
    this.key = key;
    this.data = data;
    this.sign = sign;
  }
}

//export const stClientUtil = StClientUtil
export const stClientUtil = {
  /**
   * 生成aeskey
   */
  createAesKey: () : string => {
    const key = []
    for (let i = 0; i < 16; i++) {
      const num = Math.floor(Math.random() * 26)
      const charStr = String.fromCharCode(97 + num)
      key.push(charStr.toUpperCase())
    }
    const result = key.join('')
    return result
  },

  /**
   * AES加密
   * 转Utf8编码: CryptoJS.enc.Utf8.parse();
   * 转Base64: CryptoJS.enc.Base64.stringify();
   * @param data 需要加密的数据
   * @param key 密钥
   * @returns 加密后的数据
   */
  encodeAES: (data : Object | string, key : string) : string => {
    let tempData : string;
    if (typeof data !== 'string') {
      tempData = JSON.stringify(data)
    } else {
      tempData = data;
    }
    // AES加密
    // const result = CryptoJS.AES.encrypt(data, CryptoJS.enc.Utf8.parse(key), {
    //   iv: CryptoJS.enc.Utf8.parse('1234567812345678'), // 向量。使用CBC模式时，需要使用向量。
    //   mode: CryptoJS.mode.CBC,
    //   padding: CryptoJS.pad.ZeroPadding // 偏移量。使用非补码方式时，需要使用ZeroPadding。默认为PKCS5Padding。
    // })
    const result = CryptoJS.AES.encrypt(tempData, CryptoJS.enc.Utf8.parse(key), {
      mode: CryptoJS.mode.ECB
    })
    // base64转码
    return CryptoJS.enc.Base64.stringify(result.ciphertext)
  },

  /**
   * AES解密
   * @param data 需要解密的数据
   * @param key 密钥
   * @returns 解密后的数据
   */
  decodeAES: (data : Object | string | null, key : string) : string | null => {
    let tempData : string;
    if (data == null) {
      return null;
    }
    if (typeof data !== 'string') {
      tempData = JSON.stringify(data)
    } else {
      tempData = data;
    }
    // const result = CryptoJS.AES.decrypt(data, CryptoJS.enc.Utf8.parse(key), {
    //   iv: CryptoJS.enc.Utf8.parse('1234567812345678'), // 向量。使用CBC模式时，需要使用向量。
    //   mode: CryptoJS.mode.CBC,
    //   padding: CryptoJS.pad.ZeroPadding // 偏移量。使用非补码方式时，需要使用ZeroPadding。默认为PKCS5Padding。
    // })
    const result = CryptoJS.AES.decrypt(tempData, CryptoJS.enc.Utf8.parse(key), {
      mode: CryptoJS.mode.ECB,
    })
    // 转为utf-8编码
    return CryptoJS.enc.Utf8.stringify(result)
  },

  /**
   * RSA加密
   * @param data 需要加密的数据
   * @param key 密钥
   * @returns 加密后的数据
   */
  encodeRSA: (data : Object | string, key : string) : string => {
    let tempData : string;
    if (typeof data !== 'string') {
      tempData = JSON.stringify(data)
    } else {
      tempData = data;
    }
    let pk = "-----BEGIN PUBLIC KEY-----\n" + key + "\n-----END PUBLIC KEY-----";
    let pub = new jsrsasign.KEYUTIL.getKey(pk);
    let enc = jsrsasign.KJUR.crypto.Cipher.encrypt(tempData, pub);
    return enc;
  },

  /**
   * RSA解密
   * @param data 需要解密的数据
   * @param key 密钥
   * @returns 解密后的数据
   */
  decodeRSA: (data : Object | string, key : string) : string => {
    let tempData : string;
    if (typeof data !== 'string') {
      tempData = JSON.stringify(data)
    } else {
      tempData = data;
    }
    let pk = "-----BEGIN PRIVATE  KEY-----\n" + key + "\n-----END PRIVATE  KEY-----";
    let prv = jsrsasign.KEYUTIL.getKey(pk);
    return jsrsasign.KJUR.crypto.Cipher.decrypt(tempData, prv);
  },

  /**
   * 签名,MD5withRSA 签名并转换成hex编码
   * @param data 需要签名的数据
   * @param key SHA1签名的密钥
   */
  sign: (data : Object | string, key : string) : string => {
    let tempData : string;
    if (typeof data !== 'string') {
      tempData = JSON.stringify(data)
    } else {
      tempData = data;
    }
    let pk = "-----BEGIN PRIVATE  KEY-----\n" + key + "\n-----END PRIVATE  KEY-----";
    // RSA signature generation
    var rsaKey = new jsrsasign.KEYUTIL.getKey(pk);
    var sig = new jsrsasign.KJUR.crypto.Signature({ "alg": "MD5withRSA" });
    sig.init(rsaKey);
    sig.updateString(tempData);
    return jsrsasign.hextob64(sig.sign());
  },

  /**
   * 验证签名
   * @param  data 签名内容
   * @param  sign 签名值
   * @param  key rsa公钥
   */
  signVerify: (data : Object | string | null, sign : string | null, key : string) : boolean => {
    let tempData : string;
    if (data == null) {
      return false;
    }
    if (sign == null) {
      return false;
    }
    if (typeof data !== 'string') {
      tempData = JSON.stringify(data)
    } else {
      tempData = data;
    }
    let pk = "-----BEGIN PUBLIC KEY-----\n" + key + "\n-----END PUBLIC KEY-----";
    // RSA signature generation
    var rsaKey = new jsrsasign.KEYUTIL.getKey(pk);
    var sig = new jsrsasign.KJUR.crypto.Signature({ "alg": "MD5withRSA" });
    sig.init(rsaKey);
    sig.updateString(tempData);
    return sig.verify(sign);
  },

  /**
   * 通过md5获取摘要信息
   * @param  data
   */
  md5: (data : Object | string) : string => {
    let tempData : string;
    if (typeof data !== 'string') {
      tempData = JSON.stringify(data)
    } else {
      tempData = data;
    }
    return CryptoJS.MD5(tempData).toString();
  },

  /**
   *
   * @param {签名文件} file
   */
  md5File: (file : File) : any => {
    return new Promise((resolve) => {
      const bmf = new BMF();
      bmf.md5(file, (err : Object, md5 : string) : void => {
        console.log('err:', err);
        console.log('md5 string:', md5);
        resolve({ code: 0, err: err, md5: md5, process: 100 });
        //reject(new Error("Something awful happened"));
      });
    });
  },

  /**
   *  对文件内容进行base64编码，再进行aes加密
   * @param {加密结果} result
   * @param {AES加密key} aesKey
   * @param {加密文件} file
   */
  enFile: (aesKey : string, file : File) : any => {
    return new Promise((resolve, reject) => {
      //获取文件原名称
      const fileName = file.name;
      const reader = new FileReader();
      reader.onload = () => {
        const fileBase64 : string | ArrayBuffer | null = reader.result;
        if (fileBase64 == null) {
          reject("error");
          return;
        }
        let fileResult : string;
        if (fileBase64 instanceof ArrayBuffer) {
          const decoder = new TextDecoder();
          fileResult = decoder.decode(fileBase64);
        } else {
          fileResult = fileBase64;
        }
        //获取文件类型
        let fileContenType : string;
        const fileContenTypes = fileResult.match(/^data:\w+\/\w+[-]?\w+/);
        if (fileContenTypes == null || fileContenTypes.length == 0) {
          fileContenType = "";
        } else {
          fileContenType = fileContenTypes[0].replace("data:", "");
        }
        //删除base64前缀
        const endata = stClientUtil.encodeAES(fileResult.replace(/^data:\w+\/\w+[-]?\w+;base64,/, ""), aesKey);
        //重新生成上传文件
        resolve(new File([endata], fileName, { type: fileContenType }));
      }
      // readAsText(file, encoding)： 以纯文本形式读取文件， 读取到的文本保存在result属性中。 第二个参数代表编码格式。
      // readAsDataURL(file)： 读取文件并且将文件以数据URI的形式保存在result属性中。
      // readAsBinaryString(file)： 读取文件并且把文件以字符串保存在result属性中。
      // readAsArrayBuffer(file)： 读取文件并且将一个包含文件内容的ArrayBuffer保存咋result属性中。
      // FileReader.abort()： 中止读取操作。 在返回时， readyState属性为DONE。
      reader.readAsDataURL(file);
    })
  },

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
  encryptFormData: (publicKey : string, aesKey : string, t : string | Number, appId : string, appAuth : string, formData : any, neeDynamicKey : boolean) : FormData => {
    //读取表单中的文件
    if (formData == null) {
      return new FormData();
    }
    let tempFormData = new FormData();
    let otherInfo : any = {};
    for (let key of formData) {
      if (key != null && key.length == 2 && key[1] instanceof File) {
        tempFormData.append(key[0], key[1])
      } else {
        otherInfo[key[0]] = key[1];
      }
    }
    let enOtherInfo : StResquestBody = stClientUtil.encrypt(publicKey, aesKey, t, appId, appAuth, otherInfo, neeDynamicKey, false);
    if (neeDynamicKey) {
      if (enOtherInfo.data != null) {
        tempFormData.append("data", enOtherInfo.data);
      }
      if (enOtherInfo.key != null) {
        tempFormData.append("key", enOtherInfo.key);
      }

    } else {
      if (enOtherInfo.data != null) {
        tempFormData.append("data", enOtherInfo.data)
      }
    }
    return tempFormData;
  },
  /**
   *
   * @param  publicKey 公钥
   * @param  aesKey    加密key
   * @param  data 	 后端响应的加密数据
   */
  dencrypt: (publicKey : string, aesKey : string, data : StResponseBody) : StResponseResult => {
    //使用aes解密响应结果
    let dataTemp = stClientUtil.decodeAES(data.data, aesKey);
    if (dataTemp == null) {
      //验证签名是否正确
      return new StResponseResult(null, false);
    } else {
      //验证签名是否正确
      let v = stClientUtil.signVerify(dataTemp, data.sign, publicKey)
      return new StResponseResult(JSON.parse(dataTemp), v);
    }
  },

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
  encrypt: (publicKey : string, aesKey : string, t : string | Number, appId : string, appAuth : string, data : Object | string | null, neeDynamicKey : boolean, needEncodeURI : boolean) : StResquestBody => {
    //验证加密数据是否为空
    if (data != null) {
      //不是string类型则判断是否值
      if (typeof data !== 'string') {
        if (Object.keys(data).length === 0) {
          data = null;
        }
      } else {
        if (data.trim() == null || data.trim() == '') {
          data = null;
        }
      }
    }
    //是否需要往后端传key
    if (neeDynamicKey) {
      appId = appId == null ? "null" : appId;
      appAuth = appAuth == null ? "null" : appAuth;
      //按照格式拼接代码
      let keyTemp = [aesKey, t, appId, appAuth].join("###");
      //rsa加密key
      let keyTempEncrypt = stClientUtil.encodeRSA(keyTemp, publicKey);

      if (data == null) {
        return new StResquestBody(null, needEncodeURI ? encodeURI(keyTempEncrypt) : keyTempEncrypt, null)
      } else {
        //aes加密内容
        let dataTemp = data ? stClientUtil.encodeAES(data, aesKey) : null;
        if (needEncodeURI) {
          return new StResquestBody(null, encodeURI(keyTempEncrypt), dataTemp == null ? null : encodeURI(dataTemp))
        } else {
          return new StResquestBody(null, keyTempEncrypt, dataTemp)
        }
      }
    } else {
      if (data == null) {
        return new StResquestBody(null, null, null);
      } else {
        let dataTemp = stClientUtil.encodeAES(data, aesKey);
        return new StResquestBody(null, null, needEncodeURI ? encodeURI(dataTemp) : dataTemp);
      }
    }
  }
}
