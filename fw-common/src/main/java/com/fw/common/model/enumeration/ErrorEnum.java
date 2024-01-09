package com.fw.common.model.enumeration;

import lombok.Getter;

public enum ErrorEnum {
    //auth 1-100
    CHECK_AUTHEN("14", "check.authen"),
    USER_NOT_ACCEPT("1", "user.not.accept"),
    REFRESH_TOKEN_REQUIRED("2", "refresh.token.required"),
    REFRESH_TOKEN_NOT_EXIST("3", "refresh.token.not.exist"),
    USER_NOT_FOUND("7", "username.not.found"),

    //common 1000-2000
    RECORD_NOT_EXIST("1000", "record.does.not.exist"), //bản ghi không tồn tại
    INVALID_FORMAT_DATE("1001", "invalid.format.date"), //Format date không hợp lệ

    //file 2000-3000
    ERROR_OCCURRED_DOWNLOAD("2000", "error.occurred.download"), //xảy ra lỗi khi tải xuống
    ERROR_OCCURRED_UPLOAD("2001", "error.occurred.upload"), //xảy ra lỗi khi tải lên
    NO_DOCUMENT_DATA("2002", "no.document.data"), //Không có thông tin tài liệu
    INVALID_FILE_UPLOAD("2003", "invalid.file.upload"), //Định dạng file không hợp lệ
    NON_RIGHT_CREATE_DOC("2004", "non.right.create.doc"), //Không có quyền tạo báo cáo
    NON_RIGHT_UPDATE_DOC("2005", "non.right.update.doc"), //Không có quyền cập nhật báo cáo
    NON_RIGHT_APPROVE_DOC("2006", "non.right.approve.doc"), //Không có quyền phê duyệt báo cáo
    NON_RIGHT_DELETE_DOC("2007", "non.right.delete.doc"), //Không có quyền xoá báo cáo
    NON_RIGHT_DOWNLOAD_DOC("2008", "non.right.download.doc"), //Không có quyền tải tệp báo cáo
    MISSING_COLUMN_ERROR("2009","missing.column.error"),//Lỗi thừa thiếu cột
    INVALID_DOC_PERIOD("2010","invalid.doc.period"),//Kỳ báo cáo không hợp lệ
    ENDED_DOC_PERIOD("2011","ended.doc.period"),//Kỳ báo cáo đã kết thúc
    INVALID_TMP_UPLOAD("2012", "invalid.tmp.upload"), //Định dạng template không hợp lệ
    NO_DATA_IMPORT("2013", "no.data.import"), //Dữ liệu import không hợp lệ. File import phải có ít nhất 1 bản ghi

    //config 3000-4000-
    START_DATE("3000", "effect.date.must.be.greater.than.now.date"),//Ngày hiệu lực phải lớn hơn hoặc bằng ngày hiện tại
    EFFECT_DATE("3001", "effect.end.date.must.be.greater.than.start.date"),//Ngày kết thúc hiệu lực phải lớn hơn hoặc bằng ngày bắt đầu hiệu lực
    NO_DATA("3002", "no.data.config"),//Không có thông tin cấu hình
    UNAVAILABLE_STATUS("3003", "unavailable.status.config"),//Trạng thái của cấu hình không hợp lệ
    INVALID_CONF_DATA("3004", "invalid.config.data"),//Thông tin của cấu hình không hợp lệ
    NON_RIGHT_CREATE_CONF("3005", "non.right.create.conf"),//Không có quyền tạo cấu hình
    NON_RIGHT_UPDATE_CONF("3006", "non.right.update.conf"), //Không có quyền cập nhật cấu hình
    NON_RIGHT_APPROVE_CONF("3007", "non.right.approve.conf"), //Không có quyền phê duyệt cấu hình
    NON_RIGHT_DELETE_CONF("3008", "non.right.delete.conf"), //Không có quyền xoá cấu hình
    NON_RIGHT_DOWNLOAD_CONF("3009", "non.right.download.conf"), //Không có quyền tải file cấu hình

    //report 4000-5000
    NO_DATA_REPORT("4000", "no.data.report")//Không có thông tin báo cáo
    ;

    @Getter
    private final String messageId;
    @Getter
    private final String code;

    private ErrorEnum(String code, String messageId) {
        this.messageId = messageId;
        this.code = code;
    }
}
