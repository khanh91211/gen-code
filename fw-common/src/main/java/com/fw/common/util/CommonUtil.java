package com.fw.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.logging.log4j.ThreadContext;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.TypeToken;
//import org.modelmapper.convention.MatchingStrategies;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class CommonUtil {
    private CommonUtil() {
    }

    private static String localIp;

//    private static ModelMapper mapper = null;

    public static String getLocalIp() {
        if (!isNullOrEmpty(localIp)) {
            return localIp;
        }
        try {
            int lowest = Integer.MAX_VALUE;
            InetAddress result = null;
            for (Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces(); nics.hasMoreElements(); ) {
                NetworkInterface ifc = nics.nextElement();
                if (ifc.isUp() && (ifc.getIndex() < lowest || result == null)) {
                    lowest = ifc.getIndex();
                    result = getFirstNonLoopbackIPv4Address(ifc);
                }
            }
            if (result != null) {
                return result.getHostAddress();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static InetAddress getFirstNonLoopbackIPv4Address(NetworkInterface ifc) {
        for (Enumeration<InetAddress> addrs = ifc.getInetAddresses(); addrs.hasMoreElements(); ) {
            InetAddress address = addrs.nextElement();
            if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                return address;
            }
        }
        return null;
    }

    public static String getRemoteAddress(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

//    public static <S, T> T toObject(S s, Class<T> targetClass) {
//        getMapper();
//        if (s == null) {
//            return null;
//        }
//        return mapper.map(s, targetClass);
//    }

//    public static <S, T> List<T> toListObject(List<S> list, Class<T> targetClass) {
//        if (list == null || list.isEmpty()) {
//            return Collections.emptyList();
//        }
//        List<T> result = new ArrayList<>();
//        for (S item : list) {
//            result.add(toObject(item, targetClass));
//        }
//
//        return result;
//    }

//    private static void getMapper() {
//        if (mapper == null) {
//            mapper = new ModelMapper();
//            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        }
//    }

    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String jsonString = "";
            try {
                jsonString = mapper.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                jsonString = "Can't build json from object";
            }
            return jsonString;
        }
    }

    public static <T> String listToJson(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("[");
        for (T item : list) {
            sb.append(beanToString(item)).append(",");
        }
        return sb.substring(0, sb.length() - 1) + "]";
    }

    public static <T> List<T> jsonToList(String json, Class<T> clazz) throws IOException {
        if (CommonUtil.isNullOrEmpty(json)) {
            return new ArrayList<>();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
        return mapper.readValue(json, listType);
    }

    @SuppressWarnings("unchecked")
    public static <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(str, clazz);
            } catch (IOException e) {
                return null;
            }
        }
    }

    public static Long safeToLong(Object obj) {
        return safeToLong(obj, null);
    }

    public static Long safeToLong(Object obj, Long defaultValue) {
        if (obj != null) {
            return Long.parseLong(obj.toString());
        }
        return defaultValue;
    }

    private static String safeToString(Object obj, String defaultValue) {
        if (obj != null && obj != "") {
            return obj.toString();
        }
        return defaultValue;
    }

    public static String safeToString(Object obj) {
        return safeToString(obj, null);
    }


    private static final Set<Class<?>> BASE_TYPES = new HashSet<>(
            Arrays.asList(String.class, Boolean.class, Character.class, Byte.class, Short.class, Integer.class,
                    Long.class, Float.class, Double.class, Void.class, Date.class, Timestamp.class));

    public static boolean isBaseType(Class<? extends Object> clazz) {
        return BASE_TYPES.contains(clazz);
    }

    public static boolean listIsEmptyOrNull(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertStringToObject(String str, TypeReference<T> typeReference) {
        if (isNullOrEmpty(str) || typeReference == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return typeReference.getType().equals(String.class) ? (T) str : mapper.readValue(str, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> convertJsonString(String json) {
        List<String> listNationality = new ArrayList<>();
        if (isNullOrEmpty(json)) {
            return listNationality;
        }
        String nationality = json;
        if (!CommonUtil.isNullOrEmpty(json)) {
            nationality = nationality.replace("]", "");
            nationality = nationality.replace("[", "");
            String[] list = nationality.split(",");

            for (String s : list) {
                if (!CommonUtil.isNullOrEmpty(s)) {
                    listNationality.add(s);
                }
            }
        }
        return listNationality;
    }

    public static boolean compareString(String s1, String s2) {
        if (isNullOrEmpty(s1)) {
            return isNullOrEmpty(s2);
        } else {
            return s1.equals(s2);
        }
    }

    public static String convertDateFormat(String date, String oldFormat, String newFormat) {
        if (isNullOrEmpty(date)) {
            return null;
        }
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(oldFormat))
                    .format(DateTimeFormatter.ofPattern(newFormat));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @content Get uuid into ThreadContext
     */
    public static String getClientMessageId() {
        return ThreadContext.get("uuid");
    }

//    public static <T> List<T> mapAsList(Iterable<?> fromList) {
//        getMapper();
//        return mapper.map(fromList, new TypeToken<List<T>>() {
//        }.getType());
//    }

    public static List<String> stringToList(String str) {
        if (str == null) {
            return new ArrayList<>();
        }
        if (str.charAt(str.length() - 1) == ']') {
            str = str.replace(str.substring(str.length() - 1), "");
        }
        if (str.charAt(0) == '[') {
            str = str.substring(1);
        }
        if (str.split(",").length == 0) {
            return Arrays.asList(str);
        }
        return Arrays.asList(str.split(","));
    }

    public static <T> String listToString(List<T> str) {
        StringBuilder responseBuilder = new StringBuilder();
        if (str == null || str.isEmpty()) {
            return null;
        }
        for (T item : str) {
            responseBuilder.append(item).append(",");
        }
        String response = responseBuilder.toString();
        if (isNullOrEmpty(response)) {
            return null;
        }
        return response.substring(0, (response.length() - 1));
    }

    public static Date convertStringToDateTime(String date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            return format.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertStringToDate(String date) {
        if (date == null) {
            return null;
        }
        if (date.length() <= 8) {
            return date;
        }
        date = date.replaceAll("-", "");
        date = date.replaceAll("/", "");
        date = date.replaceAll(":", "");
        date = date.replace(date.substring(8), "");
        return date;
    }

    public static String convertLanguage(String value) {
        if (value == null) {
            return null;
        }
        value = value.replaceAll("â|á|ạ|à|ã|ả|ấ|ẩ|ậ|ầ|ắ|ă|ẳ|ằ|ẵ|ặ|ẫ", "a");
        value = value.replaceAll("ê|é|ẽ|ẻ|ẹ|è|ễ|ệ|ế|ề|ể", "e");
        value = value.replaceAll("ư|ự|ữ|ử|ừ|ụ|ủ|ù|ú|ũ", "u");
        value = value.replaceAll("ý|ỷ|ỹ|ỵ|ỹ", "y");
        value = value.replaceAll("ỉ|ĩ|ị|ì|í", "i");
        if (value == null) {
            return null;
        }
        value = removeAccents(value);
        if (value == null) {
            return null;
        }
        value = value.replaceAll("Á|À|Ạ|Ả|Ã|Â|Ấ|Ẩ|Ầ|Ẫ|Ậ|Ă|Ẳ|Ắ|Ằ|Ẵ|Ặ", "A");
        value = value.replaceAll("Ê|Ế|Ề|Ể|Ệ|Ễ|Ẹ|È|Ẽ|Ẻ|É", "E");
        value = value.replaceAll("Ư|Ự|Ữ|Ử|Ừ|Ụ|Ủ|Ú|Ù|Ũ", "U");
        value = value.replaceAll("Ý|Ỷ|Ỹ|Ỵ|Ỳ", "Y");
        value = value.replaceAll("Ỉ|Ĩ|Ị|Ì|Í", "I");
        value = value.replaceAll("Đ", "D");
        value = value.replaceAll("ó|ỏ|ọ|õ|ò|ô|ố|ỗ|ồ|ộ|ổ|ở|ơ|ờ|ỡ|ớ|ợ", "o");
        value = value.replaceAll("!|@|#|$|&|~|`|<|>|_|,", " ");
        return value;
    }

    public static String removeAccents(String value) {
        if (value == null) {
            return null;
        }
        value = value.replace("\u0110", "D").replace("\u0111", "d");
        String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

//    public static Long convertDateToNumber(Date date) {
//        if (date != null) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.FORMAT_YYYYMMDD);
//        return Long.parseLong(dateFormat.format(date));
//        }
//        return null;
//    }

    public static String convertPeriod(Object period) {
        String inputString = String.valueOf(period);
        if (inputString.length() != 6) {
            return null;
        }

        // Tách phần MM và YYYY từ chuỗi đầu vào
        String mm = inputString.substring(4);
        String yyyy = inputString.substring(0, 4);

        // Kết hợp thành chuỗi dạng "MM/YYYY"
        return mm + "/" + yyyy;
    }

    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null; // Trả về null nếu không tìm thấy giá trị trong map
    }

    public static boolean isFormatNumber(String input) {
        try {
            Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return false;
        }
        int indexOfDot = input.indexOf(".");
        if (indexOfDot != -1) {
            String valueAfterDot = input.substring(indexOfDot + 1);
            if (valueAfterDot.length() > 2) {
                return false;
            }
        }
        return true;
    }
}
