package jpabook.jpashop.domain;

import lombok.Data;

import java.util.List;

@Data
public class Ftem {

    private Long Fd;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;
}
