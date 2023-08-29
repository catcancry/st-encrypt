package vip.ylove.sdk.json;

/**
 * json序列方式
 */
public interface StAbstractJsonDcode {

    /**
     * bean to String
     * @param data data
     * @return String
     */
    String toJson(Object data) ;

    /**
     * String to Bean
     * @param data data
     * @param cls Class<T></>
     * @return T
     * @param <T>
     */
    <T> T toBean(String data,Class<T> cls);

    /**
     * String to Bean
     * @param data data
     * @param cls Class<T></>
     * @return T
     * @param <T>
     */
    <T> T toBean(byte[] data,Class<T> cls);

}
