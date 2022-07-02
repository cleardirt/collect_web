package com.example.demo.service;

import com.example.demo.vo.ResultVO;
import com.example.demo.vo.user.UserVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public ResultVO<UserVO> save(MultipartFile file);
    public ResultVO<UserVO> getFile(String phone, String password);
    public UserVO getUser(Integer uid);
}
