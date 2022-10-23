package ocrexcel.demo.fuxin.enums;

import lombok.Getter;

@Getter
public enum MergeEnum {
    COL(0,"同列判断"),
    ROW(1,"同行判断");

    MergeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;
}
