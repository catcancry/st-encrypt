package vip.ylove;


import javax.servlet.http.HttpServletRequest;
import java.util.Locale;


/**
 *
 * code from org.apache.commons.fileupload;
 *
 */
public class FileUploadUtils {

    /**
     * 判断请求是否为上传
     * @param request   request
     * @return boolean
     */
    public final static boolean isMultipartContent(HttpServletRequest request){
        if(!"POST".equalsIgnoreCase(request.getMethod())){
            return false;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        } else {
            return contentType.toLowerCase(Locale.ENGLISH).startsWith("multipart/");
        }
    }

}
