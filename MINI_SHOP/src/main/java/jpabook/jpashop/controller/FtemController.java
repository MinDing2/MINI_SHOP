package jpabook.jpashop.controller;


import jpabook.jpashop.domain.Ftem;
import jpabook.jpashop.domain.FtemRepository;
import jpabook.jpashop.domain.UploadFile;
import jpabook.jpashop.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FtemController {


    private final FtemRepository ftemRepository;

    private final FileStore fileStore;

    @GetMapping("/itens/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "iten-form";
    }

    @PostMapping("/itens/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        //데이터베이스에 저장
        Ftem Ftem = new Ftem();
        Ftem.setItemName(form.getItemName());
        Ftem.setAttachFile(attachFile);
        Ftem.setImageFiles(storeImageFiles);
        ftemRepository.save(Ftem);

        redirectAttributes.addAttribute("itemId", Ftem.getFd());

        return "redirect:/itens/{itemId}";
    }



    @GetMapping("/itens/{id}")
    public String items(@PathVariable Long id, Model model) {
        Ftem item = ftemRepository.findById(id);
        model.addAttribute("Ftem", item);
        return "iten-view";
    }



    @ResponseBody
    @GetMapping("/image/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws
            MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/attach/{FtemFd}")
    public  ResponseEntity<Resource> downloadAttach(@PathVariable Long FtemFd) throws MalformedURLException {
        Ftem ftem =ftemRepository.findById(FtemFd);
        String storeFileName = ftem.getAttachFile().getStoreFileName();
        String uploadFileName = ftem.getAttachFile().getUploadFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}", uploadFileName);

        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
