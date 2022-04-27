package vip.ylove.demo.client.common.other;

public interface FileUploadType {

    /**
     * 分片上传中
     */
    int UPLOADING = 0;
    /**
     * 上传成功
     */
    int SUCCESS = 2;
    /**
     * 上传失败
     */
    int FAIL = 1;
    /**
     * 分片上传完成，正在进行后续逻辑处理，例如合成文件、上传对象存储等
     */
    int DEAL = 3;

}
